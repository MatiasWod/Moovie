function liked() {
    let button = document.getElementById("likeButton");
    if (!button.classList.contains("liked")) {
        button.classList.add("liked");
        button.innerHTML = "<i class=\"bi bi-hand-thumbs-up-fill\"></i> Liked";
    } else {
        button.classList.remove("liked");
        button.innerHTML = "<i class=\"bi bi-hand-thumbs-up\"></i> Like";
    }
}

function roundUpProgress() {
    const progressBar = document.getElementById("progressBar");
    if (progressBar) {
        const currentWidth = parseFloat(progressBar.style.width);
        const roundedWidth = Math.ceil(currentWidth);
        progressBar.style.width = roundedWidth + "%";
        progressBar.setAttribute("aria-valuenow", String(roundedWidth));
        progressBar.textContent = roundedWidth + "%";
    }
}

// Call the roundUpProgress function on page load

window.onload = function (){
    roundUpProgress();
}

const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]')
const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl))

function openPopup(className) {
    const popup = document.querySelector("." + className);
    const overlay = document.querySelector("." + className + "-overlay");
    if (popup) {
        popup.style.display = "block";
        overlay.style.display = "block";
    }
}

function closePopup(className) {
    const popup = document.querySelector("." + className);
    const overlay = document.querySelector("." + className + "-overlay");
    if (popup) {
        popup.style.display = "none";
        overlay.style.display = "none";
    }
}