<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

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

    <title><spring:message code="profile.title" arguments="${username}"/></title>
</head>
<body id="grad">
<c:import url="../common/navBar.jsp"/>
<sec:authorize access="isAuthenticated()">
    <div style="align-items: center" class="d-flex flex-column">
        <div class="d-flex container justify-content-center">
            <c:if test="${!isMe}">
                <img id="profile-image-big" class="profile-image cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="profile pic">
            </c:if>
            <c:if test="${isMe}">
                <button style="background: none; border: none; padding: 0; cursor: pointer" onclick="openPopup('image-popup')">
                    <img class="profile-image cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="profile pic">
                </button>
                <div class="popup-overlay image-popup-overlay" onclick="closePopup('popup')"></div>
                <div class="popup image-popup">
                    <div class="p-1 container d-flex flex-column justify-content-center">
                        <div class="d-flex justify-content-between">
                            <h1 class="m-1"><spring:message code="profile.changeProfilePicture"/></h1>
                            <button class="btn" onclick="closePopup('popup')">
                                <i class="bi bi-x"></i>
                            </button>
                        </div>
                        <div class="m-3">
                            <img class="profile-image profile-image-preview m-2 cropCenter" style="height:300px;width:300px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${profile.username}" alt="preview">
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
                    <h1><c:out value="${profile.username}"/><c:if test="${profile.hasBadge}"><i class="bi bi-trophy"></i></c:if></h1>
                    <c:if test="${profile.role == 2 || profile.role == -102}">
                        <img class="cropCenter" style="height:50px;width:50px" src="${pageContext.request.contextPath}/resources/moderator_logo.png" alt="moderator profile pic">
                    </c:if>
                    <c:if test="${profile.role == -2 || profile.role == -101}">
                        <span class="ms-2 me-2 badge text-bg-danger" aria-disabled="true"><spring:message code="profile.banned"/></span>
                    </c:if>

                    <sec:authorize access="hasRole('ROLE_MODERATOR')">
                        <c:if test="${profile.role != 2 && !isMe}">
                            <div class="btn-group">
                                <button class="btn dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="bi bi-three-dots-vertical"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <c:choose>
                                        <c:when test="${profile.role != -2 && profile.role != -101 }">
                                            <li>
                                                <button class="dropdown-item" onclick="openPopup('ban-popup')"><spring:message code="profile.banUser"/></button>
                                            </li>
                                            <li>
                                                <button class="dropdown-item" onclick="openPopup('mod-popup')"><spring:message code="profile.makeMod"/></button>
                                            </li>
                                        </c:when>
                                        <c:when test="${profile.role == -2 || profile.role == -101}">
                                            <li>
                                                <button class="dropdown-item" onclick="openPopup('unban-popup')"><spring:message code="profile.unbanUser"/></button>
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
                        <i class="bi bi-list-ul"></i>
                        <h5>
                                <c:out value="${profile.moovieListCount}"/>
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-star"></i>
                                <c:out value="${profile.reviewsCount}"/>
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                                <c:out value="${profile.milkyPoints}"/>
                        </h5>
                    </div>
                </div>
            </div>
        </div>
        <div class="popup-overlay ban-popup-overlay" onclick="closePopup('ban-popup')"></div>
        <div style="background-color: transparent; box-shadow: none" class="popup ban-popup">
            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-warning" role="alert">
                <h5 class="alert-heading"><spring:message code="profile.confirmUserBan"/></h5>
                <p><spring:message code="profile.banPrompt"/></p>

                <form class="m-0" action="${pageContext.request.contextPath}/banUser/${profile.userId}" method="post">
                    <spring:message code="profile.explainBanPlaceholder" var="explainBanPlaceholder"/>
                    <textarea name="message" id="message" rows="6" cols="50"
                              placeholder='${explainBanPlaceholder}' maxlength="500"></textarea>
                    <div class="d-flex justify-content-evenly">
                        <button type="submit" class="btn btn-danger" id="banUserButton"><spring:message code="profile.banUser"/></button>
                        <button type="button" onclick="closePopup('ban-popup')" class="btn btn-secondary" id="cancelBanButton"><spring:message code="profile.cancel"/></button>
                    </div>
                </form>
            </div>
        </div>
        <div class="popup-overlay unban-popup-overlay" onclick="closePopup('unban-popup')"></div>
        <div style="background-color: transparent; box-shadow: none" class="popup unban-popup">
            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-success" role="alert">
                <h5 class="alert-heading"><spring:message code="profile.confirmUserUnban"/></h5>
                <p><spring:message code="profile.unbanPrompt"/></p>
                <div class="d-flex justify-content-evenly">
                    <form class="m-0" action="${pageContext.request.contextPath}/unbanUser/${profile.userId}" method="post">
                        <button type="submit" class="btn btn-success" id="unbanUserButton"><spring:message code="profile.unbanUser"/></button>
                    </form>
                    <button type="button" onclick="closePopup('unban-popup')" class="btn btn-secondary" id="cancelUnbanButton"><spring:message code="profile.cancel"/></button>
                </div>
            </div>
        </div>
        <div class="popup-overlay mod-popup-overlay" onclick="closePopup('mod-popup')"></div>
        <div style="background-color: transparent; box-shadow: none" class="popup mod-popup">
            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-info" role="alert">
                <h5 class="alert-heading"><spring:message code="profile.confirmMakeMod"/></h5>
                <p><spring:message code="profile.makeModPrompt"/></p>
                <div class="d-flex justify-content-evenly">
                    <form class="m-0" action="${pageContext.request.contextPath}/makeUserMod/${profile.userId}" method="post">
                        <button type="submit" class="btn btn-info" id="makeUserModButton"><spring:message code="profile.makeModerator"/></button>
                    </form>
                    <button type="button" onclick="closePopup('mod-popup')" class="btn btn-secondary" id="cancelModButton"><spring:message code="profile.cancel"/></button>
                </div>
            </div>
        </div>
        <hr class="my-8">
        <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div><c:out value="${successMessage}"/></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
        </c:if>
        <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div><c:out value="${errorMessage}"/></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
        </c:if>
        <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div><c:out value="${param.error}"/></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
        </c:if>
        <hr class="my-8">

        <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">
            <c:if test="${isMe}">
                <input type="radio" class="btn-check" name="btnradio" id="btnradio-watched-list" autocomplete="off" ${(param.list != null && param.list == 'watched-list')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-watched-list"><spring:message code="profile.watched"/></label>
            </c:if>
            <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-lists" autocomplete="off" ${(param.list == null || param.list == '' || param.list == 'user-lists')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-user-lists"><spring:message code="profile.userLists"/></label>

            <c:if test="${isMe}">
                <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-private-lists" autocomplete="off" ${(param.list != null && param.list == 'user-private-lists')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-user-private-lists"><spring:message code="profile.privateUserLists"/></label>
            </c:if>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-liked-lists" autocomplete="off" ${(param.list != null && param.list == 'liked-lists')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-liked-lists"><spring:message code="profile.likedLists"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-followed" autocomplete="off" ${(param.list != null && param.list == 'followed')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-followed"><spring:message code="profile.followedLists"/></label>

            <form id="selected-radio" action="${pageContext.request.contextPath}/profile/${profile.username}">
                <input type="hidden" name="list" id="listField">
            </form>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-reviews" autocomplete="off" ${(param.list != null && param.list == 'reviews')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-reviews"><spring:message code="profile.reviews"/></label>

            <c:if test="${isMe}">
                <input type="radio" class="btn-check" name="btnradio" id="btnradio-watchlist" autocomplete="off" ${(param.list != null && param.list == 'watchlist')? 'checked':''}>
                <label class="btn btn-outline-success" for="btnradio-watchlist"><spring:message code="profile.watchlist"/></label>
            </c:if>
        </div>

        <c:if test="${param.list == 'watched-list' || param.list == 'watchlist'}">
            <div class="container lists-container" id="detailed-list" style="margin-top: 30px">
                <c:import url="/WEB-INF/jsp/moovieList/listExtract.jsp">
                    <c:param name="listType" value="${param.list == 'watched-list' ? ('watched-list'):('watchlist')}"/>
                </c:import>
            </div>
        </c:if>

        <c:if test="${param.list == 'liked-lists' || param.list == 'user-private-lists' || param.list == 'followed' || param.list == null || param.list == '' || param.list == 'user-lists'}">
            <div class="container lists-container" id="liked-lists" style="margin-top: 30px">

                <c:if test="${showLists.size()==0}">
                    <div class=" text-center ">

                        <c:if test="${isMe}">
                            <c:if test="${param.list == 'liked-lists'}">
                                <h4><spring:message code="profile.noLikedListsMessagePersonal"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/lists"><spring:message code="profile.noLikedListsButtonPersonal"/></a>
                                <%--                            profile.noLikedListsMessagePersonal--%>
                                <%--                            profile.noLikedListsButtonPersonal--%>
                            </c:if>
                            <c:if test="${param.list == null || param.list == 'user-lists' || param.list == 'user-private-lists'}">
                                <h4><spring:message code="profile.noUserListsMessagePersonal"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/createList"><spring:message code="profile.noUserListsButtonPersonal"/></a>
                                <%--                            profile.noUserListsMessagePersonal--%>
                                <%--                            profile.noUserListsButtonPersonal--%>
                            </c:if>
                            <c:if test="${param.list == 'followed'}">
                                <h4><spring:message code="profile.noFollowedListsMessagePersonal"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/lists"><spring:message code="profile.noFollowedListsButtonPersonal"/></a>
                                <%--                same, no carga profile.noFollowedListsMessagePersonal--%>
                                <%--                            profile.noFollowedListsButtonPersonal--%>
                            </c:if>
                        </c:if>
                        <c:if test="${!isMe}">
                            <c:if test="${param.list == 'liked-lists'}">
                                <h4><spring:message code="profile.noLikedListsMessageGuest"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/lists"><spring:message code="profile.noLikedListsButtonGuest"/></a>
                                <%--                            profile.noLikedListsMessageGuest--%>
                                <%--                            profile.noLikedListsButtonGuest--%>
                            </c:if>
                            <c:if test="${param.list == null || param.list == 'user-lists'}">
                                <h4><spring:message code="profile.noUserListsMessageGuest"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/lists"><spring:message code="profile.noUserListsButtonGuest"/></a>
                                <%--                            profile.noUserListsMessageGuest--%>
                                <%--                            profile.noUserListsButtonGuest--%>
                            </c:if>
                            <c:if test="${param.list == 'followed'}">
                                <h4><spring:message code="profile.noFollowedListsMessageGuest"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/lists"><spring:message code="profile.noFollowedListsButtonGuest"/></a>
                                <%--                            profile.noFollowedListsMessageGuest--%>
                                <%--                            profile.noFollowedListsButtonGuest--%>
                            </c:if>
                        </c:if>


                    </div>
                </c:if>

                <c:forEach var="cardList" items="${showLists}">
                    <%@include file="../common/listCard.jsp"%>
                </c:forEach>
            </div>
        <div class="m-1">
            <c:import url="/WEB-INF/jsp/common/pagination.jsp">
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
                                                    <a href="${pageContext.request.contextPath}/details/${review.mediaId}"
                                                       style="text-decoration: none; color: inherit;">
                                                        <h5><c:out value="${review.mediaTitle}"/></h5>
                                                    </a>
                                                </div>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between">
                                                <h5>
                                                    <i class="bi bi-star-fill ml-2"></i><c:out value="${review.rating}"/>/5
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
                                                            <h5 class="alert-heading"><spring:message code="profile.confirmReviewDeletion"/></h5>
                                                            <p><spring:message code="profile.reviewDeletionPrompt"/></p>
                                                            <div class="d-flex justify-content-evenly">
                                                                <form class="m-0" action="${pageContext.request.contextPath}/deleteReview/${review.mediaId}" method="post">
                                                                    <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                                    <input type="hidden" name="path" value="/profile/${username}"/>
                                                                    <button type="submit" class="btn btn-danger"><spring:message code="profile.delete"/></button>
                                                                </form>
                                                                <button type="button" onclick="closePopup('review${review.reviewId}')" class="btn btn-secondary" id="cancelModButton"><spring:message code="profile.cancel"/></button>
                                                            </div>
                                                        </div>
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
                            <c:if test="${isMe}">
                                <h4><spring:message code="profile.noReviewsMessagePersonal"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/discover"><spring:message code="profile.noReviewsButtonPersonal"/></a>
                            </c:if>
                            <c:if test="${!isMe}">
                                <h4><spring:message code="profile.noReviewsMessageGuest"/></h4>
                                <a class="btn btn-outline-success" href="${pageContext.request.contextPath}/discover"><spring:message code="profile.noReviewsButtonGuest"/></a>
                            </c:if>

                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="m-1">
                    <c:import url="/WEB-INF/jsp/common/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="${urlBase}"/>
                    </c:import>
                </div>
            </div>



        </c:if>



</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <c:import url="../main/signUpAlert.jsp"/>
</sec:authorize>

</body>
</html>
