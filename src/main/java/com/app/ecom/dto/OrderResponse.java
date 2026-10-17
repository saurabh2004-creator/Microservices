package com.app.ecom.dto;

import com.app.ecom.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;

    public <R> OrderResponse(Long id, BigDecimal totalAmount, OrderStatus status, Stream<R> rStream) {
    }
}
