package com.neuedu.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class JedisPoolFactory {
    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool getJedisPoolFactory(){
        JedisPoolConfig config=new JedisPoolConfig();

        config.setMaxIdle(redisConfig.getMaxIdle());
        config.setMaxTotal(redisConfig.getMaxTotal());
        config.setMinIdle(redisConfig.getMinIdle());
        //毫秒为单位
        config.setMaxWaitMillis(redisConfig.getPoolMaxWait());
        config.setTestOnBorrow(redisConfig.getTestBorrow());
        config.setTestOnReturn(redisConfig.getTestReturn());

        JedisPool jedisPool=new JedisPool(config,redisConfig.getHost(),redisConfig.getPort(),redisConfig.getTimeout(),redisConfig.getPassword(),0);


        return jedisPool;

    }
}
