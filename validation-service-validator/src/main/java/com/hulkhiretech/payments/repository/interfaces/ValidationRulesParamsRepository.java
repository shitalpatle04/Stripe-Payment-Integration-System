package com.hulkhiretech.payments.repository.interfaces;

import java.util.Map;

/**
 * Repository for loading validator rule parameters from DB
 */
public interface ValidationRulesParamsRepository {

    /**
     * Returns:
     * ruleName → (paramName → paramValue)
     *
     * Example:
     * {
     *   "PAYMENT_ATTEMPT_THRESHOLD_RULE": {
     *       "durationInMins": "2",
     *       "maxPaymentThreshold": "5"
     *   }
     * }
     */
    Map<String, Map<String, String>> loadAllValidatorParams();
}