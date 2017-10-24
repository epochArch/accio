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

import com.epocharch.accio.dto.AccioRPCLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by chenyuyao on 2017/10/16.
 */
@Mapper
public interface AccioRPCErrMapper {

    @Insert("insert into monitor_client_biz_log_er (UNIQ_REQ_ID, REQ_ID, CALL_APP, CALL_HOST, CALL_ZONE, CALL_IDC," +
            "LAYER_TYPE, SUCCESSED, REQ_TIME, RESP_TIME, COST_TIME, GMT_CREATE, MEMO, PROVIDER_APP,PROVIDER_HOST," +
            "PROVIDER_ZONE,PROVIDER_IDC, EXCEPTION_CLASSNAME, ERROR_TYPE, COMM_ID, IN_PARAM, " +
            "OUT_PARAM, EXCEPTION_DESC,METHOD_NAME,SERVICE_METHOD_NAME) values (#{uniqId}, #{reqId}, " +
            "#{callApp},#{callHost}, #{callZone}, #{callIDC}, #{layerType}, #{successed},#{reqTime}, " +
            "#{respTime}, #{costTime}, now(),#{memo}, #{providerApp}, #{providerHost},#{providerZone}," +
            "#{providerIDC}, #{exceptionClassname},#{errorType}, #{commId}, #{inParam}, " +
            "#{outParam},#{exceptionDesc},#{methodName},#{serviceMethodName})")
    void insert(AccioRPCLog accioRPCLog);
}
