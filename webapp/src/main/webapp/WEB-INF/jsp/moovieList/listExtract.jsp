<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sprng" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title>Moovie List</title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=67" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=87"></script>

    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
</head>
<body>

<div class="container d-flex flex-column">
    <div class="header d-flex text-center" style="background-image: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.2)), url('${mediaList[0].backdropPath}'); background-size: cover; background-position: center;">
        <div class="d-flex flex-column flex-grow-1">
            <h1 style="font-size: 60px; font-weight: bold;">
                <c:choose>
                    <c:when test="${moovieList.name == 'Watchlist'}">
                        <spring:message code="listExtract.watchListTitle"/>
                    </c:when>
                    <c:when test="${moovieList.name == 'Watched'}">
                        <spring:message code="listExtract.watchedListTitle"/>
                    </c:when>
                    <c:otherwise>
                    <c:out value="${moovieList.name}"/>
                    </c:otherwise>
                </c:choose>
            </h1>
            <h3><c:out value="${moovieList.description}"/></h3>
            <c:if test="${param.publicList == 'true'}">
                <h4 style="color: ghostwhite;"><spring:message code="listExtract.by"/>
                    <a style="text-decoration: none; color: inherit;" href="${pageContext.request.contextPath}/profile/${listOwner}">
                        <c:out value="${listOwner}"/>
                        <c:if test="${moovieList.hasBadge}"><i class="bi bi-trophy"></i></c:if>
                    </a>
                </h4>
            </c:if>
        </div>
        <c:if test="${param.publicList == 'true'}">
            <c:if test="${currentUser.username != moovieList.username}">
                <c:if test="${moovieList.username != 'Moovie'}">
                    <sec:authorize access="isAuthenticated()">
                        <div style="margin-bottom: 15px">
                            <a href="${pageContext.request.contextPath}/reports/new?id=${moovieList.moovieListId}&reportedBy=${currentUser.userId}&type=moovieList" class="btn btn-warning" style="font-size: 14px;margin-left: 10px;" ><spring:message code="report.title"/>
                                <i class="bi bi-flag"></i>
                            </a>
                        </div>
                    </sec:authorize>
                </c:if>
            </c:if>
            <c:if test="${isOwner}">
                <div style="position: absolute;" class="d-flex">
                    <button onclick="openPopup('popup')" class="btn btn-danger btn-sm">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
                <div class="popup-overlay" onclick="closePopup('popup')"></div>
                <div style="background-color: transparent; box-shadow: none" class="popup">
                    <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                        <h5 class="alert-heading"><spring:message code="listExtract.confirmListDeletion"/></h5>
                        <p><spring:message code="listExtract.confirmListDeletionPrompt"/></p>
                        <div class="d-flex justify-content-evenly">
                            <form class="m-0" action="${pageContext.request.contextPath}/deleteMoovieList/${moovieList.moovieListId}" method="post">
                                <button type="submit" class="btn btn-danger"><spring:message code="listExtract.delete"/></button>
                            </form>
                            <button type="button" onclick="closePopup('popup')" class="btn btn-secondary" id="cancelModButton"><spring:message code="listExtract.cancel"/></button>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${!isOwner}">
                <sec:authorize access="hasRole('ROLE_MODERATOR')">
                    <div style="position: absolute;" class="d-flex">
                        <button onclick="openPopup('popup')" class="btn btn-danger btn-sm">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                    <div class="popup-overlay" onclick="closePopup('popup')"></div>
                    <div style="background-color: transparent; box-shadow: none" class="popup">
                        <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                            <h5 class="alert-heading"><spring:message code="listExtract.confirmListDeletion"/></h5>
                            <p><spring:message code="listExtract.confirmListDeletionPrompt"/></p>
                            <div class="d-flex justify-content-evenly">
                                <form class="m-0" action="${pageContext.request.contextPath}/deleteList/${moovieList.moovieListId}" method="post">
                                    <button type="submit" class="btn btn-danger"><spring:message code="listExtract.delete"/></button>
                                </form>
                                <button type="button" onclick="closePopup('popup')" class="btn btn-secondary" id="cancelModButton"><spring:message code="listExtract.cancel"/></button>
                            </div>
                        </div>
                    </div>
                </sec:authorize>
            </c:if>


        </c:if>



        <c:if test="${isOwner}">
            <div style="position: relative;" class="d-flex">
                <div style="position: absolute; top: 0; right: 0;">
                    <a href="${pageContext.request.contextPath}/editList/${moovieList.moovieListId}">
                        <button class="btn btn-primary btn-sm">
                            <i class="bi bi-pencil"></i>
                        </button>
                    </a>
                </div>
            </div>
        </c:if>
    </div>
    <div class="buttons">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <c:if test="${moovieList.type==publicType}">
            <div class="d-flex flex-row justify-content-center">
                <div>
                    <form action="${pageContext.request.contextPath}/like" method="POST">
                        <input type="hidden" name="listId" value="${moovieList.moovieListId}"/>
                        <c:choose>
                            <c:when test="${moovieList.currentUserHasLiked}">
                                <button type="submit" class="btn btn-style"><i
                                        class="bi bi-hand-thumbs-up-fill"></i><spring:message code="listExtract.liked" arguments="${moovieList.likeCount}"/>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" class="btn btn-style"><i
                                        class="bi bi-hand-thumbs-up"></i><spring:message code="listExtract.like" arguments="${moovieList.likeCount}"/>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </div>
                <div>
                    <form action="${pageContext.request.contextPath}/followList" method="POST">
                        <input type="hidden" name="listId" value="${moovieList.moovieListId}"/>
                        <c:choose>
                            <c:when test="${moovieList.currentUserHasFollowed}">
                                <button type="submit" class="btn btn-style2"><i class="bi bi-bell-fill"></i> <spring:message code="listExtract.following"/>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" class="btn btn-style2"><i class="bi bi-bell"></i> <spring:message code="listExtract.follow"/>
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </form>
                </div>
            </div>
            </c:if>
            <form id="sortForm" method="get">
                <div style="display: flex; align-items: center;">
                    <h2 style="padding-right: 4px"><spring:message code="listExtract.sortBy"/></h2>
                    <c:if test="${param.listType != null}">
                        <input style="display: none" name="list" value="${param.listType}"/>
                    </c:if>
                    <select name="orderBy" class="form-select filter-width" aria-label="Filter!" id="sortSelect">
                        <option ${'customOrder' == param.orderBy ? 'selected' : ''} value="customOrder"><spring:message code="listExtract.orderByCustomOrder"/></option>
                        <option ${'name' == param.orderBy ? 'selected' : ''} value="name"><spring:message code="listExtract.orderByTitle"/></option>
                        <option ${'type' == param.orderBy ? 'selected' : ''} value="type"><spring:message code="listExtract.orderByType"/></option>
                        <option ${'tmdbRating' == param.orderBy ? 'selected' : ''} value="tmdbRating"><spring:message code="listExtract.orderByScore"/></option>
                        <option ${'totalRating' == param.orderBy ? 'selected' : ''} value="totalRating"><spring:message code="listExtract.orderByUsersScore"/> </option>
                        <option ${'releaseDate' == param.orderBy ? 'selected' : ''} value="releaseDate"><spring:message code="listExtract.orderByReleaseDate"/></option>
                    </select>
                    <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='desc'? 'desc':'asc'}">
                    <div style="margin: 0;" class="btn btn-style" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                        <i id="sortIcon" class="bi bi-arrow-${param.order == 'desc' ? 'down' : 'up'}-circle-fill"></i>
                    </div>
                    <button type="submit" id="applyButton" class="btn btn-style2"><spring:message code="listExtract.apply"/></button>
                </div>
            </form>
        </div>
    </div>
    <div>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show m-2" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div><c:out value="${errorMessage}"/> <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}"><c:out value="${insertedMooovieList.name}"/></a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show m-2" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div><c:out value="${successMessage}"/> <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}"><c:out value="${insertedMooovieList.name}"/></a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
    </div>
    <div>
        <c:if test="${moovieList.name!='Watched'}">
        <h4><spring:message code="listExtract.listProgress"/></h4>
        <div class="progress">
            <div class="progress-bar" role="progressbar" style="width: ${listCount==0 ? 0:(watchedCount*100)/listCount}%;"
                 id="progressBar"
                 aria-valuenow="${listCount==0 ? 0:(watchedCount*100)/listCount}" aria-valuemin="0" aria-valuemax="100">
                <c:out value="${(watchedCount*100)/listCount}"/>%
            </div>
        </div>
        </c:if>
    </div>
    <div style="display: flex; align-items: center;justify-content: center">
        <c:if test="${moviesCount > 0}">
            <h4><spring:message code="listExtract.moviesCount" arguments="${moviesCount}"/></h4>
        </c:if>
        <c:if test="${moviesCount > 0 && tvSeriesCount > 0}">
            <h4 style="margin-right: 5px;margin-left: 5px"><spring:message code="listExtract.and"/></h4>
        </c:if>
        <c:if test="${tvSeriesCount > 0}">
            <h4><spring:message code="listExtract.seriesCount" arguments="${tvSeriesCount}"/></h4>
        </c:if>
    </div>
    <table class="table table-striped" id="movieTable">
        <thead>
        <tr>
            <th scope="col"></th>
            <th scope="col"><spring:message code="listExtract.orderByTitle"/></th>
            <th scope="col"><spring:message code="listExtract.orderByType"/></th>
            <th scope="col"><spring:message code="listExtract.orderByScore"/></th>
            <th scope="col"><spring:message code="listExtract.orderByUsersScore"/></th>
            <th scope="col"><spring:message code="listExtract.orderByReleaseDate"/></th>
            <th scope="col" style="width: 50px"></th>
        </tr>
        </thead>
        <c:choose>
        <c:when test="${not empty mediaList}">
        <tbody>
        <c:forEach var="index" items="${mediaList}" varStatus="loop">
        <tr>
            <!-- Title -->
            <td>
                <div class="col-auto">
                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                       style="text-decoration: none; color: inherit;">
                        <img src="${mediaList[loop.index].posterPath}" class="img-fluid" width="100"
                             height="100" alt="${mediaList[loop.index].name} poster"/>
                    </a>
                </div>
            </td>
            <td>
                <div class="col-auto" style="text-align: center">
                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                       style="text-decoration: none; color: inherit;">
                        <strong><c:out value="${mediaList[loop.index].name}"/></strong>
                    </a>
                </div>
</div>
<!-- Type -->
<td>
    <c:choose>
        <c:when test="${mediaList[loop.index].type}">
            <spring:message code="listExtract.series"/>
        </c:when>
        <c:otherwise>
            <spring:message code="listExtract.movies"/>
        </c:otherwise>
    </c:choose>
</td>
<!-- Score -->
<td><c:out value="${mediaList[loop.index].tmdbRating}"/><i class="bi bi-star-fill" style="margin-left: 5px"></i>
</td>
<!--User Score -->
<td>
    <c:choose>
<c:when test="${mediaList[loop.index].voteCount>0}">
<c:out value="${mediaList[loop.index].totalRating}"/><i class="bi bi-star" style="margin-left: 5px"></i>
</c:when>
        <c:otherwise>
            <span data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.noUsersRatingsMessage"/>">
            N/A<i class="bi bi-star" style="margin-left: 5px"></i>
                </span>
        </c:otherwise>
    </c:choose>
</td>
<td>
    <span><c:out value="${mediaList[loop.index].releaseDate}"/></span>
</td>
<td>
    <sec:authorize access="isAuthenticated()">
        <c:choose>
            <c:when test="${mediaList[loop.index].watched}">
                <div class="col-auto text-center m-0">
                    <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                        <input type="hidden" name="listId" value="${watchedListId}"/>
                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-eye-fill" style="color: green; cursor: pointer;"></i>
                        </span>
                        </button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-auto text-center m-0">
                    <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                        <input type="hidden" name="listId" value="${watchedListId}"/>
                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-eye-fill" style="color: gray; cursor: pointer;"></i>
                        </span>
                        </button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${mediaList[loop.index].watchlist}">
                <div class="col-auto text-center m-0">
                    <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                        <input type="hidden" name="listId" value="${watchlistId}"/>
                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-bookmark-check-fill" style="color: green; cursor: pointer;"></i>
                        </span>
                        </button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-auto text-center m-0">
                    <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                        <input type="hidden" name="listId" value="${watchlistId}"/>
                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-bookmark-check-fill" style="color: gray; cursor: pointer;"></i>
                        </span>
                        </button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
    </sec:authorize>
</td>
</tr>
</c:forEach>
</tbody>

</c:when>
<c:otherwise>
    <tbody>
    <tr>
        <td colspan="10" style="text-align: center"><spring:message code="listExtract.listIsEmpty"/></td>
    </tr>
    </tbody>
</c:otherwise>
</c:choose>
</table>
<c:import url="/WEB-INF/jsp/common/pagination.jsp">
    <c:param name="mediaPages" value="${numberOfPages}"/>
    <c:param name="currentPage" value="${currentPage + 1}"/>
    <c:param name="url" value="${urlBase}"/>
</c:import>
</div>
</body>
</html>
<script src="${pageContext.request.contextPath}/resources/moovieListFunctions.js?version=81"></script>