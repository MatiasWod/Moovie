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

</head>
<body>
<c:import url="navBar.jsp"/>


<div class="container d-flex flex-column">

    <div class="d-flex container justify-content-center">
        <div class="m-2">
            <div class="d-flex align-items-center">
                <div class="m-1 d-flex align-items-center">
                    <i class="bi bi-list-ul"></i>
                    <h5>
<%--                        ${profile.moovieListCount}--%>
                        666
                    </h5>
                </div>
                <div class="m-1 d-flex align-items-center">
                    <h5>
                        <i class="bi-star"></i>
<%--                        ${profile.reviewsCount}--%>
                        69
                    </h5>
                </div>
                <div class="m-1 d-flex align-items-center">
                    <h5>
                        <img style="padding-bottom: 6px;" height="37" width="37" src="${pageContext.request.contextPath}/resources/logo.png" alt="moo">
<%--                        ${profile.milkyPoints}--%>
                        420
                    </h5>
                </div>
            </div>
        </div>
    </div>

    <div class="btn-group m-2" role="group" aria-label="Basic radio toggle button group">

        <input type="radio" class="btn-check" name="btnradio" id="btnradio-watched-list" autocomplete="off" ${(param.list != null && param.list == 'watched-list')? 'checked':''}>
        <label class="btn btn-outline-success" for="btnradio-watched-list"><spring:message code="profile.watched"/></label>

        <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-lists" autocomplete="off" ${(param.list == null || param.list == '' || param.list == 'user-lists')? 'checked':''}>
        <label class="btn btn-outline-success" for="btnradio-user-lists"><spring:message code="profile.userLists"/></label>

        <input type="radio" class="btn-check" name="btnradio" id="btnradio-user-private-lists" autocomplete="off" ${(param.list != null && param.list == 'private-user-lists')? 'checked':''}>
        <label class="btn btn-outline-success" for="btnradio-user-private-lists"><spring:message code="profile.privateUserLists"/></label>

        <input type="radio" class="btn-check" name="btnradio" id="btnradio-liked-lists" autocomplete="off" ${(param.list != null && param.list == 'liked-lists')? 'checked':''}>
        <label class="btn btn-outline-success" for="btnradio-liked-lists"><spring:message code="profile.likedLists"/></label>

        <input type="radio" class="btn-check" name="btnradio" id="btnradio-banned" autocomplete="off" ${(param.list != null && param.list == 'banned')? 'checked':''}>
        <label class="btn btn-outline-success" for="btnradio-liked-lists"><spring:message code="profile.likedLists"/></label>

        <form id="selected-radio" action="${pageContext.request.contextPath}/reports/review">
            <input type="hidden" name="list" id="listField">
        </form>

    </div>

    <c:forEach items="${commentReports}" var="report">
        <div>
            reportedBy username: ${report.reportedBy.username} --
            reported comment content: ${report.comment.content} --
            ${report.report_date} --
            ${report.type} --
            ${report.content} --
        </div>
    </c:forEach>
    
    <c:choose>
        
        <c:when test="${param.list == 'users'}">
            <div>
                Esto sera un bloque para revisar un reporte de un user dado.
            </div>
        </c:when>
    </c:choose>


</div>



</body>
</html>
