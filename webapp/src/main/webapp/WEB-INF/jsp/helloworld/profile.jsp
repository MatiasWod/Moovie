<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=79" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=79" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/lists.css?version=60" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=62" rel="stylesheet"/>

    <script src="${pageContext.request.contextPath}/resources/profileFunctions.js"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=85"></script>

    <title>Moovie ${user.username}</title>
</head>
<body id="grad">
<c:import url="navBar.jsp">
    <c:param name="userName" value="${myUser.username}"/>
</c:import>
<sec:authorize access="isAuthenticated()">
    <div style="align-items: center" class="d-flex flex-column">
        <div class="d-flex container justify-content-center">
            <img id="profile-image-big" class="cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="profile pic">
            <div class="m-2">
                <div class="d-flex align-items-center justify-content-between">
                    <h1><c:out value="${profile.username}"/></h1>
                    <c:if test="${profile.role == 2}"><img class="cropCenter" style="height:100px;width:100px" src="${pageContext.request.contextPath}/resources/moderator_logo.png"></c:if>
                    <sec:authorize access="hasRole('ROLE_MODERATOR')">
                        <c:if test="${profile.role != 2 && !isMe}">
                            <div class="text-center" style="margin-top: 20px">
                                <form action="${pageContext.request.contextPath}/banUser/${profile.userId}" method="post">
                                    <button type="submit" class="btn btn-danger btn-sm">Ban User</button>
                                </form>
                            </div>
                            <div class="text-center" style="margin-top: 20px">
                                <form action="${pageContext.request.contextPath}/makeUserMod/${profile.userId}" method="post">
                                    <button type="submit" class="btn btn-success btn-sm">Make User Mod</button>
                                </form>
                            </div>
                        </c:if>
                    </sec:authorize>
                </div>
                <c:if test="${isMe}"><h5><c:out value="${profile.email}"/></h5></c:if>
            </div>
        </div>
        <div class="d-flex container justify-content-center">
            <c:if test="${profile.role == -2}"><h2 style="color: red">User is banned undefinetly</h2></c:if>
        </div>
        <c:if test="${isMe}">
            <div class="p-1 container d-flex justify-content-center">
                <h4 class="m-1">Edit Profile Picture:</h4>
                <div class="m-1">
                    <form action="${pageContext.request.contextPath}/uploadProfilePicture" method="post" enctype="multipart/form-data">
                        <input type="file" name="file" accept="image/*" />
                        <input type="submit" value="Submit" />
                    </form>
                </div>
            </div>
            <div class="alert alert-danger" id="errorAlert" style="display: none">
                <c:if test="${param.error == 'invalidType'}">File is of invalid type</c:if>
                <c:if test="${param.error == 'noFile'}">No file was provided</c:if>
                <c:if test="${param.error == 'failedSetProfilePicture' || param.error == 'error'}">Error uploading file </c:if>
                <c:if test="${param.error == 'fileTooBig'}">File is too big </c:if>
            </div>
        </c:if>
        <hr class="my-8">
        <div class="d-flex container justify-content-center">
            <div class="d-flex m-1 align-items-center">
                <img height="30" width="30" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                <h5>
                        ${profile.moovieListCount}
                </h5>
            </div>
            <div class="m-1">
                <h5>
                    <i class="bi-hand-thumbs-up"></i>
                        ${profile.likedMoovieListCount}
                </h5>
            </div>
            <div class="m-1">
                <h5>
                    <i class="bi-star"></i>
                        ${profile.reviewsCount}
                </h5>
            </div>
        </div>
        <hr class="my-8">

        <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">
            <form id="selected-radio" action="${pageContext.request.contextPath}/profile/${profile.username}">
                <input type="hidden" name="list" id="listField">
            </form>
                <c:if test="${isMe}">
                    <input type="radio" class="btn-check" name="btnradio" id="btnradio-watched-list" autocomplete="off" ${(param.list != null && param.list == 'watched-list')? 'checked':''}>
                    <label class="btn btn-outline-success" for="btnradio-watched-list">Watched</label>
                </c:if>

                <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-lists" autocomplete="off" ${(param.list == null || param.list == '' || param.list == 'user-lists')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-user-lists">User Lists</label>

                <input type="radio" class="btn-check" name="btnradio" id="btnradio-liked-lists" autocomplete="off" ${(param.list != null && param.list == 'liked-lists')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-liked-lists">Liked Lists</label>

                <input type="radio" class="btn-check" name="btnradio" id="btnradio-reviews" autocomplete="off" ${(param.list != null && param.list == 'reviews')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-reviews">Reviews</label>
                <c:if test="${isMe}">
                    <input type="radio" class="btn-check" name="btnradio" id="btnradio-watchlist" autocomplete="off" ${(param.list != null && param.list == 'watchlist')? 'checked':''}>
                    <label class="btn btn-outline-success" for="btnradio-watchlist">Watchlist</label>
                </c:if>


        </div>

        <c:if test="${param.list == 'watched-list'}">
            <div class="container lists-container" id="watched-list" style="margin-top: 30px">

                <div class="container d-flex flex-column">
                    <div class="header">
                        <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${moovieList.name}"/></h1>
                        <h3><c:out value="${moovieList.description}"/></h3>
                    </div>
                    <div style="display: flex; align-items: center;justify-content: center">
                        <c:if test="${moviesCount > 0}">
                            <h4>${moviesCount} Movies</h4>
                        </c:if>
                        <c:if test="${moviesCount > 0 && tvSeriesCount > 0}">
                            <h4 style="margin-right: 5px;margin-left: 5px">and</h4>
                        </c:if>
                        <c:if test="${tvSeriesCount > 0}">
                            <h4>${tvSeriesCount} Series</h4>
                        </c:if>
                    </div>
                    <table class="table table-striped" id="movieTable">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Title</th>
                            <th scope="col">Type</th>
                            <th scope="col">Score</th>
                            <th scope="col">Release Date</th>
                        </tr>
                        </thead>
                        <c:choose>
                            <c:when test="${not empty mediaList}">
                                <tbody>
                                <c:forEach var="index" items="${mediaList}" varStatus="loop">
                                    <tr>
                                        <!-- Index -->
                                        <td style="text-align: center">${loop.index + 1}</td>
                                        <!-- Title -->
                                        <td>
                                            <div class="row align-items-center">
                                                <div class="col-auto">
                                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <img src="${mediaList[loop.index].posterPath}" class="img-fluid" width="100"
                                                             height="100" alt="${mediaList[loop.index].name} poster"/>
                                                    </a>
                                                </div>
                                                <div class="col">
                                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <strong>${mediaList[loop.index].name}</strong>
                                                    </a>
                                                </div>
                                                <c:if test="${watchedMovies.contains(mediaList[loop.index].mediaId)}">
                                                    <div class="col-auto">
                                                        <i class="bi bi-check-circle-fill" style="color: green"></i>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </td>
                                        <!-- Type -->
                                        <td>
                                            <c:choose>
                                                <c:when test="${mediaList[loop.index].type}">
                                                    Tv Series
                                                </c:when>
                                                <c:otherwise>
                                                    Movie
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <!-- Score -->
                                        <td>${mediaList[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
                                        </td>
                                        <td>
                                            <span>${mediaList[loop.index].releaseDate}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:when>
                            <c:otherwise>
                                <tbody>
                                <tr>
                                    <td colspan="5">List is empty</td>
                                </tr>
                                </tbody>
                            </c:otherwise>
                        </c:choose>
                    </table>
                    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="/list/${moovieList.moovieListId}/"/>
                    </c:import>
                </div>

            </div>
        </c:if>

        <c:if test="${param.list == 'watchlist'}">
            <div class="container lists-container" id="watchlist" style="margin-top: 30px">

                <div class="container d-flex flex-column">
                    <div class="header">
                        <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${moovieList.name}"/></h1>
                        <h3><c:out value="${moovieList.description}"/></h3>
                    </div>
                    <div style="display: flex; align-items: center;justify-content: center">
                        <c:if test="${moviesCount > 0}">
                            <h4>${moviesCount} Movies</h4>
                        </c:if>
                        <c:if test="${moviesCount > 0 && tvSeriesCount > 0}">
                            <h4 style="margin-right: 5px;margin-left: 5px">and</h4>
                        </c:if>
                        <c:if test="${tvSeriesCount > 0}">
                            <h4>${tvSeriesCount} Series</h4>
                        </c:if>
                    </div>
                    <table class="table table-striped" id="movieTable">
                        <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Title</th>
                            <th scope="col">Type</th>
                            <th scope="col">Score</th>
                            <th scope="col">Release Date</th>
                        </tr>
                        </thead>
                        <c:choose>
                            <c:when test="${not empty mediaList}">
                                <tbody>
                                <c:forEach var="index" items="${mediaList}" varStatus="loop">
                                    <tr>
                                        <!-- Index -->
                                        <td style="text-align: center">${loop.index + 1}</td>
                                        <!-- Title -->
                                        <td>
                                            <div class="row align-items-center">
                                                <div class="col-auto">
                                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <img src="${mediaList[loop.index].posterPath}" class="img-fluid" width="100"
                                                             height="100" alt="${mediaList[loop.index].name} poster"/>
                                                    </a>
                                                </div>
                                                <div class="col">
                                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <strong>${mediaList[loop.index].name}</strong>
                                                    </a>
                                                </div>
                                                <c:if test="${watchedMovies.contains(mediaList[loop.index].mediaId)}">
                                                    <div class="col-auto">
                                                        <i class="bi bi-check-circle-fill" style="color: green"></i>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </td>
                                        <!-- Type -->
                                        <td>
                                            <c:choose>
                                                <c:when test="${mediaList[loop.index].type}">
                                                    Tv Series
                                                </c:when>
                                                <c:otherwise>
                                                    Movie
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <!-- Score -->
                                        <td>${mediaList[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
                                        </td>
                                        <td>
                                            <span>${mediaList[loop.index].releaseDate}</span>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:when>
                            <c:otherwise>
                                <tbody>
                                <tr>
                                    <td colspan="5">List is empty</td>
                                </tr>
                                </tbody>
                            </c:otherwise>
                        </c:choose>
                    </table>
                    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="/list/${moovieList.moovieListId}/"/>
                    </c:import>
                </div>

            </div>
        </c:if>

        <c:if test="${param.list == 'liked-lists'}">
            <div class="container lists-container" id="liked-lists" style="margin-top: 30px">
                <c:if test="${showLists.size()==0}">
                    <h3>No results were found</h3>
                </c:if>
                <c:forEach var="showList" items="${showLists}">
                    <div class="list-card card"
                         onclick="location.href='${pageContext.request.contextPath}/list/${showList.moovieListId}?page=1'">
                        <div class="list-img-container card-img-top">
                            <c:forEach var="image" items="${showList.images}">
                                <img class="cropCenterImage" src="${image}" alt="...">
                            </c:forEach>
                            <c:forEach begin="${fn:length(showList.images)}" end="3">
                                <img class="cropCenterImage"
                                     src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
                            </c:forEach>
                        </div>
                        <div class="card-body cardBodyFlex">
                            <div>
                                <div class="card-name-likes">
                                    <div class="card-content overflow-hidden">
                                        <h5 class="card-title"><strong><c:out value="${showList.name}"/></strong></h5>
                                    </div>
                                    <div class="card-likes">
                                        <h5><i class="bi bi-hand-thumbs-up"></i>${showList.likeCount}</h5>
                                    </div>
                                </div>
                                <div style="display: flex;">
                                    <c:if test="${showList.moviesAmount > 0}">
                                        <p>${showList.moviesAmount} Movies</p>
                                    </c:if>

                                    <c:if test="${showList.moviesAmount > 0 && (showList.size - showList.moviesAmount) > 0}">
                                        <style>
                                            p {
                                                margin-right: 10px; /* Add a space between "Movies" and "Series" */
                                            }
                                        </style>
                                    </c:if>
                                    <c:if test="${(showList.size - showList.moviesAmount) > 0}">
                                        <p>${(showList.size - showList.moviesAmount)} Series</p>
                                    </c:if>
                                </div>
                                <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">by <c:out
                                        value="${showList.username}"/>
                                </p>
                                <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                                    <c:out value="${showList.description}"/>
                                </p>
                            </div>
                        </div>

                    </div>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${param.list == null || param.list == '' || param.list == 'user-lists'}">
            <div class="container lists-container" id="user-lists" style="margin-top: 30px">
                <c:if test="${showLists.size()==0}">
                <h3>No results were found</h3>
                </c:if>
                <c:forEach var="showList" items="${showLists}">
        <div class="list-card card"
             onclick="location.href='${pageContext.request.contextPath}/list/${showList.moovieListId}?page=1'">
            <div class="list-img-container card-img-top">
                <c:forEach var="image" items="${showList.images}">
                    <img class="cropCenterImage" src="${image}" alt="...">
                </c:forEach>
                <c:forEach begin="${fn:length(showList.images)}" end="3">
                    <img class="cropCenterImage"
                         src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
                </c:forEach>
            </div>
            <div class="card-body cardBodyFlex">
                <div>
                    <div class="card-name-likes">
                        <div class="card-content overflow-hidden">
                            <h5 class="card-title"><strong><c:out value="${showList.name}"/></strong></h5>
                        </div>
                        <div class="card-likes">
                            <h5><i class="bi bi-hand-thumbs-up"></i>${showList.likeCount}</h5>
                        </div>
                    </div>
                    <div style="display: flex;">
                        <c:if test="${showList.moviesAmount > 0}">
                            <p>${showList.moviesAmount} Movies</p>
                        </c:if>

                        <c:if test="${showList.moviesAmount > 0 && (showList.size - showList.moviesAmount) > 0}">
                            <style>
                                p {
                                    margin-right: 10px; /* Add a space between "Movies" and "Series" */
                                }
                            </style>
                        </c:if>
                        <c:if test="${(showList.size - showList.moviesAmount) > 0}">
                            <p>${(showList.size - showList.moviesAmount)} Series</p>
                        </c:if>
                    </div>
                    <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">by <c:out
                            value="${showList.username}"/>
                    </p>
                    <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                        <c:out value="${showList.description}"/>
                    </p>
                </div>
            </div>

        </div>
        </c:forEach>
            </div>
        </c:if>

        <c:if test="${param.list == 'reviews'}">

            <div id="reviews" class="container lists-container" style="margin-top: 30px">
                <!-- Reviews -->
                <hr class="my-8">
                <c:choose>
                    <c:when test="${fn:length(reviewsList)>0}">
                        <div class="scrollableDiv">
                            <c:forEach var="review" items="${reviewsList}">
                                <div class="card mb-3">
                                    <div class="card-body">
                                        <div class="d-flex align-items-center justify-content-between">
                                            <div class="d-flex align-items-center">
                                                <a href="${pageContext.request.contextPath}/details/${review.mediaId}"
                                                   style="text-decoration: none; color: inherit;">
                                                    <img src="${review.mediaPosterPath}"
                                                         alt="" class="mr-3 review-profile-image rounded-circle"
                                                         width="64" height="64">
                                                </a>
                                                <div class="mt-0" style="margin-left: 15px">
                                                    <a href="${pageContext.request.contextPath}/profile/${review.username}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <h5><c:out value="${review.mediaTitle}"/></h5>
                                                    </a>
                                                </div>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between">
                                                <h5>
                                                    <i class="bi bi-star-fill ml-2"></i> ${review.rating}/5
                                                </h5>
                                                <sec:authorize access="hasRole('ROLE_MODERATOR')">
                                                    <div class="text-center" style="margin: 10px">
                                                        <form action="${pageContext.request.contextPath}/deleteReview/${review.mediaId}" method="post">
                                                            <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                            <button type="submit" class="btn btn-danger btn-sm">Delete Review</button>
                                                        </form>
                                                    </div>
                                                </sec:authorize>
                                            </div>

                                        </div>
                                        <p>
                                            <c:out value="${review.reviewContent}"/>
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

        </c:if>



</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <c:import url="signUpAlert.jsp"/>
</sec:authorize>

</body>
</html>
