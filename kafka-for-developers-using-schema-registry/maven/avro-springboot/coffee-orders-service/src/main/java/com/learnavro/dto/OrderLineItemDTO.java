package com.learnavro.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotBlank(message = "coffeeOrder.orderLineItem.name is mandatory")
    private String name;

    @NotNull(message = "coffeeOrder.orderLineItem.size is mandatory")
    private Size size;// enum - trying to use the same avro generated class

    @NotNull(message = "coffeeOrder.orderLineItem.size is mandatory")
    private Integer quantity;

    @NotNull
    private BigDecimal cost;
}

/**
 * The `@NotBlank` annotation means that the annotated String field:
 *
 * 1. Must not be `null`
 * 2. Must not be an empty string (`""`)
 * 3. Must not contain only whitespace characters
 *
 * It ensures that the string has at least one non-whitespace character. The field can still contain spaces, but it cannot be just spaces or empty.
 *
 * This differs from:
 * - `@NotNull` - Only checks that the value isn't null
 * - `@NotEmpty` - Checks that a string, collection, map, or array isn't null and has at least one element
 *
 * In your code, it's enforcing that the `name` field must contain actual text content.
 */
