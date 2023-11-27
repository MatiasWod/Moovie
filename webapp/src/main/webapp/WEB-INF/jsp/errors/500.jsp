<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title><spring:message code="error500.message"/></title>

</head>

<body style="background: whitesmoke">

<c:import url="../common/navBar.jsp"/>
<div class="container d-flex flex-column">

    <div class="col"><img src="${pageContext.request.contextPath}/resources/logo.png" alt="Moovie logo" height="100px" width="100px"/></div>

    <div class="col">
        <h1><spring:message code="error500.message"/></h1>
        <p><spring:message code="error500.description"/></p>
        <c:if test="${extraInfo != null}">${extraInfo}</c:if>
        <button type="button" onclick="history.back()" class="btn btn-outline-success" id="goBackButton"><spring:message code="error500.call_to_action"/></button>
    </div>


</div>
</html>
