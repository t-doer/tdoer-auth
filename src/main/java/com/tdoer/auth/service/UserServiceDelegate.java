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

import com.tdoer.bedrock.product.ClientCategory;
import com.tdoer.bedrock.product.UnknownClientCategoryException;
import com.tdoer.bedrock.tenant.BaseUser;
import com.tdoer.interfaces.user.service.BEndUserService;
import com.tdoer.interfaces.user.service.CEndUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Htinker Hu (htinker@163.com)
 * @create 2019-11-11
 */
@Service
@Slf4j
public class UserServiceDelegate {

    private BEndUserService bEndUserService;

    private CEndUserService cEndUserService;

    /**
     * The beans of {@link BEndUserService} and {@link CEndUserService} are from
     * Feign client {@link com.tdoer.interfaces.user}.
     *
     * @param bEndUserService
     * @param cEndUserService
     */
    public UserServiceDelegate(BEndUserService bEndUserService, CEndUserService cEndUserService){
        Assert.notNull(bEndUserService, "BEndUserService cannot be null");
        Assert.notNull(cEndUserService, "CEndUserService cannot be null");
        this.bEndUserService = bEndUserService;
        this.cEndUserService = cEndUserService;
    }

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
                throw new UnknownClientCategoryException(clientCategory);
        }

        if(user == null){
            log.info("User ({}, {}, {}) not found", tenantId, clientCategory, account);
            throw new UsernameNotFoundException("User (" + tenantId + ", " + clientCategory + ", " + account + ") not found");
        }

        return user;
    }
}
