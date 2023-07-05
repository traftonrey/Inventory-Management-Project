package com.skillstorm.traftonreynolds.project1traftonreynolds.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class BookController {

    @Autowired
    BookService bookService;

    /*
     * GET MAPPINGS
     */
    
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        List<Book> books = bookService.findAllBooks();

        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable int id) {
        Book book = bookService.findBookById(id);

        return new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    /*
     * POST MAPPINGS
     */

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book savedBook = bookService.createBook(book);

        return new ResponseEntity<Book>(savedBook, HttpStatus.CREATED);
    }

    /*
     * DELETE MAPPINGS
     */

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Book> deleteBook(@RequestBody Book book) {
        Book deletedBook = bookService.deleteBook(book);

        return new ResponseEntity<Book>(deletedBook, HttpStatus.OK);
    }

    /*
     * PUT MAPPINGS
     */

    @PutMapping("/book/{id}")
    public ResponseEntity<Integer> updateMovie(@RequestBody Book book,
                                                @RequestParam(required = false) String newTitle,
                                                @RequestParam(required = false) String newAuthor,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newPublishDate,
                                                @RequestParam(required = false) String newIsbn
                                                ) {

        int updated = bookService.updateMovie(book, newTitle, newAuthor, newPublishDate, newIsbn);
        return new ResponseEntity<Integer>(updated, HttpStatus.OK);
    }
}
