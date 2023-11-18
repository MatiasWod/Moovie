<table class="table table-striped" id="movieTable">
    <thead>
    <tr>
        <th scope="col"></th>
        <th scope="col"><spring:message code="listExtract.orderByTitle"/></th>
        <th scope="col"><spring:message code="listExtract.orderByType"/></th>
        <th scope="col"><spring:message code="listExtract.orderByScore"/></th>
        <th scope="col"><spring:message code="listExtract.orderByUsersScore"/></th>
        <th scope="col"><spring:message code="listExtract.orderByReleaseDate"/></th>
        <th scope="col" style="width: 50px"></th>
    </tr>
    </thead>
    <c:choose>
        <c:when test="${not empty profiles}">
            <tbody>
            <c:forEach var="index" items="${profiles}" varStatus="loop">
                <tr>
                    <!-- Title -->
                    <td>
                        <div class="col-auto">
                            <a href="${pageContext.request.contextPath}/profile/${index.username}"
                               style="text-decoration: none; color: inherit;">
                                <img src="${pageContext.request.contextPath}/profile/image/${index.username}" class="img-fluid" width="100"
                                     height="100" alt="${index.username} picture"/>
                            </a>
                        </div>
                    </td>
                    <td>
                        <div class="col-auto" style="text-align: center">
                            <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                               style="text-decoration: none; color: inherit;">
                                <strong>${mediaList[loop.index].name}</strong>
                            </a>
                        </div>
                        </div>
                        <!-- Type -->
                    <td>
                        <c:choose>
                            <c:when test="${mediaList[loop.index].type}">
                                <spring:message code="listExtract.series"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="listExtract.movies"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <!-- Score -->
                    <td>${mediaList[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
                    </td>
                    <!--User Score -->
                    <td>
                        <c:choose>
                            <c:when test="${mediaList[loop.index].voteCount>0}">
                                ${mediaList[loop.index].totalRating}<i class="bi bi-star" style="margin-left: 5px"></i>
                            </c:when>
                            <c:otherwise>
            <span data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.noUsersRatingsMessage"/>">
            N/A<i class="bi bi-star" style="margin-left: 5px"></i>
                </span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <span>${mediaList[loop.index].releaseDate}</span>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${mediaList[loop.index].watched}">
                                <div class="col-auto text-center m-0">
                                    <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                                        <input type="hidden" name="listId" value="${watchedListId}"/>
                                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-eye-fill" style="color: green; cursor: pointer;"></i>
                        </span>
                                        </button>
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="col-auto text-center m-0">
                                    <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                        <input type="hidden" name="listId" value="${watchedListId}"/>
                                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-eye-fill" style="color: gray; cursor: pointer;"></i>
                        </span>
                                        </button>
                                    </form>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${mediaList[loop.index].watchlist}">
                                <div class="col-auto text-center m-0">
                                    <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
                                        <input type="hidden" name="listId" value="${watchlistId}"/>
                                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-bookmark-check-fill" style="color: green; cursor: pointer;"></i>
                        </span>
                                        </button>
                                    </form>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="col-auto text-center m-0">
                                    <form action="${pageContext.request.contextPath}/insertMediaToList" method="post">
                                        <input type="hidden" name="listId" value="${watchlistId}"/>
                                        <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
                                        <button class="btn btn-lg" type="submit">
                        <span class="d-inline-block"  tabindex="0" data-bs-toggle="popover" data-bs-trigger="hover" data-bs-content="<spring:message code="listExtract.watchedMessage"/>">
                            <i class="bi bi-bookmark-check-fill" style="color: gray; cursor: pointer;"></i>
                        </span>
                                        </button>
                                    </form>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>

        </c:when>
        <c:otherwise>
            <tbody>
            <tr>
                <td colspan="10" style="text-align: center">List is empty</td>
            </tr>
            </tbody>
        </c:otherwise>
    </c:choose>
</table>