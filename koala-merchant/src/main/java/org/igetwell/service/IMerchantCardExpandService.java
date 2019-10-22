package org.igetwell.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCard;
import org.igetwell.merchant.card.entity.MerchantCardExpand;

public interface IMerchantCardExpandService {


    /**
     * 根据会员卡ID查询
     * @param id
     * @return
     */
    MerchantCardExpand get(Long id);

    ResponseEntity deleteById(Long id);

    ResponseEntity insert(MerchantCardExpand card);

    ResponseEntity update(MerchantCardExpand card);
}
