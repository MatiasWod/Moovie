<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet"/>
    <title>Moovie</title>
</head>
<body id="grad">
<div class="container">
    <!-- Titulo y Logo -->
    <div class="row align-items-center justify-content-center">
        <div class="col-2">
            <img src="https://static.vecteezy.com/system/resources/previews/000/620/624/original/vector-sun-generic-logo-and-symbols.jpg" alt="Logo" class="img-fluid">
        </div>
        <div class="col">
            <h1>Moovie</h1>
        </div>
    </div>

    <!-- Welcome y description -->
    <div class="row mb-5 justify-content-center">
        <div class="col">
            <h2>Welcome</h2>
            <p>Your one stop solution for all your entertainment queries</p>
        </div>
    </div>

    <!-- Posters -->
    <div class="row mb-5 justify-content-center">
        <c:forEach var="i" begin="1" end="5">
            <div class="col">
                <img src="https://rukminim2.flixcart.com/image/850/1000/jf8khow0/poster/a/u/h/small-hollywood-movie-poster-blade-runner-2049-ridley-scott-original-imaf3qvx88xenydd.jpeg?q=20" class="img-fluid poster">
            </div>
        </c:forEach>
    </div>

    <!-- LogIn -->
    <div class="row mb-3 justify-content-center">
        <div class="col">
            <a href="register.jsp" class="btn btn-primary">Log In</a>
        </div>
    </div>

    <!-- Browse -->
    <div class="row justify-content-center">
        <div class="col">
            <a href="index.jsp" class="btn btn-link btn-sm">Continue without logging in</a>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
</body>
</html>
