package org.igetwell.wechat.sdk.card.bean;

import org.igetwell.common.enums.CardDateType;
import org.igetwell.common.enums.CardNameField;
import org.igetwell.common.enums.CardType;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.wechat.sdk.card.bean.create.WxCardCreate;

public class Main {

    public static void main(String [] args){


        WxMemberCard wxMemberCard = new WxMemberCard();
        wxMemberCard.setBackgroundPicUrl("http://www.baidu.com");

        WxCardBasis basis = new WxCardBasis();
        basis.setLogoUrl("http://mmbiz.qpic.cn/mmbiz/iaL1LJM1mF9aRKPZ/0");
        basis.setBrandName("海底捞");
        basis.setCodeType("CODE_TYPE_TEXT");
        basis.setTitle("海底捞会员卡");
        basis.setColor("Color010");



        WxCardDate cardDate = new WxCardDate();
        cardDate.setType(CardDateType.PERMANENT.getType()); //设置卡永久有效
        basis.setDateInfo(cardDate);

        WxCardSku sku = new WxCardSku();
        sku.setQuantity(100000000);
        basis.setSku(sku);
        basis.setServicePhone("0755-0010");
        wxMemberCard.setBaseInfo(basis);

        wxMemberCard.setSupplyBonus(true);
        wxMemberCard.setBonusUrl("http://www.mi.com");

        wxMemberCard.setSupplyBalance(true);
        wxMemberCard.setBalanceUrl("http://www.baidu.com");


        wxMemberCard.setPrerogative("特权说明");
        wxMemberCard.setBonusCleared("积分清零规则");
        wxMemberCard.setBalanceRules("储值规则说明");

        WxCardClazzField clazzField = new WxCardClazzField();
        clazzField.setNameType(CardNameField.FIELD_NAME_TYPE_COUPON.toString());
        clazzField.setName("优惠券");
        clazzField.setUrl("http://www.baidu.com");
        wxMemberCard.setCustomField1(clazzField);


        WxCardCustomerCell cell = new WxCardCustomerCell();
        cell.setName("商城入口");
        cell.setTips("商城入口");
        cell.setName("http://www.tmall.com");
        wxMemberCard.setCustomCell1(cell);

        WxCardHigh wxCardHigh = new WxCardHigh();

        wxMemberCard.setDiscount(0);


        WxCard card = new WxCard();
        card.setCardType(CardType.MEMBER_CARD.toString());
        card.setMemberCard(wxMemberCard);

        WxCardCreate<WxCard> wxCardCreate = new WxCardCreate<WxCard>();
        wxCardCreate.setCard(card);
        System.err.println(GsonUtils.toJson(wxCardCreate));
    }
}
