<%--
  Created by IntelliJ IDEA.
  User: striczkof
  Date: 03/06/2023
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <!-- test if user is really admin -->
    <c:if test="${empty sessionScope.user or !sessionScope.user.admin}">
        <jsp:forward page="login.jsp"/>
    </c:if>
    <head>
        <title>Title</title>
    </head>
    <body>

    </body>
</html>
