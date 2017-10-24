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
package com.epocharch.accio.web.internal.facade;

/**
 * Created by chenyuyao on 2017/9/21.
 */
import com.epocharch.accio.common.AWebConstants;
import com.epocharch.accio.common.AccioConfig;
import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.common.IAccioConfig;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.stream.AccioStatisticHandler;
import com.epocharch.accio.stream.IMethodAnalyse;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioUtil;
import com.epocharch.accio.util.dto.AccioUniqIdVo;
import com.epocharch.accio.web.internal.IAccioInternalFacade;
import com.epocharch.accio.web.internal.dto.AccioWebContext;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;


/**
 * This class act as the bridge between internal and external classes
 */
public class AccioWebInternalFacade implements IAccioInternalFacade {
    private static Logger logger = LoggerFactory.getLogger(AccioWebInternalFacade.class);

    private static AccioWebInternalFacade calClientInternalFacade = new AccioWebInternalFacade();

    private IAccioConfig iAccioConfig;
    private AccioWebInternalFacade() {
        try {
            String className = AccioContextUtil.getInternalConfig().getString("auto-config.accioConfig");
            if(AccioUtil.isBlankString(className)){
                className= AccioConstants.DEFAULT_ACCIO_CONFIG;
            }
            iAccioConfig=(IAccioConfig)Class.forName(className).newInstance();
        }catch (Exception e){
            logger.error("load auto-config.accioConfig error",e);
        }
        iAccioConfig.init(AWebConstants.ACCIO_CONFIG_PATH_ZONE);
        init();
    }

    public static IAccioInternalFacade getInstance() {
        return calClientInternalFacade;
    }

    public synchronized void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                IAccioInternalFacade calClientInternalFacade = AccioWebInternalFacade.getInstance();
                if (null != calClientInternalFacade) {// It may be null because of gc.
                    calClientInternalFacade.destroy();
                    if (null != logger) {
                        logger.info("AccioWebInternalFacade.getInstance().destroy() was called in shutdownHook.");
                    }
                } else {
                    if (null != logger) {
                        logger.info("AccioWebInternalFacade is null. So do not call AccioWebInternalFacade.getInstance().destroy().");
                    }
                }
            }
        }));
    }

    public synchronized void destroy() {
//        CalAnalyzer.shutdown();
//        CalStatisticTargetHandler.shutdown();
//        CalStatisticDataHelper.clearAll();
//        CalConfigCenter.destroy();
//        MonitorJmsSendUtil.destroy();
    }

    public AccioWebContext initAndGetAWebContext(HttpServletRequest httpServletRequest) {
        AccioWebContext returnValue = null;
        try {
            AccioUniqIdVo accioUniqIdVo = AccioContextUtil.getAccioUniqVo();
            if (null != accioUniqIdVo) {
                Long localLayer = this.getLocalLayer();

                boolean theFirstOnTheChain = accioUniqIdVo.isNewCreated();
                String globalId = accioUniqIdVo.getTraceId();
                Integer requestHop = Integer.valueOf(AccioContextUtil.getRequestHop());
                returnValue = new AccioWebContext(localLayer, theFirstOnTheChain, globalId, requestHop);
            }
        } catch (Throwable t) {
            logger.error("Error occurs when initAndGetAWebContext. httpServletRequest=" + httpServletRequest, t);
        }

        return returnValue;
    }

    private Long getLocalLayer() {
        Long localLayer = Long.valueOf(System.nanoTime());
        return localLayer;
    }


    public AccioWebContext initAndGetAWebContext() {
        AccioWebContext returnValue = this.initAndGetAWebContext(null);
        return returnValue;
    }

    /**
     * clean AccioWebContext if needed
     *
     * @param accioWebContext
     */
    public void cleanAccioWebContext(AccioWebContext accioWebContext) {
        try {
            if (null != accioWebContext && accioWebContext.isTheFirstOnTheChain()) {
                AccioContextUtil.cleanGlobal();
            }
        } catch (Throwable t) {
            if (null != logger) {
                logger.error("Error occurs when cleanAccioWebContext. accioWebContext=" + accioWebContext, t);
            }
        }
    }

    public boolean isAWebEnabled() {
        boolean isAWebEnabled = iAccioConfig.isAEnabled();
        return isAWebEnabled;
    }

    public boolean isSendSuccessd(){
        return iAccioConfig.isToSendSuccess();
    }


    public void handleForSpring3Interceptor(HttpServletRequest request,
                                                        Object handler,
                                                        Exception caughtException,
                                                        AccioWebContext accioWebContext) {

        long startHandleTime = System.currentTimeMillis();
        try {
            AccioLog accioLog = GenerateLogUtil.createAccioLog(accioWebContext, caughtException, request);
            GenerateLogUtil.setClassName4Spring(accioLog, request, handler);
            /**
             * merger and send log*/

            IMethodAnalyse iMethodAnalyse=GenerateLogUtil.createMethodAnalyse(accioLog,isSendSuccessd());
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);
        } catch (Throwable t) {
            logger.error("Error occurs for handleForSpring3Interceptor.", t);
        } finally {
            if (logger.isDebugEnabled()) {
                long handleCostTime = System.currentTimeMillis() - startHandleTime;
                logger.debug("It takes " + handleCostTime + " milliseconds for handleForSpring3Interceptor. caughtException="
                        + ((null == caughtException) ? "null" : caughtException.getMessage()));
            }
        }
    }


    public void handleForStruts2Interceptor(Exception caughtException, AccioWebContext accioWebContext) {
        long startHandleTime = System.currentTimeMillis();
        try {
            HttpServletRequest httpServletRequest = ServletActionContext.getRequest();
            AccioLog accioLog = GenerateLogUtil.createAccioLog(accioWebContext, caughtException, httpServletRequest);
            GenerateLogUtil.setClassName4Struts2(accioLog);
            /**
             * merger and send log*/

            IMethodAnalyse iMethodAnalyse=GenerateLogUtil.createMethodAnalyse(accioLog,isSendSuccessd());
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);
        } catch (Exception e) {
            logger.error("Error occurs when handle for struts2 interceptor.", e);
        } finally {
            if (logger.isDebugEnabled()) {
                long handleCostTime = System.currentTimeMillis() - startHandleTime;
                logger.debug("It takes " + handleCostTime + " milliseconds for handleForStruts2Interceptor. caughtException="
                        + ((null == caughtException) ? "null" : caughtException.getMessage()));

            }
        }
    }

    public void handleForWebFilter(HttpServletRequest httpServletRequest, Exception caughtException, AccioWebContext accioWebContext) {
        long startHandleTime = System.currentTimeMillis();
        try {
            AccioLog accioLog = GenerateLogUtil.createAccioLog(accioWebContext, caughtException, httpServletRequest);
            /**
             * merger and send log*/

            IMethodAnalyse iMethodAnalyse=GenerateLogUtil.createMethodAnalyse(accioLog,isSendSuccessd());
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);
        } catch (Exception e) {
            logger.error("Error occurs when handle for web filter.", e);
        } finally {
            if (logger.isDebugEnabled()) {
                long handleCostTime = System.currentTimeMillis() - startHandleTime;
                logger.debug("It takes " + handleCostTime + " milliseconds for handleForWebFilter. caughtException="
                        + ((null == caughtException) ? "null" : caughtException.getMessage()));

            }
        }
    }

}
