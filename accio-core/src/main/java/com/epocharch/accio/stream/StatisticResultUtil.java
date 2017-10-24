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
package com.epocharch.accio.stream;

import com.epocharch.accio.dto.AccioCommonAnanlyse;
import com.epocharch.accio.dto.AccioRpcAnalyse;
import com.epocharch.accio.stream.plugin.AccioAnalysePlugin;
import com.epocharch.accio.stream.plugin.RpcAnalysePlugin;
import com.epocharch.accio.stream.statistics.AccioStatisticsUtil;
import com.epocharch.accio.transport.AccioTransportManager;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * The timed task scheduling object
 *
 * Created by chenyuyao on 2017/9/27.
 */
public class StatisticResultUtil {
    private static Long DEFAULT_DELAYTIME=1000L;
    private static Long DEFAULT_PERIOD=5*1000L;
    private static Logger logger= LoggerFactory.getLogger(StatisticResultUtil.class);
    private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(2);

    private static StatisticResultUtil statisticResultUtil=new StatisticResultUtil();

    public static StatisticResultUtil getInstance(){
        return statisticResultUtil;
    }
    private StatisticResultUtil(){
        init();
    }

    private static void init(){
        long period=AccioContextUtil.getSchedule_period();
        long delay=AccioContextUtil.getSchedule_initialDelay();
        if(period==0){
            period=DEFAULT_PERIOD;
        }
        if(delay==0){
            delay=DEFAULT_DELAYTIME;
        }
        scheduledExecutor.scheduleAtFixedRate(getScheduleBizLogTask(), delay, period, TimeUnit.MILLISECONDS);
    }

    private static Runnable getScheduleBizLogTask() {
        return new Runnable() {
            public void run() {
                Date currentTimeAsMinuteStart = AccioDateUtil.getMinuteDate(new Date());
                ConcurrentMap<Date,ConcurrentMap<IMethodAnalyse,AnalystStream>> map=AccioStatisticsUtil.getAnalyseMap();
                Set<Map.Entry<Date,ConcurrentMap<IMethodAnalyse,AnalystStream>>> resultSet = map.entrySet();

                for(Map.Entry<Date,ConcurrentMap<IMethodAnalyse,AnalystStream>> entry:resultSet){
                    Date minStart=entry.getKey();
                    if(minStart.before(currentTimeAsMinuteStart)){
                        ConcurrentMap<IMethodAnalyse,AnalystStream> value=entry.getValue();
                        toEntry(value,minStart);
                        AccioStatisticsUtil.getAnalyseMap().remove(minStart);
                    }
                }
            }
        };
    }

    private static void toEntry(ConcurrentMap<IMethodAnalyse,AnalystStream> valueMap,Date minStart){
        Set<Map.Entry<IMethodAnalyse,AnalystStream>> resultSet = valueMap.entrySet();
        if(null==resultSet){
            return;
        }
        for(Map.Entry<IMethodAnalyse,AnalystStream> entry:resultSet){
            Date startTime = minStart;
            Date endTime = AccioDateUtil.getNextMinuteDate(startTime);
            AnalystStream analystStream=entry.getValue();
            Object message=null;
            if(analystStream instanceof AccioAnalysePlugin) {

                AccioCommonAnanlyse accioCommonAnanlyse = (AccioCommonAnanlyse) analystStream.toEntity();
                accioCommonAnanlyse.setStartTime(startTime);
                accioCommonAnanlyse.setEndTime(endTime);
                message=AccioTransportManager.getInstance().parseJSON(accioCommonAnanlyse.getAppType(),true,accioCommonAnanlyse);
            }else if(analystStream instanceof RpcAnalysePlugin){
                AccioRpcAnalyse accioRpcAnalyse=(AccioRpcAnalyse)analystStream.toEntity();
                accioRpcAnalyse.setStartTime(startTime);
                accioRpcAnalyse.setEndTime(endTime);
                message=AccioTransportManager.getInstance().parseJSON(accioRpcAnalyse.getAppType(),true,accioRpcAnalyse);
            }
            AccioTransportManager.getInstance().transport(message);
        }
    }


}
