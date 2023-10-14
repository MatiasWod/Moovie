<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 10/14/2023
  Time: 6:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="${pageContext.request.contextPath}/resources/main.css?version=82" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">

    <title>Search</title>
</head>
<body>
<c:import url="navBar.jsp"/>

<div class="container d-flex flex-column">
    <%--TODO: test="${nameMedia}"--%>
    <c:if test="${nameMediaFlag}">
        <div class="container d-flex justify-content-between mt-2 p-2">
            <h3>Results for: ${param.query}</h3>
            <a href="${pageContext.request.contextPath}/discover?query=${param.query}">see more</a>
        </div>
        <hr class="my-1">
        <div class="container d-flex overflow-hidden" style="max-height: 300px;"> <!-- Set a fixed maximum height for the container -->
            <c:forEach var="media" items="${nameMedia}" end="5">
                <a href="${pageContext.request.contextPath}/details/${media.mediaId}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class="cropCenter" src="${media.posterPath}" alt="media poster">
                        <div class="card-img-overlay">
                            <h6 class="card-title text-center">${media.name}</h6>
                            <div class="d-flex justify-content-evenly">
                                <p class="card-text">
                                    <i class="bi bi-star-fill"></i>
                                        ${media.tmdbRating}
                                </p>
                                <p class="card-text">
                                    <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                                </p>
                            </div>
                            <div class="d-flex justify-content-evenly flex-wrap">
                                <c:forEach var="genre" items="${media.genres}" end="1">
                                    <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                </c:forEach>
                            </div>
                            <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                <c:forEach var="provider" items="${media.providers}" end="1">
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

    </c:if>

    <%--TODO: test="${creditMedia}"--%>
    <c:if test="${creditMediaFlag}">
        <div class="container d-flex justify-content-between mt-2 p-2">
            <h3>Credited media for: ${param.query}</h3>
            <a href="${pageContext.request.contextPath}/discover?credit=${param.query}">see more</a>
        </div>
        <hr class="my-1">
        <div class="container d-flex overflow-hidden" style="max-height: 300px;"> <!-- Set a fixed maximum height for the container -->
            <c:forEach var="media" items="${creditMedia}" end="5">
                <a href="${pageContext.request.contextPath}/details/${media.mediaId}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class="cropCenter" src="${media.posterPath}" alt="media poster">
                        <div class="card-img-overlay">
                            <h6 class="card-title text-center">${media.name}</h6>
                            <div class="d-flex justify-content-evenly">
                                <p class="card-text">
                                    <i class="bi bi-star-fill"></i>
                                        ${media.tmdbRating}
                                </p>
                                <p class="card-text">
                                    <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                                </p>
                            </div>
                            <div class="d-flex justify-content-evenly flex-wrap">
                                <c:forEach var="genre" items="${media.genres}" end="1">
                                    <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                </c:forEach>
                            </div>
                            <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                <c:forEach var="provider" items="${media.providers}" end="1">
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

    </c:if>

    <%--TODO: test="${userList}"--%>
<%--    <c:if test="${userList != null}">--%>
<%--        <div>--%>
<%--            Aqui van los users--%>
<%--        </div>--%>

<%--    </c:if>--%>
</div>


</body>
</html>
