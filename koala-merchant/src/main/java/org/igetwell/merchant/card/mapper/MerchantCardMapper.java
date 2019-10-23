package org.igetwell.merchant.card.mapper;

import org.igetwell.merchant.card.entity.MerchantCard;

/**
 * MerchantCardMapper继承基类
 */
public interface MerchantCardMapper {

    int deleteById(Long id);

    int insert(MerchantCard card);

    MerchantCard get(Long id);

    int update(MerchantCard card);
}