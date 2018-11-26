package com.neuedu.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:redis.properties")
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {


    private String  host;
    private Integer port;
    private Integer timeout;
    private Integer poolMaxWait;
    private String  password;
    //最大连接数
    private Integer  maxTotal;
    //最大空闲数
    private Integer  maxIdle;
    //最小空闲数
    private Integer  minIdle;

    //在获取jedis实例时判断该实例是否为有效的实例。
    private Boolean  testBorrow;
    //在把jedis实例放回到连接池是，检查实例是否有效
    private Boolean  testReturn;

    public Integer getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(Integer poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Boolean getTestBorrow() {
        return testBorrow;
    }

    public void setTestBorrow(Boolean testBorrow) {
        this.testBorrow = testBorrow;
    }

    public Boolean getTestReturn() {
        return testReturn;
    }

    public void setTestReturn(Boolean testReturn) {
        this.testReturn = testReturn;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
