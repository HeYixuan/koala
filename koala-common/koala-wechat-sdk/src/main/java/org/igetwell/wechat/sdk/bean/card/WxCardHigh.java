package org.igetwell.wechat.sdk.bean.card;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 微信卡券高级信息
 */
@Getter
@Setter
public class WxCardHigh {

    /**
     * 使用门槛（条件）字段，<br>
     * 若不填写使用条件则在券面拼写：无最低消费限制，全场通用，不限品类；<br>
     * 并在使用说明显示：可与其他优惠共享
     */
    private WxCardHighUseCondition useCondition;

    /**
     * 封面摘要
     */
    @SerializedName("abstract")
    private WxCardCover cover;

    /**
     * 图文列表，显示在详情内页 ，优惠券券开发者须至少传入 一组图文列表
     */
    private List<WxCardHighTextImage> textImageList;

    /**
     * 商家服务类型：
     * BIZ_SERVICE_DELIVER 外卖服务；
     * BIZ_SERVICE_FREE_PARK 停车位；
     * BIZ_SERVICE_WITH_PET 可带宠物；
     * BIZ_SERVICE_FREE_WIFI 免费wifi，
     * 可多选
     */
    private List<String> businessService;

    /**
     * 使用时段限制
     */
    private List<WxCardHightTimeLimit> timeLimit;
}
