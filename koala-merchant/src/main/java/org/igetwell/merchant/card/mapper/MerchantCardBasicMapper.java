package org.igetwell.merchant.card.mapper;

import org.igetwell.merchant.card.entity.MerchantCardBasic;

public interface MerchantCardBasicMapper {

    int deleteById(Long id);

    int insert(MerchantCardBasic card);

    /**
     * 根据会员卡ID查询
     * @param id
     * @return
     */
    MerchantCardBasic get(Long id);

    int update(MerchantCardBasic card);
}