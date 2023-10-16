<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Error 400</title>

</head>

<body style="background: whitesmoke">

<c:import url="navBar.jsp"/>
<div class="container d-flex flex-column">

    <div class="col"><img src="${pageContext.request.contextPath}/resources/logo.png"  alt="Moovie logo" height="100px" width="100px"/> </div>

    <div class="col">
        <h1>500 Internal Server Error</h1>
        <p>Oops! Something went wrong on our end. We're working to fix the issue. Please try again later.</p>
        <c:if test="${extraInfo != null}">${extraInfo}</c:if>
        <button type="button" onclick="history.back()" class="btn btn-outline-success" id="goBackButton">Go Back</button>
    </div>


</div>
</html>
