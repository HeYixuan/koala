package org.igetwell.wechat.sdk.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.sdk.WxAppAccessToken;
import org.igetwell.wechat.sdk.api.MediaApi;
import org.igetwell.wechat.sdk.bean.media.UploadImgResponse;
import org.igetwell.wechat.sdk.service.IWxAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class WxAppService implements IWxAppService {

    String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void getAccessToken(String appId, String secret) {
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(secret)) {
            return;
        }
        WxAppAccessToken accessToken = redisUtils.get("WX_APP_ACCESS_TOKEN");
        if (accessToken == null) {
            String response = HttpClientUtils.getInstance().sendHttpsGet(String.format(ACCESS_TOKEN_URL, appId, secret));
            accessToken = GsonUtils.fromJson(response, WxAppAccessToken.class);
            if (accessToken != null) {
                redisUtils.set("WX_APP_ACCESS_TOKEN", accessToken, accessToken.getExpiresIn());
            }
        }
    }

    public ResponseEntity uploadLogo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "请上传LOGO");
        }
        String fileName = file.getOriginalFilename();
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!"jpg".equals(fileExt.toLowerCase())) {
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "请上传jpg格式的LOGO图");
        }

        boolean bool = org.igetwell.common.uitls.FileUtils.checkFileSize(file.getSize(), 1,"M");
        if (!bool){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "请上传大小限制1MB的LOGO，推荐像素为300*300");
        }

        File buffer = null;
        try {
            buffer = new File(file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WxAppAccessToken accessToken = redisUtils.get("WX_APP_ACCESS_TOKEN");
        if (accessToken == null){
            return ResponseEntity.error(HttpStatus.BAD_REQUEST, "未获取到token");
        }
        UploadImgResponse response = MediaApi.logo(accessToken.getAccessToken(), buffer);
        return ResponseEntity.ok(response);
    }
}
