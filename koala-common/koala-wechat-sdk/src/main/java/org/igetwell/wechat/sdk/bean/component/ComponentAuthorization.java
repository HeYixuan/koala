package org.igetwell.wechat.sdk.bean.component;

import java.io.Serializable;
import java.util.List;

public class ComponentAuthorization implements Serializable {

    private String authorizerAppid;
    private String authorizerAccessToken;
    private Long expiresIn;
    private String authorizerRefreshToken;
    private List<FuncInfo> funcInfo;

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getAuthorizerRefreshToken() {
        return authorizerRefreshToken;
    }

    public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
        this.authorizerRefreshToken = authorizerRefreshToken;
    }

    public List<FuncInfo> getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(List<FuncInfo> funcInfo) {
        this.funcInfo = funcInfo;
    }
}
