package com.sinosoft.ops.cimp.export.common;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Created by SML
 * date : 2017/11/8
 * des :
 */
public class IntervalImpl {

    private Interval interval;

    public IntervalImpl(Interval interval) {
        this.interval = interval;
    }

    /**
     * 两边闭合区间
     *
     * @param dateTime
     * @return
     */
    public boolean contains(DateTime dateTime) {
        return dateTime == null || this.interval == null || this.contains(dateTime.getMillis());
    }

    /**
     * 左开右闭
     *
     * @param dateTime
     * @return
     */
    public boolean containsLeftGt(DateTime dateTime) {
        return dateTime == null || this.interval == null || this.containsLeftGt(dateTime.getMillis());
    }

    /*  左闭右开
     * @param dateTime
     * @return
     */
    public boolean containsRightLt(DateTime dateTime) {
        return dateTime == null || this.interval == null || this.containsRightLt(dateTime.getMillis());
    }

    public boolean contains(long var1) {
        long var3 = interval.getStartMillis();
        long var5 = interval.getEndMillis();
        return var1 >= var3 && var1 <= var5;
    }

    public boolean containsRightLt(long var1) {
        long var3 = interval.getStartMillis();
        long var5 = interval.getEndMillis();
        return var1 >= var3 && var1 < var5;
    }

    public boolean containsLeftGt(long var1) {
        long var3 = interval.getStartMillis();
        long var5 = interval.getEndMillis();
        return var1 > var3 && var1 <= var5;
    }

    public long getStartMillis() {
        return interval.getStartMillis();
    }

    public long getEndMillis() {
        return interval.getEndMillis();
    }
}
