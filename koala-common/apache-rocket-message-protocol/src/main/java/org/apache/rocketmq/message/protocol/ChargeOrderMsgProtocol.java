package org.apache.rocketmq.message.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.message.constant.MessageProtocolConst;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * 订单结果通知协议
 */
@Getter
@Setter
public class ChargeOrderMsgProtocol extends BaseMsg implements Serializable {

    private static final long serialVersionUID = 73717163386598209L;

    /**订单号*/
    private String orderNo;
    /**用户下单手机号*/
    private String mobile;
    /**商品id*/
    private String prodId;
    /**用户交易金额*/
    private String chargeMoney;

    private Map<String, String> header;
    private Map<String, String> body;

    @Override
    public String encode() {
        // 组装消息协议头
        ImmutableMap.Builder headerBuilder = new ImmutableMap.Builder<String, String>()
                .put("version", this.getVersion())
                .put("topicName", MessageProtocolConst.SECKILL_CHARGE_ORDER_TOPIC.getTopic());
        header = headerBuilder.build();

        body = new ImmutableMap.Builder<String, String>()
                .put("orderNo", this.getOrderNo())
                .put("mobile", this.getMobile())
                .put("prodId", this.getProdId())
                .put("chargeMoney", this.getChargeMoney())
                .build();

        ImmutableMap<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("header", header)
                .put("body", body)
                .build();

        // 返回序列化消息Json串
        String ret_string = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ret_string = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("ChargeOrderMsgProtocol消息序列化json异常", e);
        }
        return ret_string;
    }

    @Override
    public void decode(String msg) {
        Preconditions.checkNotNull(msg);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(msg);
            // header
            this.setVersion(root.get("header").get("version").asText());
            this.setTopicName(root.get("header").get("topicName").asText());
            // body
            this.setOrderNo(root.get("body").get("orderNo").asText());
            this.setMobile(root.get("body").get("mobile").asText());
            this.setChargeMoney(root.get("body").get("chargeMoney").asText());
            this.setProdId(root.get("body").get("prodId").asText());
        } catch (IOException e) {
            throw new RuntimeException("ChargeOrderMsgProtocol消息反序列化异常", e);
        }
    }

}
