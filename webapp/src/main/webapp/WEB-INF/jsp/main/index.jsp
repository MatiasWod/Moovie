<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=79" rel="stylesheet"/>
    <title><spring:message code="index.title"/></title>
</head>
<body style="background: whitesmoke;">
    <c:import url="../common/navBar.jsp"/>
    <div class="container d-flex flex-column" style="margin-bottom: 50px">
        <div style="position: page;" class="d-flex flex-column align-items-center justify-content-center">
            <div class="container d-flex flex-column justify-content-end">
                <div class="container d-flex justify-content-center p-4">
                    <h1 class="text-center"><spring:message code="index.immerse"/><br><spring:message code="index.discover"/></h1>
                </div>
                <div class="container d-flex justify-content-between p-2">
                    <h3><spring:message code="index.topRatedMovies"/></h3>
                    <a href="${pageContext.request.contextPath}/featuredList/topRatedMovies"><spring:message code="index.seeMore"/></a>
                </div>
                <hr class="my-1">
                <div class="container dflex- scrollableDiv">
                    <c:forEach var="movie" items="${movieList}" end="5">
                        <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                            <div class="card-img-container"> <!-- Add a container for the image -->
                                <img class="cropCenter" src="${movie.posterPath}" alt="media poster">
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

                <div class="container d-flex justify-content-between p-2">
                    <h3><spring:message code="index.mostPopularMovies"/></h3>
                    <a href="${pageContext.request.contextPath}/featuredList/mostPopularMovies"><spring:message code="index.seeMore"/></a>
                </div>
                <hr class="my-1">
                <div class="container d-flex scrollableDiv">
                    <c:forEach var="movie" items="${movieListPopular}" end="5">
                        <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                            <div class="card-img-container"> <!-- Add a container for the image -->
                                <img class="cropCenter" src="${movie.posterPath}" alt="media poster">
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

                <div class="container d-flex justify-content-between mt-2 p-2">
                    <h3><spring:message code="index.topRatedSeries"/></h3>
                    <a href="${pageContext.request.contextPath}/featuredList/topRatedSeries"><spring:message code="index.seeMore"/></a>
                </div>
                <hr class="my-1">
                <div class="container d-flex overflow-hidden" style="max-height: 300px;"> <!-- Set a fixed maximum height for the container -->
                        <c:forEach var="series" items="${tvList}" end="5">
                            <a href="${pageContext.request.contextPath}/details/${series.mediaId}" class="poster card text-bg-dark m-1">
                                <div class="card-img-container"> <!-- Add a container for the image -->
                                    <img class="cropCenter" src="${series.posterPath}" alt="media poster">
                                    <div class="card-img-overlay">
                                        <h6 class="card-title text-center"><c:out value="${series.name}"/></h6>
                                        <div class="d-flex justify-content-evenly">
                                            <p class="card-text">
                                                <i class="bi bi-star-fill"></i>
                                                    <c:out value="${series.tmdbRating}"/>
                                            </p>
                                            <p class="card-text">
                                                <fmt:formatDate value="${series.releaseDate}" pattern="YYYY"/>
                                            </p>
                                        </div>
                                        <div class="d-flex justify-content-evenly flex-wrap">
                                            <c:forEach var="genre" items="${series.genres}" end="1">
                                                <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                            </c:forEach>
                                        </div>
                                        <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                            <c:forEach var="provider" items="${series.providers}" end="1">
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

                <div class="container d-flex justify-content-between mt-2 p-2">
                    <h3><spring:message code="index.mostPopularSeries"/></h3>
                    <a href="${pageContext.request.contextPath}/featuredList/mostPopularSeries"><spring:message code="index.seeMore"/></a>
                </div>
                <hr class="my-1">
                <div class="container d-flex overflow-hidden" style="max-height: 300px;"> <!-- Set a fixed maximum height for the container -->
                    <c:forEach var="series" items="${tvListPopular}" end="5">
                        <a href="${pageContext.request.contextPath}/details/${series.mediaId}" class="poster card text-bg-dark m-1">
                            <div class="card-img-container"> <!-- Add a container for the image -->
                                <img class="cropCenter" src="${series.posterPath}" alt="media poster">
                                <div class="card-img-overlay">
                                    <h6 class="card-title text-center"><c:out value="${series.name}"/></h6>
                                    <div class="d-flex justify-content-evenly">
                                        <p class="card-text">
                                            <i class="bi bi-star-fill"></i>
                                                <c:out value="${series.tmdbRating}"/>
                                        </p>
                                        <p class="card-text">
                                            <fmt:formatDate value="${series.releaseDate}" pattern="YYYY"/>
                                        </p>
                                    </div>
                                    <div class="d-flex justify-content-evenly flex-wrap">
                                        <c:forEach var="genre" items="${series.genres}" end="1">
                                            <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                        </c:forEach>
                                    </div>
                                    <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                        <c:forEach var="provider" items="${series.providers}" end="1">
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
            </div>
        </div>
    </div>
</body>
</html>
