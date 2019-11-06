package org.igetwell.wechat.sdk;

import java.io.Serializable;

public class AuthorizationInfo implements Serializable {

    ComponentAuthorization authorizationInfo;

    public ComponentAuthorization getAuthorizationInfo() {
        return authorizationInfo;
    }

    public void setAuthorizationInfo(ComponentAuthorization authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
    }
}
