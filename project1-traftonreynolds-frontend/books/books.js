const URL = "http://localhost:8080/books/";
const tableBodyId = "book-table-body";

let allBooks = [];

let isTitleSortedAscending = true;
let isAuthorSortedAscending = true;
let isPublicationDateSortedAscending = true;
let isISBNSortedAscending = true;

document.addEventListener("DOMContentLoaded", () => {
    refreshTable(URL, tableBodyId, addBook);
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
    if (!validateForm("create")) {
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

            await refreshTable(URL, tableBodyId, addBook);
            clearForm('create');
            dismissModal('create');
            showSuccessMessage('Book created successfully!');
        }
    } catch (error) {
        // Handle network errors
        console.error('Error creating book:', error);
    }
}

async function saveChanges() {

    // validate the form data
    if (!validateForm("edit")) {
        return;
    }

    // Retrieve the values from the input fields
    const bookId = document.getElementById('edit-book-id').value.trim();
    const newTitle = document.getElementById('edit-title').value.trim();
    const newAuthor = document.getElementById('edit-author').value.trim();
    const newPublicationDate = document.getElementById('edit-publication-date').value.trim();
    const newIsbn = document.getElementById('edit-isbn').value.trim();

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

            await refreshTable(URL, tableBodyId, addBook);
            clearForm("edit");
            dismissModal("edit");
            showSuccessMessage("Book updated successfully.");
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
            
            await refreshTable(URL, tableBodyId, addBook);
            dismissModal("editModal");
            showSuccessMessage("Book deleted successfully.");
        }
    } catch (error) {
        // Handle network errors
        console.error('Error deleting book:', error);
    }
}

async function openEditModal(bookId) {
    try {
      // Fetch the latest book data from the server
      const response = await fetch(`${URL}book/${bookId}`);
      if (response.ok) {
        const book = await response.json();
  
        // Populate the form fields with the updated book data
        document.getElementById("edit-title").value = book.title;
        document.getElementById("edit-author").value = book.author;
        document.getElementById("edit-publication-date").value = book.publicationDate;
        document.getElementById("edit-isbn").value = book.isbn;
        document.getElementById("edit-book-id").value = bookId;
  
        // Open the edit modal
        const editModal = new bootstrap.Modal(document.getElementById("editModal"));
        editModal.show();
      } else {
        console.error('Error retrieving book:', response.status);
      }
    } catch (error) {
      console.error('Error retrieving book:', error);
    }
  }
  

function validateForm(formId) {
    // Get the values of the input fields
    const title = document.getElementById(formId + "-title").value;
    const author = document.getElementById(formId + "-author").value;
    const publicationDate = document.getElementById(formId + "-publication-date").value;
    const isbn = document.getElementById(formId + "-isbn").value;

    // Check if any of the fields are blank or contain only whitespace
    if (title === "" || author === "" || publicationDate === "" || isbn === "") {
        alert("Please fill in all required fields.");
        return false; // Prevent form submission
    }

    if (!isValidISBNFormat(isbn)) {
        alert("Please enter a valid ISBN.");
        return;
    }

    return true; // Allow form submission
}

function isValidISBNFormat(isbn) {
    // Remove any hyphens from the entered ISBN
    const cleanedISBN = isbn.replace(/-/g, "");
  
    // ISBN-10 format: 10 digits ending with a check digit (0-9 or 'X')
    // ISBN-13 format: 13 digits starting with a valid prefix (978 or 979) followed by 10 digits ending with a check digit (0-9)
    const isbn10Pattern = /^\d{9}[\d|X]$/;
    const isbn13Pattern = /^(978|979)\d{10}$/;
  
    return isbn10Pattern.test(cleanedISBN) || isbn13Pattern.test(cleanedISBN);
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
