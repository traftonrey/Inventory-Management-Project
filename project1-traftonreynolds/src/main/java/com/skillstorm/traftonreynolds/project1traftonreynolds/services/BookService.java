package com.skillstorm.traftonreynolds.project1traftonreynolds.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Inventory;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.BookRepository;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.InventoryRepository;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    InventoryRepository inventoryRepository;

    /*
     * GET MAPPINGS
     */

    public List<Book> findAllBooks() {

        try {
            // Retrieve all books from the database
            return bookRepository.findAll();
        } catch (Exception e) {
            // If no books are found, throw an exception
            throw new EntityNotFoundException("No books found.");
        }
    }

    public Book findBookById(int id) {

        // Retrieve the book from the database
        return bookRepository.findById(id)
                // Throw an exception if the book does not exist
                .orElseThrow(() -> new EntityNotFoundException("Book with ID " + id + " does not exist."));
    }

    /*
     * POST MAPPINGS
     */

    public Book createBook(Book book) {

        // check if book with same isbn already exists in the database
        Optional<Book> bookOptional = bookRepository.findByIsbn(book.getIsbn());

        // if book already exists, throw an exception
        if (bookOptional.isPresent()) {
            throw new DuplicateKeyException("Book with ISBN " + book.getIsbn() + " already exists.");
        }

        // if book does not already exist, save it to the database
        return bookRepository.save(book);
    }

    /*
     * DELETE MAPPINGS
     */

    @Transactional
    public void deleteBook(Book book) {
        try {
            // Get the associated inventory items
            Optional<List<Inventory>> inventoriesOptional = inventoryRepository.findByBook(book);

            List<Inventory> inventories = inventoriesOptional.get();

            // Update the capacity of the associated warehouses
            for (Inventory inventory : inventories) {
                Warehouse warehouse = inventory.getWarehouse();
                int capacityDifference = inventory.getQuantity();
                int currentCapacity = warehouse.getCapacity();
                int newCapacity = currentCapacity + capacityDifference;

                warehouseService.updateCapacity(warehouse, newCapacity);
            }
        } catch (Exception e) {
            // If no inventories are found, do nothing
        }
        // Delete the inventory items
        inventoryRepository.deleteByBookBookId(book.getBookId());

        // Delete the book from the database
        bookRepository.delete(book);
    }

    /*
     * PUT MAPPINGS
     */

    @Transactional
    public int updateBook(Book book, String newTitle, String newAuthor, LocalDate newPublishDate, String newIsbn) {

        // Retrieve the book from the database
        Optional<Book> existingBookOptional = bookRepository.findById(book.getBookId());
        if (!existingBookOptional.isPresent()) {
            throw new EntityNotFoundException("Book with ID " + book.getBookId() + " does not exist.");
        }

        // Get the book from the Optional
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

        // Return 1 to indicate success
        return 1;
    }
}
