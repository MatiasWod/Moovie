<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: juana
  Date: 10/29/2023
  Time: 6:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>TEST</title>
</head>
<body>

<h2>Status list:</h2>
<c:forEach items="${statusList}" var="status">
    <div>${status}</div>
</c:forEach>

<h2>Series status list:</h2>
<c:forEach items="${seriesStatus}" var="status">
    <div>${status}</div>
</c:forEach>

<h2>Language list</h2>
<c:forEach items="${langList}" var="lang">
    <div>${lang}</div>
</c:forEach>

<h2>Series language list:</h2>
<c:forEach items="${seriesLangs}" var="status">
    <div>${status}</div>
</c:forEach>
</body>
</html>
