<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=56" rel="stylesheet"/>
    <title>Discover your next favorite experience</title>
</head>
<body style="background: whitesmoke">
<div class="container d-flex flex-column">
    <div class="container d-flex flex-row"> <%-- dos columnas (flex-row) izquierda-filtros->luego peliculas     derecha-preview --%>
        <%--        FILTROS y PELIS    --%>

        <div class="container d-flex flex-column flex-grow-1 scrollableDiv">
            <div class="mb-2 d-flex flex-row">
                <select class="form-select filter-width" aria-label="Filter!">
                    <option selected>Movies</option>
                    <option>Series</option>
                </select>
                <select class="form-select filter-width" aria-label="Filter!">
                    <option selected>Popular</option>
                    <option>By Year</option>
                    <option>By Language</option>
                    <option>Trending</option>
                </select>
                <select class="form-select filter-width" aria-label="Filter!">
                    <option selected>2023</option>
                    <option>2022</option>
                </select>
            </div>
            <div class="flex-wrap d-flex">
                <c:forEach var="media" items="${mediaList}">
                    <div class="poster card text-bg-dark m-1">
                        <div class="card-img-container"> <!-- Add a container for the image -->
                            <img style="height: 200px" src="${media.posterPath}">
                            <div class="card-img-overlay">
                                <h5 class="card-title">${media.name}</h5>
                                <p class="card-text">${media.tmdbRating}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>
    </div>
</div>
</body>
</html>
