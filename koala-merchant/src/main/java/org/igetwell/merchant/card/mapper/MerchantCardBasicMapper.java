package org.igetwell.merchant.card.mapper;

import org.igetwell.merchant.card.entity.MerchantCardBasic;

public interface MerchantCardBasicMapper {

    int deleteById(Long id);

    int insert(MerchantCardBasic card);

    /**
     * 根据会员卡ID查询
     * @param cardId
     * @return
     */
    MerchantCardBasic get(String cardId);

    int update(MerchantCardBasic card);
}