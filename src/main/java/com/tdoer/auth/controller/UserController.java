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
 * @author Michael Lavelle
 *         <p/>
 *         Added to provide an endpoint from which Spring Social can obtain authentication details
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
