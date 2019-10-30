package org.igetwell.wechat.sdk.api;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.wechat.sdk.bean.menu.Menu;
import org.igetwell.wechat.sdk.response.BaseResponse;

import java.nio.charset.Charset;

/**
 * 菜单API
 */
public class MenuAPI extends API{

    /**
     * 创建菜单
     * @param accessToken accessToken
     * @param postJson 菜单json 数据 例如{\"button\":[{\"type\":\"click\",\"name\":\"今日歌曲\",\"key\":\"V1001_TODAY_MUSIC\"},{\"type\":\"click\",\"name\":\"歌手简介\",\"key\":\"V1001_TODAY_SINGER\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"},{\"type\":\"click\",\"name\":\"赞一下我们\",\"key\":\"V1001_GOOD\"}]}]}
     * @return BaseResponse
     */
    public static BaseResponse createMenu(String accessToken,String postJson){
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(APPLICATION_JSON)
                .setUri(BASE_URI+"/cgi-bin/menu/create")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .setEntity(new StringEntity(postJson, Charset.forName("utf-8")))
                .build();
        return HttpClients.execute(httpUriRequest, BaseResponse.class);
    }

    /**
     * 创建菜单
     * @param accessToken accessToken
     * @param menu menu
     * @return BaseResponse
     */
    public static BaseResponse createMenu(String accessToken, Menu menu){
        return createMenu(accessToken, GsonUtils.toJson(menu));
    }

    /**
     * 获取菜单
     * @param accessToken access_token
     * @return Menu
     */
    public static Menu getMenu(String accessToken){
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setUri(BASE_URI+"/cgi-bin/menu/get")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .build();
        return HttpClients.execute(httpUriRequest,Menu.class);
    }

    /**
     * 删除菜单
     * @param accessToken accessToken
     * @return BaseResponse
     */
    public static BaseResponse delete(String accessToken){
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setUri(BASE_URI+"/cgi-bin/menu/delete")
                .addParameter(ACCESS_TOKEN, API.accessToken(accessToken))
                .build();
        return HttpClients.execute(httpUriRequest,BaseResponse.class);
    }

}
