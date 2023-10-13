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
    <link href="${pageContext.request.contextPath}/resources/main.css?version=87" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/lists.css?version=87" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=87" rel="stylesheet"/>

    <script src="${pageContext.request.contextPath}/resources/profileFunctions.js?version=88"></script>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=87"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=87"></script>

    <title>Moovie ${profile.username}</title>
</head>
<body id="grad">
<c:import url="navBar.jsp">
    <c:param name="userName" value="${profile.username}"/>
</c:import>
<sec:authorize access="isAuthenticated()">
    <div style="align-items: center" class="d-flex flex-column">
        <div class="d-flex container justify-content-center">
            <c:if test="${!isMe}">
                <img id="profile-image-big" class="cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="profile pic">
            </c:if>
            <c:if test="${isMe}">
                <button style="background: none; border: none; padding: 0; cursor: pointer" onclick="openPopup('image-popup')">
                    <img id="profile-image-" class="cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="profile pic">
                </button>
                <div class="popup-overlay image-popup-overlay" onclick="closePopup('popup')"></div>
                <div class="popup image-popup">
                    <div class="p-1 container d-flex flex-column justify-content-center">
                        <div class="d-flex justify-content-between">
                            <h1 class="m-1">Change Profile Picture</h1>
                            <button class="btn" onclick="closePopup('popup')">
                                <i class="bi bi-x"></i>
                            </button>
                        </div>
                        <div class="m-3">
                            <img id="profile-image-preview" class="m-2 cropCenter" style="height:300px;width:300px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="preview">
                            <form action="${pageContext.request.contextPath}/uploadProfilePicture" method="post" enctype="multipart/form-data">
                                <input class="btn btn-success-outline" type="file" name="file" accept="image/*" onchange="previewImage(this)" />
                                <input class="btn btn-success" type="submit" value="Submit" />
                            </form>
                        </div>
                    </div>
                </div>
            </c:if>
            <div class="m-2">
                <div class="d-flex align-items-center justify-content-between">
                    <h1><c:out value="${profile.username}"/></h1>
                    <c:if test="${profile.role == 2}">
                        <img class="cropCenter" style="height:50px;width:50px" src="${pageContext.request.contextPath}/resources/moderator_logo.png" alt="moderator profile pic">
                    </c:if>
                    <c:if test="${profile.role == -2}">
                        <a class="ms-2 me-2 btn btn-danger btn-sm" aria-disabled="true">banned</a>
                    </c:if>

                    <sec:authorize access="hasRole('ROLE_MODERATOR')">
                        <c:if test="${profile.role != 2 && !isMe}">
                            <div class="btn-group">
                                <button class="btn dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-three-dots-vertical"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <c:choose>
                                        <c:when test="${profile.role != -2}">
                                            <li>
                                                <button class="dropdown-item" onclick="openPopup('ban-popup')">Ban User</button>
                                            </li>
                                            <li>
                                                <button class="dropdown-item" onclick="openPopup('mod-popup')">Make User Mod</button>
                                            </li>
                                        </c:when>
                                        <c:when test="${profile.role == -2}">
                                            <li>
                                                <button class="dropdown-item" onclick="openPopup('unban-popup')">Unban User</button>
                                            </li>
                                        </c:when>
                                    </c:choose>
                                </ul>
                            </div>
                        </c:if>
                    </sec:authorize>
                </div>
                <c:if test="${isMe}">
                    <h5>
                        <c:out value="${profile.email}"/>
                    </h5>
                </c:if>
                <div class="d-flex align-items-center">
                    <div class="m-1 d-flex align-items-center">
                        <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                        <h5>
                                ${profile.moovieListCount}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-hand-thumbs-up"></i>
                                ${profile.likedMoovieListCount}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-star"></i>
                                ${profile.reviewsCount}
                        </h5>
                    </div>
                </div>
            </div>
        </div>
        <div class="popup-overlay ban-popup-overlay" onclick="closePopup('ban-popup')"></div>
        <div style="background-color: transparent; box-shadow: none" class="popup ban-popup">
            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-warning" role="alert">
                <h5 class="alert-heading">Confirm User Ban</h5>
                <p>Are you sure you want to ban this user? Once banned, they will no longer have access to the app and their account will be suspended indefinitely.</p>
                <%--<div class="mb-3">
                    <label for="banReason" class="form-label">Reason for Ban</label>
                    <input type="text" class="form-control" id="banReason">
                </div>--%>
                <div class="d-flex justify-content-evenly">
                    <form class="m-0" action="${pageContext.request.contextPath}/banUser/${profile.userId}" method="post">
                        <button type="submit" class="btn btn-danger" id="banUserButton">Ban User</button>
                    </form>
                    <button type="button" onclick="closePopup('ban-popup')" class="btn btn-secondary" id="cancelBanButton">Cancel</button>
                </div>
            </div>
        </div>
        <div class="popup-overlay unban-popup-overlay" onclick="closePopup('unban-popup')"></div>
        <div style="background-color: transparent; box-shadow: none" class="popup unban-popup">
            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-success" role="alert">
                <h5 class="alert-heading">Confirm User Unban</h5>
                <p>Are you sure you want to unban this user? Once unbanned, they will regain access to the app.</p>
                <div class="d-flex justify-content-evenly">
                    <form class="m-0" action="${pageContext.request.contextPath}/unbanUser/${profile.userId}" method="post">
                        <button type="submit" class="btn btn-success" id="unbanUserButton">Unban User</button>
                    </form>
                    <button type="button" onclick="closePopup('unban-popup')" class="btn btn-secondary" id="cancelUnbanButton">Cancel</button>
                </div>
            </div>
        </div>
        <div class="popup-overlay mod-popup-overlay" onclick="closePopup('mod-popup')"></div>
        <div style="background-color: transparent; box-shadow: none" class="popup mod-popup">
            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-info" role="alert">
                <h5 class="alert-heading">Confirm User Promotion</h5>
                <p>Are you sure you want to make this user a moderator? Once promoted, they will have additional privileges in the app.</p>
                <div class="d-flex justify-content-evenly">
                    <form class="m-0" action="${pageContext.request.contextPath}/makeUserMod/${profile.userId}" method="post">
                        <button type="submit" class="btn btn-info" id="makeUserModButton">Make Moderator</button>
                    </form>
                    <button type="button" onclick="closePopup('mod-popup')" class="btn btn-secondary" id="cancelModButton">Cancel</button>
                </div>
            </div>
        </div>
        <hr class="my-8">
        <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div>${successMessage}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div>${errorMessage}</div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
        </c:if>
        <hr class="my-8">

        <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">
            <c:if test="${isMe}">
                <input type="radio" class="btn-check" name="btnradio" id="btnradio-watched-list" autocomplete="off" ${(param.list != null && param.list == 'watched-list')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-watched-list">Watched</label>
            </c:if>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-lists" autocomplete="off" ${(param.list == null || param.list == '' || param.list == 'user-lists')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-user-lists">User Lists</label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-liked-lists" autocomplete="off" ${(param.list != null && param.list == 'liked-lists')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-liked-lists">Liked Lists</label>

            <form id="selected-radio" action="${pageContext.request.contextPath}/profile/${profile.username}">
                <input type="hidden" name="list" id="listField">
            </form>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-reviews" autocomplete="off" ${(param.list != null && param.list == 'reviews')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-reviews">Reviews</label>

            <c:if test="${isMe}">
                <input type="radio" class="btn-check" name="btnradio" id="btnradio-watchlist" autocomplete="off" ${(param.list != null && param.list == 'watchlist')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-watchlist">Watchlist</label>
            </c:if>
        </div>

        <c:if test="${param.list == 'watched-list' || param.list == 'watchlist'}">
            <div class="container lists-container" id="detailed-list" style="margin-top: 30px">

                <div class="container d-flex flex-column">
                    <div class="header">
                        <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${listDetails.card.name}"/></h1>
                        <h3><c:out value="${listDetails.card.description}"/></h3>
                    </div>
                    <div style="display: flex; align-items: center;justify-content: center">
                        <c:if test="${listDetails.card.moviesAmount > 0}">
                            <h4>${listDetails.card.moviesAmount} Movies</h4>
                        </c:if>
<%--                        <c:if test="${listDetails.card.moviesAmount > 0 && tvSeriesCount > 0}">--%>
<%--                            <h4 style="margin-right: 5px;margin-left: 5px">and</h4>--%>
<%--                        </c:if>--%>
<%--                        <c:if test="${tvSeriesCount > 0}">--%>
<%--                            <h4>${tvSeriesCount} Series</h4>--%>
<%--                        </c:if>--%>
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
                            <c:when test="${not empty listDetails.content}">
                                <tbody>
                                <c:forEach var="index" items="${listDetails.content}" varStatus="loop">
                                    <tr>
                                        <!-- Index -->
                                        <td style="text-align: center">${loop.index + 1}</td>
                                        <!-- Title -->
                                        <td>
                                            <div class="row align-items-center">
                                                <div class="col-auto">
                                                    <a href="${pageContext.request.contextPath}/details/${listDetails.content[loop.index].mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <img src="${listDetails.content[loop.index].posterPath}" class="img-fluid" width="100"
                                                             height="100" alt="${listDetails.content[loop.index].name} poster"/>
                                                    </a>
                                                </div>
                                                <div class="col">
                                                    <a href="${pageContext.request.contextPath}/details/${listDetails.content[loop.index].mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <strong>${listDetails.content[loop.index].name}</strong>
                                                    </a>
                                                </div>
                                                <c:if test="${listDetails.content[loop.index].watched}">
                                                    <div class="col-auto">
                                                        <i class="bi bi-check-circle-fill" style="color: green"></i>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </td>
                                        <!-- Type -->
                                        <td>
                                            <c:choose>
                                                <c:when test="${listDetails.content[loop.index].type}">
                                                    Tv Series
                                                </c:when>
                                                <c:otherwise>
                                                    Movie
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <!-- Score -->
                                        <td>${listDetails.content[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
                                        </td>
                                        <td>
                                            <span>${listDetails.content[loop.index].releaseDate}</span>
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
                </div>
            </div>
        <div class="m-1">
            <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                <c:param name="mediaPages" value="${numberOfPages}"/>
                <c:param name="currentPage" value="${currentPage + 1}"/>
                <c:param name="url" value="${urlBase}"/>
            </c:import>
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
        <div class="m-1">
            <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                <c:param name="mediaPages" value="${numberOfPages}"/>
                <c:param name="currentPage" value="${currentPage + 1}"/>
                <c:param name="url" value="${urlBase}"/>
            </c:import>
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
        <div class="m-1">
            <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                <c:param name="mediaPages" value="${numberOfPages}"/>
                <c:param name="currentPage" value="${currentPage + 1}"/>
                <c:param name="url" value="${urlBase}"/>
            </c:import>
        </div>
        </c:if>

        <c:if test="${param.list == 'reviews'}">

            <div id="reviews" class="container lists-container" style="margin-top: 30px">
                <!-- Reviews -->
                <hr class="my-8">
                <c:choose>
                    <c:when test="${fn:length(reviewsList)>0}">
                        <div class="container scrollableDiv">
                            <c:forEach var="review" items="${reviewsList}">
                                <div class="container card mb-3">
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
                <div class="m-1">
                    <c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="${urlBase}"/>
                    </c:import>
                </div>
            </div>



        </c:if>



</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <c:import url="signUpAlert.jsp"/>
</sec:authorize>

</body>
</html>
