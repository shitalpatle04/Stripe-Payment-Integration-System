package com.hulkhiretech.payments.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.exception.StripeProviderException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentResponse;
import com.hulkhiretech.payments.service.ValidationService;
import com.hulkhiretech.payments.service.helper.CreatePaymentHelper;
import com.hulkhiretech.payments.service.interfaces.PaymentsService;
import com.hulkhiretech.payments.stripe.CheckoutSessionResponse;
import com.hulkhiretech.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentsService {

	private final HttpServiceEngine httpServiceEngine;
	
	private final CreatePaymentHelper createPaymentHelper;
	
	private final JsonUtil jsonUtil;

	private final ValidationService validationService;
	 
	@Override
	public PaymentResponse createPayment(CreatePaymentReq createPaymentReq) {
		log.info("Processing payment creation logic... createPaymentReq: {}", 
				createPaymentReq);
		
		// Validate request
		validationService.isValid(createPaymentReq);
		
		HttpRequest httpRequest = createPaymentHelper
				.prepareStripeCreateSessionRequest(createPaymentReq);
		
		ResponseEntity<String> httpResponse = httpServiceEngine.makeHttpCall(httpRequest);
		log.info("Received response from HttpServiceEngine: {}", httpResponse);
		
		// Convert response body to CheckoutSessionResponse and log it
		CheckoutSessionResponse sessionResponse = jsonUtil.convertJsonToObject(httpResponse.getBody(), CheckoutSessionResponse.class);
		log.info("CheckoutSessionResponse: {}", sessionResponse);
		
		PaymentResponse paymentResponse = mapCheckoutSessionToPaymentResponse(sessionResponse);
		log.info("Mapped PaymentResponse: {}", paymentResponse);
		
		return paymentResponse;
	}
	
	/*
	 * Write a map method to take CheckoutSessionResponse and convert it to 
	 * a PaymentResponse which is out internal response object. 
	 * This way we 
	 */
	
	public PaymentResponse mapCheckoutSessionToPaymentResponse(
			CheckoutSessionResponse sessionResponse) {
		if (sessionResponse == null) {
			log.warn("mapCheckoutSessionToPaymentResponse called with null sessionResponse");
			return null;
		}
		
		PaymentResponse paymentResponse = new PaymentResponse();
		paymentResponse.setStripeSessionId(sessionResponse.getId());
		paymentResponse.setHostedPageUrl(sessionResponse.getUrl());
		
		
		log.info("Mapped CheckoutSessionResponse to PaymentResponse: {}", paymentResponse);
		return paymentResponse;
	}
}