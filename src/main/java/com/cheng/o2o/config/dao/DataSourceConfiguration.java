package com.cheng.o2o.config.dao;

import com.cheng.o2o.util.DESUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;


/**
 * 配置 datasource 到ioc容器
 *
 * @author cheng
 *         2018/4/19 16:40
 */
@Configuration
// 配置 mybatis mapper 的扫描路径
@MapperScan("com.cheng.o2o.dao")
public class DataSourceConfiguration {

    @Value("${jdbc.driver}")
    private String jdbcDriver;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * 生成与 spring-dao.xml 对应的 bean : dataSource
     *
     * @return
     * @throws PropertyVetoException
     */
    @Bean(name = "dataSource")
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {
        // 生成 datasource 实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        // 与 ssm 配置文件一样设置以下信息
        // 驱动
        dataSource.setDriverClass(jdbcDriver);
        // 数据库连接 url
        dataSource.setJdbcUrl(jdbcUrl);
        // 设置用户名
        dataSource.setUser(DESUtil.getDecryptString(jdbcUsername));
        // 设置用户密码
        dataSource.setPassword(DESUtil.getDecryptString(jdbcPassword));

        // 配置c3p0连接池的私有属性

        // 连接池初始线程数
        dataSource.setInitialPoolSize(5);
        // 连接池最大线程数
        dataSource.setMaxPoolSize(30);
        // 连接池最小线程数
        dataSource.setMinPoolSize(0);
        // 关闭链接后不自动 commit
        dataSource.setAutoCommitOnClose(false);
        // 连接超时时间
        dataSource.setCheckoutTimeout(10000);
        // 连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);

        return dataSource;
    }
}
