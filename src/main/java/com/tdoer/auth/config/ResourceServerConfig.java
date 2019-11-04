package com.tdoer.auth.config;

import com.tdoer.oauth2.config.annotation.web.configuration.ResourceServerProperties;
import com.tdoer.oauth2.provider.token.AuthorizationServerCheckUserStatusTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;


@Configuration
@EnableResourceServer
@Order(0)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private TokenGranter tokenGranter;

    @Autowired
    private OAuth2RequestFactory requestFactory;

    @Autowired
    private ResourceServerProperties resourceServerProperties;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationServerCheckUserStatusTokenServices redisTokenServices;

    @Autowired
    public AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
//        ResourceServerOAuth2ProviderAuthenticationManager authenticationManager = new ResourceServerOAuth2ProviderAuthenticationManager();
//        authenticationManager.setResourceId(resourceServerProperties.getResourceId());
//        authenticationManager.setTokenGranter(tokenGranter);
//        authenticationManager.setTokenServices(redisTokenServices);
//        authenticationManager.setClientDetailsService(trustClientDetailServices);
//        authenticationManager.setRequestFactory(requestFactory);
//
//        resources.resourceId(resourceServerProperties.getResourceId())
//                .stateless(true)
//                .tokenStore(tokenStore)
//                .tokenServices(redisTokenServices)
//                .tokenExtractor(tokenExtractor)
//                // Check and refresh token
//                .authenticationManager(authenticationManager)
//                // Read appId and user agent etc. from HttpServletRequest
//                .authenticationDetailsSource(authenticationDetailsSource)
//                // Handle OAuth2Exceptions and response in OAuth2AuthenticationProcessingFilter
//                .authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
//                .addFilterBefore(new OAuth2AccessTokenResponseHeaderSettingFilter(), AnonymousAuthenticationFilter.class)
                // stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .requestMatchers().antMatchers("/oauth/users/**", "/oauth/clients/**","/me")
                .and()
                .authorizeRequests()
                .antMatchers("/me").access("#oauth2.hasScope('read')")
                .regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
                .access("#oauth2.clientHasRole('ROLE_TRUSTED_CLIENT')")
                .regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
                .access("#oauth2.clientHasRole('ROLE_TRUSTED_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
                .regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
                .access("#oauth2.clientHasRole('ROLE_TRUSTED_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')")
                .and()
                // Handle the exceptions which were not caught in controllers in ExceptionTranslationFilter
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
        // @formatter:on
    }

}