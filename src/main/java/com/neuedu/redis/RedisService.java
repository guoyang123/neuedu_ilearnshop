package com.neuedu.redis;

import com.neuedu.common.RedisPool;
import com.neuedu.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {
    @Autowired
    private JedisPool jedisPool;


    /**
     * get
     * */
    public <T> T get(String key,Class<T> clazz){

        Jedis jedis=null;
        try{
            jedis=  jedisPool.getResource();
            String json=jedis.get(key);
            T t=JsonUtils.string2Obj(json,clazz);
            return t;
        }catch (Exception e){
            e.printStackTrace();
            if(jedis!=null){
                jedisPool.returnBrokenResource(jedis);
            }
        }finally {
            if(jedis!=null){
                jedisPool.returnResource(jedis);
            }
        }
       return null;
    }


    /**
     * @param  key
     * @param  t
     * 添加key-value
     * */
    public   <T>   String  set(String key,T t){

        Jedis jedis=null;
        String result=null;
        try{
            jedis=jedisPool.getResource();
            String jsonvalue=JsonUtils.obj2String(t);
            result=jedis.set(key, jsonvalue);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }

        return  null;
    }


    /**
     * 设置过期时间的key-value
     * */
    public  <T>  String  setex(String key,T t,int expireTime){

        Jedis jedis=null;
        String result=null;
        try{
            jedis=jedisPool.getResource();
            String jsonvalue=JsonUtils.obj2String(t);
            result=jedis.setex(key,expireTime, jsonvalue);
        } catch (Exception e){
            e.printStackTrace();
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }

        return  result;
    }



    /**
     * 删除
     * */
    public    Long  del(String key){

        Jedis jedis=null;
        Long result=null;
        try{
            jedis=jedisPool.getResource();
            result=jedis.del(key);
        } catch (Exception e){
            e.printStackTrace();
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }

        return  result;
    }

    /**
     * 设置key的有效时间
     * */
    public    Long  expire(String key,int expireTime){

        Jedis jedis=null;
        Long result=null;
        try{
            jedis=jedisPool.getResource();
            result=jedis.expire(key,expireTime);
        } catch (Exception e){
            e.printStackTrace();
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis);
        }

        return  result;
    }


}
