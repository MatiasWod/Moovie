<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/likeButtons.css?version=1" rel="stylesheet"/>
    <title>Moovie-${media.name}</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body id="grad">
<c:import url="navBar.jsp">

</c:import>
<div class="container my-1">
    <div class="row align-items-center justify-content-center" style="margin-bottom: 20px">

        <!-- Poster -->
        <div class="col text-center">
            <c:choose>
                <c:when test="${empty media.posterPath}">
                    <img
                            src="${pageContext.request.contextPath}/resources/defaultPoster.png"
                            alt="${media.name} picture not found"
                            style="max-width: 100%; height: 400px; border-radius: 5px;">
                </c:when>
                <c:otherwise>
                    <img src="${media.posterPath}"
                         alt="${media.name} poster" class="img-fluid" width="300" height="300">
                </c:otherwise>
            </c:choose>

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
                        ${media.runtime}m
                        <span style="margin: 0 5px;">•</span>
                        Movie
                    </c:when>
                    <c:otherwise>
                        TV Series
                        <span style="margin: 0 5px;">•</span>
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
            <!-- Watch it on -->
            <div class="d-flex flex-row align-items-center">
                <div class="providers-container" style="max-width: 80%; overflow-x: auto; max-height: 200px;">
                    <!-- Providers content here -->
                    <c:forEach var="provider" items="${providersList}">
                        <span class="badge text-bg-light border border-black" style="margin: 3px;">
                            <img src="${provider.logoPath}" alt="${provider.providerName} logo not found"
                                 style="height: 1.6em; margin-right: 5px;">
                            ${provider.providerName}
                        </span>
                    </c:forEach>
                </div>
            </div>
            <!-- Genres -->
            <div class="d-flex flex-row  align-items-center ">
                <div style="margin-right: 10px">
                    <h5>Genres:</h5>
                </div>
                <div>
                    <c:forEach var="genre" items="${genresList}">
                        <a style="text-decoration: none;" href="${pageContext.request.contextPath}/discover?g=${genre}&media=${media.type? 'Series':'Movies'}">
                            <span class="badge text-bg-dark">${genre}</span>
                        </a>

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
                            <a href="${pageContext.request.contextPath}/discover?credit=${media.director}">
                                <span class="badge text-bg-light border border-black">${media.director}</span>
                            </a>
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
                    <c:if test="${creators.size()>0}">
                        <div class="d-flex flex-row align-items-center">
                            <div style="margin-right: 10px">
                                <c:choose>
                                    <c:when test="${creators.size()>1}">
                                        <h5>Creators:</h5>
                                    </c:when>
                                    <c:otherwise>
                                        <h5>Creator:</h5>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <c:forEach var="creator" items="${creators}">
                                    <a style="text-decoration: none;" href="${pageContext.request.contextPath}/discover?credit=${creator.creatorName.trim()}">
                                        <span class="badge text-bg-light border border-black">${creator.creatorName}</span>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
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
            <div class="flex-row d-flex">
                <div class="dropdown">
                    <div class="btn btn-dark dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"
                         style="margin-right: 10px"><i
                            class="bi bi-plus-circle-fill"></i> Add to list
                    </div>
                    <ul class="dropdown-menu scrollable-menu">
                        <c:forEach var="privateList" items="${privateLists}">
                            <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                <input type="hidden" name="listId" value="${privateList.moovieListId}"/>
                                <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                <li>
                                    <button class="dropdown-item" type="submit"><c:out
                                            value="${privateList.name}"/></button>
                                </li>
                            </form>
                        </c:forEach>
                        <c:forEach var="publicList" items="${publicLists}">
                            <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                <input type="hidden" name="listId" value="${publicList.moovieListId}"/>
                                <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                <li>
                                    <button class="dropdown-item" type="submit"><c:out
                                            value="${publicList.name}"/></button>
                                </li>
                            </form>
                        </c:forEach>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/createList?s=${media.mediaId}"><i
                                class="bi bi-plus-circle-fill"></i> Create new List</a></li>
                    </ul>
                </div>
                <button type="button" class="btn btn-light border border-black" onclick="openPopup('rate-popup')"><i
                        class="bi bi-star-fill"></i> Rate
                </button>
            </div>

        </div>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show m-2" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div>${errorMessage} <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}">${insertedMooovieList.name}</a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show m-2" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div>${successMessage} <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}">${insertedMooovieList.name}</a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
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
                                <c:choose>
                                    <c:when test="${actor.profilePath=='None'}">
                                        <img
                                                    src="${pageContext.request.contextPath}/resources/defaultProfile.jpg"
                                                    alt="${actor.actorName} picture not found"
                                                    style="max-width: 100px; height: 150px; border-radius: 5px;"

                                        >
                                    </c:when>
                                    <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/discover?credit=${actor.actorName}">
                                        <img
                                                src="${actor.profilePath}"
                                                alt="${actor.actorName} picture"
                                                style="max-width: 150px; max-height: 150px; border-radius: 5px;"
                                                href="${pageContext.request.contextPath}/discover?credit=${actor.actorName}">
                                    </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="col-8" style="min-width: 160px">
                                <div class="card-body" style="min-width: 120px">
                                    <a style="color:black; text-decoration: none;" href="${pageContext.request.contextPath}/discover?credit=${actor.actorName}">
                                        <h5 class="card-title">${actor.actorName}</h5>
                                    </a>
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

        <div class="popup-overlay rate-popup-overlay" onclick="closePopup('rate-popup')"></div>
        <div class="popup rate-popup">
            <!-- Popup content goes here -->
            <h2>Your rating of "${media.name}"</h2>
            <hr class="my-8">
            <div class="rating">
                <c:forEach var="i" begin="1" end="5" varStatus="loopStatus">
                    <c:set var="reverseIndex" value="${5 - loopStatus.count + 1}"/>
                    <i class="bi bi-star" onclick="rate(${reverseIndex})"></i>
                </c:forEach>
            </div>
            <h5>Your rating: <span id="selectedRating">Not selected</span></h5>

            <form:form modelAttribute="detailsForm" action="${pageContext.request.contextPath}/createrating"
                       method="POST">
                <form:textarea path="reviewContent" class="review-textarea" id="reviewContent" rows="3"
                               placeholder="Your review (Optional)" maxlength="500"/>
                <span><span id="charCount" class="text-muted">0</span>/500</span>
                <form:input path="mediaId" type="hidden" id="mediaId" value="${media.mediaId}"/>
                <form:input path="rating" type="hidden" id="rating"/>

                <form:errors path="reviewContent" cssClass="error"/>
                <form:errors path="rating" cssClass="error"/>
                <form:errors path="mediaId" cssClass="error"/>

                <!-- Submit Button -->
                <div class="text-center" style="margin-top: 20px">
                    <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                            onclick="closePopup('rate-popup')">
                        Cancel
                    </button>
                    <button type="submit" class="btn btn-dark" style="margin-inline: 10px" id="submitButton" disabled>
                        Submit
                    </button>
                </div>
            </form:form>

        </div>
        <!-- Reviews -->
        <h2>Reviews</h2>
        <hr class="my-8">
        <c:choose>
            <c:when test="${fn:length(reviewsList)>0}">
                <div class="scrollableDiv">
                    <c:forEach var="review" items="${reviewsList}">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <a href="${pageContext.request.contextPath}/profile/${review.username}"
                                           style="text-decoration: none; color: inherit;">
                                            <img class="cropCenter mr-3 review-profile-image rounded-circle"
                                                 style="height:60px;width:60px;border: solid black; border-radius: 50%"
                                                 src="${pageContext.request.contextPath}/profile/image/${review.username}"
                                                 alt="${review.userId} Reviewer Profile"
                                            >
                                        </a>
                                        <div class="mt-0" style="margin-left: 15px">
                                            <a href="${pageContext.request.contextPath}/profile/${review.username}"
                                               style="text-decoration: none; color: inherit;">
                                                <h5><c:out value="${review.username}"/></h5>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center justify-content-between">
                                        <h5 class="m-0">
                                            <i class="bi bi-star-fill ml-2"></i> ${review.rating}/5
                                        </h5>
                                        <sec:authorize access="hasRole('ROLE_MODERATOR')">
                                            <div class="text-center m-2" >
                                                <button onclick="openPopup('review${review.reviewId}')" class="btn btn-danger btn-sm">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </div>
                                            <div class="review${review.reviewId}-overlay popup-overlay" onclick="closePopup('review${review.reviewId}')"></div>
                                            <div style="background-color: transparent; box-shadow: none" class="popup review${review.reviewId}">
                                                <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                                                    <h5 class="alert-heading">Confirm Review Deletion</h5>
                                                    <p>Are you sure you want to delete this review? Once deleted, it cannot be recovered</p>
                                                    <div class="d-flex justify-content-evenly">
                                                        <form class="m-0" action="${pageContext.request.contextPath}/deleteReview/${media.mediaId}" method="post">
                                                            <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                            <input type="hidden" name="path" value="/details/${media.mediaId}"/>
                                                            <button type="submit" class="btn btn-danger">Delete</button>
                                                        </form>
                                                        <button type="button" onclick="closePopup('review${review.reviewId}')" class="btn btn-secondary" id="cancelModButton">Cancel</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </sec:authorize>
                                    </div>
                                </div>
                                <p>
                                    <c:out value="${review.reviewContent}"/>
                                </p>
                                <div class="d-flex align-items-center justify-content-start">
                                    <c:choose>
                                        <c:when test="${review.currentUserHasLiked}">
                                            <form action="${pageContext.request.contextPath}/unlikeReview"
                                                  method="post">
                                                <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                                <button class="btn like-btn-style" style="font-size: 14px">
                                        <span>
                                         <i class="bi bi-hand-thumbs-up-fill"></i>
                                        ${review.reviewLikes}
                                        </span>
                                                    Liked
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="${pageContext.request.contextPath}/likeReview" method="post">
                                                <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                                <button class="btn like-btn-style" style="font-size: 14px">
                                        <span>
                                    <i class="bi bi-hand-thumbs-up"></i>
                                        ${review.reviewLikes}
                                    </span>
                                                    Like
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
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
    <div class="m-1">
        <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
            <c:param name="mediaPages" value="${numberOfPages}"/>
            <c:param name="currentPage" value="${currentPage + 1}"/>
            <c:param name="url" value="/details/${media.mediaId}"/>
        </c:import>
    </div>
</div>
</body>
</html>

<script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=84"></script>