<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=79" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>


    <title>Share your favorite media</title>
    <script src="${pageContext.request.contextPath}/resources/createListFunctions.js?version=82"></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

</head>
<body style="background: whitesmoke">

<c:import url="navBar.jsp"/>
<div class="container d-flex flex-column">
    <c class="container d-flex flex-row ">
        <div class="container d-flex flex-column">
            <div >
                <form id="filter-form" class="mb-2 d-flex flex-row justify-content-between" action="${pageContext.request.contextPath}/createList" method="get" onsubmit="beforeSubmit()">
                    <input type="hidden"  id="selected-media-input" name="s" />
                    <div class="d-flex flex-row">
                        <select name="m" class="form-select filter-width" aria-label="Filter!">
                            <option ${'Movies and Series' == param.m ? 'selected' : ''}>Movies and Series</option>
                            <option  ${'Movies' == param.m ? 'selected' : ''}>Movies</option>
                            <option  ${'Series' == param.m ? 'selected' : ''}>Series</option>
                        </select>
                       <%-- <select name="f" id="filter-types" class="form-select filter-width" aria-label="Filter!" onchange="toggleGenreSelect()">
                            <option ${'Popular' == param.f ? 'selected' : ''}>Popular</option>
                            <option ${'Genre' == param.f ? 'selected' : ''}>Genre</option>
                        </select>
                        <select name="g" id="genre-select" class="form-select filter-width" aria-label="Filter!" style="display:none">
                            <c:forEach var="genre" items="${genresList}">
                                <option value="${genre}" ${genre == param.g? 'selected' : ''}>${genre}</option>
                            </c:forEach>
                        </select>--%>
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
                    <div class="d-flex flex-row">
                        <input class="form-control me-2" type="search" name="q" value="${param.q}" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success" type="submit">Search</button>
                        <a style="height: 100%;" class="btn btn-outline-success align-bottom" href="${pageContext.request.contextPath}/createList">
                            Reset Filters
                        </a>
                    </div>
                </form>
                <div class="container d-flex justify-content-left p-0" id="genre-chips">
                    <c:forEach var="gen" items="${param.g}">
                        <div class="m-1 badge text-bg-dark">
                            <span class="text-bg-dark"> ${gen} </span>
                            <i class="btn bi bi-trash-fill" onclick="deleteGenre(this)"></i>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="scrollableDiv flex-wrap d-flex">
                <c:if test="${fn:length(mediaList) == 0 }">
                    <div class="d-flex m-2 flex-column">
                        No media was found.
                        <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/createList">Go Back</a>
                    </div>
                </c:if>
                <c:forEach var="movie" items="${mediaList}" end="24">
                    <div class="poster card text-bg-dark m-1"
                         onclick="displayMediaName(
                             '${(fn:replace(fn:replace(movie.name,"'", "\\'"), "\"", "&quot;"))}',
                             ${movie.mediaId})">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                            <img class="height-full" src="${movie.posterPath}" alt="poster image">
                            <div class="card-img-overlay">
                                <h5 class="card-title">${movie.name}</h5>
                                <p class="card-text">${movie.tmdbRating}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>
        <div id="preview" style="position: relative" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
            <div class="image-blur height-full background" style="background: dimgray"></div>
            <form:form modelAttribute="ListForm" action="${pageContext.request.contextPath}/createListAction"
                       method="POST">
            <div style="position: absolute;top: 0;left: 0" class="d-flex p-4 container flex-column">
                    <h2 class="m-2">List Name:</h2>
                <form:input path="listName" name="listName" id="list-name" required="required"
                            class="form-control me-2 createListInput" maxlength="50"/>
                <span id="listNameCharCount" class="text-muted"><span id="listNameRemainingChars">0</span>/50</span>
                <form:errors path="listName" cssClass="error"/>
                    <h3 class="m-2" >Description:</h3>
                <form:textarea path="listDescription" class="review-textarea" rows="3" name="listDescription"
                               placeholder="Your description..." maxlength="255" />
                <span id="listDescriptionCharCount" class="text-muted"><span id="listDescriptionRemainingChars">0</span>/255</span>
                <form:errors path="listDescription" cssClass="error"/>
                    <h3 class="m-2" >Email:</h3>
                <form:input path="userEmail" required="required" name="userEmail" type="email" id="list-email"
                            class="form-control me-2 createListInput"/>
                <form:errors path="userEmail" cssClass="error"/>
                <form:input path="mediaIdsList" type="hidden" name="mediaIds" id="selected-create-media"/>
                </form:form>
                <div class="scrollableMedia d-flex flex-column m-2 p-2" id="selected-media-names">
                    <c:forEach var="sel" items="${selected}">
                        <div class="d-flex justify-content-between ">
                            <div id="${sel.mediaId}" class="distinct-class">${sel.name}</div>
                            <i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>
                        </div>
                    </c:forEach>
                </div>
                <button id="preview-details" type="submit" class="m-4 btn btn-outline-success align-bottom">Create List</button>
                <div class="d-flex" id="preview-list"></div>
            </div>
        </div>
    </c>

</div>

<script src="${pageContext.request.contextPath}/resources/createListRealTimeFunctions.js?version=79"></script>
</body>
</html>
