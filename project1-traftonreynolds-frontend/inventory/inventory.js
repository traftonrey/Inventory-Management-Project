const URL = "http://localhost:8080/inventory/";
const tableBodyId = "inventory-table-body";

let allInventory = [];

let isBookIdSortedAscending = true;
let isWarehouseIdSortedAscending = true;
let isBookSortedAscending = true;
let isWarehouseSortedAscending = true;
let isQuantitySortedAscending = true;

document.addEventListener("DOMContentLoaded", () => {
    refreshTable(URL, tableBodyId, addInventory);
});

function addInventory(newInventory) {
    let tr = document.createElement("tr");

    let bookId = document.createElement("td");
    let warehouseId = document.createElement("td");
    let book = document.createElement("td");
    let warehouse = document.createElement("td");
    let quantity = document.createElement("td");

    let editBtn = document.createElement("td");

    bookId.innerText = newInventory.id.bookId;
    warehouseId.innerText = newInventory.id.warehouseId;
    book.innerText = newInventory.book.title;
    warehouse.innerText = newInventory.warehouse.warehouseName;
    quantity.innerText = newInventory.quantity;

    editBtn.innerHTML = `<button class='btn btn-primary' onclick='openEditModal(${newInventory.id.bookId}, ${newInventory.id.warehouseId})'>Edit</button>`;

    tr.appendChild(bookId);
    tr.appendChild(warehouseId);
    tr.appendChild(book);
    tr.appendChild(warehouse);
    tr.appendChild(quantity);

    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newInventory.id);
    tr.setAttribute("class", "inventory-row");

    document.getElementById("inventory-table-body").appendChild(tr);

    allInventory.push(newInventory);

};

async function createInventory() {
    // Validate the form data
    if (!validateForm("create")) {
        return;
    }

    // Retrieve the values from the input fields
    const bookId = document.getElementById("create-book-id").value;
    const warehouseId = document.getElementById("create-warehouse-id").value;
    const quantity = document.getElementById("create-quantity").value;

    const url = `${URL}?bookId=${bookId}&warehouseId=${warehouseId}&quantity=${quantity}`;

    try {
        // Send a POST request to create a new inventory item
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            // Inventory successfully created
            await refreshTable(URL, tableBodyId, addInventory);
            clearForm("add");
            dismissModal("addInventory");
            showSuccessMessage("Inventory successfully created!");
        }
    } catch (error) {
        console.log('Error creating inventory:', error);
    }
}

async function updateInventory() {
    // Validate the form data
    if (!validateForm("edit")) {
        return;
    }

    // Retrieve the values from the input fields
    const bookId = document.getElementById("edit-book-id").value;
    const warehouseId = document.getElementById("edit-warehouse-id").value;

    const quantity = document.getElementById("edit-quantity").value;

    const url = URL + bookId + "/" + warehouseId + "/" + quantity;

    try {
        // Send a PUT request to update the inventory item
        const response = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            // Inventory successfully updated
            console.log("Inventory successfully updated");

            await refreshTable(URL, tableBodyId, addInventory);
            clearForm("edit");
            dismissModal("edit");
            showSuccessMessage("Inventory successfully updated!");
        }
    } catch (error) {
        console.log('Error updating inventory:', error);
    }
};

async function deleteInventory() {
    try {
        // Retrieve the book and warehouse IDs from the input fields
        const bookId = document.getElementById("edit-book-id").value;
        const warehouseId = document.getElementById("edit-warehouse-id").value;

        // Send a DELETE request to delete the inventory item
        const response = await fetch(`${URL}${bookId}/${warehouseId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
        });

        if (response.ok) {
            // Inventory successfully deleted
            console.log("Inventory successfully deleted");

            await refreshTable(URL, tableBodyId, addInventory);
            clearForm("edit");
            dismissModal("edit");
            showSuccessMessage("Inventory successfully deleted!");
        }
    } catch (error) {
        console.log('Error deleting inventory:', error);
    }
};

async function openEditModal(bookId, warehouseId) {
    try {
        // fetch the latest inventory data from the server
        const response = await fetch(`${URL}${Number(bookId)}/${Number(warehouseId)}`);

        if (response.ok) {
            // Inventory successfully retrieved
            const inventory = await response.json();

            // Populate the input fields with the updated inventory data
            document.getElementById("edit-book-id").value = inventory.id.bookId;
            document.getElementById("edit-warehouse-id").value = inventory.id.warehouseId;
            document.getElementById("edit-quantity").value = inventory.quantity;
            document.getElementById("edit-book-bookName").value = inventory.book.title;
            document.getElementById("edit-warehouse-warehouseName").value = inventory.warehouse.warehouseName;

            // Open the edit modal
            const editModal = new bootstrap.Modal(document.getElementById("editModal"));
            editModal.show();
        } else {
            console.error('Error retrieving inventory:', response.status);
        }
    } catch (error) {
        console.log('Error opening edit modal:', error);
    }
};

function validateForm(formId) {
    // Get the values of the input fields
    const bookId = document.getElementById(formId + "-book-id").value;
    const warehouseId = document.getElementById(formId + "-warehouse-id").value;
    const quantity = document.getElementById(formId + "-quantity").value;

    // Check if any of the input fields are empty or only contain whitespace
    if (bookId.trim() === "" || warehouseId.trim() === "" || quantity.trim() === "") {
        alert("Please fill out all fields!");
        return false;
    }

    if (quantity < 0) {
        alert("Quantity cannot be negative!");
        return false;
    }

    return true;
}

function sortTableBy(columnName) {
    let tableBody = document.getElementById("inventory-table-body");
    let rows = Array.from(tableBody.getElementsByTagName("tr"));

    if (columnName === "bookId") {
        isBookIdSortedAscending = !isBookIdSortedAscending;

        rows.sort((rowA, rowB) => {
            let bookIdA = parseInt(rowA.getElementsByTagName("td")[0].innerText);
            let bookIdB = parseInt(rowB.getElementsByTagName("td")[0].innerText);

            if (isBookIdSortedAscending) {
                return bookIdA - bookIdB;
            } else {
                return bookIdB - bookIdA;
            }
        });
    } else if (columnName === "warehouseId") {
        isWarehouseIdSortedAscending = !isWarehouseIdSortedAscending;

        rows.sort((rowA, rowB) => {
            let warehouseIdA = parseInt(rowA.getElementsByTagName("td")[1].innerText);
            let warehouseIdB = parseInt(rowB.getElementsByTagName("td")[1].innerText);

            if (isWarehouseIdSortedAscending) {
                return warehouseIdA - warehouseIdB;
            } else {
                return warehouseIdB - warehouseIdA;
            }
        });
    } else if (columnName === "book") {
        isBookSortedAscending = !isBookSortedAscending;

        rows.sort((rowA, rowB) => {
            let bookA = rowA.getElementsByTagName("td")[2].innerText;
            let bookB = rowB.getElementsByTagName("td")[2].innerText;

            if (isBookSortedAscending) {
                return bookA.localeCompare(bookB);
            } else {
                return bookB.localeCompare(bookA);
            }
        });
    } else if (columnName === "warehouse") {
        isWarehouseSortedAscending = !isWarehouseSortedAscending;

        rows.sort((rowA, rowB) => {
            let warehouseA = rowA.getElementsByTagName("td")[3].innerText;
            let warehouseB = rowB.getElementsByTagName("td")[3].innerText;

            if (isWarehouseSortedAscending) {
                return warehouseA.localeCompare(warehouseB);
            } else {
                return warehouseB.localeCompare(warehouseA);
            }
        });
    } else if (columnName === "quantity") {
        isQuantitySortedAscending = !isQuantitySortedAscending;

        rows.sort((rowA, rowB) => {
            let quantityA = parseInt(rowA.getElementsByTagName("td")[4].innerText);
            let quantityB = parseInt(rowB.getElementsByTagName("td")[4].innerText);

            if (isQuantitySortedAscending) {
                return quantityA - quantityB;
            } else {
                return quantityB - quantityA;
            }
        });
    }

    tableBody.innerHTML = "";
    rows.forEach((row) => {
        tableBody.appendChild(row);
    });
}

// Add event listeners to the table headers for sorting
document.getElementById("book-id-header").addEventListener("click", () => {
    sortTableBy("bookId");
});

document.getElementById("warehouse-id-header").addEventListener("click", () => {
    sortTableBy("warehouseId");
});

document.getElementById("book-header").addEventListener("click", () => {
    sortTableBy("book");
});

document.getElementById("warehouse-header").addEventListener("click", () => {
    sortTableBy("warehouse");
});

document.getElementById("quantity-header").addEventListener("click", () => {
    sortTableBy("quantity");
});