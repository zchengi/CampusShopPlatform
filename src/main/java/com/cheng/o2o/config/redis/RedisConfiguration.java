package com.cheng.o2o.config.redis;

import com.cheng.o2o.cache.JedisPoolWriper;
import com.cheng.o2o.cache.JedisUtil;
import com.cheng.o2o.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 配置 redis
 *
 * @author cheng
 *         2018/4/19 20:11
 */
@Configuration
public class RedisConfiguration {

    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.timeout}")
    private int timeout;
    @Value("${redis.pool.maxActive}")
    private int maxActive;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWaitMillis}")
    private long maxWaitMillis;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolWriper jedisPoolWriper;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建 redis 连接池的设置
     *
     * @return
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 控制一个连接池可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxActive);
        // 连接池种最多可空闲 maxIdle 个连接，这里取值为20，
        // 表示即使没有数据库连接时依然可以保持20空闲的连接，而不被清除，随时处于待命状态
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 最大等待时间：当没有可用连接时，连接池等待连接被归还的最大等待时间(以毫秒计数)，超时则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);

        return jedisPoolConfig;
    }

    /**
     * 创建 redis 连接池
     *
     * @return
     */
    @Bean(name = "jedisWritePool")
    public JedisPoolWriper createJedisPoolWriper() {
        return new JedisPoolWriper(jedisPoolConfig, hostname, port, timeout, DESUtil.getDecryptString(password));
    }

    /**
     * 创建 redis 工具类，封装好 redis 的连接以进行相关操作
     *
     * @return
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    /**
     * redis 的 key 操作
     *
     * @return
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKey() {
        return jedisUtil.new Keys();
    }

    /**
     * redis 的 Strings 操作
     *
     * @return
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings() {
        return jedisUtil.new Strings();
    }
}
