package com.hulkhiretech.payments.pojo;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(name = "PaymentResponse", description = "Response returned after creating a payment session.")
public class PaymentResponse {

    @Schema(description = "Stripe-created session ID. Use this to reference the session in Stripe APIs.", example = "cs_test_a1b2c3d4e5")
    private String stripeSessionId;

    @Schema(description = "URL to the Stripe-hosted checkout page where the user completes payment.", example = "https://checkout.stripe.com/pay/cs_test_a1b2c3d4e5")
    private String hostedPageUrl;
}