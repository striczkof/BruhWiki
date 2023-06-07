<%--
  Created by IntelliJ IDEA.
  User: striczkof
  Date: 04/06/2023
  Time: 18:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!-- Set default values -->
<!DOCTYPE html>
<html>
    <c:choose>
        <c:when test="${empty param.page or param.page <= 0}">
            <!-- Enter default page values here -->
            <c:set var="page" scope="page" value="${1}"/>
        </c:when>
        <c:otherwise>
            <c:set var="page" scope="page" value="${param.page}"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${empty param.show or param.page <= 0}">
            <!-- Enter default page values here -->
            <c:set var="show" scope="page" value="${10}"/>
        </c:when>
        <c:otherwise>
            <c:set var="show" scope="page" value="${param.show}"/>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${not empty param.id}">
            <!-- Show articles in category -->
            <jsp:include page="/article-servlet">
                <jsp:param name="categoryId" value="${param.id}"/>
                <jsp:param name="show" value="${pageScope.show}"/>
                <jsp:param name="page" value="${pageScope.page}"/>
                <jsp:param name="truncate" value="${300}"/>
            </jsp:include>
            <c:choose>
                <c:when test="${not empty requestScope.category}">
                    <c:set var="title" value="${requestScope.category.name} Category"/>
                </c:when>
                <c:otherwise>
                    <jsp:forward page="index.jsp"/>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <jsp:include page="/article-servlet">
                <jsp:param name="showCategories" value="${true}"/>
                <jsp:param name="show" value="${pageScope.show}"/>
                <jsp:param name="page" value="${pageScope.page}"/>
            </jsp:include>
            <c:set var="title" value="Categories"/>
        </c:otherwise>
    </c:choose>
    <c:set var="maxPage" scope="page" value="${requestScope.maxPage}"/>
    <head>
        <meta charset="UTF-8"/>
        <title><c:out value="${title}"/> - <c:out value="${pageContext.servletContext.servletContextName}"/></title>
        <link rel="stylesheet" href="styles.css" type="text/css">
    </head>
    <body>
        <div class="main main--big">
            <div class="title">
                <h1 class="title-header"><c:out value="${pageContext.servletContext.servletContextName}"/></h1>
            </div>
            <div class="sidebar">
                <ul class="sidebar-list">
                    <!-- Check if user is logged in -->
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <li class="sidebar-list__item sidebar-list__item--user">
                                <p>Hi! You're not logged in.</p>
                                <p><a href="login.jsp">Login</a> or <a href="register.jsp">register</a></p>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <!-- If user's name is null, use their username as their name -->
                            <!-- Wait a minute, I can just do that in the bean -->
                            <li class="sidebar-list__item sidebar-list__item--user">
                                <p>Hi <c:out value="${sessionScope.user.name}!"/></p>
                                <p><a href="user.jsp">Edit user</a> | <a href="user-servlet?logout">Logout</a></p>
                            </li>
                            <c:if test="${sessionScope.user.admin}">
                                <li class="sidebar-list__item">
                                    <a class="nav-item" href="admin.jsp">Admin Panel</a>
                                </li>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                    <li class="sidebar-list__item">
                        <a class="nav-item" href="index.jsp">Home</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item active" href="categories.jsp">Categories</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item" href="articles.jsp">Articles</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item" href="about.jsp">About</a>
                    </li>
                </ul>
            </div>
            <div class="content">
                <c:choose>
                    <c:when test="${not empty param.id}">
                        <div class="content__main">
                            <h2 class="content-header"><c:out value="${title}"/></h2>
                            <!-- Must be included before -->
                            <ul class="article-list">
                                <c:forEach items="${requestScope.articles}" var = "article">
                                    <li class="article-list__item article">
                                        <h3 class="article__title"><c:out value="${article.title}"/></h3>
                                        <p class="article__subtitle">In <a href="categories.jsp?id=${article.categoryId}"><c:out value="${article.categoryName}"/></a> last edited on <fmt:formatDate type="date" value="${article.lastEditedDate}"/></p>
                                        <p class="article__body"><c:out value="${article.content}"/> <a href="articles.jsp?id=${article.id}">Read more >>></a> </p>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="content__main">
                            <h2 class="content-header">All Categories</h2>
                            <!-- Must be included before -->
                            <ul class="article-list">
                                <c:forEach var = "i" begin="0" end="${requestScope.maxShow}">
                                    <li class="article-list__item--small article ">
                                        <h4 class="article__title"><a href="categories.jsp?id=${requestScope.categories[i].id}"><c:out value="${requestScope.categories[i].name}"/></a></h4>
                                        <c:choose>
                                            <c:when test="${requestScope.articleCounts[i] eq 0}">
                                                <p class="article__subtitle">Has no articles</p>
                                            </c:when>
                                            <c:otherwise>
                                                <p class="article__subtitle">Has: ${requestScope.articleCounts[i]}, latest article: <a href="articles.jsp?id=${requestScope.recentArticles[i].id}">${requestScope.recentArticles[i].title}</a></p>
                                            </c:otherwise>
                                        </c:choose>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="content__footer">
                    <c:if test="${not empty param.id}">
                        <c:set var="idParam" scope="page" value="&id=${param.id}"/>
                    </c:if>
                    <c:if test="${not empty param.show}">
                        <c:set var="showParam" scope="page" value="&show=${param.show}"/>
                    </c:if>
                    <c:choose>
                        <c:when test="${pageScope.page == 1 and pageScope.maxPage == 1}">
                            <p class="page-number"><< < 1 > >></p>
                        </c:when>
                        <c:when test="${pageScope.page == 1}">
                            <p class="page-number"><< < 1 <a href="categories.jsp?page=2${pageScope.idParam}${pageScope.showParam}">></a> <a href="categories.jsp?page=${requestScope.maxPage}${pageScope.idParam}${pageScope.showParam}">>></a></p>
                        </c:when>
                        <c:when test="${pageScope.page == requestScope.maxPage}">
                            <p class="page-number"><a href="categories.jsp?page=1${pageScope.idParam}${pageScope.showParam}"><<</a> <a href="categories.jsp?page=${pageScope.page - 1}${pageScope.idParam}${pageScope.showParam}"><</a> ${pageScope.page} > >></p>
                        </c:when>
                        <c:otherwise>
                            <p class="page-number"><a href="categories.jsp?page=1${pageScope.idParam}${pageScope.showParam}"><<</a> <a href="categories.jsp?page=${pageScope.page - 1}${pageScope.idParam}${pageScope.showParam}"><</a> ${pageScope.page} <a href="categories.jsp?page=${pageScope.page + 1}${pageScope.idParam}${pageScope.showParam}">></a> <a href="categories.jsp?page=${requestScope.maxPage}${pageScope.idParam}${pageScope.showParam}">>></a></p>
                        </c:otherwise>
                    </c:choose>
                    <p class="copyright">Copyright © 2023 Alvin. This is a free software released under the <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">GNU Affero General Public License v3.0.</a></p>
                    <p class="disclaimer">All articles and categories are AI-generated. It is safe to assume that they are false. <br/> All source code is contained in <a href="https://github.com/striczkof/BruhWiki">this GitHub repository</a>.</p>
                </div>
            </div>
        </div>
    </body>
</html>
