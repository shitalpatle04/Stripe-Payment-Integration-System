package com.hulkhiretech.payments.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.cache.ValidatorRuleCache;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.constant.ValidatorRuleEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import com.hulkhiretech.payments.helper.StripeProviderHelper;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.http.HttpServiceEngine;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.pojo.stripe.HostedPageUrlResponse;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ApplicationContext applicationContext;
    
    private final ValidatorRuleCache validatorRuleCache;
    
    private final StripeProviderHelper stripeProviderHelper;

    private final HttpServiceEngine httpServiceEngine;

    // cached configs
    private List<String> validatorRules;
    
    private Map<String, Map<String, String>> validatorRuleParams;

    @Override
    public HostedPageUrlResponse validateAndCreatePayment(
            PaymentRequest paymentRequest) {

        log.info("Validating and creating payment: {}", paymentRequest);
        
        List<String> validatorRules = validatorRuleCache.getValidatorRules();
        log.debug("Loaded validator rules from cache: {}", validatorRules);

        // 🔴 IMPORTANT CHECK
        if (validatorRules == null || validatorRules.isEmpty()) {
            log.error("No validator rules configured, cannot process payment");

            throw new PaymentValidationException(
                    ErrorCodeEnum.NO_VALIDATION_RULES_CONFIGURED.getErrorCode(),
                    ErrorCodeEnum.NO_VALIDATION_RULES_CONFIGURED.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        for (String rule : validatorRules) {

            log.info("Applying validation rule: {}", rule);

            Optional<Class<? extends BusinessValidator>> validatorClass =
                    ValidatorRuleEnum.getValidatorClassByRule(rule);

            if (!validatorClass.isPresent()) {
                log.warn("No validator found for rule: {}", rule);
                continue;
            }

            BusinessValidator validator =
                    applicationContext.getBean(validatorClass.get());

            validator.validate(paymentRequest);
        }

        log.info("All validations passed");

        /*
         * Prepare request for stripe-provider-service
         */
        HttpRequest httpRequest =
                stripeProviderHelper.prepareStripeProviderRequest(
                        paymentRequest);

        log.info("Prepared HttpRequest for stripe-provider-service: {}",
                httpRequest);

        /*
         * Call stripe-provider-service
         */
        ResponseEntity<String> response =
                httpServiceEngine.makeHttpCall(httpRequest);

        log.info("Received response from stripe-provider-service: {}",
                response);

        /*
         * Process response
         */
        HostedPageUrlResponse hostedPageUrlResponse =
                stripeProviderHelper.processStripeProviderResponse(
                        response);

        log.info("Processed HostedPageUrlResponse: {}",
                hostedPageUrlResponse);

        return hostedPageUrlResponse;
    }
}