package org.igetwell.wechat.sdk.bean.component;

import java.io.Serializable;

public class Authorization implements Serializable {

    ComponentAuthorization authorizationInfo;

    public ComponentAuthorization getAuthorizationInfo() {
        return authorizationInfo;
    }

    public void setAuthorizationInfo(ComponentAuthorization authorizationInfo) {
        this.authorizationInfo = authorizationInfo;
    }
}
