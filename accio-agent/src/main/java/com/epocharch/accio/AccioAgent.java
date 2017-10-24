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

import com.epocharch.accio.transformer.AccioTransformer;
import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;

import java.lang.instrument.Instrumentation;

/**
 * premain agent to hook in the class pre processor
 * run proograms in java 5 by using the -javaagent:pathto/accioagent.jar
 * Created by chenyuyao on 2017/9/15.
 */
public class AccioAgent {
    private static Instrumentation s_instrucmentation;


    public static void premain(String options,Instrumentation ins){
        s_instrucmentation=ins;
        s_instrucmentation.addTransformer(new AccioTransformer());
        s_instrucmentation.addTransformer(new ClassPreProcessorAgentAdapter());

    }

    public static Instrumentation getInstrunctation(){
        if(s_instrucmentation!=null) {
            return s_instrucmentation;
        }else{
            throw new UnsupportedOperationException("Java 5 was not started with perMain -javaagent for AccioAgent");
        }
    }

}
