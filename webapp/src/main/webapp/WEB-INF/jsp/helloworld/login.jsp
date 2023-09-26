<%@    taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@    taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>
<c:url value="/login" var="loginUrl"/>
<c:url value="/register" var="registerUrl"/>
<c:url value="/" var="homeUrl"/>
<form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
    <div>
        <label for="username">Username: </label>
        <input id="username" name="username" type="text"/></div>
    <div><label for="password">Password: </label> <input id="password" name="password" type="password"/></div>
    <div><label><input name="rememberme" type="checkbox"/>Remember Me</label></div>
    <div><input type="submit" value="Login!"/></div>
</form>
<a class="m-4 btn btn-outline-success align-bottom" href="${registerUrl}">Register</a>
<a class="m-4 btn btn-outline-success align-bottom" href="${homeUrl}">Continue as guest</a>

</body>
</html>