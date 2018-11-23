package com.neuedu.common;

import com.neuedu.utils.PropertiesUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis连接池
 * */
public class RedisPool {

    private static JedisPool pool;
    //最大连接数
    private static Integer maxTotal=Integer.parseInt( PropertiesUtils.readByKey("redis.max.total"));
    //最大空闲数
    private static Integer maxIdle=Integer.parseInt( PropertiesUtils.readByKey("redis.max.idle"));
    //最小空闲数
    private static Integer minIdle=Integer.parseInt( PropertiesUtils.readByKey("redis.min.idle"));

   //在获取jedis实例时判断该实例是否为有效的实例。
    private static boolean  testOnBorrow=Boolean.parseBoolean(PropertiesUtils.readByKey("redis.test.borrow"));
    //在把jedis实例放回到连接池是，检查实例是否有效
    private static boolean  testOnReturn=Boolean.parseBoolean(PropertiesUtils.readByKey("redis.test.return"));

    private static String redisIp=PropertiesUtils.readByKey("redis.ip");
    private  static  Integer redisPort=Integer.parseInt(PropertiesUtils.readByKey("redis.port"));

    private  static  void initPool(){
        JedisPoolConfig config=new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //在连接耗尽时，是否阻塞；false：抛出异常，true:等待连接直到超时。默认true
        config.setBlockWhenExhausted(true);

         pool=new JedisPool(config,redisIp,redisPort,1000*2);
    }

    static {
        initPool();
    }

  public  static Jedis getJedis(){
        return pool.getResource();
  }

  public  static  void  returnResource(Jedis jedis){
        pool.returnResource(jedis);
  }

  public  static  void  returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
  }

    public static void main(String[] args) {

        Jedis jedis=getJedis();
        jedis.set("username","zhangsan");

        returnResource(jedis);
        pool.destroy();
        System.out.println("=====program is end===");

    }


}
