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
            return inventoryRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("No inventory found.");
        }
    }

    public Inventory findInventoryByIds(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory with ID " + id + " does not exist."));
    }

    public List<Inventory> findInventoryByWarehouse(Warehouse warehouse) {
        List<Inventory> inventoryList = inventoryRepository.findByWarehouse(warehouse);
        if (inventoryList.isEmpty()) {
            throw new EntityNotFoundException("Warehouse " + warehouse + " does not exist.");
        }
        return inventoryList;
    }

    public List<Inventory> findInventoryByBook(Book book) {
        List<Inventory> inventoryList = inventoryRepository.findByBook(book);
        if (inventoryList.isEmpty()) {
            throw new EntityNotFoundException("Book " + book + " does not exist.");
        }
        return inventoryList;
    }

    /*
     * POST MAPPINGS
     */

    @Transactional
    public Inventory createInventory(int bookId, int warehouseId, int quantity) {
    Warehouse warehouse = warehouseService.findWarehouseById(warehouseId);
    Book book = bookService.findBookById(bookId);

    int currentCapacity = warehouse.getMaximumCapacity();
    int newCapacity = currentCapacity - quantity;

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

    return inventoryRepository.save(inventory);
}


    /*
     * DELETE MAPPINGS
     */

    public Inventory deleteInventory(Inventory inventory) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventory.getId());
        if (!inventoryOptional.isPresent()) {
            throw new EntityNotFoundException("Inventory with ID " + inventory.getId() + " does not exist.");
        }
        inventoryRepository.delete(inventory);
        return inventory;
    }

    /* 
     * PUT MAPPINGS
     */

    @Transactional
    public int updateInventory(Inventory inventory, Book book, Warehouse warehouse, int quantity) {
        // Retrieve the inventory from the database
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventory.getId());
        if (!inventoryOptional.isPresent()) {
            throw new EntityNotFoundException("Inventory with ID " + inventory.getId() + " does not exist.");
        }

        Inventory existingInventory = inventoryOptional.get();

        if (book != null) {
            existingInventory.setBook(book);
        }
        if (warehouse != null) {
            existingInventory.setWarehouse(warehouse);
        }
        if (quantity != 0) {
            if (quantity > existingInventory.getQuantity()) {
                int difference = quantity - existingInventory.getQuantity();
                int currentCapacity = existingInventory.getWarehouse().getMaximumCapacity();
                int newCapacity = currentCapacity - difference;

                if (newCapacity < 0) {
                    throw new DataIntegrityViolationException("Inventory quantity exceeds warehouse capacity.");
                }

                // Update the warehouse capacity
                warehouseService.updateCapacity(existingInventory.getWarehouse(), newCapacity);
            } else if (quantity < existingInventory.getQuantity()) {
                int difference = existingInventory.getQuantity() - quantity;
                int currentCapacity = existingInventory.getWarehouse().getMaximumCapacity();
                int newCapacity = currentCapacity + difference;

                // Update the warehouse capacity
                warehouseService.updateCapacity(existingInventory.getWarehouse(), newCapacity);
            }
            existingInventory.setQuantity(quantity);
        }
        return 1;
    }

}
