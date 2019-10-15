package org.igetwell.merchant.card.create;

import lombok.Getter;
import lombok.Setter;
import org.igetwell.merchant.card.WxCard;

@Getter
@Setter
public class WxCardCreate <T extends WxCard> {

    private T card;
}
