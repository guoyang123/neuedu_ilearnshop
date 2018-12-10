package com.neuedu.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiuNiuUtils {

    public static DefaultPutRet upLoadImage(String filePath,String keys) {

        DefaultPutRet putRet = null;
        //构造一个带指定Zone对象的配置类,华北区域
        Configuration cfg = new Configuration(Zone.zone1());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        String accessKey = "6JhwYyJs0AuaYLArQ7RxxRK_LRog-t4F2I9-s5e6";
        String secretKey = "J8miH3bnt2D5k2Sy66njm6rnLHsrnZzG5EyxTTOe";
        String bucket = "neuedu-ilearn";
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = filePath;
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = keys;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println("返回的内容"+JsonUtils.obj2String(response));
            System.out.println(response.statusCode);
            System.out.println(putRet.hash);

            return putRet;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
        return putRet;
    }
}
