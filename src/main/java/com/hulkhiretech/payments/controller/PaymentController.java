package com.hulkhiretech.payments.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentResponse;
import com.hulkhiretech.payments.service.interfaces.PaymentsService;

import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
@Tag(name = "Payments", description = "APIs to create and manage payment sessions via Stripe")
public class PaymentController {

    private final PaymentsService paymentsService;

    public PaymentController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping
    @Operation(summary = "Create payment session", description = "Creates a Stripe checkout session for the provided line items and returns the session id and hosted page URL.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment session created successfully", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request payload"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public PaymentResponse createPayment(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create payment request containing success/cancel URLs and line items", required = true, content = @Content(schema = @Schema(implementation = CreatePaymentReq.class))) @RequestBody CreatePaymentReq createPaymentReq) {
        log.info("Creating payment... createPaymentReq: {}", createPaymentReq);

        PaymentResponse paymentResponse = paymentsService.createPayment(createPaymentReq);
        log.info("Payment creation response: {}", paymentResponse);

        return paymentResponse;
    }
}