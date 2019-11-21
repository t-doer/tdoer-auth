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
package com.tdoer.auth.controller;

import com.tdoer.auth.service.DefaultUserDetailsService;
import com.tdoer.bedrock.security.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-11-11
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private DefaultUserDetailsService userService;

    @ResponseBody
    @GetMapping("/me")
    public UserDetails getUser() {
        OAuth2Authentication auth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        String clientId = auth2Authentication.getOAuth2Request().getClientId();
        String login = auth2Authentication.getUserAuthentication().getName();
        log.debug("User request: tenant's clientId='{}', login='{}'", clientId, login);

        return userService.loadUserByUsername(login);
    }
}
