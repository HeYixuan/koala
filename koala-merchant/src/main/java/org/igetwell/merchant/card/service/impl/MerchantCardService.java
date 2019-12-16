package org.igetwell.merchant.card.service.impl;

import org.igetwell.common.enums.*;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCard;
import org.igetwell.merchant.card.entity.MerchantCardBasic;
import org.igetwell.merchant.card.mapper.MerchantCardBasicMapper;
import org.igetwell.merchant.card.mapper.MerchantCardMapper;
import org.igetwell.merchant.card.service.IMerchantCardService;
import org.igetwell.wechat.sdk.bean.card.*;
import org.igetwell.wechat.sdk.bean.card.create.WxCardCreate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class MerchantCardService implements IMerchantCardService {

    @Resource
    private MerchantCardMapper merchantCardMapper;

    @Resource
    private MerchantCardBasicMapper cardBasicMapper;

    @Override
    public MerchantCard get(Long id) {
        return merchantCardMapper.get(id);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = merchantCardMapper.deleteById(id);
        if (i > 0) {
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(MerchantCard card) {
        if (!checkParam(card)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡名称不可为空!");
        }
        MerchantCard merchantCard = get(card.getId());
        if (!StringUtils.isEmpty(merchantCard)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息已存在!");
        }
        int i = merchantCardMapper.insert(card);
        if (i > 0) {
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(MerchantCard card) {
        if (!checkParam(card)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡名称不可为空!");
        }
        MerchantCard merchantCard = get(card.getId());
        if (StringUtils.isEmpty(merchantCard)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息不存在!");
        }
        int i = merchantCardMapper.update(card);
        if (i > 0) {
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(MerchantCard card) {
        if (StringUtils.isEmpty(card) || !StringUtils.hasText(card.getCardName())) {
            return false;
        }
        return true;
    }


    /**
     * 同步微信会员卡
     * @param cardId
     * @return
     */
    public ResponseEntity syncWechat(Long cardId){
        if (cardId == null){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡ID不可为空");
        }
        MerchantCard card = get(cardId);
        if (StringUtils.isEmpty(card) && StringUtils.isEmpty(card.getMerchantCardId())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息不存在!");
        }
        MerchantCardBasic basic = cardBasicMapper.get(card.getMerchantCardId());
        if (StringUtils.isEmpty(basic) && StringUtils.isEmpty(basic.getMerchantCardId())){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息不存在!");
        }
        String json = copyFieldToWxCard(card, basic);
        //http请求微信开卡
        return new ResponseEntity(json);
    }


    /**
     * 拷贝字段到微信会员卡字段里面
     *
     * @param card
     * @param basicCard
     */
    private static String copyFieldToWxCard(MerchantCard card, MerchantCardBasic basicCard) {
        WxMemberCard wxMemberCard = new WxMemberCard();

        if (!StringUtils.isEmpty(card.getCardBackUrl())) {
            wxMemberCard.setBackgroundPicUrl(card.getCardBackUrl());
        }

        if (!StringUtils.isEmpty(card.getPrivilege())) {
            wxMemberCard.setPrerogative(card.getPrivilege()); //特权说明
        }

        //设置储值、积分、清零规则
       /* wxMemberCard.setBonusRules("积分规则");
        wxMemberCard.setBalanceRules("储值规则");*/
        wxMemberCard.setBonusCleared("清零规则");
        wxMemberCard.setSupplyBonus(basicCard.getSupplyBonus());
        if (StringUtils.isEmpty(basicCard.getBonusUrl())){
            wxMemberCard.setBonusUrl(basicCard.getBonusUrl());
        }
        wxMemberCard.setSupplyBalance(basicCard.getSupplyBalance());
        if (StringUtils.isEmpty(basicCard.getBalanceUrl())){
            wxMemberCard.setBonusUrl(basicCard.getBalanceUrl());
        }

        //设置显示积分、余额、优惠券
        if (!StringUtils.isEmpty(basicCard.getDisplayField())) {
            String[] displayField = CharacterUtils.toArray(basicCard.getDisplayField(), ",");

            for (int i = 0; i < displayField.length; i++) {
               /* if (displayField[i].equalsIgnoreCase(CardNameField.FIELD_NAME_TYPE_SET_POINTS.getType())) {
                    wxMemberCard.setSupplyBonus(true); //显示积分
                    wxMemberCard.setBonusUrl("http://www.qq.com");
                }*/
                /*if (displayField[i].equalsIgnoreCase(CardNameField.FIELD_NAME_TYPE_TIMS.getType())) {
                    wxMemberCard.setSupplyBalance(true);
                    wxMemberCard.setBalanceUrl("http://www.mi.com");
                }*/
                if (displayField[i].equalsIgnoreCase(CardNameField.FIELD_NAME_TYPE_COUPON.getType())) {
                    WxCardClazzField customField1 = new WxCardClazzField();
                    customField1.setName(CardNameField.FIELD_NAME_TYPE_COUPON.getName());
                    customField1.setNameType(CardNameField.FIELD_NAME_TYPE_COUPON.getType());
                    customField1.setUrl("http://www.baidu.com");
                    wxMemberCard.setCustomField1(customField1);
                }
            }
        }

        //设置BaseInfo字段
        WxCardBasis basis = new WxCardBasis();
        basis.setLogoUrl(card.getBrandLogo());
        basis.setBrandName(card.getBrandName());
        basis.setTitle(card.getCardName());
        //如果卡背景图片是空,卡背景色不为空,则设置背景色
        if (StringUtils.isEmpty(wxMemberCard.getBackgroundPicUrl()) && !StringUtils.isEmpty(card.getCardBackColor())) {
            basis.setColor(card.getCardBackColor());
        }
        if (!StringUtils.isEmpty(card.getNotice())) {
            basis.setNotice(card.getNotice());
        }
        if (!StringUtils.isEmpty(card.getDescription())) {
            basis.setDescription(card.getDescription());
        }

        //设置营销入口
        WxCardCustomerCell customerCell = new WxCardCustomerCell();
        customerCell.setName("会员专享");
        customerCell.setTips("会员有礼");
        customerCell.setUrl("http://www.mi.com");
        wxMemberCard.setCustomCell1(customerCell);

        basis.setCustomUrlName("会员充值");
        basis.setCustomUrlSubTitle("充值有礼");
        basis.setCustomUrl("www.baidu.com");

        basis.setPromotionUrlName("积分商城");
        basis.setPromotionUrlSubTitle("营销入口");
        basis.setPromotionUrl("www.qq.com");

        //设置卡SKU
        WxCardSku sku = new WxCardSku();
        sku.setQuantity(100000000);
        basis.setSku(sku);

        //设置卡使用日期，有效期的信息
        WxCardDate dateInfo = new WxCardDate();
        if (basicCard.getValidType() != null) {
            //永久有效
            if (basicCard.getValidType().intValue() == CardDateType.PERMANENT.getValue()) {
                dateInfo.setType(CardDateType.PERMANENT.getType());
            }
            if (basicCard.getValidType().intValue() == CardDateType.FIX_DATE.getValue()) {
                dateInfo.setType(CardDateType.FIX_DATE.getType());
                dateInfo.setBeginTimestamp(basicCard.getBeginTime());
                dateInfo.setEndTimestamp(basicCard.getEndTime());
            }
            if (basicCard.getValidType().intValue() == CardDateType.FIX_LONG_TIME.getValue()) {
                dateInfo.setType(CardDateType.FIX_LONG_TIME.getType());
                dateInfo.setFixedTerm(basicCard.getBeginTime());
                dateInfo.setFixedBeginTerm(basicCard.getEndTime());
            }
        }

        basis.setDateInfo(dateInfo);

        //支持门店
        if (basicCard.supportStore()) {
            basis.setUseAllLocations(true);
        } else {
            String[] arr = CharacterUtils.toArray(basicCard.getSupportStores(), ",");
            Integer integer[] = CharacterUtils.toArray(arr);
            basis.setLocationIdList(integer);
        }

        //中部按钮类型不为空，且展示按钮，且文案不为空
        if (basicCard.getCenterType() != null && basicCard.getCenterType() != CardButtonType.NO_DISPLAY.getValue() && !StringUtils.isEmpty(basicCard.getCenterText())) {
            if (basicCard.getCenterType().intValue() == CardButtonType.MEMBER_PAY.getValue()) {
                basis.setCenterTitle(CardButtonType.MEMBER_PAY.getText()); //basicCard.getCenterText();
                basis.setCenterSubTitle("点击生成付款码"); //basicCard.getCenterSubText()
            }
            if (basicCard.getCenterType().intValue() == CardButtonType.WECHAT_PAY.getValue()) {
                basis.setCenterTitle(CardButtonType.WECHAT_PAY.getText());
            }
            if (basicCard.getCenterType().intValue() == CardButtonType.CUSTOM_PAY.getValue()) {
                basis.setCenterTitle(CardButtonType.CUSTOM_PAY.getText()); //basicCard.getCenterText();
                basis.setCenterSubTitle("买单立享9.00折，更有积分相送"); //basicCard.getCenterSubText()
                basis.setCenterUrl("http://www.mi.com"); //basicCard.getCenterUrl();
            }

        }

        //展示条形码方式
        if (basicCard.getBarType() != null) {
            if (basicCard.getBarType().intValue() == CardCodeType.BARCODE.getType()) {
                basis.setCodeType(CardCodeType.BARCODE.getValue());
            }
            if (basicCard.getBarType().intValue() == CardCodeType.QRCODE.getType()) {
                basis.setCodeType(CardCodeType.QRCODE.getValue());
            }
            if (basicCard.getBarType().intValue() == CardCodeType.TEXT.getType()) {
                basis.setCodeType(CardCodeType.TEXT.getValue());
            }
            if (basicCard.getBarType().intValue() == CardCodeType.ONLY_QRCODE.getType()) {
                basis.setCodeType(CardCodeType.ONLY_QRCODE.getValue());
            }
            if (basicCard.getBarType().intValue() == CardCodeType.ONLY_BARCODE.getType()) {
                basis.setCodeType(CardCodeType.ONLY_BARCODE.getValue());
            }
            if (basicCard.getBarType().intValue() == CardCodeType.NONE.getType()) {
                basis.setCodeType(CardCodeType.NONE.getValue());
            }
        }

        //激活方式
        if (basicCard.getActivateType() != null) {
            if (basicCard.getActivateType() == ActivateType.AUTO.getValue()) {
                wxMemberCard.setAutoActivate(true);
            }
            if (basicCard.getActivateType() == ActivateType.WX_ACTIVATE.getValue()) {
                wxMemberCard.setWxActivate(true);
            }
            if (basicCard.getActivateType() == ActivateType.CUSTOM_ACTIVATE.getValue()) {
                wxMemberCard.setActivateUrl("http://www.baidu.com"); //basicCard.getActivateUrl();
            }
        }
        //限领数量
        basis.setGetLimit(basicCard.getCardLimit());
        wxMemberCard.setBaseInfo(basis);

        MemberCard wxCard = new MemberCard();
        wxCard.setCardType(CardType.MEMBER_CARD.toString());
        wxCard.setMemberCard(wxMemberCard);

        WxCardCreate<MemberCard> wxCardCreate = new WxCardCreate<MemberCard>();
        wxCardCreate.setCard(wxCard);

        return GsonUtils.toJson(wxCardCreate);
    }

    public static void main(String[] args) {
        System.err.println(copyFieldToWxCard(null, null));
    }
}
