package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;

import java.util.List;

public interface CartService {
    boolean addToCart(String userId, CartItemRequest request);

    boolean deleteItemFromCart(String userId, Long productId);

    List<CartItem> fetchCartItems(String userId);

    void clearCart(String userId);
}
