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
package com.epocharch.accio.aspect;

import com.epocharch.accio.aspect.internal.AccioProviderFacade;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenyuyao on 2017/10/19.
 */
@Aspect
public  abstract class  IAccioRPCProviderAspect {
    private static Logger logger= LoggerFactory.getLogger(IAccioRPCProviderAspect.class);

    @Pointcut
    public abstract void receiver();

    @Around("receiver()")
    public Object aroundReceiver(ProceedingJoinPoint pjp) throws Throwable{
        Object result=null;
        AccioProviderFacade.getInstance().accioHandle4Receiver(pjp);
        Throwable throwable=null;
        try {
            result = pjp.proceed();
        }catch (Exception e){
            throwable=e;
        }finally {
            AccioProviderFacade.getInstance().handle(throwable);
        }
        return result;
    }

}
