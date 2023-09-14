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
    <link href="${pageContext.request.contextPath}/resources/main.css?version=58" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Discover your next favorite experience</title>
    <script>
        window.onload = function() {
            const filterTypesSelect = document.getElementById("filter-types");
            const genreSelect = document.getElementById("genre-select");

            if (filterTypesSelect.value === "Genre") {
                genreSelect.style.display = "block";
            }
        };
        function beforeSubmit() {
            const filterTypesSelect = document.getElementById("filter-types");
            const genreSelect = document.getElementById("genre-select");

            if (filterTypesSelect.value === "Popular") {
                genreSelect.removeAttribute("name");
            }
        }

        function loadPreview(title, rating, posterPath, overview) {
            document.getElementById("preview").style.display = 'block';
            document.getElementById("preview-title").innerText = title;
            document.getElementById("preview-rating").innerText = rating;
            document.getElementById("preview-img").src = posterPath;
            document.getElementById("preview-synopsis").innerText = overview;
        }
        function toggleGenreSelect() {
            const filterTypesSelect = document.getElementById("filter-types");
            const genreSelect = document.getElementById("genre-select");

            if (filterTypesSelect.value === "Genre") {
                genreSelect.style.display = "block";
            } else {
                genreSelect.style.display = "none";
            }
        }
    </script>
</head>
<body style="background: whitesmoke">
<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>
    <c class="container d-flex flex-row "> <%-- dos columnas (flex-row) izquierda-filtros->luego peliculas     derecha-preview --%>
<%--        FILTROS y PELIS    --%>

        <div class="container d-flex flex-column seventy-width">
            <div >
                <form class="mb-2 d-flex flex-row" action="${pageContext.request.contextPath}/discover" method="get" onsubmit="beforeSubmit()">
                <select name="media" class="form-select filter-width" aria-label="Filter!">
                        <option ${'Movies and Series' == param.media ? 'selected' : ''}>Movies and Series</option>
                        <option  ${'Movies' == param.media ? 'selected' : ''}>Movies</option>
                        <option  ${'Series' == param.media ? 'selected' : ''}>Series</option>
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
                </form>
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
        <div id="preview" style="position: relative; display:none !important" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
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
    </div>
</div>
</body>


</html>
