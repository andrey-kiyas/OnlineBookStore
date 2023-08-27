package com.onlinebookstore.service;

import com.onlinebookstore.dto.cart_item.CartItemRequestDto;
import com.onlinebookstore.dto.shopping_cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(String email);

    ShoppingCartResponseDto addCartItem(String email, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(String email, Long cartItemId,
                                           CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto deleteCartItem(String email, Long cartItemId);
}
