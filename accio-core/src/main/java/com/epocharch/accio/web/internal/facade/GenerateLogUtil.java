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

import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.dto.AccioRPCLog;
import com.epocharch.accio.stream.IMethodAnalyse;
import com.epocharch.accio.stream.dto.AccioCommonWrapper;
import com.epocharch.accio.stream.dto.AccioRPCWrapper;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.AccioUtil;
import com.epocharch.accio.util.KeyUtils;
import com.epocharch.accio.web.internal.dto.AccioWebContext;
import com.epocharch.accio.web.util.AccioWebUtil;
import com.epocharch.accio.web.util.WebUtil;
import com.epocharch.common.util.SystemUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

import static com.epocharch.accio.common.AccioConstants.SUCCESSED_FAIL;
import static com.epocharch.accio.common.AccioConstants.SUCCESSED_SUCCESS;

/**
 * Created by chenyuyao on 2017/9/20.
 */
public class GenerateLogUtil {
    private static Logger logger= LoggerFactory.getLogger(GenerateLogUtil.class);

    private static final String LINESEPARATOR = System.getProperty("line.separator");
    private static final String STRUTS2_ACTION_METHOD_DEFAULT = "execute";

    /**
     * create accioLog by common information
     *
     * @param accioWebContext
     * @param throwable
     * @param httpServletRequest
     */

    public static AccioLog createAccioLog(AccioWebContext accioWebContext,Throwable throwable,HttpServletRequest httpServletRequest){

        AccioLog accioLog = new AccioLog();
        if(null!=accioWebContext) {
            Long localLayer = accioWebContext.getLocalLayer();
            accioLog.setLocalLayer(localLayer);
            Integer requestHop = accioWebContext.getRequestHop();
            accioLog.setCurtLayer(requestHop);

            accioLog.setRequestTime(accioWebContext.getRequetTime());
            accioLog.setResponseTime(accioWebContext.getResponseTime());

            String globalId = accioWebContext.getTraceId();
            accioLog.setUniqId(globalId);
        }
        accioLog.setAppType(LayerTypeEnum.WEB.getCode());
        String appName = AccioContextUtil.getAppName();
        accioLog.setAppName(appName);

        String localhostIp = SystemUtil.getLocalhostIp();
        accioLog.setAppHost(localhostIp);

        String reqId = KeyUtils.generateReqId(appName);
        accioLog.setReqId(reqId);
        if(null!=throwable) {
            // String exceptionClassname = throwable.getClass().getName();
            // Use the simple format

            String exceptionClassname = AccioUtil.getRawClassName(throwable);
            accioLog.setExceptionClassname(exceptionClassname);

            String exceptionDesc = AccioUtil.getFullStackTrace(throwable);
            accioLog.setExceptionDesc(exceptionDesc);
            accioLog.setSuccessed(SUCCESSED_FAIL);
        } else {
            accioLog.setSuccessed(SUCCESSED_SUCCESS);
        }
        String requestIp = WebUtil.getClientIp(httpServletRequest);
        accioLog.addTags("requestIp",requestIp);

        String requestUrl = WebUtil.getRequestFullUrl(httpServletRequest);
        accioLog.addTags("requestIp",requestUrl);

        String requestInfo = getRequestInfo(httpServletRequest);
        accioLog.addTags("requestInfo",requestInfo);
        return accioLog;
    }

    public static AccioLog createAccioLog(String type){
        AccioLog accioLog = new AccioLog();

        Long localLayer =(Long) AccioContextUtil.getAttribute(AccioConstants.ACCIO_LOCALLAYER,0L);
        accioLog.setLocalLayer(localLayer);
        Integer requestHop =(Integer) AccioContextUtil.getAttribute(AccioConstants.ACCIO_REQUEST_HOP,0);
        accioLog.setCurtLayer(requestHop);

        Date requestTime=(Date) AccioContextUtil.getAttribute(AccioConstants.ACCIO_REQUESTTIME,new Date());
        Date responseTime=(Date) AccioContextUtil.getAttribute(AccioConstants.ACCIO_RESPONSETIME,new Date());
        accioLog.setRequestTime(requestTime);
        accioLog.setResponseTime(responseTime);

        String globalId =( String) AccioContextUtil.getAttribute(AccioConstants.ACCIO_TRACE_ID,"");
        accioLog.setUniqId(globalId);

        String parentId=(String)AccioContextUtil.getAttribute(AccioConstants.ACCIO_PARENT_ID,"");
        accioLog.setParentId(parentId);

        String serviceMethod=(String)AccioContextUtil.getAttribute(AccioConstants.serviceMethod,"");
        accioLog.setServiceClassMethod(serviceMethod);

        accioLog.setAppType(type);
        String appName = AccioContextUtil.getAppName();
        accioLog.setAppName(appName);

        String localhostIp = SystemUtil.getLocalhostIp();
        accioLog.setAppHost(localhostIp);

        String reqId = KeyUtils.generateReqId(appName);
        accioLog.setReqId(reqId);
        return accioLog;
    }

    /**
     *create AccioRPCLog by common information
     * */
    public static AccioRPCLog createAccioRPCLog(Throwable throwable){
        AccioRPCLog accioRPCLog=new AccioRPCLog();
        Long localLayer =(Long) AccioContextUtil.getAttribute(AccioConstants.ACCIO_LOCALLAYER,0L);
        Integer requestHop =(Integer) AccioContextUtil.getAttribute(AccioConstants.ACCIO_REQUEST_HOP,0);
        Date requestTime=(Date) AccioContextUtil.getAttribute(AccioConstants.ACCIO_REQUESTTIME,new Date());
        Date responseTime=(Date) AccioContextUtil.getAttribute(AccioConstants.ACCIO_RESPONSETIME,new Date());
        String globalId =( String) AccioContextUtil.getAttribute(AccioConstants.ACCIO_TRACE_ID,"");

        String serviceAppName=(String)AccioContextUtil.getAttribute(AccioConstants.serviceAppName,"unkownApp");
        String serviceMethod=(String)AccioContextUtil.getAttribute(AccioConstants.serviceMethod,"");
        String serviceUrl=(String) AccioContextUtil.getAttribute(AccioConstants.serviceurl,"unknownProviderHost");
        String clientMethod=(String)AccioContextUtil.getAttribute(AccioConstants.clientMethod,"");

        String localhostIp = SystemUtil.getLocalhostIp();
        String appName = AccioContextUtil.getAppName();
        String reqId = (String)AccioContextUtil.getAttribute(AccioConstants.ACCIO_REQID,KeyUtils.generateReqId(appName));
        accioRPCLog.setProviderApp(serviceAppName);
        accioRPCLog.setCallApp(appName);
        accioRPCLog.setMethodName(clientMethod);
        accioRPCLog.setServiceMethodName(serviceMethod);
        accioRPCLog.setCallHost(localhostIp);
        accioRPCLog.setProviderHost(serviceUrl);
        accioRPCLog.setReqId(reqId);
        accioRPCLog.setLocalLayer(localLayer);
        accioRPCLog.setCurtLayer(requestHop);
        accioRPCLog.setUniqId(globalId);
        accioRPCLog.setReqTime(requestTime);
        accioRPCLog.setRespTime(responseTime);

        accioRPCLog.setAppType(LayerTypeEnum.RPC.getCode());
        if(null!=throwable) {
            String exceptionClassname = AccioUtil.getRawClassName(throwable);
            accioRPCLog.setExceptionClassname(exceptionClassname);

            String exceptionDesc = AccioUtil.getFullStackTrace(throwable);
            accioRPCLog.setExceptionDesc(exceptionDesc);
            accioRPCLog.setSuccessed(SUCCESSED_FAIL);

            Object[] inparams=(Object [])(AccioContextUtil.getAttribute(AccioConstants.inParams,null));
            accioRPCLog.setInParamObjects(inparams);
        } else {
            accioRPCLog.setSuccessed(SUCCESSED_SUCCESS);
        }

        return accioRPCLog;
    }

    public static void setClassName4Spring(AccioLog accioLog, HttpServletRequest httpServletRequest, Object handler) {
        String handlerClassName = null;
        String handlerMethodName = null;
        if (null != handler) {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Object bean = handlerMethod.getBean();
                handlerClassName = AccioUtil.getRawClassName(bean);
                Method method = handlerMethod.getMethod();
                String methodName = method.getName();
                handlerMethodName = AccioWebUtil.getMethodNameWithClassName(handlerClassName, methodName);
                accioLog.setServiceClassMethod(handlerMethodName);
            } else {
                handlerClassName = AccioUtil.getRawClassName(handler);
                accioLog.setServiceClassMethod(handlerClassName);
            }
        } else {
            logger.warn("spring3Web's handler is null for requestURI={}", httpServletRequest.getRequestURI());
        }
    }

    public static void setClassName4Struts2(AccioLog accioLog) {
        ActionInvocation actionInvocation = ActionContext.getContext().getActionInvocation();
        String actionClassName = AccioUtil.getRawClassName(actionInvocation.getAction());
        String actionMethodName = getStruts2ActionMethodName();
        String methodNameWithClassName = AccioWebUtil.getMethodNameWithClassName(actionClassName, actionMethodName);
        accioLog.setServiceClassMethod(methodNameWithClassName);
    }

    private static String getStruts2ActionMethodName() {
        String returnValue = null;
        ActionInvocation actionInvocation = ActionContext.getContext().getActionInvocation();
        // Returns the method to execute, or null if no method has been
        // specified (meaning "execute" will be invoked)
        returnValue = actionInvocation.getProxy().getMethod();
        if (AccioUtil.isBlankString(returnValue)) {
            returnValue = STRUTS2_ACTION_METHOD_DEFAULT;
        }
        return returnValue;
    }

    /**
     * Get the request info which include
     * "Request method,request header info,request params info"
     *
     * @param httpServletRequest
     * @return
     */
    private static String getRequestInfo(HttpServletRequest httpServletRequest) {
        String returnValue = null;
        StringBuilder sbForRequestInfo = new StringBuilder();
        sbForRequestInfo.append(WebUtil.getRequestMethodInfo(httpServletRequest));
        sbForRequestInfo.append(LINESEPARATOR);
        sbForRequestInfo.append(WebUtil.getRequestHeadersInfo(httpServletRequest));
        sbForRequestInfo.append(LINESEPARATOR);
        sbForRequestInfo.append(WebUtil.getRequestParamtersInfo(httpServletRequest));
        returnValue = sbForRequestInfo.toString();
        return returnValue;
    }


    public static IMethodAnalyse createMethodAnalyse(AccioLog log,boolean isSendSuccess){
        IMethodAnalyse iMethodAnalyse=new AccioCommonWrapper(
                log.getAppName(),
                log.getServiceClassMethod(),
                log.getAppHost(),
                log.getAppType(),
                log.getSuccessed(),
                log.getCostTime(),
                log.getRequestTime(),
                isSendSuccess,
                log);
     return iMethodAnalyse;
    }

    public static IMethodAnalyse createMethodAnalyse(AccioRPCLog log,boolean isSendSuccess){
        IMethodAnalyse iMethodAnalyse=new AccioRPCWrapper(
                log.getProviderApp(),
                log.getCallApp(),
                log.getMethodName(),
                log.getServiceMethodName(),
                log.getProviderHost(),
                log.getAppType(),
                log.getSuccessed(),
                log.getCostTime(),
                log.getReqTime(),
                isSendSuccess,
                log);
        return iMethodAnalyse;
    }

}
