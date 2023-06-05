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
<c:choose>
    <c:when test="${not empty param.id}">
        <jsp:include page="/article-servlet">
            <jsp:param name="categoryId" value="${param.id}"/>
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
        </jsp:include>
        <c:set var="title" value="Categories"/>
    </c:otherwise>
</c:choose>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>Bruh Wiki - <c:out value="${title}"/></title>
    <link rel="stylesheet" href="styles.css" type="text/css">
</head>
<body>

</body>
</html>
