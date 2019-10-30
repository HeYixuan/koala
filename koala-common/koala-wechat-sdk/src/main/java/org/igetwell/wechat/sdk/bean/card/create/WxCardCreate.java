package org.igetwell.wechat.sdk.bean.card.create;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.bean.card.WxCard;

@Getter
@Setter
public class WxCardCreate<T extends WxCard> {

    private T card;
}
