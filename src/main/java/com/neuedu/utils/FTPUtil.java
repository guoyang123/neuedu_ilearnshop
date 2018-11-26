package com.neuedu.utils;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.List;

public class FTPUtil {


    private static  final  String FTPIP=PropertiesUtils.readByKey("ftp.server.ip");
    private static  final  String FTPUSER=PropertiesUtils.readByKey("ftp.server.user");
    private static  final  String FTPPASSWORD=PropertiesUtils.readByKey("ftp.server.password");


     private  String  ftpIp;
     private String  ftpUser;
     private  String ftpPass;
     private  Integer port;

    public FTPUtil(String ftpIp, String ftpUser, String ftpPass, Integer port) {
        this.ftpIp = ftpIp;
        this.ftpUser = ftpUser;
        this.ftpPass = ftpPass;
        this.port = port;
    }


    /**
     * 图片上传到FTP
     * */

    public  static  boolean uploadFile(List<File> fileList) throws IOException {

        FTPUtil ftpUtil=new FTPUtil(FTPIP,FTPUSER,FTPPASSWORD,21);

        System.out.println("开始连接FTP服务器...");

        ftpUtil.uploadFile("img",fileList);

        return false;
    }


    public  boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        FileInputStream fileInputStream=null;
        //连接ftp服务器
        if(connectFTPServer(ftpIp,ftpUser,ftpPass)){

            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动传输模式
                for(File file:fileList){
                    fileInputStream=new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),fileInputStream);
                }
                System.out.println("====文件上传成功====");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件上传出错...");
            }finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }


        }
        return  false;
    }


    /**
     * 连接ftp服务器
     * */
    FTPClient ftpClient=null;

    public    boolean connectFTPServer(String ip,String user,String password) {
      ftpClient=new FTPClient();

        try {
            ftpClient.connect(ip);
           return  ftpClient.login(user,password);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接FTP服务器异常...");
        }
        return false;

    }


    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPass() {
        return ftpPass;
    }

    public void setFtpPass(String ftpPass) {
        this.ftpPass = ftpPass;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
