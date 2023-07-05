package com.skillstorm.traftonreynolds.project1traftonreynolds.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.BookRepository;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WarehouseService warehouseService;

    /*
     * GET MAPPINGS
     */

    public List<Book> findAllBooks() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("No books found.");
        }
    }

    public Book findBookById(int id) {
        if (!bookRepository.findById(id).isPresent()) {
            throw new RuntimeException("Book with ID " + id + " does not exist.");
        }
        return bookRepository.findById(id).get();
    }

    /*
     * POST MAPPINGS
     */

    public Book createBook(Book book) {
        Optional<Book> bookOptional = bookRepository.findByIsbn(book.getIsbn());
        if (bookOptional.isPresent()) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " already exists.");
        }
        return bookRepository.save(book);
    }

    /*
     * DELETE MAPPINGS
     */

    public Book deleteBook(Book book) {
        Optional<Book> bookOptional = bookRepository.findByIsbn(book.getIsbn());
        if (!bookOptional.isPresent()) {
            throw new RuntimeException("Book with ISBN " + book.getIsbn() + " does not exist.");
        }
        bookRepository.delete(book);
        return book;
    }

    /*
     * PUT MAPPINGS
     */

    @Transactional
    public int updateMovie(Book book, String newTitle, String newAuthor, LocalDate newPublishDate, String newIsbn) {
        // Retrieve the book from the database
        Optional<Book> existingBookOptional = bookRepository.findById(book.getBookId());
        if (!existingBookOptional.isPresent()) {
            throw new RuntimeException("Book with ID " + book.getBookId() + " does not exist.");
        }

        Book existingBook = existingBookOptional.get();

        // Update the book's properties if new values are provided
        if (newTitle != null) {
            existingBook.setTitle(newTitle);
        }
        if (newAuthor != null) {
            existingBook.setAuthor(newAuthor);
        }
        if (newPublishDate != null) {
            existingBook.setPublicationDate(newPublishDate);
        }
        if (newIsbn != null) {
            existingBook.setIsbn(newIsbn);
        }

        // Save the updated book to the database
        bookRepository.save(existingBook);

        return 1;
    }

}
