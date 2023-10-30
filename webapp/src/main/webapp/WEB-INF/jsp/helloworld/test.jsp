<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>
    <title>Moovie</title>
</head>

<body>

<div class="col-8 offset-2">
    <h4>Recently Added TV Shows</h4>

    <a>${ml.name}</a>
    <a>${ml.userId}</a>
    <a>${ml.description}</a>

    <%-- c:if test="${not empty media}">
        <c:forEach var="med" items="${media}">
            <h1>${med.name}</h1>
            <c:if test="${not empty med.providers}">
            <c:forEach var="prov" items="${med.providers}">
                <h2>${prov.providerName}</h2>
            </c:forEach>
        </c:if>
            <c:if test="${not empty med.genres}">
                <c:forEach var="gen" items="${med.genres}">
                    <h2>${gen.genre}</h2>
                </c:forEach>
            </c:if>
        </c:forEach>

    </c:if >
    </div--%>

</body>
</html>