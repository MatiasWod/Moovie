const listNameTextarea = document.getElementById("list-name");
const listNameCharCount = document.getElementById("listNameRemainingChars");
const listDescriptionTextarea = document.querySelector("textarea[name='listDescription']");
const listDescriptionCharCount = document.getElementById("listDescriptionRemainingChars");

listNameTextarea.addEventListener("input", function () {
    const remainingChars = listNameTextarea.value.length;
    listNameCharCount.textContent = remainingChars;

    if (remainingChars < 0) {
        listNameCharCount.style.color = "red";
    } else {
        listNameCharCount.style.color = "inherit";
    }
});

listDescriptionTextarea.addEventListener("input", function () {
    const remainingChars = listDescriptionTextarea.value.length;
    listDescriptionCharCount.textContent = remainingChars;

    if (remainingChars < 0) {
        listDescriptionCharCount.style.color = "red";
    } else {
        listDescriptionCharCount.style.color = "inherit";
    }
});