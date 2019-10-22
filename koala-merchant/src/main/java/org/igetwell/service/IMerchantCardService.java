package org.igetwell.service;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.entity.MerchantCard;

public interface IMerchantCardService {


    MerchantCard get(Long id);

    ResponseEntity deleteById(Long id);

    ResponseEntity insert(MerchantCard card);

    ResponseEntity update(MerchantCard card);
}
