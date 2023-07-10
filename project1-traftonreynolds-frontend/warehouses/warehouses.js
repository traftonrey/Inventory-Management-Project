const URL = "http://localhost:8080/warehouses/";
let allWarehouses = [];

let isNameSortedAscending = true;
let isCapacitySortedAscending = true;

document.addEventListener("DOMContentLoaded", () => {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let warehouses = JSON.parse(xhr.responseText);

            warehouses.forEach((newWarehouse) => {
                addWarehouse(newWarehouse);
            });
        }
    };

    xhr.open("GET", URL);
    xhr.send();

});

function addWarehouse(newWarehouse) {
    let tr = document.createElement("tr");

    let warehouseName = document.createElement("td");
    let capacity = document.createElement("td");

    let editBtn = document.createElement("td");

    warehouseName.innerText = newWarehouse.warehouseName;
    capacity.innerText = newWarehouse.capacity;

    editBtn.innerHTML =
        `<button class='btn btn-primary' id='edit-button'>Edit</button>`;

    tr.appendChild(warehouseName);
    tr.appendChild(capacity);
    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newWarehouse.id);

    document.getElementById("warehouse-table-body").appendChild(tr);

    allWarehouses.push(newWarehouse);
};

function sortTableBy(columnName) {
    let tableBody = document.getElementById("warehouse-table-body");
    let rows = Array.from(tableBody.getElementsByTagName("tr"));

    if (columnName === "name") {
        isNameSortedAscending = !isNameSortedAscending;

        rows.sort((rowA, rowB) => {
            let nameA = rowA.getElementsByTagName("td")[0].innerText;
            let nameB = rowB.getElementsByTagName("td")[0].innerText;

            if (isNameSortedAscending) {
                return nameA.localeCompare(nameB);
            } else {
                return nameB.localeCompare(nameA);
            }
        });
    } else if (columnName === "capacity") {
        isCapacitySortedAscending = !isCapacitySortedAscending;

        rows.sort((rowA, rowB) => {
            let capacityA = parseInt(rowA.getElementsByTagName("td")[1].innerText);
            let capacityB = parseInt(rowB.getElementsByTagName("td")[1].innerText);

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

// Add event listeners to the table headers for sorting
document.getElementById("name-header").addEventListener("click", () => {
    sortTableBy("name");
});

document.getElementById("capacity-header").addEventListener("click", () => {
    sortTableBy("capacity");
});
