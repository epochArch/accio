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

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import java.io.IOException;
import java.util.Date;

/**
 *This filter will do some statistics for servlet call.
 *
 *  Created by chenyuyao on 2017/9/19.
 */
public class AccioWebFilter implements Filter{
    private static Logger logger = LoggerFactory.getLogger(AccioWebFilter.class);

    private IAccioInternalFacade accioWebInternalFacade;
    public AccioWebFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(logger.isInfoEnabled())
            logger.info("init for AccioWebFilter.");
        accioWebInternalFacade = AccioWebInternalFacade.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        AccioWebContext accioWebContext = null;
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            try {
                accioWebContext = accioWebInternalFacade.initAndGetAWebContext(httpServletRequest);
            } finally {
                Exception caughtException = null;
                try {
                    chain.doFilter(request, response);
                } catch(IOException ioException) {
                    caughtException = ioException;
                    throw ioException;
                } catch(ServletException servletException){
                    caughtException = servletException;
                    throw servletException;
                } catch (RuntimeException runtimeException) {
                    caughtException = runtimeException;
                    throw runtimeException;
                } finally{
                    try {
                        accioWebContext.setResponseTime(new Date());
                        if(accioWebInternalFacade.isAWebEnabled()) {
                            /**
                             * the log processing part
                             * */
                            accioWebInternalFacade.handleForWebFilter(httpServletRequest, caughtException, accioWebContext);
                            if(null!=caughtException) {
                               logger.error("AccioWebFilter catched Exception. Details: {}",caughtException);
                            }
                        }
                    } catch(Throwable t) {
                        logger.error("Error occurs when doFilter method of CalFilter called.", t);
                    }
                }
            }
        } finally {
            accioWebInternalFacade.cleanAccioWebContext(accioWebContext);
        }
    }

    @Override
    public void destroy() {
        logger.info("destroy for CalFilter.");
        accioWebInternalFacade.destroy();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
    }
}
