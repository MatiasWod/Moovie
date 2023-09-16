<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            <c:set var="hours" value="${Math.floor(media.runtime / 60)}"/>
            <c:set var="minutes" value="${media.runtime % 60}"/>
            <h1>${media.name}</h1>
            <h5 style="display: flex; align-items: center">
                <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                <fmt:formatNumber var="formattedHours" value="${hours}"/>
                ${formattedHours}h ${minutes}m
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
            <!-- Director -->
            <div class="d-flex flex-row  align-items-center">
                <div style="margin-right: 10px">
                    <h5>Director:</h5>
                </div>
                <div>
                    <span class="badge text-bg-light border border-black">${media.director}</span>
                </div>
            </div>
            <!-- Description and Buttons-->
            <p>${media.overview}</p>

            <button type="button" class="btn btn-dark"><i class="bi bi-plus-circle-fill"></i> Add to list
            </button>
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
                        <button type="button" class="btn btn-dark show-more-button" onclick="showMoreActors()">
                            See more
                        </button>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="popup-overlay" onclick="closeReviewPopup()"></div>
        <div class="popup">
            <form id="reviewCreator" action="${pageContext.request.contextPath}/createreview" method="POST">
                <input type="hidden" name="mediaId" value="${media.mediaId}" />
                <!-- Popup content goes here -->
                <h2>Your rating of "${media.name}"</h2>
                <hr class="my-8">
                <div class="rating">
                    <c:forEach var="i" begin="1" end="10" varStatus="loopStatus">
                        <c:set var="reverseIndex" value="${10 - loopStatus.count + 1}"/>
                        <i class="bi bi-star" onclick="rate(${reverseIndex})"></i>
                    </c:forEach>
                    <input type="hidden" name="rating" id="rating" value="${reverseIndex}">
                </div>
                <h5>Your rating: <span id="selectedRating">Not selected</span></h5>
                <h3>
                    <label for="reviewContent">Leave a review also!</label>
                </h3>
                <textarea class="review-textarea" id="reviewContent" name="reviewContent" rows="3" placeholder="Your review (Optional)"></textarea>

                <h2 class="m-2">Email:</h2>
                <input type="text" class="form-control" id="userEmail" name="userEmail" required placeholder="Enter email">
                <hr class="my-8">

                <div class="text-center" style="margin-top: 20px">
                    <button type="button" class="btn btn-danger" style="margin-inline: 10px" onclick="closeReviewPopup()">
                        Cancel
                    </button>
                    <button type="submit" class="btn btn-dark" style="margin-inline: 10px" id="submitButton"
                            form="reviewCreator">
                        Submit
                    </button>
                </div>
            </form>
        </div>
        <!-- Reviews
        <h2>Reviews</h2>
        <hr class="my-8">
        <if test="${fn:length(reviews)==0}">
            <div class="scrollableDiv">
                <c:forEach begin="1" end="10" step="1">
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
                </c:forEach>
            </div>
        </if>
         -->
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

    var selectedRating = 0;
    var stars = document.querySelectorAll('.rating > i');

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

        document.getElementById("selectedRating").textContent = selectedRating;
        document.getElementById("submitButton").disabled = false;
    }

    function submitReview() {
        const rating = 1;
        const review = "Malo malo el bicho siuuu";
        const email = "jarnaude@itba.edu.ar";
        mediaId = 2;
       /* const rating = selectedRating;
        const review = document.getElementById("review").value;
        const email = document.getElementById("email").value;
        const reviewContent = document.getElementById("review").value;
        if (email === "") {
            alert("Please enter your email");
            return;
        }*/

        $.ajax({
            url:"/createreview",
            type:"POST",
            data: {
                userEmail: email,
                mediaId: media.mediaId,
                rating: rating,
                reviewContent: review
            },
            success: function(response){
                alert("Review sumbitted!")
            },
            error: function(error){
                alert("Error sumbitting review :c")
            }

        })



    }
</script>