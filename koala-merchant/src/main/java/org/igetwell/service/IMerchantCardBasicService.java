package org.igetwell.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCardBasic;

public interface IMerchantCardBasicService {


    /**
     * 根据会员卡ID查询
     * @param cardId
     * @return
     */
    MerchantCardBasic get(String cardId);

    ResponseEntity deleteById(Long id);

    ResponseEntity insert(MerchantCardBasic card);

    ResponseEntity update(MerchantCardBasic card);
}
