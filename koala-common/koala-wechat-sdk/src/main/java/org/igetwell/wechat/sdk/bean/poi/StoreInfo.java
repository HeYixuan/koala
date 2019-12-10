package org.igetwell.wechat.sdk.bean.poi;

import lombok.Getter;
import lombok.Setter;

/**
 * 门店信息－提交对象
 */
@Getter
@Setter
public class StoreInfo {

    /**
     * 门店ID，修改服务信息是需要指定
     */
    private String poiId;
    /**
     * 商户自己的id，用于后续审核通过收到poi_id 的通知时，做对应关系。请商户自己保证唯一识别性<br>
     * 必填：否
     */
    private String sid;
    /**
     * 门店名称（仅为商户名，如：国美、麦当劳，不应包含地区、地址、分店名等信息，错误示例：北京国美）<br>
     * 必填：是
     */
    private String businessName;
    /**
     * 分店名称（不应包含地区信息，不应与门店名有重复，错误示例：北京王府井店）<br>
     * 必填：是
     */
    private String branchName;
    /**
     * 门店所在的省份（直辖市填城市名,如：北京市）<br>
     * 必填：是
     */
    private String province;
    /**
     * 门店所在的城市<br>
     * 必填：是
     */
    private String city;
    /**
     * 门店所在地区<br>
     * 必填：是
     */
    private String district;
    /**
     * 门店所在的详细街道地址（不要填写省市信息）<br>
     * 必填：是
     */
    private String address;
    /**
     * 门店的电话（纯数字，区号、分机号均由“-”隔开）<br>
     * 必填：是
     */
    private String telephone;
    /**
     * 门店的类型（不同级分类用“,”隔开，如：美食，川菜，火锅。详细分类参见附件：微信门店类目表）<br>
     * 必填：是
     */
    private String[] categories;
    /**
     * 坐标类型，1 为火星坐标（目前只能选1）<br>
     * 必填：是
     */
    private Integer offsetType;
    /**
     * 门店所在地理位置的经度<br>
     * 必填：是
     */
    private Double longitude;
    /**
     * 门店所在地理位置的纬度（经纬度均为火星坐标，最好选用腾讯地图标记的坐标）<br>
     * 必填：是
     */
    private Double latitude;
    /**
     * 图片列表，url
     * 形式，可以有多张图片，尺寸为640*340px。必须为上一接口生成的url。图片内容不允许与门店不相关，不允许为二维码、员工合照（
     * 或模特肖像）、营业执照、无门店正门的街景、地图截图、公交地铁站牌、菜单截图等<br>
     * 必填：否
     */
    private StorePhoto[] photoList;
    /**
     * 推荐品，餐厅可为推荐菜；酒店为推荐套房；景点为推荐游玩景点等，针对自己行业的推荐内容<br>
     * 必填：否
     */
    private String recommend;
    /**
     * 特色服务，如免费wifi，免费停车，送货上门等商户能提供的特色功能或服务<br>
     * 必填：否
     */
    private String special;
    /**
     * 商户简介，主要介绍商户信息等<br>
     * 必填：否
     */
    private String introduction;
    /**
     * 营业时间，24 小时制表示，用“-”连接，如 8:00-20:00<br>
     * 必填：否
     */
    private String openTime;
    /**
     * 人均价格，大于0 的整数<br>
     * 必填：否
     */
    private Float avgPrice;
}
