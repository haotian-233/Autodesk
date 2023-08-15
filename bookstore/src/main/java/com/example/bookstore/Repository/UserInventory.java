package com.example.bookstore.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookstore.Entity.User;

@Repository
public interface UserInventory extends JpaRepository<User, String>{
}
