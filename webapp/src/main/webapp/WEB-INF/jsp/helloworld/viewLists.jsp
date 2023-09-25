<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/lists.css?version=58" rel="stylesheet"/>
    <title>Discover your next passion</title>
</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp"/>

<div style="margin-bottom: 30px">
    <div class="container">

        <c:if test="${fn:length(param.search) > 0}">
            <h2>
                Results for: "${param.search}"
            </h2>
        </c:if>
        <h1>
            Community lists
        </h1>
        <form class="d-flex mb-0" role="search" action="${pageContext.request.contextPath}/lists" method="get">
            <input class="form-control me-2" type="search" name="search" placeholder="Search a Moovie List!" aria-label="Moovie List id">
            <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
    </div>

    <div class="lists-container" style="margin-top: 30px">
            <c:if test="${mapTuple.size()==0}">
                <h3>No results were found</h3>
            </c:if>
            <c:forEach var="map" items="${mapTuple}">
                <div class="list-card card"
                     onclick="location.href='${pageContext.request.contextPath}/list/${map.value.first.moovieListId}'">
                    <div class="list-img-container card-img-top">
                        <c:forEach var="image" items="${map.value.second}">
                            <img class="cropCenterImage" src="${image}" alt="...">
                        </c:forEach>
                        <c:forEach begin="${fn:length(map.value.second)}" end="3">
                            <img class="cropCenterImage"
                                 src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
                        </c:forEach>
                    </div>
                    <div class="card-body cardBodyFlex">
                        <div>
                            <h5 class="card-title"><strong><c:out value="${map.value.first.name}"/></strong></h5>
                            <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">by <c:out
                                    value="${map.value.first.userId}"/></p>
                            <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                                <c:out value="${map.value.first.description}"/>
                            </p>

                        </div>
                    </div>

                </div>
            </c:forEach>
        </div>
</div>
</body>
</html>
