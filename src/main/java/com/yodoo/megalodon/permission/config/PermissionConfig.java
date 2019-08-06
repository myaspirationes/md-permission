package com.yodoo.megalodon.permission.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yodoo.megalodon.datasource.annotation.EnableProvisioningDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * @Author houzhen
 * @Date 11:26 2019/8/6
**/
@Configuration
@ComponentScan(basePackages = {"com.yodoo.megalodon.permission"})
@MapperScan(basePackages = "com.yodoo.megalodon.permission.mapper", sqlSessionFactoryRef = PermissionConfig.SQL_SESSION_FACTORY_BEAN_NAME)
@EnableTransactionManagement
@EnableProvisioningDataSource
public class PermissionConfig {

    private static Logger logger = LoggerFactory.getLogger(PermissionConfig.class);

    public static final String TRANSACTION_MANAGER_BEAN_NAME = "permissionTransactionManager";

    public static final String SQL_SESSION_FACTORY_BEAN_NAME = "permissionSessionFactory";


    /***
     * Json <--> object
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 忽略未知字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean(TRANSACTION_MANAGER_BEAN_NAME)
    public DataSourceTransactionManager permissionTransactionManager(DataSource provisioningDataSource) {
        return new DataSourceTransactionManager(provisioningDataSource);
    }

    @Bean(SQL_SESSION_FACTORY_BEAN_NAME)
    public SqlSessionFactory permissionSqlSessionFactory(DataSource provisioningDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        sqlSessionFactoryBean.setEnvironment("development");
        sqlSessionFactoryBean.setDataSource(provisioningDataSource);
        // 下边仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/yodoo/megalodon/permission/mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

}