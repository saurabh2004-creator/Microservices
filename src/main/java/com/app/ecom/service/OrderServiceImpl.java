package com.app.ecom.service;

import com.app.ecom.constants.OrderStatus;
import com.app.ecom.dto.OrderItemDTO;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.model.CartItem;
import com.app.ecom.model.Order;
import com.app.ecom.model.OrderItem;
import com.app.ecom.model.User;
import com.app.ecom.repository.CartItemRepository;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartService cartService;

    @Override
    public Optional<OrderResponse> createorder(String userId) {
        // validate cart item
        List<CartItem> cartItem  = cartService.fetchCartItems(userId);
        if(cartItem.isEmpty()){
            return Optional.empty();
        }

        //validate for user
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){
            Optional.empty();
        }
        User user = userOpt.get();

        //calculate total price
        BigDecimal totalPrice = cartItem.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //create Order

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItem.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //clear the cart

        cartService.clearCart(userId);

        return Optional.of(mapToorderResponse(savedOrder));
    }

    private OrderResponse mapToorderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getId(),
                                item.getProduct().getId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
