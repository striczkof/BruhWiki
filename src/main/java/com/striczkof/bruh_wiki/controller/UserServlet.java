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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebServlet(name = "UserServlet", value = "/user-servlet")
public class UserServlet extends HttpServlet {
    private Logger log;
    private DatabaseAccess dao;
    // Initializing commonly used context parameters
    private int saltSize;
    private String hashAlgorithm;

    /**
     * Initialising servlet with database connection and prepared statements.
     */
    public void init() {
        log = Logger.getLogger(UserServlet.class.getName());
        log.info( getServletName() + " initialising...");
        // Parameter initialisation
        saltSize = Integer.parseInt(getServletContext().getInitParameter("salt-size"));
        hashAlgorithm = getServletContext().getInitParameter("hashing-algorithm");
        // SQL statements for the prepared statements
        PS[] ps = new PS[]{
                PS.USERS_GET_COUNT,
                PS.USERS_GET_ALL,
                PS.USERS_GET_ONE,
                PS.USERS_GET_ID_ONE_BY_UNAME,
                PS.USERS_GET_ID_SALT_ONE_BY_UNAME,
                PS.USERS_MAKE_ONE,
                PS.USERS_SET_LAST_LOGIN_ONE_BY_ID_HASH,
                PS.USERS_DEL_ONE_BY_ID_HASH,
                PS.USERS_SET_PASSWORD_ONE_BY_ID_HASH,
                PS.USERS_SET_UNAME_ONE,
                PS.USERS_SET_NAME_ONE,
                PS.USERS_SET_UNAME_NAME_ONE,
                PS.USERS_GET_ID_UNAME_ONE_LIKE_UNAME
        };
        dao = new DatabaseAccess(this, ps);
        log.info("UserServlet initialised.");
    }

    /**
     * Closing the database upon servlet destruction.
     */
    public void destroy() {
        dao.close();
        log = null;
    }

    /**
     * Get user by id
     * @param id id of user
     * @return user object with everything except password hash if successful, null if unsuccessful
     */
    private User getUser(int id) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.USERS_GET_ONE);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // User exists
                return new User(rs.getInt("id"), rs.getString("username"), rs.getBytes("salt"), rs.getString("name"), rs.getBoolean("admin"), rs.getLong("created"), rs.getLong("last_login"));
            } else {
                // Invalid id
                return null;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Login with username and password, updates last login date
     * @param username username string
     * @param password password string
     * @return user object with everything except password hash if successful, initialised user object with id -1 if incorrect password, initialialed user object with id -2 if username doesn't exist, null if error
     */
    private User login(String username, String password) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.USERS_GET_ID_SALT_ONE_BY_UNAME);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Username exists
                int id = rs.getInt("id");
                byte[] salt = rs.getBytes("salt");
                byte[] hash = hashPassword(password, salt);
                ps = dao.getPreparedStatement(PS.USERS_SET_LAST_LOGIN_ONE_BY_ID_HASH);
                ps.setInt(1, id);
                ps.setBytes(2, hash);
                if (ps.executeUpdate() == 1) {
                    // Successful login
                    return getUser(id);
                } else {
                    // Incorrect password
                    User user = new User();
                    user.setId(-1);
                    return user;
                }
            } else {
                // Username doesn't exist
                User user = new User();
                user.setId(-2);
                return user;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a bruh moment.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Register a new user
     * @param user user object, containing username, password, and name
     * @return user object of a logged-in user. (has id, no hash) User id will be of the registered user, -1 if username exists, -3 if SQL error
     */
    private User register(User user) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.USERS_GET_ID_ONE_BY_UNAME);
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Username exists
                user.setId(-1);
                return user;
            } else {
                // Username doesn't exist
                ps = dao.getPreparedStatement(PS.USERS_MAKE_ONE);
                ps.setString(1, user.getUsername());
                ps.setBytes(2, user.getPasswordHash());
                ps.setBytes(3, user.getPasswordSalt());
                ps.setString(4, user.getName());
                if (ps.executeUpdate() == 1) {
                    // Successful registration
                    // Get user id
                    ps = dao.getPreparedStatement(PS.USERS_GET_ID_ONE_BY_UNAME);
                    ps.setString(1, user.getUsername());
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        // Username exists
                        user.setPasswordHash(null);
                        user.setId(rs.getInt("id"));
                        // Set name as username if name is empty
                        if (user.getName() == null || user.getName().equals("")) {
                            user.setName(user.getUsername());
                        }
                        return user;
                    } else {
                        // Username not found after successful registration, how?
                        user.setId(-3);
                        return user;
                    }
                } else {
                    // SQL error
                    user.setId(-3);
                    return user;
                }
            }
        } catch (SQLException e) {
            // Bruh
            e.printStackTrace();
            user.setId(-3);
            return user;
        }
    }

    /**
     * Updates user's username, name, and admin status
     * @param user regular user object, containing id, username, name, and admin status
     * @param username new username
     * @param name new name
     * @return user object with updated variables if successful, unchanged user object if unsuccessful. null if SQL error
     */
    private User updateUser(User user, String username, String name) {
        try {
            PreparedStatement ps;
            // If changing username, check if username is taken by another user
            if (!user.getUsername().equals(username)) {
                ps = dao.getPreparedStatement(PS.USERS_GET_ID_ONE_BY_UNAME);
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    // Username exists
                    return null;
                }
            }
            // Update user, select statement on whether to update username, name, or both
            if (username != null) {
                if (name != null) {
                    // Change both
                    ps = dao.getPreparedStatement(PS.USERS_SET_UNAME_NAME_ONE);
                    ps.setString(1, username);
                    ps.setString(2, name);
                    ps.setInt(3, user.getId());
                } else {
                    // Change username only
                    ps = dao.getPreparedStatement(PS.USERS_SET_UNAME_ONE);
                    ps.setString(1, username);
                    ps.setInt(2, user.getId());
                }
            } else {
                // Change name only
                ps = dao.getPreparedStatement(PS.USERS_SET_NAME_ONE);
                ps.setString(1, name);
                ps.setInt(2, user.getId());
            }
            if (ps.executeUpdate() == 1) {
                // Update successful
                // Update user object
                if (username != null) {
                    user.setUsername(username);
                }
                if (name != null) {
                    user.setName(name);
                }
                return user;
            } else {
                // Update failed
                return null;
            }

        } catch (SQLException e) {
            // Bruh
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Change user's password
     * Only same user can change password, make sure to check that
     * @param user user object, containing id, username, name, and admin status
     * @param newPassword new password
     * @param oldPassword old password for verification
     * @return user object with updated salt if successful, unchanged user object if unsuccessful. null if SQL error
     */
    private User changePassword(User user, String newPassword, String oldPassword) {
        try {
            // Generate new hash and salt
            byte[] salt = generateSalt();
            byte[] hash = hashPassword(newPassword, salt);
            PreparedStatement ps = dao.getPreparedStatement(PS.USERS_SET_PASSWORD_ONE_BY_ID_HASH);
            ps.setBytes(1, salt);
            ps.setBytes(2, hash);
            // Match user id and old password hash
            ps.setInt(3, user.getId());
            ps.setBytes(4, hashPassword(oldPassword, user.getPasswordSalt()));
            if (ps.executeUpdate() == 1) {
                // Update successful
                // Update user object
                user.setPasswordSalt(salt);
                return user;
            } else {
                // Update failed
                return user;
            }
        } catch (SQLException e) {
            // Bruh
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Look for users by their username
     * @param username username to match
     * @return array of matching users
     */
    private User[] searchUsers(String username) {
        return null;
    }

    /**
     * Generates a hash from the password and salt
     * @param password password string
     * @param salt bytes of salt
     * @return hash
     */
    private byte[] hashPassword(String password, byte[] salt) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(hashAlgorithm);
            messageDigest.update(salt);
            return messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a salt for hashing
     * @return salt bytes of salt
     */
    private byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[saltSize];
        secureRandom.nextBytes(salt);
        return salt;
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
     * Gets rid of existing parameters in the URL and adds the new ones, if any.
     * Because IDEA is crying about 'multiple occurrences'
     * @param url URL to modify
     * @return modified URL
     */
    private String fixURL(String url) {
        return fixURL(url, "");
    }

    /**
     * doGet for quick stuff
     * Handles logout
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gets (or creates) the session
        HttpSession session = request.getSession(true);

        String referer = request.getHeader("referer");
        // Handles logout
        if (referer != null) {
            // Handles logout
            if (request.getParameter("logout") != null) {
                if (session.getAttribute("user") != null) {
                    // User logged in, log out
                    session.removeAttribute("user");
                    session.invalidate();
                    response.sendRedirect(referer);
                } else {
                    // Not even logged in, why are you here
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    session.invalidate();
                    response.sendRedirect(referer);
                }
            }
        } else {
            // No referer, go to index
            response.sendRedirect("index.jsp");
        }
    }

    /**
     * doPost for secure stuff
     * Handles login, register, change info, and change password
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gets (or creates) the session
        HttpSession session = request.getSession(true);

        String referer = request.getHeader("referer");
        if (referer != null) {
            // Handles login, register, change info, and change password
            if (request.getParameter("login") != null) {
                // If referred from user.jsp, accept it then replace user.jsp with login.jsp. Dirty hack ik but im too lazy to do it properly
                if (referer.contains("user.jsp")) {
                    referer = referer.replace("user.jsp", "login.jsp");
                }
                if (referer.contains("login.jsp")) {
                    // Logs in
                    if (session.getAttribute("user") != null) {
                        // My god you are already logged in you should not even be here
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.sendRedirect(fixURL(referer, "result=already-logged-in"));
                    } else {
                        // Not logged in, good
                        User user = login(request.getParameter("username"), request.getParameter("password"));
                        if (user == null) {
                            // SQL error
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.sendRedirect(fixURL(referer, "result=sql-error"));
                        } else {
                            // User object made, hmm
                            if (user.getId() == -2) {
                                // Username doesn't exist
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.sendRedirect(fixURL(referer, "result=user-not-found"));
                            } else if (user.getId() == -1) {
                                // Incorrect password
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.sendRedirect(fixURL(referer, "result=wrong-pass"));
                            } else {
                                // Successful login
                                session.setAttribute("user", user);
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.sendRedirect(fixURL(referer, "result=success"));
                            }
                        }
                    }
                } else {
                    // Not from login page, why are you here
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendRedirect(referer);
                }
            } else if (request.getParameter("register") != null) {
                if (referer.contains("register.jsp")) {
                    // Registers and logs in
                    if (session.getAttribute("user") != null) {
                        // My god you are already logged in you should not even be here
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    } else {
                        // Not logged in, good
                        String username = request.getParameter("username");
                        String password = request.getParameter("password");
                        String name = request.getParameter("name");
                        if (username == null || password == null) {
                            // Invalid parameters
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            return;
                        } else {
                            // Valid parameters
                            byte[] salt = generateSalt();
                            byte[] hash = hashPassword(password, salt);
                            // Username, password, name
                            User user = new User();
                            user.setUsername(username);
                            user.setPasswordHash(hash);
                            user.setPasswordSalt(salt);
                            user.setName(name);
                            User regUser = register(user);
                            if (regUser.getId() == -3) {
                                // SQL error
                                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                response.sendRedirect(fixURL(referer, "result=sql-error"));
                            } else if (regUser.getId() == -1) {
                                // Username already exists
                                response.setStatus(HttpServletResponse.SC_CONFLICT);
                                // request.getRequestDispatcher("/register.jsp?result=user-exists").forward(request, response);
                                response.sendRedirect(fixURL(referer, "result=user-exists"));
                            } else {
                                // Successful registration
                                session.setAttribute("user", regUser);
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.sendRedirect(fixURL(referer, "result=success"));
                            }
                        }
                    }
                } else {
                    // Not from register page, why are you here
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.sendRedirect(fixURL(referer));
                }
            } else if (request.getParameter("user-changes") != null) {
                // CAREFUL! Check if user is the same as the one being changed!
                return;
            } else {
                // Invalid action
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.sendRedirect(fixURL(referer, "?result=invalid-action"));
                return;
            }
        } else {
            // No referer, go to index
            response.sendRedirect("index.jsp");
        }
    }
}
