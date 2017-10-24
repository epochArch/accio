/*
 * Copyright 2017 EpochArch.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epocharch.accio.web.filter;

import com.epocharch.accio.web.internal.IAccioInternalFacade;
import com.epocharch.accio.web.internal.dto.AccioWebContext;
import com.epocharch.accio.web.internal.facade.AccioWebInternalFacade;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/21.
 */
public class AccioStruts2Interceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 4904124558735205025L;

    private static Logger logger = LoggerFactory.getLogger(AccioStruts2Interceptor.class);

    private IAccioInternalFacade accioWebInternalFacade;
    @Override
    public void init() {
        if(logger.isInfoEnabled())
            logger.info("init for AccioStruts2Interceptor.");
        accioWebInternalFacade=AccioWebInternalFacade.getInstance();
    }

    @Override
    public void destroy() {
        logger.info("destroy for AccioStruts2Interceptor.");
        accioWebInternalFacade.destroy();
    }

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String result = Action.NONE;
        AccioWebContext accioWebContext = null;
        try {
            HttpServletRequest httpServletRequest = ServletActionContext.getRequest();
            try {
                accioWebContext = accioWebInternalFacade.initAndGetAWebContext(httpServletRequest);
            } finally {
                long startTime = System.currentTimeMillis();
                Exception caughtException = null;
                try {
                    result = invocation.invoke();
                } catch (Exception exception) {
                    caughtException = exception;
                    throw exception;
                } finally {
                    try {
                        accioWebContext.setResponseTime(new Date());
                        long costTime = System.currentTimeMillis() - startTime;
                        if(accioWebInternalFacade.isAWebEnabled()) {
                           accioWebInternalFacade.handleForStruts2Interceptor(caughtException, accioWebContext);
                            if(null!=caughtException) {
                                logger.error("AccioStruts2Interceptor catched Exception. Details: {}",caughtException);
                            }
                        }
                    } catch(Throwable t) {
                        logger.error("Error occurs when intercept method of AccioStruts2Interceptor called.", t);
                    }
                }
            }
        } finally {
           accioWebInternalFacade.cleanAccioWebContext(accioWebContext);
        }

        return result;
    }
}
