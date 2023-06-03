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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", value = "/user-servlet")
public class UserServlet extends HttpServlet {
    private Database database;
    // Initializing commonly used context parameters
    private int saltSize;
    private String hashAlgorithm;

    /**
     * Initialising servlet with database connection and prepared statements.
     */
    public void init() {
        // Parameter initialisation
        saltSize = Integer.parseInt(getServletContext().getInitParameter("salt-size"));
        hashAlgorithm = getServletContext().getInitParameter("hashing-algorithm");
        // SQL statements for the prepared statements
        String sqls = "";
        // PS[0]: Count all users
        sqls += "SELECT COUNT(id) FROM users;";
        // PS[1]: Get all users
        sqls += "SELECT id, username, name, admin, UNIX_TIMESTAMP(created) as 'created', UNIX_TIMESTAMP(last_login) as 'last_login' FROM users;";
        // PS[2]: Get salt for hashing and id for authentication, 1 parameter for username, returns none if username not found
        sqls += "SELECT id, salt FROM users WHERE username = ?;";
        // PS[3]: Update user's last login date, 1 returned means successful login, 2 parameters for id and hash
        sqls += "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ? AND hash = ?;";
        // PS[4]: Insert new user, 1 returned means successful registration, 4 parameters for username, hash and salt, and name
        sqls += "INSERT INTO users (username, hash, salt, name) VALUES (?, ?, ?, ?);";
        // PS[5]: Change password, 1 returned means successful password change, 3 parameters for new hash and salt, id, and old hash
        sqls += "UPDATE users SET hash = ?, salt = ? WHERE id = ? AND hash = ?;";
        // PS[6]: Delete user, asks for password, 1 returned means successful deletion, 2 parameters for id and hash
        sqls += "DELETE FROM users WHERE id = ? AND hash = ?;";
        // PS[7]: Get user by id except for password hash, 1 parameter for id
        sqls += "SELECT id, username, salt, name, admin, UNIX_TIMESTAMP(created) as 'created', UNIX_TIMESTAMP(last_login) as 'last_login' FROM users WHERE id = ?;";
        // 3 statements just to change either the username or name or both
        // User and admin made changes so no admin status change
        // PS[8]: Change username, 1 returned means successful username change, 2 parameters for id and username
        sqls += "UPDATE users SET username = ? WHERE id = ?;";
        // PS[9]: Change name, 1 returned means successful name change, 2 parameters for id and name
        sqls += "UPDATE users SET name = ? WHERE id = ?;";
        // PS[10]: Change username and name, 1 returned means successful username and name change, 3 parameters for id, username and name
        sqls += "UPDATE users SET username = ?, name = ? WHERE id = ?;";
        // PS[11]: Look up users by username, returns id and username, 1 parameter for username
        sqls += "SELECT id, username FROM users WHERE username LIKE %?%;";
        // PS[12]: Check if username exists, returns id if username exists, 1 parameter for username
        sqls += "SELECT id FROM users WHERE username = ?;";
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
     * Get user by id
     * @param id id of user
     * @return user object with everything except password hash if successful, null if unsuccessful
     */
    private User getUser(int id) {
        try {
            PreparedStatement ps = database.getPreparedStatements()[7];
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
            // Bruh
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
            PreparedStatement ps = database.getPreparedStatements()[2];
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Username exists
                int id = rs.getInt("id");
                byte[] salt = rs.getBytes("salt");
                byte[] hash = hashPassword(password, salt);
                ps = database.getPreparedStatements()[3];
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
            // Bruh
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
            PreparedStatement ps = database.getPreparedStatements()[12];
            ps.setString(1, user.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Username exists
                user.setId(-1);
                return user;
            } else {
                // Username doesn't exist
                ps = database.getPreparedStatements()[4];
                ps.setString(1, user.getUsername());
                ps.setBytes(2, user.getPasswordHash());
                ps.setBytes(3, user.getPasswordSalt());
                ps.setString(4, user.getName());
                if (ps.executeUpdate() == 1) {
                    // Successful registration
                    // Get user id
                    ps = database.getPreparedStatements()[12];
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
                ps = database.getPreparedStatements()[12];
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
                    ps = database.getPreparedStatements()[10];
                    ps.setString(1, username);
                    ps.setString(2, name);
                    ps.setInt(3, user.getId());
                } else {
                    // Change username only
                    ps = database.getPreparedStatements()[8];
                    ps.setString(1, username);
                    ps.setInt(2, user.getId());
                }
            } else {
                // Change name only
                ps = database.getPreparedStatements()[9];
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
            PreparedStatement ps = database.getPreparedStatements()[5];
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
     * doGet for quick stuff
     * Handles logout
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet received");
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
     * doPost for secure stuff
     * Handles login, register, change info, and change password
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Gets (or creates) the session
        HttpSession session = request.getSession(true);

        String referer = request.getHeader("referer");
        if (referer != null) {
            // Handles login, register, change info, and change password
            if (request.getParameter("login") != null) {
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
