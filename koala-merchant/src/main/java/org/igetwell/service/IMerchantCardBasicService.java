package org.igetwell.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCardBasic;

public interface IMerchantCardBasicService {


    /**
     * 根据会员卡ID查询
     * @param id
     * @return
     */
    MerchantCardBasic get(Long id);

    ResponseEntity deleteById(Long id);

    ResponseEntity insert(MerchantCardBasic card);

    ResponseEntity update(MerchantCardBasic card);
}
