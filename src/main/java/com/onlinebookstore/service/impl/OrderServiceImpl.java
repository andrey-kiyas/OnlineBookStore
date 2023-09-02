package com.onlinebookstore.service.impl;

import com.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.OrderItemMapper;
import com.onlinebookstore.mapper.OrderMapper;
import com.onlinebookstore.model.Order;
import com.onlinebookstore.model.Order.Status;
import com.onlinebookstore.model.OrderItem;
import com.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.repository.OrderRepository;
import com.onlinebookstore.repository.ShoppingCartRepository;
import com.onlinebookstore.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto create(Long id, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find shopping cart by id: " + id)
                );
        Order order = new Order();
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setUser(shoppingCart.getUser());
        order.setStatus(Order.Status.PENDING);
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setPrice(cartItem.getBook().getPrice()
                            .multiply(new BigDecimal(cartItem.getQuantity())));
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        BigDecimal total = orderItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> findAllOrders(Pageable pageable) {
        return orderRepository.findAllOrders(pageable).stream()
                .map(orderMapper::toDto).toList();
    }

    @Override
    public void updateOrderStatus(Long orderId, Status status) {
        orderRepository.updateOrderByStatus(orderId, status);
    }

    @Override
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId)
                );
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    public OrderItemResponseDto findOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order by id: " + orderId));
        return order.getOrderItems().stream()
                .filter(o -> o.getId().equals(itemId))
                .findFirst()
                .map(orderItemMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find item by id: " + itemId
                                + ", with order id:  " + orderId)
                );
    }
}
