package com.learnavro.dto;

import java.time.LocalDateTime;
import java.util.List;

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
    private String name;
    private String nickName;
    private StoreDTO store;
    private List<OrderLineItemDTO> orderLineItems;
    private LocalDateTime orderedTime;
    private PickUp pickUp;
    private String status;
}
