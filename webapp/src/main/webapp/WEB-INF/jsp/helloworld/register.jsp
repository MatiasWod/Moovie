<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<body>
<form:form modelAttribute="registerForm" action="${pageContext.request.contextPath}/registerpost" method="post">
    <form:input path="username" class="form-control" id="username" placeholder="Enter username"/>
    <form:input path="email" type="email" class="form-control" id="email" placeholder="Enter email"/>
    <form:input path="password" class="form-control" id="password" placeholder="Enter password"/>
    <div><input type="submit" value="Login!"/></div>
</form:form>
</body>
</html>