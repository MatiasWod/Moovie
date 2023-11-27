<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><spring:message code="report.title"/></title>

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
<body style="background: whitesmoke">
<c:import url="../common/navBar.jsp"/>

<div style="border: solid black; min-width: 40%; min-height: 50%; position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);" class="container-gray justify-content-center p-1 d-flex flex-column">
    <div class="d-flex justify-content-between align-items-center mt-2 m-1">
        <button class="btn btn-sm btn-outline-success" onclick="history.back()"><spring:message code="report.back"/></button>
        <img width="40" height="40" src="${pageContext.request.contextPath}/resources/logo.png" alt="logo">
    </div>
    <div class="d-flex justify-content-center">
        <h2><spring:message code="report.what"/></h2>
    </div>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show m-2" id="errorAlert" role="alert">
            <div class="d-flex justify-content-between align-items-center">
                <div>${errorMessage} <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}">${insertedMooovieList.name}</a></div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </div>
    </c:if>
    <form:form modelAttribute="reportForm" action="${pageContext.request.contextPath}/reports/new?id=${param.id}&reportedBy=${param.reportedBy}&type=${param.type}" method="post" cssClass="d-flex flex-column m-3 flex-grow-1">
        <form:input path="reportType" type="hidden"/>
        <form:errors path="reportType" cssClass="error m-1" element="p"/>
        <div class="d-flex m-1">
            <input value="0" class="form-check-input me-1" type="radio" name="flexRadioDefault" id="flexRadioDefault1">
            <label style="font-weight: bold" class="form-check-label" for="flexRadioDefault1" ><spring:message code="report.hate"/></label>
        </div>
        <div class="d-flex m-1">
            <input value="1" class="form-check-input me-1" type="radio" name="flexRadioDefault" id="flexRadioDefault2">
            <label style="font-weight: bold" class="form-check-label" for="flexRadioDefault2"><spring:message code="report.abuse"/></label>
        </div>
        <div class="d-flex m-1">
            <input value="2" class="form-check-input me-1" type="radio" name="flexRadioDefault" id="flexRadioDefault3">
            <label style="font-weight: bold" class="form-check-label" for="flexRadioDefault3"><spring:message code="report.privacy"/></label>
        </div>
        <div class="d-flex m-1">
            <input value="3" class="form-check-input me-1" type="radio" name="flexRadioDefault" id="flexRadioDefault4">
            <label style="font-weight: bold" class="form-check-label" for="flexRadioDefault4"><spring:message code="report.spam"/></label>
        </div>

        <form:textarea path="content" cssClass="mt-1"/>
        <form:input path="type" type="hidden" value="${param.type}"/>
        <form:input path="reportedBy" type="hidden" value="${param.reportedBy}"/>
        <form:input path="id" type="hidden" value="${param.id}"/>
        <button class="btn btn-success mt-3" type="submit"><spring:message code="details.submit"/></button>
    </form:form>
</div>

</body>
</html>

<script>
    $(document).ready(function () {
        $("input[name='flexRadioDefault']").click(function () {
            let selectedValue = $("input[name='flexRadioDefault']:checked").val();
            $("input[name='reportType']").val(selectedValue);
        });
    });
</script>
