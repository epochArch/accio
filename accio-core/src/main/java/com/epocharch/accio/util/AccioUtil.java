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
package com.epocharch.accio.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyuyao on 2017/9/18.
 */
public class AccioUtil {
    private static Logger logger= LoggerFactory.getLogger(AccioUtil.class);
    public static boolean isBlankString(String value) {
        return value == null || "".equals(value.trim());
    }
    public static String limitString(String value, int limit) {
        if (value != null && limit > 0 && value.length() > limit) {
            value = value.substring(0, limit);
        }
        return value;
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getObjectAsString(Object object) {
        String returnValue = null;
        if(null!=object) {
            try{
                returnValue = JSONObject.toJSONString(object);
            } catch(JSONException jsonException) {
                logger.warn("JSONException occurs for JSONObject.toJSONString(). So use instance of that simple class name instead. object's class="+object.getClass());
                String actionClassName = getRawClassName(object);
                returnValue = "instance of "+actionClassName;
            }
        }
        return returnValue;
    }

    public static String getRawClassName(Object object) {
        if(object != null) {
            if(!AopUtils.isAopProxy(object)) {
                if(AopUtils.isCglibProxy(object)) {
                    return AopUtils.getTargetClass(object).getSimpleName();
                }

                return object.getClass().getSimpleName();
            }

            String v = AopUtils.getTargetClass(object).getSimpleName();
            if(!v.contains("$Proxy") && !v.contains("$$")) {
                return v;
            }

            try {
                Object e = ((Advised)object).getTargetSource().getTarget();
                return getRawClassName(e);
            } catch (Exception var3) {
                ;
            }
        }

        return "UnknowClass";
    }

    public static String getFullStackTrace(Throwable throwable) {
        StringWriter out = new StringWriter();
        try {
            throwable.printStackTrace(new PrintWriter(out));
            String errorInfo = out.toString();
            return errorInfo;
        } catch (Exception e2) {
            logger.error("getFullStackTrace failure",e2);
        } finally {
            try {
                out.close();
            }catch (IOException e){
                logger.error("");
            }
        }
        return null;
    }
    public static String getArrayAsString(Object[] inParam) {
        String returnValue = null;
        if (inParam != null && inParam.length != 0) {
            StringBuilder sb = new StringBuilder();
            Map<String, Object> paramMap = new HashMap<String, Object>();
            int argsLength = inParam.length;
            for (int i = 0; i < argsLength; i++) {
                if (null != inParam[i]) {
                    paramMap.put("param[" + i + "]", getObjectAsString(inParam[i]));
                } else {
                    paramMap.put("param[" + i + "]", "null");
                }
            }
            String argsAsString = getObjectAsString(paramMap);
            sb.append(argsAsString);
            returnValue = sb.toString();
        }
        return returnValue;
    }



}
