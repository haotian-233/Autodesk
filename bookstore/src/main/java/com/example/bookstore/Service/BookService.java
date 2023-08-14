package com.example.bookstore.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookstore.Entity.Book;
import com.example.bookstore.Repository.BookInventory;

@Service
public class BookService {

    @Autowired
    private BookInventory bookInventory;

    public Book findBookByISBN(Long isbn){
        // TODO: handle not found exception
        return bookInventory.findById(isbn).orElse(null);
        // return bookInventory.getReferenceById(isbn);
    }

    public Book addNewBook(Book book){
        // TODO: need to check for duplicate key -> 400
        // TODO: server error -> 500
        // TODO: check & enforce ISBN format 13 digits
        book.setQuantity(0); // only if its new book, not existing book
        return bookInventory.saveAndFlush(book);
    }

    public void removeBook(Long isbn){
        bookInventory.deleteById(isbn);
    }

    public Integer getBookQuantityById(Long isbn){
        Book book = findBookByISBN(isbn);
        return book.getQuantity();
    }

    public void updateBookQuantityById(Long isbn, Integer newQuantity){
        Book book = findBookByISBN(isbn);
        book.setQuantity(newQuantity);
        bookInventory.saveAndFlush(book);
    }

    // TODO: updateBook: update book info for existing isbn

    public List<Book> listAllBooks(){
        // TODO: IO intensive, to be optimized 
        return bookInventory.findAll();
    }





    
}
