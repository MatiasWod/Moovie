<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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

    <script src="${pageContext.request.contextPath}/resources/moovieListFunctions.js?version=81"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
</head>
<body>

<div class="container d-flex flex-column">
    <div class="header d-flex text-center" style="background-image: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.2)), url('${mediaList[0].backdropPath}'); background-size: cover; background-position: center;">
        <div class="d-flex flex-column flex-grow-1">
            <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${moovieList.name}"/></h1>
            <h3><c:out value="${moovieList.description}"/></h3>
            <c:if test="${param.publicList == 'true'}">
                <h4 style="color: ghostwhite;">by
                    <a style="text-decoration: none; color: inherit;" href="${pageContext.request.contextPath}/profile/${listOwner}">
                        <c:out value="${listOwner}"/>
                    </a>
                </h4>
            </c:if>
        </div>
        <c:if test="${param.publicList == 'true'}">
            <sec:authorize access="hasRole('ROLE_MODERATOR')">
                <div style="position: absolute;" class="d-flex">
                    <button onclick="openPopup('popup')" class="btn btn-danger btn-sm">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
                <div class="popup-overlay" onclick="closePopup('popup')"></div>
                <div style="background-color: transparent; box-shadow: none" class="popup">
                    <div style="box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);" class="alert alert-danger" role="alert">
                        <h5 class="alert-heading">Confirm List Deletion</h5>
                        <p>Are you sure you want to delete this list? Once deleted, it cannot be recovered</p>
                        <div class="d-flex justify-content-evenly">
                            <form class="m-0" action="${pageContext.request.contextPath}/deleteList/${moovieList.moovieListId}" method="post">
                                <button type="submit" class="btn btn-danger">Delete</button>
                            </form>
                            <button type="button" onclick="closePopup('popup')" class="btn btn-secondary" id="cancelModButton">Cancel</button>
                        </div>
                    </div>
                </div>
            </sec:authorize>
        </c:if>
    </div>
    <c:if test="${param.publicList == 'true'}">
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" id="errorAlert" role="alert">
                    ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
    </c:if>
    <div class="buttons">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div class="d-flex flex-row justify-content-center">
                <div>
                <c:if test="${param.publicList == 'true'}">
                    <form action="${pageContext.request.contextPath}/like" method="POST">
                            <input type="hidden" name="listId" value="${moovieList.moovieListId}"/>
                            <c:choose>
                                <c:when test="${isLiked}">
                                    <button type="submit" class="btn btn-style"><i
                                            class="bi bi-hand-thumbs-up-fill"></i>${likedCount} Liked
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" class="btn btn-style"><i
                                            class="bi bi-hand-thumbs-up"></i>${likedCount}
                                        Like
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </div>
                    <div>
                        <form action="${pageContext.request.contextPath}/followList" method="POST">
                            <input type="hidden" name="listId" value="${moovieList.moovieListId}"/>
                            <c:choose>
                                <c:when test="${isFollowed}">
                                    <button type="submit" class="btn btn-style2"><i class="bi bi-bell-fill"></i> Following
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button type="submit" class="btn btn-style2"><i class="bi bi-bell"></i> Follow
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </div>
                </c:if>
                </div>

            <form id="sortForm" method="get">
                <div style="display: flex; align-items: center;">
                    <h2 style="padding-right: 4px">Sort by</h2>
                    <c:if test="${param.listType != null}">
                        <input style="display: none" name="list" value="${param.listType}"/>
                    </c:if>
                    <select name="orderBy" class="form-select filter-width" aria-label="Filter!" id="sortSelect">
                        <option ${'name' == param.orderBy ? 'selected' : ''} value="name">Title</option>
                        <option ${'type' == param.orderBy ? 'selected' : ''} value="type">Type</option>
                        <option ${'tmdbrating' == param.orderBy ? 'selected' : ''} value="tmdbrating">Score</option>
                        <option ${'releasedate' == param.orderBy ? 'selected' : ''} value="releasedate">Release Date</option>
                    </select>
                    <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='desc'? 'desc':'asc'}">
                    <div style="margin: 0;" class="btn btn-style" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                        <i id="sortIcon" class="bi bi-arrow-${param.order == 'desc' ? 'down' : 'up'}-circle-fill"></i>
                    </div>
                    <button type="submit" id="applyButton" class="btn btn-style2">Apply</button>
                </div>
            </form>
        </div>
    </div>
    <div>
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
    </div>
    <div>
        <h4>List progress</h4>
        <div class="progress">
            <div class="progress-bar" role="progressbar" style="width: ${(watchedCount*100)/listCount}%;"
                 id="progressBar"
                 aria-valuenow="${(watchedCount*100)/listCount}" aria-valuemin="0" aria-valuemax="100">
                ${(watchedCount*100)/listCount}%
            </div>
        </div>
    </div>
    <div style="display: flex; align-items: center;justify-content: center">
        <c:if test="${moviesCount > 0}">
            <h4>${moviesCount} Movies</h4>
        </c:if>
        <c:if test="${moviesCount > 0 && tvSeriesCount > 0}">
            <h4 style="margin-right: 5px;margin-left: 5px">and</h4>
        </c:if>
        <c:if test="${tvSeriesCount > 0}">
            <h4>${tvSeriesCount} Series</h4>
        </c:if>
    </div>
    <table class="table table-striped" id="movieTable">
        <thead>
        <tr>
            <th scope="col"></th>
            <th scope="col">Title</th>
            <th scope="col">Type</th>
            <th scope="col">Score</th>
            <th scope="col">Release Date</th>
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
                        <strong>${mediaList[loop.index].name}</strong>
                    </a>
                </div>
</div>
<!-- Type -->
<td>
    <c:choose>
        <c:when test="${mediaList[loop.index].type}">
            Tv Series
        </c:when>
        <c:otherwise>
            Movie
        </c:otherwise>
    </c:choose>
</td>
<!-- Score -->
<td>${mediaList[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
</td>
<td>
    <span>${mediaList[loop.index].releaseDate}</span>
</td>
<td>
    <div class="popup-overlay watch-popup-overlay" onclick="closePopup('remove-watch-popup-${loop.index}')"></div>
    <c:choose>
        <c:when test="${mediaList[loop.index].watched}">
            <div class="col-auto text-center">
            <span class="d-inline-block" onclick="openPopup('remove-watch-popup-${loop.index}')" tabindex="0"
                  data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="You watched this media">
                <i class="bi bi-eye-fill" style="color: green; cursor: pointer;"></i>
            </span>
            </div>
        </c:when>
        <c:otherwise>
            <div class="col-auto text-center">
            <span class="d-inline-block" onclick="openPopup('add-watch-popup-${loop.index}')" tabindex="0"
                  data-bs-toggle="popover" data-bs-trigger="hover"
                  data-bs-content="You haven't watched this media">
                <i class="bi bi-eye" style="color: grey; cursor: pointer;"></i>
            </span>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="popup-overlay remove-watch-popup-${loop.index}-overlay"
         onclick="closePopup('remove-watch-popup-${loop.index}')"></div>
    <div class="popup remove-watch-popup-${loop.index}">
        <h2>Remove "${mediaList[loop.index].name}" from Watched?</h2>
        <div class="text-center" style="margin-top: 20px">
            <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                        onclick="closePopup('remove-watch-popup-${loop.index}')">No
                </button>
                <input type="hidden" name="listId" value="${watchedListId}"/>
                <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                <button type="submit" class="btn btn-dark" style="margin-inline: 10px">Yes</button>
            </form>
        </div>
    </div>
    <div class="popup-overlay add-watch-popup-${loop.index}-overlay"
         onclick="closePopup('add-watch-popup-${loop.index}')"></div>
    <div class="popup add-watch-popup-${loop.index}">
        <h2>Add "${mediaList[loop.index].name}" to Watched?</h2>
        <div class="text-center" style="margin-top: 20px">
            <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                        onclick="closePopup('add-watch-popup-${loop.index}')">No
                </button>
                <input type="hidden" name="listId" value="${watchedListId}"/>
                <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                <button type="submit" class="btn btn-dark" style="margin-inline: 10px">Yes</button>
            </form>
        </div>
    </div>
</td>
</tr>
</c:forEach>
</tbody>

</c:when>
<c:otherwise>
    <tbody>
    <tr>
        <td colspan="5">List is empty</td>
    </tr>
    </tbody>
</c:otherwise>
</c:choose>
</table>
<c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
    <c:param name="mediaPages" value="${numberOfPages}"/>
    <c:param name="currentPage" value="${currentPage + 1}"/>
    <c:param name="url" value="${urlBase}"/>
</c:import>
</div>
</body>
</html>


