package org.igetwell.wechat.sdk.response;

public class CardResponseEntity extends BaseResponseEntity {

    private String cardId;

    /**
     * 卡券ID
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 卡券ID
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
