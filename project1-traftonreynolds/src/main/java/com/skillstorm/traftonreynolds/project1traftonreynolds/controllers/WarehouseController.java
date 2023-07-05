package com.skillstorm.traftonreynolds.project1traftonreynolds.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;
import com.skillstorm.traftonreynolds.project1traftonreynolds.services.WarehouseService;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {
    @Autowired
    WarehouseService warehouseService;

    /*
     * GET MAPPINGS
     */
    
    @GetMapping
    public ResponseEntity<List<Warehouse>> findAllWarehouses() {
        List<Warehouse> warehouses = warehouseService.findAllWarehouses();

        return new ResponseEntity<List<Warehouse>>(warehouses, HttpStatus.OK);
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<Warehouse> findWarehouseById(@PathVariable int id) {
        Warehouse warehouse = warehouseService.findWarehouseById(id);

        return new ResponseEntity<Warehouse>(warehouse, HttpStatus.OK);
    }

    /*
     * POST MAPPINGS
     */

    @PostMapping("/warehouse")
    public ResponseEntity<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseService.createWarehouse(warehouse);

        return new ResponseEntity<Warehouse>(savedWarehouse, HttpStatus.CREATED);
    }


     /*
      * DELETE MAPPINGS
      */

    @DeleteMapping("/warehouse/{id}")
    public ResponseEntity<Warehouse> deleteWarehouse(@RequestBody Warehouse warehouse) {
        Warehouse deletedWarehouse = warehouseService.deleteWarehouse(warehouse);

        return new ResponseEntity<Warehouse>(deletedWarehouse, HttpStatus.OK);
    }



    /*
     * PUT MAPPINGS
     */

    @PutMapping("/warehouse/{id}")
    public ResponseEntity<Integer> updateWarehouse(@RequestBody Warehouse warehouse,
                                                    @RequestParam(required = false) String newName,
                                                    @RequestParam(required = false) int newCapacity) {
        
        
        int updated = warehouseService.updateWarehouse(warehouse, newName, newCapacity);

        return new ResponseEntity<Integer>(updated, HttpStatus.OK);
    }
       
}
