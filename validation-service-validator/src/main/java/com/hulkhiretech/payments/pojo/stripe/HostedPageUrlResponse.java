package com.hulkhiretech.payments.pojo.stripe;

import lombok.Data;

@Data
public class HostedPageUrlResponse {

    private String stripeSessionId;

    private String hostedPageUrl;
}