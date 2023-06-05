package com.striczkof.bruh_wiki.controller;

import com.striczkof.bruh_wiki.dao.DatabaseAccess;
import com.striczkof.bruh_wiki.dao.PS;
import com.striczkof.bruh_wiki.model.Article;
import com.striczkof.bruh_wiki.model.Category;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
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
            PS.ART_GET_COUNT_BY_CAT,
            PS.ART_GET_ALL,
            PS.ART_GET_ALL_TRUNC,
            PS.ART_GET_ALL_BY_CAT,
            PS.ART_GET_ALL_TRUNC_BY_CAT,
            PS.ART_GET_ONE,
            PS.ART_GET_ONE_TRUNC,
            PS.ART_GET_SOME_BY_EDITED_RANGE,
            PS.ART_GET_SOME_TRUNC_BY_EDITED_RANGE,
            PS.ART_GET_SOME_BY_CAT_EDITED_RANGE,
            PS.ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE,
            PS.ART_GET_SOME_BY_MATCHING,
            PS.ART_GET_COUNT_BY_MATCHING,
            PS.CAT_GET_COUNT,
            PS.CAT_GET_ALL,
            PS.CAT_GET_ONE
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

    private Article getArticle(int id, int truncate) {
        try {
            PreparedStatement ps;
            if (truncate <= 0) {
                ps = dao.getPreparedStatement(PS.ART_GET_ONE);
                ps.setInt(1, id);
            } else {
                ps = dao.getPreparedStatement(PS.ART_GET_ONE_TRUNC);
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
        }
        return null;
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

    private Article[] getArticles(int truncate, int limit, int starts) {
        try {
            PreparedStatement useStatement;
            // Means all
            if (limit == 0 && starts == 0) {
                // OK This should be invalid, wait no, this is for GET ALL
                if (truncate > 0) {
                    useStatement = dao.getPreparedStatement(PS.ART_GET_ALL_TRUNC);
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                } else {
                    useStatement = dao.getPreparedStatement(PS.ART_GET_ALL);
                }
            } else {
                if (limit <= 0 || starts <= 0) {
                    log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                    return null;
                } else {
                    int limitParamNum = 1;
                    int startsParamNum = 2;
                    if (truncate > 0) {
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_TRUNC_BY_EDITED_RANGE);
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        limitParamNum = 3;
                        startsParamNum = 4;
                    } else {
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_BY_EDITED_RANGE);
                    }
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


    private Article[] getArticlesByCat(int truncate, int catId, int limit, int starts) {
        try {
            PreparedStatement useStatement;
            if (limit == 0 && starts == 0) {
                // OK This should be invalid, wait no, this is for GET ALL
                if (truncate > 0) {
                    useStatement = dao.getPreparedStatement(PS.ART_GET_ALL_TRUNC_BY_CAT);
                    useStatement.setInt(1, truncate);
                    useStatement.setInt(2, truncate);
                    useStatement.setInt(3, catId);
                } else {
                    useStatement = dao.getPreparedStatement(PS.ART_GET_ALL_BY_CAT);
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
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_TRUNC_BY_CAT_EDITED_RANGE);
                        useStatement.setInt(1, truncate);
                        useStatement.setInt(2, truncate);
                        catIdParamNum = 3;
                        limitParamNum = 4;
                        startsParamNum = 5;
                    } else {
                        useStatement = dao.getPreparedStatement(PS.ART_GET_SOME_BY_CAT_EDITED_RANGE);
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

    private Article[] searchArticles(int truncate, String search, int limit, int starts) {
        if (truncate <= 0) {
            // Already truncating, reassign it to a massive number
            truncate = Integer.MAX_VALUE;
        }
        try {
            if (limit == 0 && starts == 0) {
                // Just set limit to high number and starts to 1
                limit = Integer.MAX_VALUE;
                starts = 1;
            }
            if (limit <= 0 || starts <= 0) {
                log.warning("The servlet " + getServletName() + " has had a bruh moment.");
                return null;
            }
            PreparedStatement ps = dao.getPreparedStatement(PS.ART_GET_SOME_BY_MATCHING);
            // truncate, truncate, matching, matching, limit, offset (start - 1)
            ps.setInt(1, truncate);
            ps.setInt(2, truncate);
            ps.setString(3, search);
            ps.setString(4, search);
            ps.setInt(5, limit);
            ps.setInt(6, starts - 1);
            ResultSet out = ps.executeQuery();
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

    private int countSearchArticles(String search) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.ART_GET_COUNT_BY_MATCHING);
            ps.setString(1, search);
            ResultSet out = ps.executeQuery();
            if (!out.next()) {
                log.severe("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            } else {
                return out.getInt("count");
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets the categories from the database.
     * @return The categories. Null if there was an error. Empty array if there are no categories.
     */
    private Category[] getCategories() {
        // No fancy shit at this point, just get categories and leave
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.CAT_GET_ALL);
            ResultSet out = ps.executeQuery();
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

    private int countArticles() {
        try (ResultSet out = dao.getPreparedStatement(PS.ART_GET_COUNT).executeQuery()) {
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.severe("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    private int countArticlesByCat(int catId) {
        try {
            PreparedStatement ps = dao.getPreparedStatement(PS.ART_GET_COUNT_BY_CAT);
            ps.setInt(1, catId);
            ResultSet out = ps.executeQuery();
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.severe("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    private int countCategories() {
        try (ResultSet out = dao.getPreparedStatement(PS.CAT_GET_COUNT).executeQuery()) {
            if (out.next()) {
                return out.getInt("count");
            } else {
                log.severe("The servlet " + getServletName() + " has had a bruh moment.");
                return -1;
            }
        } catch (SQLException e) {
            log.severe("The servlet " + getServletName() + " has had a SQLException.");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Look, using request parameters is a funny way to do this, but I'm not sure if there is any other way
     * to pass variables back into the included servlet.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("categoryId") != null) {
            try {
                int catId = Integer.parseInt(request.getParameter("categoryId"));
                int show = Integer.parseInt(request.getParameter("show"));
                int page = Integer.parseInt(request.getParameter("page"));
                int truncate = 0;
                if (request.getParameter("truncate") != null) {
                    try {
                        truncate = Integer.parseInt(request.getParameter("truncate"));
                    } catch (NumberFormatException e) {
                        log.warning("The servlet " + getServletName() + " has received a GET but the 'truncate' parameter is not a number.");
                    }
                }
                // Check if category even exists
                Category cat = getCategory(catId);
                if (cat != null) {
                    // Show articles
                    int maxPage = Math.floorDiv(countArticlesByCat(catId), show) + 1;
                    Article[] articles = getArticlesByCat(truncate, catId, show, (page * show) - (show - 1));
                    if (articles != null && articles.length > 0) {
                        request.setAttribute("category", cat);
                        request.setAttribute("maxPage", maxPage);
                        request.setAttribute("articles", articles);
                        return;
                    } else {
                        request.setAttribute("category", cat);
                        request.setAttribute("maxPage", maxPage);
                        return;
                    }
                } else {
                    log.warning("The servlet " + getServletName() + " has received a GET but the 'categoryId' parameter is not a valid category.");
                }
            } catch (NumberFormatException e) {
                log.warning("The servlet " + getServletName() + " has received a GET but the 'categoryId' and/or 'show' parameter are not numbers.");
                return;
            }
        } else if (request.getParameter("showCategories") != null) {
            // Don't care, as long as it exists
            // Show and page are kinda mandatory at this point
            try {
                int show = Integer.parseInt(request.getParameter("show"));
                int page = Integer.parseInt(request.getParameter("page"));
                // Ok fuck it, 50 gorillion SQL queries in one call
                // 1. Get all cats, 2. Count cats, 3. Split them in pages, 4. Fetch article count, 5, Get most recent article, 6. ??? 6, Profit
                Category[] cats = getCategories();
                if (cats != null) {
                    int catCount = countCategories();
                    // Do the wucky fucky calculation stuff
                    int maxPage = Math.floorDiv(catCount, show) + 1;
                    int limit = page * show;
                    if (limit > catCount) {
                        limit = catCount;
                    }
                    // limit not go over cat count, if cat count g
                    // I could make a check for the start index but meh
                    Category[] catsPage = Arrays.copyOfRange(cats, ((show * page)  - show), limit);
                    int[] artCounts = new int[catsPage.length];
                    Article[] recentArticles = new Article[cats.length];
                    for (int i = 0; i < catsPage.length; i++) {
                        artCounts[i] = countArticlesByCat(catsPage[i].getId());
                        // Truncating takes too much time lel
                        Article[] recentArticle =  getArticlesByCat(0, catsPage[i].getId(), 1, 1);
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
                    request.setAttribute("recentArticles", recentArticles);
                } else {
                    // Boo
                    return;
                }
            } catch (NumberFormatException e) {
                return;
            }
            return;
        } if (request.getParameter("show") != null) {
            int truncate = 0;
            if (request.getParameter("truncate") != null) {
                try {
                    truncate = Integer.parseInt(request.getParameter("truncate"));
                } catch (NumberFormatException e) {
                    log.warning("The servlet " + getServletName() + " has received a GET but the 'truncate' parameter is not a number.");
                    // Keep going with truncate being zero
                }
            }
            if (request.getParameter("search") != null) {
                // Search articles
                String search = Objects.requireNonNullElse(request.getParameter("search"), "");
                int show = 0;
                int page = 0;
                try {
                    if (request.getParameter("show") != null) {
                        show = Integer.parseInt(request.getParameter("show"));
                    }
                    if (request.getParameter("page") != null) {
                        page = Integer.parseInt(request.getParameter("page"));
                    }
                } catch (NumberFormatException e) {
                    log.warning("The servlet " + getServletName() + " has received a GET but the 'show' or 'page' parameter is not a number.");
                    // Keep going with whatever values we have
                }
                int maxPage = Math.floorDiv(countSearchArticles(search), show) + 1;
                Article[] articles = searchArticles(truncate, search, show, (page * show) - (show - 1));
                if (articles != null && articles.length > 0) {
                    request.setAttribute("maxPage", maxPage);
                    request.setAttribute("articles", articles);
                    return;
                } else {
                    request.setAttribute("maxPage", maxPage);
                    log.warning("The servlet " + getServletName() + " has received a GET but no article has been returned.");
                    return;
                }
            } else if (request.getParameter("show").equals("all")) {
                // Show all articles
                request.setAttribute("articles", getArticles(truncate, 0, 0));
            } else {
                // Check if the show parameter is a number
                try {
                    int show = Integer.parseInt(request.getParameter("show"));
                    if (show > 0) {
                        // Show a number of articles
                        if (request.getParameter("page") != null) {
                            // Check if the page parameter is a number
                            try {
                                int page = Integer.parseInt(request.getParameter("page"));
                                if (page > 0) {
                                    // Show a number of articles from a page
                                    Article[] articles = getArticles(truncate, show, (page * show) - (show - 1));
                                    int maxPage = Math.floorDiv(countArticles(), show) + 1;
                                    if (articles != null && articles.length > 0) {
                                        request.setAttribute("maxPage", maxPage);
                                        request.setAttribute("articles", articles);
                                        return;
                                    } else {
                                        request.setAttribute("maxPage", maxPage);
                                        log.warning("The servlet " + getServletName() + " has received a GET but no article has been returned.");
                                        return;
                                    }
                                } else {
                                    log.warning("The servlet " + getServletName() + " has received a GET but the 'page' parameter is not a positive number.");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                log.warning("The servlet " + getServletName() + " has received a GET but the 'page' parameter is not a number.");
                                return;
                            }
                        } else {
                            // Show without offset
                            int maxPage = Math.floorDiv(countArticles(), show) + 1;
                            Article[] articles = getArticles(truncate, show, 1);
                            if (articles != null && articles.length > 0) {
                                request.setAttribute("maxPage", maxPage);
                                request.setAttribute("articles", articles);
                                return;
                            } else {
                                request.setAttribute("maxPage", maxPage);
                                log.warning("The servlet " + getServletName() + " has received a GET but no article has been returned.");
                                return;
                            }
                        }
                    } else {
                        log.warning("The servlet " + getServletName() + " has received a GET but the 'show' parameter is not a positive number.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    log.warning("The servlet " + getServletName() + " has received a GET but the 'show' parameter is not a number.");
                    return;
                }
            }
        } else if (request.getParameter("articleId") != null) {
            try {
                int id = Integer.parseInt(request.getParameter("articleId"));
                int truncate = 0;
                if (request.getParameter("truncate") != null) {
                    try {
                        truncate = Integer.parseInt(request.getParameter("truncate"));
                    } catch (NumberFormatException e) {
                        log.warning("The servlet " + getServletName() + " has received a GET but the 'truncate' parameter is not a number.");
                    }
                }
                // Show a single article
                request.setAttribute("article", getArticle(id, truncate));
                return;
            } catch (NumberFormatException e) {
                log.warning("The servlet " + getServletName() + " has received a GET but the 'articleId' parameter is not a number.");
                return;
            }
        } else {
            log.warning("The servlet " + getServletName() + " has received a GET but hit none of the ifs for some funny reason.");
        }
    }
}
