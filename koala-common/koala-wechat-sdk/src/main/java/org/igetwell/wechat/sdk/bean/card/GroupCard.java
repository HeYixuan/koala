package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.common.uitls.GsonUtils;

/**
 * 团购券
 */
@Getter
@Setter
public class GroupCard extends AbstractCard {

    private Group groupon;

    public static void main(String[] args) {
        Group group = new Group();
        WxCardBasis basis = new WxCardBasis();
        WxCardHigh high = new WxCardHigh();
        group.setBaseInfo(basis);
        group.setAdvancedInfo(high);
        group.setDealDetail("团购有优惠");

        GroupCard card = new GroupCard();
        card.setCardType("GROUPON");
        card.setGroupon(group);

        System.err.println(GsonUtils.toJson(card));
    }
}
