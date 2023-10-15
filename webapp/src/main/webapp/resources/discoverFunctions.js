window.onload = function() {
    localStorage.removeItem("searchValue");
};
function deleteGenre(element) {
    console.log("discover deleteGenre");
    document.querySelectorAll('.form-check-input:checked').forEach(function(checkbox) {
        console.log('checkbox elem: '+checkbox.id);
    });
    let aux = document.getElementById("dropdownCheck" + element.previousElementSibling.innerText.trim());
    console.log(aux.checked);
    aux.checked = false;
    console.log(aux);
    console.log(aux.id);
    console.log(aux.checked);
    document.querySelectorAll('.form-check-input:checked').forEach(function(checkbox) {
        console.log('checkbox elem: '+checkbox.id);
    });
    element.parentElement.remove();
    beforeSubmit();
    document.getElementById("filter-form").submit();
}

function beforeSubmit() {
    console.log('running beforeSubmit');

    const selectedOptions = [];
    document.querySelectorAll('.special-genre-input:checked').forEach(function(checkbox) {
        console.log('checkbox elem: '+checkbox.id);
        selectedOptions.push(checkbox.nextElementSibling.innerText);
    });
    document.getElementById('hiddenGenreInput').value = selectedOptions.join(",");

    const selectedProviders = [];
    document.querySelectorAll('.special-provider-input:checked').forEach(function(checkbox) {
        console.log('checkbox elem: '+checkbox.id);
        selectedProviders.push(checkbox.nextElementSibling.innerText);
    });
    document.getElementById('hiddenProviderInput').value = selectedProviders.join(",");
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

document.addEventListener("DOMContentLoaded", function() {

    function addSearchFunctionality(searchBoxId, formCheckClass) {
        var searchBox = document.getElementById(searchBoxId);
        searchBox.addEventListener("keyup", function() {
            var value = searchBox.value.toLowerCase();
            var formChecks = document.querySelectorAll(formCheckClass);

            formChecks.forEach(function(formCheck) {
                var label = formCheck.querySelector("label").textContent.toLowerCase();
                if (label.indexOf(value) > -1) {
                    formCheck.style.display = "";
                } else {
                    formCheck.style.display = "none";
                }
            });
        });
    }

    // Añadir funcionalidad de búsqueda al campo de géneros
    addSearchFunctionality("searchBoxGenre", ".special-genre-class");

    // Añadir funcionalidad de búsqueda al campo de proveedores
    addSearchFunctionality("searchBoxProvider", ".special-provider-class");
});

