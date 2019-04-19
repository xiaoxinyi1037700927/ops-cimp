package com.sinosoft.ops.cimp.export;

import com.sinosoft.ops.cimp.export.data.AttrValue;
import com.sinosoft.ops.cimp.export.processor.AttrValueProcessor;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractExportHandler {

    //属性-属性规则
    protected Map<String, AttrValue> attrValueMap = new LinkedHashMap<>(59);

    //属性-解析器
    protected Map<String, AttrValueProcessor> attrValueProcessorMap = new HashMap<>(59);

    //属性值执行上下文
    protected Map<String, Object> attrValueContext = new LinkedHashMap<>();

    //需要添加的字符数
    protected List<Integer> addLines = new ArrayList<>();

    protected static Lock lock = new ReentrantLock();

    public AbstractExportHandler() {
        init();
    }

    protected abstract void init();

    /**
     * 根据EmpId获取所有属性的值
     *
     * @param empId 人员唯一编号
     * @return 属性名和属性值的键值对
     * @throws Exception SQL执行异常
     */
    public Map<String, Object> getAllAttrValue(String empId) throws Exception {

        Map<String, Object> attrValues = new HashMap<>(59);
        //排序
        List<Map.Entry<String, AttrValue>> sortedList = new ArrayList<>(attrValueMap.entrySet());
        Collections.sort(sortedList, Comparator.comparingInt(o -> o.getValue().getOrder()));

        for (Map.Entry<String, AttrValue> entry : sortedList) {
            AttrValue attrRule = entry.getValue();
            Object attrValue = attrRule.getAttrValue(attrValueContext, empId);
            attrValues.put(entry.getKey(), attrValue);
        }
        return attrValues;
    }

    public void generate(String templateFilePath, Map<String, Object> attrValues, String outputFilePath) throws Exception {
        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                processAttrValue(templateFilePath, attrValues, outputFilePath);
            } else {
                throw new Exception("tryLock failed!");
            }
        } finally {
            lock.unlock();
        }
    }

    public abstract void processAttrValue(String templateFilePath, Map<String, Object> attrValues, String outputFilePath) throws Exception;
}
