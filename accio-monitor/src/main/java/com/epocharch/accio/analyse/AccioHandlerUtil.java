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
package com.epocharch.accio.analyse;

import com.epocharch.accio.dao.mapper.AccioRPCMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by chenyuyao on 2017/10/12.
 */
public class AccioHandlerUtil {
    private static Logger logger= LoggerFactory.getLogger(AccioHandlerUtil.class);

    @Autowired
    private AccioRPCMapper accioRPCMapper;
    public AccioHandlerUtil(){
    }


}
