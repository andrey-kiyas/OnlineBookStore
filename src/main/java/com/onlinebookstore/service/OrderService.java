package com.onlinebookstore.service;

import com.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.onlinebookstore.model.Order;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto create(Long id, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> findAllOrders(Long id, Pageable pageable);

    void updateOrderStatus(Long orderId, Order.Status status);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId);

    OrderItemResponseDto findOrderItemById(Long orderId, Long itemId);
}
