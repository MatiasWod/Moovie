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
<div style="z-index: 1;">
    <form id="filter-form" class="mb-2 d-flex flex-row justify-content-between" action="${pageContext.request.contextPath}/${param.url}" method="get" onsubmit="beforeSubmit()">
        <c:if test="${param.query != null  && param.query.length() > 0}">
            <input type="hidden" name="query" value="${param.query}">
        </c:if>
        <c:if test="${param.credit != null && param.credit.length() > 0}">
            <input type="hidden" name="credit" value="${param.credit}">
        </c:if>
        <input type="hidden" id="selected-media-input" />
        <div role="group" class="input-group d-flex flex-row m-1 me-3">
            <select  name="m" class="form-select filter-width" aria-label="Filter!">
                <option ${'All' == param.m ? 'selected' : ''} value="All"><spring:message code="createList.allMedia"/></option>
                <option  ${'Movies' == param.m ? 'selected' : ''} value="Movies"><spring:message code="createList.movies"/></option>
                <option  ${'Series' == param.m ? 'selected' : ''} value="Series"><spring:message code="createList.series"/></option>
            </select>

            <input type="hidden" name="g" id="hiddenGenreInput">
            <div class="dropdown">
                <button style="height:100%;width: 100px;margin-right: 5px;" type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="outside">
                    <spring:message code="createList.genres"/>
                </button>
                <c:set var="isChecked" value="" />
                <div style="height: 50vh" class="dropdown-menu scrollableDiv flex-wrap p-4">
                    <input type="text" id="searchBoxGenre" placeholder="<spring:message code="createList.search"/>" class="form-control mb-3">
                    <%--   ES NECESARIO UTILIZAR LA VAR isChecked.
                      Porque al simplemente realizar fn:contains(param.g,genre)
                      existen casos como Action&Adventure que siempre daran match para Action y Adventure
                      Es preferible esto a en el controlador manejar la creacion de modelos nuevos que contemplen el checked para cada genero--%>
                    <c:forEach var="genre" items="${genresList}">
                        <%--                                    selectedGenre no deberia ser muy grande, ya que es el listado de genres seleccionados--%>
                        <c:forEach var="selectedGenre" items="${selectedGenres}">
                            <c:if test="${selectedGenre == genre}">
                                <c:set var="isChecked" value="checked" />
                            </c:if>
                        </c:forEach>
                        <div class="form-check special-genre-class">
                            <input ${isChecked} type="checkbox" class="form-check-input special-genre-input" id="dropdownCheck${genre}">
                            <label class="form-check-label" for="dropdownCheck${genresList.indexOf(genre)}">${genre}</label>
                        </div>
                        <c:set var="isChecked" value="" /> <!-- Reset the isChecked variable -->
                    </c:forEach>
                </div>
            </div>

            <input type="hidden" name="providers" id="hiddenProviderInput">
            <div class="dropdown">
                <button style="height:100%;width: 150px;margin-right: 5px;" type="button" class="btn btn-success dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" data-bs-auto-close="outside">
                    <spring:message code="discover.providers"/>
                </button>
                <c:set var="isChecked" value="" />
                <div style="height: 50vh" class="dropdown-menu scrollableDiv flex-wrap p-4">
                    <input type="text" id="searchBoxProvider" placeholder="<spring:message code="createList.search"/>" class="form-control mb-3">
                    <%--   ES NECESARIO UTILIZAR LA VAR isChecked.
                      Porque al simplemente realizar fn:contains(param.g,genre)
                      existen casos como Action&Adventure que siempre daran match para Action y Adventure
                      Es preferible esto a en el controlador manejar la creacion de modelos nuevos que contemplen el checked para cada genero--%>
                    <c:forEach var="provider" items="${providersList}">
                        <%--                                    selectedGenre no deberia ser muy grande, ya que es el listado de genres seleccionados--%>
                        <c:forEach var="selectedProvider" items="${selectedProviders}">
                            <c:if test="${selectedProvider == provider.providerName}">
                                <c:set var="isChecked" value="checked" />
                            </c:if>
                        </c:forEach>
                        <div class="form-check special-provider-class">
                            <input ${isChecked} type="checkbox" class="form-check-input special-provider-input" id="dropdownCheck${provider.providerName}">
                                <%--                                        Por el uso de inner text en el beforeSubmit, la label debe estar en este formato. Para no captar espacios de la estructura del html. por mas que el estilo no sea el mejor
                                                                            sino hay que usar fn:trim para cada comparacion del isChecked, lo cual es un gran desperdicio de performance a cambio de un cambio de la estructura del html--%>
                            <label class="form-check-label" for="dropdownCheck${providersList.indexOf(provider)}"><span class="mt-1 badge text-bg-light border border-black"><img src="${provider.logoPath}" alt="provider logo" style="height: 1.4em; margin-right: 5px;">${provider.providerName}</span></label>
                        </div>
                        <c:set var="isChecked" value="" /> <!-- Reset the isChecked variable -->
                    </c:forEach>
                </div>
            </div>

            <select name="orderBy" class="form-select filter-width" aria-label="Filter!">
                <option ${'name' == param.orderBy ? 'selected' : ''} value="name"><spring:message code="createList.orderByTitle"/></option>
                <option ${('totalrating' == param.orderBy || param.orderBy == null) ? 'selected' : ''} value="totalrating"><spring:message code="createList.orderByMoovieScore"/></option>
                <option ${('tmdbrating' == param.orderBy || param.orderBy == null) ? 'selected' : ''} value="tmdbrating"><spring:message code="createList.orderByTmdbScore"/></option>
                <option ${'releasedate' == param.orderBy ? 'selected' : ''} value="releasedate"><spring:message code="createList.orderByReleaseDate"/></option>
            </select>
            <%--                PARA TENER EN CUENTA --> MIRAR EL DEFAULT sort y orderBy en el controller para settear los valores iniciales de las labels/iconos--%>
            <input type="hidden" name="order" id="sortOrderInput" value="${param.order =='asc'? 'asc':'desc'}">
            <div class="btn btn-style me-1" id="sortButton" onclick="changeSortOrder('sortOrderInput', 'sortIcon', '${param.orderBy}')">
                <i id="sortIcon" class="bi bi-arrow-${param.order == 'asc' ? 'up' : 'down'}-circle-fill"></i>
            </div>
            <c:if test="${param.searchBar == 'true'}">
                <input class="form-control me-2" type="search" name="q" value="${param.q}" placeholder="<spring:message code="createList.searchBar"/>" aria-label="Search">
            </c:if>
            <button class="btn btn-outline-success me-1" type="submit" ><spring:message code="createList.apply"/></button>
            <a style="height: 100%;" class="btn btn-outline-success align-bottom" href="${pageContext.request.contextPath}/${param.url}">
                <spring:message code="createList.reset"/>
            </a>
        </div>
    </form>
    <div class="container d-flex justify-content-left p-0" id="genre-chips">
        <c:if test="${param.g != null && param.g.length() > 0}">
            <h4>
                <spring:message code="createList.displayGenres"/>
            </h4>
        </c:if>
        <c:forEach var="gen" items="${param.g}">
            <div class="m-1 badge text-bg-dark">
                <span class="text-bg-dark"> ${gen} </span>
                <i class="btn bi bi-trash-fill" onclick="deleteChip(this)"></i>
            </div>
        </c:forEach>
    </div>
    <div class="container d-flex justify-content-left p-0" id="provider-chips">
        <c:if test="${param.providers != null && param.providers.length() > 0}">
            <h4>
                <spring:message code="createList.displayProviders"/>
            </h4>
        </c:if>
        <c:forEach var="provider" items="${param.providers}">
            <div class="m-1 badge text-bg-dark">
                <span class="text-bg-dark">${provider}</span>
                <i class="btn bi bi-trash-fill" onclick="deleteChip(this)"></i>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
