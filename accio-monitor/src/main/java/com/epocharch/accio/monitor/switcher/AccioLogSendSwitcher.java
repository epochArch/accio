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
package com.epocharch.accio.monitor.switcher;

import com.alibaba.fastjson.JSONObject;
import com.epocharch.accio.common.ARPCConstants;
import com.epocharch.accio.common.AWebConstants;
import com.epocharch.accio.common.AccioSwitchCache;
import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.monitor.AccioSwitcher;
import com.epocharch.common.constants.ClusterUsage;
import com.epocharch.common.zone.zk.ZkZoneContainer;
import com.epocharch.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by chenyuyao on 2017/10/24.
 */
public class AccioLogSendSwitcher extends AccioSwitcher{
    private Logger logger= LoggerFactory.getLogger(AccioLogSendSwitcher.class);
    private String configPath;
    public static final String name = "logSend";
    private static ZkClient zkClient;

    /*
     * (non-Javadoc)
     *
     * @see com.epocharch.accio.monitor.AccioSwitcher#regist()
     */
    @Override
    public String regist() {
        ZkZoneContainer zkZoneContainer= ZkZoneContainer.getInstance();
        zkClient = zkZoneContainer.getLocalZkClient(ClusterUsage.SOA);
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.epocharch.accio.monitor.AccioSwitcher#logic(java.util.Map)
     */
    @Override
    public Object logic(Map<String, String[]> params) {
        JSONObject json = new JSONObject();

        if (params != null) {
            String logType=getParamter("logType",params);
            if(logType.equals(LayerTypeEnum.WEB.getCode())){
                configPath= AWebConstants.ACCIO_CONFIG_PATH_ZONE;
            }else if(logType.equals(LayerTypeEnum.RPC.getCode())){
                configPath= ARPCConstants.ACCIO_CLIENT_CONFIG_PATH_ZONE;
            }
            String cfg = getParamter("accioEnabled", params);
            StringBuilder stringBuilder=new StringBuilder();
            if (cfg != null) {
                String[] array = cfg.split(",");
                String appCode = array[0];
                boolean calEnabled = Boolean.valueOf(array[1]).booleanValue();

                stringBuilder.append("accioEnabled:"+changeAccioEnabled(appCode, calEnabled));

            }
            String cfg2 = getParamter("needToSendWhenSuccess", params);
            if (cfg2 != null) {
                String[] array = cfg.split(",");
                String appCode = array[0];
                boolean needToSendWhenSuccess = Boolean.valueOf(array[1]).booleanValue();
                stringBuilder.append("changeNeedToSendLogWhenSuccess:"+changeNeedToSendLogWhenSuccess(appCode, needToSendWhenSuccess));
            }
            json.put("result", "success");
            json.put("info", stringBuilder.toString());
            return json;
        }
        json.put("result", "failure");
        json.put("info", "param is null");
        return json;
        // return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.epocharch.accio.monitor.AccioSwitcher#urlDesc()
     */
    @Override
    public String urlDesc() {
        // TODO Auto-generated method stub
        return "switch?switch=logSend&logType=WEB|RPC|&accioEnabled=xxxx,true|false&needToSendWhenSuccess=xxx,true|false";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.epocharch.accio.monitor.AccioSwitcher#status()
     */
    @Override
    public Object status() {
        // TODO Auto-generated method stub
        return "OK";
    }

    private Object changeAccioEnabled(String appCode, boolean accioEnabled) {
        AccioSwitchCache accioSwitchCache = null;
        try {
            logger.info("changeAccioEnabled (" + appCode + "," + accioEnabled + ") ConfigPath:" + configPath);

            boolean isNodeExists = zkClient.exists(configPath);

            if (!isNodeExists) {
                zkClient.createPersistent(configPath, true);
                accioSwitchCache = new AccioSwitchCache();
            } else {
                accioSwitchCache = (AccioSwitchCache) zkClient.readData(configPath);
            }
            if (accioEnabled) {
                accioSwitchCache.getAccioDisableSet().remove(appCode);
            } else {
                accioSwitchCache.getAccioDisableSet().add(appCode);
            }
            if (null != accioSwitchCache) {
                zkClient.writeData(configPath, accioSwitchCache);
            }
        } catch (Exception e) {
            logger.error("Error for changeAccioEnabled. appCode=" + appCode + ",accioEnabled=" + accioEnabled, e);
        }
        return accioSwitchCache.getAccioDisableSet().toString();
    }

    private Object changeNeedToSendLogWhenSuccess(String appCode, boolean needToSendLogWhenSuccess) {
        AccioSwitchCache accioSwitchCache = null;
        try {
            logger.info("changeNeedToSendLogWhenSuccess (" + appCode + "," + needToSendLogWhenSuccess + ") configPath:"
                    + configPath);

            boolean isNodeExists =zkClient.exists(configPath);

            if (!isNodeExists) {
                zkClient.createPersistent(configPath, true);
                accioSwitchCache = new AccioSwitchCache();
            } else {
                accioSwitchCache = (AccioSwitchCache) zkClient.readData(configPath);
            }
            if (needToSendLogWhenSuccess) {
                accioSwitchCache.getNoToSendSuccessdSet().remove(appCode);
            } else {
                accioSwitchCache.getNoToSendSuccessdSet().add(appCode);
            }
            if (null != accioSwitchCache) {
                zkClient.writeData(configPath, accioSwitchCache);
            }
        } catch (Exception e) {
            logger.error("Error for changeNeedToSendLogWhenSuccess. appCode=" + appCode + ",needToSendLogWhenSuccess="
                    + needToSendLogWhenSuccess, e);
        }
        return accioSwitchCache.getNoToSendSuccessdSet().toString();
    }

}
