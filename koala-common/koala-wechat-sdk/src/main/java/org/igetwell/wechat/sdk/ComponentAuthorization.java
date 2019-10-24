package org.igetwell.wechat.sdk;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComponentAuthorization {

    private String authorizerAppid;
    private String authorizerAccessToken;
    private long expiresIn;
    private String authorizerRefreshToken;
    private List<FuncInfo> funcInfo;
}
