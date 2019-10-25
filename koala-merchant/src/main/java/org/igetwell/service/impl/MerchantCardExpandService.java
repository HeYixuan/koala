package org.igetwell.service.impl;

import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCardExpand;
import org.igetwell.merchant.card.mapper.MerchantCardExpandMapper;
import org.igetwell.service.IMerchantCardExpandService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class MerchantCardExpandService implements IMerchantCardExpandService {

    @Resource
    private MerchantCardExpandMapper merchantCardExpandMapper;

    /**
     * 根据会员卡ID查询
     * @param id
     * @return
     */
    @Override
    public MerchantCardExpand get(Long id) {
        return merchantCardExpandMapper.get(id);
    }

    @Override
    public ResponseEntity deleteById(Long id) {
        int i = merchantCardExpandMapper.deleteById(id);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity insert(MerchantCardExpand cardExpand) {
        if (!checkParam(cardExpand)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡ID不可为空!");
        }
        MerchantCardExpand expand = get(cardExpand.getId());
        if (!StringUtils.isEmpty(expand)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息已存在!");
        }
        int i = merchantCardExpandMapper.insert(cardExpand);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    @Override
    public ResponseEntity update(MerchantCardExpand cardExpand) {
        if (!checkParam(cardExpand)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "会员卡ID不可为空!");
        }
        MerchantCardExpand expand = get(cardExpand.getId());
        if (StringUtils.isEmpty(expand)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "此会员卡信息不存在!");
        }
        int i = merchantCardExpandMapper.update(cardExpand);
        if (i > 0){
            return ResponseEntity.ok();
        }
        return ResponseEntity.error();
    }

    private boolean checkParam(MerchantCardExpand cardExpand){
        if (StringUtils.isEmpty(cardExpand) || StringUtils.isEmpty(cardExpand.getMerchantCardId()) || !StringUtils.hasText(cardExpand.getMerchantNo())){
            return false;
        }
        return true;
    }
}
