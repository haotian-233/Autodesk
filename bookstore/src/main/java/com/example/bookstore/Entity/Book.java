package com.example.bookstore.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "inventory")
public class Book {
    
    @jakarta.persistence.Id
    @Column(name = "isbn")
    private Long isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "price")
    private double price;

    @Column(name = "quantity")
    private int quantity;
}
