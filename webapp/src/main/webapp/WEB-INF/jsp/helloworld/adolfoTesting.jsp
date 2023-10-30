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
<div style="flex: 1">

</div>
<c:forEach items="${tvCreator}" var="creator">
    <div>
            ${creator.creatorName} -- ${creator.mediaId} -- ${creator.creatorId}
    </div>

</c:forEach>
<h1>All genres:</h1>
<c:forEach items="${allGenres}" var="genre">
    <div>
        ${genre}
    </div>
</c:forEach>
<h1>Now for mediaId = 1:</h1>
<c:forEach items="${genresForMedia}" var="genre">
    <div>
            ${genre}
    </div>
</c:forEach>
<h1>Providers:</h1>
<c:forEach items="${providers}" var="provider">
    <div>
        <img width="20" height="20" src="${provider.logoPath}" alt="logo">
        ${provider.providerId}
        ${provider.providerName}
    </div>
</c:forEach>
</body>
</html>
