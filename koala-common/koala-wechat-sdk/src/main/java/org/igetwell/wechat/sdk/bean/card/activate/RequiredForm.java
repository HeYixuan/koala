package org.igetwell.wechat.sdk.bean.card.activate;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券激活设置字段-必填字段
 */
@Getter
@Setter
public class RequiredForm {

    /**
     * 当前结构（required_form或者optional_form ）内 的字段是否允许用户激活后再次修改，商户设置为true 时，需要接收相应事件通知处理修改事件
     */
    private Boolean canModify;

    /**
     * 必填字段
     */
    String [] commonFieldIdList;
}
