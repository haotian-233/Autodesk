package com.example.bookstore.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bookstore.Entity.Book;
import com.example.bookstore.Entity.User;
import com.example.bookstore.Repository.BookInventory;
import com.example.bookstore.Repository.UserInventory;

@Service
public class BookService {

    @Autowired
    private BookInventory bookInventory;

    @Autowired
    private UserInventory userInventory;

    // Store UUID token in memory code block start
    private Map<String, String> tokenMap = new HashMap<>();

    private void storeToken(String username, String token) {
        tokenMap.put(username, token);
    }

    private String getToken(String username) {
        return tokenMap.get(username);
    }

    private void removeToken(String username) {
        tokenMap.remove(username);
    }
    
    private Boolean isTokenPresent(String username) {
        return tokenMap.containsKey(username);
    }

    private Map<String, String> userMap = new HashMap<>();

    private void storeTokenUser(String username, String token) {
        userMap.put(token, username);
    }

    private String getTokenUser(String token) {
        return userMap.get(token);
    }

    private void removeTokenUser(String token) {
        userMap.remove(token);
    }

    private Boolean isTokenUserPresent(String token) {
        return userMap.containsKey(token);
    }
    // Store UUID token in memory code block end

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

    public List<Book> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Long isbn,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean available,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Book> bookPage = bookInventory.searchBooks(title, author, isbn, minPrice, maxPrice, available, PageRequest.of(page, size));
        List<Book> books = bookPage.getContent();
        return books;
    }

    public void registerUser(User newUser) {
        // password would need to be encoded and salted in prod code
        userInventory.saveAndFlush(newUser);
    }

    private Optional<User> getUserByUsername(String username){
        return userInventory.findById(username);
    } 

    public Boolean authenticateUserPassword(User user){
        Optional<User> validUser = getUserByUsername(user.getUsername());
        if(validUser.isPresent() && !(validUser.get().getPassword()==user.getPassword())){
            return true;
        }else{
            return false;
        }
    }
    
    public String generateAndStoreUUIDv4Token(String username) {
        String token = UUID.randomUUID().toString();
        if(!isTokenPresent(username)){
            storeToken(username, token);
            storeTokenUser(username, token);
        }
        return token;
    }

    public Boolean authenticateUserToken(String token){
        if(isTokenUserPresent(token)){
            return true;
        }
        return false;
    }

    public Boolean logoutUser(String token){
        if(isTokenUserPresent(token)){
            String username = getTokenUser(token);
            removeTokenUser(token);
            removeToken(username);
            return true;
        }else{
            return false;
        }
    }
    
}
