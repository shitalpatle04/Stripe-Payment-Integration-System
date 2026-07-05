package com.hulkhiretech.payments.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hulkhiretech.payments.repository.interfaces.ValidationRulesParamsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ValidationRulesParamsRepositoryImpl implements ValidationRulesParamsRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String FETCH_PARAMS_SQL = """
            SELECT validatorName, paramName, paramValue
            FROM validations.validation_rules_params
            """;

    @Override
    public Map<String, Map<String, String>> loadAllValidatorParams() {

        log.info("Loading validator rule parameters from DB");

        List<Map<String, Object>> rows = namedParameterJdbcTemplate.queryForList(
                FETCH_PARAMS_SQL,
                new MapSqlParameterSource()
        );

        Map<String, Map<String, String>> result = new HashMap<>();

        for (Map<String, Object> row : rows) {

            String validatorName = (String) row.get("validatorName");
            String paramName = (String) row.get("paramName");
            String paramValue = (String) row.get("paramValue");

            result
                .computeIfAbsent(validatorName, k -> new HashMap<>())
                .put(paramName, paramValue);
        }

        log.info("Loaded validator params: {}", result);

        return result;
    }
}