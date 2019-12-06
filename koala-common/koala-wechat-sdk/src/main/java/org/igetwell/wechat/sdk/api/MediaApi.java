package org.igetwell.wechat.sdk.api;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.igetwell.common.uitls.HttpClients;
import org.igetwell.wechat.sdk.bean.media.Media;
import org.igetwell.wechat.sdk.bean.media.MediaType;
import org.igetwell.wechat.sdk.bean.media.UploadImgResponse;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

@Slf4j
public class MediaApi extends API {

    /**
     * 新增临时素材
     * 媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param mediaType mediaType
     * @param media  	多媒体文件有格式和大小限制，如下：
    图片（image）: 2M，支持bmp/png/jpeg/jpg/gif格式
    语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
    视频（video）：10MB，支持MP4格式
    缩略图（thumb）：64KB，支持JPG格式
     * @return Media
     */
    public static Media mediaUpload(String accessToken, MediaType mediaType, File media){
        HttpPost httpPost = new HttpPost(BASE_URI+"/cgi-bin/media/upload");
        FileBody bin = new FileBody(media);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("media", bin)
                .addTextBody(ACCESS_TOKEN, API.accessToken(accessToken))
                .addTextBody("type", mediaType.uploadType())
                .build();
        httpPost.setEntity(reqEntity);
        return HttpClients.execute(httpPost,Media.class);
    }

    /**
     * 新增临时素材
     * 媒体文件在后台保存时间为3天，即3天后media_id失效。
     * @param accessToken accessToken
     * @param mediaType mediaType
     * @param inputStream  	多媒体文件有格式和大小限制，如下：
     *                      图片（image）: 2M，支持bmp/png/jpeg/jpg/gif格式
     *                      语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     *                      视频（video）：10MB，支持MP4格式
     *                      缩略图（thumb）：64KB，支持JPG格式
     * @return Media
     */
    public static Media mediaUpload(String accessToken,MediaType mediaType, InputStream inputStream){
        HttpPost httpPost = new HttpPost(BASE_URI+"/cgi-bin/media/upload");
        byte[] data = null;
        try {
            data = StreamUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            log.error("", e);
        }
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addBinaryBody("media",data, ContentType.DEFAULT_BINARY,"temp."+mediaType.fileSuffix())
                .addTextBody(ACCESS_TOKEN, API.accessToken(accessToken))
                .addTextBody("type",mediaType.uploadType())
                .build();
        httpPost.setEntity(reqEntity);
        return HttpClients.execute(httpPost,Media.class);
    }



    /**
     * 上传图文消息内的图片获取URL
     * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param accessToken accessToken
     * @param media media
     * @return UploadimgResult
     */
    public static UploadImgResponse logo(String accessToken, File media){
        HttpPost httpPost = new HttpPost(BASE_URI+"/cgi-bin/media/uploadimg");
        FileBody bin = new FileBody(media);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("buffer", bin)
                .addTextBody(ACCESS_TOKEN, API.accessToken(accessToken))
                .build();
        httpPost.setEntity(reqEntity);
        return HttpClients.execute(httpPost, UploadImgResponse.class);
    }


    /**
     * 上传图文消息内的图片获取URL
     * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param accessToken accessTokenaccessToken
     * @param media media
     * @return UploadimgResult
     */
    public static UploadImgResponse mediaUploadImg(String accessToken, File media){
        HttpPost httpPost = new HttpPost(BASE_URI+"/cgi-bin/media/uploadimg");
        FileBody bin = new FileBody(media);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("media", bin)
                .addTextBody(ACCESS_TOKEN, API.accessToken(accessToken))
                .build();
        httpPost.setEntity(reqEntity);
        return HttpClients.execute(httpPost, UploadImgResponse.class);
    }

    /**
     * 上传图文消息内的图片获取URL
     * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param accessToken accessToken
     * @param inputStream inputStream
     * @return UploadimgResult
     */
    public static UploadImgResponse mediaUploadImg(String accessToken,InputStream inputStream){
        HttpPost httpPost = new HttpPost(BASE_URI+"/cgi-bin/media/uploadimg");
        //InputStreamBody inputStreamBody =  new InputStreamBody(inputStream, ContentType.DEFAULT_BINARY, UUID.randomUUID().toString()+".jpg");
        byte[] data = null;
        try {
            data = StreamUtils.copyToByteArray(inputStream);
        } catch (IOException e) {
            log.error("", e);
        }
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addBinaryBody("media",data,ContentType.DEFAULT_BINARY,"temp.jpg")
                .addTextBody(ACCESS_TOKEN, API.accessToken(accessToken))
                .build();
        httpPost.setEntity(reqEntity);
        return HttpClients.execute(httpPost,UploadImgResponse.class);
    }


    /**
     * 上传图文消息内的图片获取URL
     * 请注意，本接口所上传的图片不占用公众号的素材库中图片数量的5000个的限制。图片仅支持jpg/png格式，大小必须在1MB以下。
     * @param accessToken accessToken
     * @param uri uri
     * @return UploadimgResult
     */
    public static UploadImgResponse mediaUploadImg(String accessToken, URI uri){
        HttpPost httpPost = new HttpPost(BASE_URI+"/cgi-bin/media/uploadimg");
        CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
        try {
            HttpEntity entity = httpClient.execute(RequestBuilder.get().setUri(uri).build()).getEntity();
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("media", EntityUtils.toByteArray(entity), ContentType.get(entity), UUID.randomUUID().toString()+".jpg")
                    .addTextBody(ACCESS_TOKEN, API.accessToken(accessToken))
                    .build();
            httpPost.setEntity(reqEntity);
            return HttpClients.execute(httpPost, UploadImgResponse.class);
        } catch (Exception e) {
            log.error("", e);
        } finally{
            try {
                httpClient.close();
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return null;
    }

}
