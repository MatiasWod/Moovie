const initialCardsNumber = 4;
window.onload = function () {
    const actorCards = document.querySelectorAll(".actor-card");

    // Loop through all actor cards and toggle their display
    for (let i = 0; i < initialCardsNumber; i++) {
        actorCards[i].style.display = "block";
    }
};

function showMoreActors() {
    const actorCards = document.querySelectorAll(".actor-card");

    // Loop through all actor cards and toggle their display
    for (let i = initialCardsNumber; i < actorCards.length; i++) {
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
    for (let i = initialCardsNumber; i < actorCards.length; i++) {
        actorCards[i].style.display = "none";
    }

    // Change "See Less" button to "See More"
    const seeMoreButton = document.querySelector(".show-more-button");
    seeMoreButton.innerHTML = "See more";
    seeMoreButton.onclick = showMoreActors;
}

function openReviewPopup() {
    const overlay = document.querySelector(".popup-overlay");
    const popup = document.querySelector(".popup");

    overlay.style.display = "block";
    popup.style.display = "block";
}

function closeReviewPopup() {
    const overlay = document.querySelector(".popup-overlay");
    const popup = document.querySelector(".popup");

    overlay.style.display = "none";
    popup.style.display = "none";
}

let selectedRating = 0;
let stars = document.querySelectorAll('.rating > i');

function rate(starsClicked) {
    selectedRating = starsClicked;

    // Remove 'bi-star' class and add 'bi-star-fill' class for selected stars
    stars.forEach(function (star, index) {
        if (index >= (10 - starsClicked)) {
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
    document.getElementById("submitButton").disabled = false;
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

// Call the function to format revenue and budget when the page loads
window.addEventListener("load", formatRevenueAndBudget);

const textarea = document.getElementById("reviewContent");
const charCount = document.getElementById("charCount");

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