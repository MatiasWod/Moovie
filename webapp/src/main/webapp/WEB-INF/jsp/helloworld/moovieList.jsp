<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/logo.png" />

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <link href="${pageContext.request.contextPath}/resources/main.css?version=55" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/resources/discoverFunctions.js?version=81"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
            crossorigin="anonymous"></script>
    <title>Moovie List</title>
</head>
<style>
    .header {
        display: flex;
        flex-direction: column;
        justify-content: center;
        color: white;
        background-color: #333;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
        margin-bottom: 10px;
    }


    .table td {
        padding: 0; /* Reduce padding for the second column */
        vertical-align: middle; /* Center the content vertically */
        font-size: x-large;
        text-align: center;
    }

    .table td:nth-child(2) {
        text-align: left; /* Left-align the second column Title */
    }

    .table thead {
        font-size: x-large;
        text-align: center;
    }

    .buttons {
        flex-direction: row;
        margin-bottom: 10px;
        margin-top: 10px;
    }

    .btn-style {
        color: white;
        background-color: #198754;
    }

    .btn-style:hover {
        color: white;
        background-color: #333333;
        border-style: solid;
    }

</style>
<body style="background: whitesmoke">
<c:import url="navBar.jsp"/>
<div class="container d-flex flex-column">
    <div class="container header ">
        <div class="text-center ">
            <h1 style="font-size: 60px; font-weight: bold;"><c:out value="${moovieList.name}"/></h1>
            <h3><c:out value="${moovieList.description}"/></h3>
            <h4 style="color: lightgray;">by <c:out value="${listOwner}"/></h4>
        </div>
    </div>
    <div class="buttons">
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <button type="button" class="btn btn-style" ><i class="bi bi-hand-thumbs-up"></i> Like</button>
            </div>
            <div style="display: flex; align-items: center;">
                <h2 style="padding-right: 4px">Sort by</h2>
                <select name="media" class="form-select filter-width" aria-label="Filter!" id="sortSelect">
                    <option value="title">Title</option>
                    <option value="type">Type</option>
                    <option value="score">Score</option>
                    <option value="release date">Release Date</option>
                </select>
            </div>
        </div>
    </div>
    <table class="table table-striped" id="movieTable">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Title</th>
            <th scope="col">Type</th>
            <th scope="col">Score</th>
            <th scope="col">Release Date</th>
        </tr>
        </thead>
        <c:choose>
            <c:when test="${not empty moovieListContent}">
                <tbody>
                <c:forEach var="index" items="${moovieListContent}" varStatus="loop">
                    <tr>
                        <!-- Index -->
                        <td style="text-align: center">${loop.index + 1}</td>
                        <!-- Title -->
                        <td>
                            <div class="row align-items-center">
                                <div class="col-auto">
                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                       style="text-decoration: none; color: inherit;">
                                        <img src="${mediaList[loop.index].posterPath}" class="img-fluid" width="100"
                                             height="100" alt="${mediaList[loop.index].name} poster"/>
                                    </a>
                                </div>
                                <div class="col">
                                    <a href="${pageContext.request.contextPath}/details/${mediaList[loop.index].mediaId}"
                                       style="text-decoration: none; color: inherit;">
                                        <strong>${mediaList[loop.index].name}</strong>
                                    </a>
                                </div>
                            </div>
                        </td>
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
</body>
</html>

<script>
    document.getElementById('sortSelect').addEventListener('change', function () {
        sortTable(this.value);
    });

    function sortTable(sortBy) {
        var table, rows, switching, i, shouldSwitch;
        table = document.getElementById("movieTable");
        switching = true;
        while (switching) {
            switching = false;
            rows = table.rows;
            for (i = 1; i < (rows.length - 1); i++) {
                shouldSwitch = false;

                // Get the data to compare based on the selected column name (sortBy)
                var dataIndex = getIndex(sortBy);
                var cellX = rows[i].cells[dataIndex];
                var cellY = rows[i + 1].cells[dataIndex];

                // Get the values from the cells
                var x = getCellValue(cellX);
                var y = getCellValue(cellY);

                // Convert values to numbers for numeric columns
                if (sortBy === 'score') {
                    x = parseFloat(x);
                    y = parseFloat(y);
                }

                // Compare dates for the "releaseDate" column
                if (sortBy === 'releaseDate') {
                    var dateX = new Date(x);
                    var dateY = new Date(y);
                    if (dateX && dateY) {
                        if (dateX.getFullYear() < dateY.getFullYear()) { // Compare using getTime() method
                            shouldSwitch = true;
                            break;
                        }
                    }
                }

                // Compare in descending order (highest value first)
                if (sortBy === 'title' || sortBy === 'type') {
                    if (x > y) {
                        shouldSwitch = true;
                        break;
                    }
                } else {
                    // For other columns, compare in ascending order (lowest value first)
                    if (x < y) {
                        shouldSwitch = true;
                        break;
                    }
                }
            }
            if (shouldSwitch) {
                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                switching = true;
            } else {
                // If no switching has been done, the table is sorted
                break;
            }
        }
        updateRowNumbers(table);
    }
    function updateRowNumbers(table) {
        var rows = table.rows;
        for (var i = 1; i < rows.length; i++) {
            rows[i].cells[0].textContent = i; // Update the first cell (row number)
        }
    }

    function getIndex(headerText) {
        var headers = document.querySelectorAll("#movieTable th");
        for (var i = 0; i < headers.length; i++) {
            if (headers[i].textContent.trim().toLowerCase() === headerText) {
                return i;
            }
        }
        return -1;
    }

    function getCellValue(cell) {
        if (cell) {
            var cellText = cell.textContent || cell.innerText;
            return cellText.trim();
        } else {
            return "";
        }
    }


</script>