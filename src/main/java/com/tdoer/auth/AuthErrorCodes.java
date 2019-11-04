package com.tdoer.auth;

import com.tdoer.springboot.annotation.ReasonPhrase;
import com.tdoer.springboot.http.StatusCodes;

/**
 * ExceptionCodes
 */
public interface AuthErrorCodes extends StatusCodes {

    @ReasonPhrase("短信验证码发送失败")
    int VERIFY_CODE_SEND_FAILED = 59000;

    @ReasonPhrase("短信验证码校验失败")
    int CHECK_VERIFY_CODE_FAILED = 59001;

    @ReasonPhrase("重置密码失败：{0}")
    int RESET_PASSWD_FAILED = 59002;

    @ReasonPhrase("密码修改失败：{0}")
    int INVALID_LOGINID_OR_PASSWD = 59003;

    @ReasonPhrase("获取用户角色失败：{0}")
    int GET_USER_ROLE_FAILED = 59004;

    @ReasonPhrase("验证码已经过期，请重新获取")
    int VERIFY_CODE_EXPIRED = 59005;

    @ReasonPhrase("未知客户端类型:{0}")
    int UNKNOWN_CLIENT_CATEGORY = 59006;
}