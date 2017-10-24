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

/**
 * Created by chenyuyao on 2017/9/21.
 */
public class AccioWebUtil {

    public static String getMethodNameWithClassName(String className, String methodName) {
        StringBuilder sb = new StringBuilder();
        if(AccioUtil.isBlankString(className)) {
            sb.append(className);
            sb.append(".");
        }
        if(AccioUtil.isBlankString(methodName)) {
            sb.append(methodName);
        }
        return sb.toString();
    }
}
