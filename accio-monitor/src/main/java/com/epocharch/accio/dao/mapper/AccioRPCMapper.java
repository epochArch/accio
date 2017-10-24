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

import com.epocharch.accio.dto.AccioRpcAnalyse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by chenyuyao on 2017/10/13.
 */
@Mapper
public interface AccioRPCMapper {
    @Insert( "INSERT INTO monitor_rpc_analyse(app_code,app_host,app_zone,app_idc,app_level,client_app_code,client_app_zone,"+
            "client_app_idc,client_app_level,service_method_name,client_method_name"+
            ",start_time,end_time,called_counts,successed_counts,failed_counts,fast_counts,common_counts,slow_counts,"+
            "agv_cost_time,total_cost_time,max_cost_time,min_cost_time,memo,long_hash) values ("+
            "#{appCode},#{appHost},#{appZone},#{appIDC},#{appLevel},#{clientAppCode},#{clientAppZone},#{clientAppIDC},#{clientAppLevel},"+
            "#{serviceMethodName},#{clientMethodName},#{startTime},#{endTime},#{calledCounts},#{successedCounts},#{failedCounts},#{fastCounts},"+
            "#{commonCounts},#{slowCounts},#{agvCostTime},#{totalCostTime},#{maxCostTime},#{minCostTime},#{memo},#{longHash})")
    void insert(AccioRpcAnalyse accioRpcAnalyse);
}
