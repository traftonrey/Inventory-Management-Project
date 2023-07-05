package com.skillstorm.traftonreynolds.project1traftonreynolds.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
    
    Optional<Warehouse> findById(int id);

}
