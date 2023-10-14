<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/lists.css?version=60" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=65" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <title>Discover your next passion</title>
    <script src="${pageContext.request.contextPath}/resources/browseListsFunctions.js?version=87"></script>
</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp">

</c:import>

<div style="margin-bottom: 30px">
    <div class="container">

        <c:if test="${fn:length(param.search) > 0}">
            <h2>
                Results for: "<c:out value="${param.search}"/>"
            </h2>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div>${successMessage}</div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
        <h1>
            Community lists
        </h1>
        <form class="d-flex mb-0" role="search" action="${pageContext.request.contextPath}/lists" method="get">
            <input class="form-control me-2" type="search" name="search" placeholder="Search a Moovie List!" aria-label="Moovie List id">
            <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
    </div>

    <div class="lists-container" style="margin-top: 30px">
        <form id="sortForm" method="get">
            <div style="display: flex; align-items: center;">
                <h2 style="padding-right: 4px">Sort by</h2>
                <select name="orderBy" class="form-select filter-width" aria-label="Filter!" id="sortSelect">
                    <option ${'likeCount' == param.orderBy ? 'selected' : ''} value="likeCount">Likes</option>
                    <option ${'moovieListId' == param.orderBy ? 'selected' : ''} value="moovieListId">Recent</option>
                </select>
                <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='desc'? 'desc':'asc'}">
                <div style="margin: 0;" class="btn btn-style" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                    <i id="sortIcon" class="bi bi-arrow-${param.order == 'desc' ? 'up' : 'down'}-circle-fill"></i>
                </div>
                <button type="submit" id="applyButton" class="btn btn-style2">Apply</button>
            </div>
        </form>
        <c:if test="${showLists.size()==0}">
                <h3>No results were found</h3>
            </c:if>
        <c:forEach var="showList" items="${showLists}">
                <div class="list-card card"
                     onclick="location.href='${pageContext.request.contextPath}/list/${showList.moovieListId}?page=1'">
                    <div class="list-img-container card-img-top">
                        <c:forEach var="image" items="${showList.images}">
                            <img class="cropCenterImage" src="${image}" alt="...">
                        </c:forEach>
                        <c:forEach begin="${fn:length(showList.images)}" end="3">
                            <img class="cropCenterImage"
                                 src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
                        </c:forEach>
                    </div>
                    <div class="card-body cardBodyFlex">
                        <div>
                            <div class="card-name-likes">
                                <div class="card-content overflow-hidden">
                                    <h5 class="card-title"><strong><c:out value="${showList.name}"/></strong></h5>
                                </div>
                                <div class="card-likes">
                                    <h5><i class="bi bi-hand-thumbs-up"></i>${showList.likeCount}</h5>
                                </div>
                            </div>
                            <div style="display: flex;">
                                <c:if test="${showList.moviesAmount > 0}">
                                    <p>${showList.moviesAmount} Movies</p>
                                </c:if>

                                <c:if test="${showList.moviesAmount > 0 && (showList.size - showList.moviesAmount) > 0}">
                                    <style>
                                        p {
                                            margin-right: 10px; /* Add a space between "Movies" and "Series" */
                                        }
                                    </style>
                                </c:if>
                                <c:if test="${(showList.size - showList.moviesAmount) > 0}">
                                    <p>${(showList.size - showList.moviesAmount)} Series</p>
                                </c:if>
                            </div>
                            <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">by <c:out
                                    value="${showList.username}"/>
                            </p>
                            <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                                <c:out value="${showList.description}"/>
                            </p>
                        </div>
                    </div>

                </div>
            </c:forEach>
    </div>
</div>
</body>
<div class="m-1">
    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
        <c:param name="mediaPages" value="${numberOfPages}"/>
        <c:param name="currentPage" value="${currentPage + 1}"/>
        <c:param name="url" value="${urlBase}"/>
    </c:import>
</div>
</html>

<style>
    .card-name-likes {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
    }

    .card-likes {
        text-align: right;
        margin-right: 10px;
        min-width: 50px;
    }
</style>