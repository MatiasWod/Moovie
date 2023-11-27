<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title>Moovie List</title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=67" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=87"></script>

    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
</head>
<body>
<c:import url="../common/navBar.jsp"/>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mt-2 m-1">
        <button class="btn btn-sm btn-outline-success" onclick="history.back()"><spring:message code="cast.back"/></button>
    </div>

    <c:choose>
        <c:when test="${actor != null}">
            <div class="m-2">
                <div class="d-flex align-items-center justify-content-between">
                    <h1><c:out value="${actor.actorName}"/></h1>
                </div>
                <div class="d-flex align-items-center">
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                            <c:out value="${actor.medias.size()}"/>
                        </h5>
                    </div>
                </div>
            </div>
            <div class="scrollableDiv flex-wrap d-flex container">
                <c:if test="${fn:length(actor.medias) == 0 }">
                    <div class="d-flex m-2 flex-column">
                        <spring:message code="discover.noResults"/>
                        <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover"><spring:message code="discover.prompt"/></a>
                    </div>
                </c:if>
                <c:forEach var="movie" items="${actor.medias}" varStatus="loop">
                    <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                        <div class="card-img-container"> <!-- Add a container for the image -->
                            <sec:authorize access="isAuthenticated()">
                                <div style="position:absolute;bottom:0;left: 20%;z-index: 2" class="d-flex m-2">
<%--                                    COULD NOT MAKE TVCREATOR WATCHED & WATCHLIST STATUS WORK--%>
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
        </c:when>
        <c:when test="${tvCreator != null}">
            <div class="m-2">
                <div class="d-flex align-items-center justify-content-between">
                    <h1><c:out value="${tvCreator.creatorName}"/></h1>
                </div>
                <div class="d-flex align-items-center">
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                            <c:out value="${tvCreator.medias.size()}"/>
                        </h5>
                    </div>
                </div>
            </div>
            <div class="scrollableDiv flex-wrap d-flex container">
                <c:if test="${fn:length(tvCreator.medias) == 0 }">
                    <div class="d-flex m-2 flex-column">
                        <spring:message code="discover.noResults"/>
                        <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover"><spring:message code="discover.prompt"/></a>
                    </div>
                </c:if>
                <c:forEach var="movie" items="${tvCreator.medias}" varStatus="loop">
                    <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                        <div class="card-img-container"> <!-- Add a container for the image -->
                            <sec:authorize access="isAuthenticated()">
                                <div style="position:absolute;bottom:0;left: 20%;z-index: 2" class="d-flex m-2">
        <%--                                    COULD NOT MAKE TVCREATOR WATCHED & WATCHLIST STATUS WORK--%>
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

        </c:when>
        <c:when test="${directorMedia != null}">
            <div class="m-2">
                <div class="d-flex align-items-center justify-content-between">
                    <h1><c:out value="${directorMedia[0].director}"/></h1>
                </div>
                <div class="d-flex align-items-center">
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                            <c:out value="${directorMedia.size()}"/>
                        </h5>
                    </div>
                </div>
            </div>
            <div class="scrollableDiv flex-wrap d-flex container">
                <c:if test="${fn:length(directorMedia) == 0 }">
                    <div class="d-flex m-2 flex-column">
                        <spring:message code="discover.noResults"/>
                        <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/discover"><spring:message code="discover.prompt"/></a>
                    </div>
                </c:if>
                <c:forEach var="movie" items="${directorMedia}" varStatus="loop">
                    <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                        <div class="card-img-container"> <!-- Add a container for the image -->
                            <sec:authorize access="isAuthenticated()">
                                <div style="position:absolute;bottom:0;left: 20%;z-index: 2" class="d-flex m-2">
    <%--                                    COULD NOT MAKE TVCREATOR WATCHED & WATCHLIST STATUS WORK--%>
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

        </c:when>
    </c:choose>
</div>


