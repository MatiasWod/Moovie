<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css" rel="stylesheet"/>
    <title>Moovie-${media.name}</title>
</head>
<body id="grad">
<c:import url="navBar.jsp"/>

<div class="container my-1">
    <div class="row align-items-center justify-content-center">
        <!-- Poster -->
        <div class="col text-center">
            <img src="${media.posterPath}"
                 alt="${media.name} poster image not found" class="img-fluid" width="300" height="300">
        </div>
        <div class="col">
            <!-- Title and Details -->
            <c:set var="hours" value="${Math.floor(media.runtime / 60)}"/>
            <c:set var="minutes" value="${media.runtime % 60}"/>
            <h1>${media.name}</h1>
            <h5 style="display: flex; align-items: center">
                <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                <fmt:formatNumber var="formattedHours" value="${hours}"/>
                ${formattedHours}h ${minutes}m</h5>

            <!-- Ratings -->
            <h1>
                <i class="bi bi-star-fill"></i>
                ${media.tmdbRating}
            </h1>
            <!-- Genres -->
            <div class="d-flex flex-row  align-items-center ">
                <div style="margin-right: 10px">
                    <h5>Genres:</h5>
                </div>
                <div>
                    <c:forEach var="genre" items="${genresList}">
                        <span class="badge text-bg-dark">${genre.genre}</span>
                    </c:forEach>
                </div>
            </div>
            <!-- Director -->
            <div class="d-flex flex-row  align-items-center">
                <div style="margin-right: 10px">
                    <h5>Director:</h5>
                </div>
                <div>
                    <span class="badge text-bg-light border border-black">${media.director}</span>
                </div>
            </div>
            <!-- Description -->
            <p>${media.overview}</p>

            <button type="button" class="btn btn-dark"><i class="bi bi-plus-circle-fill"></i> Add to list
            </button>
            <button type="button" class="btn btn-light border border-black"><i class="bi bi-star-fill"></i> Rate
            </button>
        </div>
        <!-- Cast -->
        <div class="row ">
            <h2>Cast</h2>
            <hr class="my-8">
            <div class="flex-wrap d-flex align-items-center container">
                <c:forEach var="actor" items="${actorsList}">
                    <div class="card actor-card" id="actor-card"
                         style="max-width: 300px;border-radius: 5px; margin: 5px; display:none !important">
                        <div class="row">
                            <div class="col-4">
                                <img
                                        src="${actor.profilePath}"
                                        alt="${actor.actorName} picture not found"
                                        style="max-width: 150px; max-height: 150px; border-radius: 5px;"></div>
                            <div class="col-8" style="min-width: 160px">
                                <div class="card-body" style="min-width: 120px">
                                    <h5 class="card-title">${actor.actorName}</h5>
                                    <p class="card-text">${actor.characterName}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="text-center" style="align-items: center">
                <c:if test="${fn:length(actorsList) > 4}">
                    <div class="p-2 align-items-center">
                        <button type="button" class="btn btn-dark show-more-button" onclick="showMoreActors()">See
                            more
                        </button>
                    </div>
                </c:if>
            </div>
        </div>


        <!-- Reviews -->
        <!--
        <h2>Reviews</h2>
        <hr class="my-8">
        <div class="scrollableDiv">
            <forEach begin="1" end="10" step="1">
                <div class="card mb-3">
                    <div class="card-body">
                        <div class="d-flex align-items-center justify-content-between">
                            <div class="d-flex align-items-center">
                                <img src="https://m.media-amazon.com/images/M/MV5BNjE3NDQyOTYyMV5BMl5BanBnXkFtZTcwODcyODU2Mw@@._V1_FMjpg_UX1000_.jpg"
                                     alt="Reviewer Profile" class="mr-3 rounded-circle" width="64" height="64">
                                <div class="mt-0" style="margin-left: 15px">
                                    <h5>Reviewer Username</h5>
                                    <div class="text-body-secondary">
                                        24 reviews
                                    </div>
                                </div>
                            </div>
                            <h5 class="align-items-left"><i class="bi bi-star-fill ml-2"></i> 8/10</h5>
                        </div>

                        <p>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed id tincidunt libero, sed
                            placerat
                            dolor. Fusce vehicula turpis vitae odio facilisis, ut euismod orci varius. Nulla
                            facilisi.
                            Curabitur vel semper odio.
                        </p>
                    </div>
                </div>
            </forEach>
        </div>
        -->
    </div>
</div>
</body>
</html>

<script>
    window.onload = function() {
        const actorCards = document.querySelectorAll(".actor-card");

        // Loop through all actor cards and toggle their display
        for (let i = 0; i < 4; i++) {
            actorCards[i].style.display = "block";
        }
    };
    function showMoreActors() {
        const actorCards = document.querySelectorAll(".actor-card");

        // Loop through all actor cards and toggle their display
        for (let i = 0; i < actorCards.length; i++) {
            actorCards[i].style.display = "block";

        }


        // Change "See More" button to "See Less"
        const seeMoreButton = document.querySelector(".show-more-button");
        seeMoreButton.innerHTML = "See less";
        seeMoreButton.onclick = showLessActors;
    }

    function showLessActors() {
        const actorCards = document.querySelectorAll(".actor-card");

        // Loop through all actor cards and toggle their display
        for (let i = 4; i < actorCards.length; i++) {
            actorCards[i].style.display = "none";
        }

        // Change "See Less" button to "See More"
        const seeMoreButton = document.querySelector(".show-more-button");
        seeMoreButton.innerHTML = "See more";
        seeMoreButton.onclick = showMoreActors;
    }
</script>