package com.learnavro.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnavro.domain.generated.PickUp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoffeeOrderDTO {//* This is the representation of the CoffeeOrder-create.json (Jackson-mapper is going to take care of conversion when we hit the rest-endpoint with body and mapping those values of json to DTOs)
    private String id;

    @NotBlank(message = "coffeeOrder.name is mandatory")
    private String name;
    private String nickName;

    @NotNull(message = "coffeeOrder.store is mandatory")
    @Valid
    private StoreDTO store;

    @NotNull(message = "coffeeOrder.orderLineItems is mandatory")
    @JsonProperty("orderLineItems")
    private List<OrderLineItemDTO> orderLineItems;
    private LocalDateTime orderedTime;

    @NotNull(message = "coffeeOrder.pickUp is mandatory")
    @JsonProperty("pickUp")
    private PickUp pickUp;// enum - trying to use the same avro generated class

    private String status;
}

/**
 * The `@JsonProperty("pickUp")` annotation is not part of bean validation but rather belongs to Jackson, a JSON processing library.
 *
 * This annotation serves the following purposes:
 *
 * 1. It explicitly maps the JSON property named "pickUp" to the Java field `pickUp` during deserialization
 * 2. When serializing the Java object to JSON, it ensures the field is named "pickUp" in the output
 *
 * While in this case the property name matches the field name (making it seem redundant), the annotation provides several benefits:
 *
 * - It makes the JSON mapping explicit
 * - It ensures the mapping remains intact even if the Java field is renamed
 * - It provides clarity about the expected JSON structure
 *
 * The bean validation for this field is handled by the separate `@NotNull` annotation on line 38, which ensures the field cannot be null when validating the object.
 */
