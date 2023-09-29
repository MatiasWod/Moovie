<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <title>Moovie - Sign Up!</title>
</head>
<body style="background: linear-gradient(to bottom, lightskyblue, darkseagreen)">
<div style="position: absolute;bottom: 0;height: 250%;width: 100%;overflow: hidden" class="d-flex">
    <img class="grassImage" src="${pageContext.request.contextPath}/resources/grassLand.png"/>
</div>
<div style="position: absolute;bottom: 0;height: 250%;width: 100%;overflow: hidden" class="d-flex">
    <img class="grassImage" style="animation-direction: reverse" src="${pageContext.request.contextPath}/resources/grassLand.png"/>
</div>
<div style="position: absolute;bottom: 0;height: 250%;width: 100%;overflow: hidden" class="d-flex">
    <img class="grassImageAlt" style="left: -20%" src="${pageContext.request.contextPath}/resources/grassLand.png"/>
</div>
<div style="position: absolute;bottom: 0;height: 250%;width: 100%;overflow: hidden" class="d-flex">
    <img class="grassImageAlt" style="right: -20%" src="${pageContext.request.contextPath}/resources/grassLand.png"/>
</div>
<div style="border:solid black;width: 25%; height: 50%; position: absolute; left: 37.5%; padding: 5%; margin-top: 5%" class="container-gray align-items-center justify-content-center d-flex flex-column">
    <div style="align-content: center; align-items: center">
        <form:form modelAttribute="registerForm" action="${pageContext.request.contextPath}/register" method="post" class="">
            <h1>Sign Up</h1>
            <div class="">
                <div style="margin: 5px; width: 40%">
                    <form:label path="username">Username: </form:label>
                    <form:input type="text" path="username"/>
                    <form:errors path="username" cssClass="formError" element="p"/>
                </div>
                <div style="margin: 5px; width: 40%">
                    <form:label path="email">Email: </form:label>
                    <form:input type="email" path="email"/>
                    <form:errors path="email" cssClass="formError" element="p"/>
                </div>
                <div style="margin: 5px; width: 40%">
                    <form:label path="password">Password: </form:label>
                    <form:input type="password" path="password" />
                    <form:errors path="password" cssClass="formError" element="p"/>
                </div>
                <div style="margin: 5px; width: 40%">
                    <form:label path="repeatPassword">Repeat password: </form:label>
                    <form:input type="password" path="repeatPassword"/>
                    <form:errors path="repeatPassword" cssClass="formError" element="p"/>
                </div>
                <div style="margin: 5px; width: 35%">
                    <input class="btn btn-outline-success align-bottom" type="submit" value="Register!"/>

                </div>
            </div>
        </form:form>
    </div>

</div>
</body>
</html>