package org.igetwell.wechat.sdk.bean.card;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.wechat.sdk.bean.card.create.WxCardCreate;

/**
 * 会员卡
 */
@Getter
@Setter
public class MemberCard extends AbstractCard {

    private AbstractMemberCard memberCard;

    public static void main(String[] args) {
        AbstractMemberCard memberCard = new AbstractMemberCard();
        memberCard.setBackgroundPicUrl("http://www.baidu.com");
        memberCard.setPrerogative("特权说明");
        WxCardBasis basis = new WxCardBasis();
        basis.setLogoUrl("http://mmbiz.qpic.cn/mmbiz/iaL1LJM1mF9aRKPZ/0");
        basis.setBrandName("海底捞");
        basis.setCodeType("CODE_TYPE_TEXT");
        basis.setTitle("海底捞会员卡");
        basis.setColor("Color010");
        basis.setNotice("使用时向服务员出示此卡");
        basis.setServicePhone("020-88888888");
        basis.setDescription("不可与其他优惠同享");
        basis.setGetLimit(1);
        WxCardHigh high = new WxCardHigh();

        memberCard.setBaseInfo(basis);
        memberCard.setAdvancedInfo(high);
        memberCard.setDiscount(30);


        MemberCard card = new MemberCard();
        card.setCardType("MEMBER_CARD");
        card.setMemberCard(memberCard);
        WxCardCreate create = new WxCardCreate();
        create.setCard(card);

        System.err.println(GsonUtils.toJson(card));
        //System.err.println(GsonUtils.toJson(create));
    }
}
