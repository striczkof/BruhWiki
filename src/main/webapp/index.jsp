<%--
A trash attempt to create a wiki from scratch using JSP, HTML, CSS, and JS.
Copyright (C) 2023 Alvin

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
--%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>Home - <c:out value="${pageContext.servletContext.servletContextName}"/></title>
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
                        <a class="nav-item active" href="index.jsp">Home</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item" href="categories.jsp">Categories</a>
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
                <div class="content__main">
                    <h2 class="content-header">Recent Articles</h2>
                    <ul class="article-list">
                        <!-- Set how many articles to show -->
                        <c:set var="show" scope="page" value="${3}"/>
                        <jsp:include page="/article-servlet">
                            <jsp:param name="show" value="${pageScope.show}"/>
                            <jsp:param name="truncate" value="500"/>
                        </jsp:include>

                        <c:forEach items="${requestScope.articles}" var = "article" begin = "0" end = "${pageScope.show - 1}">
                            <li class="article-list__item article">
                                <h3 class="article__title"><c:out value="${article.title}"/></h3>
                                <p class="article__subtitle">In <a href="categories.jsp?id=<c:out value="${article.categoryId}"/>"><c:out value="${article.categoryName}"/></a> last edited on <fmt:formatDate type="date" value="${article.lastEditedDate}"/></p>
                                <p class="article__body"><c:out value="${article.content}"/> <a href="articles.jsp?id=<c:out value="${article.id}"/>">Read more >>></a> </p>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
                <div class="content__footer">
                    <p class="copyright">Copyright © 2023 Alvin. This is a free software released under the <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">GNU Affero General Public License v3.0.</a></p>
                    <p class="disclaimer">All articles and categories are AI-generated. It is safe to assume that they are false. <br/> All source code is contained in <a href="https://github.com/striczkof/BruhWiki">this GitHub repository</a>.</p>
                </div>
            </div>
        </div>
    </body>
</html>