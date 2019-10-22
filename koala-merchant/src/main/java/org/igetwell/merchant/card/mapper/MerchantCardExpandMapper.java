package org.igetwell.merchant.card.mapper;

import org.igetwell.merchant.card.entity.MerchantCardExpand;

public interface MerchantCardExpandMapper {

    int deleteById(Long id);

    int insert(MerchantCardExpand card);

    /**
     * 根据会员卡ID查询
     * @param id
     * @return
     */
    MerchantCardExpand get(Long id);

    int update(MerchantCardExpand card);
}