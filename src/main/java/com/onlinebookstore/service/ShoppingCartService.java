package com.onlinebookstore.service;

import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getShoppingCartById(Long id);

    ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id);

    ShoppingCartResponseDto addCartItemByUserId(Long id, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long id, Long cartItemId,
                                           CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId);
}
