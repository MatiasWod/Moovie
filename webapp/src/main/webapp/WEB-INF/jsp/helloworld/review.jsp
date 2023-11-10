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
        <spring:message code="review.title" arguments="hello"/>
    </title>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=82" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=84"></script>

</head>
<body>
<c:import url="navBar.jsp"/>
<div class="d-flex justify-content-center">
    <div class="d-flex flex-column flex-grow-1 m-3" >
        <div class="card">
            <div class="card-body">
                <div class="d-flex align-self-center">
                    <a href="${pageContext.request.contextPath}/profile/${review.username}" style="text-decoration: none; color: inherit;">
                        <img class="cropCenter mr-3 profile-image rounded-circle"
                             style="height:60px;width:60px;border: solid black; border-radius: 50%"
                             src="${pageContext.request.contextPath}/profile/image/${review.username}"
                             alt="${review.userId} Reviewer Profile">
                    </a>
                    <div class="ms-2 d-flex flex-column">
                        <div class="d-flex justify-content-between">
                            <h4 class="card-title">Username (agregar href)</h4>
                            <h5 class="ms-4">
                                <i class="bi bi-star-fill ml-2"></i> ${review.rating}/5
                            </h5>
                        </div>
                        <div class="d-flex">
<%--                            <c:choose>--%>
<%--                                <c:when test="${review.currentUserHasLiked}">--%>
                                    <form action="${pageContext.request.contextPath}/unlikeReview" method="post">
                                        <input type="hidden" name="reviewId" value="${review.reviewId}"/>
                                        <input type="hidden" name="mediaId" value="${media.mediaId}"/>
                                        <button class="btn btn-success">
                                            <i class="bi bi-hand-thumbs-up-fill"></i>
                                                ${review.reviewLikes}
                                            <spring:message code="details.liked"/>
                                        </button>
                                    </form>
<%--                                </c:when>--%>
<%--                                <c:otherwise>--%>
<%--                                    <form action="${pageContext.request.contextPath}/likeReview" method="post">--%>
<%--                                        <input type="hidden" name="reviewId" value="${review.reviewId}"/>--%>
<%--                                        <input type="hidden" name="mediaId" value="${media.mediaId}"/>--%>
<%--                                        <button class="btn btn-success">--%>
<%--                                            <i class="bi bi-hand-thumbs-up"></i>--%>
<%--                                                ${review.reviewLikes}--%>
<%--                                            <spring:message code="details.like"/>--%>
<%--                                        </button>--%>
<%--                                    </form>--%>
<%--                                </c:otherwise>--%>
<%--                            </c:choose>--%>
<%--                            <c:if test="${currentUsername==review.username}">--%>
                                <button class="ms-1 btn btn-primary btn-sm" onclick="openPopup('rate-popup')">
                                    <i class="bi bi-pencil"></i>
                                    <spring:message code="details.editReview"/>
                                </button>
<%--                            </c:if>--%>
                        </div>
                        <p class="mt-2 card-text">Review review review </p>
                    </div>
                </div>
            </div>
            <%--<c:if test="${review.hasComments}">--%>
            <div class="m-3">
                <div class="input-group mt-2 mb-3">
                    <textarea class="form-control" placeholder="<spring:message code="details.addComment"/>" aria-label="With textarea"></textarea>
                </div>
                <c:forEach begin="1" end="5">
                    <div class="mb-2 mt-2 card card-body">
                        <div class="d-flex justify-content-between">
                            <h6 class="card-title">Username (agregar href)</h6>
                            <div class="d-flex">
                                <a class="me-1 btn-sm btn btn-outline-success">
                                    <i class="m-1 bi bi-hand-thumbs-up"></i>
                                </a>
                                <a class="btn btn-sm btn-outline-danger">
                                    <i class="m-1 bi bi-hand-thumbs-down"></i>
                                </a>
                            </div>
                        </div>
                        <p class="card-text">Comment Commetn Commetn</p>
                    </div>
                </c:forEach>
            </div>
            <%--</c:if>--%>
        </div>
    </div>
    <div class="d-flex flex-column m-3" style="max-width: 20vw">
        <div class="card">
            <div class="card-body">
                <a href="${pageContext.request.contextPath}/featuredList/topRatedMovies" class="mb-1 card-title">
                    <spring:message code="review.recommended"/>
                </a>
                <c:forEach var="movie" items="${movieList}" end="5">
                    <a href="${pageContext.request.contextPath}/details/${movie.mediaId}" class="card text-bg-dark m-1">
                        <div class="card-img-container">
                            <img class="cropCenter" src="${movie.posterPath}" alt="media poster">
                            <div class="card-img-overlay">
                                <h6 class="card-title text-center">${movie.name}</h6>
                                <div class="d-flex justify-content-evenly">
                                    <p class="card-text">
                                        <i class="bi bi-star-fill"></i>
                                            ${movie.tmdbRating}
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
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</body>
</html>
