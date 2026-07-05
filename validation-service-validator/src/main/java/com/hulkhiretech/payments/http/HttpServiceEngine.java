package com.hulkhiretech.payments.http;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.exception.StripeProviderException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpServiceEngine {

    private final RestClient restClient;

    public ResponseEntity<String> makeHttpCall(
            HttpRequest httpRequest) {

        try {

            log.info("Making HTTP call : {}", httpRequest);

            ResponseEntity<String> response =
                    restClient
                            .method(httpRequest.getHttpMethod())
                            .uri(httpRequest.getUrl())
                            .headers(headers ->
                                    headers.addAll(
                                            httpRequest.getHttpHeaders()))
                            .body(httpRequest.getRequestData())
                            .retrieve()
                            .toEntity(String.class);

            log.info("Received HTTP response : {}", response);

            return response;

        } catch (HttpClientErrorException | HttpServerErrorException ex) {

            log.error("Stripe provider returned error response", ex);

            throw new StripeProviderException(
                    ErrorCodeEnum.STRIPE_PROVIDER_ERROR.getErrorCode(),
                    ex.getResponseBodyAsString(),
                    HttpStatus.valueOf(ex.getStatusCode().value())
            );

        } catch (RestClientException ex) {

            log.error("Unable to connect to stripe provider service",
                    ex);

            throw new StripeProviderException(
                    ErrorCodeEnum.STRIPE_PROVIDER_UNAVAILABLE.getErrorCode(),
                    ErrorCodeEnum.STRIPE_PROVIDER_UNAVAILABLE.getErrorMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}