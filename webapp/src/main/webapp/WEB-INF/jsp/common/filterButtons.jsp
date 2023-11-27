<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<c:set var="selectedGenres" value="${fn:split(param.g, ',')}" />
<c:set var="selectedProviders" value="${fn:split(param.providers, ',')}" />
<c:set var="selectedLanguages" value="${fn:split(param.l,',' )}" />
<c:set var="selectedStatus" value="${fn:split(param.status,',' )}"/>
<div>


        <div class="container d-flex justify-content-left p-0 flex-wrap" id="genre-chips">
            <c:if test="${param.g != null && param.g.length() > 0}">
                <h4>
                    <spring:message code="createList.displayGenres"/>
                </h4>
            </c:if>
            <c:forEach var="gen" items="${param.g}">
                <div class="m-1 badge text-bg-dark">
                    <span class="text-bg-dark"><c:out value="${gen}"/></span>
                    <i class="btn bi bi-trash-fill" onclick="deleteChip(this)"></i>
                </div>
            </c:forEach>
        </div>
        <div class="container d-flex justify-content-left p-0 flex-wrap" id="provider-chips">
            <c:if test="${param.providers != null && param.providers.length() > 0}">
                <h4>
                    <spring:message code="createList.displayProviders"/>
                </h4>
            </c:if>
            <c:forEach var="provider" items="${param.providers}">
                <div class="m-1 badge text-bg-dark">
                    <span class="text-bg-dark"><c:out value="${provider}"/></span>
                    <i class="btn bi bi-trash-fill" onclick="deleteChip(this)"></i>
                </div>
            </c:forEach>
        </div>

    <c:if test="${param.searchBar == 'true'}">
        <button class="btn btn-success m-1" type="button" data-bs-toggle="collapse" data-bs-target="#collapseFilters" aria-expanded="false" aria-controls="collapseFilters">
            <i class="bi bi-filter"></i>
        </button>
    <div class="collapse m-1" id="collapseFilters">
    </c:if>




        <form id="filter-form" class="mb-2 d-flex flex-column justify-content-between" action="${pageContext.request.contextPath}/${param.url}" method="get" onsubmit="beforeSubmit()">

            <c:if test="${param.query != null  && param.query.length() > 0}">
                <input type="hidden" name="query" value="${param.query}">
            </c:if>
            <c:if test="${param.credit != null && param.credit.length() > 0}">
                <input type="hidden" name="credit" value="${param.credit}">
            </c:if>
            <div class="d-flex flex-row m-1">
                <button class="btn btn-outline-success me-1" type="submit" ><spring:message code="createList.apply"/></button>
                <a style="height: 100%;" class="btn btn-outline-success align-bottom" href="${pageContext.request.contextPath}/${param.url}">
                    <spring:message code="createList.reset"/>
                </a>
            </div>



            <input type="hidden" id="selected-media-input" />
            <select  name="m" class="form-select filter-width m-1" aria-label="Filter!">
                <option ${'All' == param.m ? 'selected' : ''} value="All"><spring:message code="createList.allMedia"/></option>
                <option  ${'Movies' == param.m ? 'selected' : ''} value="Movies"><spring:message code="createList.movies"/></option>
                <option  ${'Series' == param.m ? 'selected' : ''} value="Series"><spring:message code="createList.series"/></option>
            </select>

            <div class="d-flex flex-row m-1">
                <select name="orderBy" class="form-select filter-width" aria-label="Filter!">
                    <option ${'name' == param.orderBy ? 'selected' : ''} value="name"><spring:message code="createList.orderByTitle"/></option>
                    <option ${('totalRating' == param.orderBy || param.orderBy == null) ? 'selected' : ''} value="totalRating"><spring:message code="createList.orderByUserScore"/></option>
                    <option ${('tmdbRating' == param.orderBy || param.orderBy == null) ? 'selected' : ''} value="tmdbRating"><spring:message code="createList.orderByMoovieScore"/></option>
                    <option ${'releaseDate' == param.orderBy ? 'selected' : ''} value="releaseDate"><spring:message code="createList.orderByReleaseDate"/></option>
                </select>
                <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='asc'? 'asc':'desc'}">
                <div class="btn btn-style me-1" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                    <i id="sortIcon" class="bi bi-arrow-${param.order == 'asc' ? 'up' : 'down'}-circle-fill"></i>
                </div>
            </div>

            <input type="hidden" name="g" id="hiddenGenreInput">
            <button class="btn btn-success m-1" type="button" data-bs-toggle="collapse" data-bs-target="#collapseGenres" aria-expanded="false" aria-controls="collapseGenres">
                <spring:message code="createList.genres"/>
            </button>

            <c:set var="isChecked" value="" />
            <div style="max-height: 20vh;overflow: auto " class="collapse m-1" id="collapseGenres">
                <input type="text" id="searchBoxGenre" placeholder="<spring:message code="createList.search"/>" class="form-control mb-3">
                <c:forEach var="genre" items="${genresList}">
                    <c:forEach var="selectedGenre" items="${selectedGenres}">
                        <c:if test="${selectedGenre == genre}">
                            <c:set var="isChecked" value="checked" />
                        </c:if>
                    </c:forEach>
                    <div class="form-check special-genre-class">
                        <input ${isChecked} type="checkbox" class="form-check-input special-genre-input" id="dropdownCheck${genre}">
                        <label class="form-check-label" for="dropdownCheck${genresList.indexOf(genre)}"><c:out value="${genre}"/></label>
                    </div>
                    <c:set var="isChecked" value="" />
                </c:forEach>
            </div>

            <input type="hidden" name="providers" id="hiddenProviderInput">
            <button class="btn btn-success m-1" type="button" data-bs-toggle="collapse" data-bs-target="#collapseProvider" aria-expanded="false" aria-controls="collapseProvider">
                <spring:message code="discover.providers"/>
            </button>

            <c:set var="isChecked" value="" />
            <div style="max-height: 20vh;overflow: auto " class="collapse m-1" id="collapseProvider">
                <input type="text" id="searchBoxProvider" placeholder="<spring:message code="createList.search"/>" class="form-control mb-3">
                <c:forEach var="provider" items="${providersList}">
                    <c:forEach var="selectedProvider" items="${selectedProviders}">
                        <c:if test="${selectedProvider == provider.providerName}">
                            <c:set var="isChecked" value="checked" />
                        </c:if>
                    </c:forEach>
                    <div class="form-check special-provider-class">
                        <input ${isChecked} type="checkbox" class="form-check-input special-provider-input" id="dropdownCheck${provider.providerName}">
                        <label class="form-check-label" for="dropdownCheck${providersList.indexOf(provider)}"><span class="mt-1 badge text-bg-light border border-black"><img src="${provider.logoPath}" alt="provider logo" style="height: 1.4em; margin-right: 5px;">${provider.providerName}</span></label>
                    </div>
                    <c:set var="isChecked" value="" />
                </c:forEach>
            </div>


            <input type="hidden" name="l" id="hiddenLangInput">
            <button class="btn btn-success m-1" type="button" data-bs-toggle="collapse" data-bs-target="#collapseLanguages" aria-expanded="false" aria-controls="collapseLanguages">
                <spring:message code="createList.lang"/>
            </button>
            <c:set var="isChecked" value="" />
            <div style="max-height: 20vh;overflow: auto " class="collapse m-1" id="collapseLanguages">
                <input type="text" id="searchBoxLanguage" placeholder="<spring:message code="createList.search"/>" class="form-control mb-3">
                <c:forEach var="genre" items="${langList}">
                    <c:forEach var="selectedGenre" items="${selectedLanguages}">
                        <c:if test="${selectedGenre == genre}">
                            <c:set var="isChecked" value="checked" />
                        </c:if>
                    </c:forEach>
                    <div class="form-check special-language-class">
                        <input ${isChecked} type="checkbox" class="form-check-input special-language-input" id="dropdownCheck${genre}">
<%--                        <label class="form-check-label" for="dropdownCheck${langList.indexOf(genre)}">${genre}</label>--%>
                        <label class="form-check-label" for="dropdownCheck${langList.indexOf(genre)}"><c:out value="${genre}"/></label>
                    </div>
                    <c:set var="isChecked" value="" />
                </c:forEach>
            </div>


            <input type="hidden" name="status" id="hiddenStatusInput">
            <button class="btn btn-success m-1" type="button" data-bs-toggle="collapse" data-bs-target="#collapseStatus" aria-expanded="false" aria-controls="collapseStatus">
                <spring:message code="createList.status"/>
            </button>
            <c:set var="isChecked" value="" />
            <div style="max-height: 20vh;overflow: auto " class="collapse m-1" id="collapseStatus">
                <input type="text" id="searchBoxStatus" placeholder="<spring:message code="createList.search"/>" class="form-control mb-3">
                <c:forEach var="genre" items="${statusList}">
                    <c:forEach var="selectedGenre" items="${selectedStatus}">
                        <c:if test="${selectedGenre == genre}">
                            <c:set var="isChecked" value="checked" />
                        </c:if>
                    </c:forEach>
                    <div class="form-check special-status-class">
                        <input ${isChecked} type="checkbox" class="form-check-input special-status-input" id="dropdownCheck${genre}">
                        <label class="form-check-label" for="dropdownCheck${statusList.indexOf(genre)}"><c:out value="${genre}"/></label>
                    </div>
                    <c:set var="isChecked" value="" />
                </c:forEach>
            </div>


            <c:if test="${param.searchBar == 'true'}">
                <input class="form-control me-2" type="search" name="q" value="${param.q}" placeholder="<spring:message code="createList.searchBar"/>" aria-label="Search">
            </c:if>


        </form>
    <c:if test="${param.searchBar == 'true'}">
    </div>
    </c:if>

</div>
</body>
</html>
