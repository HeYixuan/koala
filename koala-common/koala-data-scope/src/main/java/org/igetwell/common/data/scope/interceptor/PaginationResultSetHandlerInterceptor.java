package org.igetwell.common.data.scope.interceptor;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.igetwell.common.uitls.Pagination;
import java.sql.Statement;
import java.util.Properties;

@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class PaginationResultSetHandlerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	Object rows = null;
        try{
            //获取StatementHandler
            ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
            //获取statementHandler包装类
            MetaObject metaStatementHandler = SystemMetaObject.forObject(resultSetHandler);
            RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("rowBounds");
            rows = invocation.proceed();
            if (rowBounds instanceof Pagination) {
                metaStatementHandler.setValue("rowBounds.rows", rows);
            }
            return rows;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
