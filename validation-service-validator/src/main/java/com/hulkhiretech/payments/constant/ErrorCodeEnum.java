package com.hulkhiretech.payments.constant;

public enum ErrorCodeEnum {

	
	GENERIC_ERROR("10000", "An unexpected error occurred. "
			+ "Please try again later."),
	
    /* -----------------------------
       PAYMENT REQUEST VALIDATION
       ----------------------------- */

    USER_MISSING("10001", "user object must not be null"),
    PAYMENT_MISSING("10002", "payment object must not be null"),

    /* -----------------------------
       USER VALIDATION
       ----------------------------- */

    USER_ID_MISSING("10003", "endUserID must not be blank"),
    FIRSTNAME_MISSING("10004", "firstname must not be blank"),
    LASTNAME_MISSING("10005", "lastname must not be blank"),

    EMAIL_MISSING("10006", "email must not be blank"),
    EMAIL_INVALID("10007", "email must be a valid email address"),

    MOBILE_PHONE_MISSING("10008", "mobilePhone must not be blank"),
    MOBILE_PHONE_INVALID("10009", "mobilePhone must be a valid phone number"),

    /* -----------------------------
       PAYMENT VALIDATION
       ----------------------------- */

    CURRENCY_INVALID("10010", "currency must be a valid 3-letter ISO code"),
    
    AMOUNT_INVALID("10011", "amount must be greater than 0"),
    AMOUNT_MISSING("10012", "amount must not be null"),

    BRAND_NAME_MISSING("10013", "brandName must not be blank"),
    LOCALE_MISSING("10014", "locale must not be blank"),
    COUNTRY_MISSING("10015", "country must not be blank"),

    MERCHANT_TXN_REF_MISSING("10016", "merchantTxnRef must not be blank"),
    PAYMENT_METHOD_MISSING("10017", "paymentMethod must not be blank"),
    PROVIDER_MISSING("10018", "provider must not be blank"),
    PAYMENT_TYPE_MISSING("10019", "paymentType must not be blank"),

    SUCCESS_URL_MISSING("10020", "successUrl must not be blank"),
    SUCCESS_URL_INVALID("10021", "successUrl must be a valid HTTP/HTTPS URL"),

    CANCEL_URL_MISSING("10022", "cancelUrl must not be blank"),
    CANCEL_URL_INVALID("10023", "cancelUrl must be a valid HTTP/HTTPS URL"),

    /* -----------------------------
       LINE ITEM VALIDATION
       ----------------------------- */

    LINE_ITEMS_EMPTY("10024", "lineItems must not be empty"),

    PRODUCT_NAME_INVALID("10025", "productName must not be blank"),
    UNIT_AMOUNT_INVALID("10026", "unitAmount must be greater than 0"),
    QUANTITY_INVALID("10027", "quantity must be greater than 0"), 
    
    FIRSTNAME_CONTAINS_HELLO("10028", "firstname must not contain 'hello'"),
    LASTNAME_CONTAINS_HELLO("10029", "lastname must not contain 'hello'"), 
    DUPLICATE_TRANSACTION("10030", "Duplicate transaction detected for the given merchantTxnReference"), 
    FAILED_TO_SAVE_PAYMENT_REQUEST("10031", "Failed to save the payment request. Please try again later."),
    
    INVALID_HMAC("10032", "Invalid HMAC signature. Authentication failed."), 
    MISSING_HMAC("10033", "HMAC signature is missing in the request header. "
    		+ "Authentication failed."), 
    HMAC_COMPUTATIOM_ERROR("10034", "Error occurred while computing HMAC signature. "
    		+ "Please try again later."), 
    PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED("10035", "Too many payment attempts in a short period. Please try again later."),
    
    NO_VALIDATION_RULES_CONFIGURED("10036", "No validation rules configured in system"),
    
    STRIPE_PROVIDER_ERROR("10037", "Stripe provider service error"),

    STRIPE_PROVIDER_UNAVAILABLE("10038", "Unable to connect to stripe provider service"),

    INTERNAL_SERVER_ERROR("10039", "Unexpected internal server error");

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