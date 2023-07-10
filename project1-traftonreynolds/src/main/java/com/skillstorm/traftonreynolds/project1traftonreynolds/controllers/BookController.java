package com.skillstorm.traftonreynolds.project1traftonreynolds.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.BookService;

@RestController
@RequestMapping("/books")
@CrossOrigin("http://127.0.0.1:5500/")
public class BookController {

    @Autowired
    BookService bookService;

    /*
     * GET MAPPINGS
     */
    
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {

        // calling service method to find all books
        List<Book> books = bookService.findAllBooks();
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable int id) {

        // calling service method to find book by id
        Book book = bookService.findBookById(id);
        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    /*
     * POST MAPPINGS
     */

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {

        // calling service method to create book
        Book savedBook = bookService.createBook(book);
        return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
    }

    /*
     * DELETE MAPPINGS
     */

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Book> deleteBook(@RequestBody Book book) {

        // calling service method to delete book
        bookService.deleteBook(book);
        return ResponseEntity.noContent().build();
    }

    /*
     * PUT MAPPINGS
     */

    @PutMapping("/book/{id}")
    public ResponseEntity<Integer> updateBook(@RequestBody Book book,
                                                @RequestParam(required = false) String newTitle,
                                                @RequestParam(required = false) String newAuthor,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newPublishDate,
                                                @RequestParam(required = false) String newIsbn
                                                ) {

        // calling service method to update book
        int updated = bookService.updateBook(book, newTitle, newAuthor, newPublishDate, newIsbn);
        return new ResponseEntity<Integer>(updated, HttpStatus.OK);
    }
}
