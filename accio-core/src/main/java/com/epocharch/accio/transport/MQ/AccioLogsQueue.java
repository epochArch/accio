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

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Journal queue
 * Created by chenyuyao on 2017/9/29.
 */
public class AccioLogsQueue<T>{
    private static final Logger logger = Logger.getLogger(AccioLogsQueue.class);
    private int defaultCount = 200; ///0;
    private static final int maxCount = 6000;
    private final BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<T>();

    /**
     * Placing elements into a queue
     *
     * @param accioLog
     * @throws InterruptedException
     */
    public void put(T accioLog) throws InterruptedException {
        if (blockingQueue.size() < maxCount) {
            blockingQueue.put(accioLog);
        } else {
            logger.warn(accioLog.getClass().getSimpleName() + "'s BlockingQueue is more than max count,drop current object.");
            System.out.println(accioLog.getClass().getSimpleName() + "'s BlockingQueue is more than max count,drop current object.");
        }
    }

    /**
     * Gets the bulk of the log to be sent
     *
     * @return
     */
    public List<List<T>> getSendLogs() {
        List<T> all = new ArrayList<T>();
        List<List<T>> result = new ArrayList<List<T>>();
        blockingQueue.drainTo(all);
        int size = all.size();
        if (size != 0 && size <= defaultCount) {
            result.add(all);
            return result;
        }

        List<T> curtList = new ArrayList<T>();
        result.add(curtList);
        for (int i = 0; i < size; i++) {
            if (i != 0 && i % defaultCount == 0) {
                curtList = new ArrayList<T>();
                curtList.add(all.get(i));
                result.add(curtList);
            } else {
                curtList.add(all.get(i));
            }
        }
        return result;
    }

    public BlockingQueue<T> getBlockingQueue() {
        return blockingQueue;
    }

    public int getDefaultCount() {
        return defaultCount;
    }

    public void setDefaultCount(int defaultCount) {
        this.defaultCount = defaultCount;

    }
}
