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
package com.epocharch.accio.web.internal;

import com.epocharch.accio.web.internal.dto.AccioWebContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by chenyuyao on 2017/9/21.
 */
public interface IAccioInternalFacade {
    AccioWebContext initAndGetAWebContext(HttpServletRequest httpServletRequest);

    void cleanAccioWebContext(AccioWebContext accioWebContext);

    boolean isAWebEnabled();

    void handleForSpring3Interceptor(HttpServletRequest request, Object handler, Exception caughtException, AccioWebContext accioWebContext);

    void handleForStruts2Interceptor(Exception caughtException, AccioWebContext accioWebContext);

    void handleForWebFilter(HttpServletRequest httpServletRequest,
                                        Exception caughtException, AccioWebContext accioWebContext);

    void destroy();
}
