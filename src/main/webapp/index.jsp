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
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>Bruh Wiki</title>
        <link rel="stylesheet" href="styles.css" type="text/css">
    </head>
    <body>
        <div class="main main--big">
            <div class="title">
                <h1 class="title-header">Bruh Wiki</h1>
            </div>
            <div class="sidebar">
                <ul class="sidebar-list">
                    <!-- Check if user is logged in -->
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <li class="sidebar-list__item sidebar-list__item--user">
                                <p>Hi! You're not logged in.</p>
                                <p><a href="login.jsp">Login</a> or <a href="register.jsp">register</a></p>
                                <form class="search" action="/article-servlet" method="get">
                                    <label hidden="hidden" for="search">Search</label>
                                    <input class="search__input" type="text" name="search" id="search" placeholder="Search" required/>
                                    <button hidden="hidden" type="submit">Search</button>
                                </form>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <!-- If user's name is null, use their username as their name -->
                            <!-- Wait a minute, I can just do that in the bean -->
                            <li class="sidebar-list__item sidebar-list__user">
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
                        <a class="nav-item active">Home</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item" href="about.jsp">About</a>
                    </li>
                </ul>
            </div>
            <div class="content">
                <div class="content-main">
                    <c:if test="${empty pageNumber}">
                        <c:set var="pageNumber" scope="page" value="${1}"/>
                    </c:if>
                    <ul>
                        <jsp:include page="/article-servlet">
                            <jsp:param name="pageNumber" value="${pageNumber}"/>
                            <jsp:param name="show" value="all"/>
                            <jsp:param name="truncate" value="300"/>
                        </jsp:include>
                        <c:forEach var = "i" begin = "0" end = "3">
                            <li><c:out value="${articles[i].getContent()}"/></li>
                        </c:forEach>

                    </ul>
                </div>
                <div class="content-footer">
                    <p class="page-number"></p>
                </div>
            </div>
        </div>
    </body>
</html>