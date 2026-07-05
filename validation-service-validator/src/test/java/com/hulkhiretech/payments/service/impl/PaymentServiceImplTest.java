//package com.hulkhiretech.payments.service.impl;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//
//import com.hulkhiretech.payments.cache.ValidatorRuleCache;
//import com.hulkhiretech.payments.constant.ErrorCodeEnum;
//import com.hulkhiretech.payments.exception.PaymentValidationException;
//import com.hulkhiretech.payments.pojo.PaymentRequest;
//import com.hulkhiretech.payments.service.impl.businessvalidators.DuplicateTxnValidator;
//
//@MockitoSettings(strictness = Strictness.LENIENT)
//@ExtendWith(MockitoExtension.class)
//public class PaymentServiceImplTest {
//
//    @Mock
//    private ApplicationContext applicationContext;
//
//    @Mock
//    private ValidatorRuleCache validatorRuleCache;
//
//    @Mock
//    private DuplicateTxnValidator duplicateTxnValidator;
//
//    @InjectMocks
//    private PaymentServiceImpl paymentService;
//
//    private PaymentRequest paymentRequest;
//
//    @BeforeEach
//    void setUp() {
//        paymentRequest = new PaymentRequest();
//    }
//
//    /**
//     * ✅ SUCCESS FLOW
//     * Validator exists and validation passes
//     */
//    @Test
//    void testValidateAndCreatePayment_Success() {
//
//        // Arrange
//        when(validatorRuleCache.getValidatorRules())
//                .thenReturn(List.of("DUPLICATE_TXN_RULE"));
//
//        when(applicationContext.getBean(DuplicateTxnValidator.class))
//                .thenReturn(duplicateTxnValidator);
//
//        doNothing().when(duplicateTxnValidator)
//                .validate(paymentRequest);
//
//        // Act
//        String response =
//                paymentService.validateAndCreatePayment(paymentRequest);
//
//        // Assert
//        assertEquals(
//                "From Service Payment created successfully!\n" + paymentRequest,
//                response);
//
//        verify(duplicateTxnValidator, times(1))
//                .validate(paymentRequest);
//    }
//
//    /**
//     * ✅ NULL RULES
//     */
//    @Test
//    void testValidateAndCreatePayment_WhenRulesAreNull() {
//
//        // Arrange
//        when(validatorRuleCache.getValidatorRules())
//                .thenReturn(null);
//
//        // Act + Assert
//        PaymentValidationException ex = assertThrows(
//                PaymentValidationException.class,
//                () -> paymentService.validateAndCreatePayment(paymentRequest)
//        );
//
//        assertEquals(
//                ErrorCodeEnum.NO_VALIDATION_RULES_CONFIGURED.getErrorCode(),
//                ex.getErrorCode());
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
//                ex.getHttpStatus());
//    }
//
//    /**
//     * ✅ EMPTY RULES
//     */
//    @Test
//    void testValidateAndCreatePayment_WhenRulesAreEmpty() {
//
//        // Arrange
//        when(validatorRuleCache.getValidatorRules())
//                .thenReturn(List.of());
//
//        // Act + Assert
//        PaymentValidationException ex = assertThrows(
//                PaymentValidationException.class,
//                () -> paymentService.validateAndCreatePayment(paymentRequest)
//        );
//
//        assertEquals(
//                ErrorCodeEnum.NO_VALIDATION_RULES_CONFIGURED.getErrorCode(),
//                ex.getErrorCode());
//    }
//
//    /**
//     * ✅ UNKNOWN RULE
//     * Rule not present in enum mapping
//     */
//    @Test
//    void testValidateAndCreatePayment_WhenRuleNotFound() {
//
//        // Arrange
//        when(validatorRuleCache.getValidatorRules())
//                .thenReturn(List.of("UNKNOWN_RULE"));
//
//        // Act
//        String response =
//                paymentService.validateAndCreatePayment(paymentRequest);
//
//        // Assert
//        assertEquals(
//                "From Service Payment created successfully!\n" + paymentRequest,
//                response);
//    }
//
//    /**
//     * ✅ VALIDATOR THROWS EXCEPTION
//     */
//    @Test
//    void testValidateAndCreatePayment_WhenValidatorThrowsException() {
//
//        // Arrange
//        when(validatorRuleCache.getValidatorRules())
//                .thenReturn(List.of("DUPLICATE_TXN_RULE"));
//
//        when(applicationContext.getBean(DuplicateTxnValidator.class))
//                .thenReturn(duplicateTxnValidator);
//
//        doNothing().when(duplicateTxnValidator)
//                .validate(paymentRequest);
//
//        PaymentValidationException exception =
//                new PaymentValidationException(
//                        "10029",
//                        "Duplicate transaction detected",
//                        HttpStatus.BAD_REQUEST);
//
//        org.mockito.Mockito.doThrow(exception)
//                .when(duplicateTxnValidator)
//                .validate(paymentRequest);
//
//        // Act + Assert
//        PaymentValidationException ex = assertThrows(
//                PaymentValidationException.class,
//                () -> paymentService.validateAndCreatePayment(paymentRequest)
//        );
//
//        assertEquals("10029", ex.getErrorCode());
//    }
//}