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
package com.epocharch.accio.config;
import com.epocharch.accio.dto.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by chenyuyao on 2017/9/15.
 */
public class ConfigLoader {
    private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private static ConfigLoader configLoader=new ConfigLoader();

    private Map<String,Properties> pMap=new HashMap<String, Properties>();

    public static ConfigLoader getInstance(){
        return configLoader;
    }

    private ConfigLoader(){
        init();
    }
    private void init(){
        String fpath = System.getProperty(Constant.PROPERITIES_PATH_KEY);
        loadConfigProperties(fpath,Constant.NAMESPACE_ACCIO);
    }
    /**
     * Load file config
     * @param filePath
     * @param namespace
     */
    private void loadConfigProperties(String filePath,String namespace) {
        InputStream input = null;
        Properties properties = new Properties();
        try {
            if (filePath != null) {
                filePath=filePath+"/"+Constant.agent_file ;
                File file = new File(filePath);
                if (file.exists()) {
                    input = new FileInputStream(file);
                } else {
                    input = loadFileByClasspath(filePath);
                }
            } else {
                input = loadFileByClasspath(filePath);
            }
            if (input != null) {
                properties.load(input);
                if (!properties.isEmpty()) {
                    pMap.put(namespace,properties);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Load properties file:" + Constant.agent_file + " failed!!!");
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Load properties file:" +  Constant.agent_file + " failed!!!", e);
            System.exit(1);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /*
     * Classpath based resource could be loaded by this way in j2ee environment.
     */
    private InputStream loadFileByClasspath(String filePath) throws IOException {
        InputStream input = null;

        ClassLoader clzLoader = this.getClass().getClassLoader();
        URL url = clzLoader.getSystemResource(filePath);

        if (url != null) {
            input = url.openStream();
        } else {
            input = clzLoader.getSystemResourceAsStream(filePath);
            if (input == null) {
                input = clzLoader.getResourceAsStream(filePath);
            }
        }
        return input;
    }

    public String getPropertyByNameSpace(String nameSpace,String key){
        String value = null;
        Properties nsp = pMap.get(nameSpace);
        if(nsp!=null){
            value = nsp.getProperty(key);
        }
        return value;
    }

}
