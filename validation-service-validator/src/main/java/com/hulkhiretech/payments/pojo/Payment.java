package com.hulkhiretech.payments.pojo;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Payment {

    @Pattern(
        regexp = "^[A-Z]{3}$",
        message = "CURRENCY_INVALID"
    )
    private String currency;

    @NotNull(message = "AMOUNT_MISSING")
    @Positive(message = "AMOUNT_INVALID")
    private Integer amount;

    @NotBlank(message = "BRAND_NAME_MISSING")
    private String brandName;

    @NotBlank(message = "LOCALE_MISSING")
    private String locale;

    @NotBlank(message = "COUNTRY_MISSING")
    private String country;

    @NotBlank(message = "MERCHANT_TXN_REF_MISSING")
    private String merchantTxnRef;

    @NotBlank(message = "PAYMENT_METHOD_MISSING")
    private String paymentMethod;

    @NotBlank(message = "PROVIDER_MISSING")
    private String provider;

    @NotBlank(message = "PAYMENT_TYPE_MISSING")
    private String paymentType;

    @NotBlank(message = "SUCCESS_URL_MISSING")
    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "SUCCESS_URL_INVALID"
    )
    private String successUrl;

    @NotBlank(message = "CANCEL_URL_MISSING")
    @Pattern(
        regexp = "^(http|https)://.*$",
        message = "CANCEL_URL_INVALID"
    )
    private String cancelUrl;

    @NotEmpty(message = "LINE_ITEMS_EMPTY")
    @Valid
    private List<LineItem> lineItems;
}