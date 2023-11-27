<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
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

        const profileImages = document.querySelectorAll(".profile-image");
        profileImages.forEach(profileImage => {
            profileImage.onerror = function() {
                profileImage.src = "${pageContext.request.contextPath}/resources/defaultProfile.jpg";
            }
        });

        links.forEach(link => {
            const href = link.getAttribute("href");
            if (currentPath.includes(href)) {
                link.classList.add("active");
            }
        });

        function addHoverableDropdownStyle(dropdownId) {
            var element = document.getElementById(dropdownId);

            element.addEventListener("click", function() {
                if (this.getAttribute("aria-expanded") === "true") {
                    this.classList.add("expanded");
                } else {
                    this.classList.remove("expanded");
                }
            });

            document.addEventListener("click", function(event) {
                if (!element.contains(event.target)) {
                    element.classList.remove("expanded");
                    element.setAttribute("aria-expanded", "false");
                }
            });
        }

        addHoverableDropdownStyle("dropdownButtonRated");
        addHoverableDropdownStyle("dropdownButtonPopular");
        if (document.getElementById("dropdownButtonProfile")){
            addHoverableDropdownStyle("dropdownButtonProfile");
        }

        var lazyImages = document.querySelectorAll('.async-image');

        var observer = new IntersectionObserver(function(entries, observer) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    entry.target.src = entry.target.dataset.src;
                    observer.unobserve(entry.target);
                }
            });
        }, { rootMargin: '0px 0px 100px 0px' });

        lazyImages.forEach(function(image) {
            observer.observe(image);
        });

    });
</script>

    <nav style="z-index: 2;" class="sticky-top navbar navbar-expand-lg navbar-light container-nav mb-4">
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
                        <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/discover"><spring:message code="navBar.discover"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/lists"><spring:message code="navBar.browseLists"/></a>
                    </li>
                    <sec:authorize access="isAuthenticated()">
                        <li class="nav-item">
                            <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/createList"><spring:message code="navBar.createList"/></a>
                        </li>
                    </sec:authorize>
                    <li class="nav-item">
                        <div class="d-flex flex-row align-self-left">
                            <div class="collapse navbar-collapse" id="navBarTopRatedDropdown">
                                <ul class="navbar-nav">
                                    <li class="nav-item dropdown">
                                        <button id="dropdownButtonRated" class="btn bg-transparent customDropdownButton dropdown-toggle" style="color: rgba(0, 0, 0, 0.65);" data-bs-toggle="dropdown" aria-expanded="false">
                                            <spring:message code="navBar.topRated"/>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/featuredList/topRatedMedia"><spring:message code="navBar.media"/></a></li>
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/featuredList/topRatedMovies"><spring:message code="navBar.movies"/></a></li>
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/featuredList/topRatedSeries"><spring:message code="navBar.series"/></a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </li>
                    <li class="nav-item">
                        <div class="collapse navbar-collapse" id="navBarMostPopularDropdown">
                            <ul class="navbar-nav">
                                <li class="nav-item dropdown">
                                    <button id="dropdownButtonPopular" class="btn bg-transparent dropdown-toggle customDropdownButton" style="color: rgba(0, 0, 0, 0.65);" data-bs-toggle="dropdown" aria-expanded="false">
                                        <spring:message code="navBar.mostPopular"/>
                                    </button>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/featuredList/mostPopularMedia"><spring:message code="navBar.media"/></a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/featuredList/mostPopularMovies"><spring:message code="navBar.movies"/></a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/featuredList/mostPopularSeries"><spring:message code="navBar.series"/></a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link nav-item-link" aria-current="page" href="${pageContext.request.contextPath}/milkyLeaderboard"><spring:message code="navBar.leaderboard"/></a>
                    </li>
                </ul>




                <form class="d-flex mb-0" role="search" action="${pageContext.request.contextPath}/search" method="get">
                <div class="input-group">
                    <input id="searchInput" class="form-control me-2" type="search" name="query" placeholder="<spring:message code="navBar.search"/>" aria-label="Search" required>
                    <button class="btn btn-outline-success" type="submit">
                        <i class="bi bi-search"></i> <!-- Bootstrap search icon -->
                        <spring:message code="navBar.search"/>
                    </button>
                </div>
            </form>

                <div style="margin-left: 15px; margin-right:10px" class="d-flex nav-item justify-content-center">
                        <sec:authorize access="hasRole('ROLE_USER') || hasRole('ROLE_BANNED')">
                            <sec:authentication property="name" var="username"/>
                            <div class="collapse navbar-collapse" id="navbarNavDarkDropdown">
                                <ul class="navbar-nav">
                                    <li class="nav-item dropdown">
                                        <sec:authorize access="hasRole('ROLE_MODERATOR')"><img src="${pageContext.request.contextPath}/resources/moderator_logo.png" height="50" alt="Moderator logo"></sec:authorize>
                                        <button id="dropdownButtonProfile" class="btn bg-transparent dropdown-toggle customDropdownButton" style="color: rgba(0, 0, 0, 0.65);" data-bs-toggle="dropdown" aria-expanded="false">
                                            <c:choose>
                                                <c:when test="${currentUser.getHasPfp()}">
                                                    <img id="profile-image" style="height: 50px; width: 50px; border:solid black; border-radius: 50%" class="profile-image cropCenter" src="${pageContext.request.contextPath}/profile/image/${username}" alt="profile picture"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img id="profile-image" style="height: 50px; width: 50px; border:solid black; border-radius: 50%" class="profile-image cropCenter" src="${pageContext.request.contextPath}/resources/defaultProfile.jpg" alt="profile picture"/>
                                                </c:otherwise>
                                            </c:choose>
                                                <c:url value="${username}"/>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-end">
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile/${username}"><spring:message code="navBar.profile"/></a></li>
                                            <sec:authorize access="hasRole('ROLE_MODERATOR')">
                                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reports/review"><spring:message code="navBar.review"/></a></li>
                                            </sec:authorize>
                                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout"><spring:message code="navBar.logout"/></a></li>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </sec:authorize>
                        <sec:authorize access="!isAuthenticated()">
                            <a href="${pageContext.request.contextPath}/login"><spring:message code="navBar.login"/></a>
                        </sec:authorize>
                </div>

            </div>
        </div>
    </nav>

<style>
    .navbar-nav > .nav-item > .nav-link.active {
        color: green !important;
    }

    .customDropdownButton:hover {
        color: #000000 !important;
    }

    .customDropdownButton.expanded {
        color: green !important;
    }

</style>
