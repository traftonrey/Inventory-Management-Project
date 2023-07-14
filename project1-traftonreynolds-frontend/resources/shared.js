// shared.js

// Function to clear a form
function clearForm(formId) {
    const form = document.getElementById(formId + "-form");
    form.reset();
}

// Function to dismiss a modal
function dismissModal(modalId) {
    const modalElement = document.getElementById(modalId + "Modal");
    const modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
}

// Function to show a success message
function showSuccessMessage(message) {
    const successMessage = document.getElementById("success-message");
    if (successMessage) {
        successMessage.innerText = message;
        successMessage.style.display = "block";
        setTimeout(() => {
            successMessage.style.opacity = 0;
            setTimeout(() => {
                successMessage.style.display = "none";
                successMessage.style.opacity = 1;
            }, 500);
        }, 3000);
    }
}

async function refreshTable(url, tableBodyId, addFunction) {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let items = JSON.parse(xhr.responseText);

            // Clear the table body
            let tableBody = document.getElementById(tableBodyId);
            tableBody.innerHTML = "";

            // populate the table
            items.forEach((newItem) => {
                addFunction(newItem);
            });
        }
    };

    xhr.open("GET", url);
    xhr.send();
}
