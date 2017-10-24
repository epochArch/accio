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

import com.epocharch.accio.common.AWebConstants;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.common.IAccioConfig;
import com.epocharch.accio.common.AccioSwitchCache;
import com.epocharch.common.constants.ClusterUsage;
import com.epocharch.common.zone.zk.ZkZoneContainer;
import com.epocharch.zkclient.IZkDataListener;
import com.epocharch.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by chenyuyao on 2017/9/19.
 */
public class AWebConfig implements IZkDataListener,IAccioConfig {
    private static Logger logger= LoggerFactory.getLogger(AWebConfig.class);
    private volatile boolean aWedEnabled = true;
    private volatile boolean toSendSuccess = true;
    private static ZkClient zkClient;

    final String awebPath= AWebConstants.ACCIO_CONFIG_PATH_ZONE;

    private static AWebConfig awebConfig=new AWebConfig();

    private AWebConfig(){
        ZkZoneContainer zkZoneContainer= ZkZoneContainer.getInstance();
        zkClient = zkZoneContainer.getLocalZkClient(ClusterUsage.SOA);
        awebConfig.init(awebPath);
    }

    public static synchronized AWebConfig getInstance(){
        return awebConfig;
    }

    public static synchronized void destroy(){
        if(null!=awebConfig){
            awebConfig.destry();
            awebConfig = null;
        }
    }

    public void init(String awebPath){
        String appName= AccioContextUtil.getAppName();
        try {
            if (!zkClient.exists(awebPath)) {
                zkClient.createPersistent(awebPath, true);
                AccioSwitchCache accioSwitchCache = new AccioSwitchCache();
                zkClient.writeData(awebPath, accioSwitchCache);
            }
            zkClient.subscribeDataChanges(awebPath, this);
            AccioSwitchCache accioSwitchCache = (AccioSwitchCache) zkClient.readData(awebPath);
            boolean aWebEnabled = isAWebEnabled(accioSwitchCache);
            setaWedEnabled(aWebEnabled);

            boolean toSendSuccess=isToSendSuccess(accioSwitchCache);
            setToSendSuccess(toSendSuccess);
            if(logger.isInfoEnabled())
                logger.info("AWebConfig initialized. appName="+appName+",aWedEnabled="+aWedEnabled+",toSendSuccess="+toSendSuccess);
        }catch (Exception e){
            logger.error("init error for AWebConfig.", e);
        }
    }

    public void handleDataChange(String dataPath, Object data) throws Exception {
        AccioSwitchCache accioSwitchCache =(AccioSwitchCache)data;
        if(logger.isInfoEnabled()){
            logger.info("the path of:["+dataPath+"] handleDataChange,the new AccioWebDto:"+ accioSwitchCache.toString());
        }
        boolean aWebEnabled = isAWebEnabled(accioSwitchCache);
        setaWedEnabled(aWebEnabled);

        boolean toSendSuccess=isToSendSuccess(accioSwitchCache);
        setToSendSuccess(toSendSuccess);
    }

    public void handleDataDeleted(String dataPath) throws Exception {
        if(logger.isInfoEnabled()){
            logger.info(" handleDataDeleted do nothing for datapath:"+dataPath);
        }
    }
    private void destry(){
        try{
            zkClient.unsubscribeDataChanges(awebPath, this);
            if(logger.isInfoEnabled()) {
                String appName = AccioContextUtil.getAppName();
                logger.info("AWebConfig is destroyed. appName=" + appName + ",aWedEnabled=" + aWedEnabled);
            }
        } catch(Exception e) {
            logger.error("destry error for AWebConfig.", e);
        }
    }

    public boolean isAEnabled(){
        return aWedEnabled;
    }
    private boolean isAWebEnabled(AccioSwitchCache accioSwitchCache) {
        boolean isCalEnabled = true;
        if(null!= accioSwitchCache){
            Set<String> awebDisableSet = accioSwitchCache.getAccioDisableSet();
            String appname=AccioContextUtil.getAppName();
            if(awebDisableSet.contains(appname)){
                isCalEnabled = false;
            }
        }
        return isCalEnabled;
    }

    public boolean isToSendSuccess(){
        return toSendSuccess;
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

    private void setaWedEnabled(boolean aWedEnabled){
        this.aWedEnabled=aWedEnabled;
        this.notifyAll(aWedEnabled);
    }

    private void setToSendSuccess(boolean toSendSuccess) {
        this.toSendSuccess = toSendSuccess;
    }

    private void notifyAll(boolean aWedEnabled) {
        if(logger.isInfoEnabled())
            logger.info("notifyAll due to aWedEnabled="+aWedEnabled);
//        CalAnalyzer.notified(aWedEnabled);
//        CalStatisticTargetHandler.notified(aWedEnabled);
//        CalStatisticDataHelper.notified(aWedEnabled);
    }



}
