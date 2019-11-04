package com.tdoer.auth.config;

import javax.servlet.http.HttpServletRequest;

import com.tdoer.bedrock.security.CloudPasswordEncoder;
import com.tdoer.bedrock.security.CloudWebAuthenticationDetailsSource;
import com.tdoer.bedrock.security.PasswordEncoderRegistry;
import com.tdoer.bedrock.web.CloudEnvironmentParseFilterFactory;
import com.tdoer.oauth2.provider.authentication.OAuth2ProviderLogoutHandler;
import com.tdoer.oauth2.provider.authentication.OAuth2TokenAuthenticationSuccessHandler;
import com.tdoer.oauth2.provider.authentication.RedirectUriAuthenticationFailureHandler;
import com.tdoer.oauth2.provider.token.AuthorizationServerCheckUserStatusTokenServices;
import com.tdoer.security.autoconfigure.EnableManagementProtection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@EnableManagementProtection
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CloudEnvironmentParseFilterFactory clientProperties;

    @Autowired
    private ClientDetailsService clientDetailsService;

    // From auto component scan
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoderRegistry passwordEncoderRegistry;

    @Autowired
    private TokenGranter tokenGranter;

    @Autowired
    private AuthorizationServerCheckUserStatusTokenServices redisTokenServices;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private OAuth2RequestFactory requestFactory;

    @Autowired
    private OAuth2TokenAuthenticationSuccessHandler auth2TokenAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

    @Autowired
    private TokenExtractor tokenExtractor;

    @Autowired
    protected CloudEnvironmentParseFilterFactory cloudEnvironmentParseFilterFactory;

//    private OAuth2AccessTokenProcessingFilter accessTokenProcessingFilter(){
//        OAuth2ProviderTokenAuthenticationManager authenticationManager = new OAuth2ProviderTokenAuthenticationManager();
//        authenticationManager.setTokenGranter(tokenGranter);
//        authenticationManager.setTokenServices(redisTokenServices);
//        authenticationManager.setClientDetailsService(trustClientDetailServices);
//        authenticationManager.setRequestFactory(requestFactory);
//
//        OAuth2AccessTokenProcessingFilter filter = new OAuth2AccessTokenProcessingFilter();
//        filter.setTokenExtractor(tokenExtractor);
//        filter.setAuthenticationManager(authenticationManager);
//        filter.setAuthenticationDetailsSource(authenticationDetailsSource);
//        filter.whiteListAntMatcher("/oauth/login");
//
//        return filter;
//    }

    @Bean
    public LogoutHandler logoutHandler() {
        OAuth2ProviderLogoutHandler handler = new OAuth2ProviderLogoutHandler();
        handler.setTokenServices(consumerTokenServices);
        return handler;
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // @formatter:off
        http
                // parse out CloudEnvironment from request
                .addFilterBefore(cloudEnvironmentParseFilterFactory.newCloudEnvironmentParseFilter(),SecurityContextPersistenceFilter.class)
                // process access token in request if it exists
                //.addFilterAfter(accessTokenProcessingFilter(), SecurityContextPersistenceFilter.class)
                // stateless
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/authorize", "/oauth/confirm_access", "/passwd/modify").authenticated()
                .anyRequest().permitAll()
                .and()
                .logout().addLogoutHandler(logoutHandler())
                .and()
                .formLogin().authenticationDetailsSource(new CloudWebAuthenticationDetailsSource())
                .loginPage("/passport.html")
                .loginProcessingUrl("/oauth/login")
                .successHandler(auth2TokenAuthenticationSuccessHandler)
                .failureHandler(new RedirectUriAuthenticationFailureHandler("/passport.html?error=401"))
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf().disable();
    } // @formatter:on

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new CloudPasswordEncoder(passwordEncoderRegistry));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(true);

        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
        web.ignoring()
                // basic
                .antMatchers("/error")
                // swagger
                .antMatchers("/swagger-ui.html", "/swagger-resources.**", "/webjars/**", "/v2/api-docs**")
                // static html/js/icon etc.
                .antMatchers("/webjars/**", "/static/**", "/public/**", "/resource/**", "/favicon.ico");
    }
}
