package org.igetwell.merchant.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 卡券有效期
 */
@Getter
@Setter
public class WxCardDate {

    /**
     * 使用时间的类型，旧文档采用的1和2依然生效。<br>
     * DATE_TYPE_FIX_TIME_RANGE： 表示固定日期区间； <br>
     * DATE_TYPE_FIX_TERM：表示固定时长<br>
     * 添加必填
     */
    private String type;

    /**
     * type为DATE_TYPE_FIX_TIME_RANGE时专用，表示起用时间。<br>
     * 从1970年1月1日00:00:00至起用时间的秒数，<br>
     * 最终需转换为字符串形态传入。（东八区时间，单位为秒） <br>
     * 添加必填
     */
    private Integer beginTimestamp;

    /**
     * 表示结束时间，建议设置为截止日期的23:59:59过期。（东八区时间，单位为秒）<br>
     * 可用于DATE_TYPE_FIX_TERM时间类型，表示卡券统一过期时间，建议设置为截止日期的23:59:59过期。（东八区时间，单位为秒），<br>
     * 设置了fixed_term卡券，当时间达到end_timestamp时卡券统一过期<br>
     * 添加必填
     */
    private Integer endTimestamp;

    /**
     * type为DATE_TYPE_FIX_TERM时专用，表示自领取后多少天内有效，不支持填写0。 <br>
     * 添加必填，不支持修改
     */
    private Integer fixedTerm;

    /**
     * type为DATE_TYPE_FIX_TERM时专用，表示自领取后多少天开始生效，领取后当天生效填写0。（单位为天）<br>
     * 添加必填，不支持修改
     */
    private Integer fixedBeginTerm;
}
