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
package com.epocharch.accio.transport;

import com.alibaba.fastjson.JSONObject;
import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.dto.AccioCommonAnanlyse;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.dto.AccioRPCLog;
import com.epocharch.accio.dto.AccioRpcAnalyse;
import com.epocharch.accio.stream.dto.AppAnalyse;
import com.epocharch.accio.util.AccioUtil;
import com.epocharch.accio.web.internal.facade.GenerateLogUtil;
import com.epocharch.common.util.SystemUtil;
import org.junit.Test;

import java.util.Date;

import static com.epocharch.accio.common.AccioConstants.SUCCESSED_FAIL;

/**
 * Created by chenyuyao on 2017/10/9.
 */
public class AccioTransportTest {

    @Test
    public void testTransport(){
        //AppAnalyse appAnalyse=getAppAnalyse();
        AppAnalyse appAnalyse=getRpcAnalyse();

        JSONObject jsonObject=AccioTransportManager.getInstance().parseJSON(appAnalyse.getAppType(),true,appAnalyse);
        AccioTransportManager.getInstance().transport(jsonObject);

    }

    @Test
    public void testErrTransport(){
        AccioRPCLog accioRPCLog=getAccioRPCLog();
        JSONObject jsonObject=AccioTransportManager.getInstance().parseJSON(accioRPCLog.getAppType(),false,accioRPCLog);
//        AccioLog accioLog=getAccioLog();
//        JSONObject jsonObject=AccioTransportManager.getInstance().parseJSON(accioLog.getAppType(),false,accioLog);
        AccioTransportManager.getInstance().transport(jsonObject);
    }
    public AccioLog getAccioLog(){
        AccioLog accioLog=GenerateLogUtil.createAccioLog(LayerTypeEnum.SEVER.getCode());
        accioLog.setAppName("testAppName");
        accioLog.setServiceClassMethod("serviceMethodName");
        Throwable e=new Exception("this is web error");
        String exceptionClassname = AccioUtil.getRawClassName(e);
        accioLog.setExceptionClassname(exceptionClassname);
        String exceptionDesc = AccioUtil.getFullStackTrace(e);
        accioLog.setExceptionDesc(exceptionDesc);
        accioLog.setSuccessed(SUCCESSED_FAIL);

        return accioLog;
    }
    public AccioRPCLog getAccioRPCLog(){
        Throwable e=new Exception("java.util.concurrent.TimeoutException: Futures timed out "+
                "after [500 milliseconds]\\r\\n\\tat scala.concurrent.impl.Promise$DefaultPromise.ready(Promise.scala:219)\\r\\n\\tat scala.concurrent");
        AccioRPCLog accioRPCLog= GenerateLogUtil.createAccioRPCLog(e);
        accioRPCLog.setProviderApp("providerApp");
        accioRPCLog.setCallApp("callApp");
        accioRPCLog.setUniqId("9834598345L");
        accioRPCLog.setServiceMethodName("serviceMethod");
        accioRPCLog.setMethodName("methodName");
        accioRPCLog.setAppType(LayerTypeEnum.RPC.getCode());
        return accioRPCLog;
    }
    public AccioCommonAnanlyse getAppAnalyse(){
        AccioCommonAnanlyse accioCommonAnanlyse=new AccioCommonAnanlyse();
        accioCommonAnanlyse.setAppCode("test1");
        accioCommonAnanlyse.setAppHost("host");
        accioCommonAnanlyse.setServiceMethodName("serviceMethodName");
        accioCommonAnanlyse.setCalledCounts(23434L);
        accioCommonAnanlyse.setAppType(LayerTypeEnum.WEB.getCode());
        return accioCommonAnanlyse;
    }

    public AccioRpcAnalyse getRpcAnalyse(){
        AccioRpcAnalyse accioRpcAnalyse=new AccioRpcAnalyse();

        accioRpcAnalyse.setAppCode("appCode1");
        accioRpcAnalyse.setClientAppCode("clientAppCode");
        String localhostIp = SystemUtil.getLocalhostIp();

        accioRpcAnalyse.setAppHost(localhostIp);
        accioRpcAnalyse.setServiceMethodName("serviceMethd");
        accioRpcAnalyse.setClientMethodName("clientMethod");
        accioRpcAnalyse.setCalledCounts(234L);
        accioRpcAnalyse.setAppType(LayerTypeEnum.RPC.getCode());
        accioRpcAnalyse.setStartTime(new Date());
        accioRpcAnalyse.setEndTime(new Date());

        return accioRpcAnalyse;
    }
}
