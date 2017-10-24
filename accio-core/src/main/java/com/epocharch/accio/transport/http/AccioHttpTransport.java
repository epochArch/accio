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
package com.epocharch.accio.transport.http;

import com.alibaba.fastjson.JSONObject;
import com.epocharch.accio.transport.IAccioTransport;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.web.util.AccioHttpClientUtil;
import com.typesafe.config.Config;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by chenyuyao on 2017/9/30.
 */
public class AccioHttpTransport implements IAccioTransport{
    private static Logger logger= LoggerFactory.getLogger(AccioHttpTransport.class);
    private String serviceUrl=null;
    public void init(){
        try {
            serviceUrl = AccioContextUtil.getInternalConfig().getString("default-transport.url");
            if(logger.isInfoEnabled()){
                logger.info("default-transport.url:"+serviceUrl);
            }
        }catch (Exception e){
            logger.error("Failed to get the default transport url ",e);
        }
    }
    public void transport(Object message) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("rmi", JSONObject.toJSONString(message));
        if(serviceUrl==null){
            logger.warn("Transport is failure and the default transport url is null,please check reference.conf!! ");
            return;
        }
        try {
            AccioHttpClientUtil.remoteExcuter(serviceUrl, params);
        }catch (Exception e){
            logger.error("Transport is failure,the serviceUrl:["+serviceUrl+"] and the params:"+params,e);
        }
    }

}
