package org.igetwell.paypal.factory;

import org.igetwell.common.enums.PayChannel;
import org.igetwell.common.uitls.SpringContextHolder;
import org.igetwell.paypal.service.IPayStrategy;
import org.igetwell.paypal.service.impl.AliPayStrategy;
import org.igetwell.paypal.service.impl.UnionPayStrategy;
import org.igetwell.paypal.service.impl.WxPayStrategy;

public class StrategyFactory {

    public static IPayStrategy getPayStrategy(PayChannel channel) {
        switch (channel) {
            case WECHAT:
                return SpringContextHolder.getBean(WxPayStrategy.class);
            case ALIPAY:
                return SpringContextHolder.getBean(AliPayStrategy.class);
            case UNIONPAY:
                return SpringContextHolder.getBean(UnionPayStrategy.class);
            default:
                throw new IllegalArgumentException("支付渠道错误!请联系技术人员");
        }
    }
}
