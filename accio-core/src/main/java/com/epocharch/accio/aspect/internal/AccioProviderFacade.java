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
package com.epocharch.accio.aspect.internal;

import com.epocharch.accio.IAccioFacade;
import com.epocharch.accio.common.*;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.dto.Constant;
import com.epocharch.accio.stream.AccioStatisticHandler;
import com.epocharch.accio.stream.IMethodAnalyse;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioUtil;
import com.epocharch.accio.web.internal.facade.GenerateLogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;

import static com.epocharch.accio.common.AccioConstants.SUCCESSED_FAIL;
import static com.epocharch.accio.common.AccioConstants.SUCCESSED_SUCCESS;

/**
 * Created by chenyuyao on 2017/9/26.
 */
public class AccioProviderFacade extends IAccioFacade {
    private Logger logger= LoggerFactory.getLogger(AccioProviderFacade.class);

    private static AccioProviderFacade accioProviderFacade=new AccioProviderFacade();

    private IAccioConfig iAccioConfig=null;
    private AccioProviderFacade(){
        try {
            String className = AccioContextUtil.getInternalConfig().getString("auto-config.accioConfig");
            if(AccioUtil.isBlankString(className)){
                className=AccioConstants.DEFAULT_ACCIO_CONFIG;
            }
            iAccioConfig=(IAccioConfig)Class.forName(className).newInstance();
        }catch (Exception e){
            logger.error("load auto-config.accioConfig error",e);
        }
        iAccioConfig.init(ARPCConstants.ACCIO_SERVER_CONFIG_PATH_ZONE);
    }
    public synchronized static AccioProviderFacade getInstance(){
        return accioProviderFacade;
    }

    @Override
    public void handle(Object obj){
        if(isAccioEnable()) {
            Throwable e = (Throwable) obj;
            AccioLog acciolog = GenerateLogUtil.createAccioLog(LayerTypeEnum.SEVER.getCode());
            throwableLog(e, acciolog);
            cleanAccioContext();
            /**
             * merger and send log*/

            IMethodAnalyse iMethodAnalyse=GenerateLogUtil.createMethodAnalyse(acciolog,isSendSuccessd());
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);

        }
    }

    private void getGlobalContext(Object inparam,Class clazz) throws Throwable{
        Field tracefield = clazz.getDeclaredField(Constant.trace_id);
        tracefield.setAccessible(true);
        AccioContextUtil.setAttribute(AccioConstants.ACCIO_TRACE_ID,tracefield.get(inparam));

        Field request_hopField = clazz.getDeclaredField(Constant.request_hop);
        request_hopField.setAccessible(true);
        AccioContextUtil.setAttribute(AccioConstants.ACCIO_REQUEST_HOP,request_hopField.get(inparam));

        AccioContextUtil.getAccioContext().increaseHopValue();

        Field reqid_field = clazz.getDeclaredField(Constant.req_id);
        reqid_field.setAccessible(true);
        AccioContextUtil.setAttribute(AccioConstants.ACCIO_PARENT_ID,reqid_field.get(inparam));
    }

    public void accioHandle4Receiver(ProceedingJoinPoint pjp) {
        try {
            AccioContextUtil.setAttribute(AccioConstants.ACCIO_REQUESTTIME,new Date());
            Object[] inParams = pjp.getArgs();
            for (Object inParam : inParams) {
                String classname = inParam.getClass().getName();
                if (classname.equals(getInClassParamsName()))
                 getGlobalContext(inParam, inParam.getClass());
            }
            initAndGetAccioContext();
        }catch (Throwable e){
            logger.error("The global context is failed,and the sender may not have access to the Accio-agent package.Please check it!",e);
        }
    }
    public void throwableLog(Throwable throwable,AccioLog accioLog){
        if (throwable != null) {
            String exceptionClassname = AccioUtil.getRawClassName(throwable);
            accioLog.setExceptionClassname(exceptionClassname);

            String exceptionDesc = AccioUtil.getFullStackTrace(throwable);
            accioLog.setExceptionDesc(exceptionDesc);
            accioLog.setSuccessed(SUCCESSED_FAIL);
        } else {
            accioLog.setSuccessed(SUCCESSED_SUCCESS);
        }
    }


    private String getInClassParamsName(){
        return AccioContextUtil.getConfig().getString("param.className");
    }
    public boolean isAccioEnable(){
        return iAccioConfig.isAEnabled();
    }

    public boolean isSendSuccessd(){
        return iAccioConfig.isToSendSuccess();
    }



}
