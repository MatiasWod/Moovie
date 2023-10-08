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

document.addEventListener('DOMContentLoaded', (event) => {
    const radioButtons = document.querySelectorAll('[name="btnradio"]');
    const divs = document.querySelectorAll('#user-lists, #liked-lists, #reviews, #watched-list, #watchlist');

    console.log(divs)

    radioButtons.forEach(radio => {
        radio.addEventListener('change', (event) => {
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

document.addEventListener("DOMContentLoaded", function() {
    const profileImage = document.getElementById("profile-image-big");
    if (profileImage) {
        profileImage.onerror = function() {
            profileImage.src = "${pageContext.request.contextPath}/resources/defaultProfile.jpg";
        }
    }
});

// Get the error message from the alert div
var errorAlert = document.getElementById("errorAlert");

// Check if the error message is not empty
if (errorAlert.textContent.trim() !== "") {
    // Show the error alert
    errorAlert.style.display = "block";
}
