<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const currentPath = window.location.pathname;
        const links = document.querySelectorAll(".nav-item-link");

        const storedSearchValue = localStorage.getItem("searchValue");
        const searchInput = document.getElementById("searchInput");
        if (storedSearchValue) {
            searchInput.value = storedSearchValue;
        }
        searchInput.addEventListener("input", function() {
            localStorage.setItem("searchValue", searchInput.value);
        });

        const profileImage = document.getElementById("profile-image");
        if (profileImage) {
            profileImage.onerror = function() {
                profileImage.src = "${pageContext.request.contextPath}/resources/defaultProfile.jpg";
            }
        }

        links.forEach(link => {
            const href = link.getAttribute("href");
            if (currentPath.includes(href)) {
                link.classList.add("active");
            }
        });

        const profileImages = document.querySelectorAll(".review-profile-image");

        profileImages.forEach(profileImage => {

            profileImage.onerror = function() {

                profileImage.src = "${pageContext.request.contextPath}/resources/defaultProfile.jpg";

            }

        });
    });
</script>

    <nav class="sticky-top navbar navbar-expand-lg navbar-light container-gray mb-4">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/resources/logo.png" height="50" alt="Moovie logo">
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
                <div class="input-group">
                    <input id="searchInput" class="form-control me-2" type="search" name="query" placeholder="Search" aria-label="Search">
                    <button class="btn btn-outline-success" type="submit">
                        <i class="bi bi-search"></i> <!-- Bootstrap search icon -->
                        Search
                    </button>
                </div>
            </form>

                <div style="margin-left: 15px; margin-right:10px" class="d-flex nav-item justify-content-center">
                        <sec:authorize access="hasRole('ROLE_USER')">
                            <sec:authentication property="name" var="username"></sec:authentication>
                            <div class="collapse navbar-collapse" id="navbarNavDarkDropdown">
                                <ul class="navbar-nav">
                                    <li class="nav-item dropdown">
                                        <sec:authorize access="hasRole('ROLE_MODERATOR')"><img src="${pageContext.request.contextPath}/resources/moderator_logo.png" height="50" alt="Moderator logo"></sec:authorize>
                                        <button class="btn bg-transparent dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                            <img id="profile-image" style="height: 50px; width: 50px; border:solid black; border-radius: 50%" class="cropCenter" src="${pageContext.request.contextPath}/profile/image/${username}" alt="profile picture"/>
                                                ${username}
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile/${username}">Profile</a></li>
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </sec:authorize>
                        <sec:authorize access="!isAuthenticated()">
                            <a href="${pageContext.request.contextPath}/login">Log In</a>
                        </sec:authorize>
                </div>

            </div>
        </div>
    </nav>

<style>
    .navbar-nav > .nav-item > .nav-link.active {
        color: green !important;
    }
</style>
