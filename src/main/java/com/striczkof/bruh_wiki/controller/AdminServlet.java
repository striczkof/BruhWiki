package com.striczkof.bruh_wiki.controller;

import com.striczkof.bruh_wiki.dao.DatabaseAccess;
import com.striczkof.bruh_wiki.dao.PS;
import com.striczkof.bruh_wiki.model.Article;
import com.striczkof.bruh_wiki.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

@WebServlet(name = "AdminServlet", value = "/admin-servlet")
public class AdminServlet extends HttpServlet {
    private Logger log;
    private DatabaseAccess dao;

    /**
     * Initialising servlet with database connection and prepared statements.
     */
    public void init() {
        log = Logger.getLogger(AdminServlet.class.getName());
        log.info( getServletName() + " initialising...");
        // SQL statements for the prepared statements, this is delineated by a semicolon
        PS[] ps = new PS[]{
                PS.USERS_GET_ADMIN_ONE,
                PS.USERS_GET_COUNT,
                PS.ART_GET_COUNT,
                PS.CAT_GET_COUNT,
                PS.USERS_GET_ALL,
                PS.ART_ADMIN_GET_ALL,
                PS.ART_ADMIN_GET_COUNT,
                PS.CAT_GET_ALL,
                PS.USERS_GET_ONE,
                PS.ART_GET_ONE,
                PS.CAT_GET_ONE,
                PS.USERS_SET_UNAME_ONE,
                PS.USERS_SET_NAME_ONE,
                PS.USERS_SET_UNAME_NAME_ONE,
                PS.USERS_ADMIN_SET_ADMIN_ONE,
                PS.USERS_ADMIN_SET_UNAME_ADMIN_ONE,
                PS.USERS_ADMIN_SET_NAME_ADMIN_ONE,
                PS.USERS_ADMIN_SET_UNAME_NAME_ADMIN_ONE,
                PS.USERS_ADMIN_DEL_ONE,
                PS.ART_ADMIN_SET_CAT_ONE,
                PS.ART_ADMIN_SET_TITLE_ONE,
                PS.ART_ADMIN_SET_CONTENT_ONE,
                PS.ART_ADMIN_SET_HIDDEN_ONE,
                PS.ART_ADMIN_SET_CAT_TITLE_ONE,
                PS.ART_ADMIN_SET_CAT_CONTENT_ONE,
                PS.ART_ADMIN_SET_CAT_HIDDEN_ONE,
                PS.ART_ADMIN_SET_TITLE_CONTENT_ONE,
                PS.ART_ADMIN_SET_TITLE_HIDDEN_ONE,
                PS.ART_ADMIN_SET_CONTENT_HIDDEN_ONE,
                PS.ART_ADMIN_SET_CAT_TITLE_CONTENT_ONE,
                PS.ART_ADMIN_SET_CAT_TITLE_HIDDEN_ONE,
                PS.ART_ADMIN_SET_CAT_CONTENT_HIDDEN_ONE,
                PS.ART_ADMIN_SET_TITLE_CONTENT_HIDDEN_ONE,
                PS.ART_ADMIN_SET_CAT_TITLE_CONTENT_HIDDEN_ONE,
                PS.ART_ADMIN_DEL_ONE,
                PS.CAT_ADMIN_SET_NAME_ONE,
                PS.CAT_ADMIN_DEL_ONE
        };
        // FUCKING FINALLY

        dao = new DatabaseAccess(this, ps);
        log.info( getServletName() + " initialised.");
    }

    /**
     * Closing the database upon servlet destruction.
     */
    public void destroy() {
        dao.close();
        log.info(getServletName() + " destroyed.");
        log = null;
    }

    /**
     * Make sure that the user is logged in and is an admin.
     * Quick SQL just in case they got demoted live lol
     * @param session The session to check. Should be "request.getSession(false)" to prevent a new session from being created.
     * @return True if the user is logged in and is an admin, false otherwise.
     */
    private boolean authenticateAdmin(HttpSession session) {
        if (session != null) {
            if (session.getAttribute("user") != null) {
                // It will be assumed that the user attribute is actually a user object, if not, fuck me
                User user = (User) session.getAttribute("user");
                if (user.getAdmin()) {
                    try {
                        PreparedStatement ps = dao.getPreparedStatement(PS.USERS_GET_ADMIN_ONE);
                        ps.setInt(1, user.getId());
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            return rs.getBoolean("admin");
                        } else {
                            // User not found, boi got deleted live
                            return false;
                        }
                    } catch (SQLException e) {
                        System.out.println("The servlet " + getServletName() + " has had a bruh moment.");
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Gets user from the database.
     */
    private User getUser(int id) {
        return null;
    }

    private Article getArticle(int id, int truncate) {
        try {
            PreparedStatement ps;
            if (truncate <= 0) {
                ps = dao.getPreparedStatement(PS.ART_ADMIN_GET_ONE);
                ps.setInt(1, id);
            } else {
                ps = dao.getPreparedStatement(PS.ART_ADMIN_GET_ONE_TRUNC);
                ps.setInt(1, truncate);
                ps.setInt(2, truncate);
                ps.setInt(3, id);
            }
            ResultSet out = ps.executeQuery();
            if (out.next()) {
                return new Article(out.getInt("id"), out.getInt("category_id"), out.getString("category_name"), out.getLong("made"), out.getLong("lastEdited"), out.getString("title"), out.getString("content"), out.getBoolean("hidden"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    private Article[] getArticles(int truncate, int limit, int starts) {
        try {
            ResultSet out;
            PreparedStatement useStatement;
            // Means all
            if (limit == 0 && starts == 0) {
                // OK This should be invalid, wait no, this is for GET ALL
                if (truncate > 0) {
                    useStatement = dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_TRUNC);
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                } else {
                    useStatement = dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL);
                }
            } else {
                if (limit <= 0 || starts <= 0) {
                    log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                    return null;
                } else {
                    int limitParamNum = 1;
                    int startsParamNum = 2;
                    if (truncate > 0) {
                        useStatement = dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_TRUNC_BY_EDITED_RANGE);
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        limitParamNum = 3;
                        startsParamNum = 4;
                    } else {
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_BY_EDITED_RANGE);
                    }
                    useStatement.setInt(limitParamNum, limit);
                    useStatement.setInt(startsParamNum, starts - 1);
                }
            }
            out = useStatement.executeQuery();
            // Bruh why do I have to use list
            ArrayList<Article> articles = new ArrayList<>();
            while (out.next()) {
                articles.add(new Article(out.getInt("id"), out.getInt("category_id"), out.getString("category_name"), out.getLong("made"), out.getLong("lastEdited"), out.getString("title"), out.getString("content"), out.getBoolean("hidden")));
            }
            Article[] articlesArray = new Article[articles.size()];
            articlesArray = articles.toArray(articlesArray);
            return articlesArray;
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates article's title, content, and/or category.
     * @param article article object to update
     * @param catId new category id, set to -1 if not changing
     * @param title new title, set to null if not changing
     * @param content new content, set to null if not changing
     * @return the updated article object, null if something went wrong
     */
    private Article updateArticle(Article article, int catId, String title, String content) {
        try {
            PreparedStatement ps;
            // Choosing what to change
            if (catId != -1) {
                if (title != null) {
                    // Changing category and title
                    if (content != null) {
                        // Changing category, title, and content
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_TITLE_CONTENT_ONE);
                        ps.setInt(1, catId);
                        ps.setString(2, title);
                        ps.setString(3, content);
                        ps.setInt(4, article.getId());
                    } else {
                        // Changing category and title
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_TITLE_ONE);
                        ps.setInt(1, catId);
                        ps.setString(2, title);
                        ps.setInt(3, article.getId());
                    }
                } else {
                    // Changing category
                    if (content != null) {
                        // Changing category and content
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_CONTENT_ONE);
                        ps.setInt(1, catId);
                        ps.setString(2, content);
                        ps.setInt(3, article.getId());
                    } else {
                        // Changing category
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_ONE);
                        ps.setInt(1, catId);
                        ps.setInt(2, article.getId());
                    }
                }
            } else {
                // Not changing category
                if (title != null) {
                    // Changing title
                    if (content != null) {
                        // Changing title and content
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_TITLE_CONTENT_ONE);
                        ps.setString(1, title);
                        ps.setString(2, content);
                        ps.setInt(3, article.getId());
                    } else {
                        // Changing title
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_TITLE_ONE);
                        ps.setString(1, title);
                        ps.setInt(2, article.getId());
                    }
                } else {
                    // Not changing title
                    if (content != null) {
                        // Changing content
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CONTENT_ONE);
                        ps.setString(1, content);
                        ps.setInt(2, article.getId());
                    } else {
                        // Nothing to change
                        return article;
                    }
                }
            }
            // Executing the query
            int out = ps.executeUpdate();
            if (out == 1) {
                // Article updated
                // Returning the updated article, far easier to just get it from the database again
                return getArticle(article.getId(), 0);
            } else {
                // Article not updated
                return null;
            }


        } catch (SQLException e) {
            System.out.println("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates article's title, content, category, and/or hidden.
     * I literally just copied this, problem?
     * @param article article object to update
     * @param catId new category id, set to -1 if not changing
     * @param title new title, set to null if not changing
     * @param content new content, set to null if not changing
     * @param hidden new hidden value, try changing it to null lol
     * @return the updated article object, null if something went wrong
     */
    private Article updateArticle(Article article, int catId, String title, String content, boolean hidden) {
        try {
            PreparedStatement ps;
            // Choosing what to change, must have hidden
            if (catId != 1) {
                // Changing category and hidden
                if (title != null) {
                    // Changing category, title, and hidden
                    if (content != null) {
                        // Changing category, title, content, and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_TITLE_CONTENT_HIDDEN_ONE);
                        ps.setInt(1, catId);
                        ps.setString(2, title);
                        ps.setString(3, content);
                        ps.setBoolean(4, hidden);
                        ps.setInt(5, article.getId());
                    } else {
                        // Changing category, title, and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_TITLE_HIDDEN_ONE);
                        ps.setInt(1, catId);
                        ps.setString(2, title);
                        ps.setBoolean(3, hidden);
                        ps.setInt(4, article.getId());
                    }
                } else {
                    // Changing category and hidden
                    if (content != null) {
                        // Changing category, content, and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_CONTENT_HIDDEN_ONE);
                        ps.setInt(1, catId);
                        ps.setString(2, content);
                        ps.setBoolean(3, hidden);
                        ps.setInt(4, article.getId());
                    } else {
                        // Changing category and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CAT_HIDDEN_ONE);
                        ps.setInt(1, catId);
                        ps.setBoolean(2, hidden);
                        ps.setInt(3, article.getId());
                    }
                }
            } else {
                // Not changing category
                if (title != null) {
                    // Changing title and hidden
                    if (content != null) {
                        // Changing title, content, and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_TITLE_CONTENT_HIDDEN_ONE);
                        ps.setString(1, title);
                        ps.setString(2, content);
                        ps.setBoolean(3, hidden);
                        ps.setInt(4, article.getId());
                    } else {
                        // Changing title and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_TITLE_HIDDEN_ONE);
                        ps.setString(1, title);
                        ps.setBoolean(2, hidden);
                        ps.setInt(3, article.getId());
                    }
                } else {
                    // Not changing title
                    if (content != null) {
                        // Changing content and hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_CONTENT_HIDDEN_ONE);
                        ps.setString(1, content);
                        ps.setBoolean(2, hidden);
                        ps.setInt(3, article.getId());
                    } else {
                        // Changing hidden
                        ps = dao.getPreparedStatement(PS.ART_ADMIN_SET_HIDDEN_ONE);
                        ps.setBoolean(1, hidden);
                        ps.setInt(2, article.getId());
                    }
                }
            }
            // Executing the query
            int out = ps.executeUpdate();
            if (out == 1) {
                // Article updated
                // Returning the updated article, far easier to just get it from the database again
                return getArticle(article.getId(), 0);
            } else {
                // Article not updated
                return null;
            }
        } catch (SQLException e) {
            System.out.println("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * doGet method for the servlet. Handles GET requests.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Before doing funny stuff, check if user is logged in and admin first, if not, yeet the guy into the index!
        if(authenticateAdmin(request.getSession(false))) {
            // Nothing to do here for now
            String referer = request.getHeader("referer");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            if (referer != null) {
                // Nothing to do here for now
                response.sendRedirect(referer);
            } else {
                // Nothing to do here for now
                response.sendRedirect("admin.jsp");
            }
        } else {
            // Not admin or not logged in, idc, back to the lobby
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("index.jsp");
        }
    }

    /**
     * doPost method for the servlet. Handles POST requests.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Before doing funny stuff, check if user is logged in and admin first, if not, yeet the guy into the index!
        if(authenticateAdmin(request.getSession(false))) {
            // TODO: doPost admin stuff, where the real fun begins.
            // Almost all admin stuff has to be done in/from admin.jsp
            String referer = request.getHeader("referer");
            if (referer != null) {
                // Referer exists, check if from admin.jsp
                if (referer.contains("admin.jsp")) {
                    // Referer is from admin.jsp, so we can do some stuff
                    return;
                } else {
                    // Might be able to do some stuff outside admin.jsp later, but for now, yeet the guy into the admin
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendRedirect("admin.jsp");
                }
            } else {
                // No referer, but you are an admin, so you will go to admin.jsp
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendRedirect("admin.jsp");
            }
        } else {
            // Not admin or not logged in, idc, back to the lobby
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("index.jsp");
        }
    }
}
