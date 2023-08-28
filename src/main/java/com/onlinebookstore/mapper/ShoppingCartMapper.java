package com.onlinebookstore.mapper;

import com.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCart toEntity(ShoppingCartResponseDto shoppingCartResponseDto);
}
