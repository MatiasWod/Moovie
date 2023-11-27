<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=83" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>


    <title><spring:message code="createList.title"/></title>
    <script async src="${pageContext.request.contextPath}/resources/createListFunctions.js?version=84"></script>
    <script async src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>

</head>
<body style="background: whitesmoke">

<c:import url="../common/navBar.jsp"/>
<sec:authorize access="isAuthenticated()">
    <c:set var="selectedGenres" value="${fn:split(param.g, ',')}" />
    <c:set var="selectedProviders" value="${fn:split(param.providers, ',')}" />
    <div class="container d-flex flex-column">
        <div class="container d-flex flex-row ">
            <div class="container d-flex flex-column">
                <c:if test="${param.q != null && param.q.length() > 0}">
                    <h3>
                        <spring:message code="discover.results" arguments="${param.q}"/>
                    </h3>
                </c:if>
                <c:import url="../common/filterButtons.jsp">
                    <c:param name="g" value="${param.g}"/>
                    <c:param name="providers" value="${param.providers}"/>
                    <c:param name="url" value="createList"/>
                    <c:param name="searchBar" value="true"/>
                </c:import>
                <div class="scrollableDiv flex-wrap d-flex">
                    <c:if test="${fn:length(mediaList) == 0 }">
                        <div class="d-flex m-2 flex-column">
                            <spring:message code="createList.noResults"/>
                            <a class="btn mt-2 btn-outline-success align-bottom" href="${pageContext.request.contextPath}/createList"><spring:message code="createList.call_to_action"/></a>
                        </div>
                    </c:if>
                    <c:forEach var="movie" items="${mediaList}" end="24">
                        <div onclick="displayMediaName('${(fn:replace(fn:replace(movie.name,"'", "\\'"), "\"", "&quot;"))}',${movie.mediaId})" class="poster card text-bg-dark m-1">
                            <div id="${movie.mediaId}" class="card-img-container"> <!-- Add a container for the image -->

                                <img class="cropCenter async-image" src="${pageContext.request.contextPath}/resources/defaultPoster.png" data-src="${movie.posterPath}" alt="media poster">

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
                        </div>
                    </c:forEach>
                </div>
                <div class="m-1">
                    <c:import url="/WEB-INF/jsp/common/pagination.jsp">
                        <c:param name="mediaPages" value="${numberOfPages}"/>
                        <c:param name="currentPage" value="${currentPage + 1}"/>
                        <c:param name="url" value="/createList?m=${param.m}&g=${param.g}&q=${param.q}"/>
                    </c:import>
                </div>
            </div>
            <div id="preview" style="position: relative" class="container d-flex p-0 container-gray-transp fullHeightDiv thirty-width">
                <div class="image-blur height-full background" style="background: dimgray"></div>
                <div style="position: absolute;top: 0;left: 0;height: 100%;overflow: hidden" class="d-flex p-4 container flex-column ">

                    <div class="d-flex justify-content-between">
                        <h2 class="m-2"><spring:message code="createList.listName"/></h2>
                        <button class="btn btn-danger m-2" onclick="deleteStorage()"><spring:message code="createList.clearAll"/></button>
                    </div>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show" id="errorAlert" role="alert">
                            <div class="d-flex justify-content-between align-items-center">
                                <div><spring:message code="${errorMessage}"/></div>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </div>
                    </c:if>
                    <div class="d-flex flex-column">
                        <form:form modelAttribute="ListForm" action="${pageContext.request.contextPath}/createListAction"
                                   method="POST" id="create-form">
                            <form:input path="listName" name="listName" id="list-name" required="required"
                                        class="form-control me-2 createListInput" maxlength="50"/>
                            <span id="listNameCharCount" class="text-muted"><span id="listNameRemainingChars">0</span>/50</span>
                            <form:errors path="listName" cssClass="error"/>
                            <h3 class="m-2" ><spring:message code="createList.description"/></h3>
                            <form:textarea path="listDescription" id="list-description" class="review-textarea" rows="3" name="listDescription"
                                            maxlength="255" />
                            <span id="listDescriptionCharCount" class="text-muted"><span id="listDescriptionRemainingChars">0</span>/255</span>
                            <form:errors path="listDescription" cssClass="error"/>
                            <form:input path="mediaIdsList" type="hidden"  name="mediaIds" id="selected-create-media"/>
                            <div style="max-height: 100px" class="scrollableMedia d-flex flex-column m-2 p-2" id="selected-media-names">
                                <c:forEach var="sel" items="${selected}">
                                    <div class="other-distinct d-flex justify-content-between ">
                                        <div id="${sel.mediaId}" class="distinct-class"><c:out value="${sel.name}"/></div>
                                        <i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="d-flex justify-content-between">
                                <button id="preview-details" type="submit" class="btn btn-lg btn-outline-success mt-4"><spring:message code="createList.createList"/></button>
                                <button id="toggle-button" type="button" data-bs-toggle="button" class="btn btn-outline-secondary mt-4"><spring:message code="createList.visibility"/>
                                    <i id="toggle-icon" class="bi bi-eye-fill"></i>
                                </button>
                                <form:input type="hidden" id="toggle-state" name="toggleState" value="true" path="privateList"/>
                            </div>
                        </form:form>
                    </div>
                    <div>
                        <h6><spring:message code="createList.keepProgressMessage"/></h6>
                    </div>
                    <div class="d-flex" id="preview-list"></div>
                </div>
            </div>
        </div>
    </div>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <c:import url="signUpAlert.jsp"/>
</sec:authorize>

<script src="${pageContext.request.contextPath}/resources/createListRealTimeFunctions.js?version=79"></script>
</body>
</html>
