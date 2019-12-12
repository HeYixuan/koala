package org.igetwell.wechat.app.service.impl;

import org.apache.commons.io.FileUtils;
import org.igetwell.common.constans.cache.RedisKey;
import org.igetwell.common.enums.HttpStatus;
import org.igetwell.common.uitls.GsonUtils;
import org.igetwell.common.uitls.HttpClientUtils;
import org.igetwell.common.uitls.RedisUtils;
import org.igetwell.common.uitls.ResponseEntity;
import org.igetwell.wechat.app.service.IWxAppService;
import org.igetwell.wechat.sdk.WxAppAccessToken;
import org.igetwell.wechat.sdk.api.MediaApi;
import org.igetwell.wechat.sdk.api.TokenAPI;
import org.igetwell.wechat.sdk.bean.media.UploadImgResponse;
import org.igetwell.wechat.sdk.bean.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/***
 * 微信公众号获取令牌业务
 */
@Service
public class WxAppService implements IWxAppService {

    private Logger logger = LoggerFactory.getLogger(WxAppService.class);

    @Autowired
    private RedisUtils redisUtils;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    /**
     * 获取微信公众号令牌
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception {
        Token token = redisUtils.get(String.format(RedisKey.WX_APP_ACCESS_TOKEN, appId));
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(token.getAccessToken())) {
            logger.error("[微信公众号]-从缓存中获取微信公众号令牌失败.");
            throw new Exception("[微信开放平台]-从缓存中获取微信公众号令牌失败.");
        }
        return token.getAccessToken();
    }

    /**
     * 是否强制刷新微信公众号令牌
     * @param forceRefresh
     * @throws Exception
     */
    @Override
    public void getAccessToken(boolean forceRefresh) throws Exception {
        if (forceRefresh){
            oauthToken();
            return;
        }
        boolean bool = redisUtils.exist(String.format(RedisKey.WX_APP_ACCESS_TOKEN, appId));
        if (!bool){
            oauthToken();
            return;
        }
    }

    /**
     * 获取微信公众号令牌
     * @throws Exception
     */
    private void oauthToken() throws Exception {
        logger.info("[微信公众号]-获取微信公众号令牌开始.appId={}", appId);
        if (StringUtils.isEmpty(appId.trim()) || StringUtils.isEmpty(secret.trim())) {
            logger.error("[微信公众号]-获取微信公众号令牌失败.未获取到appId.");
            throw new Exception("[微信公众号]-获取微信公众号令牌失败.未获取到appId.");
        }
        Token token = TokenAPI.oauthToken(appId, secret);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(token.getAccessToken())) {
            logger.error("[微信公众号]-获取微信公众号令牌失败. {}", GsonUtils.toJson(token));
            throw new Exception("[微信公众号]-获取微信公众号令牌失败.");
        }
        redisUtils.set(String.format(RedisKey.WX_APP_ACCESS_TOKEN, appId), token, token.getExpiresIn());
        logger.info("[微信公众号]-获取微信公众号令牌结束.");
        return;
    }

    public ResponseEntity uploadLogo(MultipartFile file) throws Exception {
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
            throw new Exception("上传文件服务器异常");
        }
        UploadImgResponse response = MediaApi.logo(getAccessToken(), buffer);
        return ResponseEntity.ok(response);
    }
}
