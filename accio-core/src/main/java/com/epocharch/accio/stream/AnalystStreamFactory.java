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
package com.epocharch.accio.stream;

import com.epocharch.accio.common.LayerTypeEnum;
import com.epocharch.accio.stream.plugin.AccioAnalysePlugin;
import com.epocharch.accio.stream.plugin.RpcAnalysePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenyuyao on 2017/9/28.
 */
public class AnalystStreamFactory {
    private Map<String,Class> pluginMaps=new ConcurrentHashMap<String, Class>();

    private static AnalystStreamFactory analystStreamFactory=new AnalystStreamFactory();
    private AnalystStreamFactory(){
        regist(LayerTypeEnum.WEB.getCode(),AccioAnalysePlugin.class);
        regist(LayerTypeEnum.SEVER.getCode(),AccioAnalysePlugin.class);
        regist(LayerTypeEnum.ACTION.getCode(),AccioAnalysePlugin.class);
        regist(LayerTypeEnum.MQ.getCode(),AccioAnalysePlugin.class);
        regist(LayerTypeEnum.CACHE.getCode(),AccioAnalysePlugin.class);
        regist(LayerTypeEnum.PRISM.getCode(),AccioAnalysePlugin.class);
        regist(LayerTypeEnum.RPC.getCode(), RpcAnalysePlugin.class);
    }


    public static AnalystStreamFactory getInstance(){
        return analystStreamFactory;
    }

    public void regist(String type,Class analystStream){
        pluginMaps.put(type,analystStream);
    }

    public Class getPulginClass(String type){
        if(pluginMaps.containsKey(type)){
            return pluginMaps.get(type);
        }
        return null;
    }
}
