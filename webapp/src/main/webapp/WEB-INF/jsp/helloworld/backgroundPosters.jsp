<%@    taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@    taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<div style="z-index: -1; position: absolute; left: 0; top: 0; width: 100%; height: 100%"
     class="d-flex flex-row ">
    <c:forEach begin="0" end="1">
        <div class="d-flex flex-column" >
            <div class="d-flex movingContainer" style="height: 34%; width: 100vw">
                <c:forEach begin="0" end="6" var="j">
                    <img style=""
                         class="cropCenter me-1" src="${mediaList[j].posterPath}" alt="poster">
                </c:forEach>
            </div>
            <div class="d-flex mt-1 movingContainer2" style="height: 34%">
                <c:forEach begin="7" end="13" var="j">
                    <img style=""
                         class="cropCenter me-1" src="${mediaList[j].posterPath}" alt="poster">
                </c:forEach>
            </div>
            <div class="d-flex mt-1 movingContainer" style="height: 34%">
                <c:forEach begin="14" end="20" var="j">
                    <img style=""
                         class="cropCenter me-1" src="${mediaList[j].posterPath}" alt="poster">
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</div>
<style>
    @keyframes moveRightToLeft {
        0% {
            transform: translateX(0);
        }
        100% {
            transform: translateX(-100%);
        }
    }

    .movingContainer {
        animation: moveRightToLeft 60s linear infinite;
    }
    .movingContainer2 {
        animation: moveRightToLeft 120s linear reverse infinite;
    }

</style>
</html>
