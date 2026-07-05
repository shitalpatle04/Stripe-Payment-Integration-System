package com.hulkhiretech.payments.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.pojo.stripe.HostedPageUrlResponse;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/payments")
@Slf4j
@RequiredArgsConstructor
//@RefreshScope
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public HostedPageUrlResponse createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest) {

        log.info("Creating payment... paymentRequest: {}", paymentRequest);

        // Validate and create the payment using the service layer
        HostedPageUrlResponse serviceResponse =
                paymentService.validateAndCreatePayment(paymentRequest);

        log.info("Payment creation result: {}", serviceResponse);

        return serviceResponse;
    }
    
    @GetMapping
    public String getPaymentStatus() {
    	log.info("Fetching payment status... This endpoint is under construction.");
		return "Payment status endpoint is under construction.";
    }
}   