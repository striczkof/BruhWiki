package com.striczkof.bruh_wiki.dao;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServlet;
// Tomcat logging
import java.util.logging.Logger;

import java.sql.*;
import java.util.EnumMap;

public class DatabaseAccess {
    private Logger log;
    private Connection connection;
    private EnumMap<PS, PreparedStatement> psArray;
    private String servletName;

    public DatabaseAccess(HttpServlet servlet, PS[] ps) {
        log = Logger.getLogger(DatabaseAccess.class.getName());
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            ServletContext context = servlet.getServletContext();
            servletName = servlet.getServletName();
            this.connection = DriverManager.getConnection("jdbc:mysql://"+ context.getInitParameter("db-url") + "/" + context.getInitParameter("db-name"), context.getInitParameter("db-user"), context.getInitParameter("db-pass"));
            if (connection == null) {
                log.info("The servlet " + servletName + "'s DAO has failed to connect to the database.");
            } else {
                log.info("The servlet " + servletName + "'s DAO has successfully connected to the database.");
            }
            if (ps != null && ps.length > 0) {
                this.psArray = new EnumMap<>(PS.class);
                for (PS p : ps) {
                    this.psArray.put(p, connection.prepareStatement(SQLStatement.valueOf(p.name()).getStatement()));
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            log.severe("The servlet " + servletName + "'s DAO has had a bruh moment.");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (psArray != null && !psArray.isEmpty()) {
                for (PreparedStatement ps : psArray.values()) {
                    ps.close();
                }
            }
        } catch (SQLException e) {
            log.severe("The servlet " + servletName + "'s DAO has had a bruh moment.");
            e.printStackTrace();
        }
        log = null;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public boolean addPreparedStatement(PS ps) {
        try {
            if (psArray == null) {
                psArray = new EnumMap<>(PS.class);
            }
            psArray.put(ps, connection.prepareStatement(SQLStatement.valueOf(ps.name()).getStatement()));
            return true;
        } catch (SQLException e) {
            log.severe("The servlet " + servletName + "'s DAO has had a bruh moment.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean removePreparedStatement(PS ps) {
        if (psArray != null && psArray.containsKey(ps)) {
            try {
                psArray.get(ps).close();
                psArray.remove(ps);
                return true;
            } catch (SQLException e) {
                log.severe("The servlet " + servletName + "'s DAO has had a bruh moment.");
                e.printStackTrace();
                return false;
            }
        } else {
            // huh
            return false;
        }
    }

    public PreparedStatement getPreparedStatement(PS ps) {
        return this.psArray.get(ps);
    }
}
