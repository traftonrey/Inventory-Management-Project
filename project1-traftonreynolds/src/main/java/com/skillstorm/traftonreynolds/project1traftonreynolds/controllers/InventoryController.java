package com.skillstorm.traftonreynolds.project1traftonreynolds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Inventory;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.InventoryId;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.InventoryRepository;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.InventoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        try {
            List<Inventory> inventoryList = inventoryService.findAllInventory();
            return new ResponseEntity<>(inventoryList, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting all inventory.", e);
        }
    }

    @GetMapping("/{bookId}/{warehouseId}")
    public ResponseEntity<Inventory> findInventoryByIds(@PathVariable int bookId, @PathVariable int warehouseId) {
        InventoryId inventoryId = new InventoryId(bookId, warehouseId);
        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);
        if (!optionalInventory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Inventory with ID " + inventoryId + " does not exist.");
        }
        Inventory inventory = optionalInventory.get();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Inventory>> getInventoryByWarehouse(@PathVariable int warehouseId) {
        try {
            Warehouse warehouse = new Warehouse();
            warehouse.setWarehouseId(warehouseId);
            List<Inventory> inventoryList = inventoryService.findInventoryByWarehouse(warehouse);
            return new ResponseEntity<>(inventoryList, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting inventory by warehouse.",
                    e);
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Inventory>> getInventoryByBook(@PathVariable int bookId) {
        try {
            Book book = new Book();
            book.setBookId(bookId);
            List<Inventory> inventoryList = inventoryService.findInventoryByBook(book);
            return new ResponseEntity<>(inventoryList, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error getting inventory by book.", e);
        }
    }

    /*
     * POST MAPPINGS
     */

    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @RequestParam("bookId") int bookId,
            @RequestParam("warehouseId") int warehouseId,
            @RequestParam("quantity") int quantity) {
        Inventory inventory = inventoryService.createInventory(bookId, warehouseId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(inventory);
    }

    /*
     * DELETE MAPPINGS
     */

    @DeleteMapping("/{bookId}/{warehouseId}")
    public ResponseEntity<Integer> deleteInventory(@PathVariable int bookId, @PathVariable int warehouseId) {
        InventoryId inventoryId = new InventoryId(bookId, warehouseId);
        Optional<Inventory> optionalInventory = inventoryRepository.findById(inventoryId);
        if (!optionalInventory.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Inventory with ID " + inventoryId + " does not exist.");
        }

        inventoryRepository.delete(optionalInventory.get());
        return new ResponseEntity<Integer>(1, HttpStatus.OK);
    }

    /*
     * PUT MAPPINGS
     */

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateInventory(@RequestBody Inventory inventory,
            @RequestParam(required = false) Book book,
            @RequestParam(required = false) Warehouse warehouse,
            @RequestParam(required = false) int quantity) {
        int updated = inventoryService.updateInventory(inventory, book, warehouse, quantity);
        return new ResponseEntity<Integer>(updated, HttpStatus.OK);
    }

}
