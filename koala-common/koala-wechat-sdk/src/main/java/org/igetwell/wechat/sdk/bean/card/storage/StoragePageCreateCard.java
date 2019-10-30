package org.igetwell.wechat.sdk.bean.card.storage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoragePageCreateCard {

    /**
     * 所要在页面投放的card_id<br>
     * 必填：是
     */
    private String cardId;

    /**
     * 缩略图url<br>
     * 必填：是
     */
    private String thumbUrl;

}
