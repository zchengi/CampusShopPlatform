package com.cheng.o2o.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 指定redis的JedisPool接口构造函数，
 * 这样才能在服务器成功创建jedisPool
 *
 * @author cheng
 *         2018/4/16 17:00
 */
public class JedisPoolWriper {

    /**
     * redis连接池对象
     */
    private JedisPool jedisPool;

    private static Logger logger = LoggerFactory.getLogger(JedisPoolWriper.class);

    public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host,
                           final int port, final int timeout, final String password) {
        try {
            jedisPool = new JedisPool(poolConfig, host, port, timeout, password);
        } catch (Exception e) {
            logger.error("Jedis Pool init failed : " + e);
            e.printStackTrace();
        }
    }

    /**
     * 注入redis连接池对象
     *
     * @param jedisPool
     */
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获取redis连接池对象
     *
     * @return
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
