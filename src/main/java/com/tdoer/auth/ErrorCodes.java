/*
 * Copyright 2019 T-Doer (tdoer.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tdoer.auth;

import com.tdoer.springboot.annotation.ReasonPhrase;
import com.tdoer.springboot.http.StatusCodes;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-11-11
 */
public interface ErrorCodes extends StatusCodes {

    @ReasonPhrase("短信验证码发送失败")
    int VERIFY_CODE_SEND_FAILED = 59000;

    @ReasonPhrase("短信验证码校验失败")
    int CHECK_VERIFY_CODE_FAILED = 59001;

    @ReasonPhrase("重置密码失败：{0}")
    int RESET_PASSWD_FAILED = 59002;

    @ReasonPhrase("密码修改失败：{0}")
    int INVALID_LOGINID_OR_PASSWD = 59003;

    @ReasonPhrase("验证码已经过期，请重新获取")
    int VERIFY_CODE_EXPIRED = 59005;

}