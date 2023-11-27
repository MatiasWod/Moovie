<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title>Moovie List</title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=67" rel="stylesheet"/>

    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
</head>
<body>
<c:import url="../common/navBar.jsp"/>
<div class="container d-flex flex-column">

    <div class="flex-row d-flex text-center">
        <div class="d-flex align-items-center justify-content-center flex-grow-1">
            <img src="${pageContext.request.contextPath}/resources/logo.png" height="100" alt="Moovie logo">
            <h1 style="font-size: 60px; font-weight: bold;"><spring:message code="mpl.title"/></h1>
            <img src="${pageContext.request.contextPath}/resources/logo.png" height="100" alt="Moovie logo">
        </div>
    </div>

<table class="table table-striped" id="movieTable">
    <thead>
    <tr>
        <th scope="col"></th>
        <th scope="col"></th>
        <th scope="col"><spring:message
                code="mpl.username"/></th>
        <th scope="col"><spring:message
                code="mpl.moovieListCount"/></th>
        <th scope="col"><spring:message
                code="mpl.reviewsCount"/></th>
        <th scope="col"><spring:message
                code="mpl.points"/></th>
    </tr>
    </thead>
    <c:choose>
        <c:when test="${not empty profiles}">
            <tbody>
            <c:forEach var="user" items="${profiles}" varStatus="loop">
                <tr>
                    <td></td> <!-- Placeholder for an icon or any additional column -->

                    <!-- Profile Picture -->
                    <td>
                        <div class="col-auto" style="margin: 10px">
                            <a href="${pageContext.request.contextPath}/profile/${user.username}"
                               style="text-decoration: none; color: inherit;">
                                <img class="profile-image cropCenter" style="height:100px;width:100px;border: solid black; border-radius: 50%" src="${pageContext.request.contextPath}/profile/image/${user.username}" alt="profile pic">
                            </a>
                        </div>
                    </td>

                    <!-- Username -->
                    <td>
                        <a href="${pageContext.request.contextPath}/profile/${user.username}"
                           style="text-decoration: none; color: inherit;">
                        <div class="col-auto"><c:out value="${user.username}"/><c:if test="${user.hasBadge}"><i class="bi bi-trophy"></i></c:if>
                        </div>

                        </a>
                    </td>

                    <!-- Moovie List Count -->
                    <td>
                            <a href="${pageContext.request.contextPath}/profile/${user.username}?list=user-lists"
                               style="text-decoration: none; color: inherit;">
                            <c:out value="${user.moovieListCount}"/> <i class="bi bi-list-ul"></i>
                            </a>
                    </td>

                    <!-- Reviews Count -->
                    <td>
                        <a href="${pageContext.request.contextPath}/profile/${user.username}?list=reviews"
                           style="text-decoration: none; color: inherit;">
                            <c:out value="${user.reviewsCount}"/> <i class="bi-star"></i>
                        </a>
                    </td>

                    <!-- MilkyPoints -->
                    <td>
                        <a href="${pageContext.request.contextPath}/profile/${user.username}"
                           style="text-decoration: none; color: inherit;">
                        <span><c:out value="${user.milkyPoints}"/><img src="${pageContext.request.contextPath}/resources/logo.png" height="30" alt="Moovie logo"></span>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </c:when>
        <c:otherwise>
            <tbody>
            <tr>
                <td colspan="4" style="text-align: center"><spring:message
                        code="mpl.userListIsEmpty"/></td>
            </tr>
            </tbody>
        </c:otherwise>
    </c:choose>
</table>
</div>
<c:import url="/WEB-INF/jsp/common/pagination.jsp">
    <c:param name="mediaPages" value="${numberOfPages}"/>
    <c:param name="currentPage" value="${currentPage + 1}"/>
    <c:param name="url" value="${urlBase}"/>
</c:import>
