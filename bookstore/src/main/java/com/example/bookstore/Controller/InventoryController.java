package com.example.bookstore.Controller;

import java.util.List;

import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.Entity.Book;
import com.example.bookstore.Service.BookService;

@RestController
public class InventoryController {

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public Book addBook(@RequestBody Book book){

        return bookService.addNewBook(book);
    }

    @GetMapping("/listAll")
    public List<Book> listAllBooks(){
        return bookService.listAllBooks();
    }

    @PostMapping("/removeBook")
    public void removeBook(Long isbn){
        bookService.removeBook(isbn);
    }

    @GetMapping("/getQuantity")
    public Integer getBookQuantityById(@RequestParam Long isbn){
        return bookService.getBookQuantityById(isbn);
    }

    @PutMapping("/updateQuantity")
    public void updateBookQuantityById(@RequestParam Long isbn, @RequestParam Integer quantity){
        bookService.updateBookQuantityById(isbn, quantity);
    }


}
