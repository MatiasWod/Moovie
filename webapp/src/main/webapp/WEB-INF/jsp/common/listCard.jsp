<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link href="${pageContext.request.contextPath}/resources/lists.css?version=60" rel="stylesheet"/>

<div class="list-card card"
     onclick="location.href='${pageContext.request.contextPath}/list/${cardList.moovieListId}?page=1'">
    <div class="list-img-container card-img-top">
        <c:forEach var="image" items="${cardList.images}">
            <img class="cropCenterImage async-image" src="/resources/defaultPoster.png" data-src="${image}" alt="...">
        </c:forEach>
        <c:forEach begin="${fn:length(cardList.images)}" end="3">
            <img class="cropCenterImage async-image"
                 src="/resources/defaultPoster.png"
                 data-src=${pageContext.request.contextPath}/resources/defaultPoster.png alt="...">
        </c:forEach>
    </div>
    <div class="card-body cardBodyFlex">
        <div>
            <div class="card-name-likes">
                <div class="card-content overflow-hidden">
                    <h5 class="card-title"><strong><c:out value="${cardList.name}"/></strong>
                        <c:if test="${cardList.currentUserWatchAmount == cardList.size }">             <span
                                class="d-inline-block"
                                data-bs-toggle="popover" data-bs-trigger="hover"
                                data-bs-content="<spring:message code="listCard.allMediasWatched"/>">
                <i class="bi bi-check-circle-fill" style="color: green"></i>
            </span></c:if></h5>
                </div>
                <div class="card-likes">
                    <h5><i class="bi bi-hand-thumbs-up"></i><c:out value="${cardList.likeCount}"/></h5>
                </div>
            </div>
            <div style="display: flex;">
                <c:if test="${cardList.moviesAmount > 0}">
                    <p>${cardList.moviesAmount} <spring:message code="listCard.movies"/></p>
                </c:if>

                <c:if test="${cardList.moviesAmount > 0 && (cardList.size - cardList.moviesAmount) > 0}">
                    <style>
                        p {
                            margin-right: 10px; /* Add a space between "Movies" and "Series" */
                        }
                    </style>
                </c:if>
                <c:if test="${(cardList.size - cardList.moviesAmount) > 0}">
                    <p>${(cardList.size - cardList.moviesAmount)} <spring:message code="listCard.series"/></p>
                </c:if>
            </div>
            <p style="max-height: 4.5rem" class="card-text overflow-hidden text-muted">
                <spring:message code="listCard.by" arguments="${cardList.username}"/>
                <c:if test="${cardList.hasBadge}"><i class="bi bi-trophy"></i></c:if>
            </p>
            <p style="max-height: 3.5rem; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" class="card-text">
                <c:out value="${cardList.description}"/>
            </p>
        </div>
    </div>

</div>