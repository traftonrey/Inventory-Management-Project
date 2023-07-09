package com.skillstorm.traftonreynolds.project1traftonreynolds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Inventory;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.InventoryId;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.InventoryRepository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    BookService bookService;

    /*
     * GET MAPPINGS
     */

    public List<Inventory> findAllInventory() {

        try {
            // Retrieve all inventory from the database
            return inventoryRepository.findAll();
            
        } catch (Exception e) {
            // If no inventory is found, throw an exception
            throw new EntityNotFoundException("No inventory found.");
        }
    }

    public Inventory findInventoryByIds(int bookId, int warehouseId) {

        // Retrieve the inventory from the database
        InventoryId inventoryId = new InventoryId(bookId, warehouseId);
        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);

        // Throw an exception if the inventory does not exist
        if (!optionalInventory.isPresent()) {
            throw new EntityNotFoundException("Inventory with " + inventoryId + " does not exist.");
        }

        // Return the inventory
        return inventoryRepository.findById(inventoryId).get();
    }

    public List<Inventory> findInventoryByWarehouse(int warehouseId) {
        
        // find the inventory by warehouse
        Optional<List<Inventory>> inventoryOptional = inventoryRepository.findByWarehouse(warehouseService.findWarehouseById(warehouseId));

        // throw an exception if no inventory is found
        if (!inventoryOptional.isPresent()) {
            throw new EntityNotFoundException("No inventory for warehouse" + warehouseId + " found.");
        }

        // return the list of inventories
        return inventoryOptional.get();
    }

    public List<Inventory> findInventoryByBook(int bookId) {

        // find the inventory by book
        Optional<List<Inventory>> inventoryOptional = inventoryRepository.findByBook(bookService.findBookById(bookId));

        // throw an exception if no inventory is found
        if (!inventoryOptional.isPresent()) {
            throw new EntityNotFoundException("No inventory for book" + bookId + " found.");
        }

        // return the list of inventories
        return inventoryOptional.get();
    }

    /*
     * POST MAPPINGS
     */

    @Transactional
    public Inventory createInventory(int bookId, int warehouseId, int quantity) {

        // Retrieve the warehouse and book objects
        Warehouse warehouse = warehouseService.findWarehouseById(warehouseId);
        Book book = bookService.findBookById(bookId);

        // Check if the warehouse has/will have enough capacity
        int currentCapacity = warehouse.getCapacity();
        int newCapacity = currentCapacity - quantity;

        // Throw an exception if the warehouse does not have enough capacity
        if (newCapacity < 0) {
            throw new DataIntegrityViolationException("Inventory quantity exceeds warehouse capacity.");
        }

        // Update the warehouse capacity
        warehouseService.updateCapacity(warehouse, newCapacity);

        // Create the inventory
        Inventory inventory = new Inventory();
        InventoryId inventoryId = new InventoryId();
        inventoryId.setBookId(bookId);
        inventoryId.setWarehouseId(warehouseId);
        inventory.setId(inventoryId);
        inventory.setBook(book);
        inventory.setWarehouse(warehouse);
        inventory.setQuantity(quantity);

        // Save the inventory
        return inventoryRepository.save(inventory);
    }

    /*
     * DELETE MAPPINGS
     */

    @Transactional
    public int deleteInventory(int bookId, int warehouseId) {

        // finding inventory object to delete based on bookId and warehouseId
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(findInventoryByIds(bookId, warehouseId).getId());
        if (!inventoryOptional.isPresent()) {
            throw new EntityNotFoundException("Inventory with ID " + findInventoryByIds(bookId, warehouseId).getId() + " does not exist.");
        }

        // Retrieve inventory object once found
        Inventory inventory = inventoryOptional.get();
        
        // Retrieve warehouse object and get ready to update capacity
        Warehouse warehouse = warehouseService.findWarehouseById(warehouseId);
        int quantity = inventory.getQuantity();
        int currentCapacity = warehouse.getCapacity();
        int newCapacity = currentCapacity + quantity;

        // Update the warehouse capacity
        warehouseService.updateCapacity(warehouse, newCapacity);

        // Delete the inventory object
        inventoryRepository.delete(inventory);

        // Return 1 if successful
        return 1;
    }

    /*
     * PUT MAPPINGS
     */

    @Transactional
    public int updateInventory(int bookId, int warehouseId, int quantity) {

        // finding inventory object to update based on bookId and warehouseId
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(findInventoryByIds(bookId, warehouseId).getId());
        if (!inventoryOptional.isPresent()) {
            throw new EntityNotFoundException("Inventory with ID " + findInventoryByIds(bookId, warehouseId).getId() + " does not exist.");
        }
        // Retrieve inventory object once found
        Inventory existingInventory = inventoryOptional.get();

        /* ASSUME SINCE INVENTORY OBJECT IS FOUND, THAT BOOK AND WAREHOUSE EXIST */
        Book book = bookService.findBookById(bookId);
        Warehouse warehouse = warehouseService.findWarehouseById(warehouseId);

        // Update the inventory object
        if (book != null) {
            existingInventory.setBook(book);
        }
        if (warehouse != null) {
            existingInventory.setWarehouse(warehouse);
        }
        if (quantity != 0) {
            // Check if the warehouse has/will have enough capacity
            int currentQuantity = existingInventory.getQuantity();
            int quantityDifference = quantity - currentQuantity;
            int currentCapacity = existingInventory.getWarehouse().getCapacity();
            int newCapacity = currentCapacity - quantityDifference;

            if (newCapacity < 0) {
                throw new DataIntegrityViolationException("Inventory quantity exceeds warehouse capacity.");
            }

            // Update the warehouse capacity
            warehouseService.updateCapacity(existingInventory.getWarehouse(), newCapacity);

            // Update the inventory quantity
            existingInventory.setQuantity(quantity);
        } else if (quantity == 0) {
            // If the quantity is 0, throw an error
            throw new DataIntegrityViolationException("Quantity cannot be 0. Did you mean to delete the inventory?");
        }

        // Return 1 if successful
        return 1;
    }

}
