document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("selected-radio");
    const radios = document.querySelectorAll("input[name='btnradio']");
    const listField = document.getElementById("listField");

    radios.forEach((radio) => {
        radio.addEventListener("change", function() {
            listField.value = radio.id.replace("btnradio-", "");
            form.submit();
        });
    });
});


document.getElementById('sortSelectWatched').addEventListener('change', function () {
    sortTable(this.value,"movieTableWatched");
});
document.getElementById('sortSelectWatchlist').addEventListener('change', function () {
    sortTable(this.value,"movieTableWatchlist");
});

document.addEventListener('DOMContentLoaded', () => {
    const radioButtons = document.querySelectorAll('[name="btnradio"]');
    const divs = document.querySelectorAll('#user-lists,#user-private-lists, #liked-lists, #reviews, #watched-list, #watchlist');

    console.log(divs)

    radioButtons.forEach(radio => {
        radio.addEventListener('change', () => {
            divs.forEach(div => {
                console.log(div.id)
                if (div.id === radio.id.replace('btnradio-', '')) {
                    div.style.display = 'block';
                } else {
                    div.style.display = 'none';
                }
            });
        });
    });
});

setTimeout(function() {
    document.getElementById('errorAlert').style.display = 'none';
}, 5000);

function previewImage(input) {
    let preview = document.querySelector('.profile-image-preview');
    let file = input.files[0];
    let reader = new FileReader();
    reader.onload = function() {
        preview.src = reader.result;
    }
    if (file) {
        reader.readAsDataURL(file);
    }
}


// Get the error message from the alert div
var errorAlert = document.getElementById("errorAlert");

// Check if the error message is not empty
if (errorAlert.textContent.trim() !== "") {
    // Show the error alert
    errorAlert.style.display = "block";
}
