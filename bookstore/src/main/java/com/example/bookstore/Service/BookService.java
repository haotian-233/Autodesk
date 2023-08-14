package com.example.bookstore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.bookstore.Entity.Book;
import com.example.bookstore.Repository.BookInventory;

@Service
public class BookService {

    @Autowired
    private BookInventory bookInventory;

    public Book findBookByISBN(Long isbn){
        return bookInventory.findById(isbn).orElse(null);
    }

    public Boolean doesBookExists(Long isbn){
        return bookInventory.existsById(isbn);
    }

    public Boolean isValidISBN(Long isbn){
        return (String.valueOf(isbn).length() == 13);
    }

    public Book addNewBook(Book book){        
        return bookInventory.saveAndFlush(book);
    }

    public void removeBook(Long isbn){
        bookInventory.deleteById(isbn);
    }

    public void updateBookQuantityById(Long isbn, Integer newQuantity){
        Book book = findBookByISBN(isbn);
        book.setQuantity(newQuantity);
        bookInventory.saveAndFlush(book);
    }

    public List<Book> listAllBooks(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> bookPage = bookInventory.findAll(pageable);
        List<Book> books = bookPage.getContent();
        return books;
    }





    
}
