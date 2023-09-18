<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=79" rel="stylesheet"/>
    <title>Moovie</title>
</head>
<body style="background: whitesmoke;">
<div class="container d-flex flex-column">
    <div style="position: relative;" class="container d-flex flex-column">
        <c:import url="imageBackdrop.jsp"/>
    </div>
    <c:import url="navBar.jsp"/>

    <div style="margin-top: calc(100vh - 250px);" class="container d-flex flex-column justify-content-end">
        <div class="container d-flex justify-content-center p-4">
            <h1 class="text-center">Immerse Yourself in Movies and Series, <br> Discover Your Next Favorite Experience.</h1>
        </div>
        <%--<div class="d-flex justify-content-center">
            <button class="btn btn-success btn-lg">GET STARTED - SIGN IN</button>
        </div>--%>
        <div class="container d-flex justify-content-between p-2">
            <h3>Discover Your Next Favorite Movies</h3>
            <a href="${pageContext.request.contextPath}/discover?media=Movies">see more</a>
        </div>
        <hr class="my-1">
        <div class="container d-flex scrollableDiv">
            <c:forEach var="movie" items="${movieList}" end="5">
                <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="poster card text-bg-dark m-1">
                    <div   class="card-img-container"> <!-- Add a container for the image -->
                        <img class="height-full cropCenterLanding" src="${movie.posterPath}" alt="${movie.name} poster">
                        <div class="card-img-overlay">
                            <h5 class="card-title">${movie.name}</h5>
                            <p class="card-text">${movie.tmdbRating}</p>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>

        <div class="container d-flex justify-content-between mt-2 p-2">
            <h3>Discover Your Next Favorite Series</h3>
            <a href="${pageContext.request.contextPath}/discover?media=Series">see more</a>
        </div>
        <hr class="my-1">
        <div class="container d-flex overflow-scroll">
            <c:forEach var="series" items="${tvList}" end="5">
                <a href="${pageContext.request.contextPath}/details/${series.mediaId}" class="poster card text-bg-dark m-1">
                    <div  class="card-img-container"> <!-- Add a container for the image -->
                        <img class="height-full cropCenterLanding" src="${series.posterPath}" alt="${series.name} poster">
                        <div class="card-img-overlay">
                            <h5 class="card-title">${series.name}</h5>
                            <p class="card-text">${series.tmdbRating}</p>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>

    </div>
</div>
</body>
</html>
