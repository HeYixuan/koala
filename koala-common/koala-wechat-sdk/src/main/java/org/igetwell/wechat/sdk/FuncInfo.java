package org.igetwell.wechat.sdk;

import java.io.Serializable;

public class FuncInfo implements Serializable {

    private FuncCategory funcscopeCategory;

    public FuncCategory getFuncscopeCategory() {
        return funcscopeCategory;
    }

    public void setFuncscopeCategory(FuncCategory funcscopeCategory) {
        this.funcscopeCategory = funcscopeCategory;
    }
}
