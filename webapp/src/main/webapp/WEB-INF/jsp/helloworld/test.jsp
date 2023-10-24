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
    <div class="container-fluid">
        <div class="row flex-row flex-nowrap overflow-auto">
            <div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-3">
                <a href="${pageContext.request.contextPath}/details/${media.mediaId}" class="poster card text-bg-dark m-1">
                    <div class="card-img-container"> <!-- Add a container for the image -->
                        <img class="cropCenter" src="${media.posterPath}" alt="media poster">
                        <div class="card-img-overlay">
                            <h6 class="card-title text-center">${media.name}</h6>
                            <div class="d-flex justify-content-evenly">
                                <p class="card-text">
                                    <i class="bi bi-star-fill"></i>
                                    ${media.tmdbRating}
                                </p>
                                <p class="card-text">
                                    <fmt:formatDate value="${media.releaseDate}" pattern="YYYY"/>
                                </p>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>