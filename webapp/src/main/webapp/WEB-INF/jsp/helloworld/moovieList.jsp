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
    <h1>${moovieList.name} by ${listOwner}</h1>
    <h2 style="font-size: large">${moovieList.description}</h2>
    <table class="table table-striped">
        <thead>
        <tr class="d-flex">
            <th scope="col" class="col-1">#</th>
            <th scope="col" class="col-2">Poster</th>
            <th scope="col" class="col-3">Type</th>
            <th scope="col" class="col-4">Title</th>
            <th scope="col" class="col-2">Score</th>
        </tr>
        </thead>

        <c:if test="${moovieListContent.size()}!=0">
            <tbody>
            <c:forEach var="index" begin="0" end="${moovieListContent.size()-1}" step="1">
                <tr class="d-flex">
                    <th scope="row" class="col-1"> ${index + 1} </th>
                    <td class="col-2" ><a href="${pageContext.request.contextPath}/details/${mediaList.get(index).mediaId}"><img src="${mediaList.get(index).posterPath}" class="img-fluid" width="100" height="100"></a></td>
                    <td class="col-3"> <c:if test="${mediaList.get(index).type == true}"><p>TvSerie</p></c:if><c:if test="${mediaList.get(index).type != true}"><p>Movie</p></c:if></td>
                    <td class="col-4"><a href="${pageContext.request.contextPath}/details/${mediaList.get(index).mediaId}">${mediaList.get(index).name}</a><br><p style="font-size: smaller; font-style: italic"> ${mediaList.get(index).releaseDate}</p></td>
                    <td class="col-2">${mediaList.get(index).tmdbRating}<i class="bi bi-star-fill"></i></td>
                </tr>
            </c:forEach>
            </tbody>
        </c:if>
        <c:if test="${moovieListContent.size()}==0">
            List is empty
        </c:if>
    </table>

</div>
</body>
</html>
