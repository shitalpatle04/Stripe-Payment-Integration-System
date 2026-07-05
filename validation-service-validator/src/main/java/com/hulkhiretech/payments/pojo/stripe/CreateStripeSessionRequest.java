package com.hulkhiretech.payments.pojo.stripe;

import java.util.List;

import com.hulkhiretech.payments.pojo.LineItem;

import lombok.Data;

@Data
public class CreateStripeSessionRequest {

    private String successUrl;

    private String cancelUrl;

    private List<LineItem> lineItems;
}