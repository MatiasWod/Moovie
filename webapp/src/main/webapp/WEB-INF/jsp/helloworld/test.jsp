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
    <br>
    <h4>Recently Added Films</h4>
    <div class="container-fluid">
        <div class="row flex-row flex-nowrap overflow-auto">
            <c:forEach var="movie" items="${movieList}">
                <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                    <jsp:include page="/WEB-INF/jsp/helloworld/card.jsp">
                        <jsp:param name="posterPath" value="${movie.posterPath}"/>
                        <jsp:param name="movieName" value="${movie.movieName}"/>
                        <jsp:param name="releaseDate" value="${movie.releaseDate}"/>
                        <jsp:param name="movieId" value="${movie.movieId}"/>
                    </jsp:include>
                </div>
            </c:forEach>
        </div>
    </div>
    <br>
</div>
<%--<script>--%>
<%--    $(function () {--%>
<%--        $('[data-toggle="tooltip"]').tooltip();--%>
<%--    });--%>
<%--</script>--%>
</body>
</html>