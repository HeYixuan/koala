package org.igetwell.common.data.tenant;

public interface TenantHandler {

    Long getTenantId();

    String getTenantIdColumn();

    boolean doTableFilter(String tableName);
}
