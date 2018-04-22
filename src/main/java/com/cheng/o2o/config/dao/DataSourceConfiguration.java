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

        // 每60秒检查所有的连接池中的空闲连接。Default：0
        dataSource.setIdleConnectionTestPeriod(60);
        // 连接池初始线程数 default : 15
        dataSource.setInitialPoolSize(5);
        // 连接池最大线程数
        dataSource.setMaxPoolSize(15);
        // 连接池最小线程数
        dataSource.setMinPoolSize(5);
        // 最大空闲时间，60秒内未使用则连接被丢弃。若为0则永不丢弃。Default：0
        dataSource.setMaxIdleTime(60);
        // JDBC的标准参数，用以控制数据源内加载PreparedStatements数量。
        // 但由于预缓存的statements属于单个connect而不是整个连接池。所以设置这个参数需要考虑多方面的因素。
        // 如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default：0
        dataSource.setMaxStatements(100);
        // maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。Default:0
        dataSource.setMaxStatementsPerConnection(3);
        // 连接失败重试次数
        dataSource.setAcquireRetryAttempts(3);
        //  两次连接中间隔事件，单位毫秒。Default：1000
        dataSource.setAcquireRetryDelay(1000);
        // 当连接池用完时客户端调用getConnection()后等待获取新连接的时间，
        // 超出后将抛出SQLException，如设为0则无限期等待。单位毫秒。Default：0
        dataSource.setCheckoutTimeout(10000);
        // 连接关闭时默认将所有未提交的操作回滚 default : false
        dataSource.setAutoCommitOnClose(false);

        return dataSource;
    }
}
