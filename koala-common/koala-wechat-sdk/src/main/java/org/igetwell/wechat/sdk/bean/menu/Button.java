package org.igetwell.wechat.sdk.bean.menu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Button {

    private String type; // click|view|scancode_waitmsg|scancode_push|pic_sysphoto|pic_photo_or_album|pic_weixin|location_select|media_id|view_limited|miniprogram
    private String name;
    private String key;
    private String url;
    private String mediaId;
    private List<Button> subButton;
}
