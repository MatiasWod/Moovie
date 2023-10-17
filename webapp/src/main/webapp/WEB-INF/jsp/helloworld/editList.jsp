<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/details.css?version=87" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <title>Moovie List</title>
    <link href="${pageContext.request.contextPath}/resources/moovieList.css?version=67" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/resources/buttonsStyle.css?version=1" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/resources/detailsFunctions.js?version=87"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListFunctions.js?version=81"></script>
    <script src="${pageContext.request.contextPath}/resources/moovieListSort.js?version=82"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <title>Edit your List!</title>
</head>
<body style="background: whitesmoke">
<c:import url="navBar.jsp"/>
<div class="container d-flex flex-column">
    <div class="header d-flex text-center" style="background-image: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.2)), url('${mediaList[0].backdropPath}'); background-size: cover; background-position: center;">
        <div class="d-flex flex-column flex-grow-1">
            <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${moovieList.name}"/></h1>
            <h3><c:out value="${moovieList.description}"/></h3>
                <h4 style="color: ghostwhite;">by
                    <a style="text-decoration: none; color: inherit;" href="${pageContext.request.contextPath}/profile/${moovieList.username}">
                        <c:out value="${moovieList.username}"/>
                    </a>
                </h4>
        </div>
    </div>
    <div>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show m-2" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div>${errorMessage} <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}">${insertedMooovieList.name}</a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show m-2" id="errorAlert" role="alert">
                <div class="d-flex justify-content-between align-items-center">
                    <div>${successMessage} <a href="${pageContext.request.contextPath}/list/${insertedMooovieList.moovieListId}">${insertedMooovieList.name}</a></div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </div>
        </c:if>
    </div>
    <div class="justify-content-between d-flex flex-row" style="margin: 10px">
        <h2><strong>Editing mode</strong> </h2>
        <form action="${pageContext.request.contextPath}/updateMoovieListOrder/${moovieList.moovieListId}" method="POST" onsubmit="beforeSubmit()">
            <input type="hidden" name="toPrevArray" id="toPrevArray" value="">
            <input type="hidden" name="currentArray" id="currentArray" value="">
            <input type="hidden" name="toNextArray" id="toNextArray" value="">
            <button type="submit" class="btn btn-style">Apply new order</button>
        </form>
    </div>
<table class="table table-striped" id="movieTable">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col"></th>
        <th scope="col">Title</th>
        <th scope="col">Type</th>
        <th scope="col">Score</th>
        <th scope="col">Release Date</th>
        <th scope="col" style="width: 50px"></th>
        <th scope="col" style="width: 100px"></th>
    </tr>
    </thead>
    <c:choose>
        <c:when test="${not empty mediaList}">
            <tbody>
            <c:forEach var="index" items="${mediaList}" varStatus="loop">
                <tr class="draggable-row" style="cursor: pointer;"  onmousedown="changeCursor(this)"  onmouseup="resetCursor(this)" data-mediaId="${mediaList[loop.index].mediaId}">
                    <!-- Title -->
                    <td class="position-td">
                        <div class="col-auto" style="text-align: center">
                            <span>${(loop.index + 1)+(pagingSize*currentPage)}</span>
                        </div>
                    </td>
                    <td>
                        <div class="col-auto">
                                <img src="${mediaList[loop.index].posterPath}" class="img-fluid" width="100"
                                     height="100" alt="${mediaList[loop.index].name} poster"/>
                        </div>
                    </td>
                    <td>
                        <div class="col-auto" style="text-align: center">
                                <strong>${mediaList[loop.index].name}</strong>
                        </div>
                        </div>
                        <!-- Type -->
                    <td>
                        <c:choose>
                            <c:when test="${mediaList[loop.index].type}">
                                Tv Series
                            </c:when>
                            <c:otherwise>
                                Movie
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <!-- Score -->
                    <td>${mediaList[loop.index].tmdbRating}<i class="bi bi-star-fill" style="margin-left: 5px"></i>
                    </td>
                    <td>
                        <span>${mediaList[loop.index].releaseDate}</span>
                    </td>
<td>
    <div class="dropdown">
        <div class="col-auto text-center" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-three-dots-vertical"></i>
        </div>
        <ul class="dropdown-menu">
            <!-- Dropdown menu items go here -->
            <c:if test="${(currentPage+1)!=numberOfPages}">
                <li><a class="dropdown-item" onclick="openPopup('nextPage-popup-${loop.index}')"><i
                        class="bi bi-caret-right-fill"></i> Next page</a></li>
            </c:if>
            <c:if test="${currentPage>0}">
                <li><a class="dropdown-item" onclick="openPopup('previousPage-popup-${loop.index}')"><i
                        class="bi bi-caret-left-fill"></i> Previous page</a></li>
            </c:if>
        </ul>
    </div>
    <div class="popup-overlay nextPage-popup-${loop.index}-overlay"
         onclick="closePopup('nextPage-popup-${loop.index}')"></div>
    <div class="popup nextPage-popup-${loop.index}">
        <h2>Send "${mediaList[loop.index].name}" to the next page?</h2>
        <div class="text-center" style="margin-top: 20px">
            <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                    onclick="closePopup('nextPage-popup-${loop.index}')">No
            </button>
            <button class="btn btn-dark" style="margin-inline: 10px"
                    onclick="moveRowToNextPage(${loop.index},${mediaList[loop.index].mediaId})">Yes
            </button>
        </div>
    </div>


    <div class="popup-overlay previousPage-popup-${loop.index}-overlay"
         onclick="closePopup('previousPage-popup-${loop.index}')"></div>
    <div class="popup previousPage-popup-${loop.index}">
        <h2>Send "${mediaList[loop.index].name}" to the previous page?</h2>
        <div class="text-center" style="margin-top: 20px">
            <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                    onclick="closePopup('previousPage-popup-${loop.index}')">No
            </button>
            <button class="btn btn-dark" style="margin-inline: 10px"
                    onclick="moveRowToPreviousPage(${loop.index},${mediaList[loop.index].mediaId})">Yes
            </button>
        </div>
    </div>
</td>
    <td>
        <div class="popup-overlay remove-popup-overlay" onclick="closePopup('remove-popup-${loop.index}')"></div>
        <div class="col-auto text-center">
            <button class="btn btn-danger" onclick="openPopup('remove-popup-${loop.index}')">
                    <i class="bi bi-trash-fill"></i> Remove
            </button>
        </div>
    </td>
<div class="popup-overlay remove-popup-${loop.index}-overlay"
     onclick="closePopup('remove-popup-${loop.index}')"></div>
<div class="popup remove-popup-${loop.index}">
    <h2>Remove "${mediaList[loop.index].name}" from ${moovieList.name}?</h2>
    <div class="text-center" style="margin-top: 20px">
        <form action="${pageContext.request.contextPath}/deleteMediaFromList" method="post">
            <button type="button" class="btn btn-danger" style="margin-inline: 10px"
                    onclick="closePopup('remove-popup-${loop.index}')">No
            </button>
            <input type="hidden" name="listId" value="${moovieList.moovieListId}"/>
            <input type="hidden" name="mediaId" value="${mediaList[loop.index].mediaId}"/>
            <button type="submit" class="btn btn-dark" style="margin-inline: 10px">Yes</button>
        </form>
    </div>
</div>
</tr>
            </c:forEach>
            </tbody>
        </c:when>
        <c:otherwise>
            <tbody>
            <tr>
                <td colspan="5">List is empty</td>
            </tr>
            </tbody>
        </c:otherwise>
    </c:choose>
</table>
</div>
<c:import url="/WEB-INF/jsp/helloworld/pagination.jsp">
    <c:param name="mediaPages" value="${numberOfPages}"/>
    <c:param name="currentPage" value="${currentPage + 1}"/>
    <c:param name="url" value="/editList/${moovieList.moovieListId}"/>
</c:import>

</body>
<script>
    $(document).ready(function() {
        // Enable drag-and-drop functionality for the table rows
        $("#movieTable tbody").sortable({
            handle: "td", // Use the position-td as the handle for   dragging.
            placeholder: "sortable-placeholder", // Add the CSS class for the placeholder
            forcePlaceholderSize: true, // Ensure that the placeholder size matches the dragged item
            start: function (event, ui) {
                ui.placeholder.height(ui.item.height()); // Set the placeholder height to match the row height
            },
            update: function(event, ui) {
                // This function is removed
            },
            stop: function(event, ui) {
                updateRowIndices()
            }
        });
    });

    function changeCursor(row) {
        row.style.cursor = 'grabbing';
    }

    function resetCursor(row) {
        row.style.cursor = 'pointer'; // Set it to the default cursor
    }

    function openPopup(className) {
        const popup = document.querySelector("." + className);
        const overlay = document.querySelector("." + className + "-overlay");
        if (popup) {
            popup.style.display = "block";
            overlay.style.display = "block";
        }
    }

    function closePopup(className) {
        const popup = document.querySelector("." + className);
        const overlay = document.querySelector("." + className + "-overlay");
        if (popup) {
            popup.style.display = "none";
            overlay.style.display = "none";
        }
    }

    let toPrev  = [];
    let current  = [];
    let toNext  = [];

    function moveRowToNextPage(index, mediaId) {
        toNext.push(mediaId); // Add the row to the toNext array
        const row = $(`#movieTable .nextPage-popup-` + index).closest("tr"); // Get the parent row
        row.hide(); // Hide the row
        closePopup(`nextPage-popup-` + index); // Close the popup
        updateRowIndices();
    }

    function moveRowToPreviousPage(index, mediaId) {
        toNext.push(mediaId); // Add the mediaId to the toNext array
        const row = $(`#movieTable .previousPage-popup-` + index).closest("tr"); // Get the parent row
        row.hide(); // Hide the row
        updateRowIndices();
        closePopup(`previousPage-popup-` + index); // Close the popup
    }

    function updateRowIndices() {
        $("#movieTable tbody tr:visible").each(function (index) {
            let newIndex = (${currentPage} *
            ${pagingSize})
            +(index + 1);
            $(this).find("td.position-td span").text(newIndex);
            $(this).attr("data-index", newIndex - 1);
        });
    }

    function beforeSubmit() {
        // Convert the arrays to comma-separated strings
        $("#movieTable tbody tr:visible").each(function (index) {
            const mediaId = $(this).attr("data-mediaId");
            if (mediaId) {
                current.push(mediaId);
            }
        });

        const toPrevArrayValue = toPrev.join(',');
        const currentArrayValue = current.join(',');
        const toNextArrayValue = toNext.join(',');

        const toPrevArray = toPrevArrayValue.split(',').map(Number);
        const currentArray = currentArrayValue.split(',').map(Number);
        const toNextArray = toNextArrayValue.split(',').map(Number);

        // Set the values of the hidden input fields
        document.getElementById("toPrevArray").value = toPrevArrayValue;
        document.getElementById("currentArray").value = currentArrayValue;
        document.getElementById("toNextArray").value = toNextArrayValue;

        // Submit the form
        return true; // Allow the form to be submitted
    }


</script>
<style>
    .sortable-placeholder {
        border: 2px solid blue; /* Set the desired border style and color */
        background-color: #f0f0f0; /* Set the background color for the placeholder */
        height: 100px; /* Set the height to match your row height */
        margin: 0; /* Reset margin to prevent unwanted spacing */
    }
</style>

</html>
