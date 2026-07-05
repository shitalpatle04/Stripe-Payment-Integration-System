package com.hulkhiretech.payments.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.pojo.stripe.CreateStripeSessionRequest;
import com.hulkhiretech.payments.pojo.stripe.HostedPageUrlResponse;
import com.hulkhiretech.payments.util.JsonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StripeProviderHelper {

    @Value("${stripe-provider.create-session-url}")
    private String stripeProviderUrl;

    private final JsonUtil jsonUtil;

    public HttpRequest prepareStripeProviderRequest(
            PaymentRequest paymentRequest) {

        CreateStripeSessionRequest stripeRequest =
                new CreateStripeSessionRequest();

        stripeRequest.setSuccessUrl(
                paymentRequest.getPayment().getSuccessUrl());

        stripeRequest.setCancelUrl(
                paymentRequest.getPayment().getCancelUrl());

        stripeRequest.setLineItems(
                paymentRequest.getPayment().getLineItems());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpRequest httpRequest = new HttpRequest();

        httpRequest.setHttpMethod(HttpMethod.POST);
        httpRequest.setUrl(stripeProviderUrl);
        httpRequest.setHttpHeaders(headers);
        httpRequest.setRequestData(stripeRequest);

        log.info("Prepared stripe-provider request : {}",
                httpRequest);

        return httpRequest;
    }

    public HostedPageUrlResponse processStripeProviderResponse(
            ResponseEntity<String> responseEntity) {

        log.info("Processing stripe-provider response");

        HostedPageUrlResponse response =
                jsonUtil.convertJsonToObject(
                        responseEntity.getBody(),
                        HostedPageUrlResponse.class);

        log.info("Processed response : {}", response);

        return response;
    }
}