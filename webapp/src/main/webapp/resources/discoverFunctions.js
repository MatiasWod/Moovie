window.onload = function() {
    const filterTypesSelect = document.getElementById("filter-types");
    const genreSelect = document.getElementById("genre-select");

    if (filterTypesSelect.value === "Genre") {
        genreSelect.style.display = "block";
    }
};
function beforeSubmit() {
    const filterTypesSelect = document.getElementById("filter-types");
    const genreSelect = document.getElementById("genre-select");

    if (filterTypesSelect.value === "Popular") {
        genreSelect.removeAttribute("name");
    }
};

function loadPreview(title, rating, posterPath, overview, adult, id, year) {
    document.getElementById("preview").style.display = 'block';
    document.getElementById("preview-title").innerText = title;
    document.getElementById("preview-rating").innerText = rating;
    document.getElementById("preview-img").src = posterPath;
    document.getElementById("preview-synopsis").innerText = overview;
    document.getElementById("preview-details").href = 'details/' + String(id);
    var yearSubstring = year.split('-')[0];
    document.getElementById("preview-year").innerText = yearSubstring;
    if (adult == 'true'){
        document.getElementById("preview-explicit").style.display = 'block';
    }
};
function toggleGenreSelect() {
    const filterTypesSelect = document.getElementById("filter-types");
    const genreSelect = document.getElementById("genre-select");

    if (filterTypesSelect.value === "Genre") {
        genreSelect.style.display = "block";
    } else {
        genreSelect.style.display = "none";
    }
};