<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <title><spring:message code="details.title" arguments="${media.name}"/></title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body id="grad">
<c:import url="../common/navBar.jsp">

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
                    <img src="${media.posterPath}" alt="${media.name} poster" class="img-fluid" style="width: 70%; height: 70%;">
                </c:otherwise>
            </c:choose>

        </div>
        <div class="col">
            <!-- Title and Details -->
            <h1><c:out value="${media.name}"/> <a href="${pageContext.request.contextPath}/discover?status=${media.status}"><sup class="badge text-bg-light border border-black"
                                      style="font-size: 14px;"><c:out value="${media.status}"/></sup></a>
                <a href="${pageContext.request.contextPath}/discover?l=${media.originalLanguage}"><sup class="badge text-bg-light border border-black"
                        style="font-size: 14px;"><c:out value="${media.originalLanguage}"/></sup></a>
            </h1>

            <h5 style="display: flex; align-items: center">
                <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                <!--Separator -->
                <span style="margin: 0 5px;">•</span>

                <c:choose>
                    <c:when test="${media.type==false}">
                        <c:out value="${media.runtime}"/>m
                        <span style="margin: 0 5px;">•</span>
                        <spring:message code="details.movie"/>
                    </c:when>
                    <c:otherwise>
                        <spring:message code="details.series"/>
                        <span style="margin: 0 5px;">•</span>
                        <c:out value="${media.numberOfSeasons}"/>
                        <c:choose>
                            <c:when test="${media.numberOfSeasons == 1}">
                                <spring:message code="details.season"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="details.seasons"/>
                            </c:otherwise>
                        </c:choose>
                        <span style="margin: 0 5px;">•</span>
                        <c:out value="${media.numberOfEpisodes}"/>
                        <c:choose>
                            <c:when test="${media.numberOfEpisodes == 1}">
                                <spring:message code="details.episode"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="details.episodes"/>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </h5>

            <!-- Ratings -->
            <div>

                <h1 class="d-flex flex-row align-items-center">
                    <div data-bs-toggle="popover" data-bs-trigger="hover"
                         data-bs-content="<spring:message code="details.popoverMoovieRating"/>">
                        <i class="bi bi-star-fill"></i>
                        <c:out value="${media.tmdbRating}"/>
                    </div>
                    <c:if test="${media.voteCount > 0}">
                        <div data-bs-toggle="popover" data-bs-trigger="hover"
                             data-bs-content="<spring:message code="details.popoverUserRating"/>">
                            <span style="margin: 0 5px;">•</span>
                            <i class="bi bi-star"></i><c:out value="${media.totalRating}"/>
                        </div>
                    </c:if>
            </h1>


            </div>
            <!-- Watch it on -->
            <div class="d-flex flex-row align-items-center">
                <div class="providers-container" style="max-width: 80%; overflow-x: auto; max-height: 200px;">
                    <!-- Providers content here -->
                    <c:forEach var="provider" items="${providersList}">
                        <a style="text-decoration: none;" href="${pageContext.request.contextPath}/discover?providers=${provider.providerName}">
                            <span class="badge text-bg-light border border-black" style="margin: 3px;">
                            <img src="${provider.logoPath}" alt="${provider.providerName} logo not found"
                                 style="height: 1.6em; margin-right: 5px;">
                            <c:out value="${provider.providerName}"/>
                        </span>
                        </a>

                    </c:forEach>
                </div>
            </div>
            <!-- Genres -->
            <div class="d-flex flex-row  align-items-center ">
                <div style="margin-right: 10px">
                    <h5><spring:message code="details.genres"/></h5>
                </div>
                <div>
                    <c:forEach var="genre" items="${genresList}">
                        <a style="text-decoration: none;" href="${pageContext.request.contextPath}/discover?g=${genre}&media=${media.type? 'Series':'Movies'}">
                            <span class="badge text-bg-dark"><c:out value="${genre}"/></span>
                        </a>

                    </c:forEach>
                </div>
            </div>
            <!-- Media data -->
            <c:choose>
                <c:when test="${media.type==false}">
                    <div class="d-flex flex-row  align-items-center">
                        <div style="margin-right: 10px">
                            <h5><spring:message code="details.director"/></h5>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/cast/director/${media.directorId}">
                                <span class="badge text-bg-light border border-black"><c:out value="${media.director}"/></span>
                            </a>
                        </div>
                    </div>
                    <c:if test="${media.budget != 0}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5><spring:message code="details.budget"/></h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black" id="budget"><c:out value="${media.budget}"/></span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${media.revenue != 0}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5><spring:message code="details.revenue"/></h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black"
                                      id="revenue"><c:out value="${media.revenue}"/></span>
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
                                        <h5><spring:message code="details.creators"/></h5>
                                    </c:when>
                                    <c:otherwise>
                                        <h5><spring:message code="details.creator"/></h5>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div>
                                <c:forEach var="creator" items="${creators}">
                                    <a style="text-decoration: none;" href="${pageContext.request.contextPath}/cast/creator/${creator.creatorId}">
                                        <span class="badge text-bg-light border border-black"><c:out value="${creator.creatorName}"/></span>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${media.lastAirDate != null}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5><spring:message code="details.lastAirDate"/></h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black"><c:out value="${media.lastAirDate}"/></span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${media.nextEpisodeToAir != null}">
                        <div class="d-flex flex-row  align-items-center">
                            <div style="margin-right: 10px">
                                <h5><spring:message code="details.nextEpisodeToAir"/></h5>
                            </div>
                            <div>
                                <span class="badge text-bg-light border border-black"><c:out value="${media.nextEpisodeToAir}"/></span>
                            </div>
                        </div>
                    </c:if>
                </c:otherwise>
            </c:choose>
            <!-- Description and Buttons-->
            <c:if test="${media.trailerLink != null && media.trailerLink != 'None'}">
                <div style="margin-bottom: 5px; margin-top: 5px;">
                    <!-- Iframe for the trailer (initially hidden) -->
                    <iframe id="trailerIframe" style="width:85% ; height: 315px "
                        <%-- Replace "watch?v=" with "embed/" in the YouTube link for proper embedding --%>
                            src="${media.trailerLink.replace("watch?v=", "embed/")}"
                            title="${media.name}"
                            frameborder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                            allowfullscreen
                    ></iframe>
                </div>
            </c:if>

            <p>${media.overview}</p>
            <div class="flex-row d-flex">
                <div class="dropdown">
                    <div class="btn btn-dark dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"
                         style="margin-right: 10px"><i
                            class="bi bi-plus-circle-fill"></i> <spring:message code="details.addToList"/>
                    </div>
                    <ul class="dropdown-menu scrollable-menu">
                        <c:forEach var="privateList" items="${privateLists}">
                            <c:if test="${privateList.name != 'Watched' && privateList.name != 'Watchlist'}">
                                <!-- Mostrar siempre -->
                                <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                    <input type="hidden" name="listId" value="${privateList.moovieListId}"/>
                                    <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                    <li>
                                        <button class="dropdown-item" type="submit"><c:out
                                                value="${privateList.name}"/></button>
                                    </li>
                                </form>
                            </c:if>
                            <c:if test="${privateList.name == 'Watched' && !media.watched}">
                                <!-- Mostrar si media.watched es falso -->
                                <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                    <input type="hidden" name="listId" value="${privateList.moovieListId}"/>
                                    <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                    <li>
                                        <button class="dropdown-item" type="submit"><c:out
                                                value="${privateList.name}"/></button>
                                    </li>
                                </form>
                            </c:if>
                            <c:if test="${privateList.name == 'Watchlist' && !media.watchlist}">
                                <!-- Mostrar si media.watchlist es falso -->
                                <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                    <input type="hidden" name="listId" value="${privateList.moovieListId}"/>
                                    <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                    <li>
                                        <button class="dropdown-item" type="submit"><c:out
                                                value="${privateList.name}"/></button>
                                    </li>
                                </form>
                            </c:if>



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
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/createList" onclick="setCreateListContent('${media.name}',${media.mediaId})"><i
                                class="bi bi-plus-circle-fill"></i><spring:message code="details.createNewList"/></a></li>
                    </ul>
                </div>
                <c:choose>
                    <c:when test="${userReview!=null}">
                        <button type="button" class="btn btn-dark border border-black" onclick="openPopup('edit-popup')"><i
                                class="bi bi-star-fill"></i> <spring:message code="details.rated"/>
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-light border border-black" onclick="openPopup('rate-popup')"><i
                                class="bi bi-star-fill"></i> <spring:message code="details.rate"/>
                        </button>
                    </c:otherwise>
                </c:choose>

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
                    <div><c:out value="${successMessage}"/><a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}"><c:out value="${insertedMooovieList.name}"/></a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
        <!-- Cast -->
        <div class="row ">
            <h2><spring:message code="details.cast"/></h2>
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
                                    <a href="${pageContext.request.contextPath}/cast/actor/${actor.actorId}">
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
                                    <a style="color:black; text-decoration: none;" href="${pageContext.request.contextPath}/cast/actor/${actor.actorId}">
                                        <h5 class="card-title"><c:out value="${actor.actorName}"/></h5>
                                    </a>
                                    <p class="card-text"><c:out value="${actor.characterName}"/></p>
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
                            <spring:message code="details.seeMore"/>
                        </button>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="popup-overlay rate-popup-overlay" onclick="closePopup('rate-popup')"></div>
        <div class="popup rate-popup">
            <!-- Popup content goes here -->
            <h2><spring:message code="details.yourRatingForMedia" arguments="${media.name}"/></h2>
            <hr class="my-8">
            <div class="rating">
                <c:forEach var="i" begin="1" end="5" varStatus="loopStatus">
                    <c:set var="reverseIndex" value="${5 - loopStatus.count + 1}"/>
                    <i class="bi bi-star" onclick="rate(${reverseIndex})"></i>
                </c:forEach>
            </div>
            <h5><spring:message code="details.yourRating"/> <span id="selectedRating"><spring:message code="details.notSelected"/></span></h5>

            <form:form modelAttribute="detailsForm" action="${pageContext.request.contextPath}/createrating"
                       method="POST" id="rateForm">
                <spring:message code="details.yourReviewPlaceholder" var="yourReviewPlaceholder"/>
                <form:textarea path="reviewContent" class="review-textarea" id="reviewContent" rows="3"
                               placeholder='${yourReviewPlaceholder}' maxlength="500"/>
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
                        <spring:message code="details.cancel"/>
                    </button>
                    <button id="submitRating" type="submit" class="btn btn-dark" style="margin-inline: 10px" disabled >
                        <spring:message code="details.submit"/>
                    </button>
                </div>
            </form:form>

        </div>
        <div class="popup-overlay edit-popup-overlay"></div>
        <div class="popup edit-popup">
            <!-- Popup content goes here -->
            <h2><spring:message code="details.yourRatingForMedia" arguments="${media.name}"/></h2>
            <hr class="my-8">
            <div class="rating">
                <c:forEach var="i" begin="1" end="5" varStatus="loopStatus">
                    <c:set var="reverseIndex" value="${5 - loopStatus.count + 1}"/>
                    <c:choose>
                        <c:when test="${userReview.rating>=reverseIndex}">
                            <i class="bi bi-star-fill selected" onclick="rate2(${reverseIndex})"></i>
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-star" onclick="rate2(${reverseIndex})"></i>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
            <h5><spring:message code="details.yourRating"/> <span id="selectedRatingEdit"><c:out value="${userReview.rating}"/></span></h5>
            <textarea class="review-textarea" id="reviewContent2" rows="3"
                      maxlength="500"><c:out value="${userReview.reviewContent}"/></textarea>
            <span><span class="text-muted" id="charCount2"><c:out value="${userReview.reviewContent.length()}"/></span>/500</span>
            <!-- Submit Button -->
            <div class="text-center" style="margin-top: 20px">
                <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                        onclick="closePopup('edit-popup')">
                    <spring:message code="details.cancel"/>
                </button>
                <button class="btn btn-dark" id="submitButton2"  style="margin-inline: 10px" ${userReview.reviewContent.length()>=0 ? '' : 'disabled'} onclick="submitFirstForm() " >
                    <spring:message code="details.submit"/>
                </button>
            </div>
        </div>
        <!-- Reviews -->
        <h2><spring:message code="details.reviews"/></h2>
        <hr class="my-8">
        <c:choose>
            <c:when test="${fn:length(reviewsList)>0}">
                <div class="scrollableDiv">
                    <c:forEach var="review" items="${reviewsList}">
                        <div class="card mb-3">
                            <div class="card-body">
                                <div class="d-flex align-items-center justify-content-between">
                                    <div class="d-flex align-items-center">
                                        <a href="${pageContext.request.contextPath}/profile/${review.user.username}"
                                           style="text-decoration: none; color: inherit;">
                                            <img class="cropCenter mr-3 profile-image rounded-circle"
                                                 style="height:60px;width:60px;border: solid black; border-radius: 50%"
                                                 src="${pageContext.request.contextPath}/profile/image/${review.user.username}"
                                                 alt="${review.user.userId} Reviewer Profile">
                                        </a>
                                        <div class="mt-0" style="margin-left: 15px">
                                            <a href="${pageContext.request.contextPath}/profile/${review.user.username}"
                                               style="text-decoration: none; color: inherit;">
                                                <h5><c:out value="${review.user.username}"/><c:if test="${review.user.hasBadge}"><i class="bi bi-trophy"></i></c:if></h5>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="d-flex align-items-center justify-content-between">
                                        <h5 class="m-0">
                                            <i class="bi bi-star-fill ml-2"></i> <c:out value="${review.rating}"/>/5
                                        </h5>
                                        <c:choose>
                                            <c:when test="${currentUser.username==review.user.username}">
                                                <div class="text-center m-2">
                                                    <button onclick="openPopup('review${review.reviewId}')" class="btn btn-danger btn-sm">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </div>
                                                <div class="review${review.reviewId}-overlay popup-overlay" onclick="closePopup('review${review.reviewId}')"></div>
                                                <div style="background-color: transparent; box-shadow: none" class="popup review${review.reviewId}">
                                                    <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                                                        <h5 class="alert-heading"><spring:message code="details.confirmReviewDeletion"/></h5>
                                                        <p><spring:message code="details.confirmReviewDeletionPrompt"/></p>
                                                        <div class="d-flex justify-content-evenly">
                                                            <form class="m-0" action="${pageContext.request.contextPath}/deleteUserReview/${media.mediaId}" method="post">
                                                                <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                                <button type="submit" class="btn btn-danger"><spring:message code="details.delete"/></button>
                                                            </form>
                                                            <button type="button" onclick="closePopup('review${review.reviewId}')" class="btn btn-secondary" id="cancelButton"><spring:message code="details.cancel"/></button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                        <sec:authorize access="hasRole('ROLE_MODERATOR')">
                                            <div class="text-center m-2" >
                                                <button onclick="openPopup('review${review.reviewId}')" class="btn btn-danger btn-sm">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </div>
                                            <div class="review${review.reviewId}-overlay popup-overlay" onclick="closePopup('review${review.reviewId}')"></div>
                                            <div style="background-color: transparent; box-shadow: none" class="popup review${review.reviewId}">
                                                <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                                                    <h5 class="alert-heading"><spring:message code="details.confirmReviewDeletion"/></h5>
                                                    <p><spring:message code="details.confirmReviewDeletionPrompt"/></p>
                                                    <div class="d-flex justify-content-evenly">
                                                        <form class="m-0" action="${pageContext.request.contextPath}/deleteReview/${media.mediaId}" method="post">
                                                            <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                            <button type="submit" class="btn btn-danger"><spring:message code="details.delete"/></button>
                                                        </form>
                                                        <button type="button" onclick="closePopup('review${review.reviewId}')" class="btn btn-secondary" id="cancelModButton"><spring:message code="details.cancel"/></button>
                                                    </div>
                                                </div>
                                            </div>
                                        </sec:authorize>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <p>
                                    <c:out value="${review.reviewContent}"/>
                                </p>
                                <div class="d-flex align-items-center justify-content-start ">
                                    <div>
                                        <c:choose>
                                            <c:when test="${review.currentUserHasLiked}">
                                                <form action="${pageContext.request.contextPath}/unlikeReview"
                                                      method="post">
                                                    <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                    <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                                    <button class="btn btn-style" style="font-size: 14px">
                                                        <span>
                                                            <i class="bi bi-hand-thumbs-up-fill"></i>
                                                                <c:out value="${review.reviewLikes}"/>
                                                        </span>
                                                        <spring:message code="details.liked"/>
                                                    </button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <form action="${pageContext.request.contextPath}/likeReview" method="post">
                                                    <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                    <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                                    <button class="btn btn-style" style="font-size: 14px">
                                                        <span>
                                                            <i class="bi bi-hand-thumbs-up"></i>
                                                                <c:out value="${review.reviewLikes}"/>
                                                        </span>
                                                        <spring:message code="details.like"/>
                                                    </button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <c:if test="${currentUser.username==review.user.username}">
                                        <div style="margin-bottom: 15px">
                                            <button class="btn btn-primary" style="font-size: 14px;margin-left: 10px;" onclick="openPopup('edit-popup')">                                                <span>
                                                   <i class="bi bi-pencil" ></i>
                                                </span>
                                                <spring:message code="details.editReview"/>
                                            </button>
                                        </div>
                                    </c:if>
                                    <c:if test="${currentUser.username != review.user.username}">
                                        <sec:authorize access="isAuthenticated()">
                                            <div style="margin-bottom: 15px">
                                                <a href="${pageContext.request.contextPath}/reports/new?id=${review.reviewId}&reportedBy=${currentUser.userId}&type=details" class="btn btn-warning" style="font-size: 14px;margin-left: 10px;" ><spring:message code="report.title"/>
                                                <i class="bi bi-flag"></i>
                                                </a>
                                            </div>
                                        </sec:authorize>
                                    </c:if>
                                </div>

                                <sec:authorize access="isAuthenticated()">
                                    <div class="input-group mt-2 d-flex">
                                        <form:form modelAttribute="commentForm" cssClass="d-flex " action="${pageContext.request.contextPath}/createcomment" method="POST">
                                            <form:input path="reviewId" type="hidden" value="${review.reviewId}"/>
                                            <form:input path="listMediaId" type="hidden" value="${media.mediaId}"/>
                                            <spring:message code="details.addCommentPlaceholder" var="addCommentPlaceholder"/>
                                            <form:input path="content" class="form-control" placeholder='${addCommentPlaceholder}' aria-label="With textarea"/>

                                            <form:errors path="reviewId" cssClass="error"/>
                                            <form:errors path="listMediaId" cssClass="error"/>
                                            <form:errors path="content" cssClass="error"/>
                                            <button type="submit" class="ms-1 btn btn-dark" id="submitButton">
                                                <spring:message code="details.submit"/>
                                            </button>
                                        </form:form>
                                    </div>
                                </sec:authorize>
                                <c:if test="${review.commentCount > 0}">
                                    <a class="ms-1" data-bs-toggle="collapse" href="#collapse${review.reviewId}" role="button" aria-expanded="false" aria-controls="collapse${review.reviewId}">
                                        <spring:message code="details.comment"/> (${review.commentCount})
                                    </a>
                                    <div class="collapse" id="collapse${review.reviewId}">
                                            <c:forEach items="${review.comments}" var="comment" end="4">
                                                <div class="mb-2 mt-2 card card-body">
                                                    <div class="d-flex justify-content-between">
                                                        <h6 class="card-title"><a href="${pageContext.request.contextPath}/profile/${comment.username}" style="text-decoration: none; color: black"><c:out value="${comment.username}"/><c:if test="${comment.hasBadge}"><i class="bi bi-trophy"></i></c:if></a></h6>
                                                        <div class="d-flex">
                                                            <p style="margin: 10px">${comment.commentLikes - comment.commentDislikes}<img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo"></p>
                                                            <sec:authorize access="isAuthenticated()">
                                                                <form action="${pageContext.request.contextPath}/likeComment" method="post">
                                                                    <input hidden name="commentId" value="${comment.commentId}">
                                                                    <input hidden name="mediaId" value="${media.mediaId}">
                                                                    <c:if test="${!comment.currentUserHasLiked}">
                                                                        <button type="submit" class="me-1 btn-sm btn btn-outline-success">
                                                                            <i class="m-1 bi bi-hand-thumbs-up"></i>
                                                                        </button>
                                                                    </c:if>
                                                                    <c:if test="${comment.currentUserHasLiked}">
                                                                        <button type="submit" class="me-1 btn-sm btn btn-success">
                                                                            <i class="m-1 bi bi-hand-thumbs-up"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </form>
                                                                <form action="${pageContext.request.contextPath}/dislikeComment" method="post">
                                                                    <input hidden name="commentId" value="${comment.commentId}">
                                                                    <input hidden name="mediaId" value="${media.mediaId}">
                                                                    <c:if test="${!comment.currentUserHasDisliked}">
                                                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                                                            <i class="m-1 bi bi-hand-thumbs-down"></i>
                                                                        </button>
                                                                    </c:if>
                                                                    <c:if test="${comment.currentUserHasDisliked}">
                                                                        <button type="submit" class="btn btn-sm btn-danger">
                                                                            <i class="m-1 bi bi-hand-thumbs-down"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </form>
                                                                <c:if test="${currentUser.username!=comment.username}">
                                                                <a href="${pageContext.request.contextPath}/reports/new?id=${comment.commentId}&reportedBy=${currentUser.userId}&type=reviewComment" class="btn btn-sm btn-warning ms-1" ><spring:message code="report.title"/>
                                                                    <i class="bi bi-flag"></i>
                                                                </a>
                                                                </c:if>
                                                            </sec:authorize>
                                                        </div>
                                                    </div>
                                                    <p class="card-text"><c:out value="${comment.content}"/></p>
                                                </div>
                                            </c:forEach>

                                        <c:if test="${review.commentCount > 5}">
                                            <a class="ms-1" href="${pageContext.request.contextPath}/review/${media.mediaId}/${review.reviewId}">
                                                <spring:message code="details.seeMore"/>
                                            </a>
                                        </c:if>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center">
                    <h3><spring:message code="details.noReviews"/></h3>
                    <button type="button" class="btn btn-light border border-black" onclick="openPopup('rate-popup')"><i
                            class="bi bi-star-fill"></i> <spring:message code="details.addReview"/>
                    </button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="m-1">
        <c:import url="/WEB-INF/jsp/common/pagination.jsp">
            <c:param name="mediaPages" value="${numberOfPages}"/>
            <c:param name="currentPage" value="${currentPage + 1}"/>
            <c:param name="url" value="/details/${media.mediaId}"/>
        </c:import>
    </div>
    </div>
</div>
</body>
</html>

<script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=91"></script>