package com.onlinebookstore.dto.order;

import com.onlinebookstore.model.Order;
import lombok.Data;

@Data
public class OrderUpdateRequestDto {
    private Order.Status status;
}
