<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/css/main.css?version=55" rel="stylesheet"/>
    <title>Moovie</title>
</head>
<body>

<div class="background carousel slide carousel-fade" data-bs-ride="carousel">
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="https://a.ltrbxd.com/resized/sm/upload/az/63/do/1q/bottoms-1200-1200-675-675-crop-000000.jpg?v=2227c4e937" class="background-image" alt="...">
        </div>
        <div class="carousel-item">
            <img src="https://image.tmdb.org/t/p/original/35z8hWuzfFUZQaYog8E9LsXW3iI.jpg" class="background-image" alt="...">
        </div>
        <div class="carousel-item">
            <img src="https://image.tmdb.org/t/p/original/jZIYaISP3GBSrVOPfrp98AMa8Ng.jpg" class="background-image" alt="...">
        </div>
    </div>
</div>


<div class="d-flex container flex-column justify-content-center height-full">
    <nav class="navbar navbar-expand-lg navbar-light container-gray mb-4">
        <div class="container">
            <a class="navbar-brand">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo" height="30">
                Moovie
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="container d-flex flex-row-reverse collapse navbar-collapse" id="navbarSupportedContent">
                <form class="d-flex me-2" role="search">
                    <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                    <button class="btn btn-outline-success" type="submit">Search</button>
                </form>
                <ul class="navbar-nav">
                    <li class="nav-item active margin-horizontal">
                        <a>Discover</a>
                    </li>
                    <li class="nav-item active margin-horizontal">
                        <a>Discover</a>
                    </li>
                    <li class="nav-item active margin-horizontal">
                        <a>Discover</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <%--Main Body aca CONTENT--%>
    <div class="d-flex container container-gray-transp justify-content-around flex-column align-items-center">
        <p class="title-lg">Welcome to Moovie!</p>
        <div class="d-flex align-items-around flex-row mb-2 justify-content-center">
            <img class="poster" src="https://image.tmdb.org/t/p/original/xnFFz3etm1vftF0ns8RMHA8XdqT.jpg">
            <img class="poster" src="https://image.tmdb.org/t/p/original/ywBt4WKADdMVgxTR1rS2uFwMYTH.jpg">
            <img class="poster" src="https://image.tmdb.org/t/p/original/gGC7zSDgG0FY0MbM1pjfhTCWQBI.jpg">
            <img class="poster" src="https://image.tmdb.org/t/p/original/ywBt4WKADdMVgxTR1rS2uFwMYTH.jpg">
            <img class="poster" src="https://image.tmdb.org/t/p/original/gGC7zSDgG0FY0MbM1pjfhTCWQBI.jpg">
        </div>
        <div>
            <button class="btn btn-success button-fixed-width">Log In</button>
        </div>
        <a class="text-black mb-1" href="">continue as guest</a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js" integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>
</body>
</html>
