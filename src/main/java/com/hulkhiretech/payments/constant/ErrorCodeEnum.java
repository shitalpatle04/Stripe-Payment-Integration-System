package com.hulkhiretech.payments.constant;

public enum ErrorCodeEnum {
    REQUEST_BODY_MISSING("30001", "Request body is missing"),
    SUCCESS_URL_MISSING("30002", "Success URL is missing"),
    SUCCESS_URL_INVALID("30003", "Success URL is not a valid URL"),
    CANCEL_URL_MISSING("30004", "Cancel URL is missing"),
    CANCEL_URL_INVALID("30005", "Cancel URL is not a valid URL"),
    LINE_ITEMS_MISSING("30006", "Line items are missing or empty"),

    LINE_ITEM_NULL("30007", "Line item at index %d is null"),
    LINE_ITEM_PRODUCT_NAME_MISSING("30008", "Line item productName is missing at index %d"),
    LINE_ITEM_CURRENCY_MISSING("30009", "Line item currency is missing at index %d"),
    LINE_ITEM_CURRENCY_INVALID("30010", "Line item currency must be a 3-letter ISO code at index %d"),
    LINE_ITEM_UNIT_AMOUNT_NEGATIVE("30011", "Line item unitAmount must be non-negative at index %d"),
    LINE_ITEM_QUANTITY_INVALID("30012", "Line item quantity must be at least 1 at index %d");

    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
