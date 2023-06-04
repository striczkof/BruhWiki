<%--
  Created by IntelliJ IDEA.
  User: striczkof
  Date: 03/06/2023
  Time: 23:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
  <!-- get session and user if exists -->
  <c:if test="${empty sessionScope.user}">
    <jsp:forward page="login.jsp"/>
  </c:if>
  <head>
    <meta charset="UTF-8"/>
    <title>Bruh Wiki - <c:out value="${sessionScope.user.name}"/></title>
    <link rel="stylesheet" href="styles.css" type="text/css"/>
    <script src="js/login-register.js" type="application/javascript"></script>
  </head>
  <body>
    <h1>hello world</h1>
  </body>
</html>
