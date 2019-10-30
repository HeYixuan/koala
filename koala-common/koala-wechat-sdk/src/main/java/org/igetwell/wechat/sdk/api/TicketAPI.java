package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.wechat.sdk.ticket.JsTicket;
/**
 * JSAPI ticket
 */
public class TicketAPI extends API {

    /**
     * 获取 jsapi_ticket
     * @param accessToken access_token
     * @return ticket
     */
    public static JsTicket getTicket(String accessToken){
        return getTicket(accessToken,"jsapi");
    }

    /**
     * 获取 ticket
     * @param accessToken access_token
     * @param type jsapi or wx_card
     * @return ticket
     */
    public static JsTicket getTicket(String accessToken, String type){
        HttpUriRequest httpUriRequest = RequestBuilder.get()
                .setUri(BASE_URI + "/cgi-bin/ticket/getticket")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .addParameter("type", type)
                .build();
        return HttpClients.execute(httpUriRequest, JsTicket.class);
    }
}
