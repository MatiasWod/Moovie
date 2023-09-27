var sortOrder = 'asc';

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
            } else if (sortBy === 'release date') {
                x = Date.parse(x);
                y = Date.parse(y);
            }

            // Check the sorting order and compare accordingly
            if (sortOrder === 'asc') {
                if (x > y) {
                    shouldSwitch = true;
                    break;
                }
            } else {
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

function changeSortOrder() {
    sortOrder = (sortOrder === 'asc') ? 'desc' : 'asc';

    // Get the currently selected column for sorting
    var selectedColumn = document.getElementById('sortSelect').value;

    var sortIcon = document.getElementById('sortIcon');
    if (sortOrder === 'asc') {
        sortIcon.className = 'bi bi-arrow-up-circle-fill';
    } else {
        sortIcon.className = 'bi bi-arrow-down-circle-fill';
    }
    // Call the sortTable function with the selected column
    sortTable(selectedColumn);
}