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
        <!-- Check if user is logged in -->
        <c:if test="${not empty sessionScope.user}">
            <jsp:useBean id="user" class="striczkof.bruh_wiki.model.User"/>
        </c:if>
        <div class="main">
            <div class="title">
                <h1 class="title-header">Bruh Wiki</h1>
            </div>
            <div class="sidebar">
                <ul class="sidebar-list">
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="about.jsp">About</a></li>
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
                            <jsp:param name="truncate" value="100"/>
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