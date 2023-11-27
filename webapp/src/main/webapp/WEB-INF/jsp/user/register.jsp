<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title><spring:message code="register.title"/></title>
</head>

<body style="background: whitesmoke; overflow: hidden">
    <c:import url="../common/navBar.jsp"/>
    <c:import url="../main/backgroundPosters.jsp"/>
    <div style="border: solid black; width: fit-content" class="container-gray container d-flex flex-column p-3 mt-5">
        <h1><spring:message code="register.signUp"/></h1>
        <div class="alert alert-danger" id="errorAlert" style="display: none;">
            <c:if test="${param.error == 'email_taken'}"><spring:message code="register.emailAlreadyRegistered"/></c:if>
            <c:if test="${param.error == 'username_taken'}"><spring:message code="register.usernameAlreadyRegistered"/></c:if>
        </div>
        <form:form modelAttribute="registerForm" action="${pageContext.request.contextPath}/register" method="post" class="">
            <div class="me-5 d-flex flex-column">
                <form:label path="username"><spring:message code="register.username"/></form:label>
                <div>
                    <form:input type="text" path="username"/>
                </div>
                <form:errors path="username" cssClass="error" element="p"/>

                <form:label path="email"><spring:message code="register.email"/></form:label>
                <div>
                    <form:input type="email" path="email"/>
                </div>
                <form:errors path="email" cssClass="error" element="p"/>

                <form:label path="password"><spring:message code="register.password"/></form:label>
                <div>
                    <form:input type="password" path="password" />
                </div>
                <form:errors path="password" cssClass="error" element="p"/>

                <form:label path="repeatPassword"><spring:message code="register.repeatPassword"/></form:label>
                <div>
                    <form:input type="password" path="repeatPassword"/>
                </div>
                <form:errors path="repeatPassword" cssClass="error" element="p"/>
                <form:errors path="passwordMatch" cssClass="error" element="p"/>
                <div >
                    <input class="mt-2 btn btn-outline-success" type="submit" value="<spring:message code="register.register"/>"/>
                </div>

                <div>
                    <spring:message code="register.hasAccount"/>
                    <a href="${pageContext.request.contextPath}/login"><spring:message code="register.login"/></a>
                </div>
                <div>
                    <spring:message code="register.continue"/>
                    <a href="#" onclick="history.back()"><spring:message code="register.without"/></a>
                </div>
            </div>
        </form:form>
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