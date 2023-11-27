<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 10/14/2023
  Time: 6:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">

    <title><spring:message code="search.title"/></title>
</head>
<body>
<c:import url="../common/navBar.jsp"/>

<div class="container d-flex flex-column">

    <c:if test="${moovieListFlag}">
        <div class="container d-flex justify-content-between mt-2 p-2">
            <h3><spring:message code="search.lists" arguments="${param.query}"/></h3>
            <a href="${pageContext.request.contextPath}/lists?query=${param.query}"><spring:message code="search.seeMore"/></a>
        </div>
        <hr class="my-1">
        <div class="d-flex flex-row flex-wrap justify-content-center">
            <c:forEach items="${moovieListsList}" var="cardList">
                <%@include file="../common/listCard.jsp"%>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${nameMediaFlag}">
        <div class="container d-flex justify-content-between mt-2 p-2">
            <h3><spring:message code="search.results" arguments="${param.query}"/></h3>
            <a href="${pageContext.request.contextPath}/discover?query=${param.query}"><spring:message code="search.seeMore"/></a>
        </div>
        <hr class="my-1">
        <div class="container d-flex overflow-hidden" style="max-height: 300px;"> <!-- Set a fixed maximum height for the container -->
            <c:forEach var="media" items="${nameMedia}" end="5">
                <a href="${pageContext.request.contextPath}/details/${media.mediaId}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class="cropCenter async-image" src="${pageContext.request.contextPath}/resources/defaultPoster.png" data-src="${media.posterPath}" alt="media poster">
                        <div class="card-img-overlay">
                            <h6 class="card-title text-center"><c:out value="${media.name}"/></h6>
                            <div class="d-flex justify-content-evenly">
                                <p class="card-text">
                                    <i class="bi bi-star-fill"></i>
                                    <c:out value="${media.tmdbRating}"/>
                                </p>
                                <p class="card-text">
                                    <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                                </p>
                            </div>
                            <div class="d-flex justify-content-evenly flex-wrap">
                                <c:forEach var="genre" items="${media.genres}" end="1">
                                    <span class="mt-1 badge text-bg-dark">${fn:replace(genre,"\"" ,"" )}</span>
                                </c:forEach>
                            </div>
                            <div class="d-flex mt-3 justify-content-evenly flex-wrap">
                                <c:forEach var="provider" items="${media.providers}" end="1">
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

    </c:if>

    <c:if test="${actorsFlag}">
        <div style = "margin-bottom: 20px">
            <div class="container d-flex justify-content-between mt-2 p-2">
                <h3><spring:message code="search.actors" arguments="${param.query}"/></h3>
                    <%--            <a href="${pageContext.request.contextPath}/discover?credit=${param.query}"><spring:message code="search.seeMore"/></a>--%>
            </div>
            <hr class="my-1">
            <div class="container d-flex" style="height: 200px;"> <!-- Set a fixed maximum height for the container -->
                <c:forEach var="actor" items="${actors}" end="5">
                    <a href="${pageContext.request.contextPath}/cast/actor/${actor.actorId}" class="poster card text-bg-dark m-1">
                        <div class="card-img-container"> <!-- Add a container for the image -->
                            <c:choose>
                                <c:when test="${actor.profilePath == 'None'}">
                                    <img src="${pageContext.request.contextPath}/resources/defaultProfile.jpg.png" class="cropCenter"  alt="${actor.actorName} profile">
                                </c:when>
                                <c:otherwise>
                                    <img data-src="${actor.profilePath}"
                                         src="${pageContext.request.contextPath}/resources/defaultProfile.jpg"
                                         class="cropCenter async-image" alt="${actor.actorName} profile">
                                </c:otherwise>
                            </c:choose>
                            <div class="card-img-overlay">
                                <h6 class="card-title text-center"><c:out value="${actor.actorName}"/></h6>
                            </div>
                        </div>
                    </a>

                </c:forEach>
            </div>
        </div>
    </c:if>

    <c:if test="${creatorsFlag}">
        <div class="container d-flex justify-content-between mt-2 p-2" >
            <h3><spring:message code="search.creators" arguments="${param.query}"/></h3>
                <%--            <a href="${pageContext.request.contextPath}/discover?credit=${param.query}"><spring:message code="search.seeMore"/></a>--%>
        </div>
        <hr class="my-1">
        <div class="container d-flex" style="height: 200px;"> <!-- Set a fixed maximum height for the container -->
            <c:forEach var="actor" items="${directors}">
                <a href="${pageContext.request.contextPath}/cast/director/${actor.directorId}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class=" cropCenter"  src="${pageContext.request.contextPath}/resources/logo.png"/>
                        <div class="card-img-overlay" style="opacity: 1; background-color: rgba(255,255,255,0.1);">
                            <h3 class="card-title text-center">${actor.name}</h3>
                            <h4 class="card-title text-center"><spring:message code="search.total"/>${actor.totalMedia}</h4>
                        </div>
                    </div>
                </a>
            </c:forEach>
            <c:forEach var="actor" items="${creators}">
                <a href="${pageContext.request.contextPath}/cast/creator/${actor.creatorId}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class=" cropCenter"  src="${pageContext.request.contextPath}/resources/logo.png"/>
                        <div class="card-img-overlay" style="opacity: 1; background-color: rgba(255,255,255,0.1);">
                            <h3 class="card-title text-center">${actor.creatorName}</h3>
                            <h4 class="card-title text-center"><spring:message code="search.total"/>${fn:length(actor.medias)}</h4>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>

    </c:if>

    <c:if test="${usersFlag}">
        <div class="container d-flex justify-content-between mt-3 p-2">
            <h3><spring:message code="search.usersFor" arguments="${param.query}"/></h3>
                <%--            <a href="${pageContext.request.contextPath}/discover?credit=${param.query}">see more</a>--%>
        </div>
        <hr class="my-1">
        <div class="container d-flex overflow-hidden" style="max-height: 300px;">
            <c:forEach items="${usersList}" var="user">
                <a href="${pageContext.request.contextPath}/profile/${user.username}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class=" cropCenter"  src="${pageContext.request.contextPath}/resources/logo.png"/>
                        <div class="card-img-overlay" style="opacity: 1; background-color: rgba(255,255,255,0.1);">
                            <div class="d-flex">
                                <c:if test="${user.role == 2 || user.role == -102}">
                                    <img class="cropCenter" style="height:50px;width:50px" src="${pageContext.request.contextPath}/resources/moderator_logo.png" alt="moderator profile pic">
                                </c:if>
                                <img id="profile-image-${user.username}" style="height: 50px; width: 50px; border:solid black; border-radius: 50%; margin-left: auto" class="cropCenter profile-image" src="${pageContext.request.contextPath}/profile/image/${user.username}" alt=""/>
                            </div>

                            <h3 class="card-title text-center">${user.username}</h3>
                            <div class="d-flex align-items-center">
                                <div class="m-1 d-flex align-items-center">
                                    <i class="bi bi-list-ul"></i>
                                    <h5>
                                            ${user.moovieListCount}
                                    </h5>
                                </div>
                                <div class="m-1 d-flex align-items-center">
                                    <h5>
                                        <i class="bi-star"></i>
                                            ${user.reviewsCount}
                                    </h5>
                                </div>
                                <div class="m-1 d-flex align-items-center">
                                    <h5>
                                        <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                                            ${user.milkyPoints}
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>

    </c:if>



    <c:if test="${!(actorsFlag||creatorsFlag||nameMediaFlag||usersFlag||moovieListFlag)}">
        <div style="border: solid black; min-width: 40%; min-height: 50%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); padding: 5%;" class="container-gray justify-content-center d-flex flex-column">
            <div class="text-center m-3">
                <img style="height: 15vh" src="${pageContext.request.contextPath}/resources/logo.png" alt="logo">
                <h3><spring:message code="search.noResults" arguments="${param.query}"/></h3>
            </div>
            <a class="btn mt-2 btn-outline-success align-bottom" href="#" onclick="history.back()"><spring:message code="search.call_to_action"/></a>
        </div>
    </c:if>
</div>


</body>
<script>
    window.onload = function (){
        localStorage.removeItem("searchValue");

    }


</script>
</html>
