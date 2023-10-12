window.onload = function() {
    localStorage.removeItem("searchValue");
};
function deleteGenre(element) {
    let aux = document.getElementById("dropdownCheck" + element.previousElementSibling.innerText.trim());
    aux.checked = false;
    element.parentElement.remove();
    beforeSubmit();
    document.getElementById("filter-form").submit();
}

function beforeSubmit() {
    const selectedOptions = [];
    document.querySelectorAll('.form-check-input:checked').forEach(function(checkbox) {
        selectedOptions.push(checkbox.nextElementSibling.innerText);
    });


    document.getElementById('hiddenGenreInput').value = selectedOptions.join(",");
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