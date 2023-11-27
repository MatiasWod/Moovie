<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title><spring:message code="login.title"/></title>
</head>
<body style="background: whitesmoke; overflow: hidden">
<c:url value="/login" var="loginUrl"/>
<c:url value="/register" var="registerUrl"/>
<c:url value="/" var="homeUrl"/>
<c:import url="../common/navBar.jsp"/>
<div style="border:solid black;width: fit-content; min-height: 60%; padding: 5%; margin-top: 5%" class="container container-gray align-items-center justify-content-center d-flex flex-column">

    <c:import url="../main/backgroundPosters.jsp"/>

    <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded" >
        <h1>Login</h1>
        <div class="alert alert-danger" id="errorAlert" style="display: none;">
            <c:if test="${param.error == 'locked'}"><spring:message code="login.banned"/></c:if>
            <c:if test="${param.error == 'disabled'}"><spring:message code="login.emailVerificationPending"/></c:if>
            <c:if test="${param.error == 'bad_credentials'}"><spring:message code="login.passwordDoesNotMatchUsername"/></c:if>
            <c:if test="${param.error == 'unknown_user'}"><spring:message code="login.usernameNotVerified"/></c:if>
            <c:if test="${param.error == 'unknown_error'}"><spring:message code="login.loginFailed"/></c:if>
        </div>
        <div class="alignt-items-left text-left">
            <div style="margin: 5px; width: 35%">
                <label for="username"><spring:message code="login.username"/></label>
                <input required id="username" name="username" type="text"/></div>
            <div style="margin: 5px; width: 35%">
                <label for="password"><spring:message code="login.password"/></label>
                <input required id="password" name="password" type="password"/>
            </div>
            <div>
                <label class="m-1">
                    <input name="rememberme" type="checkbox"/> <spring:message code="login.rememberMe"/>
                </label>
            </div>
        </div>

        <div style="margin: 5px; width: 35%">
            <input class="btn btn-outline-success align-bottom" type="submit" value="<spring:message code="login.login"/>"/>
        </div>
        <div style="margin-top: 20px; margin-bottom: 2px">
            <spring:message code="login.noAccount"/>
            <a href="${pageContext.request.contextPath}/register"><spring:message code="login.signUp"/></a>

        </div>
        <div>
            <spring:message code="login.continue"/>
            <a href="#" onclick="history.back()"><spring:message code="login.without"/></a>
        </div>
    </form>

</div>
<script>
    // Get the error message from the alert div
    var errorAlert = document.getElementById("errorAlert");

    // Check if the error message is not empty
    if (errorAlert.textContent.trim() !== "") {
        // Show the error alert
        errorAlert.style.display = "block";
    }
</script>
</body>
</html>