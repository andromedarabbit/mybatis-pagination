package net.andromedarabbit.mybatis.pagination.plugin;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * References:
 * https://github.com/sogyf/mybatis-pagination/tree/master/src/main/java/org/mybatis/pagination
 * https://github.com/sogyf/plum/tree/master/src/main/java/plum/mybatis
 * https://github.com/ptzhuf/commons/tree/master/mybatis-spring-paging/mybatis-spring-paging/src/main/java/com/cyou/fz/common/mybatis
 * http://stamen.iteye.com/blog/1901576
 * http://www.javawebdevelop.com/1108146/
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
                }
        ),
        @Signature(
                type = Executor.class,
                method = "query",
                args = {
                        MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class
                }
        ),
})
public class PaginationInterceptor implements Interceptor {
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";

    /**
     * mapped statement parameter index.
     */
    private static final int MAPPED_STATEMENT_INDEX = 0;
    /**
     * parameter index.
     */
    private static final int PARAMETER_INDEX = 1;
    /**
     * parameter index.
     */
    private static final int ROWBOUNDS_INDEX = 2;


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        processIntercept(invocation.getArgs());
        return invocation.proceed();
    }

    private void processIntercept(final Object[] queryArgs) throws IllegalAccessException {

        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];

        // Clean up
        BoundSql boundSql = ms.getBoundSql(parameter);
        String originalSql = boundSql.getSql();


        final RowBounds rowBounds = (RowBounds) queryArgs[ROWBOUNDS_INDEX];
        if (rowBounds == null || (rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET && rowBounds.getLimit() == RowBounds.NO_ROW_LIMIT)) {

            List<ParameterMapping> mappings = boundSql.getParameterMappings();
            boolean limitExists = Iterables.any(mappings, new Predicate<ParameterMapping>() {
                @Override
                public boolean apply(ParameterMapping input) {
                    return input.getProperty().equals(LIMIT) || input.getProperty().equals(OFFSET);
                }

                @Override
                public boolean test(@NullableDecl ParameterMapping input) {
                    return this.apply(input);
                }
            });

            if (limitExists == false) {
                return;
            }

            BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, originalSql);

            MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
            queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
            return;
        }


        Configuration configuration = ms.getConfiguration();
        String pagingSql = originalSql;

        int size = getPageParamNum(originalSql);
        if (size < 1) {
            pagingSql = getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit());
            size = getPageParamNum(pagingSql);
        }


        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, pagingSql);
        Object parameterObj = newBoundSql.getParameterObject();

        if (size == 1) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, LIMIT, Integer.class);
            newBoundSql.getParameterMappings().add(builder.build());

            if (parameterObj instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<Object> map = (MapperMethod.ParamMap<Object>) newBoundSql.getParameterObject();
                map.put(LIMIT, rowBounds.getLimit());
            }
            newBoundSql.setAdditionalParameter(LIMIT, rowBounds.getLimit());
        }

        if (size == 2) {
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, OFFSET, Integer.class);
            newBoundSql.getParameterMappings().add(builder.build());

            if (parameterObj instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<Object> map = (MapperMethod.ParamMap<Object>) newBoundSql.getParameterObject();
                map.put(OFFSET, rowBounds.getOffset());
            }
            newBoundSql.setAdditionalParameter(OFFSET, rowBounds.getOffset());

            builder = new ParameterMapping.Builder(configuration, LIMIT, Integer.class);
            newBoundSql.getParameterMappings().add(builder.build());

            if (parameterObj instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<Object> map = (MapperMethod.ParamMap<Object>) newBoundSql.getParameterObject();
                map.put(LIMIT, rowBounds.getLimit());
            }
            newBoundSql.setAdditionalParameter(LIMIT, rowBounds.getLimit());
        }


        MappedStatement newMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
        queryArgs[MAPPED_STATEMENT_INDEX] = newMs;
    }

    private String getLimitString(String query, int offset, int limit) {
        return getLimitString(query, (offset > 0));
    }

    private String getLimitString(String sql, boolean hasOffset) {
        return sql + (hasOffset ? " limit ?, ?" : " limit ?");
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        List<ParameterMapping> mappings = boundSql.getParameterMappings();
        List<ParameterMapping> newMappings = null;

        if (mappings.isEmpty()) {
            newMappings = new ArrayList<>();
        } else {
            newMappings = Lists.newArrayList(
                    Collections2.filter(mappings, new Predicate<ParameterMapping>() {
                        @Override
                        public boolean apply(ParameterMapping input) {
                            return input.getProperty().equals(LIMIT) == false && input.getProperty().equals(OFFSET) == false;
                        }

                        @Override
                        public boolean test(@NullableDecl ParameterMapping input) {
                            return this.apply(input);
                        }
                    })
            );
        }

        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, newMappings, boundSql.getParameterObject());
        for (ParameterMapping mapping : newMappings) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        return newBoundSql;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        String[] keyProperties = ms.getKeyProperties();
        builder.keyProperty(keyProperties == null ? null : keyProperties[0]);
//setStatementTimeout()
        builder.timeout(ms.getTimeout());
//setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());
//setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
//setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    private int getPageParamNum(String boundSql) {
        int lastIndexOfLimit = boundSql.lastIndexOf("limit ?");
        if (lastIndexOfLimit < 0) {
            return 0;
        }
        String addSql = boundSql.substring(boundSql.lastIndexOf("limit"));

        int size = 0;
        Pattern pattern = Pattern.compile("[?]");
        Matcher matcher = pattern.matcher(addSql);
        while (matcher.find()) {
            size++;
        }
        return size;
    }

    private static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
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
