const URL = "http://localhost:8080/books/";
let allBooks = [];

let isTitleSortedAscending = true;
let isAuthorSortedAscending = true;
let isPublicationDateSortedAscending = true;
let isISBNSortedAscending = true;

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

function sortTableBy(columnName) {
    let tableBody = document.getElementById("book-table-body");
    let rows = Array.from(tableBody.getElementsByTagName("tr"));

    if (columnName === "title") {
        isTitleSortedAscending = !isTitleSortedAscending;

        rows.sort((rowA, rowB) => {
            let titleA = rowA.getElementsByTagName("td")[0].innerText;
            let titleB = rowB.getElementsByTagName("td")[0].innerText;

            if (isTitleSortedAscending) {
                return titleA.localeCompare(titleB);
            } else {
                return titleB.localeCompare(titleA);
            }
        });
    } else if (columnName === "author") {
        isAuthorSortedAscending = !isAuthorSortedAscending;

        rows.sort((rowA, rowB) => {
            let authorA = rowA.getElementsByTagName("td")[1].innerText;
            let authorB = rowB.getElementsByTagName("td")[1].innerText;

            if (isAuthorSortedAscending) {
                return authorA.localeCompare(authorB);
            } else {
                return authorB.localeCompare(authorA);
            }
        });
    } else if (columnName === "publicationDate") {
        isPublicationDateSortedAscending = !isPublicationDateSortedAscending;

        rows.sort((rowA, rowB) => {
            let publicationDateA = rowA.getElementsByTagName("td")[2].innerText;
            let publicationDateB = rowB.getElementsByTagName("td")[2].innerText;

            if (isPublicationDateSortedAscending) {
                return publicationDateA.localeCompare(publicationDateB);
            } else {
                return publicationDateB.localeCompare(publicationDateA);
            }
        });
    } else if (columnName === "isbn") {
        isISBNSortedAscending = !isISBNSortedAscending;

        rows.sort((rowA, rowB) => {
            let isbnA = rowA.getElementsByTagName("td")[3].innerText;
            let isbnB = rowB.getElementsByTagName("td")[3].innerText;

            if (isISBNSortedAscending) {
                return isbnA.localeCompare(isbnB);
            } else {
                return isbnB.localeCompare(isbnA);
            }
        });
    }

    tableBody.innerHTML = "";
    rows.forEach((row) => {
        tableBody.appendChild(row);
    });
}

// Add event listeners to the table headers for sorting
document.getElementById("title-header").addEventListener("click", () => {
    sortTableBy("title");
});

document.getElementById("author-header").addEventListener("click", () => {
    sortTableBy("author");
});

document.getElementById("publication-date-header").addEventListener("click", () => {
    sortTableBy("publicationDate");
});

document.getElementById("isbn-header").addEventListener("click", () => {
    sortTableBy("isbn");
});
