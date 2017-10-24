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

import com.epocharch.accio.dto.AccioLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by chenyuyao on 2017/10/16.
 */
@Mapper
public interface AccioWebErrMapper {
    @Insert("insert into monitor_web_log_info_er(UNIQ_REQ_ID, REQ_ID, APP_CODE,APP_HOST," +
            "CALL_ZONE,CALL_IDC,CALL_LEVEL, SUCCESSED,EXCEPTION_CLASSNAME,EXCEPTION_DESC, IN_PARAM, " +
            "OUT_PARAM ,ACTION_SERVICEMETHOD,LOG_TIME,COST_TIME,GMT_CREATE) values (#{uniqId}, #{reqId}, " +
            "#{appName}, #{appHost},#{appZone},#{appIdc},#{appLevel},#{successed}, #{exceptionClassname}," +
            "#{exceptionDesc},#{inParam},#{outParam},#{serviceClassMethod},#{requestTime},#{costTime},now())")
    void insert(AccioLog accioLog);
}
