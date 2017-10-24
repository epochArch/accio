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
package com.epocharch.accio.common;

/**
 * Created by chenyuyao on 2017/9/18.
 */
public interface AccioConstants {
    public static final String ACCIO_TRACE_ID="traceId";

    public static final String ACCIO_REQUEST_ID = "reqId";

    public static final String ACCIO_PARENT_ID = "parentId";


    public static final String ACCIO_REQUEST_HOP = "reqHop";

    public static final String ACCIO_LOCALLAYER = "localLayer";

    public static final String ACCIO_RESPONSETIME = "responsetime";

    public static final String ACCIO_REQUESTTIME = "requesttime";

    public static final String ACCIO_FIRSTONTHECHAIN = "firstOnTheChain";

    public static final String ACCIO_REQID="reqId";

    public static final int VALUE_LENGTH_LIMIT = 150;

    public static final Integer SUCCESSED_FAIL = Integer.valueOf(-1);
    public static final Integer SUCCESSED_SUCCESS = Integer.valueOf(1);

    public static final String EMPTY="";

    public static final String serviceAppName="serverApplicationName";

    public static final String clientMethod="clientMethod";

    public static final String serviceMethod="serviceMethod";

    public static final String serviceurl="serviceurl";

    public static final String inParams="inParams";

    public static final String ACCIO_THREAD_NAME_PREFIX="ACCIO-";

    public static final String DEFAULT_ACCIO_CONFIG="com.epocharch.accio.common.AccioConfig";

    public static final String DEFAULT_TRANSPORT="com.epocharch.accio.transport.http.AccioHttpTransport";
}
