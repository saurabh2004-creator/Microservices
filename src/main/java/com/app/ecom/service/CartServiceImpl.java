package com.app.ecom.service;

import com.app.ecom.dto.CartItemRequest;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Product;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.ProductRepository;
import com.app.ecom.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public boolean addToCart(String userId, CartItemRequest request) {
        Optional<Product> productOpt =  productRepository.findById(request.getProductId());

        if(productOpt.isEmpty()){
            return false;
        }
        Product product = productOpt.get();
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){
            return false;
        }
        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(existingCartItem != null){
            //update the quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }else{
            //create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    @Override
    public boolean deleteItemFromCart(String userId, Long productId) {
        Optional<Product> productOpt =  productRepository.findById(productId);
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        if(productOpt.isPresent() &&  userOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }

        return false;
    }

    @Override
    public List<CartItem> fetchCartItems(String userId) {
        User user = userRepo.findById(Long.valueOf(userId)).get();
        return cartItemRepository.findByUser(user);
    }

    @Override
    public void clearCart(String userId) {
        userRepo.findById(Long.valueOf(userId)).ifPresent(cartItemRepository::deleteByUser);
    }
}
