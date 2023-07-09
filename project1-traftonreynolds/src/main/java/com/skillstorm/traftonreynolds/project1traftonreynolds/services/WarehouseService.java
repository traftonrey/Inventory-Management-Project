package com.skillstorm.traftonreynolds.project1traftonreynolds.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;
import com.skillstorm.traftonreynolds.project1traftonreynolds.repositories.WarehouseRepository;

@Service
public class WarehouseService {

    @Autowired
    WarehouseRepository warehouseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /*
     * GET MAPPINGS
     */

    public List<Warehouse> findAllWarehouses() {

        try {
            // Retrieve all warehouses from the database
            return warehouseRepository.findAll();

        } catch (Exception e) {
            // If no warehouses are found, throw an exception
            throw new EntityNotFoundException("No warehouses found.");
        }
    }

    public Warehouse findWarehouseById(int id) {

        // Retrieve the warehouse from the database
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);

        // Throw an exception if the warehouse does not exist
        if (!warehouseOptional.isPresent()) {
            throw new EntityNotFoundException("Warehouse with ID " + id + " does not exist.");
        }

        // Return the warehouse
        return warehouseOptional.get();
    }
    
    /*
     * POST MAPPINGS
     */

    public Warehouse createWarehouse(Warehouse warehouse) {

        // Check if the warehouse already exists
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getWarehouseId());

        // If the warehouse already exists, throw an exception
        if (warehouseOptional.isPresent()) {
            throw new DuplicateKeyException("Warehouse with ID " + warehouse.getWarehouseId() + " already exists.");
        }

        // Save warehouse to the database
        return warehouseRepository.save(warehouse);
    }

    /*
     * DELETE MAPPINGS
     */
    
    public void deleteWarehouse(Warehouse warehouse) {

        // Check if the warehouse exists
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getWarehouseId());

        // If the warehouse does not exist, throw an exception
        if (!warehouseOptional.isPresent()) {
            throw new EntityNotFoundException("Warehouse with ID " + warehouse.getWarehouseId() + " does not exist.");
        }

        // Delete the warehouse
        warehouseRepository.delete(warehouse);
    }

    /*
     * PUT MAPPINGS
     */
    
    @Transactional
    public int updateWarehouse(Warehouse warehouse, String newName, int newCapacity) {

        // Check if the warehouse exists
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getWarehouseId());

        // If the warehouse does not exist, throw an exception
        if (!warehouseOptional.isPresent()) {
            throw new EntityNotFoundException("Warehouse with ID " + warehouse.getWarehouseId() + " does not exist.");
        }

        // get the existing warehouse
        Warehouse existingWarehouse = warehouseOptional.get();

        // update the warehouse
        if (newName != null && !newName.isEmpty()) {
            existingWarehouse.setWarehouseName(newName);
        }
        if (newCapacity > 0) {
            existingWarehouse.setCapacity(newCapacity);
        } else {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }

        // save the warehouse
        warehouseRepository.save(existingWarehouse);

        // return 1 if successful
        return 1;
    }

    @Transactional
    public void updateCapacity(Warehouse warehouse, int newCapacity) {

        // set the new capacity
        warehouse.setCapacity(newCapacity);

        // merge the warehouse
        entityManager.merge(warehouse);
    }
}
