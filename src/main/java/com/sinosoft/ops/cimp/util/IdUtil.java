package com.sinosoft.ops.cimp.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class IdUtil {

    private IdUtil() {
    }

    private static final ConcurrentHashMap<String, AtomicInteger> SEQUENCE_MAP = new ConcurrentHashMap<String, AtomicInteger>();

    public static String uuid() {
        return com.vip.vjtools.vjkit.id.IdUtil.fastUUID().toString();
    }

    public static String uuidWithoutMinus() {
        return com.vip.vjtools.vjkit.id.IdUtil.fastUUID().toString().replaceAll("-", "");
    }

    /**
     * 添加序列
     * 保留目前最大的值
     *
     * @param key   序列名
     * @param value 初始化的序列值
     */
    public static void addSequence(String key, int value) {
        AtomicInteger oldValue = SEQUENCE_MAP.get(key);
        if (oldValue != null) {
            int oldV = oldValue.get();
            if (oldV < value) {
                SEQUENCE_MAP.put(key, new AtomicInteger(value));
            }
        } else {
            SEQUENCE_MAP.put(key, new AtomicInteger(value));
        }
    }

    public static int getValueAdd(String sequence) {
        AtomicInteger atomicInteger = SEQUENCE_MAP.get(sequence);
        if (atomicInteger == null) {
            SEQUENCE_MAP.put(sequence, new AtomicInteger(1));
            return 1;
        }
        return atomicInteger.incrementAndGet();
    }

    public void removeSequence(String seq) {
        SEQUENCE_MAP.remove(seq);
    }

}
