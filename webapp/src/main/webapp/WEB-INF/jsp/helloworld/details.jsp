<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
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
            <h1>${media.name} <sup class="badge text-bg-light border border-black"
                                   style="font-size: 14px;">${media.status}</sup>
                <sup class="badge text-bg-light border border-black"
                     style="font-size: 14px;">${media.originalLanguage}</sup>

            </h1>

            <h5 style="display: flex; align-items: center">
                <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>

                <!--Separator -->
                <span style="margin: 0 5px;">•</span>

                <c:choose>
                    <c:when test="${media.type==false}">
                        <c:set var="hours" value="${Math.floor(media.runtime / 60)}"/>
                        <c:set var="minutes" value="${media.runtime % 60}"/>
                        <fmt:formatNumber var="formattedHours" value="${hours}"/>
                        ${formattedHours}h ${minutes}m
                    </c:when>
                    <c:otherwise>
                        ${media.numberOfSeasons}
                        <c:choose>
                            <c:when test="${media.numberOfSeasons == 1}">
                                Season
                            </c:when>
                            <c:otherwise>
                                Seasons
                            </c:otherwise>
                        </c:choose>
                        <span style="margin: 0 5px;">•</span>
                        ${media.numberOfEpisodes}
                        <c:choose>
                            <c:when test="${media.numberOfEpisodes == 1}">
                                Episode
                            </c:when>
                            <c:otherwise>
                                Episodes
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </h5>
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
            <!-- Media data -->
            <c:choose>
                <c:when test="${media.type==false}">
                    <div class="d-flex flex-row  align-items-center">
                        <div style="margin-right: 10px">
                            <h5>Director:</h5>
                        </div>
                        <div>
                            <span class="badge text-bg-light border border-black">${media.director}</span>
                        </div>
                    </div>
                    <c:if test="${media.budget != 0}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5>Budget:</h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black" id="budget">${media.budget}</span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${media.revenue != 0}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5>Revenue:</h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black"
                                      id="revenue">${media.revenue}</span>
                            </div>
                        </div>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:if test="${media.lastAirDate != null}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5>Last Air Date:</h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black">${media.lastAirDate}</span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${media.nextEpisodeToAir != null}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5>Next episode to air:</h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black">${media.nextEpisodeToAir}</span>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <!-- Description and Buttons-->
            <p>${media.overview}</p>
            <!--
            <button type="button" class="btn btn-dark"><i class="bi bi-plus-circle-fill"></i> Add to list
            </button> -->
            <button type="button" class="btn btn-light border border-black" onclick="openReviewPopup()"><i
                    class="bi bi-star-fill"></i> Rate
            </button>
        </div>
        <!-- Cast -->
        <div class="row ">
            <h2>Cast</h2>
            <hr class="my-8">
            <div class="flex-wrap d-flex align-items-center justify-content-center container" id="actors-container">
                <c:forEach var="actor" items="${actorsList}">
                    <div class="card actor-card" id="actor-card"
                         style="width: 300px;height: 152px; border-radius: 5px; margin: 5px; display:none !important; position: relative; overflow: hidden;">
                        <div class="row">
                            <div class="col-4 text-center">
                                <img
                                        src="${actor.profilePath}"
                                        alt="${actor.actorName} picture not found"
                                        style="max-width: 150px; max-height: 150px; border-radius: 5px;">
                            </div>
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
                        <button type="button" class="btn btn-dark show-more-button" onclick="showMoreActors()">
                            See more
                        </button>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="popup-overlay" onclick="closeReviewPopup()"></div>
        <div class="popup">
            <!-- Popup content goes here -->
            <h2>Your rating of "${media.name}"</h2>
            <hr class="my-8">
            <div class="rating">
                <c:forEach var="i" begin="1" end="10" varStatus="loopStatus">
                    <c:set var="reverseIndex" value="${10 - loopStatus.count + 1}"/>
                    <i class="bi bi-star" onclick="rate(${reverseIndex})"></i>
                </c:forEach>
            </div>
            <h5>Your rating: <span id="selectedRating">Not selected</span></h5>

            <form action="/createrating" method="POST">
                <textarea class="review-textarea" id="reviewContent" rows="3" name="reviewContent"
                          placeholder="Your review (Optional)"></textarea>
                <h2 class="m-2">Email:</h2>
                <input type="text" class="form-control" id="userEmail" name="userEmail" required
                       placeholder="Enter email">
                <input type="hidden" id="mediaId" name="mediaId" value="${media.mediaId}">
                <input type="hidden" id="rating" name="rating">
                <!-- Submit Button -->
                <div class="text-center" style="margin-top: 20px">
                    <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                            onclick="closeReviewPopup()">
                        Cancel
                    </button>
                    <button type="submit" class="btn btn-dark" style="margin-inline: 10px" id="submitButton" disabled>
                        Submit
                    </button>
                </div>
            </form>
        </div>
        <!-- Reviews -->
        <h2>Reviews</h2>
        <hr class="my-8">
        <c:choose>
            <c:when test="${fn:length(reviewList)>0}">
                <div class="scrollableDiv">
                    <c:forEach var="review" items="${reviewList}">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <img src="https://m.media-amazon.com/images/M/MV5BNjE3NDQyOTYyMV5BMl5BanBnXkFtZTcwODcyODU2Mw@@._V1_FMjpg_UX1000_.jpg"
                                             alt="${review.userId} Reviewer Profile" class="mr-3 rounded-circle"
                                             width="64" height="64">
                                        <div class="mt-0" style="margin-left: 15px">
                                            <h5>${review.userId}</h5>
                                            <!-- <div class="text-body-secondary">
                                                24 reviews
                                            </div>
                                            -->
                                        </div>
                                    </div>
                                    <h5 class="align-items-left"><i class="bi bi-star-fill ml-2"></i> ${review.rating}/10
                                    </h5>
                                </div>
                                <p>
                                        ${review.reviewContent}
                                </p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center">
                    <h3>No reviews yet</h3>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>

<script>
    const initialCardsNumber = 4;
    window.onload = function () {
        const actorCards = document.querySelectorAll(".actor-card");

        // Loop through all actor cards and toggle their display
        for (let i = 0; i < initialCardsNumber; i++) {
            actorCards[i].style.display = "block";
        }
    };

    function showMoreActors() {
        const actorCards = document.querySelectorAll(".actor-card");

        // Loop through all actor cards and toggle their display
        for (let i = initialCardsNumber; i < actorCards.length; i++) {
            actorCards[i].style.display = "block";

        }

        // Scroll to the middle of the actors container
        const actorsContainer = document.querySelector("#actors-container");
        actorsContainer.scrollIntoView({behavior: "smooth", block: "center"});


        // Change "See More" button to "See Less"
        const seeMoreButton = document.querySelector(".show-more-button");
        seeMoreButton.innerHTML = "See less";
        seeMoreButton.onclick = showLessActors;
    }

    function showLessActors() {
        const actorCards = document.querySelectorAll(".actor-card");

        // Loop through all actor cards and toggle their display
        for (let i = initialCardsNumber; i < actorCards.length; i++) {
            actorCards[i].style.display = "none";
        }

        // Change "See Less" button to "See More"
        const seeMoreButton = document.querySelector(".show-more-button");
        seeMoreButton.innerHTML = "See more";
        seeMoreButton.onclick = showMoreActors;
    }

    function openReviewPopup() {
        const overlay = document.querySelector(".popup-overlay");
        const popup = document.querySelector(".popup");

        overlay.style.display = "block";
        popup.style.display = "block";
    }

    function closeReviewPopup() {
        const overlay = document.querySelector(".popup-overlay");
        const popup = document.querySelector(".popup");

        overlay.style.display = "none";
        popup.style.display = "none";
    }

    let selectedRating = 0;
    let stars = document.querySelectorAll('.rating > i');

    function rate(starsClicked) {
        selectedRating = starsClicked;

        // Remove 'bi-star' class and add 'bi-star-fill' class for selected stars
        stars.forEach(function (star, index) {
            if (index >= (10 - starsClicked)) {
                star.classList.remove('bi-star');
                star.classList.add('bi-star-fill');
                star.classList.add('selected')
            } else {
                star.classList.remove('bi-star-fill');
                star.classList.remove('selected')
                star.classList.add('bi-star');
            }
        });
        document.getElementById("rating").value = selectedRating;
        document.getElementById("selectedRating").textContent = selectedRating;
        document.getElementById("submitButton").disabled = false;
    }

    // Function to format a number with commas and dots and add a dollar sign
    function formatNumberWithDollarSign(number) {
        return '$' + number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

    }

    // Function to format revenue and budget elements
    function formatRevenueAndBudget() {
        const revenueElement = document.getElementById("revenue");
        const budgetElement = document.getElementById("budget");

        formatElementValue(revenueElement);
        formatElementValue(budgetElement);
    }

    // Function to format a specific element's value
    function formatElementValue(element) {
        if (element) {
            const elementValue = parseFloat(element.textContent);
            if (!isNaN(elementValue)) {
                element.textContent = formatNumberWithDollarSign(elementValue);
            }
        }
    }

    // Call the function to format revenue and budget when the page loads
    window.addEventListener("load", formatRevenueAndBudget);

</script>