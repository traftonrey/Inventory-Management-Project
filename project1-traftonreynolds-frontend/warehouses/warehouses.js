// const URL = "http://localhost:8080/warehouses/";
// let allWarehouses = [];

// let isNameSortedAscending = true;
// let isCapacitySortedAscending = true;

// document.addEventListener("DOMContentLoaded", () => {
//     let xhr = new XMLHttpRequest();

//     xhr.onreadystatechange = () => {
//         if (xhr.readyState === 4 && xhr.status === 200) {
//             let warehouses = JSON.parse(xhr.responseText);

//             warehouses.forEach((newWarehouse) => {
//                 addWarehouse(newWarehouse);
//             });
//         }
//     };

//     xhr.open("GET", URL);
//     xhr.send();

// });

// function addWarehouse(newWarehouse) {
//     let tr = document.createElement("tr");

//     let warehouseName = document.createElement("td");
//     let capacity = document.createElement("td");

//     let editBtn = document.createElement("td");

//     warehouseName.innerText = newWarehouse.warehouseName;
//     capacity.innerText = newWarehouse.capacity;

//     editBtn.innerHTML =
//         `<button class='btn btn-primary' id='edit-button'>Edit</button>`;

//     tr.appendChild(warehouseName);
//     tr.appendChild(capacity);
//     tr.appendChild(editBtn);

//     tr.setAttribute("id", "TR" + newWarehouse.id);

//     document.getElementById("warehouse-table-body").appendChild(tr);

//     allWarehouses.push(newWarehouse);
// };

// function sortTableBy(columnName) {
//     let tableBody = document.getElementById("warehouse-table-body");
//     let rows = Array.from(tableBody.getElementsByTagName("tr"));

//     if (columnName === "name") {
//         isNameSortedAscending = !isNameSortedAscending;

//         rows.sort((rowA, rowB) => {
//             let nameA = rowA.getElementsByTagName("td")[0].innerText;
//             let nameB = rowB.getElementsByTagName("td")[0].innerText;

//             if (isNameSortedAscending) {
//                 return nameA.localeCompare(nameB);
//             } else {
//                 return nameB.localeCompare(nameA);
//             }
//         });
//     } else if (columnName === "capacity") {
//         isCapacitySortedAscending = !isCapacitySortedAscending;

//         rows.sort((rowA, rowB) => {
//             let capacityA = parseInt(rowA.getElementsByTagName("td")[1].innerText);
//             let capacityB = parseInt(rowB.getElementsByTagName("td")[1].innerText);

//             if (isCapacitySortedAscending) {
//                 return capacityA - capacityB;
//             } else {
//                 return capacityB - capacityA;
//             }
//         });
//     }

//     tableBody.innerHTML = "";
//     rows.forEach((row) => {
//         tableBody.appendChild(row);
//     });
// }

// // Add event listeners to the table headers for sorting
// document.getElementById("name-header").addEventListener("click", () => {
//     sortTableBy("name");
// });

// document.getElementById("capacity-header").addEventListener("click", () => {
//     sortTableBy("capacity");
// });

const URL = "http://localhost:8080/warehouses/";
let allWarehouses = [];

let isNameSortedAscending = true;
let isCapacitySortedAscending = true;

async function refreshTable() {
    const response = await fetch(URL);
    if (response.ok) {
        const warehouses = await response.json();

        // Clear the table body
        const tableBody = document.getElementById("warehouse-table-body");
        tableBody.innerHTML = "";

        // Populate the table
        warehouses.forEach((warehouse) => {
            addWarehouse(warehouse);
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    refreshTable();
});

function addWarehouse(newWarehouse) {
    let tr = document.createElement("tr");

    let name = document.createElement("td");
    let capacity = document.createElement("td");

    let editBtn = document.createElement("td");

    name.innerText = newWarehouse.warehouseName;
    capacity.innerText = newWarehouse.capacity;

    editBtn.innerHTML = `<button class='btn btn-primary' onclick='openEditModal(${newWarehouse.warehouseId})'>Edit</button>`;

    // Add the table cells to the table row
    tr.appendChild(name);
    tr.appendChild(capacity);
    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newWarehouse.warehouseId);
    tr.setAttribute("class", "warehouse-row");

    document.getElementById("warehouse-table-body").appendChild(tr);

    allWarehouses.push(newWarehouse);
}

async function createWarehouse() {
    // Validate the form data
    if (!validateForm()) {
        return;
    }

    // Retrieve the values from the input fields
    const name = document.getElementById("create-warehouse-name").value;
    const capacity = document.getElementById("create-warehouse-capacity").value;

    const requestBody = {
        warehouseName: name,
        capacity: capacity
    };

    try {
        const response = await fetch(URL + "warehouse/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            // Warehouse was created successfully
            await refreshTable();
        }
    } catch (error) {
        console.error("Error creating warehouse:", error);
    }
}

async function saveWarehouseChanges() {
    // Validate the form data
    if (!validateForm()) {
        return;
    }

    const warehouseId = document.getElementById("edit-warehouse-id").value;
    const newName = document.getElementById("edit-warehouse-name").value;
    const newCapacity = document.getElementById("edit-warehouse-capacity").value;

    const requestBody = {
        warehouseId: warehouseId
    };

    const url = URL + "warehouse/" + warehouseId +
        "?newName=" + encodeURIComponent(newName) +
        "&newCapacity=" + encodeURIComponent(newCapacity);

    try {
        const response = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            console.log("Warehouse updated successfully.");

            await refreshTable();
        }
    } catch (error) {
        console.error("Error updating warehouse:", error);
    }
}

async function deleteWarehouse() {
    try {
        // Retrieve the warehouse ID from the input field
        const warehouseId = document.getElementById("edit-warehouse-id").value;

        const requestBody = {
            warehouseId: warehouseId
        }

        const response = await fetch(`${URL}warehouse/${warehouseId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            // Warehouse was deleted successfully
            console.log("Warehouse deleted successfully.");

            await refreshTable();
        }
    } catch (error) {
        // handle network errors
        console.error("Error deleting warehouse:", error);
    }
}

function openEditModal(warehouseId) {
    // Find the warehouse in the array based on the ID
    const warehouse = allWarehouses.find((w) => w.warehouseId === warehouseId);

    // Populate the form fields with the warehouse data
    document.getElementById("edit-warehouse-name").value = warehouse.warehouseName;
    document.getElementById("edit-warehouse-capacity").value = warehouse.capacity;
    document.getElementById("edit-warehouse-id").value = warehouseId;

    // Open the edit modal
    const editModal = new bootstrap.Modal(document.getElementById("editWarehouseModal"));
    editModal.show();
}

function validateForm() {
    // get the values from the input fields
    const name = document.getElementById("edit-warehouse-name").value.trim();
    const capacity = document.getElementById("edit-warehouse-capacity").value.trim();

    // check if any of the fields are empty or contain only whitespace
    if (name === "" || capacity === "") {
        alert("Please fill in all required fields.");
        return false; // prevent the form from being submitted
    }

    return true;    // allow the form to be submitted
}

function sortTableBy(columnName) {
    const tableBody = document.getElementById("warehouse-table-body");
    const rows = Array.from(tableBody.getElementsByTagName("tr"));

    if (columnName === "name") {
        isNameSortedAscending = !isNameSortedAscending;

        rows.sort((rowA, rowB) => {
            const nameA = rowA.getElementsByTagName("td")[0].innerText;
            const nameB = rowB.getElementsByTagName("td")[0].innerText;

            if (isNameSortedAscending) {
                return nameA.localeCompare(nameB);
            } else {
                return nameB.localeCompare(nameA);
            }
        });
    } else if (columnName === "capacity") {
        isCapacitySortedAscending = !isCapacitySortedAscending;

        rows.sort((rowA, rowB) => {
            const capacityA = parseInt(rowA.getElementsByTagName("td")[1].innerText);
            const capacityB = parseInt(rowB.getElementsByTagName("td")[1].innerText);

            if (isCapacitySortedAscending) {
                return capacityA - capacityB;
            } else {
                return capacityB - capacityA;
            }
        });
    }

    tableBody.innerHTML = "";
    rows.forEach((row) => {
        tableBody.appendChild(row);
    });
}

document.getElementById("name-header").addEventListener("click", () => {
    sortTableBy("name");
});

document.getElementById("capacity-header").addEventListener("click", () => {
    sortTableBy("capacity");
});
