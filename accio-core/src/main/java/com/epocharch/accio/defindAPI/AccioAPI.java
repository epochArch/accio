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
package com.epocharch.accio.defindAPI;

import com.epocharch.accio.IAccioFacade;
import com.epocharch.accio.defindAPI.dto.AccioDefinedLog;
import com.epocharch.accio.util.AccioContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenyuyao on 2017/9/22.
 */
public class AccioAPI {
    private static Logger logger = LoggerFactory.getLogger(AccioAPI.class);
    private static volatile boolean isCalInitialized = false;

    private static IAccioFacade accioFacade;
//    /**
//     * Add user-defined log
//     * @param calUserDefinedLog
//     */
//    public static void addAccioDefinedLog(CalUserDefinedLog calUserDefinedLog){
//        try {
//            if(!isCalInitialized){
//                init();
//            }
//            if(CalClientInternalFacade.getCalClientInternalFacade().isCalEnabled()) {
//                if(null!=calUserDefinedLog){
//                    CalClientInternalFacade.getCalClientInternalFacade().addCalUserDefinedLog(calUserDefinedLog);
//                }
//            }
//        } catch (Throwable t) {
//            if(null!=logger) {
//                logger.error("Error occurs when call addAccioDefinedLog for CalAPI.", t);
//            }
//        }
//    }

    /**
     * Add user-defined methods to call log information
     *
     * @param accioDefinedLog
     */
    public static void addAccioDefinedMethodLog(AccioDefinedLog accioDefinedLog){
        try {
            if(!isCalInitialized){
                init();
            }
            try {
                accioFacade.initAndGetAccioContext();
                if(accioFacade.isAccioEnable()) {
                    if(null!=accioDefinedLog){
                        accioFacade.handle(accioDefinedLog);
                    }
                }
            } finally {
               accioFacade.cleanAccioContext();
            }
        } catch (Throwable t) {
            if(null!=logger) {
                logger.error("Error occurs when call addCalUserDefinedMethodCallLog for CalAPI.", t);
            }
        }
    }

    private static synchronized void init() {
        if(!isCalInitialized){
            try {
                logger.info("init for AccioAPI.");
                accioFacade = AccioAPIFacade.getInstance();
                isCalInitialized = true;
            } catch (Throwable t) {
                if(null!=logger) {
                    logger.error("Error occurs when init for AccioAPI.", t);
                }
            }
        }
    }


}
