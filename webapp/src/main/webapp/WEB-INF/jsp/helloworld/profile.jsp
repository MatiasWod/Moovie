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
                    <input type="radio" class="btn-check" name="btnradio" id="btnradio-watched-list" autocomplete="off">
                    <label class="btn btn-outline-success" for="btnradio-watched-list">Watched</label>
                </c:if>

                <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-lists" autocomplete="off" checked>
                <label class="btn btn-outline-success" for="btnradio-user-lists">User Lists</label>

                <input type="radio" class="btn-check" name="btnradio" id="btnradio-liked-lists" autocomplete="off">
                <label class="btn btn-outline-success" for="btnradio-liked-lists">Liked Lists</label>

                <input type="radio" class="btn-check" name="btnradio" id="btnradio-reviews" autocomplete="off">
                <label class="btn btn-outline-success" for="btnradio-reviews">Reviews</label>
                <c:if test="${isMe}">
                    <input type="radio" class="btn-check" name="btnradio" id="btnradio-watchlist" autocomplete="off">
                    <label class="btn btn-outline-success" for="btnradio-watchlist">Watchlist</label>
                </c:if>


        </div>

        <c:if test="${param.list == 'watched-list'}">
        WATCHED LIST
            <div class="container" id="watched-list" style="display:none; margin-top: 30px">


                    <%--                ACA EL CODIGO PARA EL DETALLE DE UNA LISTA--%>

            </div>
        </c:if>

        <c:if test="${param.list == 'watchlist'}">
        WATCHLIST
            <div class="container" id="watchlist" style="display:none; margin-top: 30px">

            </div>
        </c:if>

        <c:if test="${param.list == 'liked-lists'}">
            LIKED LISTS
            <%--                ACA EL CODIGO PARA EL BROWSE DE LISTAS--%>
        </c:if>

        <c:if test="${param.list == null || param.list == ''}">
            USER LISTS
            <%--                ACA EL CODIGO PARA EL BROWSE DE LISTAS--%>
        </c:if>

        <c:if test="${param.list == 'reviews'}">
            REVIEWS
        <div id="reviews" class="container lists-container" style="display:none; margin-top: 30px">
            <h2>Reviews</h2>
            <hr class="my-8">
            <c:choose>
                <c:when test="${fn:length(notEmptyContentReviewList)>0}">
                    <div class="scrollableDiv">
                        <c:forEach var="review" items="${notEmptyContentReviewList}">
                            <div class="card mb-3">
                                <div class="card-body">
                                    <div class="d-flex align-items-center justify-content-between">
                                        <div class="d-flex align-items-center">
                                            <a href="${pageContext.request.contextPath}/details/${review.mediaId}" style="text-decoration: none; color: inherit;">
                                                <img src="${review.mediaPosterPath}"
                                                     alt="${review.userId} Reviewer Profile" class="mr-3 rounded-circle"
                                                     width="64" height="64">
                                            </a>
                                            <div class="mt-0" style="margin-left: 15px">
                                                <a href="${pageContext.request.contextPath}/details/${review.mediaId}"
                                                   style="text-decoration: none; color: inherit;">
                                                    <h5><c:out value="${review.mediaTitle}"/></h5>
                                                </a>
                                            </div>
                                        </div>
                                        <h5 class="align-items-left"><i
                                                class="bi bi-star-fill ml-2"></i> ${review.rating}/5
                                        </h5>
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
