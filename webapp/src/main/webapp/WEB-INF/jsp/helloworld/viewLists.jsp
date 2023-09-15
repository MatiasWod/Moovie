<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 9/14/2023
  Time: 7:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=58" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Discover your next passion</title>
</head>
<body style="background: whitesmoke">

<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>

    <div class="container d-flex flex-column ">
        <h1>
            Browse Media Lists
        </h1>
        <h2>
            Editorial Picks by Moovie
        </h2>
        <h4>
            Top 100 rated Movies
        </h4>
        <h4>
            Top 100 rated Series
        </h4>
        <h4>
            The longest Movies (only for enthusiasts!)
        </h4>
        <h4>
            The shortest Movies (all business, no waste)
        </h4>
        <h2>
            Community lists
        </h2>
        <c:forEach var="list" items="${moovieLists}">
            <h4>${list} <-- aca hay una lista</h4>
        </c:forEach>
    </div>
</div>

</body>
</html>
