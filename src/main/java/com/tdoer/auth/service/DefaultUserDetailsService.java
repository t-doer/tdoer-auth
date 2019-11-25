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
package com.tdoer.auth.service;

import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.security.UserDetails;
import com.tdoer.bedrock.tenant.BaseUser;
import com.tdoer.delegate.user.UserServiceDelegate;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-11-11
 */
@Service
@Primary
public class DefaultUserDetailsService implements UserDetailsService {

    private UserServiceDelegate userService;

    public DefaultUserDetailsService(UserServiceDelegate userService) {
        Assert.notNull(userService, "UserServiceDelegate cannot be null");
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        CloudEnvironment env = Platform.getCurrentEnvironment();
        BaseUser user = loadUser(env.getTenantId(), account);
        return new UserDetails(user);
    }

    protected BaseUser loadUser(Long tenantId, String account) throws UsernameNotFoundException{
        try {
            BaseUser user = userService.getUserByAccount(tenantId, account);
            if(user == null){
                new UsernameNotFoundException("User (" + tenantId + ", " + account + ") is not found");
            }
            return user;
        } catch (UsernameNotFoundException unfe){
            throw unfe;
        } catch (Exception ex) {
            throw new UsernameNotFoundException("Failed to load user (" + tenantId + ", " + account + ")" , ex);
        }
    }

}
