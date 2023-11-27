<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/lists.css?version=60" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=65" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <title><spring:message code="viewLists.title"/></title>
    <script src="${pageContext.request.contextPath}/resources/browseListsFunctions.js?version=87"></script>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>

</head>
<body style="background: whitesmoke">
<c:import url="../common/navBar.jsp">

</c:import>

<div style="margin-bottom: 30px">
    <div class="container">

        <c:if test="${fn:length(param.search) > 0}">
            <h2>
                <spring:message code="viewLists.results" arguments="${param.search}"/>
            </h2>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div><c:url value="${successMessage}"/></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="<spring:message code="viewLists.close"/>"></button>
                </div>
            </div>
        </c:if>
        <h1>
            <spring:message code="viewLists.communityLists"/>
        </h1>
        <div class="d-flex justify-content-between align-items-center">
            <form class="flex-grow-1" role="search" action="${pageContext.request.contextPath}/lists" method="get">
                <div class="d-flex" style="margin-right: 5px">
                    <input class="form-control" style="margin-right: 5px" type="search" name="search" placeholder="<spring:message code="viewLists.searchMoovieList"/>" aria-label="Moovie List id">
                    <button class="btn btn-outline-success ml-2" style="margin-right: 5px" type="submit"><spring:message code="viewLists.search"/></button>
                </div>
            </form>
            <form id="sortForm" method="get" class="d-flex align-items-center">
                <h2 style="padding-right: 4px"><spring:message code="viewLists.sortBy"/></h2>
                <select name="orderBy" class="form-select filter-width" aria-label="<spring:message code="viewLists.filter"/>" id="sortSelect">
<%--                    <option ${'name' == param.orderBy ? 'selected' : ''} value="name">Name</option>--%>
                    <option ${'likeCount' == param.orderBy ? 'selected' : ''} value="likeCount"><spring:message code="viewLists.likes"/></option>
                    <option ${('moovieListId' == param.orderBy || param.orderBy == null)? 'selected' : ''} value="moovieListId"><spring:message code="viewLists.recent"/></option>
                </select>
<%--                PARA TENER EN CUENTA --> MIRAR EL DEFAULT sort y orderBy en el controller para settear los valores iniciales de las labels/iconos--%>
                <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='asc'? 'asc':'desc'}">
                <div class="btn btn-style me-1" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                    <i id="sortIcon" class="bi bi-arrow-${param.order == 'asc' ? 'up' : 'down'}-circle-fill"></i>
                </div>
                <button type="submit" id="applyButton" class="btn btn-style2"><spring:message code="viewLists.apply"/></button>
            </form>
        </div>



    </div>

    <div class="lists-container" style="margin-top: 30px">
        <c:if test="${showLists.size()==0}">
                <h3><spring:message code="viewLists.noResults"/></h3>
            </c:if>
        <c:forEach var="cardList" items="${showLists}">
            <%@include file="../common/listCard.jsp"%>
            </c:forEach>
    </div>
</div>
</body>
<div class="m-1">
    <c:import url="/WEB-INF/jsp/common/pagination.jsp">
        <c:param name="mediaPages" value="${numberOfPages}"/>
        <c:param name="currentPage" value="${currentPage + 1}"/>
        <c:param name="url" value="${urlBase}"/>
    </c:import>
</div>
</html>

<style>
    .card-name-likes {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
    }

    .card-likes {
        text-align: right;
        margin-right: 10px;
        min-width: 50px;
    }

</style>

<script>
    const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]')
    const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl))

</script>