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
package com.epocharch.accio.util;

import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.util.dto.AccioContext;
import com.epocharch.accio.util.dto.AccioUniqIdVo;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by chenyuyao on 2017/9/18.
 */
public class AccioContextUtil {
    private static Logger logger= LoggerFactory.getLogger(AccioContextUtil.class);


    private static ThreadLocal<AccioContext> accioContextThreadLocal = new ThreadLocal<AccioContext>();

    private static String appName;

    private static Config config=null;

    private static Config internalconfig;
    private static Long schedule_period=0L;
    private static Long schedule_initialDelay=0L;

    static{
        try{
            config= ConfigFactory.load().getConfig("accio");
            appName=config.getString("application.name");
            internalconfig=config.getConfig("internal-config");
            schedule_period= AccioContextUtil.getInternalConfig().getLong("logsend-schedule-config.default-period");
            schedule_initialDelay=AccioContextUtil.getInternalConfig().getLong("logsend-schedule-config.default-delaytime");
        }catch (Exception e){
            logger.error("No configuration setting found for the key: accio",e);
        }
    }
    public static String getAppName(){
        return appName;
    }

    public static Config getConfig(){
        return config;
    }

    public static Config getInternalConfig(){
        return internalconfig;
    }
    public static Long getSchedule_period(){
        return schedule_period;
    }
    public static Long getSchedule_initialDelay(){
        return schedule_initialDelay;
    }

    public static AccioContext getAccioContext() {
        AccioContext ic = accioContextThreadLocal.get();
        if (ic == null) {
            ic = new AccioContext();
            accioContextThreadLocal.set(ic);
        }
        return ic;
    }

    public static void setAccioContext(AccioContext context) {
        if (context != null) {
            accioContextThreadLocal.set(context);
        }
    }
    public static int getRequestHop() {
        return getAccioContext().getHopValue();
    }

    public static AccioUniqIdVo getAccioUniqVo(){
        AccioUniqIdVo accioUniqIdVo=new AccioUniqIdVo();
        String id=getString(AccioConstants.ACCIO_TRACE_ID,"");
        if(AccioUtil.isBlankString(id)){
            if (AccioUtil.isBlankString(appName)) {
                appName = System.getProperty("appName", "unknownApp");
                if(logger.isInfoEnabled())
                    logger.info("can't find accio.application.name in the reference.conf,the appName use default value :"+appName);
            }
            id = KeyUtils.getTraceId(appName);
            setAttribute(AccioConstants.ACCIO_TRACE_ID, id);
            accioUniqIdVo.setNewCreated(true);
        }
        accioUniqIdVo.setTraceId(id);
        return accioUniqIdVo;
    }

    public static void setAttribute(String key, Object value) {
        getAccioContext().putValue(key, value);
    }

    public static Object getAttribute(String key, Object defValue) {
        Object value = defValue;
        try {
            value = getAccioContext().getValue(key, defValue);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return value;
    }


    public static String getString(String key, String defValue) {
        String v = defValue;
        try {
            v = getAccioContext().getStrValue(key, defValue);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return v;
    }

    /**
     * clear the thread variables according to trace id
     * such as:web action
     */
    public static void cleanGlobal() {
        getAccioContext().cleanGlobalContext();
    }

    /**
     * clear the thread variables according to trace id
     * who produces,who cleans
     * */
    public static void cleanGlobal(AccioUniqIdVo accioUniqIdVo) {
        if (accioUniqIdVo != null) {
            if (accioUniqIdVo.isNewCreated()) {
                getAccioContext().cleanGlobalContext();
            } else {
                getAccioContext().cleanLocalContext();
            }
        }
    }
}
