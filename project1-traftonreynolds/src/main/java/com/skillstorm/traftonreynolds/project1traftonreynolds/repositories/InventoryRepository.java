package com.skillstorm.traftonreynolds.project1traftonreynolds.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Inventory;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.InventoryId;
import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findById(InventoryId id);

    Optional<List<Inventory>> findByWarehouse(Warehouse warehouseId);

    Optional<List<Inventory>> findByBook(Book book);

}
