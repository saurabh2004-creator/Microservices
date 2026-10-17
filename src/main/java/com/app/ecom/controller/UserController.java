package com.app.ecom.controller;


import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.User;
import com.app.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> addUsers(@RequestBody UserRequest user) {
        userService.addUsers(user);
        return new ResponseEntity<>("User added successfully", HttpStatus.OK);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUsers(@RequestBody UserRequest user,@PathVariable long id) {
        boolean updateduser = userService.updateUsers(user,id);
        if(updateduser) {
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

}
