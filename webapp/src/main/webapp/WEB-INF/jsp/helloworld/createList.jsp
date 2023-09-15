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
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Share your favorite media</title>
    <script src="${pageContext.request.contextPath}/resources/discoverFunctions.js"></script>
    <script>
<%--        asumo que el parametro contiene los Ids de la Media seleccionada.
            entonces --> el controller debe cargar esas peliculas desde los IDs (server-side)
            porque la lista de media que devuelve la busqueda/filtros puede no contener la media seleccionada anteriormente
            por lo tanto
            onLoad (hay un ejemplo en discoverFunctions.js) cargo selectedMedia
            con los datos necesarios desde una variable JSTL previousSelections
            for element in previousSelections: selectedMedia.push(element)     --%>
        let selectedMedia = [];
        let selectedMediaId = [];


        function displayMediaName(name, id) {
            selectedMedia.push(name);
            selectedMediaId.push(id);
            const selectedMediaDiv = document.getElementById("selected-media-names");
            const newElement = document.createElement('div');
            newElement.id = "list-element-preview";
            newElement.className = "d-flex justify-content-between";
            newElement.innerHTML = '<a>' + name +
                '</a>' +
                '<i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>';
            selectedMediaDiv.appendChild(newElement);
            updateSelectedMediaInput();
        }

        function updateSelectedMediaInput() {
            const selectedMediaInput = document.getElementById("selected-media-input");
            selectedMediaInput.value = JSON.stringify(selectedMediaId);
        }

        function deleteMedia(element) {
            const name = element.previousElementSibling.innerText;
            const index = selectedMedia.indexOf(name);
            if (index !== -1) {
                selectedMedia.splice(index, 1);
                selectedMediaId.splice(index, 1);

            }
            element.parentElement.remove();
            updateSelectedMediaInput();
        }

        function resetSelectedMediaNames() {
            const selectedMediaDiv = document.getElementById("selected-media-names");
            selectedMediaDiv.innerHTML = '';
            selectedMedia = [];
            selectedMediaId = [];
            updateSelectedMediaInput();
        }

        function showSelectedMediaList() {
            const selectedMediaList = selectedMedia.join(', ');
            if (selectedMediaList.trim() === '') return;

            const existingList = document.getElementById('list-result');

            if (!existingList) {
                const listDiv = document.createElement('div');
                listDiv.innerHTML = '<div class="d-flex flex-column">' +
                    '<h4 id="result-title">Created Media List: </h4>' +
                    '<div style="max-height: 150px" id="list-result" class="container d-flex scrollableMedia">' + selectedMediaList + '</div>' +
                    '</div>';
                document.getElementById("preview-list").appendChild(listDiv);
            } else {
                existingList.innerHTML = selectedMediaList;
            }
            resetSelectedMediaNames();
        }

    </script>

</head>
<body style="background: whitesmoke">

<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>
    <c class="container d-flex flex-row ">
        <div class="container d-flex flex-column">
            <div >
                <form class="mb-2 d-flex flex-row justify-content-between" action="${pageContext.request.contextPath}/createList" method="get" onsubmit="beforeSubmit()">
                    <input type="hidden" id="selected-media-input" name="s" />
                    <div class="d-flex flex-row">
                        <select name="m" class="form-select filter-width" aria-label="Filter!">
                            <option ${'Movies and Series' == param.m ? 'selected' : ''}>Movies and Series</option>
                            <option  ${'Movies' == param.m ? 'selected' : ''}>Movies</option>
                            <option  ${'Series' == param.m ? 'selected' : ''}>Series</option>
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
                    </div>
                    <div class="d-flex flex-row">
                        <form class="d-flex mb-0" role="search" action="${pageContext.request.contextPath}/createList" method="get">
                            <input class="form-control me-2" type="search" name="q" value="${param.q}" placeholder="Search" aria-label="Search">
                            <button class="btn btn-outline-success" type="submit">Search</button>
                        </form>
                    </div>

                </form>
            </div>
            <div class="scrollableDiv flex-wrap d-flex">
                <c:forEach var="movie" items="${mediaList}">
                    <div class="poster card text-bg-dark m-1" onclick="displayMediaName('${movie.name}','${movie.mediaId}')">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                            <img class="height-full" src="${movie.posterPath}" alt="poster image">
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
            <img id="preview-img" class="image-blur height-full background" src="https://image.tmdb.org/t/p/original/4m1Au3YkjqsxF8iwQy0fPYSxE0h.jpg">
            <div style="position: absolute;top: 0;left: 0" class="d-flex p-4 container flex-column">
                <h2 class="m-2" id="preview-title">List Name:</h2>
                <label>
                    <input class="form-control me-2">
                </label>
                <h3 class="m-2" id="preview-title">Email:</h3>
                <label>
                    <input class="form-control me-2">
                </label>
                <div class="scrollableMedia d-flex flex-column m-2 p-2" id="selected-media-names"></div>
                <%-- ACA van la Media seleccionada! --%>
                <a id="preview-details" class="m-4 btn btn-outline-success align-bottom" onclick="showSelectedMediaList()">Create List</a>
                <div class="d-flex" id="preview-list"></div>
            </div>
        </div>
    </c>

</div>

</body>
</html>
