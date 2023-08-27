package com.onlinebookstore.dto.shopping_cart;

import com.onlinebookstore.dto.cart_item.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
