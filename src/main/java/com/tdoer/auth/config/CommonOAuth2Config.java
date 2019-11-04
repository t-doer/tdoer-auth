package com.tdoer.auth.config;

import com.tdoer.bedrock.security.CloudWebAuthenticationDetailsSource;
import com.tdoer.bedrock.security.PasswordEncoderRegistry;
import com.tdoer.oauth2.client.CloudOAuth2ClientProperties;
import com.tdoer.oauth2.config.annotation.web.configuration.ResourceServerProperties;
import com.tdoer.oauth2.provider.CloudClientDetailsService;
import com.tdoer.oauth2.provider.authentication.OAuth2TokenAuthenticationSuccessHandler;
import com.tdoer.oauth2.provider.authentication.RequestTokenExtractor;
import com.tdoer.oauth2.provider.code.RedisAuthorizationCodeServices;
import com.tdoer.oauth2.provider.error.AuthenticationEntryPointDelegator;
import com.tdoer.oauth2.provider.error.RedirectUriAuthenticationEntryPoint;
import com.tdoer.oauth2.provider.error.ResourceServerOAuth2AuthenticationEntryPoint;
import com.tdoer.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import com.tdoer.oauth2.provider.token.AuthorizationServerCheckUserStatusTokenServices;
import com.tdoer.oauth2.provider.token.RedisTokenServices;
import com.tdoer.oauth2.provider.token.store.redis.RedisTokenStore;
import com.tdoer.security.crypto.password.MD5PasswordEncoder;
import com.tdoer.utils.cache.RedisJsonObjectOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class CommonOAuth2Config {
    // From auto config of spring-boot-starter-data-redis
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // From auto component scan
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public CloudOAuth2ClientProperties clientProperties() {
        return new CloudOAuth2ClientProperties();
    }

    @Bean
    public ResourceServerProperties resourceServerProperties() {
        return new ResourceServerProperties();
    }

    @Bean
    public RedisJsonObjectOperator redisJsonObjectOperator(){
        return new RedisJsonObjectOperator(stringRedisTemplate);
    }

    @Bean
    public MD5PasswordEncoder defaultPasswordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    public PasswordEncoderRegistry passwordEncoderRegistry(){
        PasswordEncoderRegistry repository = new PasswordEncoderRegistry(defaultPasswordEncoder());
        // add other password encoders for clients
        return repository;
    }

    @Bean
    @Primary
    public ClientDetailsService clientDetailsService(){
        return new CloudClientDetailsService();
    }

    @Bean
    public RedisTokenStore tokenStore(){
        RedisTokenStore tokenStore = new RedisTokenStore(redisJsonObjectOperator());
        // we can customize token store's AuthenticationKeyGenerator
        return tokenStore;
    }

    @Bean
    @Lazy
    public ApprovalStore approvalStore() throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore());
        return store;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new RedisAuthorizationCodeServices(redisJsonObjectOperator());
    }

    // The AuthenticationManager is for ResourceOwnerPasswordTokenGranter
    @Bean
    public AuthenticationManager authenticationManagerForPasswordTokenGranter(){
//        UserWithClientIdAuthenticationProvider provider = new UserWithClientIdAuthenticationProvider(passwordEncoderRepository(), userDetailsService, trustClientDetailServices());
//        return new ProviderManager(Arrays.asList(provider));
        return null;
    }

    // The AuthenticationManager is for TokenServices to refresh token
    @Bean
    public AuthenticationManager authenticationManagerForRefrshTokenServices(){
//        PreUserWithClientIdAuthenticationProvider provider = new PreUserWithClientIdAuthenticationProvider(trustClientDetailServices(), userDetailsService);
//        return new ProviderManager(Arrays.asList(provider));
        return null;
    }

    @Bean
    public OAuth2RequestFactory requestFactory(){
        // We can customize OAuth2RequestFactory to create customized OAuth2Requests
        return new DefaultOAuth2RequestFactory(clientDetailsService());
    }

    @Bean
    @Lazy
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public UserApprovalHandler userApprovalHandler() throws Exception {
        ApprovalStoreUserApprovalHandler handler = new ApprovalStoreUserApprovalHandler();
        handler.setApprovalStore(approvalStore());
        handler.setRequestFactory(requestFactory());
        handler.setClientDetailsService(clientDetailsService());
        return handler;
    }

    @Bean
    public RedisTokenServices redisTokenServices() {

        RedisTokenServices tokenServices = new RedisTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(clientDetailsService());
        // AuthenticationProvider for refresh access token
        tokenServices.setAuthenticationManager(authenticationManagerForRefrshTokenServices());

        return tokenServices;
    }

    @Bean
    @Primary
    public AuthorizationServerCheckUserStatusTokenServices userRedisTokenServices(){
        AuthorizationServerCheckUserStatusTokenServices tokenServices = new AuthorizationServerCheckUserStatusTokenServices(redisTokenServices(), userDetailsService);
        return tokenServices;
    }

    @Bean
    public TokenGranter tokenGranter(){
        ArrayList<TokenGranter> tokenGranters = new ArrayList<>(5);
        tokenGranters.add(new AuthorizationCodeTokenGranter(redisTokenServices(), authorizationCodeServices(), clientDetailsService(), requestFactory()));
        tokenGranters.add(new RefreshTokenGranter(redisTokenServices(), clientDetailsService(), requestFactory()));
        tokenGranters.add(new ImplicitTokenGranter(redisTokenServices(), clientDetailsService(), requestFactory()));
        tokenGranters.add(new ClientCredentialsTokenGranter(redisTokenServices(), clientDetailsService(), requestFactory()));
        tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManagerForPasswordTokenGranter(), redisTokenServices(), clientDetailsService(), requestFactory()));

        return new CompositeTokenGranter(tokenGranters);
    }

    @Bean
    public OAuth2TokenAuthenticationSuccessHandler auth2TokenAuthenticationSuccessHandler(){
        OAuth2TokenAuthenticationSuccessHandler successHandler = new OAuth2TokenAuthenticationSuccessHandler(clientProperties());
        successHandler.setTargetUrlParameter("redirect_uri");
        successHandler.setClientDetailsService(clientDetailsService());
        successHandler.setRequestFactory(requestFactory());
        successHandler.setTokenServices(redisTokenServices());
        return successHandler;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPointDelegator(
                new ResourceServerOAuth2AuthenticationEntryPoint(),
                new RedirectUriAuthenticationEntryPoint("/passport.html")
        );
    }

    @Bean
    public AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource(){
        return new CloudWebAuthenticationDetailsSource();
    }

    @Bean
    public TokenExtractor tokenExtractor(){
        return new RequestTokenExtractor();
    }
}
