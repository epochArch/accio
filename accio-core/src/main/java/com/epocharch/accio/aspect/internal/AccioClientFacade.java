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
import com.epocharch.accio.common.ARPCConstants;
import com.epocharch.accio.common.AccioConfig;
import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.common.IAccioConfig;
import com.epocharch.accio.dto.AccioRPCLog;
import com.epocharch.accio.dto.Constant;
import com.epocharch.accio.stream.AccioStatisticHandler;
import com.epocharch.accio.stream.IMethodAnalyse;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioUtil;
import com.epocharch.accio.web.internal.facade.GenerateLogUtil;
import com.typesafe.config.Config;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/22.
 */
public class AccioClientFacade extends IAccioFacade {

    private Logger logger= LoggerFactory.getLogger(AccioClientFacade.class);
    private static AccioClientFacade accioClientFacade=new AccioClientFacade();
    private Config fieldConfig;
    private Config methodConfig;
    private IAccioConfig iAccioConfig=null;

    private AccioClientFacade(){
        fieldConfig =AccioContextUtil.getConfig().getConfig("param.fieldParam");
        methodConfig=AccioContextUtil.getConfig().getConfig("param.methodParam");
        try {
            String className = AccioContextUtil.getInternalConfig().getString("auto-config.accioConfig");
            if(AccioUtil.isBlankString(className)){
                className=AccioConstants.DEFAULT_ACCIO_CONFIG;
            }
            iAccioConfig=(IAccioConfig)Class.forName(className).newInstance();
        }catch (Exception e){
            logger.error("load auto-config.accioConfig error",e);
        }
        iAccioConfig.init(ARPCConstants.ACCIO_CLIENT_CONFIG_PATH_ZONE);
    }
    public static AccioClientFacade getInstance(){
        return accioClientFacade;
    }
    public boolean isSendSuccessd(){
        return iAccioConfig.isToSendSuccess();
    }


    public boolean isAccioEnable(){
        return iAccioConfig.isAEnabled();
    }

    public Object[]  accioHandle4Send(ProceedingJoinPoint pjp){

        Object[] inParams=pjp.getArgs();
        initAndGetAccioContext();
        try {
            for (Object inParam : inParams) {
                Class clazz = inParam.getClass();
                String classname = clazz.getName();
                if (classname.equals(getInParamsName()))
                    setGlobalContext(inParam, clazz);
            }

        } catch (Throwable e) {
            logger.error("The sender may not have access to the Accio-agent package.Please check it!", e);
        }
        return inParams;
    }

    private void setGlobalContext(Object inparam, Class clazz) throws Throwable{
        try {
            clazz.getDeclaredFields();
            Field tracefield = clazz.getDeclaredField(Constant.trace_id);
            tracefield.setAccessible(true);
            tracefield.set(inparam, AccioContextUtil.getAttribute(AccioConstants.ACCIO_TRACE_ID,""));
            Field request_hopField = clazz.getDeclaredField(Constant.request_hop);
            request_hopField.setAccessible(true);
            request_hopField.set(inparam, ""+AccioContextUtil.getRequestHop());

            Field reqid_field = clazz.getDeclaredField(Constant.req_id);
            reqid_field.setAccessible(true);
            reqid_field.set(inparam, AccioContextUtil.getAttribute(AccioConstants.ACCIO_REQID,""));


        }catch (Exception e){
            logger.warn("inparam object:["+inparam+"] add trace_id field failed",e);
        }
        getInfoFieldAndMethod(AccioConstants.serviceAppName,inparam,clazz);
        getInfoFieldAndMethod(AccioConstants.clientMethod,inparam,clazz);
        getInfoFieldAndMethod(AccioConstants.serviceMethod,inparam,clazz);
        getInfoFieldAndMethod(AccioConstants.serviceurl,inparam,clazz);
        getInfoFieldAndMethod(AccioConstants.inParams,inparam,clazz);
    }

    @Override
    public void handle(Object obj){
        Throwable e=(Throwable)obj;
        if(isAccioEnable()) {
            AccioRPCLog accioRPCLog = GenerateLogUtil.createAccioRPCLog(e);
            cleanAccioContext();
            IMethodAnalyse iMethodAnalyse=GenerateLogUtil.createMethodAnalyse(accioRPCLog,isSendSuccessd());
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);
        }

    }

    private void getInfoFieldAndMethod(String pname,Object inparam,Class clazz){
        String fieldName=getFieldName(pname);
        try{
            Field field=clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value=field.get(inparam);
            AccioContextUtil.setAttribute(pname, value);
        }catch (Exception e){
            logger.warn("inparam class:["+clazz+"] get field ["+pname+":"+fieldName+"] is failed");
            String methodName=getMethodName(pname);
            try {
                Object val = clazz.getDeclaredMethod(methodName).invoke(inparam);
                AccioContextUtil.setAttribute(pname, val);
            }catch (Exception e2){
                logger.warn("inparam class:["+clazz+"] get method ["+pname+":"+methodName+"] is failed");
            }
        }
    }

    private String getInParamsName(){
        return AccioContextUtil.getConfig().getString("param.className");
    }

    private String getFieldName(String field){
        String name=null;
        if(fieldConfig !=null){
             name= fieldConfig.getString(field);
        }
        return name;
    }
    private String getMethodName(String method){
        String name="";
        if(methodConfig!=null){
            name=methodConfig.getString(method);
        }
        return name;
    }
}
