<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title><spring:message code="moovieList.title"/></title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=65" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
</head>
<body style="background: whitesmoke">
<c:import url="../common/navBar.jsp"/>
<c:import url="/WEB-INF/jsp/moovieList/listExtract.jsp">
    <c:param name="publicList" value="true"/>
</c:import>

<c:if test="${RecomendedListsCards.size()>0}">
    <hr/>
    <div class="d-flex flex-column align-items-center" style="margin-bottom: 20px">
        <h2><spring:message code="moovieList.recommendations"/></h2>
        <div class="d-flex flex-row flex-wrap">
            <c:forEach var="cardList" items="${RecomendedListsCards}">
                <%@include file="../common/listCard.jsp" %>
            </c:forEach>
        </div>
    </div>
</c:if>
<c:if test="${moovieList.type==publicType}">
<hr/>
    <div class="d-flex flex-column" style="align-items: center">
    <div class="d-flex flex-column flex-grow-1 m-3">
            <div class="input-group mt-2 mb-3">
                <form:form cssClass="d-flex" modelAttribute="createReviewForm" action="${pageContext.request.contextPath}/MoovieListReview" method="POST">
                    <form:input path="mediaId" type="hidden" value="${moovieList.moovieListId}"/>
                    <form:input path="rating" type="hidden" value="5"/>
                    <spring:message code="moovieList.addCommentPlaceholder" var="addCommentPlaceholder"/>
                    <form:input path="reviewContent" class="form-control" placeholder='${addCommentPlaceholder}' aria-label="With textarea"/>
                    <form:errors path="reviewContent" cssClass="text-danger"/>
                    <form:errors path="mediaId" cssClass="text-danger"/>
                    <form:errors path="rating" cssClass="text-danger"/>
                    <button type="submit" class="ms-1 btn btn-dark" id="submitButton">
                        <spring:message code="details.submit"/>
                    </button>
                </form:form>
            </div>
    </div>
    </div>

    <div class="d-flex flex-column flex-grow-1 m-3">
        <c:forEach var="review" items="${reviews}">
            <c:if test="${review.reviewContent.length()>0}">
            <div class="card mb-3">
                <div class="card-body">
                    <div class="d-flex align-items-center justify-content-between">
                        <div class="d-flex align-items-center">
                            <img src=""/>
                            <a href="${pageContext.request.contextPath}/profile/${review.user.username}" style="text-decoration: none; color: inherit;">
                                <c:choose>
                                    <c:when test="${review.user.hasPfp}">
                                        <img class="cropCenter mr-3 profile-image rounded-circle" style="height:60px;width:60px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${review.username}" alt="${review.userId} Reviewer Profile">
                                    </c:when>
                                    <c:otherwise>
                                        <img class="cropCenter mr-3 profile-image rounded-circle" style="height:60px;width:60px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/resources/defaultProfile.jpg" alt="${review.userId} Reviewer Profile">
                                    </c:otherwise>
                                </c:choose>
                            </a>
                            <div class="mt-0" style="margin-left: 15px">
                                <a href="${pageContext.request.contextPath}/profile/${review.user.username}"
                                   style="text-decoration: none; color: inherit;">
                                    <h5><c:out value="${review.user.username}"/><c:if test="${review.user.hasBadge}"><i class="bi bi-trophy"></i></c:if></h5>
                                </a>
                            </div>
                        </div>
                        <div class="d-flex align-items-center justify-content-between">
                            <c:choose>
                                <c:when test="${currentUser.username==review.user.username}">

                                    <div class="text-center m-2">
                                        <button onclick="openPopup('review${review.moovieListReviewId}')"
                                                class="btn btn-danger btn-sm">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </div>
                                    <div class="review${review.moovieListReviewId}-overlay popup-overlay"
                                         onclick="closePopup('review${review.moovieListReviewId}')"></div>
                                    <div style="background-color: transparent; box-shadow: none"
                                         class="popup review${review.moovieListReviewId}">
                                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);"
                                             class="alert alert-danger" role="alert">
                                            <h5 class="alert-heading"><spring:message
                                                    code="details.confirmReviewDeletion"/></h5>
                                            <p><spring:message
                                                    code="details.confirmReviewDeletionPrompt"/></p>
                                            <div class="d-flex justify-content-evenly">
                                                <form class="m-0"
                                                      action="${pageContext.request.contextPath}/deleteUserMoovieListReview/${moovieList.moovieListId}"
                                                      method="post">
                                                    <input type="hidden" name="reviewId"
                                                           value="${review.moovieListReviewId}"/>
                                                    <button type="submit" class="btn btn-danger">
                                                        <spring:message code="details.delete"/></button>
                                                </form>
                                                <button type="button"
                                                        onclick="closePopup('review${review.moovieListReviewId}')"
                                                        class="btn btn-secondary" id="cancelButton">
                                                    <spring:message code="details.cancel"/></button>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="m-2 text-center">
                                        <a href="${pageContext.request.contextPath}/reports/new?id=${review.moovieListReviewId}&reportedBy=${currentUser.userId}&type=moovieListReview" class="btn btn-sm btn-warning"><spring:message code="report.title"/>
                                            <i class="bi bi-flag"></i>
                                        </a>
                                    </div>
                                    <sec:authorize access="hasRole('ROLE_MODERATOR')">
                                        <div class="text-center m-2" >
                                            <button onclick="openPopup('review${review.moovieListReviewId}')" class="btn btn-danger btn-sm">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </div>
                                        <div class="review${review.moovieListReviewId}-overlay popup-overlay" onclick="closePopup('review${review.moovieListReviewId}')"></div>
                                        <div style="background-color: transparent; box-shadow: none" class="popup review${review.moovieListReviewId}">
                                            <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                                                <h5 class="alert-heading"><spring:message code="details.confirmReviewDeletion"/></h5>
                                                <p><spring:message code="details.confirmReviewDeletionPrompt"/></p>
                                                <div class="d-flex justify-content-evenly">
                                                    <form class="m-0" action="${pageContext.request.contextPath}/deleteUserMoovieListReview/${moovieList.moovieListId}" method="post">
                                                        <input type="hidden" name="reviewId" value="${review.moovieListReviewId}"/>
                                                        <input type="hidden" name="path" value="/list/${moovieList.moovieListId}"/>
                                                        <button type="submit" class="btn btn-danger"><spring:message code="details.delete"/></button>
                                                    </form>
                                                    <button type="button" onclick="closePopup('review${review.moovieListReviewId}')" class="btn btn-secondary" id="cancelModButton"><spring:message code="details.cancel"/></button>
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
                                    <form action="${pageContext.request.contextPath}/unlikeMoovieListReview"
                                          method="post">
                                        <input type="hidden" name="reviewId" value="${review.moovieListReviewId}"/>
                                        <input type="hidden" name="mediaId" value="${moovieList.moovieListId}"/>
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
                                    <form action="${pageContext.request.contextPath}/likeMoovieListReview" method="post">
                                        <input type="hidden" name="reviewId" value="${review.moovieListReviewId}"/>
                                        <input type="hidden" name="mediaId" value="${moovieList.moovieListId}"/>
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

                    </div>
                </div>
            </div>
            </c:if>
        </c:forEach>
        <c:import url="../common/pagination.jsp">
            <c:param name="mediaPages" value="${numberOfReviewPages}"/>
            <c:param name="currentPage" value="${currentReviewPage + 1}"/>
            <c:param name="url" value="${urlBase}"/>
        </c:import>
    </div>
</c:if>
</body>
</html>