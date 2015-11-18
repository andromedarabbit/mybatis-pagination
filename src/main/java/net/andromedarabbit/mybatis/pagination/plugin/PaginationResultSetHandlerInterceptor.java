package net.andromedarabbit.mybatis.pagination.plugin;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.RowBounds;

import java.sql.Statement;
import java.util.Properties;

@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class PaginationResultSetHandlerInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept((ResultSetHandler) invocation.getTarget());
        return invocation.proceed();
    }

    private void processIntercept(ResultSetHandler resultSet) throws IllegalAccessException {

        final RowBounds rowBounds = (RowBounds) FieldUtils.readField(resultSet, "rowBounds", true);
        if (rowBounds == null) {
            return;
        }

        if (rowBounds.getOffset() != RowBounds.NO_ROW_OFFSET) {
            RowBounds newRowBounds = new RowBounds(RowBounds.NO_ROW_OFFSET, rowBounds.getLimit());
            FieldUtils.writeField(resultSet, "rowBounds", newRowBounds, true);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // To change body of implemented methods use File | Settings | File Templates.
    }
}
