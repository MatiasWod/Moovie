<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const currentPath = window.location.pathname;
        const links = document.querySelectorAll(".nav-item-link");

        links.forEach(link => {
            const href = link.getAttribute("href");
            if (currentPath.includes(href)) {
                link.classList.add("active");
            }
        });
    });
</script>

<nav class="sticky-top navbar navbar-expand-lg navbar-light container-gray mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/resources/logo.png" height="30" alt="Moovie logo">
            Moovie
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="text-center navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/discover">Discover</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/lists">Browse lists</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/createList">Create a list</a>
                </li>
            </ul>
            <form class="d-flex mb-0" role="search" action="${pageContext.request.contextPath}/search" method="get">
                <input class="form-control me-2" type="search" name="query" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success" type="submit">Search</button>
            </form>
        </div>
    </div>
</nav>

<style>
    .navbar-nav > .nav-item > .nav-link.active {
        color: blue !important;
    }
</style>
