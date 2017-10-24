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
package com.epocharch.accio.stream.statistics;

import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.dto.IBacioLog;
import com.epocharch.accio.stream.AnalystStream;
import com.epocharch.accio.stream.AnalystStreamFactory;
import com.epocharch.accio.stream.IMethodAnalyse;
import com.epocharch.accio.stream.plugin.AccioAnalysePlugin;
import com.epocharch.accio.stream.plugin.RpcAnalysePlugin;
import com.epocharch.accio.transport.AccioTransportManager;
import com.epocharch.accio.util.AccioDateUtil;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chenyuyao on 2017/9/28.
 */
public class AccioStatisticsUtil {

    private static ConcurrentMap<Date,ConcurrentMap<IMethodAnalyse,AnalystStream>> analyseMap=new ConcurrentHashMap<Date,ConcurrentMap<IMethodAnalyse,AnalystStream>>();

    public synchronized static void analyse(IMethodAnalyse iMethodAnalyse) {
            Date minStartTime = AccioDateUtil.getMinuteDate(iMethodAnalyse.getLogTime());
            ConcurrentMap<IMethodAnalyse, AnalystStream> newMap = new ConcurrentHashMap<IMethodAnalyse, AnalystStream>();
            ConcurrentMap<IMethodAnalyse, AnalystStream> oldMap = analyseMap.putIfAbsent(minStartTime, newMap);
            if(oldMap==null){
                oldMap=newMap;
            }
            AnalystStream analystStream=null;
            if(!oldMap.containsKey(iMethodAnalyse)){
                analystStream=pluginHandler(iMethodAnalyse.getAppType(),iMethodAnalyse);
                oldMap.put(iMethodAnalyse,analystStream);
            }else{
                analystStream=oldMap.get(iMethodAnalyse);
            }
            analystStream.add(iMethodAnalyse.getSuccessd(), iMethodAnalyse.getCostTime());
            if(isNeedSend(iMethodAnalyse.getSuccessd(), iMethodAnalyse.isSendSuccess())){
                IBacioLog iBacioLog=iMethodAnalyse.getiBacioLog();
                Object message= AccioTransportManager.getInstance().parseJSON(iMethodAnalyse.getAppType(),false,iBacioLog);

                AccioTransportManager.getInstance().transport(message);
            }
    }

    private static boolean isNeedSend(int successd,boolean isSendSuccess){
        if(isSendSuccess){
            return true;
        }
        if(successd== AccioConstants.SUCCESSED_FAIL){
            return true;
        }
        return false;
    }

    public static AnalystStream pluginHandler(String type,IMethodAnalyse iMethodAnalyse){
        Class clazz=AnalystStreamFactory.getInstance().getPulginClass(type);
        String className=clazz.getName();
        AnalystStream analystStream=null;
        if(className.equals(AccioAnalysePlugin.class.getName()) ){
            analystStream=new AccioAnalysePlugin();
            analystStream.analyse(iMethodAnalyse);
        }else if(className.equals(RpcAnalysePlugin.class.getName()) ){
            analystStream=new RpcAnalysePlugin();
            analystStream.analyse(iMethodAnalyse);
        }
        return analystStream;
    }

    public static ConcurrentMap<Date,ConcurrentMap<IMethodAnalyse,AnalystStream>> getAnalyseMap(){
        return analyseMap;
    }


}
