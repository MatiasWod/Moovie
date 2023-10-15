var sortOrder = 'asc';

function changeSortOrder(sortOrderInputId, sortIconId) {
    var sortIcon = document.getElementById(sortIconId);
    var sortOrderInput = document.getElementById(sortOrderInputId);

    if (sortOrderInput.value === 'asc') {
        sortIcon.className = 'bi bi-arrow-down-circle-fill';
        sortOrderInput.value = 'desc'; // Toggle sortOrder to 'desc' when changing to ascending
    } else {
        sortIcon.className = 'bi bi-arrow-up-circle-fill';
        sortOrderInput.value = 'asc'; // Toggle sortOrder to 'asc' when changing to descending
    }


}