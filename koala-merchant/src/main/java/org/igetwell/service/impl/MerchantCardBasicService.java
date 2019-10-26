package org.igetwell.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCardBasic;
import org.igetwell.merchant.card.mapper.MerchantCardBasicMapper;
import org.igetwell.service.IMerchantCardBasicService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class MerchantCardBasicService implements IMerchantCardBasicService {

    @Resource
    private MerchantCardBasicMapper cardBasicMapper;

    /**
     * 根据会员卡ID查询
     * @param id
     * @return
     */
    @Override
    public MerchantCardBasic get(Long id) {
        return cardBasicMapper.get(id);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = cardBasicMapper.deleteById(id);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(MerchantCardBasic cardBasic) {
        if (!checkParam(cardBasic)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡ID不可为空!");
        }
        MerchantCardBasic basic = get(cardBasic.getId());
        if (!StringUtils.isEmpty(basic)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息已存在!");
        }
        int i = cardBasicMapper.insert(cardBasic);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(MerchantCardBasic cardBasic) {
        if (!checkParam(cardBasic)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡ID不可为空!");
        }
        MerchantCardBasic basic = get(cardBasic.getId());
        if (StringUtils.isEmpty(basic)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息不存在!");
        }
        int i = cardBasicMapper.update(cardBasic);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(MerchantCardBasic cardBasic){
        if (StringUtils.isEmpty(cardBasic) || StringUtils.isEmpty(cardBasic.getMerchantCardId()) || !StringUtils.hasText(cardBasic.getMerchantNo())){
            return false;
        }
        return true;
    }
}
