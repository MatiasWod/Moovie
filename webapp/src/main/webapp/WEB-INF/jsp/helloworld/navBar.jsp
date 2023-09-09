<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/9/2023
  Time: 2:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-lg navbar-light bg-transparent fixed-top">
    <div class="container d-flex justify-content-between align-items-center flexSpaceBetween">
    <a class="navbar-brand">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo" height="30">
            Moovie
        </a>
        <div class="navbar-nav">
            <a class="nav-link" href="#">Sign In</a>
            <a class="nav-link" href="#">Create Account</a>
            <a class="nav-link" href="#">Discover</a>
        </div>
        <form class="form-inline">
            <input class="form-control" type="search" placeholder="Search" aria-label="Search">
        </form>
    </div>
</nav>