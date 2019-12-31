package org.igetwell.wechat.app.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.system.order.dto.request.ComponentPayRequest;

import javax.servlet.http.HttpServletRequest;

public interface IPayPalService {

    /**
     * 统一下单
     * @return
     */
    ResponseEntity paypal(HttpServletRequest request, ComponentPayRequest payRequest) throws Exception;
}
