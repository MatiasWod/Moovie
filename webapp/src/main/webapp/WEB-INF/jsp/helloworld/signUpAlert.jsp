<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/29/2023
  Time: 3:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<div style="border: solid black; min-width: 40%; min-height: 50%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); padding: 5%;" class="container-gray justify-content-center d-flex flex-column">
    <div class="text-center m-3">
        <img style="height: 15vh" src="resources/logo.png" alt="logo">
        <h1>Welcome to Moovie!</h1>
    </div>
    <h5 class="text-center m-2">Join our community by signing up or logging in</h5>
    <a class="btn btn-success" href="${pageContext.request.contextPath}/register">Sign Up</a>
    <a class="btn btn-outline-success mt-2" href="${pageContext.request.contextPath}/login">Log In</a>
</div>

</html>