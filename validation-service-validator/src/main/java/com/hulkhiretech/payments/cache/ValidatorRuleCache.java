package com.hulkhiretech.payments.cache;

import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payments.repository.interfaces.ValidationRulesParamsRepository;
import com.hulkhiretech.payments.repository.interfaces.ValidationRulesRepository;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ValidatorRuleCache {

	private static final String VALIDATOR_RULE_PARAMS_PREFIX = "validator-rule-params:";

	private static final String VALIDATOR_RULES_KEY = "validator-rules";

	@Getter
	private List<String> validatorRules;

	//private Map<String, Map<String, String>> validatorRuleParams;


	//repositories to load rules and params from DB
	private final ValidationRulesRepository validationRulesRepository;

	private final ValidationRulesParamsRepository validationRulesParamsRepository;

	private final RedisTemplate<String, String> redisTemplate; // for future use if we decide to cache in Redis instead of in-memory

	private ListOperations<String, String> listOperations;
	private HashOperations<String, String, String> hashOperations;

	public ValidatorRuleCache(
			ValidationRulesRepository validationRulesRepository,
			ValidationRulesParamsRepository validationRulesParamsRepository,
			RedisTemplate<String, String> redisTemplate) {
		this.validationRulesRepository = validationRulesRepository;
		this.validationRulesParamsRepository = validationRulesParamsRepository;
		this.redisTemplate = redisTemplate;

		this.listOperations = redisTemplate.opsForList();
		this.hashOperations = redisTemplate.opsForHash();
	}



	public List<String> getValidatorRules() {
		//read list values using listOperations for redis key "validator-rules"
		return listOperations.range(VALIDATOR_RULES_KEY, 0, -1); // example of how to read from Redis if we decide to cache there
	}
	/*
	 * 	write method to set validator rules in Redis cache. take List<String> 
	 * 	as input and write to Redis key "validator-rules"
	 */
	public void setValidatorRules(List<String> rules) {
		//write list values using listOperations for redis key "validator-rules"

		if (rules != null && !rules.isEmpty()) {
			//delete existing list in Redis before writing new values
			redisTemplate.delete(VALIDATOR_RULES_KEY);
			listOperations.rightPushAll(VALIDATOR_RULES_KEY, rules);

			log.info("Updated validator rules in Redis cache with {} rules", 
					rules.size());
		} else {
			log.warn("Attempted to set empty or null validator rules list, skipping Redis update");
		}
	}
	
	/*
	 * get validatorRule params for the given rule name. 
	 * Read from redis hash key "valdator-rule-params:{validatorName}" 
	 * and field as paraName and value as paramValue and return as Map<String, String>
	 */
	public Map<String, String> getValidatorRuleParams(String ruleName) {
		String redisKey = VALIDATOR_RULE_PARAMS_PREFIX + ruleName;
		return hashOperations.entries(redisKey);
	}
	

	/*
	 * write a method which takes Map<STring, Map<String, String>> as input 
	 * and stores in redis hash with key "validator-rule-params : {validatorName}"
	 * and field as paraName and value as paramValue.
	 * 
	 */
	public void setValidatorRuleParams(Map<String, Map<String, String>> validatorRuleParams) {
		//if null or empty then return without doing anything
		if (validatorRuleParams == null || validatorRuleParams.isEmpty()) {
			log.info("No validator rule params to update in redis cache; cleared all existing");
			return;
		}


		for (Map.Entry<String, Map<String, String>> entry : validatorRuleParams.entrySet()) {
			String validatorName = entry.getKey();
			Map<String, String> params = entry.getValue();
			String redisKey = VALIDATOR_RULE_PARAMS_PREFIX + validatorName;

			// delete existing hash for this validator
			redisTemplate.delete(redisKey);

			if (params != null && !params.isEmpty()) {
				hashOperations.putAll(redisKey, params);
				log.info("Updated params for validator {} in Redis cache: {}",
						validatorName, params);
			} else {
				log.info("No params to set for validator {}, skipping Redis update", 
						validatorName);
			}
		}

	}

	/*
	public Map<String, String> getValidatorParamsForRule(String ruleName) {
		return validatorRuleParams.getOrDefault(ruleName, Map.of());
	}
	 */

	//init method to load rules and params from DB at startup
	@PostConstruct
	public void init() {

		try {

			List<String> validatorRules = getValidatorRules();

			//if value is validatorRules then return. No need of further DB call
			if(validatorRules != null && !validatorRules.isEmpty()) {
				this.validatorRules = validatorRules;
				log.info("Validator rules already loaded in Redis cache, skipping DB load");
				return;
			}

			log.info("Loaded validator rules from Redis cache: size={}, rules={}", 
					validatorRules != null ? validatorRules.size() : 0, validatorRules);


			// ✅ Load rules
			List<String> dbRules =
					validationRulesRepository.loadActiveValidatorNamesOrderedByPriority();

			if (dbRules != null && !dbRules.isEmpty()) {
				setValidatorRules(dbRules); // also update Redis cache with DB values
				this.validatorRules = dbRules;
				log.info("Loaded {} validator rules from DB", dbRules.size());

			} else {
				//no rules found in DB, use empty list to avoid null checks later
				//				this.validatorRules = List.of();
				log.info("No validator rules found in DB; using empty rule set");
			}

			// ✅ Load params
			Map<String, Map<String, String>> validatorRuleParams =
					validationRulesParamsRepository.loadAllValidatorParams();

			if (validatorRuleParams == null) {
				validatorRuleParams = Map.of();
			}

			setValidatorRuleParams(validatorRuleParams); // also update Redis cache with DB values
			
			log.info("Loaded validator rule config entries: {}", validatorRuleParams.size());

		} catch (Exception ex) {
			log.error("Failed to load validator rules or params from DB", ex);
		}
	}
}
