<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Error 403</title>
</head>
<body style="background: whitesmoke">
<f:view>
    <h:outputLabel value="Hello, world"/>
</f:view>

<c:import url="navBar.jsp"/>
<div class="container d-flex flex-column">

    <div class="col"><img src="${pageContext.request.contextPath}/resources/logo.png"  alt="Moovie logo" height="100px" width="100px"/> </div>

    <div class="col">
        <h1>Error 403</h1>
        <h2>Cant access this page (missing permissions) </h2>
        <c:if test="${extraInfo != null}">${extraInfo}</c:if>
    </div>


</div>
</html>
