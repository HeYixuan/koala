package org.igetwell.wechat.app.service;

import org.igetwell.common.uitls.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IPayService {

    /**
     * 统一下单
     * @return
     */
    ResponseEntity componentPay(HttpServletRequest request, String amount) throws Exception;
}
