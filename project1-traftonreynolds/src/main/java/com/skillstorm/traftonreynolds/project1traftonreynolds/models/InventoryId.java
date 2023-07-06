package com.skillstorm.traftonreynolds.project1traftonreynolds.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InventoryId implements Serializable {
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "warehouse_id")
    private int warehouseId;

    public InventoryId() {
    }

    public InventoryId(int bookId, int warehouseId) {
        this.bookId = bookId;
        this.warehouseId = warehouseId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bookId;
        result = prime * result + warehouseId;
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
        InventoryId other = (InventoryId) obj;
        if (bookId != other.bookId)
            return false;
        if (warehouseId != other.warehouseId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "InventoryId [bookId=" + bookId + ", warehouseId=" + warehouseId + "]";
    }

}
