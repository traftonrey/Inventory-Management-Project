package com.skillstorm.traftonreynolds.project1traftonreynolds.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory {
    @EmbeddedId
    private InventoryId id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id", insertable = false, updatable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_id", insertable = false, updatable = false)
    private Warehouse warehouse;

    @Column(name = "quantity")
    private int quantity;

    // Constructors, getters, and setters

    public Inventory() {
    }

    public Inventory(Book book, Warehouse warehouse, int quantity) {
        this.book = book;
        this.warehouse = warehouse;
        this.quantity = quantity;
    }

    public InventoryId getId() {
        return id;
    }

    public void setId(InventoryId id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((book == null) ? 0 : book.hashCode());
        result = prime * result + ((warehouse == null) ? 0 : warehouse.hashCode());
        result = prime * result + quantity;
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
        Inventory other = (Inventory) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (book == null) {
            if (other.book != null)
                return false;
        } else if (!book.equals(other.book))
            return false;
        if (warehouse == null) {
            if (other.warehouse != null)
                return false;
        } else if (!warehouse.equals(other.warehouse))
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Inventory [id=" + id + ", book=" + book + ", warehouse=" + warehouse + ", quantity=" + quantity + "]";
    }
}

