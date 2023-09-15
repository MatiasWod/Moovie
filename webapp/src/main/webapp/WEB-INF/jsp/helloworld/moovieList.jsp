<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=58" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">

    <title>Moovie List</title>
</head>
<body style="background: whitesmoke">
<div class="container d-flex flex-column">
    <c:import url="navBar.jsp"/>
    <table class="table table-striped">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Poster</th>
            <th scope="col">Type</th>
            <th scope="col">Title</th>
            <th scope="col">Score</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="index" begin="0" end="${moovieListContent.size()-1}" step="1">
            <tr>
                <th scope="row"> ${index + 1} </th>
                <td><img src="${mediaList.get(index).posterPath}" class="img-fluid" width="100" height="100"></td>
                <td><c:if test="${mediaList.get(index).type == true}"><p>TvSerie</p></c:if><c:if test="${mediaList.get(index).type != true}"><p>Movie</p></c:if></td>
                <td>${mediaList.get(index).name}<br><p style="font-size: smaller; font-style: italic"> ${mediaList.get(index).releaseDate}</p></td>
                <td>${mediaList.get(index).tmdbRating}<i class="bi bi-star-fill"></i></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</div>
</body>
</html>
