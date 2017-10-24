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

import com.epocharch.accio.util.AccioUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenyuyao on 2017/9/18.
 */
public class AccioLog implements IBacioLog,Serializable{
    private static final long serialVersionUID = 153948L;

    private Date requestTime;

    private Date responseTime;

    /**
     * log create time*/
    private Date gmtCreate;

    private long costTime;

    private int curtLayer;

    private long localLayer;

    private String inParam;

    private String outParam;

    /**
     * a span id */
    private String reqId;

    /**
     * a trace id */
    private String uniqId;

    private String parentId;

    private final Map<String,String> tags=new ConcurrentHashMap<String, String>();

    /**
     * the call successds
     * default:
     * 1 mapto successful
     * -1 map to failure*/
    private int successed;

    private String appIdc;

    private String appZone;

    private String appLevel;

    private String exceptionClassname;

    private String exceptionDesc;

    /**
     * application name
     * */
    private String appName;

    /**
     * application host*/
    private String appHost;

    /**
     * application type */
    private String appType;

    private String serviceClassMethod;

    public void setServiceClassMethod(String classMethod){
        this.serviceClassMethod=classMethod;
    }

    public String getServiceClassMethod(){
        return serviceClassMethod;
    }

    public void setAppName(String appName){
        this.appName=appName;
    }

    public String getAppName(){
        return appName;
    }

    public void setAppHost(String host){
        this.appHost=host;
    }

    public String getAppHost(){
        return appHost;
    }

    public void setAppType(String type){
        this.appType=type;
    }

    public String getAppType(){
        return appType;
    }


    public void setReqId(String reqId){
        this.reqId=reqId;
    }
    public String getReqId(){
        return reqId;
    }

    public void setParentId(String parentId){
        this.parentId =parentId;
    }
    public String getParentId(){
        return parentId;
    }

    public void setUniqId(String uniqId){
        this.uniqId=uniqId;
    }
    public String getUniqId(){
        return uniqId;
    }

    public void setInParam(String inParam){
        this.inParam=inParam;
    }
    public String getInParam(){
        return inParam;
    }
    public void setOutParam(String outParam){
        this.outParam=outParam;
    }
    public String getOutParam(){
        return outParam;
    }

    public int getSuccessed(){
        return successed;
    }
    public void setSuccessed(int successed){
        this.successed=successed;
    }
    public void setCurtLayer(int curtLayer){
        this.curtLayer=curtLayer;
    }
    public int getCurtLayer(){
        return curtLayer;
    }

    public void setLocalLayer(long localLayer){
        this.localLayer=localLayer;
    }
    public long getLocalLayer(){
        return localLayer;
    }

    public void setAppIdc(String idc){
        this.appIdc=idc;
    }
    public String getAppIdc(){
        return appIdc;
    }
    public void setAppZone(String zone){
        this.appZone=zone;
    }
    public String getAppZone(){
        return appZone;
    }

    public void setAppLevel(String level){
        this.appLevel=level;
    }
    public String getAppLevel(){
        return appLevel;
    }
    public void setRequestTime(Date requestTime){
        this.requestTime=requestTime;
    }
    public Date getRequestTime(){
        return requestTime;
    }
    public void setResponseTime(Date responseTime){
        this.responseTime=responseTime;
    }
    public Date getResponseTime(){
        return responseTime;
    }
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate=gmtCreate;
    }
    public Date getGmtCreate(){
        return gmtCreate;
    }
    public void setCostTime(long costTime){
        this.costTime=costTime;
    }
    public long getCostTime(){
        if(requestTime!=null&&responseTime!=null){
            return (responseTime.getTime()-requestTime.getTime());
        }
        return costTime;
    }

    public void setExceptionClassname(String exceptionClassname) {
        this.exceptionClassname = exceptionClassname;
    }

    public String getExceptionClassname(){
        return exceptionClassname;
    }
    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public String getExceptionDesc() {
        if (!AccioUtil.isBlankString(exceptionDesc)) {
            if(exceptionDesc.length() > 1000)
            return exceptionDesc.substring(0, 1000);
        }
        return exceptionDesc;
    }
    public void addTags(String key,String value){
        this.tags.put(key,value);
    }

    public String getTags(String key){
        return this.tags.get(key);
    }

}
