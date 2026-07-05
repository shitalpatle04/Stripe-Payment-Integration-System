package com.hulkhiretech.payments.exception;

import org.springframework.http.HttpStatus;

public class StripeProviderException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public StripeProviderException(
            String errorCode,
            String errorMessage,
            HttpStatus httpStatus) {

        super(errorMessage);

        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}