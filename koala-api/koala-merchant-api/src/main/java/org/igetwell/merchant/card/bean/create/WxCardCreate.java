package org.igetwell.merchant.card.bean.create;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.merchant.card.bean.WxCard;

@Getter
@Setter
public class WxCardCreate<T extends WxCard> {

    private T card;
}
