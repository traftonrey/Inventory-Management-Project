package com.skillstorm.traftonreynolds.project1traftonreynolds.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name="warehouses" )
public class Warehouse {

    // Fields

    @Id
    @Column(name = "warehouse_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int warehouseId;

    @Column(name="warehouse_name")
    private String warehouseName;

    @Column(name="maximum_capacity")
    private int maximumCapacity;

    // Constructors

    public Warehouse() {}

    public Warehouse(String warehouseName, int maximumCapacity) {
        this.warehouseName = warehouseName;
        this.maximumCapacity = maximumCapacity;
    }
    
    public Warehouse(int warehouseId, String warehouseName, int maximumCapacity) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.maximumCapacity = maximumCapacity;
    }
    
    // Getters and Setters
    
    public int getWarehouseId() {
        return warehouseId;
    }
    
    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }
    
    public String getWarehouseName() {
        return warehouseName;
    }
    
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    
    public int getMaximumCapacity() {
        return maximumCapacity;
    }
    
    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }
    
    // toString
    
    @Override
    public String toString() {
        return "Warehouse [warehouseId=" + warehouseId + ", warehouseName=" + warehouseName +
                ", maximumCapacity=" + maximumCapacity + "]";
    }

    // hashCode and equals

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + warehouseId;
        result = prime * result + ((warehouseName == null) ? 0 : warehouseName.hashCode());
        result = prime * result + maximumCapacity;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Warehouse other = (Warehouse) obj;
        if (warehouseId != other.warehouseId)
            return false;
        if (warehouseName == null) {
            if (other.warehouseName != null)
                return false;
        } else if (!warehouseName.equals(other.warehouseName))
            return false;
        if (maximumCapacity != other.maximumCapacity)
            return false;
        return true;
    }
}
