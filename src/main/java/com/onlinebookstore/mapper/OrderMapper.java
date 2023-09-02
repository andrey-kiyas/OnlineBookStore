package com.onlinebookstore.mapper;

import com.onlinebookstore.config.MapperConfig;
import com.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id")
    })
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderResponseDto orderResponseDto);
}
