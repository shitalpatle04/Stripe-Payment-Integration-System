package com.hulkhiretech.payments.pojo;

import java.util.List;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "CreatePaymentReq", description = "Request payload to create a Stripe payment session.")
public class CreatePaymentReq {

    @Schema(description = "URL to redirect the user after successful payment.", example = "https://example.com/success", required = true)
    private String successUrl;

    @Schema(description = "URL to redirect the user if they cancel the payment.", example = "https://example.com/cancel", required = true)
    private String cancelUrl;
    
    @Schema(description = "List of line items included in the payment. At least one item is required.", required = true)
    List<LineItem> lineItems;
}