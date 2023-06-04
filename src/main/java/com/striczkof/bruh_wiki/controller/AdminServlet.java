package com.striczkof.bruh_wiki.controller;

import com.striczkof.bruh_wiki.dao.DatabaseAccess;
import com.striczkof.bruh_wiki.dao.PS;
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
                PS.ART_GET_ALL,
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
