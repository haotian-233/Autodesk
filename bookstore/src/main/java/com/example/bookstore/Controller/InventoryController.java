package com.example.bookstore.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.User;
import com.example.bookstore.Service.BookService;

import io.micrometer.common.util.StringUtils;

@RestController
public class InventoryController {

    @Autowired
    private BookService bookService;

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody Book book, @RequestParam String token){
        if(!bookService.authenticateUserToken(token)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token! Operation not authorized. Please login first.");
        }
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
    public ResponseEntity<String> removeBook(@RequestParam Long isbn, @RequestParam String token){
        if(!bookService.authenticateUserToken(token)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token! Operation not authorized. Please login first.");
        }
        if(!bookService.isValidISBN(isbn)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The requested ISBN is not in correct format. It should be 13 digits long.");
        }
        if(!bookService.doesBookExists(isbn)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ISBN " + isbn + " is not found in our database!");
        }
        bookService.removeBook(isbn);
        return ResponseEntity.ok().body("ISBN " + isbn + " removed successfully.");
    }

    @GetMapping("/getQuantity")
    @Cacheable(value = "bookQuantities", key = "#isbn")
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
    @CacheEvict(value = "bookQuantities", key = "#isbn")
    public ResponseEntity<String> updateBookQuantityById(@RequestParam Long isbn, @RequestParam Integer quantity, @RequestParam String token){
        if(!bookService.authenticateUserToken(token)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token! Operation not authorized. Please login first.");
        }
        if(!bookService.isValidISBN(isbn)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The requested ISBN is not in correct format. It should be 13 digits long.");
        }
        if(!bookService.doesBookExists(isbn)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ISBN " + isbn + " is not found in our database!");
        }
        bookService.updateBookQuantityById(isbn, quantity);
        return ResponseEntity.ok().body("ISBN " + isbn + " quantity has been updated to " + quantity);
    }

    @GetMapping("/listAll")
    public ResponseEntity<List<Book>> listAllBooks(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size){
        
        List<Book> bookpage = bookService.listAllBooks(page, size);

        return ResponseEntity.ok(bookpage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long isbn,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Book> books = bookService.searchBooks(title, author, isbn, minPrice, maxPrice, available, page, size);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        if (StringUtils.isEmpty(newUser.getUsername()) || StringUtils.isEmpty(newUser.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password cannot be empty");
        }
        bookService.registerUser(newUser);
        return ResponseEntity.ok().body("registered.");
    }
    
    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password cannot be empty");
        }
        Boolean valid = bookService.authenticateUserPassword(user);
        if (valid){
            String token = bookService.generateAndStoreUUIDv4Token(user.getUsername());
            return ResponseEntity.ok(token);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username does not exist or password does not match. Please register first if you didn't.");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> loginOutUser(@RequestParam String token) {
        if (StringUtils.isEmpty(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token cannot be empty");
        }
        Boolean isSuccess = bookService.logoutUser(token);
        if(isSuccess){
            return ResponseEntity.ok().body("logout successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalide token.");
        }        
    }

}
