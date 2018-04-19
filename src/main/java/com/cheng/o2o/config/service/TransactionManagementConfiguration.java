package com.cheng.o2o.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * 首先使用注解 @EnableTransactionManagement 开启事务支持
 * 在 service 方法上添加注解 @Transaction 即可
 *
 * @author cheng
 *         2018/4/19 20:03
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {

    /**
     * 注入 DataSourceConfiguration 里的 dataSource，通过 createDataSource() 获取
     */
    @Autowired
    private DataSource dataSource;

    /**
     * 事务管理，需要返回 PlatformTransactionManager 的实现
     *
     * @return
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
