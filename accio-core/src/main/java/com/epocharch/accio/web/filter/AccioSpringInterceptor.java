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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/19.
 */
public class AccioSpringInterceptor extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(AccioSpringInterceptor.class);
    private static volatile boolean isInitialized = false;
    private static final ThreadLocal<AccioWebContext> accioWebContextThreadLocal = new ThreadLocal<AccioWebContext>();
    private static IAccioInternalFacade accioWebInternalFacade;
    public AccioSpringInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        try{
            if(!AccioSpringInterceptor.isInitialized){
                AccioSpringInterceptor.init();
            }
            AccioWebContext accioWebContext = accioWebInternalFacade.initAndGetAWebContext(request);
            AccioSpringInterceptor.accioWebContextThreadLocal.set(accioWebContext);

        } catch(Throwable t) {
            logger.error("Error occurs when preHandle method of AccioSpringInterceptor called.", t);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        try {
            AccioWebContext accioWebContext = null;
            try {
                try {
                    accioWebContext = AccioSpringInterceptor.accioWebContextThreadLocal.get();

                       accioWebContext.setResponseTime(new Date());
                            if(accioWebInternalFacade.isAWebEnabled()) {
                                accioWebInternalFacade.handleForSpring3Interceptor(request, handler, ex, accioWebContext);
                                if(null!=ex) {
                                    logger.error("AccioSpringInterceptor catched Exception. Details: {}",ex);
                                }
                            }
                } finally {
                   accioWebContextThreadLocal.remove();
                }
            } finally {
                accioWebInternalFacade.cleanAccioWebContext(accioWebContext);
            }
        } catch(Throwable t) {
            logger.error("Error occurs when afterCompletion method of AccioSpringInterceptor called.", t);
        }
    }

    private static synchronized void init() {
        if(!AccioSpringInterceptor.isInitialized){
            try {
                logger.info("init for AccioSpringInterceptor.");
                accioWebInternalFacade= AccioWebInternalFacade.getInstance();
                AccioSpringInterceptor.isInitialized = true;
            } catch (Throwable t) {
                logger.error("Error occurs when init for AccioSpringInterceptor.", t);
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
