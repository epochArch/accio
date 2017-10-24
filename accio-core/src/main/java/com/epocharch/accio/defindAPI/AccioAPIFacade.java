
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
package com.epocharch.accio.defindAPI;

import com.epocharch.accio.IAccioFacade;
import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.defindAPI.dto.AccioDefinedLog;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioUtil;
import com.epocharch.accio.util.dto.AccioUniqIdVo;
import com.epocharch.accio.web.internal.facade.GenerateLogUtil;
import com.epocharch.accio.web.util.AccioWebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/22.
 */
public class AccioAPIFacade extends IAccioFacade{
    private Logger logger= LoggerFactory.getLogger(AccioAPIFacade.class);
    private static AccioAPIFacade accioAPIFacade=new AccioAPIFacade();


    public static AccioAPIFacade getInstance(){
        return accioAPIFacade;
    }

    @Override
    public boolean isAccioEnable(){
        return false;
    }
    @Override
    public void cleanAccioContext(){
        try {
           boolean isClean=(Boolean)(AccioContextUtil.getAttribute(AccioConstants.ACCIO_FIRSTONTHECHAIN,false));
            if(isClean){
                AccioContextUtil.cleanGlobal();
            }
        } catch(Throwable t) {
            logger.error("Error occurs when cleanAccioContext. accioContext="+AccioContextUtil.getAccioContext(),t);

        }
    }

    @Override
    public void handle(Object o){
        AccioDefinedLog accioDefinedLog=(AccioDefinedLog) o;
        addAccioDefinedMethodLog(accioDefinedLog);
    }

    public void addAccioDefinedMethodLog(AccioDefinedLog accioDefinedLog){
        AccioLog accioLog= GenerateLogUtil.createAccioLog(LayerTypeEnum.DEFINEDED.getCode());
        dealDefindedLog(accioLog,accioDefinedLog);
        /**
         *  日志合成与发送部分后面处理*/
    }

    public void dealDefindedLog(AccioLog accioLog,AccioDefinedLog accioDefinedLog){
        Throwable throwable=accioDefinedLog.getThrowable();

        if(null!=throwable) {
            String exceptionClassname = AccioUtil.getRawClassName(throwable);
            accioLog.setExceptionClassname(exceptionClassname);
            String exceptionDesc = AccioUtil.getFullStackTrace(throwable);
            accioLog.setExceptionDesc(exceptionDesc);
            accioLog.setSuccessed(AccioConstants.SUCCESSED_FAIL);
        } else {
            accioLog.setSuccessed(AccioConstants.SUCCESSED_SUCCESS);
        }
        Class targetClass = accioDefinedLog.getTargetClass();
        // Use the simple format
        String className = targetClass.getSimpleName();

        String medhodName = accioDefinedLog.getTargetMethod();
        String methodSignature = AccioWebUtil.getMethodNameWithClassName(className, medhodName);
        accioLog.setServiceClassMethod(methodSignature);

        Object[] inParam = accioDefinedLog.getInParam();
        String inParamAsString = AccioUtil.getArrayAsString(inParam);
        accioLog.setInParam(inParamAsString);

        Object outParam = accioDefinedLog.getOutParam();
        if(outParam!=null) {
            String outParamAsString = AccioUtil.getObjectAsString(outParam);
            accioLog.setOutParam(outParamAsString);
        }
        String memo = accioDefinedLog.getMemo();
        accioLog.addTags("memo",memo);

    }

}
