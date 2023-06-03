<%--
  Created by IntelliJ IDEA.
  User: striczkof
  Date: 30/05/2023
  Time: 20:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8"/>
    <title>Bruh Wiki - Login</title>
    <link rel="stylesheet" href="styles.css" type="text/css"/>
    <script src="js/login-register.js" type="application/javascript"></script>
  </head>
  <body>
    <!-- Check if user is logged in -->
    <c:if test="${not empty sessionScope.user}">
      <c:if test="${empty param.result}">
        <c:redirect url="index.jsp"/>
      </c:if>
    </c:if>
    <div class="main--small">
      <div class="title">
        <h1 class="title-header">Bruh Wiki</h1>
      </div>
      <div class="main-content">
        <form class="login-register-form" action="${pageContext.request.contextPath}/user-servlet" method="post">
          <!-- Temporary register -->
          <ul class="login-register-form-list">
            <li>
              <h2>Login</h2>
            </li>
            <li class="form-list-label">
              <label for="username">Username</label>
            </li>
            <li class="form-list-input">
              <input type="text" name="username" id="username" placeholder="Username" required/>
            </li>
            <li class="form-list-label">
              <label for="password">Password</label>
            </li>
            <li class="form-list-input">
              <input type="password" name="password" id="password" placeholder="Password" required/>
            </li>
            <li class="form-list-input">
              <!-- This is a cheap hack to let the post request know that this is a registration -->
              <input type="submit" name="login" value="Submit"/>
            </li>
            <li class="form-list-input">
              <p><a id="footer-back-link" href="index.jsp">Go back</a> | <a href="index.jsp">Home</a> | <a href="register.jsp">Register here</a></p>
            </li>
          </ul>
        </form>
        <!-- Result box goes here -->
        <c:if test="${not empty param.result}">
          <c:choose>
            <c:when test="${param.result eq 'success'}">
              <div class="result-box result-box--success">
                <p>You are now logged in!</p>
                <p>Redirecting to the <a href="index.jsp">index page</a> in 5 seconds...</p>
              </div>
            </c:when>
            <c:when test="${param.result eq 'user-not-found'}">
              <div class="result-box result-box--failure">
                <p>Login failed!</p>
                <p>Username not found.</p>
              </div>
            </c:when>
            <c:when test="${param.result eq 'wrong-pass'}">
              <div class="result-box result-box--failure">
                <p>Login failed!</p>
                <p>Wrong password.</p>
              </div>
            </c:when>
            <c:when test="${param.result eq 'sql-error'}">
              <div class="result-box result-box--failure">
                <p>Login failed!</p>
                <p>Internal server error.</p>
              </div>
            </c:when>
          </c:choose>
        </c:if>
      </div>
    </div>
  </body>
</html>
