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
package com.epocharch.accio.aspect;

import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.dto.AccioLog;
import com.epocharch.accio.dto.AccioRPCLog;
import com.epocharch.accio.stream.AccioStatisticHandler;
import com.epocharch.accio.stream.IMethodAnalyse;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.web.internal.facade.GenerateLogUtil;
import org.junit.Test;

/**
 * Created by chenyuyao on 2017/10/10.
 */
public class AccioClientTest {

    @Test
    public void getAccioUniqVo(){
        System.out.println(AccioContextUtil.getAccioUniqVo().getTraceId());

    }
    @Test
    public void testMerge(){
        for(int i=0;i<1000;i++) {
            AccioRPCLog accioRPCLog = GenerateLogUtil.createAccioRPCLog(null);
            IMethodAnalyse iMethodAnalyse = GenerateLogUtil.createMethodAnalyse(accioRPCLog, false);
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testCommonMerge(){
        for(int i=0;i<1000;i++) {
            AccioLog accioLog = GenerateLogUtil.createAccioLog(LayerTypeEnum.SEVER.getCode());
            IMethodAnalyse iMethodAnalyse = GenerateLogUtil.createMethodAnalyse(accioLog, false);
            AccioStatisticHandler.getInstance().handle(iMethodAnalyse);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
