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
package com.epocharch.accio.web.internal.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/19.
 */
public class AccioWebContext implements Serializable{
    private static final long serialVersionUID = 1L;

    private Date requestTime;
    private Date responseTime;
    private Long localLayer;
    private boolean theFirstOnTheChain;
    private String traceId;
    private Integer requestHop;

    public AccioWebContext() {
    }

    public AccioWebContext(Long localLayer, boolean theFirstOnTheChain,
                          String traceId, Integer requestHop) {
        super();
        this.localLayer = localLayer;
        this.theFirstOnTheChain = theFirstOnTheChain;
        this.traceId = traceId;
        this.requestHop = requestHop;
        requestTime=new Date();
    }

    public Date getRequetTime(){
        return requestTime;
    }
    public void setRequestTime(Date requestTime){
        this.requestTime=requestTime;
    }
    public Date getResponseTime(){
        return responseTime;
    }
    public void setResponseTime(Date responseTime){
        this.responseTime=responseTime;
    }

    public Long getLocalLayer() {
        return localLayer;
    }

    public boolean isTheFirstOnTheChain() {
        return theFirstOnTheChain;
    }

    public String getTraceId() {
        return traceId;
    }

    public Integer getRequestHop() {
        return requestHop;
    }


    @Override
    public String toString() {
        return "AccioWebContext [localLayer=" + localLayer
                + ", theFirstOnTheChain=" + theFirstOnTheChain + ", traceId="
                + traceId + ", requestHop=" + requestHop
                + "]";
    }
}
