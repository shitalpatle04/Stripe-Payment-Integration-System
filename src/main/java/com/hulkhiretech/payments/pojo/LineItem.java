package com.hulkhiretech.payments.pojo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "LineItem", description = "A single line item in the payment request.")
public class LineItem {

    @Schema(description = "Three-letter ISO currency code for the item (lowercase or uppercase).", example = "usd", required = true)
    private String currency;

    @Schema(description = "Display name or description of the product being purchased.", example = "T-shirt - Blue, Size M", required = true)
    private String productName;

    @Schema(description = "Unit amount in the smallest currency unit (e.g., cents for USD). Must be a non-negative integer.", example = "1999", required = true)
    private int unitAmount;

    @Schema(description = "Quantity of the item being purchased. Must be at least 1.", example = "2", required = true)
    private int quantity; 
}