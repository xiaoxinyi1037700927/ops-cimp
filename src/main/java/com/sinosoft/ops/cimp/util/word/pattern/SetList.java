package com.sinosoft.ops.cimp.util.word.pattern;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by SML
 * date : 2017/11/6
 * des : 按照List的特性 新增 不重复
 */
public class SetList<T> extends ArrayList<T> {
    private static final long serialVersionUID = 1434324234L;

    @Override
    public boolean add(T object) {
        if (size() > 0) {
            for (T t : this) {
                if (t.equals(object)) {
                    return false;
                }
            }
        }
        return super.add(object);
    }
}