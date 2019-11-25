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
package com.tdoer.auth.config;

import com.tdoer.bedrock.security.CloudPasswordEncoder;
import com.tdoer.bedrock.security.PasswordEncoderRegistry;
import com.tdoer.security.configure.EnableAuthorizationService;
import com.tdoer.security.configure.EnableManagementProtection;
import com.tdoer.security.crypto.password.MD5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-11-11
 */

@Configuration
@EnableWebSecurity
@EnableManagementProtection
@EnableAuthorizationService
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // From auto component scan in the project
    @Autowired
    private UserDetailsService userDetailsService;

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
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        try {
            return authenticationManagerBean();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // @formatter:off
        http
                // stateless
                .authorizeRequests()
                .antMatchers("/oauth/authorize", "/oauth/confirm_access", "/passwd/modify").authenticated()
                .antMatchers("/me").access("#oauth2.hasScope('read')")
                .regexMatchers(HttpMethod.DELETE, "/oauth/users/([^/].*?)/tokens/.*")
                .access("#oauth2.clientHasRole('ROLE_TRUSTED_CLIENT')")
                .regexMatchers(HttpMethod.GET, "/oauth/clients/([^/].*?)/users/.*")
                .access("#oauth2.clientHasRole('ROLE_TRUSTED_CLIENT') and (hasRole('ROLE_USER') or #oauth2.isClient()) and #oauth2.hasScope('read')")
                .regexMatchers(HttpMethod.GET, "/oauth/clients/.*")
                .access("#oauth2.clientHasRole('ROLE_TRUSTED_CLIENT') and #oauth2.isClient() and #oauth2.hasScope('read')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/passport.html")
                .loginProcessingUrl("/oauth/login");
    } // @formatter:on

    /**
     * Configure authentication manager for username/password authentication
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new CloudPasswordEncoder(passwordEncoderRegistry()));
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
