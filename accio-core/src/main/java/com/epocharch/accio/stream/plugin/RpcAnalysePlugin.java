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
package com.epocharch.accio.stream.plugin;

import com.epocharch.accio.stream.AnalystStream;
import com.epocharch.accio.stream.dto.AccioRPCWrapper;
import com.epocharch.accio.dto.AccioRpcAnalyse;
import com.epocharch.accio.stream.statistics.Statistics;

/**
 * Created by chenyuyao on 2017/9/28.
 */
public class RpcAnalysePlugin extends Statistics implements AnalystStream {

    private  AccioRpcAnalyse accioRpcAnalyse=new AccioRpcAnalyse();
    public AnalystStream analyse(Object object){
        AccioRPCWrapper accioRPCWrapper=(AccioRPCWrapper)object;

        accioRpcAnalyse.setAppCode(accioRPCWrapper.getAppName());
        accioRpcAnalyse.setClientAppCode(accioRPCWrapper.getCallApp());
        accioRpcAnalyse.setAppHost(accioRPCWrapper.getAppHost());
        accioRpcAnalyse.setServiceMethodName(accioRPCWrapper.getServiceMethodName());
        accioRpcAnalyse.setClientMethodName(accioRPCWrapper.getClientMethod());
        accioRpcAnalyse.setAppType(accioRPCWrapper.getAppType());
        return this;
    }
    public void add(int successd,long costTime){
        doAdd(successd,costTime);
    }

    public Object toEntity(){
        toEntry(accioRpcAnalyse);
        return accioRpcAnalyse;
    }

    public AnalystStream createInstance(){
        return new RpcAnalysePlugin();
    }


}
