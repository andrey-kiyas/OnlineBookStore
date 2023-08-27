package com.onlinebookstore.mapper;

import com.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.dto.cart_item.CartItemRequestDto;
import com.onlinebookstore.dto.cart_item.CartItemUpdateDto;
import com.onlinebookstore.model.Book;
import com.onlinebookstore.model.CartItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);

    @Mapping(target = "book", ignore = true)
    CartItem toEntity(CartItemUpdateDto cartItemUpdateDto);

    @AfterMapping
    default void setBookId(@MappingTarget CartItem cartItem, CartItemRequestDto cartItemRequestDto) {
        Book book = new Book();
        book.setId(cartItemRequestDto.getBookId());
        cartItem.setBook(book);
    }
}
