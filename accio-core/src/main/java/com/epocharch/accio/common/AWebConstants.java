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

import com.epocharch.common.zone.zk.ZkZoneContainer;

/**
 * Created by chenyuyao on 2017/9/19.
 */
public interface AWebConstants {
    public static final String SEPARATOR = "/";
    public static final String BASE_ROOT_IDC = "/SOA";
    public static final String IDC= ZkZoneContainer.getInstance().getIdcContainer().getLocalIdc();
    public static final String PRODUCT_NAME="AccioWeb";
    public static final String ZONE_CODE="zones";
    public static final String ZONE_NAME=ZkZoneContainer.getInstance().getLocalZoneName();
    public static final String ACCIO_MONITOR="monitor";
    public static final String _BASE_PATH=BASE_ROOT_IDC+SEPARATOR+ IDC+SEPARATOR+PRODUCT_NAME;

    public static final String ACCIO_CONFIG_PATH_ZONE = _BASE_PATH +SEPARATOR+ZONE_CODE+SEPARATOR+ZONE_NAME+ SEPARATOR + ACCIO_MONITOR+ SEPARATOR + "ACCIOWEB_CONFIG";
    public static final String ACCIO_CONFIG_PATH_IDC=_BASE_PATH+SEPARATOR+ACCIO_MONITOR+SEPARATOR+"ACCIOWEB_CONFIG";

}
