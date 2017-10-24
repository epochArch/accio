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
package com.epocharch.accio.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenyuyao on 2017/9/28.
 */
public class AccioDateUtil {
    private static Logger logger= LoggerFactory.getLogger(AccioDateUtil.class);

    private static final String DATEFORMAT_MINUTE = "yyyy-MM-dd HH:mm:00";

    /**
     * Gets the format of the date
     * @param date
     * @return date yyyy-MM-dd HH:mm:00
     * */
    public static Date getMinuteDate(Date date) {
        String  minString = getDateAsString(date,DATEFORMAT_MINUTE);
        Date minDate = getMinString(minString);

        return minDate;
    }

    /**
     * Gets the fixed format of the date by string
     * @param minuteString yyyy-MM-dd HH:mm:00
     * @return date yyyy-MM-dd HH:mm:00
     * */
    public static Date getMinString(String minuteString) {
        Date returnValue = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_MINUTE);
            returnValue = sdf.parse(minuteString);
        }catch(Exception e){
            logger.error("Error occurs when parse minuteString="+minuteString+",format="+ DATEFORMAT_MINUTE,e);
        }
        return returnValue;
    }

    private static String getDateAsString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static Date getNextMinuteDate(Date date) {
        Calendar nextMinuteDateAsCalendar = Calendar.getInstance();
        nextMinuteDateAsCalendar.setTime(date);
        nextMinuteDateAsCalendar.add(Calendar.MINUTE, 1);
        Date nextMinuteDate = nextMinuteDateAsCalendar.getTime();
        return nextMinuteDate;
    }
}
