<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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

    <title><spring:message code="discover.title"/></title>
    <script src="${pageContext.request.contextPath}/resources/discoverFunctions.js?version=81"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>

</head>
<body style="background: whitesmoke">
<c:import url="../common/navBar.jsp"/>

<%--variables para el manejo de selected filters--%>
<c:set var="selectedGenres" value="${fn:split(param.g, ',')}" />
<c:set var="selectedProviders" value="${fn:split(param.providers, ',')}" />
<div class="d-flex flex-column">
    <div class=" d-flex flex-row ">
        <div id="preview" class="fullHeightDiv ms-1" style="width: 13vw">
            <c:import url="../common/filterButtons.jsp">
                <c:param name="query" value="${param.query}"/>
                <c:param name="credit" value="${param.credit}"/>
                <c:param name="g" value="${param.g}"/>
                <c:param name="providers" value="${param.providers}"/>
                <c:param name="url" value="discover"/>
                <c:param name="searchBar" value="false"/>
            </c:import>
        </div>
        <div style="position: relative" class="container d-flex flex-row">
            <%--        FILTROS y PELIS    --%>
            <div class="container d-flex flex-column">
                <c:if test="${param.query != null && param.query.length() > 0}">
                    <h3>
                        <spring:message code="discover.results" arguments="${param.query}"/>
                    </h3>
                </c:if>
                <c:if test="${param.credit != null && param.credit.length() > 0}">
                    <h3>
                        <spring:message code="discover.credit" arguments="${param.credit}"/>
                    </h3>
                </c:if>

                <div class="scrollableDiv flex-wrap d-flex justify-space-between">
                    <c:if test="${fn:length(mediaList) == 0 }">
                        <div class="d-flex m-2 flex-column">
                            <spring:message code="discover.noResults"/>
                            <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover"><spring:message code="discover.prompt"/></a>
                        </div>
                    </c:if>
                    <c:forEach var="movie" items="${mediaList}" varStatus="loop">
                        <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                            <div class="card-img-container"> <!-- Add a container for the image -->
                                <sec:authorize access="isAuthenticated()">
                                    <div style="position:absolute;bottom:0;left: 20%;z-index: 2" class="d-flex m-2">
                                        <c:choose>
                                            <c:when test="${movie.watched}">
                                                <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                                                    <input type="hidden" name="listId" value="${watchedListId}"/>
                                                    <input type="hidden" name="mediaId" value="${movie.mediaId}"/>
                                                    <button class="btn btn-success m-1" type="submit">
                                                    <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                                                        <i class="bi bi-eye-fill" style="color: whitesmoke; cursor: pointer;"></i>
                                                    </span>
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                                    <input type="hidden" name="listId" value="${watchedListId}"/>
                                                    <input type="hidden" name="mediaId" value="${movie.mediaId}"/>
                                                    <button class="btn btn-secondary m-1" type="submit">
                                                    <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                                                        <i class="bi bi-eye-fill" style="color: whitesmoke; cursor: pointer;"></i>
                                                    </span>
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${movie.watchlist}">
                                                <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                                                    <input type="hidden" name="listId" value="${watchlistId}"/>
                                                    <input type="hidden" name="mediaId" value="${movie.mediaId}"/>
                                                    <button class="btn btn-success m-1" type="submit">
                                                    <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                                                        <i class="bi bi-bookmark-check-fill" style="color: whitesmoke; cursor: pointer;"></i>
                                                    </span>
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                                    <input type="hidden" name="listId" value="${watchlistId}"/>
                                                    <input type="hidden" name="mediaId" value="${movie.mediaId}"/>
                                                    <button class="btn btn-secondary m-1" type="submit">
                                                    <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                                                        <i class="bi bi-bookmark-check-fill" style="color: whitesmoke; cursor: pointer;"></i>
                                                    </span>
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </sec:authorize>

                                <img class="cropCenter async-image" src="${pageContext.request.contextPath}/resources/defaultPoster.png" data-src="${movie.posterPath}" alt="media poster">
                                <div class="card-img-overlay">
                                    <h6 class="card-title text-center"><c:out value="${movie.name}"/></h6>
                                    <div class="d-flex justify-content-evenly">
                                        <p class="card-text">
                                            <i class="bi bi-star-fill"></i>
                                                <c:out value="${movie.tmdbRating}"/>
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
                        <c:import url="/WEB-INF/jsp/common/pagination.jsp">
                            <c:param name="mediaPages" value="${numberOfPages}"/>
                            <c:param name="currentPage" value="${currentPage + 1}"/>
                            <c:param name="url" value="/discover?query=${param.query}&m=${param.m}&g=${param.g}&orderBy=${param.orderBy}&order=${param.order}&providers=${param.providers}&l=${param.l}&status=${param.status}"/>
                        </c:import>
                    </div>
                </c:if>
                <c:if test="${searchMode == true}">
                    <div class="m-1">
                        <c:import url="/WEB-INF/jsp/common/pagination.jsp">
                            <c:param name="mediaPages" value="${numberOfPages}"/>
                            <c:param name="currentPage" value="${currentPage + 1}"/>
                            <c:param name="url" value="/discover?query=${param.query}&m=${param.m}&g=${param.g}&orderBy=${param.orderBy}&order=${param.order}&providers=${param.providers}&l=${param.l}&status=${param.status}"/>
                        </c:import>
<%--                        // discover?m=All&orderBy=tmdbRating&order=desc&g=&providers=&l=&status=--%>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

</div>

</body>


</html>
