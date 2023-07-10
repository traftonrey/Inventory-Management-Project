const URL = "http://localhost:8080/inventory/";
let allInventory = [];

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

    let id = document.createElement("td");
    let book = document.createElement("td");
    let warehouse = document.createElement("td");
    let quantity = document.createElement("td");

    let editBtn = document.createElement("td");

    id.innerText = newInventory.id.warehouseId + "-" + newInventory.id.bookId;
    book.innerText = newInventory.book.title;
    warehouse.innerText = newInventory.warehouse.warehouseName;
    quantity.innerText = newInventory.quantity;

    editBtn.innerHTML =
    `<button class='btn btn-primary' id='edit-button'>Edit</button>`;

    tr.appendChild(id);
    tr.appendChild(book);
    tr.appendChild(warehouse);
    tr.appendChild(quantity);

    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newInventory.id);

    document.getElementById("inventory-table-body").appendChild(tr);

    allInventory.push(newInventory);

};