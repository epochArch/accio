package com.epocharch.accio.web;/*
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

import com.epocharch.accio.monitor.AccioSwitcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by chenyuyao on 2017/10/24.
 */
@RestController
public class AccioSwitcherServer {

    @RequestMapping("/switch")
    public Object serviceAnalyse(HttpServletRequest request, HttpServletResponse response){
       String type= request.getParameter("switcher");
        Map<String, String[]> params = (Map<String, String[]>) request.getParameterMap();

        Object result=AccioSwitcher.getAccioSwitcher(type).logic(params);

        return result;
    }
    @RequestMapping("/test")
    public Object service(HttpServletRequest request, HttpServletResponse response){
        Map<String, String[]> params = (Map<String, String[]>) request.getParameterMap();
        String result="";
        for(Map.Entry<String,String[]> param:params.entrySet()){
            result=result+"/n  "+param.getKey();
        }
        return result;
    }
}
