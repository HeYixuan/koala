package org.igetwell.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WxCardEvent {

    CARD_PASS_CHECK("卡券通过审核", "card_pass_check"),
    CARD_NOT_PASS_CHECK("卡券未通过审核", "card_not_pass_check"),
    USER_GET_CARD("用户领取卡券", "user_get_card"),
    USER_GIFTING_CARD("用户在转赠卡券", "user_gifting_card"),
    USER_DEL_CARD("用户删除卡券","user_del_card"),
    USER_CONSUME_CARD("核销事件", "user_consume_card"),
    USER_PAY_FROM_PAY_CELL("微信买单事件","User_pay_from_pay_cell"),
    USER_VIEW_CARD("用户点击会员卡","user_view_card"),
    USER_ENTER_SESSION_FROM_CARD("用户从卡券进入公众号会话","user_enter_session_from_card"),
    CARD_SKU_REMIND("库存报警","card_sku_remind"),
    CARD_PAY_ORDER("券点流水详情事件","card_pay_order"),
    CARD_ACTIVITY("会员卡激活事件推送", "submit_membercard_user_info");
    private String text;
    private String value;

    public java.lang.String getText() {
        return text;
    }

    public void setText(java.lang.String text) {
        this.text = text;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }
}
