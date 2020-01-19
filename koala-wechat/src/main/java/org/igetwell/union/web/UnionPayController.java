package org.igetwell.union.web;

import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.union.service.IUnionPayService;
import org.igetwell.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/union/pay")
public class UnionPayController extends BaseController {

    @Autowired
    private Sequence sequence;

    @Autowired
    private IUnionPayService iUnionPayService;

    @GetMapping("/test")
    public ModelAndView test(){
        Map<String, String> params = iUnionPayService.web(sequence.nextNo(), "测试", "测试2", "1");
        //手机网站支付
        ModelAndView modelAndView = new ModelAndView();
        String page = params.get("page") == null ? null : params.get("page");
        modelAndView.addObject("page", page);
        modelAndView.setViewName("/success");
        return modelAndView;
    }

    @GetMapping("/test2")
    public ResponseEntity test2() throws IOException {
        Map<String, String> params = iUnionPayService.scan(sequence.nextNo(), "测试", "测试2", "1");
        //Map<String, String> params = iUnionPayService.sca(sequence.nextNo(), "测试", "测试2", "1");
        String codeUrl = params.get("codeUrl") == null ? null : params.get("codeUrl");
        return ResponseEntity.ok(codeUrl);
    }


    @PostMapping("/payNotify")
    public void payNotify(){
        String text = iUnionPayService.payNotify(request.get());
        render(text);
    }

    @PostMapping("/refund")
    public ResponseEntity refund(String transactionId, String tradeNo, String outTradeNo, String fee){
        iUnionPayService.refund(transactionId, tradeNo, outTradeNo, fee);
        return ResponseEntity.ok();
    }

    @PostMapping("/refundNotify")
    public void refundNotify(){
        String text = iUnionPayService.refundNotify(request.get());
        render(text);
    }

    @PostMapping("/getOrder")
    public ResponseEntity getOrder(String transactionId, String tradeNo) {
        return iUnionPayService.getOrder(transactionId, tradeNo);
    }

    @PostMapping("/closeOrder")
    public ResponseEntity closeOrder(String transactionId, String tradeNo, String fee){
        return iUnionPayService.closeOrder(sequence.nextNo(), transactionId, tradeNo, fee);
    }

}
