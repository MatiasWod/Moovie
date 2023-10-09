let selectedMedia = [];
let selectedMediaId = [];



function fillMedia(id, name){
    console.log('media filled\n')
    selectedMedia.push(name);
    selectedMediaId.push(id);
}

function deleteGenre(element) {
    let aux = document.getElementById("dropdownCheck" + element.previousElementSibling.innerText.trim());
    aux.checked = false;
    element.parentElement.remove();
    beforeSubmit();
    document.getElementById("filter-form").submit();
}

function displayMediaName(name, id) {

    if (!selectedMediaId.includes(id)){
        selectedMedia.push(name);
        selectedMediaId.push(id);
        const selectedMediaDiv = document.getElementById("selected-media-names");
        const newElement = document.createElement('div');
        newElement.id = "list-element-preview";
        newElement.className = "d-flex justify-content-between";
        newElement.innerHTML = '<a>' + name +
            '</a>' +
            '<i class="btn bi bi-trash" onclick="deleteMedia(this)"></i>';
        selectedMediaDiv.appendChild(newElement);
        updateSelectedMediaInput();
    }


}

function updateSelectedMediaInput() {
    const selectedMediaInput = document.getElementById("selected-media-input");
    selectedMediaInput.value = JSON.stringify(selectedMediaId).replaceAll(']','').replaceAll('[','');

    const selectedCreateInput = document.getElementById("selected-create-media");
    selectedCreateInput.value =selectedMediaId.map(Number);
}

function deleteMedia(element) {
    const name = element.previousElementSibling.innerText;
    const index = selectedMedia.indexOf(name);
    if (index !== -1) {
        selectedMedia.splice(index, 1);
        selectedMediaId.splice(index, 1);

    }
    element.parentElement.remove();
    updateSelectedMediaInput();
}

function resetSelectedMediaNames() {
    const selectedMediaDiv = document.getElementById("selected-media-names");
    selectedMediaDiv.innerHTML = '';
    selectedMedia = [];
    selectedMediaId = [];
    updateSelectedMediaInput();
}

function showSelectedMediaList() {
    const selectedMediaList = selectedMedia.join(', ');
    if (selectedMediaList.trim() === '') return;

    const existingList = document.getElementById('list-result');

    if (!existingList) {
        const listDiv = document.createElement('div');
        listDiv.innerHTML = '<div class="d-flex flex-column">' +
            '<h4 id="result-title">Created List: </h4>' +
            '<div style="max-height: 150px" id="list-result" class="container d-flex scrollableMedia">' + selectedMediaList + '</div>' +
            '</div>';
        document.getElementById("preview-list").appendChild(listDiv);
    } else {
        existingList.innerHTML = selectedMediaList;
    }
    resetSelectedMediaNames();
}

window.onload = function() {

    let elems = document.getElementsByClassName("distinct-class");
    console.log(elems[0])
    let j = 0;
    while (elems[j] != null){
        selectedMediaId.push(parseInt(elems[j].id));
        selectedMedia.push(elems[j++].innerHTML);
    }
    updateSelectedMediaInput();

};


function beforeSubmit() {
    const selectedOptions = [];
    document.querySelectorAll('.form-check-input:checked').forEach(function(checkbox) {
        selectedOptions.push(checkbox.nextElementSibling.innerText);
    });

    console.log(selectedOptions)

    document.getElementById('hiddenGenreInput').value = selectedOptions.join(",");
    // mandar todas la media de las lists al url
}

function toggleGenreSelect() {
    const filterTypesSelect = document.getElementById("filter-types");
    const genreSelect = document.getElementById("genre-select");

    if (filterTypesSelect.value === "Genre") {
        genreSelect.style.display = "block";
    } else {
        genreSelect.style.display = "none";
    }
}
