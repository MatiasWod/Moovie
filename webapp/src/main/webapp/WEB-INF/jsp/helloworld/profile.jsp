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
    <title>Moovie${media.name}</title>
</head>
<script>
    document.addEventListener('DOMContentLoaded', (event) => {
        const radioButtons = document.querySelectorAll('[name="btnradio"]');
        const divs = document.querySelectorAll('#user-lists, #liked-lists, #reviews, #watched-list, #watchlist');

        console.log(divs)

        radioButtons.forEach(radio => {
            radio.addEventListener('change', (event) => {
                divs.forEach(div => {
                    console.log(div.id)
                    if (div.id === radio.id.replace('btnradio-', '')) {
                        div.style.display = 'block';
                    } else {
                        div.style.display = 'none';
                    }
                });
            });
        });
    });

</script>
<body id="grad">
<c:import url="navBar.jsp">
    <c:param name="userName" value="${myUser.username}"/>
</c:import>
<sec:authorize access="isAuthenticated()">
    <div style="align-items: center" class="d-flex flex-column">
        <div class="d-flex container justify-content-center">
            <img class="cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${user.username}">
            <div class="m-2">
                <h1><c:out value="${user.username}"/></h1>
                <c:if test="${isMe}"><h5><c:out value="${user.email}"/></h5></c:if>
            </div>
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
        </c:if>
        <hr class="my-8">
        <div class="d-flex container justify-content-center">
            <div class="d-flex m-1 align-items-center">
                <img height="30" width="30" src="${pageContext.request.contextPath}/resources/logo.png">
                <h5>
                        ${userLists.size()}
                </h5>
            </div>
            <div class="m-1">
                <h5>
                    <i class="bi-hand-thumbs-up"></i>
                        ${likedLists.size()}
                </h5>
            </div>
            <div class="m-1">
                <h5>
                    <i class="bi-star"></i>
                        ${notEmptyContentReviewList.size()}
                </h5>
            </div>
        </div>
        <hr class="my-8">

        <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">
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

        <c:if test="${isMe}">
            <div id="watched-list" style="display:none; margin-top: 30px">
                HERE GOES WATCHED
            </div>

            <div id="watchlist" style="display:none; margin-top: 30px">
                HERE GOES WATCHLIST
            </div>
        </c:if>

        <div id="liked-lists" class="lists-container" style="display:none; margin-top: 30px">
            <c:if test="${likedLists.size()==0}">
                <h3><c:out value="${user.username}"/> has no liked lists</h3>
            </c:if>
            <c:forEach var="list" items="${likedLists}">
                <div class="list-card card"
                     onclick="location.href='${pageContext.request.contextPath}/list/${list.moovieListId}'">
                    <div class="list-img-container card-img-top">
                        <c:forEach var="image" items="${list.posters}">
                            <img class="cropCenterImage" src="${image}" alt="...">
                        </c:forEach>
                        <c:forEach begin="${fn:length(list.posters)}" end="3">
                            <img class="cropCenterImage"
                                 src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
                        </c:forEach>
                    </div>
                    <div class="card-body cardBodyFlex">
                        <div>
                            <div class="card-name-likes">
                                <div class="card-content overflow-hidden">
                                    <h5 class="card-title"><strong><c:out value="${list.name}"/></strong></h5>
                                </div>
                                <div class="card-likes">
                                    <h5><i class="bi bi-hand-thumbs-up"></i>${list.likes}</h5>
                                </div>
                            </div>
                            <div style="display: flex;">
                                <c:if test="${list.mooviesCount > 0}">
                                    <p>${list.mooviesCount} Movies</p>
                                </c:if>

                                <c:if test="${list.mooviesCount > 0 && list.tvseriesCount > 0}">
                                    <style>
                                        p {
                                            margin-right: 10px; /* Add a space between "Movies" and "Series" */
                                        }
                                    </style>
                                </c:if>
                                <c:if test="${list.tvseriesCount > 0}">
                                    <p>${list.tvseriesCount} Series</p>
                                </c:if>
                            </div>
                            <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">by <c:out
                                    value="${list.owner}"/>
                            </p>
                            <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                                <c:out value="${list.description}"/>
                            </p>
                        </div>
                    </div>

                </div>
            </c:forEach>
        </div>

        <div id="user-lists" class="lists-container" style="display:block; margin-top: 30px">
            <c:if test="${userLists.size()==0}">
                <h3><c:out value="${user.username}"/> has no lists</h3>
            </c:if>
            <c:forEach var="list" items="${userLists}">
                <div class="list-card card"
                     onclick="location.href='${pageContext.request.contextPath}/list/${list.moovieListId}'">
                    <div class="list-img-container card-img-top">
                        <c:forEach var="image" items="${list.posters}">
                            <img class="cropCenterImage" src="${image}" alt="...">
                        </c:forEach>
                        <c:forEach begin="${fn:length(list.posters)}" end="3">
                            <img class="cropCenterImage"
                                 src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
                        </c:forEach>
                    </div>
                    <div class="card-body cardBodyFlex">
                        <div>
                            <div class="card-name-likes">
                                <div class="card-content overflow-hidden">
                                    <h5 class="card-title"><strong><c:out value="${list.name}"/></strong></h5>
                                </div>
                                <div class="card-likes">
                                    <h5><i class="bi bi-hand-thumbs-up"></i>${list.likes}</h5>
                                </div>
                            </div>
                            <div style="display: flex;">
                                <c:if test="${list.mooviesCount > 0}">
                                    <p>${list.mooviesCount} Movies</p>
                                </c:if>

                                <c:if test="${list.mooviesCount > 0 && list.tvseriesCount > 0}">
                                    <style>
                                        p {
                                            margin-right: 10px; /* Add a space between "Movies" and "Series" */
                                        }
                                    </style>
                                </c:if>
                                <c:if test="${list.tvseriesCount > 0}">
                                    <p>${list.tvseriesCount} Series</p>
                                </c:if>
                            </div>
                            <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">by <c:out
                                    value="${list.owner}"/>
                            </p>
                            <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                                <c:out value="${list.description}"/>
                            </p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div id="reviews" class="lists-container" style="display:none; margin-top: 30px">
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
                                                class="bi bi-star-fill ml-2"></i> ${review.rating}/10
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
        </div>
    </div>


</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <c:import url="signUpAlert.jsp"/>
</sec:authorize>

</body>