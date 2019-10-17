package org.igetwell.wechat.sdk.card.bean.create;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.card.bean.WxCard;

@Getter
@Setter
public class WxCardCreate<T extends WxCard> {

    private T card;
}
