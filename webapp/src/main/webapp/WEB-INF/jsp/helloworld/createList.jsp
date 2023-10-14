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
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=83" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>


    <title>Share your favorite media</title>
    <script src="${pageContext.request.contextPath}/resources/createListFunctions.js?version=82"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>

</head>
<body style="background: whitesmoke">

<c:import url="navBar.jsp"/>
<sec:authorize access="isAuthenticated()">
    <div class="container d-flex flex-column">
        <div class="container d-flex flex-row ">
            <div class="container d-flex flex-column">
                <div style="z-index: 1;">
                    <form id="filter-form" class="mb-2 d-flex flex-row justify-content-between" action="${pageContext.request.contextPath}/createList" method="get" onsubmit="beforeSubmit()">
                        <input type="hidden"  id="selected-media-input" />
                        <div role="group" class="input-group d-flex flex-row m-1 me-3">
                            <select  name="m" class="form-select filter-width" aria-label="Filter!">
                                <option ${'Movies and Series' == param.m ? 'selected' : ''}>Movies and Series</option>
                                <option  ${'Movies' == param.m ? 'selected' : ''}>Movies</option>
                                <option  ${'Series' == param.m ? 'selected' : ''}>Series</option>
                            </select>
                            <input type="hidden" name="g" id="hiddenGenreInput">
                            <div class="dropdown">
                                <button style="height:100%;;margin-right: 5px;" type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="outside">
                                    Genres
                                </button>
                                <div style="height: 50vh" class="dropdown-menu scrollableDiv flex-wrap p-4">
                                    <c:forEach var="genre" items="${genresList}">
                                        <div class="form-check">
                                            <label for="dropdownCheck${genre}"></label><input ${fn:contains(param.g,genre)? 'checked':''} type="checkbox" class="form-check-input" id="dropdownCheck${genre}">
                                            <label class="form-check-label" for="dropdownCheck${genresList.indexOf(genre)}">${genre}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <button class="btn btn-outline-success me-1" type="submit" >Apply</button>
                            <select name="orderBy" class="form-select filter-width" aria-label="Filter!">
                                <option ${'name' == param.orderBy ? 'selected' : ''} value="name">Title</option>
                                <option ${'tmdbrating' == param.orderBy ? 'selected' : ''} value="tmdbrating">Score</option>
                                <option ${'releasedate' == param.orderBy ? 'selected' : ''} value="releasedate">Release Date</option>
                            </select>
                            <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='desc'? 'desc':'asc'}">
                            <div class="btn btn-style me-1" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                                <i id="sortIcon" class="bi bi-arrow-${param.order == 'desc' ? 'up' : 'down'}-circle-fill"></i>
                            </div>
                        </div>
                        <div role="group" class="input-group d-flex flex-row m-1">
                                <input class="form-control me-2" type="search" name="q" value="${param.q}" placeholder="Search" aria-label="Search">
                                <button class="btn btn-outline-success" type="submit">Search</button>
                                <a style="height: 100%;" class="btn btn-outline-success align-bottom" href="${pageContext.request.contextPath}/createList">
                                    Reset
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
                        <div onclick="displayMediaName('${(fn:replace(fn:replace(movie.name,"'", "\\'"), "\"", "&quot;"))}','${movie.mediaId}')" class="poster card text-bg-dark m-1">
                            <div class="card-img-container"> <!-- Add a container for the image -->
                                <img class="cropCenter" src="${movie.posterPath}" alt="media poster">
                                <div class="card-img-overlay">
                                    <h6 class="card-title text-center">${movie.name}</h6>
                                    <div class="d-flex justify-content-evenly">
                                        <p class="card-text">
                                            <i class="bi bi-star-fill"></i>
                                                ${movie.tmdbRating}
                                        </p>
                                        <p class="card-text">
                                            <fmt:formatDate value="${movie.releaseDate}" pattern="YYYY"/>
                                        </p>
                                    </div>
                                    <div class="d-flex justify-content-evenly flex-wrap">
                                        <c:forEach var="genre" items="${movie.genres}" end="1">
                                            <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                        </c:forEach>
                                    </div>
                                    <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                        <c:forEach var="provider" items="${movie.providerLogos}" end="1">
                                        <span class="mt-1 badge text-bg-light border border-black">
                                            <img src="${provider}" alt="provider logo" style="height: 1.4em; margin-right: 5px;">
                                        </span>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="m-1">
                    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="/createList?m=${param.m}&g=${param.g}&q=${param.q}"/>
                    </c:import>
                </div>
            </div>
            <div id="preview" style="position: relative" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
                <div class="image-blur height-full background" style="background: dimgray"></div>
                <div style="position: absolute;top: 0;left: 0;height: 100%;overflow: hidden" class="d-flex p-4 container flex-column">

                    <div class="d-flex justify-content-between">
                        <h2 class="m-2">List Name:</h2>
                        <button class="btn btn-danger m-2" onclick="deleteStorage()">Clear all</button>
                    </div>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" id="errorAlert" role="alert">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>${errorMessage}</div>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </div>
                    </c:if>
                    <div class="d-flex flex-column">
                        <form:form modelAttribute="ListForm" action="${pageContext.request.contextPath}/createListAction"
                                   method="POST" id="create-form">
                            <form:input path="listName" name="listName" id="list-name" required="required"
                                        class="form-control me-2 createListInput" maxlength="50"/>
                            <span id="listNameCharCount" class="text-muted"><span id="listNameRemainingChars">0</span>/50</span>
                            <form:errors path="listName" cssClass="error"/>
                            <h3 class="m-2" >Description:</h3>
                            <form:textarea path="listDescription" id="list-description" class="review-textarea" rows="3" name="listDescription"
                                           placeholder="Your description..." maxlength="255" />
                            <span id="listDescriptionCharCount" class="text-muted"><span id="listDescriptionRemainingChars">0</span>/255</span>
                            <form:errors path="listDescription" cssClass="error"/>
                            <form:input path="mediaIdsList" type="hidden" name="mediaIds" id="selected-create-media"/>
                            <div class="scrollableMedia d-flex flex-column m-2 p-2" id="selected-media-names">
                                <c:forEach var="sel" items="${selected}">
                                    <div class="other-distinct d-flex justify-content-between ">
                                        <div id="${sel.mediaId}" class="distinct-class">${sel.name}</div>
                                        <i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>
                                    </div>
                                </c:forEach>
                            </div>
                            <button id="preview-details" type="submit" class="btn btn-lg btn-outline-success mt-4">Create List</button>
                        </form:form>
                    </div>

                    <div>
                        <h6>Feel free to use other features on the website!
                        We will keep your list progress.</h6>
                    </div>
                    <div class="d-flex" id="preview-list"></div>
                </div>
            </div>
        </div>
    </div>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <c:import url="signUpAlert.jsp"/>
</sec:authorize>

<script src="${pageContext.request.contextPath}/resources/createListRealTimeFunctions.js?version=79"></script>
</body>
</html>
