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
package com.epocharch.accio.dao.mapper;

import com.epocharch.accio.dto.AccioCommonAnanlyse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by chenyuyao on 2017/10/16.
 */
@Mapper
public interface AccioWebMapper {
    @Insert("INSERT INTO monitor_web_analyse(APP_CODE,APP_HOST,CALL_ZONE,CALL_IDC," +
            "CALL_LEVEL,ACTION_SERVICEMETHOD,GMT_CREATE,START_TIME,END_TIME,CALLED_COUNTS,SUCCESSED_COUNTS,FAILED_COUNTS," +
            "FAST_COUNTS,COMMON_COUNTS,SLOW_COUNTS,AGV_COST_TIME,TOTAL_COST_TIME,MAX_COST_TIME," +
            "MIN_COST_TIME,MEMO,LONG_HASH) values (#{appCode},#{appHost},#{appZone},#{appIDC},#{appLevel}," +
            "#{serviceMethodName},now(),#{startTime},#{endTime},#{calledCounts},#{successedCounts},#{failedCounts},#{fastCounts},"+
            "#{commonCounts},#{slowCounts},#{agvCostTime},#{totalCostTime},#{maxCostTime},#{minCostTime},#{memo},#{longHash})")
    void insert(AccioCommonAnanlyse accioCommonAnanlyse);
}
