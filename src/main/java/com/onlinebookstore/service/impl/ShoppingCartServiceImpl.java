package com.onlinebookstore.service.impl;

import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.exception.EntityNotFoundException;
import com.onlinebookstore.mapper.CartItemMapper;
import com.onlinebookstore.mapper.ShoppingCartMapper;
import com.onlinebookstore.model.CartItem;
import com.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.model.User;
import com.onlinebookstore.repository.CartItemRepository;
import com.onlinebookstore.repository.ShoppingCartRepository;
import com.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addCartItem(String email,
                                               CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = cartItemMapper.toEntity(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(String email,
                                                  Long cartItemId,
                                                  CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItemDb = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by item id: " + cartItemId)
        );
        CartItem cartItemDto = cartItemMapper.toEntity(cartItemRequestDto);
        cartItemDb.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItemDb);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(String email, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        cartItemRepository.deleteById(cartItemId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Can't find user by user email: " + email)
        );
        return shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by id " + user.getId())
        );
    }
}
