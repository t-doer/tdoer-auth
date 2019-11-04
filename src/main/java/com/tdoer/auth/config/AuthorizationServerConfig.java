package com.tdoer.auth.config;

import com.tdoer.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import com.tdoer.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import com.tdoer.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import com.tdoer.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import com.tdoer.oauth2.provider.endpoint.TrustRedirectResolver;
import com.tdoer.oauth2.provider.token.RedisTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenGranter tokenGranter;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private RedisTokenServices redisTokenServices;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    private OAuth2RequestFactory requestFactory;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .tokenGranter(tokenGranter)
                .tokenStore(tokenStore)
                .tokenServices(redisTokenServices)
                .userApprovalHandler(userApprovalHandler)
                .requestFactory(requestFactory)
                .redirectResolver(new TrustRedirectResolver())
                .authorizationCodeServices(authorizationCodeServices);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("hasRole('TRUSTED_CLIENT')")
                .checkTokenAccess("hasRole('TRUSTED_CLIENT')")
                .realm("oauth/client");
    }
}
