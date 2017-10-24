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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenyuyao on 2017/9/19.
 */
public class AccioSwitchCache implements Serializable{
    private static final long serialVersionUID = 7049933879142415214L;
    /**
     * the set of accio filter to close
     */
    private Set<String> accioDisableSet = new HashSet<String>();

    /**
     * the set of application accio log of successd don't need to collect
     */
    private Set<String> noToSendSuccessdSet = new HashSet<String>();

    public Set<String> getAccioDisableSet() {
        return accioDisableSet;
    }

    public void setAccioDisableSet(Set<String> accioDisableSet) {
        this.accioDisableSet = accioDisableSet;
    }

    public Set<String> getNoToSendSuccessdSet() {
        //If it is deserialized noToSendSuccessdSet may be null for previous version do not contain this field.
        if(null==noToSendSuccessdSet) {
            noToSendSuccessdSet = new HashSet<String>();
        }
        return noToSendSuccessdSet;
    }

    public void setNoToSendSuccessdSet(Set<String> noToSendSuccessdSet) {
        this.noToSendSuccessdSet = noToSendSuccessdSet;
    }

    @Override
    public String toString(){
        return "accioDisableSet.value:"+accioDisableSet+" noToSendSuccessSet.value:"+noToSendSuccessdSet;
    }

}
