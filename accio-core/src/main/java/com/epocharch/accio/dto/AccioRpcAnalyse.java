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
package com.epocharch.accio.dto;

import com.epocharch.accio.stream.dto.AppAnalyse;
import com.epocharch.accio.util.AccioUtil;

/**
 * Created by chenyuyao on 2017/9/28.
 */
public class AccioRpcAnalyse extends AppAnalyse {
    private String appHost;
    private String appCode;

    private String clientAppCode;


    private String clientMethodName;

    private String serviceMethodName;

    private String appZone;


    private String clientAppZone;


    private String appIDC;


    private String clientAppIDC;


    private String appLevel;

    private String clientAppLevel;

    private long longHash;

    public long getLongHash(){
        if(longHash==0){
            longHash=hashCode();
        }
        return longHash;
    }
    public void setLongHash(long longHash){
        this.longHash=longHash;
    }
    public String getAppHost() {
        return appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getAppCode(){
        return appCode;
    }

    public void setAppCode(String appCode){
        this.appCode=appCode;
    }

    public String getClientAppCode() {
        if (clientAppCode != null && clientAppCode.length() >= 50) {
            return clientAppCode.substring(0, 49);
        }
        return clientAppCode;
    }

    public void setClientAppCode(String clientAppCode) {
        this.clientAppCode = clientAppCode;
    }

    public String getClientMethodName() {
        if (!AccioUtil.isBlankString(clientMethodName) && clientMethodName.length() >= 100) {
            return clientMethodName.substring(0, 99);
        }
        return clientMethodName;
    }

    public void setClientMethodName(String clientMethodName) {
        this.clientMethodName = clientMethodName;
    }

    public String getServiceMethodName() {
        if (!AccioUtil.isBlankString(serviceMethodName) && serviceMethodName.length() >= 100) {
            return serviceMethodName.substring(0, 99);
        }
        return serviceMethodName;
    }

    public void setServiceMethodName(String serviceMethodName) {
        this.serviceMethodName = serviceMethodName;
    }

    public String getAppZone() {
        return appZone;
    }

    public void setAppZone(String appZone) {
        this.appZone = appZone;
    }

    public String getClientAppZone() {
        return clientAppZone;
    }

    public void setClientAppZone(String clientappZone) {
        this.clientAppZone = clientappZone;
    }

    public String getAppIDC() {
        return appIDC;
    }

    public void setAppIDC(String appIDC) {
        this.appIDC = appIDC;
    }

    public String getAppLevel() {
        return appLevel;
    }

    public void setAppLevel(String appLevel) {
        this.appLevel = appLevel;
    }

    public String getClientAppIDC() {
        return clientAppIDC;
    }

    public void setClientAppIDC(String clientAppIDC) {
        this.clientAppIDC = clientAppIDC;
    }

    public String getClientAppLevel() {
        return clientAppLevel;
    }

    public void setClientAppLevel(String clientAppLevel) {
        this.clientAppLevel = clientAppLevel;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((serviceMethodName == null) ? 0 : serviceMethodName.hashCode());
        result = prime * result
                + ((clientMethodName == null) ? 0 : clientMethodName.hashCode());
        result = prime
                * result
                + ((appCode == null) ? 0 : appCode.hashCode());
        result = prime
                * result
                + ((clientAppCode == null) ? 0 : clientAppCode.hashCode());
        result = prime * result + ((appType == null) ? 0 : appType.hashCode());
        result = prime * result + ((appHost == null) ? 0 : appHost.hashCode());
        return result;
    }

}
