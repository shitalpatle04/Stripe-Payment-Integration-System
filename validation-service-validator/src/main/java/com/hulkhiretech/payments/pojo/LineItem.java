package com.hulkhiretech.payments.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LineItem {

    @Pattern(
        regexp = "^[A-Z]{3}$",
        message = "CURRENCY_INVALID"
    )
    private String currency;

    @NotBlank(message = "PRODUCT_NAME_INVALID")
    private String productName;

    @Positive(message = "UNIT_AMOUNT_INVALID")
    private Integer unitAmount;

    @Positive(message = "QUANTITY_INVALID")
    private Integer quantity;
}