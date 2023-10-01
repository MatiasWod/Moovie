<%@    taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@    taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <title>Moovie - Login!</title>
</head>
<body style="background:whitesmoke">


<c:url value="/login" var="loginUrl"/>
<c:url value="/register" var="registerUrl"/>
<c:url value="/" var="homeUrl"/>

<div style="border:solid black;width: 25%; height: 60%; position: absolute; left: 37.5%; padding: 5%; margin-top: 5%" class="container-gray align-items-center justify-content-center d-flex flex-column">

    <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
        <h1>Login</h1>
        <div class="alert alert-danger" id="errorAlert" style="display: none;">
            <c:if test="${param.error == 'locked'}">Account was banned</c:if>
            <c:if test="${param.error == 'disabled'}">Email verification pending</c:if>
        </div>
        <div class="alignt-items-left text-left">
            <div style="margin: 5px; width: 35%">
                <label for="username">Username: </label>
                <input id="username" name="username" type="text"/></div>
            <div style="margin: 5px; width: 35%">
                <label for="password">Password: </label>
                <input id="password" name="password" type="password"/>
            </div>
            <div>
                <label class="m-1">
                    <input name="rememberme" type="checkbox"/> Remember Me
                </label>
            </div>
        </div>

        <div style="margin: 5px; width: 35%">
            <input class="btn btn-outline-success align-bottom" type="submit" value="Login!"/>
        </div>
        <div style="margin-top: 20px; margin-bottom: 2px">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register"> Sign Up!</a>

        </div>
        <div>
            Continue
            <a href="${homeUrl}"> without logging in</a>
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