package org.igetwell.wechat.sdk.bean.card.create;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.wechat.sdk.bean.card.AbstractCard;

@Getter
@Setter
public class WxCardCreate<T extends AbstractCard> {

    private T card;
}
