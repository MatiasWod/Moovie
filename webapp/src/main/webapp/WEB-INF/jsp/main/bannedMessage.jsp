<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=59" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title><spring:message code="bannedMessage.title"/></title>
</head>
<body style="background: whitesmoke">

<c:import url="../common/navBar.jsp"/>

<div style="border: solid black; min-width: 40%; min-height: 50%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); padding: 5%;" class="container-gray justify-content-center d-flex flex-column">
    <div class="text-center m-3">
        <img style="height: 15vh" src="${pageContext.request.contextPath}/resources/logo.png" alt="logo">
        <h1><spring:message code="bannedMessage.message"/></h1>
    </div>
    <div class="text-center">
        <c:choose>
            <c:when test="${bannedMessageObject != null}">
                <h2><spring:message code="bannedMessage.bannedBy" arguments="${bannedMessageObject.modUsername}"/></h2>
                <h3><spring:message code="bannedMessage.banReason" arguments="${bannedMessageObject.message}"/></h3>
            </c:when>
            <c:otherwise>
                <h3><spring:message code="bannedMessage.banReasonUnavailable"/></h3>
            </c:otherwise>
        </c:choose>
    </div>

    <button onclick="history.back()" class="btn btn-lg btn-outline-success mt-4"><spring:message code="bannedMessage.call_to_action"/></button>
</div>

</html>
