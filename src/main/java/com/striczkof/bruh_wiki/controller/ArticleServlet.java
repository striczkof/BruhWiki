package com.striczkof.bruh_wiki.controller;

import com.striczkof.bruh_wiki.dao.DatabaseAccess;
import com.striczkof.bruh_wiki.dao.PS;
import com.striczkof.bruh_wiki.model.Article;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

@WebServlet(name = "ArticleServlet", value = "/article-servlet")
public class ArticleServlet extends HttpServlet {
    private Logger log;
    private DatabaseAccess dao;

    /**
     * Initialising servlet with database connection and prepared statements.
     */
    public void init() {
        log = Logger.getLogger(ArticleServlet.class.getName());
        log.info( getServletName() + " initialising...");
        // Database initialisation
        PS[] psArray = new PS[]{
            PS.ART_GET_COUNT,
            PS.ART_GET_ALL,
            PS.ART_GET_ALL_TRUNC,
            PS.ART_GET_ONE,
            PS.ART_GET_ONE_TRUNC,
            PS.ART_GET_SOME_BY_EDITED_RANGE,
            PS.ART_GET_SOME_TRUNC_BY_EDITED_RANGE,
            PS.ART_GET_SOME_BY_MATCHING
        };
        dao = new DatabaseAccess(this, psArray);
        log.info( getServletName() + " initialised.");
    }

    /**
     * Closing the database upon servlet destruction.
     */
    public void destroy() {
        dao.close();
        log.info( getServletName() + " destroyed.");
        log = null;
    }

    private Article[] getArticles(int truncate, int limit, int starts) {
        try {
            ResultSet out;
            PreparedStatement useStatement;
            // Means all
            if (limit == starts) {
                // If limit and offset are for some reason equal, i'll allow it, would be nice if both zero though.
                if (truncate > 0) {
                    useStatement = dao.getPreparedStatement(PS.ART_GET_ONE_TRUNC);
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                } else {
                    useStatement = dao.getPreparedStatement(PS.ART_GET_ONE);
                }
            } else {
                if (limit <= 0 || starts <= 0) {
                    log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                    return null;
                } else {
                    int lowerRangeParamNum = 1;
                    int upperRangeParamNum = 2;
                    if (truncate > 0) {
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_TRUNC_BY_EDITED_RANGE);
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        lowerRangeParamNum = 3;
                        upperRangeParamNum = 4;
                    } else {
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_BY_EDITED_RANGE);
                    }
                    useStatement.setInt(lowerRangeParamNum, limit);
                    useStatement.setInt(upperRangeParamNum, starts);
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
            log.warning("The servlet " + getServletName() + " has received a GET but there is no 'show' parameter.");
        } else if(request.getParameter("show") != null) {
            int truncate = 0;
            if (request.getParameter("truncate") != null) {
                try {
                    truncate = Integer.parseInt(request.getParameter("truncate"));
                } catch (NumberFormatException e) {
                    log.warning("The servlet " + getServletName() + " has received a GET but the 'truncate' parameter is not a number.");
                }
            }
            if (request.getParameter("show").equals("all")) {
                request.setAttribute("articles", getArticles(truncate, 0, 0));
            }

        } else {
            log.warning("The servlet " + getServletName() + " has received a GET but hit none of the ifs for some funny reason.");
        }
    }
}
