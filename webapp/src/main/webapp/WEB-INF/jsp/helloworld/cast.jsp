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
<c:forEach var="review" items="${cast}">
<a>${review.name}</a>
</c:forEach>

