package com.neuedu.utils;

import com.neuedu.common.RedisPool;
import redis.clients.jedis.Jedis;

/**
 * redis常用api封装类
 * */
public class RedisPoolUtil {

    /**
     * @param  key
     * @param  value
     * 添加key-value
     * */
    public  static  String  set(String key,String value){

        Jedis jedis=null;
        String result=null;
       try{
           jedis= RedisPool.getJedis();
           result=jedis.set(key, value);
       } catch (Exception e){
           e.printStackTrace();
           RedisPool.returnBrokenResource(jedis);
       }finally {
           RedisPool.returnResource(jedis);
       }

          return  result;
    }


    /**
     * 设置过期时间的key-value
     * */
    public  static  String  setex(String key,String value,int expireTime){

        Jedis jedis=null;
        String result=null;
        try{
            jedis= RedisPool.getJedis();
            result=jedis.setex(key,expireTime, value);
        } catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnResource(jedis);
        }

        return  result;
    }

    /**
     * 根据Key获取value
     * */
    public  static  String  get(String key){

        Jedis jedis=null;
        String result=null;
        try{
            jedis= RedisPool.getJedis();
            result=jedis.get(key);
        } catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnResource(jedis);
        }

        return  result;
    }

    /**
     * 删除
     * */
    public  static  Long  del(String key){

        Jedis jedis=null;
        Long result=null;
        try{
            jedis= RedisPool.getJedis();
            result=jedis.del(key);
        } catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnResource(jedis);
        }

        return  result;
    }

    /**
     * 设置key的有效时间
     * */
    public  static  Long  expire(String key,int expireTime){

        Jedis jedis=null;
        Long result=null;
        try{
            jedis= RedisPool.getJedis();
            result=jedis.expire(key,expireTime);
        } catch (Exception e){
            e.printStackTrace();
            RedisPool.returnBrokenResource(jedis);
        }finally {
            RedisPool.returnResource(jedis);
        }

        return  result;
    }

    public static void main(String[] args) {

        setex("user","lisi",10);
        System.out.println(get("user"));
    }


}
