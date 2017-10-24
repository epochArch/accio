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
package com.epocharch.accio.util.dto;

import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.util.AccioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenyuyao on 2017/9/18.
 */
public class AccioContext implements Serializable {
    private static final long serialVersionUID = 2786907400458670384L;
    private static Logger logger = LoggerFactory.getLogger(AccioContext.class);

    private static Map<String, String> globalKeyList = new ConcurrentHashMap<String, String>();
    private Map<String, Object> globalContext = new ConcurrentHashMap<String, Object>();
    private Map<String, Object> localContext = new ConcurrentHashMap<String, Object>();

    static {
        globalKeyList.put(AccioConstants.ACCIO_TRACE_ID, "");
        globalKeyList.put(AccioConstants.ACCIO_REQUEST_HOP, "");
        globalKeyList.put(AccioConstants.ACCIO_REQID,"");
    }

    private Map<String, Object> getGlobalContext() {
        return globalContext;
    }

    private Map<String, Object> getLocalContext() {
        return localContext;
    }

    public void putValue(String key, Object value) {
        if (key != null && value != null) {
            if (value instanceof String) {
                if (value != null) {
                    String str = (String) value;
                    str = AccioUtil.limitString(str, AccioConstants.VALUE_LENGTH_LIMIT);
                    value = str;
                }
            }
            if (isGlobalKey(key)) {
                getGlobalContext().put(key, value);
            } else {
                getLocalContext().put(key, value);
            }
        }
    }

    public void removeValue(String key) {
        if (key != null) {
            if (isGlobalKey(key)) {
                getGlobalContext().remove(key);
            } else {
                getLocalContext().remove(key);
            }
        }
    }

    public Object getValue(String key, Object defaultObj) {
        Object value = null;
        if (key != null) {
            if (isGlobalKey(key)) {
                value = getGlobalContext().get(key);
            } else {
                value = getLocalContext().get(key);
            }
        }
        value = value == null ? defaultObj : value;
        return value;
    }

    public String getStrValue(String key, String defValue) {
        Object obj = getValue(key, defValue);
        return obj == null ? "" : obj.toString();// obj.toString()
    }

    private boolean isGlobalKey(String key) {
        boolean result = false;
        if (!AccioUtil.isBlankString(key)) {
            result = globalKeyList.containsKey(key);
        }
        return result;
    }

    public void cleanLocalContext() {
        if (getLocalContext() != null) {
            getLocalContext().clear();
        }
    }

    public void cleanGlobalContext() {
        if (getLocalContext() != null) {
            getLocalContext().clear();
        }
        if (getGlobalContext() != null) {
            getGlobalContext().clear();
        }
    }

    public int getHopValue() {
        int i = -1;
        String v = getStrValue(AccioConstants.ACCIO_REQUEST_HOP, "");
        if (AccioUtil.isBlankString(v)) {
            v = 1 + "";
            putValue(AccioConstants.ACCIO_REQUEST_HOP, v);
        }
        try {
            i = Integer.valueOf(v);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            i = 1;
            putValue(AccioConstants.ACCIO_REQUEST_HOP, 1 + "");
        }
        return i;
    }

    public void increaseHopValue() {
        putValue(AccioConstants.ACCIO_REQUEST_HOP, getHopValue() + 1);
    }

    @Override
    public AccioContext clone() {
        AccioContext result = new AccioContext();
        result.globalContext.putAll(this.getGlobalContext());
        result.localContext.putAll(this.getLocalContext());
        return result;
    }
}
