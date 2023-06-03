package com.striczkof.bruh_wiki.model;

import jakarta.servlet.ServletContext;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Database Java Bean. Idk why tbh.
 * @author Alvin
 */
public class Database implements Serializable {
    private String driver;
    private Connection connection;
    private PreparedStatement[] preparedStatements;

    /**
     * Java Bean constructor.
     */
    public Database() {
        // Java Bean constructor
    }

    /**
     * Initialising database for the servlets
     * plus initialises the PreparedStatement array too!
     */
    public Database(ServletContext servletContext, String sqlsStacked) throws SQLException, ClassNotFoundException {
        this.driver = "jdbc:mysql://"+ servletContext.getInitParameter("db-url") + "/" + servletContext.getInitParameter("db-name");
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection( driver, servletContext.getInitParameter("db-user"), servletContext.getInitParameter("db-pass"));
        String[] sqls = sqlsStacked.split(";");
        this.preparedStatements = new PreparedStatement[sqls.length];
        for (int i = 0; i < sqls.length; i++) {
            preparedStatements[i] = connection.prepareStatement(sqls[i]);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) throws SQLException{
        if(connection == null && this.connection != null) {
            try {
                if (this.preparedStatements != null) {
                    setPreparedStatements(null);
                }
                this.connection.close();
                // DriverManager.deregisterDriver(DriverManager.getDriver(driver));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            this.connection = connection;
        }
    }

    public PreparedStatement[] getPreparedStatements() {
        return preparedStatements;
    }

    public void setPreparedStatements(PreparedStatement[] preparedStatements) throws SQLException{
        if (preparedStatements == null && this.preparedStatements != null) {
            for (PreparedStatement preparedStatement : this.preparedStatements) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.preparedStatements = preparedStatements;
        }
    }
}
