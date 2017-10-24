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
package com.epocharch.accio.stream.statistics;

import com.epocharch.accio.stream.dto.AppAnalyse;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static com.epocharch.accio.common.AccioConstants.SUCCESSED_FAIL;
import static com.epocharch.accio.common.AccioConstants.SUCCESSED_SUCCESS;

/**
 * Created by chenyuyao on 2017/9/26.
 */
public abstract class Statistics {
    private static final long COSTTIME_MILISECONDS_FAST_THRESHOLD = 40L;
    private static final long COSTTIME_MILISECONDS_COMMON_THRESHOLD = 80L;
    private AtomicInteger calledCounts = new AtomicInteger(0);

    private AtomicInteger successedCounts = new AtomicInteger(0);

    private AtomicInteger failedCounts = new AtomicInteger(0);

    private AtomicInteger fastCounts = new AtomicInteger(0);

    private AtomicInteger commonCounts = new AtomicInteger(0);

    private AtomicInteger slowCounts = new AtomicInteger(0);

    private AtomicInteger agvCostTime = new AtomicInteger(0);

    private AtomicLong totalCostTime = new AtomicLong(0);

    private AtomicInteger maxCostTime = new AtomicInteger(0);

    private AtomicInteger minCostTime = new AtomicInteger(0);

    /**
     *Increase the number and time of calls
     * @param successd
     * @param costTime
     * */
    public void doAdd(int successd,long costTime){
        calledCounts.incrementAndGet();
        if(successd==SUCCESSED_FAIL){
            failedCounts.incrementAndGet();
        }
        if(successd==SUCCESSED_SUCCESS){
            successedCounts.incrementAndGet();
        }

        if(costTime<=COSTTIME_MILISECONDS_FAST_THRESHOLD) {
            fastCounts.incrementAndGet();
        } else if(costTime<=COSTTIME_MILISECONDS_COMMON_THRESHOLD){
            commonCounts.incrementAndGet();
        } else {
            slowCounts.incrementAndGet();
        }

        totalCostTime.addAndGet(costTime);

        int maxcostTime =maxCostTime.get();
        if(maxcostTime < Long.valueOf(costTime).intValue()) {
            maxCostTime.set(Long.valueOf(costTime).intValue());
        }

        int mincostTime =minCostTime.get();
        if(mincostTime > Long.valueOf(costTime).intValue()) {
            minCostTime.set(Long.valueOf(costTime).intValue());
        }
    }

    public int getAgvCostTime(){
        int agvcostTime = BigDecimal.valueOf(totalCostTime.longValue()/calledCounts.intValue()).intValue();

        return agvcostTime;
    }

    /**
     * Transforming statistical results into corresponding entity objects.
     *
     * @param entry
     * @return
     */
    public <T extends AppAnalyse> T toEntry(T entry){
        entry.setAgvCostTime(getAgvCostTime());
        entry.setCalledCounts(calledCounts.longValue());
        entry.setSuccessedCounts(successedCounts.longValue());
        entry.setFailedCounts(failedCounts.longValue());
        entry.setFastCounts(fastCounts.longValue());
        entry.setCommonCounts(commonCounts.longValue());
        entry.setSlowCounts(slowCounts.longValue());
        entry.setMaxCostTime(maxCostTime.intValue());
        entry.setMinCostTime(minCostTime.intValue());
        entry.setTotalCostTime(totalCostTime.longValue());

        return entry;
    }

}
