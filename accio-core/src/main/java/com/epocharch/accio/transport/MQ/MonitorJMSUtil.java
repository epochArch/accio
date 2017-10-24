
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
package com.epocharch.accio.transport.MQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by chenyuyao on 2017/9/29.
 */
public class MonitorJMSUtil {

    private static Logger logger= LoggerFactory.getLogger(MonitorJMSUtil.class);
    private static ExecutorService executor;
    private static ScheduledExecutorService scheduledExecutor;
    private static BlockingQueue<Runnable> queueToUse;
    private static Map<String, BatchMessage> batchSendMap = new ConcurrentHashMap<String, BatchMessage>();

    private MonitorJMSUtil(){
        ThreadFactory tf = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "MonitorJMSUtil-scheduledExecutor-thread");
                t.setDaemon(true);
                return t;
            }
        };
        queueToUse = new LinkedBlockingQueue<Runnable>(8);
        executor = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, queueToUse, tf, new ThreadPoolExecutor.DiscardOldestPolicy());

        scheduledExecutor = Executors.newScheduledThreadPool(2);
        scheduledExecutor.scheduleAtFixedRate(getScheduleBizLogTask(), 3000, 3000, TimeUnit.MILLISECONDS);
        /** Add the JVM runtime Hook to ensure that all queued messages are sent before the JVM stops */
        Runtime.getRuntime().addShutdownHook(new Thread(getDistoryTask()));
    }

    private static void asyncSendMessage(final BatchMessage batchMessage) {
        executor.execute(getSendTask(batchMessage));
    }
    /**
     *  Add the JVM runtime Hook to ensure that all queued messages are sent before the JVM stops
     * @return
     */
    private static Runnable getDistoryTask() {
        return new Runnable() {
            public void run() {
                try {
                    if (null != batchSendMap) {

                        for (Map.Entry<String, BatchMessage> entry : batchSendMap.entrySet()) {
                            asyncSendMessage(entry.getValue());
                            entry.getValue().getMessagecounts().set(0);
                        }
                    }
                } catch (Exception e) {
                    if (null != logger) {
                        logger.error("Error occurs for asyncSendMessage for batchSendMap.");
                    }
                } finally {
                    try {
                        MonitorJMSUtil.destroy();
                        if (null != logger) {
                            logger.info("MonitorJmsSendUtil.destroy() was called.");
                        }
                    } catch (Exception e) {
                        if (null != logger) {
                            logger.error("Error occurs for MonitorJmsSendUtil.destroy().", e);
                        }
                    }
                }
            }
        };
    }

    public static void destroy() {
        try {
            if (null != executor) {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            if (null != logger) {
                logger.error("Exception occurs when shuting down the executor:\n{}", e);
            }
        }
        try {
            if (null != scheduledExecutor) {
                scheduledExecutor.shutdown();
                scheduledExecutor.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            if (null != logger) {
                logger.error("Exception occurs when shuting down the scheduledExecutor:\n{}", e);
            }
        }
    //    JumperMessageSender.destroy();
    }
    /**
     * Perform the task of sending operations regularly
     *
     * @return
     */
    private static Runnable getSendTask(final BatchMessage batchMessage) {
        return new Runnable() {
            public void run() {
                try {
                    List<List<Object>> toSend = batchMessage.getBizlogsqueue().getSendLogs();
                    for (List<Object> list : toSend) {
                        if (list.size() > 0) {
                        //    JumperMessageSender.send(list, batchMessage.getQueueName());
                        }
                    }
                } catch (Exception e) {
                    logger.error("batch send message error,queueName is" + batchMessage.getQueueName(), e);
                }
            }
        };
    }

    /**
     * 获取需定时执行Task， 日志发送规则：每次往队列发送不超过defaultCount
     *
     * @return
     */
    private static Runnable getScheduleBizLogTask() {
        return new Runnable() {
            public void run() {
                try {
                    // 批量异步发送业务端日志
                    for (Map.Entry<String, BatchMessage> entry : batchSendMap.entrySet()) {
                        asyncSendMessage(entry.getValue());
                        entry.getValue().getMessagecounts().set(0);
                    }

                } catch (Exception e) {
                    if (null != logger) {
                        logger.error("Error occurs for asyncSendMessage for batchSendMap.");
                    }
                }
            }
        };
    }

    /**
     *
     *
     * @param object
     * @param queueName
     */
    public static void sendMessageAwait(Object object, String queueName) {
        BatchMessage batchMessage = batchSendMap.get(queueName);
        if (batchMessage == null) {
            batchMessage = new BatchMessage(queueName);
            batchSendMap.put(queueName, batchMessage);
        }
        try {
            if (batchMessage.putAndSend(object)) {
                asyncSendMessage(batchMessage);
            }
        } catch (Throwable e) {
            logger.error("sendMessageAwait error, queueName:" + queueName, e);
        }
    }

}
