package org.igetwell.wechat.app.web;


import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.app.service.IWxCardService;
import org.igetwell.wechat.app.service.IWxCouponService;
import org.igetwell.wechat.sdk.bean.card.*;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.bean.card.white.White;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 微信卡券
 */
@RestController
@RequestMapping("/wx/card")
public class WxCardController {

    @Autowired
    private IWxCardService iWxCardService;

    @Autowired
    private IWxCouponService iWxCouponService;

    /**
     * 创建微信会员卡
     */
    @PostMapping("/createCard")
    public ResponseEntity createCard(@RequestBody WxMemberCard wxMemberCard) throws Exception {
        String cardId = iWxCouponService.createCard(wxMemberCard);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 创建微信团购券
     */
    @PostMapping("/createGroup")
    public ResponseEntity createGroup(@RequestBody Group group) throws Exception {
        String cardId = iWxCouponService.createGroup(group);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 创建微信代金券
     */
    @PostMapping("/createCash")
    public ResponseEntity createCash(@RequestBody Cash cash) throws Exception {
        String cardId = iWxCouponService.createCash(cash);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 创建微信兑换券
     */
    @PostMapping("/createGift")
    public ResponseEntity createGift(@RequestBody Gift gift) throws Exception {
        String cardId = iWxCouponService.createGift(gift);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 创建微信折扣券
     */
    @PostMapping("/createDiscount")
    public ResponseEntity createDiscount(@RequestBody Discount discount) throws Exception {
        String cardId = iWxCouponService.createDiscount(discount);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 创建微信优惠券
     */
    @PostMapping("/createCoupon")
    public ResponseEntity createCoupon(@RequestBody Coupon coupon) throws Exception {
        String cardId = iWxCouponService.createCoupon(coupon);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 统一创建微信会员卡券
     */
    @PostMapping("/create")
    public ResponseEntity create(@RequestParam(value = "create", required = true) String create) throws Exception {
        String cardId = iWxCouponService.create(create);
        return ResponseEntity.ok(cardId);
    }

    /**
     * 创建微信卡券投放二维码
     * @param cardId
     * @param code
     * @return
     * @throws Exception
     */
    @PostMapping("/launch")
    public ResponseEntity launch(@RequestParam(value = "cardId", required = true) String cardId, @RequestParam(value = "code", required = false) String code) throws Exception {
        String qrUrl = iWxCardService.launch(cardId, code);
        return ResponseEntity.ok(qrUrl);
    }

    /**
     * 线下核销卡券Code
     * @param consume
     * @return
     * @throws Exception
     */
    @PostMapping("/consume")
    public ResponseEntity consume(@RequestBody CodeConsume consume) throws Exception {
        CodeConsumeResponse consumeResponse = iWxCardService.consume(consume);
        return ResponseEntity.ok(consumeResponse);
    }


    /**
     * 设置测试白名单
     * @param white
     * @return
     * @throws Exception
     */
    @PostMapping("/white")
    public ResponseEntity white(@RequestBody White white) throws Exception {
        boolean bool = iWxCardService.white(white);
        return ResponseEntity.ok(bool);
    }
}
