<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Review reports</title>

    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script async src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script async src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script async>
        document.addEventListener("DOMContentLoaded", function() {
            const form = document.getElementById("selected-radio");
            const radios = document.querySelectorAll("input[name='btnradio']");
            const listField = document.getElementById("listField");

            radios.forEach((radio) => {
                radio.addEventListener("change", function() {
                    listField.value = radio.id.replace("btnradio-", "");
                    form.submit();
                });
            });
        });
    </script>
</head>
<body>
<c:import url="../common/navBar.jsp"/>

<sec:authorize access="hasRole('ROLE_MODERATOR')">
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
    <div class="container d-flex flex-column">
        <div class="d-flex justify-content-center">
            <h2><spring:message code="report.adminPage"/></h2>
        </div>
        <div class="d-flex container justify-content-center">
            <div class="m-2">
                <div class="d-flex align-items-center">
                    <div class="m-2 d-flex align-items-center">
                        <i class="bi bi-flag m-1"></i>
                        <h5>
                            <spring:message code="report.total"/> ${totalReports}
                        </h5>
                    </div>
                    <div class="m-2 d-flex align-items-center">
                        <h5>
                            <i class="bi bi-person-slash m-1"></i>
                            <spring:message code="report.totalBanned"/> ${totalBanned}
                        </h5>
                    </div>
                    <div class="m-2 d-flex align-items-center">
                        <h5>
                            <i class="bi bi-envelope-exclamation m-1"></i>
                            <spring:message code="report.totalSpam"/> ${spamReports}
                        </h5>
                    </div>
                    <div class="m-2 d-flex align-items-center">
                        <h5>
                            <i class="bi bi-emoji-angry m-1"></i>
                            <spring:message code="report.totalHate"/> ${hateReports}
                        </h5>
                    </div>
                    <div class="m-2 d-flex align-items-center">
                        <h5>
                            <i class="bi bi-slash-circle m-1"></i>
                            <spring:message code="report.abuse"/> ${abuseReports}
                        </h5>
                    </div>
                    <div class="m-2 d-flex align-items-center">
                        <h5>
                            <i class="bi bi-incognito m1"></i>
                            <spring:message code="report.totalPriv"/> ${privacyReports}
                        </h5>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-comments" autocomplete="off" ${(param.list == null || param.list == '' || param.list == 'comments')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-comments"><spring:message code="report.comment"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-ml" autocomplete="off" ${(param.list != null && param.list == 'ml')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-ml"><spring:message code="report.list"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-mlReviews" autocomplete="off" ${(param.list != null && param.list == 'mlReviews')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-mlReviews"><spring:message code="report.listReview"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-reviews" autocomplete="off" ${(param.list != null && param.list == 'reviews')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-reviews"><spring:message code="report.mediaReview"/></label>

            <form id="selected-radio" action="${pageContext.request.contextPath}/reports/review">
                <input type="hidden" name="list" id="listField">
            </form>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-banned" autocomplete="off" ${(param.list != null && param.list == 'banned')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-banned"><spring:message code="report.banned"/></label>

        </div>


        <c:choose>
            <c:when test="${param.list == 'comments' || param.list == null || param.list == '' }">
                <c:forEach items="${commentList}" var="review">
                    <div class="card d-flex m-1">
                        <div class="card-body m-1">
                            <div class="d-flex justify-content-between">
                                <div class="d-flex">
                                    <a href="/profile/${review.username}">
                                        <h4  class="card-title me-2">${review.username}</h4>
                                    </a>
                                </div>
                                <div class="d-flex">
                                    <div class="d-flex m-1">
                                        <i class="bi bi-flag m-1"></i>
                                            ${review.totalReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-envelope-exclamation m-1"></i>
                                            ${review.spamReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-emoji-angry m-1"></i>
                                            ${review.hateReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-slash-circle m-1"></i>
                                            ${review.abuseReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-incognito m-1"></i>
                                            ${review.privacyReports}
                                    </div>
                                </div>
                            </div>
                            <p class="card-text">${review.content}</p>
                            <hr>
                            <div class="d-flex justify-content-evenly">
                                <button onclick="openPopup('ban${review.commentId}')" class="btn btn-lg btn-danger"><spring:message code="profile.banUser"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="ban${review.commentId}-overlay popup-overlay" onclick="closePopup('ban${review.commentId}')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup ban${review.commentId}">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="profile.confirmUserBan"/></h5>
                            <p><spring:message code="profile.banPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/banUser/${review.userId}" method="post">
                                    <button type="submit" class="btn btn-danger"><spring:message code="profile.banUser"/></button>
                                </form>
                                <button type="button" onclick="closePopup('ban${review.commentId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${param.list == 'ml'}">
                <div class="d-flex flex-wrap m-1 justify-content-center">
                    <c:forEach items="${mlList}" var="ml">
                        <div class="card m-2" style="width: 18rem;">
                            <div class="card-body text-center">
                                <h5 class="card-title">
                                    <a href="/list/${ml.moovieListId}">${ml.name}</a>
                                </h5>
                                <p class="card-text">${ml.description}</p>
                                <div class="d-flex justify-content-evenly">
                                    <button class="btn btn-warning m-1"><spring:message code="details.delete"/></button>
                                    <button class="btn btn-danger m-1"><spring:message code="profile.banUser"/></button>
                                </div>
                            </div>
                        </div>
                        <div class="ban${ml.moovieListId}-overlay popup-overlay" onclick="closePopup('ban${ml.moovieListId}')"></div>
                        <div style="background-color: transparent; box-shadow: none" class="popup ban${ml.moovieListId}">
                            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                                <h5 class="alert-heading"><spring:message code="profile.confirmUserBan"/></h5>
                                <p><spring:message code="profile.banPrompt"/></p>
                                <div class="d-flex justify-content-evenly">
                                    <form class="m-0" action="${pageContext.request.contextPath}/banUser/${ml.userId}" method="post">
                                        <button type="submit" class="btn btn-danger"><spring:message code="profile.banUser"/></button>
                                    </form>
                                    <button type="button" onclick="closePopup('ban${ml.moovieListId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                                </div>
                            </div>
                        </div>
                        <%-- TODO: poner bien el link --%>
                        <div class="review${ml.moovieListId}-overlay popup-overlay" onclick="closePopup('review${ml.moovieListId}')"></div>
                        <div style="background-color: transparent; box-shadow: none" class="popup review${ml.moovieListId}">
                            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                                <h5 class="alert-heading"><spring:message code="details.confirmReviewDeletion"/></h5>
                                <p><spring:message code="details.confirmReviewDeletionPrompt"/></p>
                                <div class="d-flex justify-content-evenly">
                                    <form class="m-0" action="${pageContext.request.contextPath}/deleteUserReview/${review.moovieListId}" method="post">
                                        <input type="hidden" name="reviewId" value="${review.moovieListReviewId}"/>
                                        <button type="submit" class="btn btn-danger"><spring:message code="details.delete"/></button>
                                    </form>
                                    <button type="button" onclick="closePopup('review${ml.moovieListId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:when test="${param.list == 'mlReviews'}">
                <c:forEach items="${mlReviewList}" var="review">
                    <div class="card d-flex m-1">
                        <div class="card-body m-1">
                            <div class="d-flex justify-content-between">
                                <div class="d-flex">
                                    <a href="/profile/${review.username}">
                                        <h4  class="card-title me-2">${review.username}</h4>
                                    </a>
                                </div>
                                <div class="d-flex">
                                    <div class="d-flex m-1">
                                        <i class="bi bi-flag m-1"></i>
                                            ${review.totalReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-envelope-exclamation m-1"></i>
                                            ${review.spamReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-emoji-angry m-1"></i>
                                            ${review.hateReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-slash-circle m-1"></i>
                                            ${review.abuseReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-incognito m-1"></i>
                                            ${review.privacyReports}
                                    </div>
                                </div>
                            </div>
                            <p class="card-text">${review.reviewContent}</p>
                            <hr>
                            <div class="d-flex justify-content-evenly">
                                <button onclick="openPopup('review${review.moovieListReviewId}')" class="btn btn-lg btn-warning"><spring:message code="details.delete"/></button>
                                <button onclick="openPopup('ban${review.moovieListReviewId}')" class="btn btn-lg btn-danger"><spring:message code="profile.banUser"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="review${review.moovieListReviewId}-overlay popup-overlay" onclick="closePopup('review${review.moovieListReviewId}')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup review${review.moovieListReviewId}">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="details.confirmReviewDeletion"/></h5>
                            <p><spring:message code="details.confirmReviewDeletionPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/deleteUserReview/${review.moovieListId}" method="post">
                                    <input type="hidden" name="reviewId" value="${review.moovieListReviewId}"/>
                                    <button type="submit" class="btn btn-danger"><spring:message code="details.delete"/></button>
                                </form>
                                <button type="button" onclick="closePopup('review${review.moovieListReviewId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="ban${review.moovieListReviewId}-overlay popup-overlay" onclick="closePopup('ban${review.moovieListReviewId}')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup ban${review.moovieListReviewId}">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="profile.confirmUserBan"/></h5>
                            <p><spring:message code="profile.banPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/banUser/${review.userId}" method="post">
                                    <input type="hidden" name="reviewId" value="${review.moovieListReviewId}"/>
                                    <button type="submit" class="btn btn-danger"><spring:message code="profile.banUser"/></button>
                                </form>
                                <button type="button" onclick="closePopup('ban${review.moovieListReviewId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${param.list == 'reviews'}">
                <c:forEach items="${reviewList}" var="review">
                    <div class="card d-flex m-1">
                        <div class="card-body m-1">
                            <div class="d-flex justify-content-between">
                                <div class="d-flex">
                                    <a href="/profile/${review.username}">
                                        <h4  class="card-title me-2">${review.username}</h4>
                                    </a>
                                    <h4>${review.rating}/5
                                       <i class="bi bi-star-fill"></i>
                                    </h4>
                                </div>
                                <div class="d-flex">
                                    <div class="d-flex m-1">
                                        <i class="bi bi-flag m-1"></i>
                                            ${review.totalReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-envelope-exclamation m-1"></i>
                                            ${review.spamReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-emoji-angry m-1"></i>
                                            ${review.hateReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-slash-circle m-1"></i>
                                            ${review.abuseReports}
                                    </div>
                                    <div class="d-flex m-1">
                                        <i class="bi bi-incognito m-1"></i>
                                            ${review.privacyReports}
                                    </div>
                                </div>
                            </div>
                            <p class="card-text">${review.reviewContent}</p>
                            <hr>
                            <div class="d-flex justify-content-evenly">
                                <button onclick="openPopup('review${review.reviewId}')" class="btn btn-lg btn-warning"><spring:message code="details.delete"/></button>
                                <button onclick="openPopup('ban${review.reviewId}')" class="btn btn-lg btn-danger"><spring:message code="profile.banUser"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="review${review.reviewId}-overlay popup-overlay" onclick="closePopup('review${review.reviewId}')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup review${review.reviewId}">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="details.confirmReviewDeletion"/></h5>
                            <p><spring:message code="details.confirmReviewDeletionPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/deleteUserReview/${review.mediaId}" method="post">
                                    <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                    <button type="submit" class="btn btn-danger"><spring:message code="details.delete"/></button>
                                </form>
                                <button type="button" onclick="closePopup('review${review.reviewId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="ban${review.reviewId}-overlay popup-overlay" onclick="closePopup('ban${review.reviewId}')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup ban${review.reviewId}">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="profile.confirmUserBan"/></h5>
                            <p><spring:message code="profile.banPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/banUser/${review.userId}" method="post">
                                    <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                    <button type="submit" class="btn btn-danger"><spring:message code="profile.banUser"/></button>
                                </form>
                                <button type="button" onclick="closePopup('ban${review.reviewId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:when test="${param.list == 'banned'}">
                <c:forEach items="${bannedList}" var="bannedUser">
                    <div class="card d-flex m-1">
                        <div class="card-body m-1">
                            <div class="d-flex justify-content-between">
                                <div class="d-flex align-items-center">
                                    <c:choose>
                                        <c:when test="${bannedUser.hasPfp}">
                                            <img style="height:50px;width:50px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${bannedUser.username}" alt="profile picture">
                                        </c:when>
                                        <c:otherwise>
                                            <img style="height:50px;width:50px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/resources/defaultProfile.jpg" alt="profile picture">
                                        </c:otherwise>
                                    </c:choose>
                                    <a class="ms-2" href="/profile/${bannedUser.username}">
                                        <h4 class="card-title">${bannedUser.username}</h4>
                                    </a>
                                </div>
                                <button onclick="openPopup('unban${bannedUser.userId}')" class="btn btn-lg btn-danger"><spring:message code="profile.unbanUser"/></button>
                            </div>
                        </div>
                    </div>
                    <div class="unban${bannedUser.userId}-overlay popup-overlay" onclick="closePopup('unban${bannedUser.userId}')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup unban${bannedUser.userId}">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="profile.confirmUserBan"/></h5>
                            <p><spring:message code="profile.unbanPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/unbanUser/${bannedUser.userId}" method="post">
                                    <button type="submit" class="btn btn-danger"><spring:message code="profile.unbanUser"/></button>
                                </form>
                                <button type="button" onclick="closePopup('unban${bannedUser.userId}')" class="btn btn-secondary"><spring:message code="details.cancel"/></button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
        </c:choose>
    </div>
</sec:authorize>
<sec:authorize access="!hasRole('ROLE_MODERATOR')">
    <c:import url="../errors/403.jsp"/>
</sec:authorize>



</body>
</html>
<script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=91"></script>