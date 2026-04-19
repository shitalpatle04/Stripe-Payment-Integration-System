package com.hulkhiretech.payments.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.StripeProviderException;
import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.LineItem;

@Service
public class ValidationService {

    /**
     * Validates the CreatePaymentReq paySload. Throws StripeProviderException with
     * sequential error codes starting at 30001 for each validation failure.
     */
    public void isValid(CreatePaymentReq req) {
        if (req == null) {
            throw new StripeProviderException(
                    ErrorCodeEnum.REQUEST_BODY_MISSING.getErrorCode(),
                    ErrorCodeEnum.REQUEST_BODY_MISSING.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (req.getSuccessUrl() == null || req.getSuccessUrl().trim().isEmpty()) {
            throw new StripeProviderException(
                    ErrorCodeEnum.SUCCESS_URL_MISSING.getErrorCode(),
                    ErrorCodeEnum.SUCCESS_URL_MISSING.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!isValidUrl(req.getSuccessUrl())) {
            throw new StripeProviderException(
                    ErrorCodeEnum.SUCCESS_URL_INVALID.getErrorCode(),
                    ErrorCodeEnum.SUCCESS_URL_INVALID.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (req.getCancelUrl() == null || req.getCancelUrl().trim().isEmpty()) {
            throw new StripeProviderException(
                    ErrorCodeEnum.CANCEL_URL_MISSING.getErrorCode(),
                    ErrorCodeEnum.CANCEL_URL_MISSING.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        if (!isValidUrl(req.getCancelUrl())) {
            throw new StripeProviderException(
                    ErrorCodeEnum.CANCEL_URL_INVALID.getErrorCode(),
                    ErrorCodeEnum.CANCEL_URL_INVALID.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        List<LineItem> items = req.getLineItems();
        if (items == null || items.isEmpty()) {
            throw new StripeProviderException(
                    ErrorCodeEnum.LINE_ITEMS_MISSING.getErrorCode(),
                    ErrorCodeEnum.LINE_ITEMS_MISSING.getErrorMessage(), HttpStatus.BAD_REQUEST);
        }

        for (int i = 0; i < items.size(); i++) {
            LineItem item = items.get(i);
            if (item == null) {
                throw new StripeProviderException(
                        ErrorCodeEnum.LINE_ITEM_NULL.getErrorCode(),
                        String.format(ErrorCodeEnum.LINE_ITEM_NULL.getErrorMessage(), i),
                        HttpStatus.BAD_REQUEST);
            }

            if (item.getProductName() == null || item.getProductName().trim().isEmpty()) {
                throw new StripeProviderException(
                        ErrorCodeEnum.LINE_ITEM_PRODUCT_NAME_MISSING.getErrorCode(),
                        String.format(ErrorCodeEnum.LINE_ITEM_PRODUCT_NAME_MISSING.getErrorMessage(), i),
                        HttpStatus.BAD_REQUEST);
            }

            if (item.getCurrency() == null || item.getCurrency().trim().isEmpty()) {
                throw new StripeProviderException(
                        ErrorCodeEnum.LINE_ITEM_CURRENCY_MISSING.getErrorCode(),
                        String.format(ErrorCodeEnum.LINE_ITEM_CURRENCY_MISSING.getErrorMessage(), i),
                        HttpStatus.BAD_REQUEST);
            }

            // Currency should be 3 letters
            if (item.getCurrency() != null && item.getCurrency().trim().length() != 3) {
                throw new StripeProviderException(
                        ErrorCodeEnum.LINE_ITEM_CURRENCY_INVALID.getErrorCode(),
                        String.format(ErrorCodeEnum.LINE_ITEM_CURRENCY_INVALID.getErrorMessage(), i),
                        HttpStatus.BAD_REQUEST);
            }

            if (item.getUnitAmount() < 0) {
                throw new StripeProviderException(
                        ErrorCodeEnum.LINE_ITEM_UNIT_AMOUNT_NEGATIVE.getErrorCode(),
                        String.format(ErrorCodeEnum.LINE_ITEM_UNIT_AMOUNT_NEGATIVE.getErrorMessage(), i),
                        HttpStatus.BAD_REQUEST);
            }

            if (item.getQuantity() <= 0) {
                throw new StripeProviderException(
                        ErrorCodeEnum.LINE_ITEM_QUANTITY_INVALID.getErrorCode(),
                        String.format(ErrorCodeEnum.LINE_ITEM_QUANTITY_INVALID.getErrorMessage(), i),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }

    private boolean isValidUrl(String url) {
        try {
            URL u = new URL(url);
            String protocol = u.getProtocol();
            return "http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol);
        } catch (MalformedURLException e) {
            return false;
        }
    }
}