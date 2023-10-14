<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 10/14/2023
  Time: 6:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="${pageContext.request.contextPath}/resources/main.css?version=82" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">

    <title>Search</title>
</head>
<body>
<c:import url="navBar.jsp"/>

<div class="container d-flex flex-column">
    <%--TODO: test="${nameMedia}"--%>
    <c:if test="true">
        <div>
            Aqui va la nameMedia
        </div>

    </c:if>

    <%--TODO: test="${creditMedia}"--%>
    <c:if test="true">
        <div>
            Aqui va la creditMedia
        </div>

    </c:if>

    <%--TODO: test="${userList}"--%>
    <c:if test="true">
        <div>
            Aqui van los users
        </div>

    </c:if>
</div>


</body>
</html>
