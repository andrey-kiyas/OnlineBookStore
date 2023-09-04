package com.onlinebookstore.controller;

import com.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.onlinebookstore.model.Order.Status;
import com.onlinebookstore.model.User;
import com.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing users orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place an order", description = "Place an order")
    @PostMapping
    public OrderResponseDto create(Authentication authentication,
                                   @RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.create(user.getId(), orderRequestDto);
    }

    @Operation(summary = "Retrieve user's order history",
            description = "Retrieve user's order history")
    @GetMapping
    public List<OrderResponseDto> findAll(Authentication authentication,
                                          Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrders(user.getId(), pageable);
    }

    @Operation(summary = "Update order status", description = "Update order status")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody OrderUpdateRequestDto orderUpdateRequestDto) {
        orderService.updateOrderStatus(id,
                Status.valueOf(String.valueOf(orderUpdateRequestDto.getStatus())));
    }

    @Operation(summary = "Retrieve all OrderItems for a specific order",
            description = "Retrieve all OrderItems for a specific order")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    public Set<OrderItemResponseDto> findAllOrderItems(@PathVariable Long orderId) {
        return orderService.findAllOrderItems(orderId);
    }

    @Operation(summary = "Retrieve a specific OrderItem within an order",
            description = "Retrieve a specific OrderItem within an order")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    public OrderItemResponseDto findOrderItemById(@PathVariable Long orderId,
                                                  @PathVariable Long itemId) {
        return orderService.findOrderItemById(orderId, itemId);
    }
}
