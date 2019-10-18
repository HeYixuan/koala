package org.igetwell.wechat.sdk.bean;

public class Message {

    public Message() {
        super();
    }

    protected Message(String touser,String msgtype) {
        super();
        this.touser = touser;
        this.msgtype = msgtype;
    }


    private String touser;

    private String msgtype;
}
