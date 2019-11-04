package com.tdoer.auth.service;

import com.tdoer.auth.AuthErrorCodes;
import com.tdoer.bedrock.product.ClientCategory;
import com.tdoer.bedrock.tenant.BaseUser;
import com.tdoer.interfaces.auth.service.BEndUserService;
import com.tdoer.interfaces.auth.service.CEndUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceDelegate {

    @Autowired
    private BEndUserService bEndUserService;

    @Autowired
    private CEndUserService cEndUserService;

    public BaseUser findByAccount(Long tenantId, ClientCategory clientCategory, String account){
        BaseUser user = null;
        switch(clientCategory){
            case B_END:
                user = bEndUserService.findByAccount(tenantId, account).getData();
                break;
            case C_END:
                user = cEndUserService.findByAccount(tenantId, account).getData();
                break;
            default:
                throw new UnknownClientCategoryException(AuthErrorCodes.UNKNOWN_CLIENT_CATEGORY, clientCategory);
        }

        if(user == null){
            log.info("User ({}, {}, {}) not found", tenantId, clientCategory, account);
            throw new UsernameNotFoundException("User (" + tenantId + ", " + clientCategory + ", " + account + ") not found");
        }

        return user;
    }
}
