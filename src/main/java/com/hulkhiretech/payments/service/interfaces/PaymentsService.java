package com.hulkhiretech.payments.service.interfaces;

import com.hulkhiretech.payments.pojo.CreatePaymentReq;
import com.hulkhiretech.payments.pojo.PaymentResponse;

public interface PaymentsService {

	PaymentResponse createPayment(CreatePaymentReq createPaymentReq);
}
