package org.igetwell.web;

import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.merchant.card.service.impl.MerchantCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memberCard")
public class MemberCardController extends BaseController {

    @Autowired
    private MerchantCardService merchantCardService;

    @PostMapping("/syncWechat/{cardId}")
    public ResponseEntity syncWechat(@PathVariable("cardId") Long cardId){
        return merchantCardService.syncWechat(cardId);
    }

}
