package com.app.ecom.controller;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/api/cart")
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                          @RequestBody CartItemRequest request){
        if(!cartService.addToCart(userId,request)){
           return ResponseEntity.badRequest().body("Product out of stock or User not found or product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/api/cart/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable Long productId){
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/cart")
    public ResponseEntity<List<CartItem>> fetchallCartItem(@RequestHeader("X-User-ID") String userId){

        return new ResponseEntity<>(cartService.fetchCartItems(userId),HttpStatus.OK);
    }
}
