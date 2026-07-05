package com.hulkhiretech.payments.service.impl.businessvalidators;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.cache.ValidatorRuleCache;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.constant.ValidatorRuleEnum;
import com.hulkhiretech.payments.exception.PaymentValidationException;
import com.hulkhiretech.payments.pojo.PaymentRequest;
import com.hulkhiretech.payments.repository.interfaces.MerchantPaymentRequestRepository;
import com.hulkhiretech.payments.service.interfaces.BusinessValidator;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentAttemptThresholdValidator implements BusinessValidator {

	private final MerchantPaymentRequestRepository merchantReqRepo;
	
	private final ValidatorRuleCache validatorRuleCache; 
	
	@Override
	public void validate(PaymentRequest paymentRequest) {
		log.info("Validating payment attempt threshold for paymentRequest: {}", 
				paymentRequest);
		
		Map<String, String> paramsMap = validatorRuleCache.getValidatorRuleParams(
		        ValidatorRuleEnum.PAYMENT_ATTEMPT_THRESHOLD_RULE.getRuleName());
		
		log.debug("Loaded parameters for {}: {}", 
				ValidatorRuleEnum.PAYMENT_ATTEMPT_THRESHOLD_RULE.getRuleName(), 
				paramsMap); 
		

		paramsMap.get("durationMinutes"); // example of how to get parameters if we decide to externalize them
		
		String durationStr = paramsMap.getOrDefault("durationInMins", "5"); // fallback
		int durationInMinutes = Integer.parseInt(durationStr);
		
		
		int maxPaymentThreshold = 5; //this can also be externalized to config
		
		int count = merchantReqRepo.countRequestsForUserInLastMinutes(
				paymentRequest.getUser().getEndUserID(), durationInMinutes);
		
		log.info("Count of payment attempts for user {} in last {} minutes: {}",
				paymentRequest.getUser().getEndUserID(), 
				durationInMinutes, 
				count);
		
		if (count <= maxPaymentThreshold) {
			log.info("Payment request is valid, attempts count {} is within threshold of {}",
					count, maxPaymentThreshold);
			
		 return; // valid, allow processing to continue
		 
		}
		
		log.error("Payment request excceeds threshold, attempts count {} exceeds threshold of {}",
				count, maxPaymentThreshold);
		
		throw new PaymentValidationException(
				ErrorCodeEnum.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED.getErrorCode(),
				ErrorCodeEnum.PAYMENT_ATTEMPT_THRESHOLD_EXCEEDED.getErrorMessage(),
 				HttpStatus.TOO_MANY_REQUESTS);
	}

}
