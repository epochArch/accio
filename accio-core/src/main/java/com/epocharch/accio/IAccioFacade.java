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
package com.epocharch.accio;

import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.util.AccioContextUtil;
import com.epocharch.accio.util.KeyUtils;
import com.epocharch.accio.util.dto.AccioContext;
import com.epocharch.accio.util.dto.AccioUniqIdVo;
import com.epocharch.accio.web.internal.dto.AccioWebContext;

import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/22.
 */
public abstract class IAccioFacade {
    AccioUniqIdVo accioUniqIdVo=null;
    public void initAndGetAccioContext(){
        AccioContextUtil.setAttribute(AccioConstants.ACCIO_REQUESTTIME,new Date());
        accioUniqIdVo = AccioContextUtil.getAccioUniqVo();
        if(null!=accioUniqIdVo) {
            Long localLayer = this.getLocalLayer();
            String globalId = accioUniqIdVo.getTraceId();
            Integer requestHop = Integer.valueOf(AccioContextUtil.getRequestHop());
            boolean theFirstOnTheChain = accioUniqIdVo.isNewCreated();
            AccioContextUtil.setAttribute(AccioConstants.ACCIO_TRACE_ID,globalId);
            AccioContextUtil.setAttribute(AccioConstants.ACCIO_REQUEST_HOP,requestHop);
            AccioContextUtil.setAttribute(AccioConstants.ACCIO_LOCALLAYER,localLayer);
            AccioContextUtil.setAttribute(AccioConstants.ACCIO_FIRSTONTHECHAIN,theFirstOnTheChain);
            String appName = AccioContextUtil.getAppName();
           AccioContextUtil.setAttribute(AccioConstants.ACCIO_REQID, KeyUtils.generateReqId(appName));
        }
    }


    public abstract void handle(Object object);

     Long getLocalLayer() {
        Long localLayer = Long.valueOf(System.nanoTime());
        return localLayer;
    }
    public void cleanAccioContext(){
        if(accioUniqIdVo!=null)
        AccioContextUtil.cleanGlobal(accioUniqIdVo);
    }

    public boolean isAccioEnable(){return false;}

}
