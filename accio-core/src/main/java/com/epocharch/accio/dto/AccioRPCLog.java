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

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/25.
 */
public class AccioRPCLog implements IBacioLog,Serializable{
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String methodName;

    private String serviceMethodName;

    private String uniqId;

    private String reqId;

    private String callZone;

    private String providerZone;

    private String callIDC;

    private String providerIDC;

    private String callLevel;

    private String providerLevel;

    private String extInfo;

    private String callApp;

    private String callHost;

    private String appType;

    private String serviceName;

    private Integer successed;
    private Long inParamLength;
    private Long outParamLength;

    private Date reqTime;

    private Date respTime;

    private Integer costTime;

    private Date gmtCreate;

    private String memo;

    private String providerApp;

    private String providerHost;

    private String exceptionClassname;

    private String inParam;

    private String outParam;

    private String exceptionDesc;
    private Integer layerType;

    @SuppressWarnings("unused")
    private String errorType;
    /*** remote层次 */
    private Integer curtLayer;
    /*** local层次 **/
    private Long localLayer;

    private String commId;

    /*
     * The parent id of the current log.
     */
    private String parentId;

    public String getInParam() {
        return inParam;
    }

    public void setInParamObjects(Object... params) {
        this.inParam = JSONObject.toJSONString(params);
        if (inParam!=null && inParam.length() > 2000) {
            this.inParam = inParam.substring(0, 2000);
        }
    }

    public String getAppType(){
        return appType;
    }
    public void setAppType(String appType){
        this.appType=appType;
    }
    public void setInParam(String inParam) {
        this.inParam = inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    public String getServiceMethodName() {
        return serviceMethodName;
    }

    public void setServiceMethodName(String serviceMethodName) {
        this.serviceMethodName = serviceMethodName;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam;
    }

    public void setOutParamObject(Object param) {
        this.outParam = JSONObject.toJSONString(param);
    }

    public String getExceptionDesc() {
        return exceptionDesc;
    }

    public void setExceptionDesc(String exceptionDesc) {
        this.exceptionDesc = exceptionDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniqId() {
        return uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCallApp() {
        return callApp == null ? "unKnowCallApp" : callApp;
    }

    public void setCallApp(String callApp) {
        this.callApp = callApp;
    }

    public String getCallHost() {
        return (callHost == null ? "unknowCallerHost" : callHost);
    }

    public void setCallHost(String callHost) {
        this.callHost = callHost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getSuccessed() {
        return successed;
    }

    public void setSuccessed(Integer successed) {
        this.successed = successed;
    }

    public Long getLocalLayer() {
        return localLayer;
    }

    public void setLocalLayer(Long localLayer) {
        this.localLayer = localLayer;
    }

    public Integer getCurtLayer() {
        return curtLayer;
    }

    public void setCurtLayer(Integer curtLayer) {
        this.curtLayer = curtLayer;
    }


    public Date getReqTime() {
        return reqTime == null ? new Date() : reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }


    public Date getRespTime() {
        return respTime;
    }


    public void setRespTime(Date respTime) {
        this.respTime = respTime;
    }

    public Integer getCostTime() {
        if (respTime != null && reqTime != null) {
            return (int) (respTime.getTime() - reqTime.getTime());
        }
        return 50;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


    public String getProviderApp() {
        return (providerApp == null ? "unknowApp" : providerApp);
    }

    public void setProviderApp(String providerApp) {
        this.providerApp = providerApp;
    }

    public String getProviderHost() {
        return (providerHost == null ? "unknowProviderHost" : providerHost);
    }

    public String getMethodName() {
        return (methodName == null ? "unknowMethodName" : methodName);
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }

    public void setProviderHost(String providerHost) {
        this.providerHost = providerHost;
    }

    public String getExceptionClassname() {
        return exceptionClassname;
    }

    public void setExceptionClassname(String exceptionClassname) {
        this.exceptionClassname = exceptionClassname;
    }

    public Integer getLayerType() {
        return layerType;
    }

    public void setLayerType(Integer layerType) {
        this.layerType = layerType;
    }

    public Long getInParamLength() {
        return inParamLength;
    }

    public void setInParamLength(Long inParamLength) {
        this.inParamLength = inParamLength;
    }

    public Long getOutParamLength() {
        return outParamLength;
    }

    public void setOutParamLength(Long outParamLength) {
        this.outParamLength = outParamLength;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public String getCallZone() {
        return callZone;
    }

    public void setCallZone(String callZone) {
        this.callZone = callZone;
    }

    public String getProviderZone() {
        return providerZone;
    }

    public void setProviderZone(String providerZone) {
        this.providerZone = providerZone;
    }

    public String getCallIDC() {
        return callIDC;
    }

    public void setCallIDC(String callidc) {
        this.callIDC = callidc;
    }

    public String getProviderIDC() {
        return providerIDC;
    }

    public void setProviderIDC(String providerIDC) {
        this.providerIDC = providerIDC;
    }

    public String getCallLevel() {
        return callLevel;
    }

    public void setCallLevel(String callLevel) {
        this.callLevel = callLevel;
    }

    public String getProviderLevel() {
        return providerLevel;
    }

    public void setProviderLevel(String providerLevel) {
        this.providerLevel = providerLevel;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
