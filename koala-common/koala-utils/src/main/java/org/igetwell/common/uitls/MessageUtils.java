package org.igetwell.common.uitls;

import com.thoughtworks.xstream.XStream;
import org.igetwell.common.event.FollowEventMessage;

import java.util.Map;

public class MessageUtils {
    public static final String MESSAGE_TEXT = "text";
    public static final String MESSAGE_IMAGE = "image";
    public static final String MESSAGE_VOICE = "voice";
    public static final String MESSAGE_VIDEO = "video";
    public static final String MESSAGE_LINK = "link";
    public static final String MESSAGE_LOCATION = "location";
    public static final String MESSAGE_EVENT = "event";

    public static final String EVENT_SUB = "subscribe";
    public static final String EVENT_UNSUB = "unsubscribe";
    public static final String EVENT_CLICK = "CLICK";
    public static final String EVENT_VIEW = "VIEW";


    public static String menuText(){
        StringBuffer sb = new StringBuffer();
        sb.append("      你关注，");
        sb.append("     或者不关注，");
        sb.append("      【你我杂志刊】都在这里!");
        sb.append("     不离，");
        sb.append("      不弃！");
        sb.append("该公众号已实现以下功能：");
        sb.append("回复“天气”、“翻译” 将有该功能的介绍与使用，");
        sb.append("如您在使用该公众有任何宝贵意见，欢迎反馈！");
        sb.append("反馈邮箱：15517551511@126.com");
        return sb.toString();
    }

    public static String initText(String toUserName, String fromUserName, String content){
        FollowEventMessage eventMessage = new FollowEventMessage();
        eventMessage.setFromUserName(toUserName);
        eventMessage.setToUserName(fromUserName);
        eventMessage.setMsgType(MESSAGE_TEXT);
        eventMessage.setCreateTime(System.currentTimeMillis());
        eventMessage.setContent(content);
        return object2Xml(eventMessage);
    }

    public static String callbackMessage(Map<String, String> resultMap){
        String fromUserName = resultMap.get("FromUserName");
        String toUserName = resultMap.get("ToUserName");
        String msgType = resultMap.get("MsgType");
        String message = null;
        //判断请求是否事件类型 event
        if(msgType.equals(MESSAGE_EVENT)){
            String eventType = resultMap.get("Event");
            //若是关注事件subscribe
            if(eventType.equals(EVENT_SUB)){
                message = initText(toUserName, fromUserName, menuText());
            }
        }
        if (msgType.equals(MESSAGE_TEXT)){
            message = initText(toUserName, fromUserName, menuText());
        }
        return message;
    }

    public static String object2Xml(FollowEventMessage eventMessage){
        XStream xstream = new XStream();
        xstream.alias("xml", eventMessage.getClass());
        return xstream.toXML(eventMessage);
    }
}
