package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.pojo.stripe.HostedPageUrlResponse;

/**
 * Service contract for validating and creating payments.
 */
public interface PaymentService {

    /**
     * Validate the given payment request and create the payment.
     *
     * @param paymentRequest the incoming payment request
     * @return a result message describing the outcome
     */
	HostedPageUrlResponse validateAndCreatePayment(
	        PaymentRequest paymentRequest);
}
