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

import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/28.
 */
public class AppAnalyse {

    protected Long id;

    protected String appCode;

    protected Date startTime;

    protected Date endTime;

    protected Integer intervalTime;

    protected Integer dateDay;

    protected Integer dateHour;

    protected Long calledCounts;


    protected Long successedCounts;


    protected Long failedCounts;

    protected String appType;

//    protected Long serverFailedCounts;

//
//    protected Long clientFailedCounts;
//
//
//    protected Long frameworkFailedCounts;
//
//
//    protected Long thirdpartyFailedCounts;

//    protected String statisticsHost;

//    protected String extInfo;

    protected Long fastCounts;

    protected Long commonCounts;

    protected Long slowCounts;

    protected Integer agvCostTime;

    protected Long totalCostTime;

    protected Integer maxCostTime;

    protected Integer minCostTime;

    protected Integer resultType;

    protected Date gmtCreate;
    protected Integer dateYear;
    protected Integer dateMonth;
    protected Integer dateDate;

    protected String memo;
//
//    /**
//     * This method returns the value of the database column
//     * monitor_app_analyse.EXT_INFO
//     *
//     * @return the value of monitor_app_analyse.EXT_INFO
//     */
//    public String getExtInfo() {
//        return extInfo;
//    }
//
//    /**
//     * This method sets the value of the database column
//     * monitor_app_analyse.EXT_INFO
//     *
//     * @param extInfo
//     *            the value for monitor_app_analyse.EXT_INFO
//     */
//    public void setExtInfo(String extInfo) {
//        this.extInfo = extInfo;
//    }

    public void setAppType(String appType){
        this.appType=appType;
    }

    public String getAppType(){
        return appType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


//    public String getAppCode() {
//        if (appCode != null && appCode.length() >= 50) {
//            return appCode.substring(0, 49);
//        }
//        return appCode;
//    }
//
//    public void setAppCode(String appCode) {
//        this.appCode = appCode;
//    }

    public Date getStartTime() {
        return startTime;
    }

    public Integer getDateDay() {
        return dateDay;
    }

    public Integer getDateHour() {
        return dateHour;
    }

    public void setDateDay(Integer dateDay) {
        this.dateDay = dateDay;
    }

    public void setDateHour(Integer dateHour) {
        this.dateHour = dateHour;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Long getCalledCounts() {
        return calledCounts;
    }

    public void setCalledCounts(Long calledCounts) {
        this.calledCounts = calledCounts;
    }

    public Long addAndGetCalledCounts(Long calledCounts) {
        this.calledCounts = add(this.calledCounts, calledCounts);
        return this.calledCounts;
    }

    public Long getSuccessedCounts() {
        if(successedCounts==null){
            return 0L;
        }
        return successedCounts;
    }

    public void setSuccessedCounts(Long successedCounts) {
        this.successedCounts = successedCounts;
    }

    public Long addAndGetSuccessedCounts(Long successedCounts) {
        this.successedCounts = add(this.successedCounts, successedCounts);
        return this.successedCounts;
    }

    public Long getFailedCounts() {
        if(failedCounts==null){
            return 0L;
        }
        return failedCounts;
    }

    public Integer getDateDate() {
        return dateDate;
    }

    public void setDateDate(Integer dateDate) {
        this.dateDate = dateDate;
    }

    public void setFailedCounts(Long failedCounts) {
        this.failedCounts = failedCounts;
    }

    public Long addAndGetFailedCounts(Long failedCounts) {
        this.failedCounts = add(this.failedCounts, failedCounts);
        return this.failedCounts;
    }

//    public Long getServerFailedCounts() {
//        return serverFailedCounts;
//    }
//
//
//    public void setServerFailedCounts(Long serverFailedCounts) {
//        this.serverFailedCounts = serverFailedCounts;
//    }
//
//    public Long addAndGetServerFailedCounts(Long serverFailedCounts) {
//        this.serverFailedCounts = add(this.serverFailedCounts, serverFailedCounts);
//        return this.serverFailedCounts;
//    }

//    public Long getClientFailedCounts() {
//        return clientFailedCounts;
//    }
//
//
//    public void setClientFailedCounts(Long clientFailedCounts) {
//        this.clientFailedCounts = clientFailedCounts;
//    }
//
//    public Long addAndGetClientFailedCounts(Long clientFailedCounts) {
//        this.clientFailedCounts = add(this.clientFailedCounts, clientFailedCounts);
//        return this.clientFailedCounts;
//    }


//    public Long getFrameworkFailedCounts() {
//        return frameworkFailedCounts;
//    }
//
//    public Long addAndGetFrameworkFailedCounts(Long frameworkFailedCounts) {
//        this.frameworkFailedCounts = add(this.frameworkFailedCounts, frameworkFailedCounts);
//        return this.frameworkFailedCounts;
//    }
//
//
//    public void setFrameworkFailedCounts(Long frameworkFailedCounts) {
//        this.frameworkFailedCounts = frameworkFailedCounts;
//    }

//    public Long getThirdpartyFailedCounts() {
//        return thirdpartyFailedCounts;
//    }
//
//
//    public void setThirdpartyFailedCounts(Long thirdpartyFailedCounts) {
//        this.thirdpartyFailedCounts = thirdpartyFailedCounts;
//    }
//
//    public Long addAndGetThirdpartyFailedCounts(Long thirdpartyFailedCounts) {
//        this.thirdpartyFailedCounts = add(this.thirdpartyFailedCounts, thirdpartyFailedCounts);
//        return this.thirdpartyFailedCounts;
//    }


    public Long getFastCounts() {
        if(fastCounts==null){
            return 0L;
        }
        return fastCounts;
    }


    public void setFastCounts(Long fastCounts) {
        this.fastCounts = fastCounts;
    }

    public Long addAndGetFastCounts(Long fastCounts) {
        this.fastCounts = add(this.fastCounts, fastCounts);
        return this.fastCounts;
    }


    public Long getCommonCounts() {
        if(commonCounts==null){
            return 0L;
        }
        return commonCounts;
    }


    public void setCommonCounts(Long commonCounts) {
        this.commonCounts = commonCounts;
    }

    public Long addAndGetCommonCounts(Long commonCounts) {
        this.commonCounts = add(this.commonCounts, commonCounts);
        return this.commonCounts;
    }


    public Long getSlowCounts() {
        if(slowCounts==null){
            return 0L;
        }
        return slowCounts;
    }


    public void setSlowCounts(Long slowCounts) {
        this.slowCounts = slowCounts;
    }

    public Long addAndGetSlowCounts(Long slowCounts) {
        this.slowCounts = add(this.slowCounts, slowCounts);
        return this.slowCounts;
    }


    public Integer getAgvCostTime() {
        if(agvCostTime==null){
            agvCostTime=0;
        }
        return agvCostTime;
    }


    public void setAgvCostTime(Integer agvCostTime) {
        this.agvCostTime = agvCostTime;
    }



    public Long getTotalCostTime() {
        if (totalCostTime == null) {
            totalCostTime = 0L;
        }
        return totalCostTime;
    }


    public void setTotalCostTime(Long totalCostTime) {
        this.totalCostTime = totalCostTime;
    }

    public Long addAndGetTotalCostTime(Long totalCostTime) {
        if (totalCostTime == null) {
            totalCostTime = 0L;
        }
        if (this.totalCostTime == null) {
            this.totalCostTime = 0L;
        }
        this.totalCostTime = this.totalCostTime + totalCostTime;
        return this.totalCostTime;
    }


    public Integer getMaxCostTime() {
        if(maxCostTime==null){
            maxCostTime=0;
        }
        return maxCostTime;
    }


    public void setMaxCostTime(Integer maxCostTime) {
        this.maxCostTime = maxCostTime;
    }


    public Integer getMinCostTime() {
        if(minCostTime==null){
            minCostTime=0;
        }
        return minCostTime;
    }


    public void setMinCostTime(Integer minCostTime) {
        this.minCostTime = minCostTime;
    }


    public Integer getResultType() {
        return resultType;
    }


    public void setResultType(Integer resultType) {
        this.resultType = resultType;
    }


//    public String getStatisticsHost() {
//        return statisticsHost;
//    }
//
//
//    public void setStatisticsHost(String statisticsHost) {
//        this.statisticsHost = statisticsHost;
//    }


    public Date getGmtCreate() {
        return gmtCreate;
    }


    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }


    protected static Long add(Long result, Long src) {
        if (result == null) {
            result = 0L;
        }
        if (src == null) {
            src = 0L;
        }

        return result + src;
    }
    public Integer getDateYear() {
        return dateYear;
    }

    public void setDateYear(Integer dateYear) {
        this.dateYear = dateYear;
    }

    public Integer getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(Integer dateMonth) {
        this.dateMonth = dateMonth;
    }

}
