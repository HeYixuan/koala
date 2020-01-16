package org.igetwell.system.order.factory;

import org.igetwell.common.uitls.SpringContextHolder;
import org.igetwell.common.uitls.WebUtils;
import org.igetwell.system.order.service.IOrderCreateStrategy;
import org.igetwell.system.order.service.impl.AliOrderStrategy;
import org.igetwell.system.order.service.impl.UnionOrderStrategy;
import org.igetwell.system.order.service.impl.WxOrderStrategy;

public class OrderCreateFactory {

    public static IOrderCreateStrategy getStrategy(){
        if (WebUtils.isWechat()){
            return SpringContextHolder.getBean(WxOrderStrategy.class);
        }
        if (WebUtils.isAliPay()){
            return SpringContextHolder.getBean(AliOrderStrategy.class);
        }
        if (WebUtils.isSafari()){
            return SpringContextHolder.getBean(UnionOrderStrategy.class);
        }
        throw new IllegalArgumentException("不支持此支付方式");
    }
}
