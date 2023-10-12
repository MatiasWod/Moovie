<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 10/12/2023
  Time: 12:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png"/>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>

    <title>Featured</title>
</head>
<body>
<c:import url="navBar.jsp"/>
<div class="container">
    <h1 class="mb-4">Lists by Moovie</h1>
    <div class="container d-flex justify-content-between p-2">
        <h3>Top Rated Movies</h3>
        <a href="${pageContext.request.contextPath}/discover?media=Movies">see more</a>
    </div>
    <hr class="my-1">
    <div class="container d-flex scrollableDiv">
        <c:forEach var="movie" items="${movieList}" end="5">
            <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                <div   class="card-img-container"> <!-- Add a container for the image -->
                    <img class="cropCenter" src="${movie.posterPath}" alt="${movie.name} poster">
                    <div class="card-img-overlay">
                        <h5 class="card-title">${movie.name}</h5>
                        <p class="card-text">${movie.tmdbRating}</p>
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
    <div class="container d-flex justify-content-between mt-2 p-2">
        <h3>Top Rated Series</h3>
        <a href="${pageContext.request.contextPath}/discover?media=Series">see more</a>
    </div>
    <hr class="my-1">
    <div class="container d-flex overflow-hidden" style="max-height: 300px;"> <!-- Set a fixed maximum height for the container -->
        <c:forEach var="series" items="${tvList}" end="5">
            <a href="${pageContext.request.contextPath}/details/${series.mediaId}" class="poster card text-bg-dark m-1">
                <div class="card-img-container" style="max-height: 100%; overflow: hidden;"> <!-- Set a maximum height for the image container -->
                    <img class="img-fluid" src="${series.posterPath}" alt="${series.name} poster" style="max-height: 100%; object-fit: cover;"> <!-- Use 'img-fluid' class for responsive images -->
                    <div class="card-img-overlay">
                        <h5 class="card-title">${series.name}</h5>
                        <p class="card-text">${series.tmdbRating}</p>
                    </div>
                </div>
            </a>
        </c:forEach>
    </div>
</div>
</body>
</html>
