<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <title>Moovie</title>
</head>
<body class="bg-black">

<div class="background carousel slide carousel-fade" data-bs-ride="carousel">
    <div class="carousel-inner">
        <div class="carousel-item active">
            <img src="https://a.ltrbxd.com/resized/sm/upload/az/63/do/1q/bottoms-1200-1200-675-675-crop-000000.jpg?v=2227c4e937" class="background-image image-blur" alt="...">
        </div>
        <div class="carousel-item">
            <img src="https://image.tmdb.org/t/p/original/35z8hWuzfFUZQaYog8E9LsXW3iI.jpg" class="background-image image-blur" alt="...">
        </div>
        <div class="carousel-item">
            <img src="https://image.tmdb.org/t/p/original/jZIYaISP3GBSrVOPfrp98AMa8Ng.jpg" class="background-image image-blur" alt="...">
        </div>
    </div>
</div>


<div class="d-flex container flex-column justify-content-center height-full">
    <c:import url="navBar.jsp"/>
    <%--Main Body aca CONTENT--%>
    <div class="d-flex container container-gray-transp justify-content-around flex-column align-items-center">
        <p class="title-lg">Welcome to Moovie!</p>
        <div class="d-flex align-items-around flex-row mb-2 justify-content-center">
            <div class="poster card text-bg-dark">
                <div class="card-img-container"> <!-- Add a container for the image -->
                    <img src="https://image.tmdb.org/t/p/original/xnFFz3etm1vftF0ns8RMHA8XdqT.jpg" class="card-img" alt="...">
                    <div class="card-img-overlay">
                        <h5 class="card-title">Card title</h5>
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                        <p class="card-text"><small>Last updated 3 mins ago</small></p>
                    </div>
                </div>
            </div>
            <div class="poster card text-bg-dark">
                <div class="card-img-container"> <!-- Add a container for the image -->
                    <img src="https://image.tmdb.org/t/p/original/ywBt4WKADdMVgxTR1rS2uFwMYTH.jpg" class="card-img" alt="...">
                    <div class="card-img-overlay">
                        <h5 class="card-title">Card title</h5>
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                        <p class="card-text"><small>Last updated 3 mins ago</small></p>
                    </div>
                </div>
            </div>
            <div class="poster card text-bg-dark">
                <div class="card-img-container"> <!-- Add a container for the image -->
                    <img src="https://image.tmdb.org/t/p/original/gGC7zSDgG0FY0MbM1pjfhTCWQBI.jpg" class="card-img" alt="...">
                    <div class="card-img-overlay">
                        <h5 class="card-title">Card title</h5>
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                        <p class="card-text"><small>Last updated 3 mins ago</small></p>
                    </div>
                </div>
            </div>
            <div class="poster card text-bg-dark">
                <div class="card-img-container"> <!-- Add a container for the image -->
                    <img src="https://image.tmdb.org/t/p/original/ywBt4WKADdMVgxTR1rS2uFwMYTH.jpg" class="card-img" alt="...">
                    <div class="card-img-overlay">
                        <h5 class="card-title">Card title</h5>
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                        <p class="card-text"><small>Last updated 3 mins ago</small></p>
                    </div>
                </div>
            </div>
            <div class="poster card text-bg-dark">
                <div class="card-img-container"> <!-- Add a container for the image -->
                    <img src="https://image.tmdb.org/t/p/original/gGC7zSDgG0FY0MbM1pjfhTCWQBI.jpg" class="card-img" alt="...">
                    <div class="card-img-overlay">
                        <h5 class="card-title">Card title</h5>
                        <p class="card-text">This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p>
                        <p class="card-text"><small>Last updated 3 mins ago</small></p>
                    </div>
                </div>
            </div>
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
