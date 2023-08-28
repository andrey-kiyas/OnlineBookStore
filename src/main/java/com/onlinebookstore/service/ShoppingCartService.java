package com.onlinebookstore.service;

import com.onlinebookstore.dto.cartitem.CartItemRequestDto;
import com.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartDtoByUserId(Long id);

    ShoppingCartResponseDto addCartItemByUserId(Long id, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long id, Long cartItemId,
                                           CartItemUpdateDto cartItemRequestDto);

    ShoppingCartResponseDto deleteCartItem(Long id, Long cartItemId);
}
