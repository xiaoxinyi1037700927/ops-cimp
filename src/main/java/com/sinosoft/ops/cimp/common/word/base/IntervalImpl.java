package com.sinosoft.ops.cimp.common.word.base;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Created by SML
 * date : 2017/11/8
 * des :
 */
public class IntervalImpl {

    Interval interval;

    public IntervalImpl(Interval interval) {
        this.interval = interval;
    }

    public boolean contains(DateTime dateTime) {
        return dateTime == null || this.interval == null ? true : this.contains(dateTime.getMillis());
    }

    public boolean contains(long var1) {
        long var3 = interval.getStartMillis();
        long var5 = interval.getEndMillis();
        return var1 >= var3 && var1 <= var5;
    }
}
