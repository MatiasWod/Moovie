<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=58" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Share your favorite media</title>
    <script>
        let selectedMediaNames = [];

        function displayMediaName(name) {
            selectedMediaNames.push(name);
            const selectedMediaDiv = document.getElementById("selected-media-names");
            selectedMediaDiv.innerHTML += name + "<br>";
        }

        function showSelectedMediaList() {
            const selectedMediaList = selectedMediaNames.join(', ');
            const listDiv = document.createElement('div');
            listDiv.innerHTML = '<h4>Lista de Media Seleccionada: ' + selectedMediaList + '</h4>';
            document.getElementById("preview-list").appendChild(listDiv);
        }

    </script>

</head>
<body style="background: whitesmoke">

<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>
    <c class="container d-flex flex-row ">
        <div class="container d-flex flex-column">
            <div class="scrollableDiv flex-wrap d-flex">
                <c:forEach var="movie" items="${mediaList}">
                    <div class="poster card text-bg-dark m-1" onclick="displayMediaName('${movie.name}')">
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
        <div id="preview" style="position: relative" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
            <img id="preview-img" style="" class="image-blur height-full background" src="https://image.tmdb.org/t/p/original/4m1Au3YkjqsxF8iwQy0fPYSxE0h.jpg">
            <div style="position: absolute;top: 0;left: 0" class="d-flex container flex-column">
                <h2 class="m-2" id="preview-title">Nombre:</h2>
                <input class="form-control me-2">
                <h2 class="m-2" id="preview-title">Email:</h2>
                <input class="form-control me-2">
                <div id="selected-media-names"></div>
<%--                ACA van la Media seleccionada! --%>
                <a id="preview-details" class="m-4 btn btn-outline-success align-bottom" onclick="showSelectedMediaList()">Crear lista</a>
                <div id="preview-list"></div>
            </div>
        </div>
    </c>

</div>

</body>
</html>
