<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=57" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Discover your next favorite experience</title>
    <script>
        function loadPreview(title, rating, posterPath, overview) {
            document.getElementById("preview-title").innerText = title;
            document.getElementById("preview-rating").innerText = rating;
            document.getElementById("preview-img").src = posterPath;
            document.getElementById("preview-synopsis").innerText = overview;
        }
    </script>
</head>
<body style="background: whitesmoke">
<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>
    <div class="container d-flex flex-row "> <%-- dos columnas (flex-row) izquierda-filtros->luego peliculas     derecha-preview --%>
<%--        FILTROS y PELIS    --%>

        <div class="container d-flex flex-column seventy-width">
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
            <div class="scrollableDiv flex-wrap d-flex">
                <c:forEach var="movie" items="${mediaList}" end="25">
<%--                    <div class="poster card text-bg-dark m-1" onclick="loadPreview('${movie.name}', '${movie.tmdbRating}', '${movie.posterPath}', '${movie.overview}')">--%>
                    <div class="poster card text-bg-dark m-1" onclick="loadPreview('${fn:replace(movie.name, "'", "\\'")}', '${movie.tmdbRating}', '${movie.posterPath}', '${fn:replace(movie.overview, "'", "\\'")}')">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                            <img class="height-full" src="${movie.posterPath}">
                            <div class="card-img-overlay">
                                <h5 class="card-title">${movie.name}</h5>
                                <p class="card-text">${movie.tmdbRating}</p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>
<%--        PREVIEW      --%>
        <c:if test="true" >
        <div style="position: relative" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
            <img id="preview-img" style="" class="image-blur height-full background" src="https://image.tmdb.org/t/p/original/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg" alt="poster">
            <div style="position: absolute;top: 0;left: 0" class="d-flex container flex-column">
                <h2 id="preview-title">Movie Title</h2>
                <div class="d-flex flex-row align-items-center">
                    <h1>
                        <i class="bi bi-star-fill"></i>
                    </h1>
                    <h1 id="preview-rating">10/10</h1>
                </div>

                <p id="preview-synopsis">La Barbie Movie ha emergido como una verdadera obra maestra cultural que ha impactado profundamente a la sociedad en diversos niveles.
                    En primer lugar, esta película representa un hito en la historia del cine al ofrecer una narrativa única y atractiva que se aleja de los estereotipos de género tradicionales.
                    A través de su trama, la Barbie Movie rompe con las limitaciones convencionales que han restringido durante mucho tiempo a las niñas y niños a roles predefinidos.
                    Al presentar a Barbie como una figura empoderada, inteligente y valiente, la película desafía los estereotipos de género y fomenta la idea de que las personas,
                    independientemente de su género, pueden aspirar a cualquier cosa. Esto tiene un impacto positivo en la cultura al promover la igualdad de género y mostrar que todos merecen
                    la oportunidad de perseguir sus sueños y pasiones sin restricciones.
                </p>
            </div>
        </div>
        </c:if>
    </div>
</div>
</body>


</html>
