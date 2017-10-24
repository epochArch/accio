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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Send messages in bulk
 * Created by chenyuyao on 2017/9/29.
 */
public class BatchMessage {

    private String queueName;
    private AccioLogsQueue<Object> accioLogsQueue = new AccioLogsQueue<Object>();
    private AtomicInteger messageCounts = new AtomicInteger(0);

    public BatchMessage(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public AccioLogsQueue<Object> getBizlogsqueue() {
        return accioLogsQueue;
    }

    public AtomicInteger getMessagecounts() {
        return messageCounts;
    }

    public boolean putAndSend(Object object) throws InterruptedException {
        accioLogsQueue.put(object);
        if (messageCounts.incrementAndGet() > accioLogsQueue.getDefaultCount()) {
            messageCounts.set(0);
            return true;
        }
        return false;
    }
}
