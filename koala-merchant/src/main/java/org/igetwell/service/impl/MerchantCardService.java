package org.igetwell.service.impl;

import org.igetwell.common.enums.*;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCard;
import org.igetwell.merchant.card.entity.MerchantCardExpand;
import org.igetwell.merchant.card.mapper.MerchantCardMapper;
import org.igetwell.service.IMerchantCardService;
import org.igetwell.wechat.sdk.card.bean.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class MerchantCardService implements IMerchantCardService {

    @Resource
    private MerchantCardMapper merchantCardMapper;

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
     * 拷贝字段到微信会员卡字段里面
     *
     * @param card
     * @param expand
     */
    private void copyFieldToWxCard(MerchantCard card, MerchantCardExpand expand) {
        WxMemberCard memberCard = new WxMemberCard();

        if (!StringUtils.isEmpty(card.getCardBackUrl())) {
            memberCard.setBackgroundPicUrl(card.getCardBackUrl());
        }

        if (!StringUtils.isEmpty(expand.getCardPrivilege())) {
            memberCard.setPrerogative(expand.getCardPrivilege()); //特权说明
        }
        if (!StringUtils.isEmpty(expand.getCardDisplayField())) {
            String[] displayField = CharacterUtils.toArray(expand.getCardDisplayField(), ",");

            for (int i = 0; i < displayField.length; i++) {
                if (displayField[i].equalsIgnoreCase(CardNameField.FIELD_NAME_TYPE_SET_POINTS.getType())) {
                    memberCard.setSupplyBonus(true); //显示积分
                    memberCard.setBonusUrl("http://www.qq.com");
                }
                if (displayField[i].equalsIgnoreCase(CardNameField.FIELD_NAME_TYPE_TIMS.getType())) {
                    memberCard.setSupplyBalance(true);
                    memberCard.setBalanceUrl("http://www.mi.com");
                }
                if (displayField[i].equalsIgnoreCase(CardNameField.FIELD_NAME_TYPE_COUPON.getType())) {
                    WxCardClazzField customField1 = new WxCardClazzField();
                    customField1.setName(CardNameField.FIELD_NAME_TYPE_COUPON.getName());
                    customField1.setNameType(CardNameField.FIELD_NAME_TYPE_COUPON.getType());
                    customField1.setUrl("http://www.baidu.com");
                    memberCard.setCustomField1(customField1);
                }
            }
        }

        //设置BaseInfo字段
        WxCardBasis basis = new WxCardBasis();
        basis.setLogoUrl(card.getBrandLogo());
        basis.setBrandName(card.getBrandName());
        basis.setTitle(card.getCardName());
        //如果卡背景图片是空,卡背景色不为空,则设置背景色
        if (StringUtils.isEmpty(memberCard.getBackgroundPicUrl()) && !StringUtils.isEmpty(card.getCardBackColor())) {
            basis.setColor(card.getCardBackColor());
        }
        if (!StringUtils.isEmpty(expand.getCardNotice())) {
            basis.setNotice(expand.getCardNotice());
        }
        if (!StringUtils.isEmpty(expand.getCardDescription())) {
            basis.setDescription(expand.getCardDescription());
        }

        //设置卡SKU
        WxCardSku sku = new WxCardSku();
        sku.setQuantity(100000000);
        basis.setSku(sku);

        //设置卡使用日期，有效期的信息
        WxCardDate dateInfo = new WxCardDate();
        if (expand.getCardValidType() != null) {
            //永久有效
            if (expand.getCardValidType().intValue() == CardDateType.PERMANENT.getValue()) {
                dateInfo.setType(CardDateType.PERMANENT.getType());
            }
            if (expand.getCardValidType().intValue() == CardDateType.FIX_DATE.getValue()) {
                dateInfo.setType(CardDateType.FIX_DATE.getType());
                dateInfo.setBeginTimestamp(expand.getCardBegin());
                dateInfo.setEndTimestamp(expand.getCardEnd());
            }
            if (expand.getCardValidType().intValue() == CardDateType.FIX_LONG_TIME.getValue()) {
                dateInfo.setType(CardDateType.FIX_LONG_TIME.getType());
                dateInfo.setFixedTerm(expand.getCardBegin());
                dateInfo.setFixedBeginTerm(expand.getCardEnd());
            }
        }

        basis.setDateInfo(dateInfo);


        if (expand.isSupportStore()) {
            basis.setUseAllLocations(true);
        } else {
            String[] arr = CharacterUtils.toArray(expand.getSupportStores(), ",");
            Integer integer[] = CharacterUtils.toArray(arr);
            basis.setLocationIdList(integer);
        }

        //中部按钮类型不为空，且展示按钮，且文案不为空
        if (expand.getCardCenterType() != null && expand.getCardCenterType() != CardButtonType.NO_DISPLAY.getValue() && !StringUtils.isEmpty(expand.getCardCenterButton())) {
            if (expand.getCardCenterType().intValue() == CardButtonType.WECHAT_PAY.getValue()) {
                basis.setCenterTitle(CardButtonType.MEMBER_PAY.getText());
                basis.setCenterSubTitle(expand.getCardCenterButton());
                basis.setCenterSubTitle("点击生成付款码");
            }
            if (expand.getCardCenterType().intValue() == CardButtonType.WECHAT_PAY.getValue()) {
                basis.setCenterTitle(CardButtonType.WECHAT_PAY.getText());
            }
            if (expand.getCardCenterType().intValue() == CardButtonType.CUSTOM_PAY.getValue()) {
                basis.setCenterTitle(CardButtonType.CUSTOM_PAY.getText());
                basis.setCenterSubTitle("买单立享9.00折，更有积分相送");
                basis.setCenterUrl("http://www.mi.com");
            }

        }

        if (expand.getCardDisplayBar() != null) {
            if (expand.getCardDisplayBar().intValue() == CardCodeType.BARCODE.getType()) {
                basis.setCodeType(CardCodeType.BARCODE.getValue());
            }
            if (expand.getCardDisplayBar().intValue() == CardCodeType.QRCODE.getType()) {
                basis.setCodeType(CardCodeType.QRCODE.getValue());
            }
            if (expand.getCardDisplayBar().intValue() == CardCodeType.TEXT.getType()) {
                basis.setCodeType(CardCodeType.TEXT.getValue());
            }
            if (expand.getCardDisplayBar().intValue() == CardCodeType.ONLY_QRCODE.getType()) {
                basis.setCodeType(CardCodeType.ONLY_QRCODE.getValue());
            }
            if (expand.getCardDisplayBar().intValue() == CardCodeType.ONLY_BARCODE.getType()) {
                basis.setCodeType(CardCodeType.ONLY_BARCODE.getValue());
            }
            if (expand.getCardDisplayBar().intValue() == CardCodeType.NONE.getType()) {
                basis.setCodeType(CardCodeType.NONE.getValue());
            }
        }

        //激活方式
        if (expand.getActivateType() != null) {
            if (expand.getActivateType() == ActivateType.AUTO.getValue()) {
                memberCard.setAutoActivate(true);
            }
            if (expand.getActivateType() == ActivateType.WX_ACTIVATE.getValue()) {
                memberCard.setWxActivate(true);
            }
            if (expand.getActivateType() == ActivateType.CUSTOM_ACTIVATE.getValue()) {
                memberCard.setActivateUrl("http://www.baidu.com");
            }
        }
        memberCard.setBaseInfo(basis);
    }
}
