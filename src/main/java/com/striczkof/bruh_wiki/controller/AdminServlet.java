package com.striczkof.bruh_wiki.controller;

import com.striczkof.bruh_wiki.model.Database;
import com.striczkof.bruh_wiki.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AdminServlet", value = "/admin-servlet")
public class AdminServlet extends HttpServlet {
    private Database database;

    /**
     * Initialising servlet with database connection and prepared statements.
     */
    public void init() {
        // SQL statements for the prepared statements, this is delineated by a semicolon
        String sqls = "";
        // Gotta confirm if the user is an admin first
        // PS[0]: Confirm if user is an admin, true means user is an admin, 1 parameter for id
        sqls += "SELECT admin FROM users WHERE id = ?;";
        // PS[1]: Count all users, 1 returned means successful count, no parameters
        sqls += "SELECT COUNT(id) FROM users;";
        // PS[2]: Count all articles, 1 returned means successful count, no parameters
        sqls += "SELECT COUNT(id) FROM articles;";
        // PS[3]: Count all categories, 1 r eturned means successful count, no parameters
        sqls += "SELECT COUNT(id) FROM categories;";
        // PS[4]: Get all users, 1 returned means successful user retrieval, no parameters
        sqls += "SELECT id, username, name, admin, unix_timestamp(created) as 'created', unix_timestamp(last_login) as 'last_login' FROM users;";
        // PS[5]: Get all articles, 1 returned means successful article retrieval, no parameters
        sqls += "SELECT id, category_id, unix_timestamp(made) AS 'made', unix_timestamp(lastEdited) AS 'lastEdited', title, content, hidden FROM articles ORDER BY id DESC;";
        // PS[6]: Get all categories, 1 returned means successful category retrieval, no parameters
        sqls += "SELECT id, name FROM categories;";
        // PS[7]: Get one user, 1 returned means successful user retrieval, 1 parameter for id
        sqls += "SELECT id, username, name, admin, unix_timestamp(created) as 'created', unix_timestamp(last_login) as 'last_login' FROM users WHERE id = ?;";
        // PS[8]: Get one article, 1 returned means successful article retrieval, 1 parameter for id
        sqls += "SELECT id, category_id, unix_timestamp(made) AS 'made', unix_timestamp(lastEdited) AS 'lastEdited', title, content, hidden FROM articles WHERE id = ?;";
        // PS[9]: Get one category, 1 returned means successful category retrieval, 1 parameter for id
        sqls += "SELECT id, name FROM categories WHERE id = ?;";
        // 9 statements for user changes
        // PS[10]: Change username, 1 returned means successful username change, 2 parameters for username and id
        sqls += "UPDATE users SET username = ? WHERE id = ?;";
        // PS[11]: Change name, 1 returned means successful name change, 2 parameters for id and name
        sqls += "UPDATE users SET name = ? WHERE id = ?;";
        // PS[12]: Change username and name, 1 returned means successful username and name change, 3 parameters for id, username and name
        sqls += "UPDATE users SET username = ?, name = ? WHERE id = ?;";
        // PS[13]: Change admin status, 1 returned means successful admin status change, 2 parameters for id and admin status
        sqls += "UPDATE users SET admin = ? WHERE id = ?;";
        // PS[14]: Change username and admin status, 1 returned means successful username and admin status change, 3 parameters for id, username and admin status
        sqls += "UPDATE users SET username = ?, admin = ? WHERE id = ?;";
        // PS[15]: Change name and admin status, 1 returned means successful name and admin status change, 3 parameters for id, name and admin status
        sqls += "UPDATE users SET name = ?, admin = ? WHERE id = ?;";
        // PS[16]: Change username, name and admin status, 1 returned means successful username, name and admin status change, 4 parameters for id, username, name and admin status
        sqls += "UPDATE users SET username = ?, name = ?, admin = ? WHERE id = ?;";
        // PS[17]: Delete user, 1 returned means successful user deletion, 1 parameter for id
        sqls += "DELETE FROM users WHERE id = ?;";
        // 15 statements to change category, title, content and hidden status, jesus
        // PS[18]: Change category, 1 returned means successful category change, 2 parameters for category id and article id
        sqls += "UPDATE articles SET category_id = ? WHERE id = ?;";
        // PS[19]: Change title, 1 returned means successful title change, 2 parameters for title and article id
        sqls += "UPDATE articles SET title = ? WHERE id = ?;";
        // PS[20]: Change content, 1 returned means successful content change, 2 parameters for content and article id
        sqls += "UPDATE articles SET content = ? WHERE id = ?;";
        // PS[21]: Change hidden status, 1 returned means successful hidden status change, 2 parameters for hidden status and article id
        sqls += "UPDATE articles SET hidden = ? WHERE id = ?;";
        // PS[22]: Change category and title, 1 returned means successful category and title change, 3 parameters for category id, title and article id
        sqls += "UPDATE articles SET category_id = ?, title = ? WHERE id = ?;";
        // PS[23]: Change category and content, 1 returned means successful category and content change, 3 parameters for category id, content and article id
        sqls += "UPDATE articles SET category_id = ?, content = ? WHERE id = ?;";
        // PS[24]: Change category and hidden status, 1 returned means successful category and hidden status change, 3 parameters for category id, hidden status and article id
        sqls += "UPDATE articles SET category_id = ?, hidden = ? WHERE id = ?;";
        // PS[25]: Change title and content, 1 returned means successful title and content change, 3 parameters for title, content and article id
        sqls += "UPDATE articles SET title = ?, content = ? WHERE id = ?;";
        // PS[26]: Change title and hidden status, 1 returned means successful title and hidden status change, 3 parameters for title, hidden status and article id
        sqls += "UPDATE articles SET title = ?, hidden = ? WHERE id = ?;";
        // PS[27]: Change content and hidden status, 1 returned means successful content and hidden status change, 3 parameters for content, hidden status and article id
        sqls += "UPDATE articles SET content = ?, hidden = ? WHERE id = ?;";
        // PS[28]: Change category, title and content, 1 returned means successful category, title and content change, 4 parameters for category id, title, content and article id
        sqls += "UPDATE articles SET category_id = ?, title = ?, content = ? WHERE id = ?;";
        // PS[29]: Change category, title and hidden status, 1 returned means successful category, title and hidden status change, 4 parameters for category id, title, hidden status and article id
        sqls += "UPDATE articles SET category_id = ?, title = ?, hidden = ? WHERE id = ?;";
        // PS[30]: Change category, content and hidden status, 1 returned means successful category, content and hidden status change, 4 parameters for category id, content, hidden status and article id
        sqls += "UPDATE articles SET category_id = ?, content = ?, hidden = ? WHERE id = ?;";
        // PS[31]: Change title, content and hidden status, 1 returned means successful title, content and hidden status change, 4 parameters for title, content, hidden status and article id
        sqls += "UPDATE articles SET title = ?, content = ?, hidden = ? WHERE id = ?;";
        // PS[32]: Change category, title, content and hidden status, 1 returned means successful category, title, content and hidden status change, 5 parameters for category id, title, content, hidden status and article id
        sqls += "UPDATE articles SET category_id = ?, title = ?, content = ?, hidden = ? WHERE id = ?;";
        // PS[33]: Delete article, 1 returned means successful article deletion, 1 parameter for id
        sqls += "DELETE FROM articles WHERE id = ?;";
        // PS[34]: Change category name, 1 returned means successful category name change, 2 parameters for name and id
        sqls += "UPDATE categories SET name = ? WHERE id = ?;";
        // PS[35]: Delete category, 1 returned means successful category deletion, 1 parameter for id
        sqls += "DELETE FROM categories WHERE id = ?;";
        // FUCKING FINALLY

        try {
            // Database and prepared statement initialisation
            database = new Database(getServletContext(), sqls);
            if (database.getConnection() == null) {
                System.out.println("The servlet " + getServletName() + " has failed to connect to the database.");
            } else {
                System.out.println("The servlet " + getServletName() + " has successfully connected to the database.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Bruh
            System.out.println("The servlet " + getServletName() + " has suffered a stronk");
            e.printStackTrace();
        }
    }

    /**
     * Closing the database upon servlet destruction.
     */
    public void destroy() {
        try {
            database.setPreparedStatements(null);
            database.setConnection(null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make sure that the user is logged in and is an admin.
     * No SQL wizardry needed, session check is enough.
     * @param session The session to check. Should be "request.getSession(false)" to prevent a new session from being created.
     * @return True if the user is logged in and is an admin, false otherwise.
     */
    private boolean authenticateAdmin(HttpSession session) {
        if (session != null) {
            if (session.getAttribute("user") != null) {
                // It will be assumed that the user attribute is actually a user object, if not, fuck me
                User user = (User) session.getAttribute("user");
                return user.getAdmin();
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
