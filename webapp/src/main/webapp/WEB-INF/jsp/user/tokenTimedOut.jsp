<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>
<div>
    <div>
        <a href="<c:url value="/"/>">
            <img src="<c:url value='/resources/logo.png'/>" alt="moovie_logo"/>
        </a>
        <div>
            <div>
                <p class="text"><spring:message code="email.timedOutToken"/></p>
                <br>
                <a href="<c:url value="/register/resendEmail?token=${token}"/>"><spring:message
                        code="email.resendEmail"/></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>