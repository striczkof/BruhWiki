package com.striczkof.bruh_wiki.controller;

import com.striczkof.bruh_wiki.dao.DatabaseAccess;
import com.striczkof.bruh_wiki.dao.PS;
import com.striczkof.bruh_wiki.model.Article;
import com.striczkof.bruh_wiki.model.Category;
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
import java.util.Arrays;
import java.util.Objects;
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
        // THAT'S A BIG BOI
        PS[] ps = new PS[]{
                PS.USERS_GET_ADMIN_ONE,
                PS.USERS_GET_COUNT,
                PS.ART_GET_COUNT,
                PS.ART_GET_COUNT_BY_CAT,
                PS.CAT_GET_COUNT,
                PS.USERS_GET_ALL,
                PS.CAT_GET_ALL,
                PS.USERS_GET_ONE,
                PS.CAT_GET_ONE,
                PS.USERS_SET_UNAME_ONE,
                PS.USERS_SET_NAME_ONE,
                PS.USERS_SET_UNAME_NAME_ONE,
                PS.USERS_ADMIN_SET_ADMIN_ONE,
                PS.USERS_ADMIN_SET_UNAME_ADMIN_ONE,
                PS.USERS_ADMIN_SET_NAME_ADMIN_ONE,
                PS.USERS_ADMIN_SET_UNAME_NAME_ADMIN_ONE,
                PS.USERS_ADMIN_DEL_ONE,
                PS.ART_ADMIN_GET_COUNT,
                PS.ART_ADMIN_GET_COUNT_BY_CAT,
                PS.ART_ADMIN_GET_COUNT_HIDDEN,
                PS.ART_ADMIN_GET_COUNT_HIDDEN_BY_CAT,

                PS.ART_GET_ALL,
                PS.ART_GET_ALL_TRUNC,
                PS.ART_GET_ALL_BY_CAT,
                PS.ART_GET_ALL_TRUNC_BY_CAT,
                // PS.ART_GET_ONE, Not needed
                // PS.ART_GET_ONE_TRUNC,
                PS.ART_GET_SOME_BY_EDITED_RANGE,
                PS.ART_GET_SOME_TRUNC_BY_EDITED_RANGE,
                PS.ART_GET_SOME_BY_CAT_EDITED_RANGE,
                PS.ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE,
                PS.ART_GET_SOME_BY_MATCHING,
                PS.ART_GET_COUNT_BY_MATCHING,

                PS.ART_ADMIN_MAKE_ONE,
                PS.ART_ADMIN_GET_ALL,
                PS.ART_ADMIN_GET_ALL_TRUNC,
                PS.ART_ADMIN_GET_ALL_BY_CAT,
                PS.ART_ADMIN_GET_ALL_TRUNC_BY_CAT,
                PS.ART_ADMIN_GET_ONE, // This needed
                PS.ART_ADMIN_GET_ONE_TRUNC,
                PS.ART_ADMIN_GET_SOME_BY_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_TRUNC_BY_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_BY_CAT_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_BY_MATCHING,
                PS.ART_ADMIN_GET_COUNT_BY_MATCHING,

                PS.ART_ADMIN_GET_ALL_HIDDEN,
                PS.ART_ADMIN_GET_ALL_HIDDEN_TRUNC,
                PS.ART_ADMIN_GET_ALL_HIDDEN_BY_CAT,
                PS.ART_ADMIN_GET_ALL_HIDDEN_TRUNC_BY_CAT,
                // PS.ART_ADMIN_GET_ONE_HIDDEN, Not needed lol
                // PS.ART_ADMIN_GET_ONE_HIDDEN_TRUNC,
                PS.ART_ADMIN_GET_SOME_HIDDEN_BY_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_HIDDEN_BY_CAT_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_CAT_EDITED_RANGE,
                PS.ART_ADMIN_GET_SOME_HIDDEN_BY_MATCHING,
                PS.ART_ADMIN_GET_COUNT_HIDDEN_BY_MATCHING,

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
                PS.CAT_ADMIN_DEL_ONE,
                PS.CAT_ADMIN_MAKE_ONE,
                PS.ADMIN_GET_LAST_ID
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
     * So they can't do any further damage
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
                        log.severe("The servlet " + getServletName() + " has had a bruh moment.");
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
     * Count the number of articles in the database.
     * @param showBoth if true, counts both hidden and non-hidden articles. If false, counts only non-hidden articles.
     * @return article count, or -1 if an error occurred.
     */
    private int countArticles(boolean showBoth) {
        return countArticles(false, showBoth);
    }

    /**
     * Count the number of articles in the database.
     * @param hidden irrelevant if showBoth is true. If false, counts only non-hidden articles. If true, counts only hidden articles.
     * @param showBoth if true, counts both hidden and non-hidden articles. If false, counts only either articles.
     * @return article count, or -1 if an error occurred.
     */
    private int countArticles(boolean hidden, boolean showBoth) {
        try {
            PreparedStatement ps;
            if (showBoth) {
                ps = dao.getPreparedStatement(PS.ART_GET_COUNT);
            } else {
                if (hidden) {
                    ps = dao.getPreparedStatement(PS.ART_ADMIN_GET_COUNT_HIDDEN);
                } else {
                    ps = dao.getPreparedStatement(PS.ART_GET_COUNT);
                }
            }
            ResultSet out = ps.executeQuery();
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Count the number of articles in the category.
     * @param catId the category to count articles in.
     * @param showBoth if true, counts both hidden and non-hidden articles. If false, counts only non-hidden articles.
     * @return article count, or -1 if an error occurred.
     */
    private int countArticlesByCat(int catId, boolean showBoth) {
        return countArticlesByCat(catId, false, showBoth);
    }

    /**
     * Count the number of articles in the category.
     * @param catId the category to count articles in.
     * @param hidden irrelevant if showBoth is true. If false, counts only non-hidden articles. If true, counts only hidden articles.
     * @param showBoth if true, counts both hidden and non-hidden articles. If false, counts only either articles.
     * @return article count, or -1 if an error occurred.
     */
    private int countArticlesByCat(int catId, boolean hidden, boolean showBoth) {
        try {
            PreparedStatement ps;
            if (showBoth) {
                ps = dao.getPreparedStatement(PS.ART_ADMIN_GET_COUNT_BY_CAT);
            } else {
                if (hidden) {
                    ps = dao.getPreparedStatement(PS.ART_ADMIN_GET_COUNT_HIDDEN_BY_CAT);
                } else {
                    ps = dao.getPreparedStatement(PS.ART_GET_COUNT_BY_CAT);
                }
            }
            ps.setInt(1, catId);
            ResultSet out = ps.executeQuery();
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    private boolean createArticle(String title, int catId, String content, boolean hidden) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.ART_ADMIN_MAKE_ONE);
            ps.setInt(1, catId);
            ps.setString(2, title);
            ps.setString(3, content);
            ps.setBoolean(4, hidden);
            int out = ps.executeUpdate();
            if (out == 1) {
                // Article created
                return true;
            } else {
                // Article not created
                return false;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQL moment.");
            e.printStackTrace();
            return false;
        }
    }

    private int countCategories() {
        try (ResultSet out = dao.getPreparedStatement(PS.CAT_GET_COUNT).executeQuery()) {
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    private int countUsers() {
        try (ResultSet out = dao.getPreparedStatement(PS.USERS_GET_COUNT).executeQuery()) {
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
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

    /**
     * Have to make this because getArticles is becoming as big as yo mama
     * Gets appropriate prepared statement based on the values below.
     * that's it lol
     * @param truncate true if the article content should be truncated.
     * @param limit true if the articles pulled should be limited.
     * @param hidden irrelevant if showBoth is true. If false, counts only non-hidden articles. If true, counts only hidden articles.
     * @param showBoth if true, gets both hidden and non-hidden articles. If false, gets only either articles.
     * @return appropriate prepared statement.
     */
    private PreparedStatement getArticlesPS(boolean truncate, boolean limit, boolean hidden, boolean showBoth, boolean byCat) {
        if (byCat) {
            if (limit) {
                if (truncate) {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_CAT_EDITED_RANGE);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE);
                        }
                    }
                } else {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_BY_CAT_EDITED_RANGE);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_HIDDEN_BY_CAT_EDITED_RANGE);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_SOME_BY_CAT_EDITED_RANGE);
                        }
                    }
                }
            } else {
                if (truncate) {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_TRUNC_BY_CAT);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_HIDDEN_TRUNC_BY_CAT);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_ALL_TRUNC_BY_CAT);
                        }
                    }
                } else {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_BY_CAT);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_HIDDEN_BY_CAT);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_ALL_BY_CAT);
                        }
                    }
                }
            }
        } else {
            if (limit) {
                if (truncate) {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_TRUNC_BY_EDITED_RANGE);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_HIDDEN_TRUNC_BY_EDITED_RANGE);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_SOME_TRUNC_BY_EDITED_RANGE);
                        }
                    }
                } else {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_BY_EDITED_RANGE);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_SOME_HIDDEN_BY_EDITED_RANGE);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_SOME_BY_EDITED_RANGE);
                        }
                    }
                }
            } else {
                if (truncate) {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_TRUNC);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_HIDDEN_TRUNC);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_ALL_TRUNC);
                        }
                    }
                } else {
                    if (showBoth) {
                        return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL);
                    } else {
                        if (hidden) {
                            return dao.getPreparedStatement(PS.ART_ADMIN_GET_ALL_HIDDEN);
                        } else {
                            return dao.getPreparedStatement(PS.ART_GET_ALL);
                        }
                    }
                }
            }
        }
    }

    /**
     * Get all articles.
     * @param truncate set to 0 to not truncate, or a positive integer to truncate.
     * @param limit the maximum number of articles to get. Set to 0 to get all.
     * @param starts the number of articles to skip. Set to 0 to not skip any.
     * @param showBoth if true, gets both hidden and non-hidden articles. If false, gets only non-hidden articles..
     * @return articles array, empty if none found, or null if an error occurred.
     */
    private Article[] getArticles(int truncate, int limit, int starts, boolean showBoth) {
        return getArticles(truncate, limit, starts, false, showBoth);
    }

    /**
     * Get all articles.
     * @param truncate set to 0 to not truncate, or a positive integer to truncate.
     * @param limit the maximum number of articles to get. Set to 0 to get all.
     * @param starts the number of articles to skip. Set to 0 to not skip any.
     * @param hidden irrelevant if showBoth is true. If false, counts only non-hidden articles. If true, counts only hidden articles.
     * @param showBoth if true, gets both hidden and non-hidden articles. If false, gets only either articles.
     * @return articles array, empty if none found, or null if an error occurred.
     */
    private Article[] getArticles(int truncate, int limit, int starts, boolean hidden, boolean showBoth) {
        try {
            ResultSet out;
            PreparedStatement useStatement;
            // Means all
            if (limit == 0 && starts == 0) {
                // No limit
                // OK This should be invalid, wait no, this is for GET ALL
                if (truncate > 0) {
                    // Yes truncate
                    useStatement = getArticlesPS(true, false, hidden, showBoth, false);
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                } else {
                    // No truncate
                    useStatement = getArticlesPS(false, false, hidden, showBoth, false);
                }
            } else {
                if (limit <= 0 || starts <= 0) {
                    log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                    return null;
                } else {
                    // Yes limit
                    int limitParamNum = 1;
                    int startsParamNum = 2;
                    if (truncate > 0) {
                        // Yes truncate
                        useStatement = getArticlesPS(true, true, hidden, showBoth, false);
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        limitParamNum = 3;
                        startsParamNum = 4;
                    } else {
                        // No truncate
                        useStatement = getArticlesPS(true, true, hidden, showBoth, false);
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
     * Get all articles by category.
     * Yes, I literally just copied the other method and changed the SQL statement, problem?
     * @param catId the category ID to get articles from.
     * @param truncate set to 0 to not truncate, or a positive integer to truncate.
     * @param limit the maximum number of articles to get. Set to 0 to get all.
     * @param starts the number of articles to skip. Set to 0 to not skip any.
     * @param showBoth if true, gets both hidden and non-hidden articles. If false, gets only non-hidden articles..
     * @return articles array, empty if none found, or null if an error occurred.
     */
    private Article[] getArticlesByCat(int catId, int truncate, int limit, int starts, boolean showBoth) {
        return getArticlesByCat(catId ,truncate, limit, starts, false, showBoth);
    }

    /**
     * Get all articles by category.
     * Yes, I literally just copied the other method and changed the SQL statement, problem?
     * @param catId the category ID to get articles from.
     * @param truncate set to 0 to not truncate, or a positive integer to truncate.
     * @param limit the maximum number of articles to get. Set to 0 to get all.
     * @param starts the number of articles to skip. Set to 0 to not skip any.
     * @param hidden irrelevant if showBoth is true. If false, counts only non-hidden articles. If true, counts only hidden articles.
     * @param showBoth if true, gets both hidden and non-hidden articles. If false, gets only either articles.
     * @return articles array, empty if none found, or null if an error occurred.
     */
    private Article[] getArticlesByCat(int catId, int truncate, int limit, int starts, boolean hidden, boolean showBoth) {
        try {
            PreparedStatement useStatement;
            if (limit == 0 && starts == 0) {
                // OK This should be invalid, wait no, this is for GET ALL
                if (truncate > 0) {
                    useStatement = getArticlesPS(true, true, hidden, showBoth, true);
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                    useStatement.setInt(3, catId);
                } else {
                    useStatement = getArticlesPS(true, true, hidden, showBoth, true);
                    useStatement.setInt(1, catId);
                }
            } else {
                if (limit <= 0 || starts <= 0) {
                    log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                    return null;
                } else {
                    int catIdParamNum = 1;
                    int limitParamNum = 2;
                    int startsParamNum = 3;
                    if (truncate > 0) {
                        useStatement = getArticlesPS(true, true, hidden, showBoth, true);
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        catIdParamNum = 3;
                        limitParamNum = 4;
                        startsParamNum = 5;
                    } else {
                        useStatement = getArticlesPS(false, true, hidden, showBoth, true);
                    }
                    useStatement.setInt(catIdParamNum, catId);
                    useStatement.setInt(limitParamNum, limit);
                    useStatement.setInt(startsParamNum, starts - 1);
                }
            }
            ResultSet out = useStatement.executeQuery();
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
            log.severe("The servlet " + getServletName() + " has had a bruh moment.");
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
            log.severe("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    private Category getCategory(int id) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.CAT_GET_ONE);
            ps.setInt(1, id);
            ResultSet out = ps.executeQuery();
            if (out.next()) {
                return new Category(out.getInt("id"), out.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    private Category[] getCategories() {
        // No fancy shit at this point, just get categories and leave
        try (ResultSet out = dao.getPreparedStatement(PS.CAT_GET_ALL).executeQuery()) {
            ArrayList<Category> cats = new ArrayList<>();
            while (out.next()) {
                cats.add(new Category(out.getInt("id"), out.getString("name")));
            }
            Category[] catsArray = new Category[cats.size()];
            catsArray = cats.toArray(catsArray);
            return catsArray;
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Good thing only name needs to be changed. Right?
     * Right??
     * @param id category to change
     * @param name new name
     * @return category with changed name if success, no change if not, null if SQL error
     */
    private boolean updateCategory(int id, String name) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.CAT_ADMIN_SET_NAME_ONE);
            ps.setString(1, name);
            ps.setInt(2, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQL moment.");
            e.printStackTrace();
            return false;
        }
    }

    private Category createCategory(String name) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.CAT_ADMIN_MAKE_ONE);
            ps.setString(1, name);
            int out = ps.executeUpdate();
            if (out == 1) {
                // Category created
                // Getting the ID of the new category
                ps = dao.getPreparedStatement(PS.ADMIN_GET_LAST_ID);
                ResultSet out2 = ps.executeQuery();
                if (out2.next()) {
                    return new Category(out2.getInt("id"), name);
                } else {
                    // last_insert_id not found???
                    log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                    return null;
                }
            } else {
                // Category not created
                return null;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQL moment.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * no point getting the object lol
     */
    private boolean deleteCategory(int id) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.CAT_ADMIN_DEL_ONE);
            ps.setInt(1, id);
            int out = ps.executeUpdate();
            // Category deleted or not
            return out == 1;
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQL moment.");
            e.printStackTrace();
            return false;
        }
    }

    private User[] getUsers() {
        // No fancy shit at this point, just get categories and leave
        try (ResultSet out = dao.getPreparedStatement(PS.USERS_GET_ALL).executeQuery()) {
            ArrayList<User> usersList = new ArrayList<>();
            while (out.next()) {
                // Not getting salt, irrelevant
                usersList.add(new User(out.getInt("id"), out.getString("username"), null, out.getString("name"), out.getBoolean("admin"), out.getLong("created"), out.getLong("last_login")));
            }
            User[] users = new User[usersList.size()];
            users = usersList.toArray(users);
            return users;
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete user
     * @param id user to be deleted
     * @return true if success, false if not, also false if SQL error
     */
    private boolean deleteUser(int id) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.USERS_ADMIN_DEL_ONE);
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            // Bruh
            log.severe("Servlet " + getServletName() + " has hit a SQL error");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Gets rid of existing parameters in the URL and adds the new ones, if any.
     * Because IDEA is crying about 'multiple occurrences'
     * @param url URL to modify
     * @param parameters parameters to add, format: 'key1=value1&key2=value2'
     * @return modified URL
     */
    private String fixURL(String url, String parameters) {
        int index = url.indexOf('?');
        if (index == -1) {
            // No parameters, just add the parameters
            return url + "?" + parameters;
        } else {
            // Parameters exist, remove them and add the new ones
            return url.substring(0, index) + "?" + parameters;
        }
    }

    /**
     * doGet method for the servlet. Handles GET requests.
     * This is supposed to be called in the JSP through <jsp:include page"/admin-servlet" />.
     * So this won't redirect but instead the variables are included in the request parameters.
     * result is now returned in numbers:
     * 0 = success
     * 1 = target not found
     * 2 = option not found
     * 3 = parsing error
     * 4 = deletion failed
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Before doing funny stuff, check if user is logged in and admin first, if not, yeet the guy into the index!
        if(authenticateAdmin(request.getSession(false))) {
            if (request.getParameter("show") != null) {
                // Admin logged in, has selected something to show, lets go!
                String adminChange = request.getParameter("show");
                // num, page, truncate kinda mandatory at this point
                int num, page, truncate; // Number of items to show and current page
                try {
                    num = Integer.parseInt(request.getParameter("num"));
                    page = Integer.parseInt(request.getParameter("page"));
                    truncate = Integer.parseInt(request.getParameter("truncate"));
                } catch (NumberFormatException e) {
                    // not a number
                    request.setAttribute("result", 3);
                    return;
                }
                switch (adminChange) {
                    case "articles":
                        // Check if id is specified
                        if (request.getParameter("id") != null) {
                            // Get article by id
                            switch (Objects.requireNonNullElse(request.getParameter("does"), "woah")) {
                                case "del":
                                    break;
                                case "hide":
                                    break;
                                case "unhide":
                                    break;
                                case "edit":
                                    break;
                                case "new":
                                    break;
                                default:
                                    // Invalid option
                                    request.setAttribute("result", 2);
                                    break;
                            }
                        } else {
                            // No id specified, show all articles
                            if (request.getParameter("hidden") != null) {
                                boolean hidden = Boolean.parseBoolean(request.getParameter("hidden"));
                                if (hidden) {
                                    int resultCount = countArticles(true, false);
                                    request.setAttribute("maxPage", Math.floorDiv(resultCount, num) + 1);
                                    request.setAttribute("articleCount", resultCount);
                                    request.setAttribute("hiddenArticleCount", countArticles(true, false));
                                    request.setAttribute("articles", getArticles(truncate, num, (page * num) - (num - 1), true, false));
                                    request.setAttribute("result", 0);
                                } else {

                                }
                            } else {
                                int resultCount = countArticles(true);
                                request.setAttribute("maxPage", Math.floorDiv(resultCount, num) + 1);
                                request.setAttribute("articleCount", resultCount);
                                request.setAttribute("hiddenArticleCount", countArticles(true, false));
                                request.setAttribute("articles", getArticles(truncate, num, (page * num) - (num - 1), true));
                                request.setAttribute("result", 0);
                            }
                            return;
                        }
                    case "categories":
                        if (request.getParameter("id") != null) {
                            // Get category by id
                            int id = Integer.parseInt(request.getParameter("id"));
                            Category category = getCategory(id);
                            if (category != null) {
                                // Category found, return it
                                request.setAttribute("category", category);
                                return;
                            } else {
                                // Category not found, back to admin.jsp
                                response.sendRedirect("admin.jsp");
                                return;
                            }
                        } else {
                            // No id specified, show all categories
                            try {
                                // Ok fuck it, 50 gorillion SQL queries in one call
                                // 1. Get all cats, 2. Count cats, 3. Split them in pages, 4. Fetch article count, 5, Get most recent article, 6. ??? 6, Profit
                                // Does is already assigned, should be
                                if (request.getParameter("does").equals("del")) {
                                    // Cat id should be a real valid id
                                    int id = Integer.parseInt(request.getParameter("catId"));
                                    if (id == 0) {
                                        request.setAttribute("result", 4);
                                    } else {
                                        deleteCategory(id);
                                    }
                                } else if (request.getParameter("does").equals("new")) {
                                    // New category default
                                    Category cat = createCategory("New Category");
                                    if (Objects.nonNull(cat)) request.setAttribute("catId", cat.getId());
                                }
                                Category[] cats = getCategories();
                                if (cats != null) {
                                    int catCount = countCategories();
                                    // Do the wucky fucky calculation stuff
                                    int maxPage = (catCount / num) + 1;
                                    int limit = page * num;
                                    if (limit > catCount) {
                                        limit = catCount;
                                    }
                                    int start = (num * page)  - num;
                                    if (start > limit) {
                                        request.setAttribute("maxShow", 0);
                                        request.setAttribute("catCount", catCount);
                                        request.setAttribute("maxPage", maxPage);
                                        // Indexes must be matching
                                        request.setAttribute("categories", null);
                                        return;
                                    }
                                    // limit not go over cat count, if cat count g
                                    // I could make a check for the start index but meh
                                    Category[] catsPage = Arrays.copyOfRange(cats, start, limit);
                                    int[] artCounts = new int[catsPage.length];
                                    int[] visArtCounts = new int[catsPage.length];
                                    int[] hidArtCounts = new int[catsPage.length];
                                    Article[] recentArticles = new Article[cats.length];
                                    for (int i = 0; i < catsPage.length; i++) {
                                        artCounts[i] = countArticlesByCat(catsPage[i].getId(), true);
                                        visArtCounts[i] = countArticlesByCat(catsPage[i].getId(), false, false);
                                        hidArtCounts[i] = countArticlesByCat(catsPage[i].getId(), true, false);
                                        // Truncating takes too much time lel
                                        Article[] recentArticle =  getArticlesByCat(catsPage[i].getId(), 0, 1, 1,true);
                                        if (recentArticle == null) {
                                            // Bruh it suffered a stronk
                                            request.setAttribute("maxPage", 1);
                                            request.setAttribute("articles", null);
                                            return;
                                        } else {
                                            if (recentArticle.length > 0) {
                                                recentArticles[i] = recentArticle[0];
                                            } else {
                                                recentArticles[i] = null;
                                            }
                                        }
                                    }
                                    request.setAttribute("maxShow", catsPage.length - 1);
                                    request.setAttribute("catCount", catCount);
                                    request.setAttribute("maxPage", maxPage);
                                    // Indexes must be matching
                                    request.setAttribute("categories", catsPage);
                                    request.setAttribute("articleCounts", artCounts);
                                    request.setAttribute("visibleArticleCounts", visArtCounts);
                                    request.setAttribute("hiddenArticleCounts", hidArtCounts);
                                    request.setAttribute("recentArticles", recentArticles);
                                } else {
                                    // Boo
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                return;
                            }
                            return;
                        }

                    case "users":

                        break;
                    default:
                        // Option not found
                        request.setAttribute("result", 2);
                        return;
                }
            } else {
                // Return total counts of articles, categories, and users
                request.setAttribute("result", 0);
                request.setAttribute("articleCount", countArticles(true));
                request.setAttribute("hiddenArticleCount", countArticles(true, false));
                request.setAttribute("categoryCount", countCategories());
                request.setAttribute("userCount", countUsers());
                return;
            }
        } else {
            // Not admin or not logged in, idc, back to the lobby
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("login.jsp");
            return;
        }
    }

    /**
     * doPost method for the servlet. Handles POST requests.
     * This is supposed to be actioned by the forms from admin.jsp.
     * So this will redirect back to admin.jsp.
     * 0 = success
     * 1 = target not found
     * 2 = option not found
     * 3 = parsing error
     * 4 = deletion failed
     * 5 = update failed
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
                    // Should be provided by the forms, originating from admin.jsp

                    String show = request.getParameter("show");
                    String action = request.getParameter("action");

                    int page, num;
                    try {
                        page = Integer.parseInt(request.getParameter("page"));
                        num = Integer.parseInt(request.getParameter("num"));
                    } catch (NumberFormatException e) {
                        // Bruh
                        response.sendRedirect(fixURL(referer, "result=3"));
                        return;
                    }
                    if (show == null) {
                        // No action specified, back to admin.jsp
                        response.sendRedirect(fixURL(referer, "&result=2"));
                        return;
                    } else if (action == null) {
                        response.sendRedirect(fixURL(referer, "num=" + num + "&show=" + show + "&result=2"));
                        return;
                    }
                    String params = "page=" + page + "&num=" + num + "&show=" + show;
                    switch (show) {
                        case "articles":
                            // Article stuff
                            break;
                        case "categories":
                            // Category stuff
                            // No need to use a switch here, since there is only one option, renaming
                            if (action.equals("rename")) {
                                // Cat id should be a real valid id
                                int id = Integer.parseInt(request.getParameter("id"));
                                if (id == 0) {
                                    request.setAttribute("result", 4);
                                } else {
                                    String newName = request.getParameter("newName");
                                    if (newName == null) {
                                        // bruh moment
                                        response.sendRedirect(fixURL(referer, params + "&result=3"));
                                    } else {
                                        // Rename the category
                                        if (updateCategory(id, newName)) {
                                            // Success
                                            response.sendRedirect(fixURL(referer, params + "&result=0"));
                                        } else {
                                            // Failure
                                            response.sendRedirect(fixURL(referer, params + "&result=4"));
                                        }
                                    }
                                }
                            } else {
                                // Option not found
                                response.sendRedirect(referer);
                                return;
                            }
                            break;
                        case "users":
                            // User stuff
                            break;
                        default:
                            // Option not found
                            response.sendRedirect(referer);
                            return;
                    }
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
            response.sendRedirect("login.jsp");
        }
    }
}
