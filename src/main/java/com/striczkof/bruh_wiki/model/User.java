package com.striczkof.bruh_wiki.model;

import java.io.Serializable;

/** User bean.
 * @author Alvin
 */
public class User implements Serializable {
    private int id; // Auto-incremented, cannot be null, cannot be changed
    private String username; // Must be unique, cannot be null, can be changed, TINYTEXT
    private byte[] passwordHash; // SHA-256 Hash, BINARY(32)
    private byte[] passwordSalt; // SHA-256 Salt, BINARY(16)
    private String name; // Can be any name or null
    private boolean admin; // True grants admin privileges
    private long created; // Unix timestam
    private long lastLogin; // Unix timestamp

    /**
     * Java Bean constructor.
     */
    public User() {
        // Java Bean constructor
    }

    /**
     * Initialise all variables except for passwordHash.
     * @param id The user ID.
     * @param username The username.
     * @param name The name.
     * @param admin Whether the user is an admin.
     * @param created The creation timestamp.
     * @param lastLogin The last login timestamp.
     */
    public User(int id, String username, byte[] passwordSalt, String name, boolean admin, long created, long lastLogin) {
        this.id = id;
        this.username = username;
        this.passwordSalt = passwordSalt;
        if (name == null || name.isEmpty()) {
            this.name = username;
        } else {
            this.name = name;
        }
        this.admin = admin;
        this.created = created;
        this.lastLogin = lastLogin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password hash. In normal situations, this should be empty.
     * @return The password hash.
     */
    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * Sets the password salt. In normal situations, this is not set.
     * @param passwordSalt The password salt.
     */
    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }
}
