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
import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenyuyao on 2017/9/30.
 */
public class AccioTransportManager {
    private Logger logger= LoggerFactory.getLogger(AccioTransportManager.class);

    private static AccioTransportManager accioTransportManager =new AccioTransportManager();

    private IAccioTransport accioTransport;
    public static AccioTransportManager getInstance(){
        return accioTransportManager;
    }
    private AccioTransportManager(){
        load();
    }

    private void load(){
        try {
            Config accioConfig = AccioContextUtil.getInternalConfig();
            String className=(String)accioConfig.getString("transport");
            if(AccioUtil.isBlankString(className)){
                className= AccioConstants.DEFAULT_TRANSPORT;
            }
            Object transportObj=Class.forName(className).newInstance();
            accioTransport = (IAccioTransport) transportObj;
            accioTransport.init();
        }catch (ConfigException e){
            logger.error("No configuration setting found for key transport",e);
        }catch (Exception e){
            logger.error("Loaded config error ",e);
        }
    }

    public void transport(Object message){
        try {
            accioTransport.transport(message);
        }catch (NullPointerException e){
            logger.error("The AccioTransport is null,please check the reference config",e);
        }
    }

    public void batchTransport(Object object){
        accioTransport.transport(object);
    }
    /**
     * The parameter format of the transport object:
     * {type:xxx,isMerge:xxx,messageBody:xxx}
     * */
    public<T> JSONObject parseJSON(String type,boolean isMerge,T object){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("isMerge",isMerge);
        jsonObject.put("messageBody",object);
        return jsonObject;
    }
}
