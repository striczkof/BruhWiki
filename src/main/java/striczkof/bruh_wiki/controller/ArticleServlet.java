package striczkof.bruh_wiki.controller;

import striczkof.bruh_wiki.model.Article;
import striczkof.bruh_wiki.model.Database;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ArticleServlet", value = "/article-servlet")
public class ArticleServlet extends HttpServlet {
    private Database database;

    /**
     * Initialising servlet with database connection and prepared statements.
     */
    public void init() {
        // Database initialisation
        // SQL statements for prepared statements, this is delineated by a semicolon
        String sqls = "";
        // PS[0]: Count all articles
        sqls += "SELECT COUNT(id) FROM articles;";
        // PS[1]: Get all articles
        sqls += "SELECT id, category_id, UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles ORDER BY lastEdited DESC;";
        // PS[2]: Gets all articles, but with truncation, parameter 1 and 2 must be equal. Idiot check expected
        sqls += "SELECT id, category_id, UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles ORDER BY lastEdited DESC;";
        // PS[3]: Gets one article, has one parameter for id
        sqls += "SELECT id, category_id, UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden  FROM articles WHERE id=?;";
        // PS[4]: Gets one article, has one parameter for title
        sqls += "SELECT id, category_id, UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden  FROM articles WHERE id=?;";
        // PS[5]: Gets some article, has 2 parameters, parameter 1 must be less than parameter 2. Idiot check expected
        sqls += "SELECT id, category_id, UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, content, hidden FROM articles WHERE id BETWEEN ? AND ? ORDER BY lastEdited DESC;";
        // PS[6]: Gets some article but with truncation, contains 4 parameters, first and second parameters must be equal, third parameter must be less than fourth parameter. Idiot check expected
        sqls += "SELECT id, category_id, UNIX_TIMESTAMP(made) AS 'made', UNIX_TIMESTAMP(lastEdited) AS 'lastEdited', title, CASE WHEN CHAR_LENGTH(content) > ? THEN CONCAT(SUBSTRING(content, 1, ?), '...') ELSE content END AS 'content', hidden FROM articles WHERE id BETWEEN ? AND ? ORDER BY lastEdited DESC;";
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

    private Article[] getArticles(int truncate, int[] range) {
        try {
            ResultSet out;
            PreparedStatement useStatement;
            // Means all
            if (range == null) {
                if (truncate > 0) {
                    useStatement = database.getPreparedStatements()[2];
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                } else {
                    useStatement = database.getPreparedStatements()[1];
                }
            } else {
                if (range.length != 2 || range[0] > range[1]) {
                    System.out.println("Bruh moment at ArticleServlet.getArticles(int, int[])");
                    return null;
                } else {
                    int lowerRangeParamNum = 1;
                    int upperRangeParamNum = 2;
                    if (truncate > 0) {
                        useStatement = database.getPreparedStatements()[6];
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        lowerRangeParamNum = 3;
                        upperRangeParamNum = 4;
                    } else {
                        useStatement = database.getPreparedStatements()[5];
                    }
                    useStatement.setInt(lowerRangeParamNum, range[0]);
                    useStatement.setInt(upperRangeParamNum, range[1]);
                }
            }
            out = useStatement.executeQuery();
            // Bruh why do I have to use list
            ArrayList<Article> articles = new ArrayList<>();
            while (out.next()) {
                articles.add(new Article(out.getInt("id"), out.getInt("category_id"), out.getLong("made"), out.getLong("lastEdited"), out.getString("title"), out.getString("content"), out.getBoolean("hidden")));
            }
            Article[] articlesArray = new Article[articles.size()];
            articlesArray = articles.toArray(articlesArray);
            return articlesArray;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Look, using request parameters is a funny way to do this, but I'm not sure if there is any other way
     * to pass variables back into the included servlet.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("show") == null) {
            System.out.println("The servlet " + getServletName() + " has received a GET but there is no 'show' parameter.");
        } else if(request.getParameter("show") != null) {
            int truncate = 0;
            if (request.getParameter("truncate") != null) {
                try {
                    truncate = Integer.parseInt(request.getParameter("truncate"));
                } catch (NumberFormatException e) {
                    System.out.println("The servlet " + getServletName() + " has received a GET but the 'truncate' parameter is not a number.");
                }
            }
            if (request.getParameter("show").equals("all")) {
                request.setAttribute("articles", getArticles(truncate, null));
            }

        } else {
            System.out.println("The servlet " + getServletName() + " has received a GET but hit none of the ifs for some funny reason.");
        }
    }
}
