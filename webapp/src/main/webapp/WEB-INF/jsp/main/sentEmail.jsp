<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: usuario
  Date: 27/9/2023
  Time: 17:17
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <title><spring:message code="sentEmail.title"/></title>
</head>
<body>
<c:import url="../common/navBar.jsp"/>
<div style="border: solid black; min-width: 40%; min-height: 50%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); padding: 5%;" class="container-gray justify-content-center d-flex flex-column">
    <div class="text-center m-3">
        <img style="height: 15vh" src="${pageContext.request.contextPath}/resources/logo.png" alt="logo">
        <h1><spring:message code="sentEmail.verify"/></h1>
    </div>
    <c:if test="${not empty param.message}">
        <div class="alert alert-success alert-dismissible fade show m-2" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div><c:out value="${param.message}"/></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="<spring:message code="sentEmail.close"/>"></button>
            </div>
        </div>
    </c:if>
    <h5 class="text-center m-2"><spring:message code="sentEmail.verificationEmailSent"/></h5>
    <form action="${pageContext.request.contextPath}/register/resendEmail" method="post" class="text-center">
        <input type="hidden" name="token" value="${param.token}">
        <button type="submit" class="btn btn-lg btn-outline-success mt-4"><spring:message code="sentEmail.resendVerificationEmail"/></button>
    </form>
</div>
</body>
</html>
