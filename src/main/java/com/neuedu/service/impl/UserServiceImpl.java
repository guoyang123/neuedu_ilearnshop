package com.neuedu.service.impl;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.util.MD5Utils;
import com.neuedu.util.TokenCache;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /*前台用户登录：登录成功添加session*/
    @Override
    public ServerResponse selectByUserName(String username,String password) {
        ServerResponse sr = null;
        //判断用户名是否空
        if(username == null || username.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_USERNAME.getDesc());
            return sr;
        }

        //判断密码是否空
        if(password == null || password.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_PASSWORD.getDesc());
            return sr;
        }

        //判断用户是否存在
        int res = userInfoMapper.checkUser(username);
        if(res == 0){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.INEXISTENCE_USER.getCode(),Const.ReponseCodeEnum.INEXISTENCE_USER.getDesc());
            return sr;
        }

        //根据用户名和密码查找用户信息
        UserInfo ui = userInfoMapper.selectByUserNameAndPassword(username,MD5Utils.getMD5Code(password));
        /* 登录成功 */
        if(ui != null){
            //处理日期
            ui.setPassword("");//不向前台传送密码
            sr = ServerResponse.createServerResponseBySuccess(ui);
            return sr;
        }
        //登录失败
        else {
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.ERROR_PASSWORD.getCode(), Const.ReponseCodeEnum.ERROR_PASSWORD.getDesc());
            return sr;
        }

    }

    /*前台用户注册*/
    @Override
    public ServerResponse insertNew(UserInfo ui) {
        ServerResponse sr = null;
        //判断参数是否空
        if(ui.getUsername() == null || ui.getPassword() == null || ui.getUsername().equals("") || ui.getPassword().equals("")
                || ui.getEmail() == null || ui.getEmail().equals("") || ui.getPhone() == null || ui.getPhone().equals("")
                || ui.getQuestion() ==null || ui.getQuestion().equals("") || ui.getAnswer() == null || ui.getAnswer().equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_PARAM.getCode(),Const.ReponseCodeEnum.EMPTY_PARAM.getDesc());
            return sr;
        }
        //判断用户是否已经存在
        int res = userInfoMapper.checkUser(ui.getUsername());
        if(res != 0){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EXIST_USER.getCode(),Const.ReponseCodeEnum.EXIST_USER.getDesc());
            return sr;
        }
        //判断邮箱是否已经存在
        int resE = userInfoMapper.checkEmail(ui.getEmail());
        if(resE != 0){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EXIST_EMAIL.getCode(),Const.ReponseCodeEnum.EXIST_EMAIL.getDesc());
            return sr;
        }
        //注册到数据库
        ui.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());//确定用户身份
        ui.setPassword(MD5Utils.getMD5Code(ui.getPassword()));//密码加密
        userInfoMapper.insert(ui);
        sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.SUCCESS_USER.getDesc());
        return sr;
    }

    /*用户注册用户名或者邮箱是否可用*/
    @Override
    public ServerResponse checkUser(String str, String type) {
        ServerResponse sr = null;
        //判断用户是否已经存在
        if(type.equals("username")){
            int res = userInfoMapper.checkUser(str);
            if(res != 0){
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EXIST_USER.getCode(),Const.ReponseCodeEnum.EXIST_USER.getDesc());
                return sr;
            }else{
                sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.SUCCESS_MSG.getDesc());
                return sr;
            }
        }else if(type.equals("email")){
            //判断邮箱是否已经存在
            int resE = userInfoMapper.checkEmail(str);
            if(resE != 0){
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EXIST_EMAIL.getCode(),Const.ReponseCodeEnum.EXIST_EMAIL.getDesc());
                return sr;
            }else{
                sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.SUCCESS_MSG.getDesc());
                return sr;
            }
        }
        return sr;
    }

    /*获取登录用户信息*/
    @Override
    public ServerResponse getLoginUserMsg(HttpSession session) {
        ServerResponse sr = null;
        //获取session中的数据
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        if(ui == null){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.FORCE_EXIT.getCode(),Const.ReponseCodeEnum.FORCE_EXIT.getDesc());
            return sr;
        }else{
            UserInfo ui2 = new UserInfo();
            ui2.setId(ui.getId());
            ui2.setUsername(ui.getUsername());
            ui2.setEmail(ui.getEmail());
            ui2.setPhone(ui.getPhone());
            ui2.setCreateTime(ui.getCreateTime());
            ui2.setUpdateTime(ui.getUpdateTime());
            ui2.setPassword("");
            ui2.setQuestion("");
            ui2.setAnswer("");
            ui2.setRole(null);
            sr = ServerResponse.createServerResponseBySuccess(ui2);
            return sr;
        }
    }

    /*忘记密码*/
    @Override
    public ServerResponse forgetUserName(String username) {
        ServerResponse sr = null;
        //判断参数是否为空
        if(username == null || username.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_USERNAME.getCode(),Const.ReponseCodeEnum.EMPTY_USERNAME.getDesc());
            return sr;
        }
        //判断用户是否存在
        int res = userInfoMapper.checkUser(username);
        if(res == 0){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.INEXISTENCE_USER.getCode(),Const.ReponseCodeEnum.INEXISTENCE_USER.getDesc());
            return sr;
        }
        //判断密码问题是否存在
        UserInfo ui = userInfoMapper.forgetUserName(username);
        if(ui.getQuestion() == null || ui.getQuestion().equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NO_QUESTION.getCode(),Const.ReponseCodeEnum.NO_QUESTION.getDesc());
            return sr;
        }else{
            sr = ServerResponse.createServerResponseBySuccess(ui.getQuestion());
            return sr;
        }
    }

    /*提交问题答案*/
    @Override
    public ServerResponse forgetGetAnswer(String username, String question, String answer) {
        ServerResponse sr = null;
        //判断参数是否为空
        if(username == null || username.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_USERNAME.getCode(),Const.ReponseCodeEnum.EMPTY_USERNAME.getDesc());
            return sr;
        }
        //判断参数是否为空
        if(question == null || question.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_QUESTION.getCode(),Const.ReponseCodeEnum.EMPTY_QUESTION.getDesc());
            return sr;
        }
        //判断参数是否为空
        if(answer == null || answer.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_ANSWER.getCode(),Const.ReponseCodeEnum.EMPTY_ANSWER.getDesc());
            return sr;
        }
        //判断问题和答案是否配对正确
        int res = userInfoMapper.selectByUserNameAndQuestionAndAnswer(username,question,answer);
        if(res == 0){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.ERROR_ANSWER.getCode(),Const.ReponseCodeEnum.ERROR_ANSWER.getDesc());
            return sr;
        }
        //生成token并返回
        String forgetToken = UUID.randomUUID().toString();
        TokenCache.set(username,forgetToken);
        sr = ServerResponse.createServerResponseBySuccess(forgetToken);
        return sr;
    }

    /*忘记密码的重设密码*/
    @Override
    public ServerResponse forgetResetPassword(String username, String passwordNew, String forgetToken) {
        ServerResponse sr = null;
        //判断参数是否为空
        if(username == null || username.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_USERNAME.getCode(),Const.ReponseCodeEnum.EMPTY_USERNAME.getDesc());
            return sr;
        }
        //判断参数是否为空
        if(passwordNew == null || passwordNew.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_PASSWORD.getCode(),Const.ReponseCodeEnum.EMPTY_PASSWORD.getDesc());
            return sr;
        }
        //判断token是否失效
        String token = TokenCache.get(username);
        if(token == null || token.equals("")){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.LOSE_EFFICACY.getCode(),Const.ReponseCodeEnum.LOSE_EFFICACY.getDesc());
            return sr;
        }else if(!token.equals(forgetToken)){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.UNLAWFULNESS_TOKEN.getCode(),Const.ReponseCodeEnum.UNLAWFULNESS_TOKEN.getDesc());
            return sr;
        }else{
            //修改数据库中的密码,记得要加密
            int res = userInfoMapper.updateByUserName(username,MD5Utils.getMD5Code(passwordNew));
            //修改密码成功
            if(res == 1){
                sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.SUCCESS_PASSWORDNEW.getDesc());
                return sr;
            }else{
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.DEFEACTED_PASSWORDNEW.getCode(),Const.ReponseCodeEnum.DEFEACTED_PASSWORDNEW.getDesc());
                return sr;
            }
        }
    }

    /*登录中状态重置密码*/
    @Override
    public ServerResponse resetPassword(HttpSession session,String passwordOld, String passwordNew) {
        ServerResponse sr = null;

        //判断登录状态,获取session
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        if(ui == null){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.FORCE_EXIT.getCode(),Const.ReponseCodeEnum.FORCE_EXIT.getDesc());
            return sr;
        }else{
            //参数非空判定
            if(passwordNew == null || passwordNew.equals("")){
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_PASSWORD.getCode(),Const.ReponseCodeEnum.EMPTY_PASSWORD.getDesc());
                return sr;
            }
            if(passwordOld == null || passwordOld.equals("")){
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_PASSWORD.getCode(),Const.ReponseCodeEnum.EMPTY_PASSWORD.getDesc());
                return sr;
            }
            //根据用户名和旧密码修改密码
            int res = userInfoMapper.updateByUserNameAndPasswordOld(ui.getUsername(),MD5Utils.getMD5Code(passwordOld),MD5Utils.getMD5Code(passwordNew));
            if(res == 1){
                sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.SUCCESS_PASSWORDNEW.getDesc());
                //重置密码成功后销毁session,重新登录
                session.removeAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
                return sr;
            }else{
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.ERROR_PASSWORDOLD.getCode(),Const.ReponseCodeEnum.ERROR_PASSWORDOLD.getDesc());
                return sr;
            }
        }
    }

    /*登录状态更新个人信息*/
    @Override
    public ServerResponse updateInformation(HttpSession session, UserInfo usr) {
        ServerResponse sr = null;

        //判断登录状态,获取session
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        if(ui == null){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.FORCE_EXIT.getCode(),Const.ReponseCodeEnum.FORCE_EXIT.getDesc());
            return sr;
        }else{
            //参数非空判定
            if(usr == null){
                sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.EMPTY_PARAM2.getCode(),Const.ReponseCodeEnum.EMPTY_PARAM2.getDesc());
                return sr;
            }
            //根据用ID修改用户信息
            usr.setId(ui.getId());
            int res = userInfoMapper.updateByID(usr);
            if(res == 1){
                sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.SUCCESS_USERMSG.getDesc());
                return sr;
            }
        }
        return sr;
    }
    /*获取更新个人信息*/
    public UserInfo selectByPrimaryKey(Integer id){
        UserInfo ui = userInfoMapper.selectByPrimaryKey(id);
        return ui;
    }

    /*获取当前登录用户的详细信息*/
    @Override
    public ServerResponse getInforamtion(HttpSession session) {
        ServerResponse sr = null;
        //获取session中的数据
        UserInfo ui = (UserInfo) session.getAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        if(ui == null){
            sr = ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.FORCE_EXIT.getCode(),Const.ReponseCodeEnum.FORCE_EXIT.getDesc());
            return sr;
        }else{
            ui.setPassword("");
            sr = ServerResponse.createServerResponseBySuccess(ui);
            return sr;
        }
    }

    /*退出登录*/
    @Override
    public ServerResponse logout(HttpSession session) {
        ServerResponse sr = null;
        session.removeAttribute(Const.RoleEnum.ROLE_CUSTOMER.getDesc());
        sr = ServerResponse.createServerResponseBySuccess(Const.ReponseCodeEnum.LOGOUT.getDesc());
        return sr;
    }

    @Override
    public void updateUserIp(UserInfo userInfo) {

        userInfoMapper.updateByPrimaryKey(userInfo);
    }

}
