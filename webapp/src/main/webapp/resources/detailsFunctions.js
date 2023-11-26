const popoverTriggerList = document.querySelectorAll('[data-bs-toggle="popover"]')
const popoverList = [...popoverTriggerList].map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl))

const cardsNumber = 4;
window.onload = function () {
    const actorCards = document.querySelectorAll(".actor-card");

    // Loop through all actor cards and toggle their display
    for (let i = 0; i < cardsNumber; i++) {
        actorCards[i].style.display = "block";
    }
};

function showMoreActors() {
    const actorCards = document.querySelectorAll(".actor-card");

    // Loop through all actor cards and toggle their display
    for (let i = cardsNumber; i < actorCards.length; i++) {
        actorCards[i].style.display = "block";

    }

    // Scroll to the middle of the actors container
    const actorsContainer = document.querySelector("#actors-container");
    actorsContainer.scrollIntoView({behavior: "smooth", block: "center"});


    // Change "See More" button to "See Less"
    const seeMoreButton = document.querySelector(".show-more-button");
    seeMoreButton.innerHTML = "See less";
    seeMoreButton.onclick = showLessActors;
}

function showLessActors() {
    const actorCards = document.querySelectorAll(".actor-card");

    // Loop through all actor cards and toggle their display
    for (let i = cardsNumber; i < actorCards.length; i++) {
        actorCards[i].style.display = "none";
    }

    // Change "See Less" button to "See More"
    const seeMoreButton = document.querySelector(".show-more-button");
    seeMoreButton.innerHTML = "See more";
    seeMoreButton.onclick = showMoreActors;
}

function openPopup(className) {
    const popup = document.querySelector("." + className);
    const overlay = document.querySelector("."+className+"-overlay");
    if (popup) {
        popup.style.display = "block";
        overlay.style.display = "block";
    }
}

function closePopup(className) {
    const popup = document.querySelector("." + className);
    const overlay = document.querySelector("."+className+"-overlay");
    if (popup) {
        popup.style.display = "none";
        overlay.style.display = "none";
    }
}

let selectedRating = 0;
let stars = document.querySelectorAll('.rating > i');

function rate(starsClicked) {
    selectedRating = starsClicked;
    // Remove 'bi-star' class and add 'bi-star-fill' class for selected stars
    stars.forEach(function (star, index) {
        if (index >= (5 - starsClicked)) {
            star.classList.remove('bi-star');
            star.classList.add('bi-star-fill');
            star.classList.add('selected')
        } else {
            star.classList.remove('bi-star-fill');
            star.classList.remove('selected')
            star.classList.add('bi-star');
        }
    });
    document.getElementById("rating").value = selectedRating;
    document.getElementById("selectedRating").textContent = selectedRating;
    document.getElementById("submitRating").disabled = false;
}

function rate2(starsClicked) {
    selectedRating = starsClicked;
    // Remove 'bi-star' class and add 'bi-star-fill' class for selected stars
    stars.forEach(function (star, index) {
        if (index >= (10-starsClicked)) {
            star.classList.remove('bi-star');
            star.classList.add('bi-star-fill');
            star.classList.add('selected')
        } else {
            star.classList.remove('bi-star-fill');
            star.classList.remove('selected')
            star.classList.add('bi-star');
        }
    });
    document.getElementById("rating").value = selectedRating;
    document.getElementById("selectedRatingEdit").textContent = selectedRating;
}

// Function to format a number with commas and dots and add a dollar sign
function formatNumberWithDollarSign(number) {
    return '$' + number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");

}

// Function to format revenue and budget elements
function formatRevenueAndBudget() {
    const revenueElement = document.getElementById("revenue");
    const budgetElement = document.getElementById("budget");

    formatElementValue(revenueElement);
    formatElementValue(budgetElement);
}

// Function to format a specific element's value
function formatElementValue(element) {
    if (element) {
        const elementValue = parseFloat(element.textContent);
        if (!isNaN(elementValue)) {
            element.textContent = formatNumberWithDollarSign(elementValue);
        }
    }
}

function setCreateListContent(name,id){

    let selectedMediaId = [];
    let selectedMedia = [];

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

    if (!selectedMediaId.includes(id)){

        selectedMedia.push(name.replaceAll(",",""));
        selectedMediaId.push(id);

        console.log(selectedMedia)

        localStorage.setItem("mediaNames",JSON.stringify(selectedMedia).replaceAll('"','').replaceAll(']','').replaceAll('[',''))
        localStorage.setItem("selectedMediaIds",JSON.stringify(selectedMediaId).replaceAll(']','').replaceAll('[',''))

    }
}

// Call the function to format revenue and budget when the page loads
window.addEventListener("load", formatRevenueAndBudget)
{

    const textarea = document.getElementById("reviewContent");
    const textarea2 = document.getElementById("reviewContent2");

    const charCount = document.getElementById("charCount");
    const charCount2 = document.getElementById("charCount2");

    if (textarea) {
        textarea.addEventListener("input", function () {
            const remainingChars = textarea.value.length;
            charCount.textContent = `${remainingChars}`;

            if (remainingChars < 0) {
                charCount.style.color = "red";
                document.getElementById("submitButton").disabled = true;
            } else {
                charCount.style.color = "inherit";
                document.getElementById("submitButton").disabled = false;
            }

            // Remove line breaks from the textarea

            textarea.value = textarea.value.replace(/\n/g, "");
        });
    }
    if(textarea2){
        textarea2.addEventListener("input", function () {
            const remainingChars = textarea2.value.length;
            charCount2.textContent = `${remainingChars}`;

            if (remainingChars < 0) {
                charCount2.style.color = "red";
                document.getElementById("submitButton").disabled = true;
            } else {
                charCount2.style.color = "inherit";
                document.getElementById("submitButton").disabled = false;
            }

            // Remove line breaks from the textarea

            textarea2.value = textarea2.value.replace(/\n/g, "");
        });
    }
}

function submitFirstForm() {
    document.getElementById('reviewContent').value = document.getElementById('reviewContent2').value;
    document.getElementById('rating').value = document.getElementById('selectedRatingEdit').innerText;
    document.getElementById('rateForm').submit();
}

