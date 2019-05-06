package com.sinosoft.ops.cimp.export.handlers;

import com.sinosoft.ops.cimp.export.data.AttrValue;
import com.sinosoft.ops.cimp.export.processor.AttrValueProcessor;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractExportWithAsposeWords implements ExportHandler {

    //属性-属性规则
    protected Map<String, AttrValue> attrValueMap = new LinkedHashMap<>(59);

    //属性-解析器
    protected Map<String, AttrValueProcessor> attrValueProcessorMap = new HashMap<>(59);

    //属性值执行上下文
    protected Map<String, Object> attrValueContext = new LinkedHashMap<>();

    //需要添加的字符数
    protected List<Integer> addLines = new ArrayList<>();

    private static Lock lock = new ReentrantLock();

    //干部id
    protected String empId;

    /**
     * 获取生成文件所需数据
     */
    public Map<String, Object> getData() {
        Map<String, Object> attrValues = new HashMap<>(59);
        //排序
        List<Map.Entry<String, AttrValue>> sortedList = new ArrayList<>(attrValueMap.entrySet());
        sortedList.sort(Comparator.comparingInt(o -> o.getValue().getOrder()));

        try {
            for (Map.Entry<String, AttrValue> entry : sortedList) {
                AttrValue attrRule = entry.getValue();
                Object attrValue = attrRule.getAttrValue(attrValueContext, empId);
                attrValues.put(entry.getKey(), attrValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attrValues;
    }

    @Override
    public boolean generate() throws Exception {
        //获取数据
        Map<String, Object> data = getData();

        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                processAttrValue(data);
            } else {
                throw new Exception("tryLock failed!");
            }
        } finally {
            lock.unlock();
        }

        processFile();

        return true;
    }

    protected abstract void processAttrValue(Map<String, Object> attrValues) throws Exception;

    /**
     * 生成文件后对文件进行处理
     */
    protected abstract void processFile() throws Exception;

}
