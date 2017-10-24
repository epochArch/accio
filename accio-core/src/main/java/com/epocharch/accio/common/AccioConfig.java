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
package com.epocharch.accio.common;

import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.common.constants.ClusterUsage;
import com.epocharch.common.zone.zk.ZkZoneContainer;
import com.epocharch.zkclient.IZkDataListener;
import com.epocharch.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by chenyuyao on 2017/9/29.
 */
public class AccioConfig implements IZkDataListener,IAccioConfig {
    private static Logger logger= LoggerFactory.getLogger(AccioConfig.class);

    private boolean aEnabled=false;
    private boolean toSendSuccess=false;

    private String switchPath=null;
    private static ZkClient zkClient;
    public boolean isAEnabled(){
        return aEnabled;
    }
    public boolean isToSendSuccess(){
        return toSendSuccess;
    }
    public AccioConfig(){
        ZkZoneContainer zkZoneContainer= ZkZoneContainer.getInstance();
        zkClient = zkZoneContainer.getLocalZkClient(ClusterUsage.SOA);

    }

    public void init(String switchPath){
        String appName= AccioContextUtil.getAppName();
        try {
            if (!zkClient.exists(switchPath)) {
                zkClient.createPersistent(switchPath, true);
                AccioSwitchCache accioSwitchCache = new AccioSwitchCache();
                zkClient.writeData(switchPath, accioSwitchCache);
            }
            zkClient.subscribeDataChanges(switchPath, this);
            AccioSwitchCache accioSwitchCache = (AccioSwitchCache) zkClient.readData(switchPath);
            boolean aWebEnabled = isAEnabled(accioSwitchCache);
            setAEnabled(aWebEnabled);

            boolean toSendSuccess=isToSendSuccess(accioSwitchCache);
            setToSendSuccess(toSendSuccess);
            if(logger.isInfoEnabled())
                logger.info("AccioConfig initialized. appName="+appName+",aEnabled="+aEnabled+",toSendSuccess="+toSendSuccess);
        }catch (Exception e){
            logger.error("init error for AWebConfig.", e);
        }
    }
    private boolean isAEnabled(AccioSwitchCache accioSwitchCache) {
        boolean isAEnabled = true;
        if(null!= accioSwitchCache){
            Set<String> accioDisableSet = accioSwitchCache.getAccioDisableSet();
            String appname=AccioContextUtil.getAppName();
            if(accioDisableSet.contains(appname)){
                isAEnabled = false;
            }
        }
        return isAEnabled;
    }

    private boolean isToSendSuccess(AccioSwitchCache accioSwitchCache){
        boolean istoSendSuccess = true;
        if(null!= accioSwitchCache){
            Set<String> noNeedToSendSuccessSet = accioSwitchCache.getNoToSendSuccessdSet();
            String appName=AccioContextUtil.getAppName();
            if(noNeedToSendSuccessSet.contains(appName)){
                istoSendSuccess = false;
            }
        }
        return istoSendSuccess;
    }
    private void setAEnabled(boolean aEnabled){
        this.aEnabled=aEnabled;
        this.notifyAll(aEnabled);
    }
    private void notifyAll(boolean aEnabled) {
        if(logger.isInfoEnabled())
            logger.info("notifyAll due to aEnabled="+aEnabled);
    }

    public void handleDataChange(String dataPath, Object data) throws Exception {
        AccioSwitchCache accioSwitchCache =(AccioSwitchCache)data;
        if(logger.isInfoEnabled()){
            logger.info("the path of:["+dataPath+"] handleDataChange,the new AccioSwitchCache:"+ accioSwitchCache.toString());
        }
        boolean aEnabled = isAEnabled(accioSwitchCache);
        setAEnabled(aEnabled);

        boolean toSendSuccess=isToSendSuccess(accioSwitchCache);
        setToSendSuccess(toSendSuccess);
    }

    public void handleDataDeleted(String dataPath) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info(" handleDataDeleted do nothing for datapath:"+dataPath);
        }
    }
    private void setToSendSuccess(boolean toSendSuccess) {
        this.toSendSuccess = toSendSuccess;
    }
}
