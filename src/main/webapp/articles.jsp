<%--
  Created by IntelliJ IDEA.
  User: striczkof
  Date: 04/06/2023
  Time: 09:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
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
      <c:set var="show" scope="page" value="${5}"/>
    </c:when>
    <c:otherwise>
      <c:set var="show" scope="page" value="${param.show}"/>
    </c:otherwise>
  </c:choose>
  <c:choose>
    <c:when test="${not empty param.id}">
      <jsp:include page="/article-servlet">
        <jsp:param name="articleId" value="${param.id}"/>
      </jsp:include>
      <c:if test="${empty requestScope.article}">
        <!-- Not found -->
        <jsp:forward page="index.jsp"/>
      </c:if>
      <c:set var="title" value="${requestScope.article.title}"/>
    </c:when>
    <c:when test="${not empty requestScope.search}">
      <c:set var="title" value="Search Result for '${requestScope.search}'"/>
      <jsp:include page="/article-servlet">
        <jsp:param name="search" value="${requestScope.search}"/>
        <jsp:param name="page" value="${pageScope.page}"/>
        <jsp:param name="show" value="${pageScope.show}"/>
        <jsp:param name="truncate" value="300"/>
      </jsp:include>
    </c:when>
    <c:otherwise>
      <c:set var="title" value="All Articles"/>
      <jsp:include page="/article-servlet">
        <jsp:param name="page" value="${pageScope.page}"/>
        <jsp:param name="show" value="${pageScope.show}"/>
        <jsp:param name="truncate" value="300"/>
      </jsp:include>
      <c:set var="maxPage" scope="page" value="${requestScope.maxPage}"/>
    </c:otherwise>
  </c:choose>
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
            <a class="nav-item" href="categories.jsp">Categories</a>
          </li>
          <li class="sidebar-list__item">
            <a class="nav-item active" href="articles.jsp">Articles</a>
          </li>
          <li class="sidebar-list__item">
            <a class="nav-item" href="about.jsp">About</a>
          </li>
        </ul>
      </div>
      <div class="content">
        <c:choose>
          <c:when test="${not empty param.id}">
            <div class="content__main article">
              <!-- Should have been called already earlier -->
              <h2 class="content-header"><c:out value="${requestScope.article.title}"/></h2>
              <p class="article__subtitle">In <a href="categories.jsp?id=<c:out value="${requestScope.article.categoryId}"/>"><c:out value="${requestScope.article.categoryName}"/></a> last edited on <fmt:formatDate type="date" value="${requestScope.article.lastEditedDate}"/></p>
              <p class="article__body"><c:out value="${requestScope.article.content}"/></p>
            </div>
          </c:when>
          <c:otherwise>
          <div class="content__main">
            <c:if test="${not empty param.search}">
              <c:set var="title" scope="page" value="${requestScope.resultCount} results for '${param.search}'"/>
            </c:if>
            <h2 class="content-header"><c:out value="${title}"/></h2>
            <form class="search" action="articles.jsp" method="get"><label for="search">Search: </label><input type="search" name="search" id="search" value="${param.search}" required/><input type="submit" value="Go!"/></form>
            <!-- Must be included before -->
            <ul class="article-list">
              <c:forEach items="${requestScope.articles}" var = "article">
                <li class="article-list__item article">
                  <h4 class="article__title"><c:out value="${article.title}"/></h4>
                  <p class="article__subtitle">In <a href="categories.jsp?id=${article.categoryId}"><c:out value="${article.categoryName}"/></a> last edited on <fmt:formatDate type="date" value="${article.lastEditedDate}"/></p>
                  <p class="article__body"><c:out value="${article.content}"/> <a href="articles.jsp?id=${article.id}">Read more >>></a> </p>
                </li>
              </c:forEach>
            </ul>
          </div>
          </c:otherwise>
        </c:choose>
        <div class="content__footer">
          <c:if test="${not empty param.search}">
            <c:set var="searchParam" scope="page" value="&search=${param.search}"/>
          </c:if>
          <c:if test="${not empty param.show}">
            <c:set var="show" scope="page" value="&show=${param.show}"/>
          </c:if>
          <c:if test="${empty param.id}">
            <c:choose>
              <c:when test="${pageScope.page == 1 and pageScope.maxPage == 1}">
                <p class="page-number"><< < 1 > >></p>
              </c:when>
              <c:when test="${pageScope.page == 1}">
                <p class="page-number"><< < 1 <a href="articles.jsp?page=2${pageScope.searchParam}">></a> <a href="articles.jsp?page=${requestScope.maxPage}${pageScope.searchParam}">>></a></p>
              </c:when>
              <c:when test="${pageScope.page == requestScope.maxPage}">
                <p class="page-number"><a href="articles.jsp?page=1${pageScope.searchParam}"><<</a> <a href="articles.jsp?page=${pageScope.page - 1}${pageScope.searchParam}"><</a> ${pageScope.page} > >></p>
              </c:when>
              <c:otherwise>
                <p class="page-number"><a href="articles.jsp?page=1${pageScope.searchParam}"><<</a> <a href="articles.jsp?page=${pageScope.page - 1}${pageScope.searchParam}"><</a> ${pageScope.page} <a href="articles.jsp?page=${pageScope.page + 1}${pageScope.searchParam}">></a> <a href="articles.jsp?page=${requestScope.maxPage}${pageScope.searchParam}">>></a></p>
              </c:otherwise>
            </c:choose>
          </c:if>
          <p class="copyright">Copyright © 2023 Alvin. This is a free software released under the <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">GNU Affero General Public License v3.0.</a></p>
          <p class="disclaimer">All articles and categories are AI-generated. It is safe to assume that they are false. <br/> All source code is contained in <a href="https://github.com/striczkof/BruhWiki">this GitHub repository</a>.</p>
        </div>
      </div>
    </div>
  </body>
</html>
