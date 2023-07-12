const URL = "http://localhost:8080/books/";
let allBooks = [];

let isTitleSortedAscending = true;
let isAuthorSortedAscending = true;
let isPublicationDateSortedAscending = true;
let isISBNSortedAscending = true;

async function refreshTable() {
    let xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            let books = JSON.parse(xhr.responseText);

            // Clear the table body
            let tableBody = document.getElementById("book-table-body");
            tableBody.innerHTML = "";

            // populate the table
            books.forEach((newBook) => {
                addBook(newBook);
            });
        }
    };

    xhr.open("GET", URL);
    xhr.send();
}

document.addEventListener("DOMContentLoaded", () => {
    refreshTable();
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

    editBtn.innerHTML = `<button class='btn btn-primary' onclick='openEditModal(${newBook.bookId})'>Edit</button>`;

    // Add the table cells to the table row
    tr.appendChild(title);
    tr.appendChild(author);
    tr.appendChild(publishDate);
    tr.appendChild(ISBN);
    tr.appendChild(editBtn);

    tr.setAttribute("id", "TR" + newBook.bookId);
    tr.setAttribute("class", "book-row");

    document.getElementById("book-table-body").appendChild(tr);

    allBooks.push(newBook);

};

async function createBook() {

    // validate the form data
    if (!validateForm()) {
        return;
    }

    // Retrieve the values from the input fields
    const title = document.getElementById('create-title').value;
    const author = document.getElementById('create-author').value;
    const publicationDate = document.getElementById('create-publication-date').value;
    const isbn = document.getElementById('create-isbn').value;

    // Create the request body object
    const requestBody = {
        title: title,
        author: author,
        publicationDate: publicationDate,
        isbn: isbn
    };

    try {
        // Send a POST request to create a new book
        const response = await fetch(URL + "book/", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            // Book successfully created
            console.log('Book created successfully.');

            await refreshTable();
        }
    } catch (error) {
        // Handle network errors
        console.error('Error creating book:', error);
    }
}

async function saveChanges() {

    // validate the form data
    if (!validateForm()) {
        return;
    }

    // Retrieve the values from the input fields
    const bookId = document.getElementById('edit-book-id').value;
    const newTitle = document.getElementById('edit-title').value;
    const newAuthor = document.getElementById('edit-author').value;
    const newPublicationDate = document.getElementById('edit-publication-date').value;
    const newIsbn = document.getElementById('edit-isbn').value;

    // Create an object with the book ID for the request body
    const requestBody = {
        bookId: bookId
    };

    // Append the new values as query parameters
    const url = URL + 'book/' + bookId +
        '?newTitle=' + encodeURIComponent(newTitle) +
        '&newAuthor=' + encodeURIComponent(newAuthor) +
        '&newPublishDate=' + encodeURIComponent(newPublicationDate) +
        '&newIsbn=' + encodeURIComponent(newIsbn);

    try {
        // Send a PUT request to update the book
        const response = await fetch(url, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            // Book successfully updated
            console.log('Book updated successfully.');

            await refreshTable();

        }
    } catch (error) {
        // Handle network errors
        console.error('Error updating book:', error);
    }
}

async function deleteBook() {
    try {
        // Retrieve the book ID from the input field
        const bookId = document.getElementById('edit-book-id').value;

        const requestBody = {
            bookId: bookId
        };

        // Send a DELETE request to delete the book
        const response = await fetch(`${URL}book/${bookId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
              },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            // Book successfully deleted
            console.log('Book deleted successfully.');
            
            await refreshTable();
        }
    } catch (error) {
        // Handle network errors
        console.error('Error deleting book:', error);
    }
}

function openEditModal(bookId) {
    // Find the book in the allBooks array based on the bookId
    const book = allBooks.find((b) => b.bookId === bookId);

    // Populate the form fields with the book data
    document.getElementById("edit-title").value = book.title;
    document.getElementById("edit-author").value = book.author;
    document.getElementById("edit-publication-date").value = book.publicationDate;
    document.getElementById("edit-isbn").value = book.isbn;
    document.getElementById("edit-book-id").value = bookId;

    // Open the edit modal
    const editModal = new bootstrap.Modal(document.getElementById("editModal"));
    editModal.show();
}

function validateForm() {
    // Get the values of the input fields
    const title = document.getElementById("edit-title").value.trim();
    const author = document.getElementById("edit-author").value.trim();
    const publicationDate = document.getElementById("edit-publication-date").value.trim();
    const isbn = document.getElementById("edit-isbn").value.trim();

    // Check if any of the fields are blank or contain only whitespace
    if (title === "" || author === "" || publicationDate === "" || isbn === "") {
        alert("Please fill in all required fields.");
        return false; // Prevent form submission
    }

    return true; // Allow form submission
}

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
