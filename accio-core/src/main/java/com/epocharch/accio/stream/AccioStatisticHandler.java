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

import com.epocharch.accio.common.AccioConstants;
import com.epocharch.accio.stream.statistics.AccioStatisticsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by chenyuyao on 2017/9/27.
 */
public class AccioStatisticHandler {
    private static Logger logger= LoggerFactory.getLogger(AccioStatisticHandler.class);
    private ExecutorService executor;
    private static final int DEFAULT_COREPOOLSIZE = 2;
    private static final int DEFAULT_MAXIMUMPOOLSIZE = 6;
    private static final long DEFAULT_KEEPALIVETIME = 60L;
    private static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;
    private static final int DEFAULT_QUEUE_CAPACITY = 200;
    private static final Lock lockForLifecycle = new ReentrantLock();

    private static AccioStatisticHandler accioStatisticHandler=new AccioStatisticHandler();
    private AccioStatisticHandler(){
        init();
        /**
         *initialze timed task
         * */
        StatisticResultUtil.getInstance();


    }
    public static AccioStatisticHandler getInstance(){
        return accioStatisticHandler;
    }
    /**
     * The log merge handle
     * */
    public void handle(IMethodAnalyse methodAnalyse){
        accioStatisticHandler.submitTask(methodAnalyse);
    }


    private void submitTask(IMethodAnalyse iMethodAnalyse){
        if(null!=executor)
            try {
                executor.submit(getStatictisTask(iMethodAnalyse));
            } catch(RejectedExecutionException rejectedExecutionException) {
                logger.warn("RejectedExecutionException caught when submit task for accio. AccioStatisticHandler={}");
            }
    }
    public static void shutdown(){
        lockForLifecycle.lock();
        try {
            if(null!=accioStatisticHandler) {
                accioStatisticHandler.destroy();
              //  accioStatisticHandler = null;
            }
        } finally {
            lockForLifecycle.unlock();
        }
        logger.info("AccioStatisticHandler is shutdown.");
    }

    private void destroy(){
        if(null!=executor){
            executor.shutdown();
            executor = null;
        }
    }
    private void init(){
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(DEFAULT_QUEUE_CAPACITY);
        executor = new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE,DEFAULT_MAXIMUMPOOLSIZE,DEFAULT_KEEPALIVETIME,DEFAULT_UNIT,queue,new AccioStatisticThreadFactory());
    }
    private static class AccioStatisticThreadFactory implements ThreadFactory {
        final AtomicInteger threadNumber = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, AccioConstants.ACCIO_THREAD_NAME_PREFIX+"AccioStatisticHandler worker thread-"+threadNumber.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }

    private static Runnable getStatictisTask(final IMethodAnalyse iMethodAnalyse) {
        return new Runnable() {
            public void run() {
                try {
                    AccioStatisticsUtil.analyse(iMethodAnalyse);
                } catch (Exception e) {
                    logger.error("Failed to execute the statictis task",e);
                }
            }
        };
    }

}
