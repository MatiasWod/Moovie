<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <title>Moovie-${media.name}</title>
</head>
<body id="grad">
<c:import url="navBar.jsp">
    <c:param name="userName" value="${user.username}"/>
</c:import>

<h2>Username: <c:out value="${user.username}"/>!</h2>
<h2>Email: <c:out value="${user.email}"/>!</h2>
<h2> <c:if test="${isMe}">ITS YOU!</c:if></h2>
<img src="${pageContext.request.contextPath}/profile/image/${user.username}">

<h2>Here Insert a couple of reviews</h2>
<h2>Here insert a couple of lists</h2>

<form action="${pageContext.request.contextPath}/uploadProfilePicture" method="post" enctype="multipart/form-data">
    <input type="file" name="file" accept="image/*" />
    <input type="submit" value="Submit" />
</form>
</body>