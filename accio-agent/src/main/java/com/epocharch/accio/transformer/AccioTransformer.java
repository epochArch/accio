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
package com.epocharch.accio.transformer;

import com.epocharch.accio.config.ConfigLoader;
import com.epocharch.accio.dto.Constant;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenyuyao on 2017/9/15.
 */
public class AccioTransformer implements ClassFileTransformer{
    private static Logger logger= LoggerFactory.getLogger(AccioTransformer.class);

    private static final Set<String> enhandsSet=new HashSet<String>(){
        private static final long serialVersionUID=1L;
        {
            init(this);
        }
    };
    private static String FILE_SEPARATOR="/";
    private static String POINT_SEPARATOR=".";

    public byte[] transform(ClassLoader classLoader,
                            String classname,
                            Class<?> classBeingRedefine,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException{

        if(!enhandsSet.contains(classname)){
            return null;
        }

        if(classname.contains(FILE_SEPARATOR)){
            classname=classname.replaceAll(FILE_SEPARATOR,POINT_SEPARATOR);
        }
        return asmAddField(classname);
    }

    private static byte[] asmAddField(String classname){
        try {
            ClassReader cr = new ClassReader(classname);
            ClassWriter cw=new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
            ClassAdapter ca=new ClassAdapter(cw);
            cr.accept(ca,ClassReader.SKIP_DEBUG);
            cw.visitField(Opcodes.ACC_PUBLIC, Constant.trace_id, Type.getDescriptor(String.class),"",null);
            cw.visitField(Opcodes.ACC_PUBLIC, Constant.request_hop, Type.getDescriptor(String.class),"",null);
            cw.visitField(Opcodes.ACC_PUBLIC, Constant.req_id, Type.getDescriptor(String.class),"",null);
            cw.visitEnd();
            if(logger.isInfoEnabled()){
                logger.info("The class:["+classname+"] visitField is successful");
            }

            byte[] code = cw.toByteArray();
            GeneratorClassLoader classLoader=new GeneratorClassLoader();
            Class<?> exampleClass = classLoader.defineClassFromClassFile(classname, code);

            return code;
        }catch (IOException ioe){
            logger.warn("The global id failed to create.cause:"+ioe.getMessage(),ioe);
            return null;
        }catch (Exception e){
            logger.warn("The global id failed to create.cause",e);
            return null;
        }
    }

    private static void init(Set<String> set){
        String value= ConfigLoader.getInstance().getPropertyByNameSpace(Constant.NAMESPACE_ACCIO,Constant.TRANSPORT_CLASS);
        if(value!=null){
            String[] values=value.split(",");
            if(logger.isInfoEnabled()){
                logger.info("The array of "+Constant.NAMESPACE_ACCIO+" :"+value);
            }
            if(values.length<=1){
                set.add(value);
            }else {
                for (String nameclass : values) {
                    nameclass = nameclass.replaceAll(POINT_SEPARATOR, FILE_SEPARATOR);
                    set.add(nameclass);
                }
            }
        }
    }

 static class GeneratorClassLoader extends ClassLoader {
        @SuppressWarnings("rawtypes")
        public Class defineClassFromClassFile(String className, byte[] classFile) throws Exception {
            return defineClass(className, classFile, 0, classFile.length);
        }
    }
}
