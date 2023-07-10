package com.skillstorm.traftonreynolds.project1traftonreynolds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Inventory;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.InventoryRepository;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.BookService;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.InventoryService;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.WarehouseService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@CrossOrigin("http://127.0.0.1:5500/")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    BookService bookService;

    @Autowired
    WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        
        // calling service method to find all inventory
        List<Inventory> inventoryList = inventoryService.findAllInventory();
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @GetMapping("/{bookId}/{warehouseId}")
    public ResponseEntity<Inventory> findInventoryByIds(@PathVariable int bookId, @PathVariable int warehouseId) {

        // calling service method to find inventory by bookId and warehouseId
        Inventory inventory = inventoryService.findInventoryByIds(bookId, warehouseId);
        return new ResponseEntity<Inventory>(inventory, HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Inventory>> getInventoryByWarehouse(@PathVariable int warehouseId) {

        // calling service method to find inventory by warehouseId
        List<Inventory> inventoryList = inventoryService.findInventoryByWarehouse(warehouseId);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Inventory>> getInventoryByBook(@PathVariable int bookId) {

        // calling service method to find inventory by bookId
        List<Inventory> inventoryList = inventoryService.findInventoryByBook(bookId);
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    /*
     * POST MAPPINGS
     */

    @PostMapping
    public ResponseEntity<Inventory> createInventory(
            @RequestParam("bookId") int bookId,
            @RequestParam("warehouseId") int warehouseId,
            @RequestParam("quantity") int quantity) {

        // calling service method to create inventory
        Inventory inventory = inventoryService.createInventory(bookId, warehouseId, quantity);
        return new ResponseEntity<Inventory>(inventory, HttpStatus.CREATED);
    }

    /*
     * DELETE MAPPINGS
     */

    // may be able to write another one of these with just inventory objects rather than IDs later
    @DeleteMapping("/{bookId}/{warehouseId}")
    public ResponseEntity<Integer> deleteInventory(@PathVariable int bookId,
            @PathVariable int warehouseId) {

        // calling service method to delete inventory
        int deleted = inventoryService.deleteInventory(bookId, warehouseId);
        return new ResponseEntity<Integer>(deleted, HttpStatus.OK);
    }

    /*
     * PUT MAPPINGS
     */

    @PutMapping("/{bookId}/{warehouseId}/{quantity}")
    public ResponseEntity<Integer> updateInventory(@PathVariable int bookId,
            @PathVariable int warehouseId,
            @PathVariable(required = false) int quantity) {

        // calling service method to update inventory
        int updated = inventoryService.updateInventory(bookId, warehouseId, quantity);
        return new ResponseEntity<Integer>(updated, HttpStatus.OK);
    }

}
