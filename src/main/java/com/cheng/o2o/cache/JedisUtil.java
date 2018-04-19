package com.cheng.o2o.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import java.util.Set;

/**
 * @author cheng
 *         2018/4/16 17:35
 */
public class JedisUtil {

    /**
     * 缓存生存时间
     */
    private final int expire = 60000;

    /**
     * 操作 Key 的方法
     */
    public Keys keys;

    /**
     * 对存储结构为 String 类型的操作
     */
    public Strings strings;

    /**
     * redis连接池对象
     */
    private JedisPool jedisPool;

    /**
     * 设置redis连接池
     *
     * @param jedisPoolWriper
     */
    public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
        this.jedisPool = jedisPoolWriper.getJedisPool();
    }

    /**
     * 获取redis连接池
     *
     * @return
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }

        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        jedis.close();
    }

    /**
     * 设置默认过期时间
     *
     * @param key
     */
    public void expire(String key) {
        expire(key, expire);
    }

    /**
     * Keys 操作
     */
    public class Keys {

        /**
         * 清空所有 key
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String status = jedis.flushAll();
            jedis.close();
            return status;
        }

        /**
         * 删除 keys 对应的记录，可以是多个 key
         *
         * @param keys
         * @return 删除的记录数
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 判断 key 是否存在
         *
         * @param key
         * @return
         */
        public boolean exists(String key) {
            Jedis jedis = getJedis();
            boolean exists = jedis.exists(key);
            jedis.close();
            return exists;
        }

        /**
         * 查找所有匹配给定的模式的键
         *
         * @param pattern
         * @return
         */
        public Set<String> keys(String pattern) {
            Jedis jedis = getJedis();
            Set<String> set = jedis.keys(pattern);
            jedis.close();
            return set;
        }
    }

    public class Strings {

        /**
         * 根据 key 获取记录
         *
         * @param key
         * @return
         */
        public String get(String key) {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * 添加记录，如果记录已存在将覆盖原有的 value
         *
         * @param key
         * @param value
         * @return
         */
        public String set(String key, String value) {
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 添加记录，如果记录已存在将覆盖原有的 value
         *
         * @param key
         * @param value
         * @return
         */
        public String set(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.set(key, value);
            jedis.close();
            return status;
        }
    }
}

