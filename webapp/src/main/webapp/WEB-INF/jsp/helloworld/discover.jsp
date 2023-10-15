<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>

</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp"/>

<%--variables para el manejo de selected filters--%>
<c:set var="selectedGenres" value="${fn:split(param.g, ',')}" />
<c:set var="selectedProviders" value="${fn:split(param.providers, ',')}" />

<div class="container d-flex flex-column">
    <div class="container d-flex flex-row ">
        <%--        FILTROS y PELIS    --%>

        <div class="container d-flex flex-column">
            <c:if test="${searchMode}">
                <div class="m-2">
                    <h1>
                        Results for: <c:out value="${param.query}"/>
                    </h1>
                </div>
            </c:if>
            <div>
                <c:if test="${!searchMode}">
                    <form id="filter-form" class="mb-2 d-flex justify-content-between flex-row" action="${pageContext.request.contextPath}/discover" method="get" onsubmit="beforeSubmit()">
                </c:if>
                <c:if test="${searchMode}">
                    <form id="filter-form" class="mb-2 d-flex justify-content-between flex-row" action="${pageContext.request.contextPath}/search" method="get" onsubmit="beforeSubmit()">
                    <input type="hidden" name="query" id="hiddenQueryParam" value="${param.query}">
                </c:if>

                    <div class="d-flex align-content-center  flex-row">
                        <select name="media" class="form-select filter-width" aria-label="Filter!">
                            <option ${'All' == param.media ? 'selected' : ''}>All media</option>
                            <option  ${'Movies' == param.media ? 'selected' : ''}>Movies</option>
                            <option  ${'Series' == param.media ? 'selected' : ''}>Series</option>
                        </select>

                        <input type="hidden" name="g" id="hiddenGenreInput">
                        <div class="dropdown">
                            <button style="height:100%;width: 150px;margin-right: 5px;" type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="outside">
                                Genres
                            </button>
                            <c:set var="isChecked" value="" />
                            <div style="height: 50vh" class="dropdown-menu scrollableDiv flex-wrap p-4">
                                <input type="text" id="searchBoxGenre" placeholder="Search..." class="form-control mb-3">
                 <%--   ES NECESARIO UTILIZAR LA VAR isChecked.
                   Porque al simplemente realizar fn:contains(param.g,genre)
                   existen casos como Action&Adventure que siempre daran match para Action y Adventure
                   Es preferible esto a en el controlador manejar la creacion de modelos nuevos que contemplen el checked para cada genero--%>
                                <c:forEach var="genre" items="${genresList}">
<%--                                    selectedGenre no deberia ser muy grande, ya que es el listado de genres seleccionados--%>
                                    <c:forEach var="selectedGenre" items="${selectedGenres}">
                                        <c:if test="${selectedGenre == genre}">
                                            <c:set var="isChecked" value="checked" />
                                        </c:if>
                                    </c:forEach>
                                    <div class="form-check special-genre-class">
                                        <input ${isChecked} type="checkbox" class="form-check-input special-genre-input" id="dropdownCheck${genre}">
                                        <label class="form-check-label" for="dropdownCheck${genresList.indexOf(genre)}">${genre}</label>
                                    </div>
                                    <c:set var="isChecked" value="" /> <!-- Reset the isChecked variable -->
                                </c:forEach>
                            </div>
                        </div>


                        <input type="hidden" name="providers" id="hiddenProviderInput">
                        <div class="dropdown">
                            <button style="height:100%;width: 150px;margin-right: 5px;" type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="outside">
                                Providers
                            </button>
                            <c:set var="isChecked" value="" />
                            <div style="height: 50vh" class="dropdown-menu scrollableDiv flex-wrap p-4">
                                <input type="text" id="searchBoxProvider" placeholder="Search..." class="form-control mb-3">
                                <%--   ES NECESARIO UTILIZAR LA VAR isChecked.
                                  Porque al simplemente realizar fn:contains(param.g,genre)
                                  existen casos como Action&Adventure que siempre daran match para Action y Adventure
                                  Es preferible esto a en el controlador manejar la creacion de modelos nuevos que contemplen el checked para cada genero--%>
                                <c:forEach var="provider" items="${providersList}">
                                    <%--                                    selectedGenre no deberia ser muy grande, ya que es el listado de genres seleccionados--%>
                                    <c:forEach var="selectedProvider" items="${selectedProviders}">
                                        <c:if test="${selectedProvider == provider.providerName}">
                                            <c:set var="isChecked" value="checked" />
                                        </c:if>
                                    </c:forEach>
                                    <div class="form-check special-provider-class">
                                        <input ${isChecked} type="checkbox" class="form-check-input special-provider-input" id="dropdownCheck${provider.providerName}">
                                        <label class="form-check-label" for="dropdownCheck${providersList.indexOf(provider)}">${provider.providerName}</label>
                                    </div>
                                    <c:set var="isChecked" value="" /> <!-- Reset the isChecked variable -->
                                </c:forEach>
                            </div>
                        </div>


                        <button class="btn btn-outline-success me-1" type="submit">Apply filters</button>
                        <select name="orderBy" class="form-select filter-width" aria-label="Filter!">
                            <option ${'name' == param.orderBy ? 'selected' : ''} value="name">Title</option>
                            <option ${('tmdbrating' == param.orderBy || param.orderBy == null) ? 'selected' : ''} value="tmdbrating">Score</option>
                            <option ${'releasedate' == param.orderBy ? 'selected' : ''} value="releasedate">Release Date</option>
                        </select>
                        <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='asc'? 'asc':'desc'}">
                        <div class="btn btn-style me-1" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                            <i id="sortIcon" class="bi bi-arrow-${param.order == 'asc' ? 'up' : 'down'}-circle-fill"></i>
                        </div>
                    </div>
                    <div >
                        <a style="height: 100%;" class="btn btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover">
                            Reset Filters
                        </a>
                    </div>
                </form>
            </div>
            <div class="container d-flex justify-content-left p-1" id="genre-chips">
                <c:forEach var="gen" items="${param.g}">
                    <div class="m-1 badge text-bg-dark">
                        <span class="text-bg-dark"> ${gen} </span>
                        <i class="btn bi bi-trash-fill" onclick="deleteGenre(this)"></i>
                    </div>
                </c:forEach>
            </div>
            <div class="scrollableDiv flex-wrap d-flex justify-space-between">
                <c:if test="${fn:length(mediaList) == 0 }">
                    <div class="d-flex m-2 flex-column">
                        No media was found.
                        <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover">Discover other content</a>
                    </div>
                </c:if>
                <c:forEach var="movie" items="${mediaList}" varStatus="loop">
                    <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
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
                                    <c:forEach var="provider" items="${movie.providers}" end="1">
                                        <span class="mt-1 badge text-bg-light border border-black">
                                            <img src="${provider.logoPath}" alt="provider logo" style="height: 1.4em; margin-right: 5px;">
                                        </span>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
            <c:if test="${searchMode == false}">
                <div class="m-1">
                    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="/discover?media=${param.media}&g=${param.g}"/>
                    </c:import>
                </div>
            </c:if>
            <c:if test="${searchMode == true}">
                <div class="m-1">
                    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="/search?query=${param.query}&media=${param.media}&g=${param.g}"/>
                    </c:import>
                </div>
            </c:if>
        </div>
    </div>
</div>

</body>


</html>
