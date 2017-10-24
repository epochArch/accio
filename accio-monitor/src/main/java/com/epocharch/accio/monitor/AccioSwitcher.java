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
package com.epocharch.accio.monitor;

import com.epocharch.accio.monitor.switcher.AccioLogSendSwitcher;
import com.epocharch.accio.util.AccioUtil;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by chenyuyao on 2017/10/24.
 */
public abstract class AccioSwitcher {
    protected static String comma = "，";
    protected static final Map<String, AccioSwitcher> switchers = new Hashtable<String, AccioSwitcher>() {
        private static final long serialVersionUID = 1L;
        {
            regist(this,new AccioLogSendSwitcher());
        }
    };

    /**
     * 注册开关， 返回开关名称
     */
    public abstract String regist();

    /**
     * 开关逻辑
     */
    public abstract Object logic(Map<String, String[]> params);

    /**
     * 开关使用描述
     */
    public abstract String urlDesc();

    /**
     * 开关状态信息
     */
    public abstract Object status();

    public static void regist(Map<String, AccioSwitcher> map, AccioSwitcher accioSwitcher) {
        map.put(accioSwitcher.regist(), accioSwitcher);
    }

    public static AccioSwitcher getAccioSwitcher(String switchName) {
        if (AccioUtil.isBlankString(switchName)) {
            return null;
        }

        return switchers.get(switchName);
    }

    public String getParamter(String key, Map<String, String[]> params) {
        if (params != null) {
            if (params.containsKey(key) && params.get(key) != null) {
                return params.get(key)[0];
            }
        }

        return null;
    }

    public static Collection<AccioSwitcher> init() {
        return switchers.values();
    }
}
