<%--
  Created by IntelliJ IDEA.
  User: striczkof
  Date: 04/06/2023
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>About - <c:out value="${pageContext.servletContext.servletContextName}"/></title>
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
                        <a class="nav-item" href="categories.jsp">Categories</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item" href="articles.jsp">Articles</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item active">About</a>
                    </li>
                </ul>
            </div>
            <div class="content">
                <div class="content__main">
                    <h2 class="content-header">About</h2>
                    <p>HI! I gotta finish this later xD</p>
                </div>
                <div class="content__footer">
                    <p class="copyright">Copyright © 2023 Alvin. This is a free software released under the <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">GNU Affero General Public License v3.0.</a></p>
                    <p class="disclaimer">All articles and categories are AI-generated. It is safe to assume that they are false. <br/> All source code is contained in <a href="https://github.com/striczkof/BruhWiki">this GitHub repository</a>.</p>
                </div>
            </div>
        </div>
    </body>
</html>
