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
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title>Moovie List</title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=67" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=87"></script>

    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
</head>
<body>
<c:import url="navBar.jsp"/>
<h1 style="font-size: 60px; font-weight: bold;"><spring:message
        code="mpl.title"/></h1>

<table class="table table-striped" id="movieTable">
    <thead>
    <tr>
        <th scope="col"></th>
        <th scope="col"><spring:message
                code="mpl.picture"/></th>
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
                        <div class="col-auto">
                            <a href="${pageContext.request.contextPath}/profile/${user.username}"
                               style="text-decoration: none; color: inherit;">
                                <img src="${pageContext.request.contextPath}/profile/image/${user.username}" class="img-fluid" width="100"
                                     height="100" alt="${mediaList[loop.index].name} poster"/>
                            </a>
                        </div>
                    </td>

                    <!-- Username -->
                    <td>
                        <div class="col-auto"><c:out value="${user.username}"/></div>
                    </td>

                    <!-- Moovie List Count -->
                    <td>${user.moovieListCount}</td>

                    <!-- Reviews Count -->
                    <td>${user.reviewsCount}</td>

                    <!-- MilkyPoints -->
                    <td>${user.milkyPoints}</td>
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

<c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
    <c:param name="mediaPages" value="${numberOfPages}"/>
    <c:param name="currentPage" value="${currentPage + 1}"/>
    <c:param name="url" value="${urlBase}"/>
</c:import>
