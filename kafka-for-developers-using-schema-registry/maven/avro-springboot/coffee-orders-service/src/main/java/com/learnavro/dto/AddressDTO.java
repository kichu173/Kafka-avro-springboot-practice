package com.learnavro.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    @NotBlank(message = "coffeeOrder.store.address.addressLine1 is mandatory")
    private String addressLine1;

    @NotBlank(message = "coffeeOrder.store.address.city is mandatory")
    private String city;

    @NotBlank(message = "coffeeOrder.store.address.state is mandatory")
    private String state;

    @NotBlank(message = "coffeeOrder.store.address.country is mandatory")
    private String country;

    @NotBlank(message = "coffeeOrder.store.address.zip is mandatory")
    private String zip;
}
