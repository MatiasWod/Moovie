let selectedMedia = [];
let selectedMediaId = [];

function deleteGenre(element) {
    console.log("createList deleteGenre");
    let aux = document.getElementById("dropdownCheck" + element.previousElementSibling.innerText.trim());
    aux.checked = false;
    console.log(aux);
    console.log(aux.innerHTML);
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
    }
}

function updateSelectedMediaInput() {
    const selectedMediaInput = document.getElementById("selected-media-input");
    selectedMediaInput.value = JSON.stringify(selectedMediaId).replaceAll(']','').replaceAll('[','');
    localStorage.setItem("selectedMediaIds",selectedMediaInput.value);
    const selectedCreateInput = document.getElementById("selected-create-media");
    selectedCreateInput.value =selectedMediaId.map(Number);
}

function deleteMedia(element) {
    const name = element.previousElementSibling.innerText;
    const index = selectedMedia.indexOf(name);
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

    console.log(localStorage.getItem("mediaNames"))

    // Obtener el String desde localStorage y convertirlo en un array de números
    const storedMediaIds = localStorage.getItem("selectedMediaIds");
    if (storedMediaIds) {
        const mediaIdArray = storedMediaIds.split(",").map(Number);
        selectedMediaId = [...selectedMediaId, ...mediaIdArray];
    }

    const storedMediaNames = localStorage.getItem("mediaNames");
    if (storedMediaNames) {
        const mediaArray = storedMediaNames.split(",").map(String);
        selectedMedia = [...selectedMedia, ...mediaArray];
    }

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
}

function beforeSubmit() {
    const selectedOptions = [];
    document.querySelectorAll('.form-check-input:checked').forEach(function(checkbox) {
        selectedOptions.push(checkbox.nextElementSibling.innerText);
    });

    console.log(selectedOptions)

    document.getElementById('hiddenGenreInput').value = selectedOptions.join(",");
}
