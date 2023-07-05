package com.skillstorm.traftonreynolds.project1traftonreynolds.repositories;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.skillstorm.traftonreynolds.project1traftonreynolds.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    // SELECT * FROM book WHERE isbn = ?
    // @Query("SELECT b FROM Book b WHERE b.isbn = ?1")
    Optional<Book> findByIsbn(String isbn);

    Optional<Book> findById(int id);

}
