package org.igetwell.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCard;
import org.igetwell.merchant.card.mapper.MerchantCardMapper;
import org.igetwell.service.IMerchantCardService;
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
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(MerchantCard card) {
        if (!checkParam(card)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡名称不可为空!");
        }
        MerchantCard merchantCard = get(card.getId());
        if (!StringUtils.isEmpty(merchantCard)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息已存在!");
        }
        int i = merchantCardMapper.insert(card);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(MerchantCard card) {
        if (!checkParam(card)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡名称不可为空!");
        }
        MerchantCard merchantCard = get(card.getId());
        if (StringUtils.isEmpty(merchantCard)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息不存在!");
        }
        int i = merchantCardMapper.update(card);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(MerchantCard card){
        if (StringUtils.isEmpty(card) || !StringUtils.hasText(card.getCardName())){
            return false;
        }
        return true;
    }
}
