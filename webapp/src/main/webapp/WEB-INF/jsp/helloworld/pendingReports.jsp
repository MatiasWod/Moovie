<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Review reports</title>

    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script async src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
    <script async src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script async>
        document.addEventListener("DOMContentLoaded", function() {
            const form = document.getElementById("selected-radio");
            const radios = document.querySelectorAll("input[name='btnradio']");
            const listField = document.getElementById("listField");

            radios.forEach((radio) => {
                radio.addEventListener("change", function() {
                    listField.value = radio.id.replace("btnradio-", "");
                    form.submit();
                });
            });
        });
    </script>
</head>
<body>
<c:import url="navBar.jsp"/>

<sec:authorize access="!hasRole('ROLE_MODERATOR')">
    <div class="container d-flex flex-column">
        <div class="d-flex justify-content-center">
            <h2><spring:message code="report.adminPage"/></h2>
        </div>
        <div class="d-flex container justify-content-center">
            <div class="m-2">
                <div class="d-flex align-items-center">
                    <div class="m-1 d-flex align-items-center">
                        <i class="bi bi-list-ul m-1"></i>
                        <h5>
                                <%--                        ${profile.moovieListCount}--%>
                            total reports: ${totalReports}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-star"></i>
                                <%--                        ${profile.reviewsCount}--%>
                            total banned: ${totalBanned}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
                                <%--                        ${profile.milkyPoints}--%>
                            total spam: ${spamReports}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-star"></i>
                                <%--                        ${profile.reviewsCount}--%>
                            total hate: ${hateReports}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-star"></i>
                                <%--                        ${profile.reviewsCount}--%>
                            total abuse: ${abuseReports}
                        </h5>
                    </div>
                    <div class="m-1 d-flex align-items-center">
                        <h5>
                            <i class="bi-star"></i>
                                <%--                        ${profile.reviewsCount}--%>
                            total abuse: ${privacyReports}
                        </h5>
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-comments" autocomplete="off" ${(param.list == null || param.list == '' || param.list == 'comments')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-comments"><spring:message code="report.comment"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-ml" autocomplete="off" ${(param.list != null && param.list == 'ml')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-ml"><spring:message code="report.list"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-mlReviews" autocomplete="off" ${(param.list != null && param.list == 'mlReviews')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-mlReviews"><spring:message code="report.listReview"/></label>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-reviews" autocomplete="off" ${(param.list != null && param.list == 'reviews')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-reviews"><spring:message code="report.mediaReview"/></label>

            <form id="selected-radio" action="${pageContext.request.contextPath}/reports/review">
                <input type="hidden" name="list" id="listField">
            </form>

            <input type="radio" class="btn-check" name="btnradio" id="btnradio-banned" autocomplete="off" ${(param.list != null && param.list == 'banned')? 'checked':''}>
            <label class="btn btn-outline-success" for="btnradio-banned"><spring:message code="report.banned"/></label>

        </div>

        <div>
            contador de la lista seleccionada : ${listCount}
        </div>

        <c:choose>
            <c:when test="${param.list == 'comments' || param.list == null || param.list == '' }">
                comments
            </c:when>
            <c:when test="${param.list == 'ml'}">
                moovieLists
            </c:when>
            <c:when test="${param.list == 'mlReviews'}">
                moovieList Reviews
            </c:when>
            <c:when test="${param.list == 'reviews'}">
                reviews
            </c:when>
            <c:when test="${param.list == 'banned'}">
                banned
            </c:when>
        </c:choose>
    </div>
</sec:authorize>
<%--<sec:authorize access="!hasRole('ROLE_MODERATOR')">
    <c:import url="403.jsp"/>
</sec:authorize>--%>



</body>
</html>
