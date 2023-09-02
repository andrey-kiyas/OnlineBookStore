package com.onlinebookstore.mapper;

import com.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.dto.orderitem.OrderItemResponseDto;
import com.onlinebookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mappings({
            @Mapping(target = "bookId", source = "book.id")
    })
    OrderItemResponseDto toDto(OrderItem orderItem);
}
