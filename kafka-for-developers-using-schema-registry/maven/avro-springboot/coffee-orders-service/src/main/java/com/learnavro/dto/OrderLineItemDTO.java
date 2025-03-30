package com.learnavro.dto;

import java.math.BigDecimal;

import com.learnavro.domain.generated.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineItemDTO {
    private String name;
    private Size size;
    private Integer quantity;
    private BigDecimal cost;
}
