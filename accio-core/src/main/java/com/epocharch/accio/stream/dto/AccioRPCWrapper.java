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
package com.epocharch.accio.stream.dto;

import com.epocharch.accio.dto.IBacioLog;
import com.epocharch.accio.stream.IMethodAnalyse;

import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/29.
 */
public class AccioRPCWrapper implements IMethodAnalyse{
    private String appName;
    private String callApp;
    private String serviceMethodName;
    private String appHost;
    private String appType;
    private int successd;
    private long costTime;
    private Date requestTime;

    private  String clientMethod;
    private boolean isSendSuccess;
    private IBacioLog iBacioLog;
    public AccioRPCWrapper(String appName,
                           String callApp,
                           String clientMethod,
                           String serviceMethodName,
                              String appHost,
                              String appType,
                              int successd,
                              long costTime,
                              Date requestTime,
                              boolean isSendSuccess,
                              IBacioLog iBacioLog){
        this.callApp=callApp;
        this.appHost=appHost;
        this.appName=appName;
        this.appType=appType;
        this.clientMethod=clientMethod;
        this.serviceMethodName=serviceMethodName;
        this.successd=successd;
        this.costTime=costTime;
        this.requestTime=requestTime;
        this.isSendSuccess=isSendSuccess;
        this.iBacioLog=iBacioLog;
    }
    public IBacioLog getiBacioLog(){
        return iBacioLog;
    }

    public boolean isSendSuccess(){
        return isSendSuccess;
    }
    public String getAppName(){
        return appName;
    }

    public String getCallApp(){
        return callApp;
    }
    public String getAppHost(){
        return appHost;
    }
    public String getServiceMethodName(){
        return serviceMethodName;
    }
    public String getAppType(){
        return appType;
    }

    public int getSuccessd(){
        return successd;
    }
    public long getCostTime(){
        return costTime;
    }
    public Date getLogTime(){
        return requestTime;
    }
    public String getClientMethod(){
        return clientMethod;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((serviceMethodName == null) ? 0 : serviceMethodName.hashCode());
        result = prime * result
                + ((clientMethod == null) ? 0 : clientMethod.hashCode());
        result = prime
                * result
                + ((appName == null) ? 0 : appName.hashCode());
        result = prime
                * result
                + ((callApp == null) ? 0 : callApp.hashCode());
        result = prime * result + ((appType == null) ? 0 : appType.hashCode());
        result = prime * result + ((appHost == null) ? 0 : appHost.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AccioRPCWrapper)) {
            return false;
        }
        AccioRPCWrapper other = (AccioRPCWrapper) obj;
        if (clientMethod == null) {
            if (other.clientMethod != null) {
                return false;
            }
        } else if (!clientMethod.equals(other.clientMethod)) {
            return false;
        }

        if (serviceMethodName == null) {
            if (other.serviceMethodName != null) {
                return false;
            }
        } else if (!serviceMethodName.equals(other.serviceMethodName)) {
            return false;
        }

        if (appName== null) {
            if (other.appName != null) {
                return false;
            }
        } else if (!appName.equals(other.appName)) {
            return false;
        }
        if (callApp== null) {
            if (other.callApp != null) {
                return false;
            }
        } else if (!callApp.equals(other.callApp)) {
            return false;
        }
        if (appHost == null) {
            if (other.appHost != null) {
                return false;
            }
        } else if (!appHost.equals(other.appHost)) {
            return false;
        }
        if (appType == null) {
            if (other.appType != null) {
                return false;
            }
        } else if (!appType.equals(other.appType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AccioRPCWrapper [appName=" + appName +", callApp="+callApp+ ", appHost="
                + appHost +",appType="+appType+ ", clientMethod="+clientMethod+", serviceMethodName=" + serviceMethodName
                + "]";
    }
}
