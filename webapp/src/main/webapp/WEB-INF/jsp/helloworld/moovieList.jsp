<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title><spring:message code="moovieList.title"/></title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=65" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=87"></script>
</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp"/>
<c:import url="/WEB-INF/jsp/helloworld/listExtract.jsp">
    <c:param name="publicList" value="true"/>
</c:import>
<c:if test="${moovieList.type==publicType}">
<hr/>
<div class="d-flex">
    <div class="d-flex flex-column flex-grow-1 m-3">
        <%--<c:if test="${review.hasComments}">--%>
            <div class="input-group mt-2 mb-3">
                <textarea class="form-control" placeholder="<spring:message code="details.addComment"/>" aria-label="With textarea"></textarea>
            </div>
            <c:forEach begin="1" end="5">
                <div class="mb-2 mt-2 card card-body">
                    <div class="d-flex justify-content-between">
                        <h6 class="card-title">Username (agregar href)</h6>
                        <div class="d-flex">
                            <a class="me-1 btn-sm btn btn-outline-success">
                                <i class="m-1 bi bi-hand-thumbs-up"></i>
                            </a>
                            <a class="btn btn-sm btn-outline-danger">
                                <i class="m-1 bi bi-hand-thumbs-down"></i>
                            </a>
                        </div>
                    </div>
                    <p class="card-text">Comment Commetn Commetn</p>
                </div>
            </c:forEach>
                <%--</c:if>--%>
    </div>
    <c:if test="${RecomendedListsCards}">
        <div style="max-width: 30vw" class="d-flex flex-column align-items-center m-2">
            <h4><spring:message code="moovieList.recommendations"/></h4>
            <div class="d-flex flex-row flex-wrap">
                <c:forEach var="cardList" items="${RecomendedListsCards}">
                    <%@include file="listCard.jsp"%>
                </c:forEach>
            </div>
        </div>
    </c:if>
</c:if>
</body>
</html>