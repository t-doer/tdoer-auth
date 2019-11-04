package com.tdoer.auth.service;

import com.tdoer.bedrock.Platform;
import com.tdoer.bedrock.CloudEnvironment;
import com.tdoer.bedrock.product.ClientCategory;
import com.tdoer.bedrock.security.UserDetails;
import com.tdoer.bedrock.tenant.BaseUser;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DefaultUserDetailsService implements UserDetailsService {

    private UserServiceDelegate userService;

    public DefaultUserDetailsService(UserServiceDelegate userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        UserDetails ret = null;
        CloudEnvironment env = Platform.getCurrentEnvironment();
        BaseUser user = loadUser(env.getTenantId(), env.getClient().getCategory(), account);
        if(user == null){
            throw new UsernameNotFoundException("User (" + env.getTenantId() + ", " + env.getClient().getCategory() + ", " + account + ") is not found");
        }

        return new UserDetails(user);
    }

    protected BaseUser loadUser(Long tenantId, ClientCategory clientCategory, String account) throws UsernameNotFoundException{
        try {
            return userService.findByAccount(tenantId, clientCategory, account);
        } catch (UsernameNotFoundException unfe){
            throw unfe;
        } catch (Exception ex) {
            throw new UsernameNotFoundException("Failed to load user (" + tenantId + ", " + clientCategory + ", " + account + ") is not found" , ex);
        }
    }

}
