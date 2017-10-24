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
package com.epocharch.accio.defindAPI.dto;

import java.util.Arrays;

/**
 * Created by chenyuyao on 2017/9/22.
 */
public class AccioDefinedLog {
    private Class targetClass;
    private String targetMethod;
    private Object[] inParam;
    private Object outParam;
    private long costTimeInMilliseconds;
    private Throwable throwable;
    private String memo;

    private AccioDefinedLog() {
        // TODO Auto-generated constructor stub
    }

    /**
     *
     * @param targetClass target class (not null ,throws IllegalArgumentException)
     * @param targetMethod target method name  （not null,throw IllegalArgumentException）
     * @param inParam  method in param
     * @param outParam return value
     * @param costTimeInMilliseconds the time to execute the targetMethod （millisecound）
     * @param throwable throwable thrown by the execution targetMethod
     * @param memo others
     *
     * @throws IllegalArgumentException if <code>theClass</code> is null or <code>methodName</code> is null or empty.
     */
    public AccioDefinedLog(Class targetClass, String targetMethod,
                                       Object[] inParam, Object outParam, long costTimeInMilliseconds,
                                       Throwable throwable, String memo) {
        super();
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.inParam = inParam;
        this.outParam = outParam;
        this.costTimeInMilliseconds = costTimeInMilliseconds;
        this.throwable = throwable;
        this.memo = memo;
        if(null==targetClass) {
            throw new IllegalArgumentException("targetClass should not be null.");
        }
        if(null==targetMethod || "".equals(targetMethod.trim())) {
            throw new IllegalArgumentException("targetMethod should not be null or empty.");
        }
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public Object[] getInParam() {
        return inParam;
    }

    public Object getOutParam() {
        return outParam;
    }

    public long getCostTimeInMilliseconds() {
        return costTimeInMilliseconds;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getMemo() {
        return memo;
    }

    @Override
    public String toString() {
        return "AccioDefinedLog [targetClass=" + targetClass
                + ", targetMethod=" + targetMethod + ", inParam="
                + Arrays.toString(inParam) + ", outParam=" + outParam
                + ", costTimeInMilliseconds=" + costTimeInMilliseconds + ", throwable=" + throwable
                + ", memo=" + memo + "]";
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
