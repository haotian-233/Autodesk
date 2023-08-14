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
    @Query("SELECT b FROM Book b WHERE b.price >= :minPrice AND b.price <= :maxPrice")
    List<Book> findBooksInPriceRange(Double minPrice, Double maxPrice);

    Page<Book> findAll(Pageable pageable);
}
