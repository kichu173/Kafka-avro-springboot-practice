package com.learnavro.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDTO {
    @NotNull(message = "coffeeOrder.store.storeId is mandatory")
    private Integer storeId;

    @Valid
    @NotNull(message = "coffeeOrder.store.address is mandatory")
    private AddressDTO address;
}

/**
 * The `@Valid` annotation enables cascading validation. When a `StoreDTO` object is validated:
 *
 * 1. The `@NotNull` annotation ensures the `address` field is not null
 * 2. The `@Valid` annotation triggers recursive validation of the `AddressDTO` object itself
 *
 * Without `@Valid`, the validation would only check if `address` is null, but wouldn't validate the contents of the `AddressDTO` object according to its own constraints (like the `@NotBlank` validations on all its fields).
 *
 * This ensures that when validating a complex object graph, all nested objects are properly validated according to their own validation rules.
 */
