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


import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chenyuyao on 2017/9/18.
 */
public class KeyUtils {
    private static final String unknown = "unknown";

    private static final AtomicLong curtValue = new AtomicLong(initValue());
    private static final int threshold = 10000000;
    private static final int partions = 10;
    private static final String separator = "@";
    private static final int maxLength = 18;
    private static final Map<String, String> appCodes = new ConcurrentHashMap<String, String>();
    private static final String dateFmt = "ddHHmm";

    private static long initValue() {
        long v1 = System.currentTimeMillis();
        long v2 = (v1 / (1000 * 60)) * (1000 * 60);
        return v1 - v2;
    }

    public static String getTraceId(String appName) {
        return generateReqId(appName);
    }

    /**
     * basic on appName generate reqid
     *
     * @param appName
     * @return
     */
    public static String generateReqId(String appName) {
        StringBuilder sBuilder = new StringBuilder();
        long curt = curtValue.incrementAndGet();
        if (curt >= threshold) {
            curtValue.set(0);
        }
        sBuilder.append(curt % partions).append(separator);
        /** 其他所有情况均为参数异常，统一按下列方式默认处理 **/

        Date reqTime = new Date();

        String innerAppCode = appCodes.get(appName);
        if (AccioUtil.isBlankString(innerAppCode) && !AccioUtil.isBlankString(appName)) {
            innerAppCode=appName;
            if (innerAppCode.length() > maxLength) {
                innerAppCode = innerAppCode.substring(0, maxLength);
            }
            appCodes.put(appName, innerAppCode);
        }
        if (AccioUtil.isBlankString(innerAppCode)) {
            innerAppCode = unknown;
        }
        sBuilder.append(formatDateTime(reqTime)).append(separator)
                .append(innerAppCode).append(separator).append(curt)
                .append(separator).append(getJvmPidHashCode()).toString();
        return sBuilder.toString();
    }

    private static String formatDateTime(Date date) {
        if (date == null) {
            date = new Date();
        }
        return AccioUtil.getFormatDateTime(date, dateFmt);
    }
    static Integer jvmPidHashcode = null;

    public static Integer getJvmPidHashCode() {
        if (jvmPidHashcode == null) {
            try {
                jvmPidHashcode = ManagementFactory.getRuntimeMXBean().getName().hashCode();
            } catch (Exception e) {
                jvmPidHashcode = (int) (Math.random() * (100000 + 1) - 1);
            }
        }
        return jvmPidHashcode;
    }

    public static String getSeparator() {
        return separator;
    }
}
