let selectedMedia = [];
let selectedMediaId = [];

function deleteChip(element) {
    let aux = document.getElementById("dropdownCheck" + element.previousElementSibling.innerText.trim());
    aux.checked = false;
    element.parentElement.remove();
    beforeSubmit();
    document.getElementById("filter-form").submit();
}



function displayMediaName(name, id) {

    if (!selectedMediaId.includes(id)){
        selectedMedia.push(name.replaceAll(",",""));
        selectedMediaId.push(id);
        console.log(selectedMedia)

        localStorage.setItem("mediaNames",JSON.stringify(selectedMedia).replaceAll('"','').replaceAll(']','').replaceAll('[',''))

        console.log(localStorage.getItem("mediaNames"))

        const selectedMediaDiv = document.getElementById("selected-media-names");
        const newElement = document.createElement('div');
        newElement.id = "list-element-preview";
        newElement.className = "d-flex other-distinct justify-content-between";
        newElement.innerHTML = '<a>' + name +
            '</a>' +
            '<i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>';
        selectedMediaDiv.appendChild(newElement);

        const poster = document.getElementById(id);
        if (poster){
            const checkedElem = document.createElement('h4');
            checkedElem.className = 'special-checked-indicator-class';
            checkedElem.id = 'checked-indicator-' + id;
            checkedElem.innerHTML = '<i style="position: absolute; right: 0; color: green; z-index: 2" class="m-2 bi bi-check-circle-fill"></i>';
            poster.insertBefore(checkedElem, poster.firstChild);

        }

        updateSelectedMediaInput();
    }
}

function displayAllMediaNames(){

    for(let i = 0; i < selectedMedia.length ; i++){
        let name = selectedMedia[i]
        let id = selectedMediaId[i]
        const selectedMediaDiv = document.getElementById("selected-media-names");
        const newElement = document.createElement('div');
        newElement.id = "list-element-preview";
        newElement.className = "d-flex other-distinct justify-content-between";
        newElement.innerHTML = '<a>' + name +
            '</a>' +
            '<i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>';
        selectedMediaDiv.appendChild(newElement);

        // <h4>
        //     <i style="position: absolute; right: 0; color: green; z-index: 2" className="m-2 bi bi-check-circle-fill"></i>
        // </h4>
        const poster = document.getElementById(selectedMediaId[i]);
        if (poster){
            const checkedElem = document.createElement('h4');
            checkedElem.className = 'special-checked-indicator-class';
            checkedElem.id = 'checked-indicator-' + selectedMediaId[i];
            checkedElem.innerHTML = '<i style="position: absolute; right: 0; color: green; z-index: 2" class="m-2 bi bi-check-circle-fill"></i>';
            poster.insertBefore(checkedElem, poster.firstChild);

        }
    }

}

function updateSelectedMediaInput() {
    const selectedMediaInput = document.getElementById("selected-media-input");
    // selectedMediaInput.value = JSON.stringify(selectedMediaId).replaceAll(']','').replaceAll('[','');
    selectedMediaInput.value = selectedMediaId.map(Number);
    localStorage.setItem("selectedMediaIds",selectedMediaInput.value);
    const selectedCreateInput = document.getElementById("selected-create-media");
    selectedCreateInput.value =selectedMediaId.map(Number);
}

function deleteMedia(element) {
    const name = element.previousElementSibling.innerText;
    const index = selectedMedia.indexOf(name);

    const posterCheck = document.getElementById('checked-indicator-'+selectedMediaId[index])
    posterCheck.remove();

    if (index !== -1) {
        selectedMedia.splice(index, 1);
        selectedMediaId.splice(index, 1);
        localStorage.setItem("mediaNames",JSON.stringify(selectedMedia).replaceAll('"','').replaceAll(']','').replaceAll('[',''))
        localStorage.setItem("selectedMediaIds",JSON.stringify(selectedMediaId).replaceAll(']','').replaceAll('[',''))

    }
    element.parentElement.remove();



    updateSelectedMediaInput();
}

window.onload = function() {
    let elems = document.getElementsByClassName("distinct-class");
    let j = 0;
    while (elems[j] != null){
        selectedMediaId.push(parseInt(elems[j].id));
        selectedMedia.push(elems[j++].innerHTML);
    }
    console.log("onload mediaNames localStorage: "+localStorage.getItem("selectedMediaIds"))
    console.log("onload mediaNames localStorage: "+localStorage.getItem("mediaNames"))

    // Obtener el String desde localStorage y convertirlo en un array de números
    const storedMediaIds = localStorage.getItem("selectedMediaIds");
    if (storedMediaIds) {
        const mediaIdArray = storedMediaIds.replaceAll("\"","").split(",").map(Number);
        selectedMediaId = [...selectedMediaId, ...mediaIdArray];
    }

    const storedMediaNames = localStorage.getItem("mediaNames");
    if (storedMediaNames) {
        const mediaArray = storedMediaNames.split(",").map(String);
        selectedMedia = [...selectedMedia, ...mediaArray];
    }

    console.log("onload selectedMediaId: "+selectedMediaId)
    console.log("onload selectedMedia: "+selectedMedia)

    updateSelectedMediaInput();
    displayAllMediaNames();
};

document.addEventListener("DOMContentLoaded", function() {
    if(localStorage.getItem('formSubmitted')){
        deleteStorage();
        localStorage.removeItem('formSubmitted');
        console.log('storageDeleted');
    }

    const storedTitleValue = localStorage.getItem("titleValue");
    const titleInput = document.getElementById("list-name");
    if (storedTitleValue) {
        titleInput.value = storedTitleValue;
    }
    titleInput.addEventListener("input", function() {
        localStorage.setItem("titleValue", titleInput.value);
    });

    const storedDescriptionValue = localStorage.getItem("descriptionValue");
    const descriptionInput = document.getElementById("list-description");
    if (storedDescriptionValue) {
        descriptionInput.value = storedDescriptionValue;
    }
    descriptionInput.addEventListener("input", function() {
        localStorage.setItem("descriptionValue", descriptionInput.value);
    });

    const form = document.getElementById('create-form');
    form.addEventListener('submit', function(e) {
        localStorage.setItem('formSubmitted', 'true');
    });

    const toggleButton = document.getElementById('toggle-button');
    const toggleIcon = document.getElementById('toggle-icon');
    const toggleStateInput = document.getElementById('toggle-state');

    toggleButton.addEventListener("click", function() {
        console.log("click!")
        let state = !(toggleStateInput.getAttribute('value') === 'true')

        toggleStateInput.setAttribute('value',state.toString())
        toggleIcon.className = state ? 'bi bi-eye-fill' : 'bi bi-eye-slash-fill';
    });

//     SEARCH FUNCTIONALITY
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

function deleteStorage() {
    localStorage.removeItem("titleValue")
    const titleInput = document.getElementById("list-name");
    titleInput.value = ""
    localStorage.removeItem("descriptionValue")
    const descriptionInput = document.getElementById("list-description");
    descriptionInput.value = ""
    const mediaNamesInputs = document.getElementsByClassName("other-distinct");
    let i = 0;
    while (mediaNamesInputs[i] != null){
        mediaNamesInputs[i].remove()
    }
    console.log(mediaNamesInputs)
    localStorage.removeItem("mediaNames")
    localStorage.removeItem("selectedMediaIds")

    selectedMedia = [];
    selectedMediaId = [];

    const formIdInput = document.getElementById("selected-create-media")
    formIdInput.value = null;
    const aux = document.querySelectorAll('.special-checked-indicator-class')
    aux.forEach(elem => {
        elem.remove();
    })

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
