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
            return warehouseRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("No warehouses found.");
        }
    }

    public Warehouse findWarehouseById(int id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse with ID " + id + " does not exist."));
    }
    
    /*
     * POST MAPPINGS
     */

    public Warehouse createWarehouse(Warehouse warehouse) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getWarehouseId());
        if (warehouseOptional.isPresent()) {
            throw new DuplicateKeyException("Warehouse with ID " + warehouse.getWarehouseId() + " already exists.");
        }
        return warehouseRepository.save(warehouse);
    }

    /*
     * DELETE MAPPINGS
     */
    
    public Warehouse deleteWarehouse(Warehouse warehouse) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getWarehouseId());
        if (!warehouseOptional.isPresent()) {
            throw new EntityNotFoundException("Warehouse with ID " + warehouse.getWarehouseId() + " does not exist.");
        }
        warehouseRepository.delete(warehouse);
        return warehouse;
    }

    /*
     * PUT MAPPINGS
     */
    
    @Transactional
    public int updateWarehouse(Warehouse warehouse, String newName, int newCapacity) {
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(warehouse.getWarehouseId());
        if (!warehouseOptional.isPresent()) {
            throw new EntityNotFoundException("Warehouse with ID " + warehouse.getWarehouseId() + " does not exist.");
        }

        Warehouse existingWarehouse = warehouseOptional.get();

        if (newName != null && !newName.isEmpty()) {
            existingWarehouse.setWarehouseName(newName);
        }
        if (newCapacity > 0) {
            existingWarehouse.setMaximumCapacity(newCapacity);
        } else {
            throw new IllegalArgumentException("Capacity must be a positive integer.");
        }

        warehouseRepository.save(existingWarehouse);

        return 1;
    }

    @Transactional
    public void updateCapacity(Warehouse warehouse, int newCapacity) {
        warehouse.setMaximumCapacity(newCapacity);
        entityManager.merge(warehouse);
    }
}
