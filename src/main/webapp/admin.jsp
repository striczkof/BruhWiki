<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <!-- Set title names -->
    <c:set var="title" scope="page" value="Admin Panel"/>
    <c:if test="${not empty param.show}">
        <c:set var="show" scope="page" value="&show=${param.show}"/>
        <c:choose>
            <c:when test="${param.show eq 'articles'}">
                <c:set var="title" scope="page" value="Articles Panel"/>
            </c:when>
            <c:when test="${param.show eq 'categories'}">
                <c:set var="title" scope="page" value="Categories Panel"/>
            </c:when>
            <c:when test="${param.show eq 'users'}">
                <c:set var="title" scope="page" value="Users Panel"/>
            </c:when>
        </c:choose>
    </c:if>
    <head>
        <meta charset="UTF-8"/>
        <title><c:out value="${title}"/> - <c:out value="${pageContext.servletContext.servletContextName}"/></title>
        <link rel="stylesheet" href="styles.css" type="text/css"/>
    </head>
    <body>
        <div class="main main--big">
            <div class="title">
                <h1 class="title-header"><c:out value="${pageContext.servletContext.servletContextName}"/></h1>
            </div>
            <div class="sidebar">
                <ul class="sidebar-list">
                    <!-- If user's name is null, use their username as their name -->
                    <!-- Wait a minute, I can just do that in the bean -->
                    <li class="sidebar-list__item sidebar-list__item--user">
                        <p>Hi <c:out value="${sessionScope.user.name}!"/></p>
                        <p><a href="user.jsp">Edit user</a> | <a href="user-servlet?logout">Logout</a></p>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item active" href="admin.jsp">Admin Panel</a>
                    </li>
                    <!-- Sub items for admin stuff -->
                    <li class="sidebar-list__item">
                        <a class="nav-item${not empty param.show and param.show eq 'articles' ? ' active' : ''}" href="admin.jsp?show=articles">Articles</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item${not empty param.show and param.show eq 'categories' ? ' active' : ''}" href="admin.jsp?show=categories">Categories</a>
                    </li>
                    <li class="sidebar-list__item">
                        <a class="nav-item${not empty param.show and param.show eq 'users' ? ' active' : ''}" href="admin.jsp?show=users">Users</a>
                    </li>
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
                        <a class="nav-item" href="about.jsp">About</a>
                    </li>
                </ul>
            </div>
            <div class="content">
                <div class="content__main">
                    <c:choose>
                        <c:when test="${empty param.show}">
                            <h2 class="content-header"><c:out value="${title}"/></h2>
                            <!-- Main admin page goes here -->
                            <p class="content-text--admin">Welcome to the admin panel! Here you can manage articles, categories and users.<br/>Click the subpages on your left to access the panels.</p>
                            <!-- Include relevant stuff -->
                            <jsp:include page="/admin-servlet"/>
                            <h3>Wiki Statistics</h3>
                            <ul>
                                <li><p>${requestScope.articleCount} total articles. (including ${requestScope.hiddenArticleCount} hidden articles)</p></li>
                                <li><p>${requestScope.categoryCount} total categories.</p></li>
                                <li><p>${requestScope.userCount} total users.</p></li>
                            </ul>
                        </c:when>
                        <c:otherwise>
                            <!-- Sub pages go here -->
                            <!-- Declare required variables -->
                            <c:set var="page" scope="page" value="${not empty param.page ? param.page : 1}"/>
                            <c:choose>
                                <c:when test="${param.show eq 'articles'}">
                                    <c:choose>
                                        <c:when test="${not empty param.does}">
                                            <!-- oh god, another choose block -->
                                            <c:set var="artId" scope="page" value="${not empty param.artId ? param.artId : -1}"/>
                                            <c:set var="does" scope="page" value="${not empty param.does ? param.does : 'woah'}"/>
                                            <c:choose>
                                                <c:when test="${param.does eq 'edit'}">
                                                    <!-- Edit article -->
                                                    <jsp:include page="/article-servlet">
                                                        <jsp:param name="id" value="${param.artId}"/>
                                                        <jsp:param name="does" value="edit"/>
                                                        <jsp:param name="show" value="${0}"/>
                                                        <jsp:param name="num" value="${0}"/>
                                                        <jsp:param name="page" value="${0}"/>
                                                        <jsp:param name="truncate" value="${0}"/>
                                                    </jsp:include>
                                                    <c:set var="title" scope="page" value="Edit '${requestScope.article.name}'"/>
                                                </c:when>
                                                <c:when test="${param.doeq eq 'new'}">
                                                    <!-- New article -->
                                                    <!-- no need to include article-servlet, you're literally making a new article -->
                                                    <!-- oh ye i forgot, need to grab the category list -->
                                                    <jsp:include page="/article-servlet">
                                                        <jsp:param name="does" value="new"/>
                                                        <jsp:param name="show" value="${0}"/>
                                                        <jsp:param name="num" value="${0}"/>
                                                        <jsp:param name="page" value="${0}"/>
                                                        <jsp:param name="truncate" value="${0}"/>
                                                    </jsp:include>
                                                    <c:set var="title" scope="page" value="New Article"/>
                                                </c:when>
                                                <c:otherwise>
                                                    Bro what are you doing here
                                                </c:otherwise>
                                            </c:choose>
                                            <!-- Prepare form -->
                                            <form name="article" id="article-form" method="post" action="admin-servlet">
                                                <input type="hidden" name="does" value="${param.does}"/>
                                                <c:if test="${param.does eq 'edit'}">
                                                    <input type="hidden" name="id" value="${param.artId}"/>
                                                </c:if>
                                                <label for="title">Title</label><br/>
                                                <input type="text" name="Title" id="title" value="${not empty pageScope.article.title ? param.does : ''}" required/><br/>
                                                <label for="category">Category</label>
                                                <br/>
                                                <select name="catId" id="category">
                                                    <c:forEach items="${requestScope.categories}" var="category">
                                                        <option value="${category.id}" ${not empty requestScope.article and category.id eq requestScope.article.categoryId ? ' selected' : ''}>${category.name}</option>
                                                    </c:forEach>
                                                </select>
                                                <br/>
                                                <label for="content">Content</label><br/>
                                                <textarea id="content" name="content" rows="40" cols="50">${not empty pageScope.article.content ? pageScope.article.content : ''}</textarea><br/>
                                                <label>Publish the article?</label><br/>
                                                <input type="radio" id="hidden-true" name="hidden" value="true">
                                                <label for="hidden-true">Yes (make it visible)</label><br>
                                                <input type="radio" id="hidden-false" name="hidden" value="false">
                                                <label for="hidden-false">No (make it hidden)</label><br>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Declare required variables -->
                                            <c:set var="num" scope="page" value="${not empty param.num ? param.num : 10}"/>
                                            <c:set var="does" scope="page" value="${not empty param.does ? param.does : null}"/>
                                            <jsp:include page="/admin-servlet">
                                                <jsp:param name="show" value="${param.show}"/>
                                                <jsp:param name="num" value="${pageScope.num}"/>
                                                <jsp:param name="page" value="${pageScope.page}"/>
                                                <jsp:param name="truncate" value="${50}"/>
                                                <jsp:param name="does" value="${pageScope.does}"/>
                                            </jsp:include>
                                            <h2 class="content-header"><c:out value="${title}"/></h2>
                                            <div class="content__main__panel--left">
                                                <ul class="article-list">
                                                    <c:forEach items="${requestScope.articles}" var = "article">
                                                        <li class="article-list__item--small article ">
                                                            <h4 class="article__title"><c:out value="${article.title}"/></h4>
                                                            <p class="article__edit"><a href="admin.jsp?${not empty param.page ? 'page='.concat(param.page.toString().concat('&')) : ''}show=categories&catId=${article.id}&does=rn">Rename</a> | <a href="admin.jsp?${not empty param.page ? 'page='.concat(param.page.toString().concat('&')) : ''}show=categories&catId=${requestScope.categories[i].id}&does=del">Delete</a></p>
                                                            <p class="article__subtitle">In <a href="categories.jsp?id=${article.categoryId}"><c:out value="${article.categoryName}"/></a> last edited on <fmt:formatDate type="date" value="${article.lastEditedDate}"/></p>
                                                            <p class="article__body"><c:out value="${article.content}"/> <a href="articles.jsp?id=${article.id}">Read more >>></a></p>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:when test="${param.show eq 'categories'}">
                                    <!-- Declare required variables -->
                                    <c:set var="num" scope="page" value="${not empty param.num ? param.num : 10}"/>
                                    <c:set var="catId" scope="page" value="${not empty param.catId ? param.catId : -1}"/>
                                    <c:set var="does" scope="page" value="${not empty param.does ? param.does : null}"/>
                                    <jsp:include page="/admin-servlet">
                                        <jsp:param name="show" value="${param.show}"/>
                                        <jsp:param name="num" value="${pageScope.num}"/>
                                        <jsp:param name="page" value="${pageScope.page}"/>
                                        <jsp:param name="truncate" value="${0}"/>
                                        <jsp:param name="catId" value="${pageScope.catId}"/>
                                        <jsp:param name="does" value="${pageScope.does}"/>
                                    </jsp:include>
                                    <h2 class="content-header"><c:out value="${title}"/></h2>
                                    <div class="content__main__panel--left">
                                        <ul class="article-list">
                                            <c:if test="${requestScope.maxShow eq 0}">
                                                <c:redirect url="admin.jsp?page=${requestScope.maxPage}&show=categories"/>
                                            </c:if>
                                            <c:forEach var = "i" begin="0" end="${requestScope.maxShow}">
                                                <li class="article-list__item--small article ">
                                                    <!-- if id is passed by request, set as pageScope.catId for rename has to be changed later -->
                                                    <c:if test="${not empty requestScope.catId}">
                                                        <c:set var="catId" scope="page" value="${requestScope.catId}"/>
                                                    </c:if>

                                                    </li>
                                                    <c:choose>
                                                        <c:when test="${(pageScope.does eq 'rn' or pageScope.does eq 'new') and pageScope.catId eq requestScope.categories[i].id}">
                                                            <form class="rename-form" action="admin-servlet" method="post">
                                                                <input type="hidden" name="id" value="${requestScope.categories[i].id}"/>
                                                                <input type="hidden" name="show" value="categories"/>
                                                                <input type="hidden" name="num" value="${pageScope.num}"/>
                                                                <input type="hidden" name="page" value="${pageScope.page}"/>
                                                                <input type="hidden" name="action" value="rename"/>
                                                                <label hidden="hidden" for="newName">Category name</label>
                                                                <input class="rename-form" type="text" id="newName" name="newName" placeholder="Category name" value="${requestScope.categories[i].name}" required/>
                                                                <input class="rename-form__button" type="submit" value="Rename"/>
                                                            </form>
                                                            <p class="article__edit">
                                                                <a href="admin.jsp?${not empty param.page ? 'page='.concat(param.page.toString().concat('&')) : ''}show=categories&catId=${requestScope.categories[i].id}&does=del">Delete</a>
                                                            </p>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <h4 class="article__title"><a href="categories.jsp?id=${requestScope.categories[i].id}"><c:out value="${requestScope.categories[i].name}"/></a></h4>
                                                            <p class="article__edit"><a href="admin.jsp?${not empty param.page ? 'page='.concat(param.page.toString().concat('&')) : ''}show=categories&catId=${requestScope.categories[i].id}&does=rn">Rename</a> | <a href="admin.jsp?${not empty param.page ? 'page='.concat(param.page.toString().concat('&')) : ''}show=categories&catId=${requestScope.categories[i].id}&does=del">Delete</a></p>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:choose>
                                                        <c:when test="${requestScope.articleCounts[i] eq 0}">
                                                            <p class="article__subtitle">Has no articles</p>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <p class="article__subtitle">Has: ${requestScope.articleCounts[i]}, latest article: <a href="articles.jsp?id=${requestScope.recentArticles[i].id}">${requestScope.recentArticles[i].title}</a></p>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </div>
                                </c:when>
                                <c:when test="${param.show eq 'users'}">
                                    <!-- Declare required variables -->
                                    <c:set var="num" scope="page" value="${not empty param.num ? param.num : 10}"/>
                                    <jsp:include page="/admin-servlet"/>
                                </c:when>
                                <c:otherwise>
                                    <!-- invalid subpage -->
                                    <h2 class="content-header">Invalid subpage</h2>
                                    <p class="content-text--admin">Where ya going dude?</p>
                                </c:otherwise>
                            </c:choose>
                            <div class="content__main__panel--right">
                                <ul class="big-blue-button-list">
                                    <c:choose>
                                        <c:when test="${param.show eq 'articles'}">
                                            <c:choose>
                                                <c:when test="${not empty param.does and param.does eq 'new'}">
                                                    <li>
                                                        <button class="big-blue-button" form="article-form">Save New Article</button>
                                                    </li>
                                                </c:when>
                                                <c:when test="${not empty param.does and param.does eq 'edit'}">
                                                    <li>
                                                        <button class="big-blue-button" form="article-form">Save Edit</button>
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li>
                                                        <a class="big-blue-button" href="admin.jsp?show=articles&does=new" >Create Article</a>
                                                    </li>
                                                    <li>
                                                        <a class="big-blue-button" href="admin.jsp?show=articles" >Show All Articles</a>
                                                    </li>
                                                    <li>
                                                        <a class="big-blue-button" href="admin.jsp?show=articles&hidden=false" >Show Visible Articles Only</a>
                                                    </li>
                                                    <li>
                                                        <a class="big-blue-button" href="admin.jsp?show=articles&hidden=true" >Show Hidden Articles Only</a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:when test="${param.show eq 'categories'}">
                                            <li>
                                                <a class="big-blue-button" href="admin.jsp?show=categories&page=${(requestScope.catCount + 1) % pageScope.num eq 1 ? requestScope.maxPage : requestScope.maxPage}&does=new" >Create Category</a>
                                            </li>
                                        </c:when>
                                    </c:choose>
                                </ul>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="content__footer">
                    <c:if test="${not empty param.show and empty param.artId}">
                        <c:set var="showParam" scope="page" value="&show=${param.show}"/>
                        <c:if test="${not empty param.hidden}">
                            <c:set var="hiddenParam" scope="page" value="&hidden=${param.hidden}"/>
                        </c:if>
                        <c:if test="${param.show eq 'articles' or param.show eq 'categories'}">
                            <c:choose>
                                <c:when test="${pageScope.page == 1 and pageScope.maxPage == 1}">
                                    <p class="page-number"><< < 1 > >></p>
                                </c:when>
                                <c:when test="${pageScope.page == 1}">
                                    <p class="page-number"><< < 1 <a href="admin.jsp?page=2${pageScope.showParam}${pageScope.hiddenParam}">></a> <a href="admin.jsp?page=${requestScope.maxPage}${pageScope.showParam}${pageScope.hiddenParam}">>></a></p>
                                </c:when>
                                <c:when test="${pageScope.page >= requestScope.maxPage}">
                                    <p class="page-number"><a href="admin.jsp?page=1${pageScope.showParam}${pageScope.hiddenParam}"><<</a> <a href="admin.jsp?page=${pageScope.page - 1}${pageScope.showParam}${pageScope.hiddenParam}"><</a> ${pageScope.page} > >></p>
                                </c:when>
                                <c:otherwise>
                                    <p class="page-number"><a href="admin.jsp?page=1${pageScope.showParam}${pageScope.hiddenParam}"><<</a> <a href="admin.jsp?page=${pageScope.page - 1}${pageScope.showParam}${pageScope.hiddenParam}"><</a> ${pageScope.page} <a href="admin.jsp?page=${pageScope.page + 1}${pageScope.showParam}${pageScope.hiddenParam}">></a> <a href="admin.jsp?page=${requestScope.maxPage}${pageScope.showParam}${pageScope.hiddenParam}">>></a></p>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:if>
                    <p class="copyright">Copyright © 2023 Alvin. This is a free software released under the <a href="https://www.gnu.org/licenses/agpl-3.0.en.html">GNU Affero General Public License v3.0.</a></p>
                    <p class="disclaimer">All articles and categories are AI-generated. It is safe to assume that they are false. <br/> All source code is contained in <a href="https://github.com/striczkof/BruhWiki">this GitHub repository</a>.</p>
                </div>
            </div>
        </div>
    </body>
</html>
