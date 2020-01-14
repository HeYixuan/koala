package org.igetwell.system.order.service.impl;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.enums.OrderStatus;
import org.igetwell.common.enums.PayChannel;
import org.igetwell.common.sequence.sequence.Sequence;
import org.igetwell.common.uitls.BigDecimalUtils;
import org.igetwell.common.uitls.CharacterUtils;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.paypal.dto.request.PayPalRefundRequest;
import org.igetwell.paypal.feign.PayPalClient;
import org.igetwell.system.order.dto.request.OrderRefundPay;
import org.igetwell.system.order.dto.request.RefundTradeRequest;
import org.igetwell.system.order.dto.request.RefundTransactionRequest;
import org.igetwell.system.order.entity.RefundOrder;
import org.igetwell.system.order.entity.TradeOrder;
import org.igetwell.system.order.mapper.RefundOrderMapper;
import org.igetwell.system.order.protocol.RefundPayProtocol;
import org.igetwell.system.order.dto.request.RefundOrderRequest;
import org.igetwell.system.order.service.IRefundOrderService;
import org.igetwell.system.order.service.ITradeOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RefundOrderService implements IRefundOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private Sequence sequence;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private RefundOrderMapper refundOrderMapper;

    @Autowired
    private ITradeOrderService iTradeOrderService;

    @Autowired
    private PayPalClient payPalClient;



    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public RefundOrder get(Long id) {
        LOGGER.info("[退款订单服务]-根据主键：{} 查询退款订单开始.", id);
        RefundOrder orders = refundOrderMapper.get(id);
        LOGGER.info("[退款订单服务]-根据主键：{} 查询退款订单结束.", id);
        return orders;
    }

    /**
     * 根据微信支付单号和商户订单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @return
     */
    public List<RefundOrder> get(String transactionId, String tradeNo) {
        LOGGER.info("[退款订单服务]-根据微信支付单号:{}, 商户订单号:{} 查询退款订单开始.", transactionId, tradeNo);
        List<RefundOrder> orders = refundOrderMapper.getTrade(transactionId, tradeNo);
        LOGGER.info("[退款订单服务]-根据微信支付单号:{}, 商户订单号:{} 查询退款订单结束.", transactionId, tradeNo);
        return orders;
    }

    /**
     * 根据微信支付单号、商户订单号、商户退款单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param outNo 商户退款单号
     * @return
     */
    public RefundOrder get(String transactionId, String tradeNo, String outNo) {
        LOGGER.info("[退款订单服务]-根据微信支付单号:{}, 商户订单号:{}, 商户退款单号：{} 查询退款订单开始.", transactionId, tradeNo, outNo);
        RefundOrder orders = refundOrderMapper.getOrder(transactionId, tradeNo, outNo);
        LOGGER.info("[退款订单服务]-根据微信支付单号:{}, 商户订单号:{}, 商户退款单号：{} 查询退款订单结束.", transactionId, tradeNo);
        return orders;
    }

    /**
     * 根据微信支付单号、商户订单号、微信退款单号、商户退款单号查询
     * @param transactionId 微信支付单号
     * @param tradeNo 商户订单号
     * @param outRefundNo 微信退款单号
     * @param outNo 商户退款单号
     * @return
     */
    public RefundOrder get(String transactionId, String tradeNo, String outRefundNo, String outNo) {
        LOGGER.info("[退款订单服务]-综合查询退款订单开始.");
        RefundOrder orders = refundOrderMapper.getTransaction(transactionId, tradeNo, outRefundNo, outNo);
        LOGGER.info("[退款订单服务]-综合查询退款订单结束.");
        return orders;
    }

    /**
     * 根据微信支付单号和商户订单号查询
     * @return
     */
    public List<RefundOrder> getOrder(RefundTradeRequest request) {
        LOGGER.info("[退款订单服务]-根据微信支付单号:{} 商户订单号:{} 查询退款订单开始.", GsonUtils.toJson(request));
        if (StringUtils.isEmpty(request) || CharacterUtils.isBlank(request.getTransactionId()) || CharacterUtils.isBlank(request.getTradeNo())) {
            LOGGER.error("[退款订单服务]-根据微信支付单号和商户订单号查询退款订单失败, 请求参数为空.");
            throw new IllegalArgumentException("[退款订单服务]-根据微信支付单号和商户订单号查询退款订单失败, 请求参数为空.");
        }
        List<RefundOrder> orders = refundOrderMapper.getTrade(request.getTransactionId(), request.getTradeNo());
        LOGGER.info("[退款订单服务]-根据微信支付单号:{} 商户订单号:{} 查询退款订单结束.", request.getTransactionId(), request.getTransactionId());
        return orders;
    }

    /**
     * 根据微信支付单号、商户订单号、微信退款单号、商户退款单号查询
     * @return
     */
    public RefundOrder getOrder(RefundTransactionRequest request) {
        LOGGER.info("[退款订单服务]-综合查询退款订单开始.");
        if (StringUtils.isEmpty(request) || CharacterUtils.isBlank(request.getTransactionId()) || CharacterUtils.isBlank(request.getTradeNo()) || CharacterUtils.isBlank(request.getOutRefundNo()) || CharacterUtils.isBlank(request.getOutNo())) {
            LOGGER.error("[退款订单服务]-综合查询退款订单失败, 请求参数为空.");
            throw new IllegalArgumentException("[退款订单服务]-综合查询退款订单失败, 请求参数为空.");
        }
        RefundOrder orders = refundOrderMapper.getTransaction(request.getTransactionId(), request.getTradeNo(), request.getOutRefundNo(), request.getOutNo());
        LOGGER.info("[退款订单服务]-综合查询退款订单结束.");
        return orders;
    }

    public void deleteById(Long id) {
        LOGGER.info("[退款订单服务]-删除退款订单. 退款ID：{}.", id);
        refundOrderMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(RefundOrder refundOrder) {
        LOGGER.info("[退款订单服务]-退款订单入库开始.");
        int i = refundOrderMapper.insert(refundOrder);
        if (i <= 0) {
            LOGGER.error("[退款订单服务]-退款订单入库失败,微信支付单号：{}, 商户订单号：{} 事务执行回滚.", refundOrder.getTransactionId(), refundOrder.getTradeNo());
            String message = String.format("[退款订单服务]-退款订单入库失败，微信支付单号：%s, 商户订单号：%s 事务执行回滚.", refundOrder.getTransactionId(), refundOrder.getTradeNo());
            throw new RuntimeException(message);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(RefundOrder refundOrder) {
        LOGGER.info("[退款订单服务]-修改退款订单开始.", refundOrder.getId());
        int i = refundOrderMapper.update(refundOrder);
        if (i <= 0) {
            LOGGER.info("[退款订单服务]-修改订单{}数据失败，事务执行回滚.", refundOrder.getId());
            String message = String.format("[退款订单服务]-修改订单%s数据失败，事务执行回滚.", refundOrder.getId());
            throw new RuntimeException(message);
        }
        LOGGER.info("[退款订单服务]-修改退款订单结束.");
    }

    /**
     * 保存退款记录
     * @param request
     */
    public void insert(RefundOrderRequest request) {
        RefundOrder order = new RefundOrder();
        order.setId(sequence.nextValue());
        order.setOutNo(sequence.nextNo());
        order.setMchId(request.getMchId());
        order.setMchNo(request.getMchNo());
        order.setMemberId(request.getMemberId());
        order.setMemberNo(request.getMemberNo());
        order.setTransactionId(request.getTransactionId());
        order.setTradeNo(request.getTradeNo());
        order.setTotalFee(request.getTotalFee());
        order.setRefundFee(request.getRefundFee());
        this.insert(order);
    }

    /**
     * 用户发起退款
     * @param request
     */
    public ResponseEntity refundOrder(RefundOrderRequest request) {
        LOGGER.info("[退款订单服务]-用户发起退款订单请求开始.", GsonUtils.toJson(request));
        if (!checkParam(request)){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "参数错误!");
        }
        String transactionId = request.getTransactionId();
        String tradeNo = request.getTradeNo();
        BigDecimal totalFee = request.getTotalFee();
        BigDecimal refundFee = request.getRefundFee();
        if (BigDecimalUtils.lessOrEquals(totalFee, BigDecimal.ZERO) || BigDecimalUtils.lessOrEquals(refundFee, BigDecimal.ZERO)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "订单金额或退款金额必须大于0");
        }
        //如果实际退款金额大于订单总金额,退款失败
        if (BigDecimalUtils.lessThan(totalFee, refundFee)) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "退款金额不能大于订单金额.");
        }
        this.insert(request);
        LOGGER.info("[退款订单服务]-用户发起退款订单请求结束.");
        return refundOrderQueue(transactionId, tradeNo);
    }

    /**
     * 退款订单入队
     * @param transactionId
     * @return
     */
    public ResponseEntity refundOrderQueue(String transactionId, String tradeNo){
        RefundPayProtocol protocol = new RefundPayProtocol(transactionId, tradeNo);
        LOGGER.info("[退款订单服务]-异步投递退款订单消息开始入队.protocol={}.", GsonUtils.toJson(protocol));
        try {
            //异步投递消息
            rocketMQTemplate.getProducer().setProducerGroup("refund-pay-order");
            rocketMQTemplate.asyncSend("refund-pay-order:refund-pay-order", MessageBuilder.withPayload(protocol).build(), new SendCallback() {
                @Override
                public void onSuccess(SendResult var) {
                    refundOrderMapper.updateRefunding(transactionId, tradeNo, 1); //退款状态：退款中
                    LOGGER.info("[退款订单服务]-退款订单异步消息投递成功,投递结果：{}.", var);
                }

                @Override
                public void onException(Throwable var) {
                    LOGGER.error("[退款订单服务]-退款订单异步消息投递失败,异常信息：{}.", var);
                }

            });
            return ResponseEntity.ok();
        } catch (Exception e) {
            int retryTimes = rocketMQTemplate.getProducer().getRetryTimesWhenSendAsyncFailed();
            LOGGER.error("[退款订单服务]-微信支付单号：{} 商户订单号：{} 异步投递退款订单消息异常.", transactionId, tradeNo, retryTimes, e);
        }
        return ResponseEntity.error();
    }

    /**
     * 前置参数校验
     * @param request
     * @return
     */
    private boolean checkParam(RefundOrderRequest request) {
        if (StringUtils.isEmpty(request)) {
            return false;
        }
        Long mchId = request.getMchId();
        String mchNo = request.getMchNo();
        Long memberId = request.getMemberId();
        String memberNo = request.getMemberNo();
        String transactionId = request.getTransactionId();
        String tradeNo = request.getTradeNo();
        BigDecimal totalFee = request.getTotalFee();
        BigDecimal refundFee = request.getRefundFee();

        if (StringUtils.isEmpty(mchId) || StringUtils.isEmpty(memberId) || CharacterUtils.isBlank(mchNo) || CharacterUtils.isBlank(memberNo)) {
            LOGGER.info("[退款订单服务]-退款必要参数不可为空.");
            return false;
        }
        if (StringUtils.isEmpty(totalFee) || StringUtils.isEmpty(refundFee) || CharacterUtils.isBlank(transactionId) || CharacterUtils.isBlank(tradeNo)) {
            LOGGER.info("[退款订单服务]-退款必要参数不可为空.");
            return false;
        }
        return true;
    }
    /**
     * 用户发起退款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity refund(OrderRefundPay refundPay) {
        LOGGER.info("[退款订单服务]-创建退款订单请求开始.请求入参={}.", GsonUtils.toJson(refundPay));
        String tradeNo = refundPay.getTradeNo();
        BigDecimal refundFee = refundPay.getRefundFee();
        if (StringUtils.isEmpty(refundPay)) {
            throw new IllegalArgumentException("退款请求参数不可为空.");
        }
        if (CharacterUtils.isBlank(tradeNo)) {
            throw new IllegalArgumentException("商户交易单号不可为空.");
        }
        if (StringUtils.isEmpty(refundFee) || BigDecimalUtils.lessOrEquals(refundFee, BigDecimal.ZERO)) {
            throw new IllegalArgumentException("退款金额必须大于0.");
        }
        TradeOrder orders = iTradeOrderService.getOrder(tradeNo);
        if (StringUtils.isEmpty(orders) || orders.getStatus() != OrderStatus.PAID.getValue()){
            throw new IllegalArgumentException("此商户交易单号未查询到已支付订单.");
        }
        String transactionId = orders.getTransactionId();
        if (CharacterUtils.isBlank(transactionId)) {
            throw new IllegalArgumentException("此商户交易单号未查询已支付交易流水.");
        }
        BigDecimal totalFee = orders.getFee();
        if (BigDecimalUtils.lessOrEquals(totalFee, BigDecimal.ZERO)) {
            throw new IllegalArgumentException("此商户交易单总金额必须大于0.");
        }
        if (BigDecimalUtils.lessThan(totalFee, refundFee)) {
            throw new IllegalArgumentException("退款金额不能大于订单总额.");
        }
        RefundOrder order = new RefundOrder(sequence.nextValue(), sequence.nextNo(), tradeNo, transactionId, totalFee, refundFee,
                orders.getMchId(), orders.getMchNo(), 20001L, "M:00001", OrderStatus.CREATE.getValue());
        this.insert(order);
        PayPalRefundRequest refundRequest = null;
        if (PayChannel.WECHAT.name().equalsIgnoreCase(orders.getChannelName())) {
            //微信退款
            refundRequest = new PayPalRefundRequest(PayChannel.WECHAT, order.getOutNo(), tradeNo, transactionId, totalFee, refundFee);
        } else if (PayChannel.ALIPAY.name().equalsIgnoreCase(orders.getChannelName())) {
            //支付宝退款
            refundRequest = new PayPalRefundRequest(PayChannel.ALIPAY, order.getOutNo(), tradeNo, transactionId, totalFee, refundFee);
        } else if (PayChannel.UNIONPAY.name().equalsIgnoreCase(orders.getChannelName())){
            ResponseEntity.ok("其他支付平台退款请联系客服.");
        }
        ResponseEntity responseEntity =  payPalClient.refund(refundRequest);
        if (StringUtils.isEmpty(responseEntity) || responseEntity.getStatus() != HttpStatus.OK.value()) {
            throw new RuntimeException("生成退款订单失败.");
        }
        return responseEntity;
    }
}
