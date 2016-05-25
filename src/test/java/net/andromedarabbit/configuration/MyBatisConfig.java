package net.andromedarabbit.configuration;

/**
 * Created by Jaehoon Choi on 5/25/16.
 */

import com.zaxxer.hikari.HikariDataSource;
import net.andromedarabbit.mybatis.pagination.plugin.PaginationInterceptor;
import net.andromedarabbit.mybatis.pagination.plugin.PaginationResultSetHandlerInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author Jaehoon Choi
 * @since v2.2.7
 */
@Configuration
@EnableTransactionManagement
@MapperScan("net.andromedarabbit")
public class MyBatisConfig {

    @Inject
    Environment env;

    private String getDatabaseHostName() {
        String value = env.getProperty("database.hostname");
        if(StringUtils.isEmpty(value) == false) {
            return value;
        }

        return "localhost";
    }

    private String getDatabaseUserName() {
        String value = env.getProperty("database.username");
        if(StringUtils.isEmpty(value) == false) {
            return value;
        }

        return "root";
    }

    private String getDatabasePassword() {
        String value = env.getProperty("database.password");
        if(StringUtils.isEmpty(value) == false) {
            return value;
        }

        return "MyPassword";
    }

    @Bean
    public DataSource dataSource() {

        // See https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        final HikariDataSource ds = new HikariDataSource();
        ds.setMaximumPoolSize(20);
        ds.setDriverClassName("org.mariadb.jdbc.Driver");
        ds.setJdbcUrl(String.format("jdbc:mariadb://%s:3306/mysql", getDatabaseHostName()));
        ds.addDataSourceProperty("user", getDatabaseUserName());
        ds.addDataSourceProperty("password", getDatabasePassword());
        ds.setAutoCommit(false);
        return ds;
    }


    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(
            DataSource dataSource,
            ApplicationContext applicationContext) throws IOException {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        // 마이바티스가 사용한 DataSource를 등록
        factoryBean.setDataSource(dataSource);

        // 마이바티스 설정파일 위치 설정
        factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis-config.xml"));
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:net/andromedarabbit/persistence/mybatis/**/*.xml"));

        factoryBean.setPlugins(new Interceptor[]{
                new PaginationInterceptor(),
                new PaginationResultSetHandlerInterceptor()
        });

        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}


