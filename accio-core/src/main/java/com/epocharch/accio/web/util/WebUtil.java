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
package com.epocharch.accio.web.util;

import com.epocharch.accio.util.AccioUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by chenyuyao on 2017/9/20.
 */
public class WebUtil {
    private static final String HOSTNAME_UNKNOWN = "unknown";
    private static final String QUERYSTRING_PREFIX = "?";
    private static final String EQUAL_STRING = "=";
    private static final String LEFT_BRACKET = "[";
    private static final String RIGHT_BRACKET = "]";
    private static final String COMMA_STRING = ",";
    private static final String EMPTY_STRING = "";

    public static String getRequestMethodInfo(HttpServletRequest httpServletRequest){
        String returnValue = null;
        StringBuilder sb = new StringBuilder();
        sb.append("Request method:");
        sb.append(httpServletRequest.getMethod());
        returnValue = sb.toString();
        return returnValue;
    }

    public static String getRequestHeadersInfo(HttpServletRequest httpServletRequest) {
        String returnValue = null;
        Enumeration headerNames = httpServletRequest.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        sb.append("Request Headers:");
        sb.append(LEFT_BRACKET);
        if (null != headerNames) {
            Object headerName = null;
            String headerValue = null;
            while (headerNames.hasMoreElements()) {
                headerName = headerNames.nextElement();
                headerValue = httpServletRequest.getHeader(headerName
                        .toString());
                sb.append(headerName).append(EQUAL_STRING).append(headerValue)
                        .append(headerNames.hasMoreElements() ? COMMA_STRING : EMPTY_STRING);
            }
        }
        sb.append(RIGHT_BRACKET);
        returnValue = sb.toString();
        return returnValue;
    }

    public static String getRequestParamtersInfo(HttpServletRequest httpServletRequest) {
        String returnValue = null;
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        StringBuilder sb = new StringBuilder();
        sb.append("Request Parameters:");
        sb.append(LEFT_BRACKET);
        String parameterName = null;
        String parameterValue = null;
        while (parameterNames.hasMoreElements()) {
            parameterName = parameterNames.nextElement();
            parameterValue = httpServletRequest.getParameter(parameterName);
            sb.append(parameterName).append(EQUAL_STRING).append(parameterValue)
                    .append(parameterNames.hasMoreElements() ? COMMA_STRING : EMPTY_STRING);
        }
        sb.append(RIGHT_BRACKET);
        returnValue = sb.toString();
        return returnValue;
    }
    /**
     * Get the client ip
     *
     * @param httpServletRequest
     * @return
     */
    public static String getClientIp(HttpServletRequest httpServletRequest) {
        String returnValue = null;
        returnValue = httpServletRequest
                .getHeader(ProxyClientIPHttpHeaderNameEnum.XFORWARDEDFOR
                        .getHeaderName());
        if (AccioUtil.isBlankString(returnValue)
                || HOSTNAME_UNKNOWN.equalsIgnoreCase(returnValue)) {
            returnValue = httpServletRequest
                    .getHeader(ProxyClientIPHttpHeaderNameEnum.PROXYCLIENTIP
                            .getHeaderName());
        }
        if (AccioUtil.isBlankString(returnValue)
                || HOSTNAME_UNKNOWN.equalsIgnoreCase(returnValue)) {
            returnValue = httpServletRequest
                    .getHeader(ProxyClientIPHttpHeaderNameEnum.WLPROXYCLIENTIP
                            .getHeaderName());
        }
        if (AccioUtil.isBlankString(returnValue)
                || HOSTNAME_UNKNOWN.equalsIgnoreCase(returnValue)) {
            returnValue = httpServletRequest.getRemoteAddr();
        }
        if (null != returnValue) {
            // 多个路由时，取第一个非unknown的ip
            String[] ips = returnValue.split(COMMA_STRING);
            for (String oneIp : ips) {
                if (!HOSTNAME_UNKNOWN.equals(oneIp)) {
                    returnValue = oneIp;
                    break;
                }
            }
        }
        return returnValue;
    }

    /**
     * Get the url which is "requestURL+?+queryString"
     *
     * @param httpServletRequest
     * @return the requestFullUrl
     */
    public static String getRequestFullUrl(HttpServletRequest httpServletRequest) {
        String returnValue = null;
        StringBuilder sb = new StringBuilder();
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        sb.append(requestURL);
        String queryString = httpServletRequest.getQueryString();
        if (AccioUtil.isBlankString(queryString)) {
            sb.append(QUERYSTRING_PREFIX).append(queryString);
        }
        returnValue = sb.toString();
        return returnValue;
    }
    private enum ProxyClientIPHttpHeaderNameEnum {
        XFORWARDEDFOR("x-forwarded-for"),
        PROXYCLIENTIP("Proxy-Client-IP"),
        WLPROXYCLIENTIP("WL-Proxy-Client-IP");

        private final String headerName;

        String getHeaderName() {
            return headerName;
        }

        private ProxyClientIPHttpHeaderNameEnum(String headerName){
            this.headerName = headerName;
        }

        @Override
        public String toString(){
            return headerName;
        }
    }
}
