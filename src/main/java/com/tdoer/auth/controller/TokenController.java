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


