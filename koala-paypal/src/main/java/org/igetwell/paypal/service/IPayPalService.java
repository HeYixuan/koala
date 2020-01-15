package org.igetwell.paypal.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;

import java.util.Map;

public interface IPayPalService {


    ResponseEntity<Map<String, String>> scan(PayPalRequest payPalRequest);

    ResponseEntity refund(PayPalRefundRequest request);
}
