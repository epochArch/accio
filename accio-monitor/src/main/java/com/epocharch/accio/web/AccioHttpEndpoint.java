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
package com.epocharch.accio.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.dao.mapper.*;

import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.dto.AccioRPCLog;
import com.epocharch.accio.dto.AccioCommonAnanlyse;
import com.epocharch.accio.dto.AccioRpcAnalyse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenyuyao on 2017/10/9.
 */
@RestController
public class AccioHttpEndpoint {

   @Autowired
    AccioRPCMapper accioRPCMapper;

   @Autowired
    AccioRPCErrMapper accioRPCErrMapper;
    private static Logger logger= LoggerFactory.getLogger(AccioHttpEndpoint.class);

    @Autowired
    AccioWebMapper accioWebMapper;

    @Autowired
    AccioWebErrMapper accioWebErrMapper;

    @Autowired
    AccioRPCSeverErrMapper accioRPCSeverErrMapper;
   // private AccioHandlerUtil accioHandlerUtil=new AccioHandlerUtil();

    @RequestMapping("/sever")
    public Object serviceAnalyse(@RequestParam(value = "rmi") String message){
        if(logger.isInfoEnabled()){
            logger.info("Receiver the message :"+message);
        }
        String value=message;
        analyse(value);
        return value;
    }
    @RequestMapping("/")
    public String hi(){
        return "hello";
    }

    public void analyse(String message){
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            String mType = jsonObject.getString("type");

            if (mType.equals(LayerTypeEnum.RPC.getCode())) {
                boolean isMerge = jsonObject.getBoolean("isMerge");
                if (isMerge) {
                    AccioRpcAnalyse accioRpcAnalyse = jsonObject.getObject("messageBody", AccioRpcAnalyse.class);
                    //save into mysql
                    accioRPCMapper.insert(accioRpcAnalyse);
                    if(logger.isInfoEnabled()){
                        logger.info("insert successfule");
                    }
                } else {
                    AccioRPCLog accioRPCLog = jsonObject.getObject("messageBody", AccioRPCLog.class);
                    if(accioRPCLog.getSuccessed()== AccioConstants.SUCCESSED_FAIL){
                        //save into db
                        accioRPCErrMapper.insert(accioRPCLog);
                        if(logger.isInfoEnabled()){
                            logger.info("accioRPCErrMapper insert successfule");
                        }
                    }

                }
            } else {
                boolean isMerge = jsonObject.getBoolean("isMerge");
                if (isMerge) {
                    AccioCommonAnanlyse accioCommonAnanlyse = jsonObject.getObject("messageBody", AccioCommonAnanlyse.class);
                    //save into db
                    if(accioCommonAnanlyse.getAppType().equals(LayerTypeEnum.WEB.getCode())){
                        accioWebMapper.insert(accioCommonAnanlyse);
                        if(logger.isInfoEnabled()){
                            logger.info("accioWebMapper insert successfule");
                        }
                    }else if(accioCommonAnanlyse.getAppType().equals(LayerTypeEnum.SEVER.getCode())){

                    }
                    //etc
                } else {
                    AccioLog accioLog = jsonObject.getObject("messageBody", AccioLog.class);
                    if(accioLog.getSuccessed()==AccioConstants.SUCCESSED_FAIL){
                        //save into db
                        if(accioLog.getAppType().equals(LayerTypeEnum.WEB.getCode())){
                            accioWebErrMapper.insert(accioLog);
                            if(logger.isInfoEnabled()){
                                logger.info("accioWebErrMapper insert successfule");
                            }
                        }if(accioLog.getAppType().equals(LayerTypeEnum.SEVER.getCode())){
                            accioRPCSeverErrMapper.insert(accioLog);
                            if(logger.isInfoEnabled()){
                                logger.info("accioRPCSeverErrMapper insert successfule");
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("accio handler the message error",e);
        }
    }
}
