package com.neuedu.utils;

import com.google.common.collect.Lists;
import com.neuedu.pojo.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * 通用的Json与java对象互相转换通用类
 * */
public class JsonUtils {


     private static ObjectMapper objectMapper=new ObjectMapper();

     static {
          //对象中所有的字段序列化
          objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
          //取消默认timestamp格式
          objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
          //忽略空bean转json错误
          objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
          //设置日期格式
          objectMapper.setDateFormat(new SimpleDateFormat(DateUtils.STANDARD_FORMAT));
         //忽略在json字符串中存在，但是在java中不存在的属性，防止出错
          objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
     }

      /**
       * 将对象转成字符串
       * */

      public  static <T> String  obj2String(T obj){

           if(obj==null){
                return null;
           }

           try {
                return  obj instanceof  String ? (String) obj:  objectMapper.writeValueAsString(obj);
           } catch (IOException e) {
                e.printStackTrace();
           }
           return null;
      }

     public  static <T> String  obj2StringPretty(T obj){

          if(obj==null){
               return null;
          }

          try {
               return  obj instanceof  String ? (String) obj:  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
          } catch (IOException e) {
               e.printStackTrace();
          }
          return null;
     }

     /**
      * 字符串转对象
      * */

     public  static  <T> T string2Obj(String str,Class<T> clazz){

          if(StringUtils.isEmpty(str)||clazz==null){
               return null;
          }

          try {
               return  clazz.equals(String.class)?(T)str: objectMapper.readValue(str,clazz);
          } catch (IOException e) {
               e.printStackTrace();
          }
          return null;
     }

     /**
      * 将json数组转集合
      * */
     public static <T> T string2Obj(String str, TypeReference<T> typeReference){

          if(StringUtils.isEmpty(str)||typeReference==null){
               return null;
          }

          try {
               return typeReference.getType().equals(String.class)?(T)str:objectMapper.readValue(str,typeReference);
          } catch (IOException e) {
               e.printStackTrace();
          }

          return null;
     }


     public  static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elements ){

         JavaType javaType= objectMapper.getTypeFactory().constructParametricType(collectionClass,elements);

          try {
               return objectMapper.readValue(str,javaType);
          } catch (IOException e) {
               e.printStackTrace();
          }
          return null;
     }

     public static void main(String[] args) {

          UserInfo userInfo=new UserInfo();
          userInfo.setId(1);
          userInfo.setUsername("战三");
          userInfo.setEmail("111@qq.com");
          userInfo.setCreateTime(new Date());

          UserInfo userInfo1=new UserInfo();
          userInfo1.setId(2);
          userInfo1.setUsername("lisi");
          userInfo1.setEmail("111@qq.com");
          userInfo1.setCreateTime(new Date());

         String json= obj2StringPretty(userInfo);

        UserInfo userInfo2= string2Obj(json,UserInfo.class);

          List<UserInfo>  userInfoList= Lists.newArrayList();
          userInfoList.add(userInfo);
         userInfoList.add(userInfo1);

        String s=obj2StringPretty(userInfoList);

      List<UserInfo> userInfoList1=  string2Obj(s, new TypeReference<List<UserInfo>>() {
      });

       List<UserInfo> userInfoList2=string2Obj(s,List.class,UserInfo.class);

          System.out.println(userInfoList2);

     }

}
