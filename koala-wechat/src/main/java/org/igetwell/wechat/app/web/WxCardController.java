package org.igetwell.wechat.app.web;


import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.app.service.IWxCardService;
import org.igetwell.wechat.sdk.bean.card.MemberCard;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsume;
import org.igetwell.wechat.sdk.bean.card.code.consume.CodeConsumeResponse;
import org.igetwell.wechat.sdk.bean.card.create.WxCardCreate;
import org.igetwell.wechat.sdk.bean.card.white.White;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wx/card")
public class WxCardController {

    @Autowired
    private IWxCardService iWxCardService;

    /**
     * 创建微信卡券
     * @param create
     * @return
     * @throws Exception
     */
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody WxCardCreate<MemberCard> create) throws Exception {
        String cardId = iWxCardService.createCard(create);
        return ResponseEntity.ok(cardId);
    }

    @PostMapping("/createCard")
    public ResponseEntity createCard(String create) throws Exception {
        String cardId = iWxCardService.createCard(create);
        return ResponseEntity.ok(cardId);
    }

    @PostMapping("/createGroup")
    public ResponseEntity createGroup(String group) throws Exception {
        String cardId = iWxCardService.createGroup(group);
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
