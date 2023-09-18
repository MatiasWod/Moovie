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
    <link href="${pageContext.request.contextPath}/resources/main.css?version=79" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>

    <title>Share your favorite media</title>
    <script src="${pageContext.request.contextPath}/resources/createListFunctions.js?version=79"></script>

</head>
<body style="background: whitesmoke">

<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>
    <c class="container d-flex flex-row ">
        <div class="container d-flex flex-column">
            <div >
                <form class="mb-2 d-flex flex-row justify-content-between" action="${pageContext.request.contextPath}/createList" method="get" onsubmit="beforeSubmit()">
                    <input type="hidden"  id="selected-media-input" name="s" />
                    <div class="d-flex flex-row">
                        <select name="m" class="form-select filter-width" aria-label="Filter!">
                            <option ${'Movies and Series' == param.m ? 'selected' : ''}>Movies and Series</option>
                            <option  ${'Movies' == param.m ? 'selected' : ''}>Movies</option>
                            <option  ${'Series' == param.m ? 'selected' : ''}>Series</option>
                        </select>
                        <select name="f" id="filter-types" class="form-select filter-width" aria-label="Filter!" onchange="toggleGenreSelect()">
                            <option ${'Popular' == param.f ? 'selected' : ''}>Popular</option>
                            <option ${'Genre' == param.f ? 'selected' : ''}>Genre</option>
                        </select>
                        <select name="g" id="genre-select" class="form-select filter-width" aria-label="Filter!" style="display:none">
                            <c:forEach var="genre" items="${genresList}">
                                <option value="${genre}" ${genre == param.g? 'selected' : ''}>${genre}</option>
                            </c:forEach>
                        </select>
                        <button class="btn btn-outline-success" type="submit">Apply filters</button>
                    </div>
                    <div class="d-flex flex-row">
                            <input class="form-control me-2" type="search" name="q" value="${param.q}" placeholder="Search" aria-label="Search">
                            <button class="btn btn-outline-success" type="submit">Search</button>
                    </div>

                </form>
            </div>
            <div class="scrollableDiv flex-wrap d-flex">
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
            <form action="${pageContext.request.contextPath}/createListAction" method="POST">
            <div style="position: absolute;top: 0;left: 0" class="d-flex p-4 container flex-column">

                    <h2 class="m-2">List Name:</h2>
                        <input name="listName" id="list-name" required class="form-control me-2 createListInput">
                    <h3 class="m-2" >Description:</h3>
                        <textarea class="review-textarea" rows="3" name="listDescription" placeholder="Your description..."></textarea>
                    <h3 class="m-2" >Email:</h3>
                        <input required name="userEmail" type="email" id="list-email" class="form-control me-2 createListInput">
                    <input type="hidden" name="mediaIds" id="selected-create-media">

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
            </form>
        </div>
    </c>

</div>

</body>
</html>
