<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/10/2023
  Time: 5:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=82" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Discover your next favorite experience</title>
    <script src="${pageContext.request.contextPath}/resources/discoverFunctions.js?version=81"></script>
</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp">
    <c:param name="userName" value="${user.username}"/>
</c:import>
<div class="container d-flex flex-column">
    <c class="container d-flex flex-row ">
        <%--        FILTROS y PELIS    --%>

        <div class="container d-flex flex-column">
            <c:if test="${searchMode}">
                <div class="m-2">
                    <h1>
                        Results for: <c:out value="${param.query}"/>
                    </h1>
                </div>
            </c:if>
            <c:if test="${!searchMode}">
                <div >
                    <form id="filter-form" class="mb-2 d-flex justify-content-between flex-row" action="${pageContext.request.contextPath}/discover" method="get" onsubmit="beforeSubmit()">
                        <div class="d-flex align-content-center  flex-row">
                            <select name="media" class="form-select filter-width" aria-label="Filter!">
                                <option ${'Movies and Series' == param.media ? 'selected' : ''}>Movies and Series</option>
                                <option  ${'Movies' == param.media ? 'selected' : ''}>Movies</option>
                                <option  ${'Series' == param.media ? 'selected' : ''}>Series</option>
                            </select>
<%--                            <select name="f" id="filter-types" class="form-select filter-width" aria-label="Filter!" onchange="toggleGenreSelect()">
                                <option ${'Popular' == param.f ? 'selected' : ''}>Popular</option>
                                <option ${'Genre' == param.f ? 'selected' : ''}>Genre</option>
                            </select>--%>
    <%--                        <select id="genre-select" class="form-select filter-width" aria-label="Filter!" style="display:none">--%>
    <%--                            <c:forEach var="genre" items="${genresList}">--%>
    <%--                                <option value="${genre}" ${genre == param.g? 'selected' : ''}>${genre}</option>--%>
    <%--                            </c:forEach>--%>
    <%--                        </select>--%>
                            <input type="hidden" name="g" id="hiddenGenreInput">
                            <div class="dropdown">
                                <button style="height:100%;width: 150px;margin-right: 5px;" type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="outside">
                                    Genres
                                </button>
                                <div class="dropdown-menu scrollableDiv flex-wrap p-4">
                                        <c:forEach var="genre" items="${genresList}">
                                            <div class="form-check">
                                                <input ${fn:contains(param.g,genre)? 'checked':''} type="checkbox" class="form-check-input" id="dropdownCheck${genre}">
                                                <label class="form-check-label" for="dropdownCheck${genresList.indexOf(genre)}">${genre}</label>
                                            </div>
                                        </c:forEach>
                                </div>
                            </div>
                            <button class="btn btn-outline-success" type="submit">Apply filters</button>
                        </div>
                        <div >
                            <a style="height: 100%;" class="btn btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover">
                                Reset Filters
                            </a>
                        </div>
                    </form>
                </div>
                <div class="container d-flex justify-content-left p-0" id="genre-chips">
                    <c:forEach var="gen" items="${param.g}">
                        <div class="m-1 badge text-bg-dark">
                            <span class="text-bg-dark"> ${gen} </span>
                            <i class="btn bi bi-trash-fill" onclick="deleteGenre(this)"></i>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            <div class="scrollableDiv flex-wrap d-flex justify-space-between">
                <c:if test="${fn:length(mediaList) == 0 }">
                    <div class="d-flex m-2 flex-column">
                        No media was found.
                        <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover">Discover other content</a>
                    </div>
                </c:if>
                <c:forEach var="movie" items="${mediaList}" end="24">
                    <div class="poster card text-bg-dark m-1"
                         onclick="loadPreview(
                                 '${(fn:replace(fn:replace(movie.name,"'", "\\'"), "\"", "&quot;"))}',
                                 '${movie.tmdbRating}',
                                 '${movie.posterPath}',
                                 '${(fn:replace(fn:replace(movie.overview, "'", "\\'"), "\"", "&quot;"))}',
                                 '${movie.adult}',
                                 '${movie.mediaId}',
                                 '${movie.releaseDate}')">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                            <img class="cropCenter" src="${movie.posterPath}">
                            <div class="card-img-overlay">
                                <h5 class="card-title">${movie.name}</h5>
                                <p class="card-text">${movie.tmdbRating}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="m-1">
                <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                    <c:param name="mediaPages" value="${numberOfPages}"/>
                    <c:param name="currentPage" value="${currentPage + 1}"/>
                    <c:param name="url" value="/discover?media=${param.media}&g=${param.g}"/>
                </c:import>
            </div>
        </div>
<%--        PREVIEW      --%>
    <div id="preview" style="position: relative; display: none !important" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
        <img id="preview-img" style="" class="image-blur height-full background" src="" alt="poster">
        <div style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; overflow: auto;" class="p-4 container">
            <h1 id="preview-explicit" class="mt-2 mb-2 bi bi-explicit" style="display: none"></h1>
            <div class="d-flex container flex-column">
                <h1 class="text-center" id="preview-title"></h1>
                <div class="m-1 d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">
                        <h3>
                            <i class="bi bi-star-fill m-1"></i>
                        </h3>
                        <h3 id="preview-rating"></h3>
                    </div>
                    <div class="d-flex align-items-center">
                        <h3 id="preview-year"></h3>
                    </div>
                </div>
                <p id="preview-synopsis"></p>
                <h3 id="preview-director"></h3>
            </div>
            <div class="text-center">
                <a id="preview-details" class="mt-4 btn btn-outline-success" type="submit">More details</a>
            </div>
        </div>
    </div>

</div>

</body>


</html>
