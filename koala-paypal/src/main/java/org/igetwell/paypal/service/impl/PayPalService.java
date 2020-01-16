package org.igetwell.paypal.service.impl;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.dto.request.PayPalRequest;
import org.igetwell.paypal.factory.StrategyFactory;
import org.igetwell.paypal.service.IPayPalService;
import org.igetwell.paypal.service.IPayStrategy;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PayPalService implements IPayPalService {



    public ResponseEntity<Map<String, String>> scan(PayPalRequest request) {
        IPayStrategy payStrategy = StrategyFactory.getPayStrategy(request.getChannel());
        return payStrategy.scan(request);
    }

    public ResponseEntity refund(PayPalRefundRequest refundRequest) {
        IPayStrategy payStrategy = StrategyFactory.getPayStrategy(refundRequest.getChannel());
        return payStrategy.refund(refundRequest);
    }
}
