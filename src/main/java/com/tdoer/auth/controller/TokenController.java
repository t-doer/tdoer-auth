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

import com.tdoer.springboot.rest.GenericResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-11-11
 */

@RestController
@Slf4j
@RequestMapping("")
public class TokenController {
    @GetMapping("/auth/{token}")
    GenericResponseData<OAuth2Authentication> loadAuthentication(
            @PathVariable("token") @NotBlank String token){
        //TODO
        return null;
    }

    @GetMapping("/token/{token}")
    GenericResponseData<OAuth2AccessToken> readAccessToken(
            @PathVariable("token") @NotBlank String token){
        //TODO
        return null;
    }

    @GetMapping("/token/refresh/{token}")
    GenericResponseData<OAuth2RefreshToken> readRefreshTokenForToken(
            @PathVariable("token") @NotBlank String token){
        //TODO
        return null;
    }
}


