const URL = "http://localhost:8080/inventory/";
let allInventory = [];

let isBookIdSortedAscending = true;
let isWarehouseIdSortedAscending = true;
let isBookSortedAscending = true;
let isWarehouseSortedAscending = true;
let isQuantitySortedAscending = true;

document.addEventListener("DOMContentLoaded", () => {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let inventory = JSON.parse(xhr.responseText);

            inventory.forEach((newInventory) => {
                addInventory(newInventory);
            });
        }
    };

    xhr.open("GET", URL);
    xhr.send();

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

    editBtn.innerHTML =
    `<button class='btn btn-primary' id='edit-button'>Edit</button>`;

    tr.appendChild(bookId);
    tr.appendChild(warehouseId);
    tr.appendChild(book);
    tr.appendChild(warehouse);
    tr.appendChild(quantity);

    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newInventory.id);

    document.getElementById("inventory-table-body").appendChild(tr);

    allInventory.push(newInventory);

};

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