<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>
        <spring:message code="review.title" arguments="${review.username}"/>
    </title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<c:import url="../common/navBar.jsp"/>
<div class="d-flex justify-content-center flex-column">
    <div class="d-flex flex-column flex-grow-1 m-3" >
        <div class="card">
            <div class="card-body">
                <div class="d-flex align-items-center justify-content-between">
                    <div class="d-flex align-items-center">
                        <a href="${pageContext.request.contextPath}/profile/${review.username}"
                           style="text-decoration: none; color: inherit;">
                            <img class="cropCenter mr-3 profile-image rounded-circle"
                                 style="height:60px;width:60px;border: solid black; border-radius: 50%"
                                 src="${pageContext.request.contextPath}/profile/image/${review.username}"
                                 alt="${review.userId} Reviewer Profile">
                        </a>
                        <div class="mt-0" style="margin-left: 15px">
                            <a href="${pageContext.request.contextPath}/profile/${review.username}"
                               style="text-decoration: none; color: inherit;">
                                <h5><c:out value="${review.username}"/></h5>
                            </a>
                        </div>
                        <c:if test="${currentUser.username != review.username}">
                            <sec:authorize access="isAuthenticated()">
                                <div style="margin-bottom: 15px">
                                    <a href="${pageContext.request.contextPath}/reports/new?id=${review.reviewId}&reportedBy=${currentUser.userId}&type=reviews" class="btn btn-warning" style="font-size: 14px;margin-left: 10px;" ><spring:message code="report.title"/>
                                        <i class="bi bi-flag"></i>
                                    </a>
                                </div>
                            </sec:authorize>
                        </c:if>
                    </div>
                    <div class="d-flex align-items-center justify-content-between">
                        <h5 class="m-0">
                            <i class="bi bi-star-fill ml-2"></i> <c:out value="${review.rating}"/>/5
                        </h5>
                        <c:choose>
                            <c:when test="${currentUser.username==review.username}">
                                <div class="text-center m-2">
                                    <button onclick="openPopup('review${review.reviewId}')"
                                            class="btn btn-danger btn-sm">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </div>
                                <div class="review${review.reviewId}-overlay popup-overlay"
                                     onclick="closePopup('review${review.reviewId}')"></div>
                                <div style="background-color: transparent; box-shadow: none"
                                     class="popup review${review.reviewId}">
                                    <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);"
                                         class="alert alert-danger" role="alert">
                                        <h5 class="alert-heading"><spring:message
                                                code="details.confirmReviewDeletion"/></h5>
                                        <p><spring:message
                                                code="details.confirmReviewDeletionPrompt"/></p>
                                        <div class="d-flex justify-content-evenly">
                                            <form class="m-0"
                                                  action="${pageContext.request.contextPath}/deleteUserReview/${mediaId}"
                                                  method="post">
                                                <input type="hidden" name="reviewId"
                                                       value="${review.reviewId}"/>
                                                <button type="submit" class="btn btn-danger">
                                                    <spring:message code="details.delete"/></button>
                                            </form>
                                            <button type="button"
                                                    onclick="closePopup('review${review.reviewId}')"
                                                    class="btn btn-secondary" id="cancelButton">
                                                <spring:message code="details.cancel"/></button>
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
                                                <form class="m-0" action="${pageContext.request.contextPath}/deleteReview/${mediaId}" method="post">
                                                    <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                                    <input type="hidden" name="path" value="/details/${mediaId}"/>
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
                <p class="m-1 card-text">${review.reviewContent}</p>
            </div>
            <%--<c:if test="${review.hasComments}">--%>
            <div class="m-3">
                <div class="input-group mt-2 mb-3">
                    <form:form modelAttribute="commentForm" action="${pageContext.request.contextPath}/createcomment" cssClass="d-flex" method="POST">
                        <form:input path="reviewId" type="hidden" value="${review.reviewId}"/>
                        <form:input path="listMediaId" type="hidden" value="${review.mediaId}"/>
                        <spring:message code="review.addCommentPlaceholder" var="addCommentPlaceholder"/>
                        <form:input path="content" class="form-control" placeholder='${addCommentPlaceholder}' aria-label="With textarea"/>
                        <button class="btn ms-1 btn-dark" type="submit"><spring:message code="details.submit"/></button>
                    </form:form>
                </div>
                <c:forEach items="${review.comments}" var="comment">
                    <div class="mb-2 mt-2 card card-body">
                        <div class="d-flex justify-content-between">
                            <h6 class="card-title"><a href="${pageContext.request.contextPath}/profile/${comment.username}"  style="text-decoration: none; color: black"><c:out value="${comment.username}"/></a></h6>
                            <div class="d-flex">
                                <p style="margin: 10px">${comment.commentLikes - comment.commentDislikes}<img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo"></p>
                                <sec:authorize access="isAuthenticated()">
                                    <form action="${pageContext.request.contextPath}/likeComment" method="post">
                                        <input hidden name="commentId" value="${comment.commentId}">
                                        <input hidden name="mediaId" value="${mediaId}">
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
                                        <input hidden name="mediaId" value="${mediaId}">
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
                                        <a href="${pageContext.request.contextPath}/reports/new?id=${comment.commentId}&reportedBy=${currentUser.userId}&type=comment" class="btn btn-sm btn-warning ms-1" ><spring:message code="report.title"/>
                                            <i class="bi bi-flag"></i>
                                        </a>
                                    </c:if>
                                </sec:authorize>
                            </div>
                        </div>
                        <p class="card-text"><c:out value="${comment.content}"/></p>
                    </div>
                </c:forEach>
            </div>
            <%--</c:if>--%>
        </div>
    </div>
    <div style="align-items: center;text-align: center">
    <a href="${pageContext.request.contextPath}/featuredList/topRatedMovies"  style="text-decoration: none;color: black"> <h2><spring:message
            code="review.recommended"/></h2> </a>
    </div>
    <div class="d-flex flex-row m-3" style="max-width: 100%">
        <div class="card-group">
            <c:forEach var="movie" items="${movieList}" end="5">
                <div class="card text-bg-dark m-1">
                    <a href="${pageContext.request.contextPath}/details/${movie.mediaId}"  style="text-decoration: none; color: white">
                        <div class="card-img-container">
                            <img class="cropCenter" src="${movie.posterPath}" alt="media poster">
                            <div class="card-img-overlay">
                                <h6 class="card-title text-center"><c:out value="${movie.name}"/></h6>
                                <div class="d-flex justify-content-evenly">
                                    <p class="card-text">
                                        <i class="bi bi-star-fill"></i>
                                        <c:out value="${movie.tmdbRating}"/>
                                    </p>
                                    <p class="card-text">
                                        <fmt:formatDate value="${movie.releaseDate}" pattern="YYYY"/>
                                    </p>
                                </div>
                                <div class="d-flex justify-content-evenly flex-wrap">
                                    <c:forEach var="genre" items="${movie.genres}" end="1">
                                        <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                    </c:forEach>
                                </div>
                                <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                    <c:forEach var="provider" items="${movie.providers}" end="1">
                                    <span class="mt-1 badge text-bg-light border border-black">
                                        <img src="${provider.logoPath}" alt="provider logo" style="height: 1.4em; margin-right: 5px;">
                                    </span>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>

</div>
</body>
</html>
