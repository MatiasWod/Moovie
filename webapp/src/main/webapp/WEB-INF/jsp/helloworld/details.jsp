<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/main.css" rel="stylesheet"/>
    <title>Moovie Blade Runner</title>
</head>
<body id="grad">
<c:import url="navBar.jsp"/>

<div class="container my-1">
    <div class="row align-items-center justify-content-center">
        <!-- Poster -->
        <div class="col text-center">
            <img src="https://rukminim2.flixcart.com/image/850/1000/jf8khow0/poster/a/u/h/small-hollywood-movie-poster-blade-runner-2049-ridley-scott-original-imaf3qvx88xenydd.jpeg?q=20"
                 alt="Blade Runner Poster" class="img-fluid" width="300" height="300">
        </div>
        <div class="col">
            <!-- Title and Details -->
            <h1>Blade Runner 2049 </h1>
            <h5 style="display: flex; align-items: center">2017 2h 43m</h5>

            <!-- Ratings -->
            <h1>
                <i class="bi bi-star-fill"></i>
                7.6
            </h1>
            <!-- Genres -->
            <div class="d-flex flex-row  align-items-center ">
                <div style="margin-right: 10px">
                    <h5>Genres:</h5>
                </div>
                <div>
                    <span class="badge text-bg-dark">Action</span>
                    <span class="badge text-bg-dark">Drama</span>
                </div>

            </div>
            <!-- Director -->
            <div class="d-flex flex-row  align-items-center">
                <div style="margin-right: 10px">
                    <h5>Director:</h5>
                </div>
                <div>
                    <span class="badge text-bg-light border border-black">Denis Villeneuve</span>
                </div>
            </div>
            <!-- Description -->
            <p>
                Rick Deckard (Harrison Ford) es un blade runner, un agente de policia destinado al retiro de
                replicantes ilegales.
                Su mision es dar caza a un grupo de cuatro de estos androides, sofisticados NEXUS 6 superiores
                en
                fuerza e inteligencia a los humanos,
                pero diseñados para vivir una corta existencia de cuatro años.
            </p>

            <button type="button" class="btn btn-dark"><i class="bi bi-plus-circle-fill"></i> Add to list
            </button>
            <button type="button" class="btn btn-light border border-black"><i class="bi bi-star-fill"></i> Rate
            </button>
        </div>
        <!-- Cast -->
        <div class="row ">
            <h2>Cast</h2>
            <hr class="my-8">
            <div class="d-flex flex-row  align-items-center">
                <div class="p-1">
                    <div class="card" style="width: fit-content;">
                        <div class="row">
                            <div class="col">
                                <img
                                        src="https://www.lecturas.com/medio/2020/09/30/ryan-gosling_d611a72b_800x800.jpg"
                                        alt="Ryan Gosling" width="150px" height="150px"></div>
                            <div class="col">
                                <div class="card-body">
                                    <h5 class="card-title">Ryan Gosling</h5>
                                    <p class="card-text">Messi</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="p-1">
                    <div class="card" style="width: fit-content;">
                        <div class="row">
                            <div class="col"><img src="https://pics.filmaffinity.com/044718214663547-nm_200.jpg"
                                                  alt="Harrison Ford" width="150px" height="150px">
                            </div>
                            <div class="col">
                                <div class="card-body">
                                    <h5 class="card-title">Harrison Ford</h5>
                                    <p class="card-text">Kun</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="p-1">
                    <div class="card" style="width: fit-content;">
                        <div class="row">
                            <div class="col"><img
                                    src="https://hips.hearstapps.com/hmg-prod/images/ana-de-armas-attends-the-20th-annual-afi-awards-at-four-news-photo-1614785457.?crop=1.00xw:0.922xh;0,0.0120xh&resize=1200:*"
                                    alt="Ana de Armas" width="150px" height="150px"></div>
                            <div class="col">
                                <div class="card-body">
                                    <h5 class="card-title">Ana de Armas</h5>
                                    <p class="card-text">Antonela</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="p-2 align-items-bottom">
                    <button type="button" class="btn btn-dark">See more</button>
                </div>
            </div>
        </div>


        <!-- Reviews -->
        <h2>Reviews</h2>
        <hr class="my-8">
        <div class="scrollableDiv">
            <c:forEach begin="1" end="10" step="1">
                <div class="card mb-3">
                    <div class="card-body">
                        <div class="d-flex align-items-center justify-content-between">
                            <div class="d-flex align-items-center">
                                <img src="https://m.media-amazon.com/images/M/MV5BNjE3NDQyOTYyMV5BMl5BanBnXkFtZTcwODcyODU2Mw@@._V1_FMjpg_UX1000_.jpg"
                                     alt="Reviewer Profile" class="mr-3 rounded-circle" width="64" height="64">
                                <div class="mt-0" style="margin-left: 15px">
                                    <h5>Reviewer Username</h5>
                                    <div class="text-body-secondary">
                                        24 reviews
                                    </div>
                                </div>
                            </div>
                            <h5 class="align-items-left"><i class="bi bi-star-fill ml-2"></i> 8/10</h5>
                        </div>

                        <p>
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed id tincidunt libero, sed
                            placerat
                            dolor. Fusce vehicula turpis vitae odio facilisis, ut euismod orci varius. Nulla
                            facilisi.
                            Curabitur vel semper odio.
                        </p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
