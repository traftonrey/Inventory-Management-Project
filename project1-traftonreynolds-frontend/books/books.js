const URL = "http://localhost:8080/books/";
let allBooks = [];

document.addEventListener("DOMContentLoaded", () => {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let books = JSON.parse(xhr.responseText);

            books.forEach((newBook) => {
                addBook(newBook);
            });
        }
    };

    xhr.open("GET", URL);
    xhr.send();

});

function addBook(newBook) {
    let tr = document.createElement("tr");

    let title = document.createElement("td");
    let author = document.createElement("td");
    let publishDate = document.createElement("td");
    let ISBN = document.createElement("td");

    let editBtn = document.createElement("td");

    title.innerText = newBook.title;
    author.innerText = newBook.author;
    publishDate.innerText = newBook.publicationDate;
    ISBN.innerText = newBook.isbn;

    editBtn.innerHTML =
    `<button class='btn btn-primary' id='edit-button'>Edit</button>`;

    tr.appendChild(title);
    tr.appendChild(author);
    tr.appendChild(publishDate);
    tr.appendChild(ISBN);
    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newBook.book_id);

    document.getElementById("book-table-body").appendChild(tr);

    allBooks.push(newBook);

};