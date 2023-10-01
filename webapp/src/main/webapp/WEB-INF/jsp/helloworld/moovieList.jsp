<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <title>Moovie List</title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=62" rel="stylesheet"/>
</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp">
    <c:param name="userName" value="${user.username}"/>
</c:import>
<div class="container d-flex flex-column">
    <div class="header">
            <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${moovieList.name}"/></h1>
            <h3><c:out value="${moovieList.description}"/></h3>
        <h4 style="color: lightgray;">by <a style="text-decoration: none; color: inherit;"
                                            href="${pageContext.request.contextPath}/profile/${listOwner}"><c:out
                value="${listOwner}"/></a></h4>
    </div>
    <div class="buttons">

        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <form action="${pageContext.request.contextPath}/like" method="POST">
                    <input type="hidden" name="listId" value="${moovieList.moovieListId}"/>
                    <c:choose>
                        <c:when test="${isLiked}">
                            <button type="submit" class="btn btn-style"><i
                                    class="bi bi-hand-thumbs-up-fill"></i>${likeCount} Liked
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" class="btn btn-style"><i class="bi bi-hand-thumbs-up"></i>${likeCount}
                                Like
                            </button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </div>
            <div style="display: flex; align-items: center;">
                <h2 style="padding-right: 4px">Sort by</h2>
                <select name="media" class="form-select filter-width" aria-label="Filter!" id="sortSelect">
                    <option value="title">Title</option>
                    <option value="type">Type</option>
                    <option value="score">Score</option>
                    <option value="release date">Release Date</option>
                </select>
                <button class="btn btn-style" id="sortButton" onclick="changeSortOrder()"><i id="sortIcon" class="bi bi-arrow-down-circle-fill"></i></button>
            </div>
        </div>
    </div>
    <div>
        <h4>List progress</h4>
        <div class="progress">
            <div class="progress-bar" role="progressbar" style="width: ${(watchedMovies.size()*100)/mediaList.size()}%;" id="progressBar"
                 aria-valuenow="${(watchedMovies.size()*100)/mediaList.size()}" aria-valuemin="0" aria-valuemax="100">
                ${(watchedMovies.size()*100)/mediaList.size()}%
            </div>
        </div>
    </div>
    <div style="display: flex; align-items: center;justify-content: center">
        <c:if test="${moviesCount > 0}">
            <h4>${moviesCount} Movies</h4>
        </c:if>
        <c:if test="${moviesCount > 0 && tvSeriesCount > 0}">
            <h4 style="margin-right: 5px;margin-left: 5px">and</h4>
        </c:if>
        <c:if test="${tvSeriesCount > 0}">
            <h4>${tvSeriesCount} Series</h4>
        </c:if>
    </div>
    <table class="table table-striped" id="movieTable">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Title</th>
            <th scope="col">Type</th>
            <th scope="col">Score</th>
            <th scope="col">Release Date</th>
        </tr>
        </thead>
        <c:choose>
            <c:when test="${not empty mediaList}">
                <tbody>
                <c:forEach var="index" items="${mediaList}" varStatus="loop">
                    <tr>
                        <!-- Index -->
                        <td style="text-align: center">${loop.index + 1}</td>
                        <!-- Title -->
                        <td>
                            <div class="row align-items-center">
                                <div class="col-auto">
                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                       style="text-decoration: none; color: inherit;">
                                        <img src="${mediaList[loop.index].posterPath}" class="img-fluid" width="100"
                                             height="100" alt="${mediaList[loop.index].name} poster"/>
                                    </a>
                                </div>
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                       style="text-decoration: none; color: inherit;">
                                        <strong>${mediaList[loop.index].name}</strong>
                                    </a>
                                </div>
                                <c:if test="${watchedMovies.contains(mediaList[loop.index].mediaId)}">
                                    <div class="col-auto">
                                        <i class="bi bi-check-circle-fill" style="color: green"></i>
                                    </div>
                                </c:if>
                            </div>
                        </td>
                        <!-- Type -->
                        <td>
                            <c:choose>
                                <c:when test="${mediaList[loop.index].type}">
                                    Tv Series
                                </c:when>
                                <c:otherwise>
                                    Movie
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <!-- Score -->
                        <td>${mediaList[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
                        </td>
                        <td>
                            <span>${mediaList[loop.index].releaseDate}</span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </c:when>
            <c:otherwise>
                <tbody>
                <tr>
                    <td colspan="5">List is empty</td>
                </tr>
                </tbody>
            </c:otherwise>
        </c:choose>
    </table>
    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
        <c:param name="mediaPages" value="${numberOfPages}"/>
        <c:param name="currentPage" value="${currentPage + 1}"/>
        <c:param name="url" value="/list/${moovieList.moovieListId}/"/>
    </c:import>
</div>
</body>
</html>

<script src="${pageContext.request.contextPath}/resources/moovieListFunctions.js?version=81"></script>

