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


<h2>Prueba de MoovieListContent:</h2>
${mlc}
<c:forEach items="${mlc}" var="cont">
    <div>
        algo:
        ${cont.name}
        ${cont.customOrder}
        ${cont.watched}
    </div>
</c:forEach>
</body>
</html>
