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
    <c:if test="${param.change ne 'delete' and param.result ne 'success'}">
      <jsp:forward page="login.jsp"/>
    </c:if>
    <c:set var="name" scope="page" value="(deleted)"/>
  </c:if>
  <c:set var="name" scope="page" value="${sessionScope.user.name}"/>
  <head>
    <meta charset="UTF-8"/>
    <title><c:out value="${pageScope.name}"/> - <c:out value="${pageContext.servletContext.servletContextName}"/></title>
    <link rel="stylesheet" href="styles.css" type="text/css"/>
    <script src="js/login-register.js" type="application/javascript"></script>
  </head>
  <body>
    <div class="main main--small">
      <div class="title">
        <h1 class="title-header"><c:out value="${pageContext.servletContext.servletContextName}"/></h1>
      </div>
      <div class="main-content">
        <c:choose>
          <c:when test="${not empty param.change}">
            <c:choose>
              <c:when test="${param.change eq 'name'}">
                <form class="login-register-form" id="change-name-form" action="${pageContext.request.contextPath}/user-servlet" method="post">
                  <ul class="login-register-form-list">
                    <li>
                      <h2>Change your name</h2>
                    </li>
                    <li>
                      <h3>User: <c:out value="${pageScope.name}"/></h3>
                    </li>
                    <li class="form-list-label">
                      <label for="username">Username (required)</label>
                    </li>
                    <li class="form-list-input">
                      <input type="text" name="username" id="username" placeholder="Username"/>
                    </li>
                    <li class="form-list-label">
                      <label for="name">Name</label>
                    </li>
                    <li class="form-list-input">
                      <input type="text" name="name" id="name" placeholder="Name"/>
                    </li>
                    <li class="form-list-input">
                      <button class="login-register-form__button--big" type="submit" name="user-change" value="name">Change</button>
                    </li>
                    <li class="form-list-input">
                      <p><a class="footer-back-link" href="index.jsp">Go back</a> | <a href="index.jsp">Home</a> | <a href="user-servlet?logout">Logout</a></p>
                    </li>
                  </ul>
                </form>
              </c:when>
              <c:when test="${param.change eq 'pass'}">
                <form class="login-register-form" id="change-pass-form" action="${pageContext.request.contextPath}/user-servlet" method="post">
                  <ul class="login-register-form-list">
                    <li>
                      <h2>Change your password</h2>
                    </li>
                    <li>
                      <h3>User: <c:out value="${pageScope.name}"/></h3>
                    </li>
                    <li class="form-list-label">
                      <label for="password">Password (required)</label>
                    </li>
                    <li class="form-list-input form-list-input--password">
                      <input type="password" name="password" id="password" placeholder="Password" required/>
                    </li>
                    <li class="form-list-label">
                      <label for="old-password">Old Password (required)</label>
                    </li>
                    <li class="form-list-input">
                      <input type="password" name="old-password" id="old-password" placeholder="Password" required/>
                    </li>
                    <li class="form-list-input">
                      <button class="login-register-form__button--big" type="submit" name="user-change" value="pass">Change</button>
                    </li>
                    <li class="form-list-input">
                      <p><a class="footer-back-link" href="index.jsp">Go back</a> | <a href="index.jsp">Home</a> | <a href="user-servlet?logout">Logout</a></p>
                    </li>
                  </ul>
                </form>
              </c:when>
              <c:when test="${param.change eq 'delete'}">
                <form class="login-register-form" id="delete-user-form" action="${pageContext.request.contextPath}/user-servlet" method="post">
                  <ul class="login-register-form-list">
                    <li>
                      <h2>Delete your account</h2>
                    </li>
                    <li>
                      <h3>User: <c:out value="${pageScope.name}"/></h3>
                    </li>
                    <li class="form-list-label">
                      <label for="password-del">Password (required)</label>
                    </li>
                    <li class="form-list-input form-list-input--password">
                      <input type="password" name="password-del" id="password-del" placeholder="Password" required/>
                    </li>
                    <li class="form-list-label">
                      <label for="delete">Are you sure?</label>
                    </li>
                    <li class="form-list-input">
                      <input type="checkbox" id="delete" name="delete" value="Boat"/>
                    </li>
                    <li class="form-list-input">
                      <button class="login-register-form__button--big" type="submit" name="user-change" value="pass">Change</button>
                    </li>
                    <li class="form-list-input">
                      <p><a class="footer-back-link" href="index.jsp">Go back</a> | <a href="index.jsp">Home</a> | <a href="user-servlet?logout">Logout</a></p>
                    </li>
                  </ul>
                </form>
              </c:when>
              <c:otherwise>
                <!-- INVALID PARAMETER -->
                <c:redirect url="user.jsp"/>
              </c:otherwise>
            </c:choose>

          </c:when>
          <c:otherwise>
            <form hidden="hidden" name="hidden-form" id="hidden-form" action="user.jsp" method="get"></form>
            <ul class="login-register-form-list">
              <li class="form-list-input">
                <h2 class="form-list-header">Welcome, <c:out value="${pageScope.name}"/>!</h2>
              </li>
              <li class="form-list-input">
                <button class="login-register-form__button--big" type="submit" form="hidden-form" formtarget="user.jsp" name="change" value="name" formmethod="get">Change username and/or name</button>
              </li>
              <li class="form-list-input">
                <button class="login-register-form__button--big" type="submit" form="hidden-form" formtarget="user.jsp" name="change" value="pass" formmethod="get">Chane password</button>
              </li>
              <li class="form-list-input">
                <button class="login-register-form__button--big" type="submit" form="hidden-form" formtarget="user.jsp" name="change" value="delete" formmethod="get">Delete account</button>
              </li>
              <li class="form-list-input">
                <p><a class="footer-back-link" href="index.jsp">Go back</a> | <a href="index.jsp">Home</a> | <a href="user-servlet?logout">Logout</a></p>
              </li>
            </ul>
          </c:otherwise>
        </c:choose>

        <!-- Result box goes here -->
        <c:if test="${not empty param.result}">
          <c:choose>
            <c:when test="${param.result eq 'success'}">
              <div class="result-box result-box--success">
                <p>Change successful!</p>
                <p>Redirecting to the <a href="index.jsp">index page</a> in 5 seconds...</p>
              </div>
            </c:when>
            <c:when test="${param.result eq 'no-change'}">
              <div class="result-box result-box--warn">
                <p>No change happened.</p>
                <p>Either you put in the same info or something happened in the backend.</p>
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
