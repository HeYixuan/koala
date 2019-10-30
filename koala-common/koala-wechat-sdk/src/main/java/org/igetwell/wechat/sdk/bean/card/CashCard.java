package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.common.uitls.GsonUtils;

/**
 * 代金券
 */
@Getter
@Setter
public class CashCard extends AbstractCard {

    private Cash cash;

    public static void main(String[] args) {
        Cash cash = new Cash();
        WxCardBasis basis = new WxCardBasis();
        WxCardHigh high = new WxCardHigh();
        cash.setBaseInfo(basis);
        cash.setAdvancedInfo(high);
        cash.setLeastCost(1000);
        cash.setReduceCost(100);

        CashCard card = new CashCard();
        card.setCardType("CASH");
        card.setCash(cash);

        System.err.println(GsonUtils.toJson(card));
    }
}
