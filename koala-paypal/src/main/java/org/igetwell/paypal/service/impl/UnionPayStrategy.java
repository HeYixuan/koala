package org.igetwell.paypal.service.impl;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.service.IPayStrategy;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UnionPayStrategy implements IPayStrategy {

    @Override
    public ResponseEntity<Map<String, String>> scan(PayPalRequest request) {
        return null;
    }

    @Override
    public ResponseEntity refund(PayPalRefundRequest request) {
        return null;
    }
}
