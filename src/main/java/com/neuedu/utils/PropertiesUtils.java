package com.neuedu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private  static Properties properties=new Properties();

    static {

        InputStream inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取配置文件的内容
     * */
    public  static String   readByKey(String key){
        return properties.getProperty(key);
    }

}
