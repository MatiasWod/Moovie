<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="card shadow">
    <div class="card-body" style="border-radius: 5%">
                <span data-toggle="tooltip" data-placement="top" title="<c:out value="${param.title}"/>(<c:out value="${param.releaseDate}"/>)">
                </span>
        <img class="card-img-top" style="border-radius: 5%" src="<c:out value="${param.posterPath}"/>" alt="posterPath">
            <h4 class="card-title"><c:out value="${param.mediaName}"/></h4>
            <h5><c:out value="${param.releaseDate}"/></h5>
    </div>
</div>