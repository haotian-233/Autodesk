package com.example.bookstore.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.bookstore.Entity.Book;

@Repository
public interface BookInventory extends JpaRepository<Book, Long>{
    Page<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:author IS NULL OR b.author LIKE %:author%) " +
            "AND (:isbn IS NULL OR b.isbn = :isbn) " +
            "AND (:minPrice IS NULL OR b.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR b.price <= :maxPrice) " +
            "AND (:available IS NULL OR b.quantity > 0)")
    Page<Book> searchBooks(String title, String author, Long isbn, Double minPrice, Double maxPrice, Boolean available, Pageable pageable);
}
