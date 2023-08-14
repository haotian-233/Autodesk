package com.example.bookstore.Controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.type.descriptor.java.IntegerJavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addBook(@RequestBody Book book){
        Long isbn = book.getIsbn();
        if(!bookService.isValidISBN(isbn)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The requested ISBN is not in correct format. It should be 13 digits long.");
        }
        if(bookService.doesBookExists(isbn)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ISBN " + isbn + " already exists. Please try update the book info if it is not a mistake.");
        }
        
        Book addedBook = bookService.addNewBook(book);
        return ResponseEntity.ok(addedBook);
    }

    @PostMapping("/removeBook")
    public ResponseEntity<String> removeBook(Long isbn){
        if(!bookService.isValidISBN(isbn)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The requested ISBN is not in correct format. It should be 13 digits long.");
        }
        if(bookService.doesBookExists(isbn)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ISBN " + isbn + " is not found in our database!");
        }
        bookService.removeBook(isbn);
        return ResponseEntity.ok().body("ISBN " + isbn + " removed successfully.");
    }

    @GetMapping("/getQuantity")
    public ResponseEntity<String> getBookQuantityById(@RequestParam Long isbn){
        String idString = String.valueOf(isbn);
        // The Format of ISBN should be 13 digits long
        if (idString.length() != 13){
            return ResponseEntity
                .badRequest()
                .body("The requested ISBN is not in correct format. It should be 13 digits long.");
        }

        Book book = bookService.findBookByISBN(isbn);

        if(book!=null){
            int quantity = book.getQuantity();
            return ResponseEntity.ok(String.valueOf(quantity));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ISBN " + idString + " is not found in our database!");
        }
        
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<String> updateBookQuantityById(@RequestParam Long isbn, @RequestParam Integer quantity){
        if(!bookService.isValidISBN(isbn)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The requested ISBN is not in correct format. It should be 13 digits long.");
        }
        if(bookService.doesBookExists(isbn)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ISBN " + isbn + " is not found in our database!");
        }
        bookService.updateBookQuantityById(isbn, quantity);
        return ResponseEntity.ok().body("ISBN " + isbn + " quantity has been updated to " + quantity);
    }

    @GetMapping("/listAll")
    public List<Book> listAllBooks(){
        return bookService.listAllBooks();
    }

    // @GetMapping("/search")
    // public List<Book> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
    //     // if (title != null && author != null) {
    //     //     return bookRepository.findByTitleAndAuthor(title, author);
    //     // } else if (title != null) {
    //     //     return bookRepository.findByTitle(title);
    //     // } else if (author != null) {
    //     //     return bookRepository.findByAuthor(author);
    //     // } else {
    //     //     return bookRepository.findAll();
    //     // }
    // }


}
