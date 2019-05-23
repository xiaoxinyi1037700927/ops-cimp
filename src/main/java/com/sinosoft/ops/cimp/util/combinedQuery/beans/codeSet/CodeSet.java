package com.sinosoft.ops.cimp.util.combinedQuery.beans.codeSet;

import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CodeSet {

    public CodeSet(String codeSetName, List<SysCodeItem> sysCodeItems) {
        items = initItems(codeSetName, sysCodeItems);
    }

    private List<CodeItem> items;

    private List<String> allCodes = new ArrayList<>();

    private int total = 0;

    public List<CodeItem> getItems() {
        return items;
    }

    private List<CodeItem> initItems(String parentCode, List<SysCodeItem> sysCodeItems) {
        List<CodeItem> items = new ArrayList<>();
        if (total >= sysCodeItems.size()) {
            return items;
        }

        String currentCode = "";
        while (total < sysCodeItems.size()) {
            SysCodeItem sysCodeItem = sysCodeItems.get(total);

            if (sysCodeItem.getParentCode() == null || sysCodeItem.getParentCode().equals(parentCode)) {
                CodeItem item = new CodeItem(sysCodeItem.getCode(), sysCodeItem.getName());
                items.add(item);
                allCodes.add(item.getCode());
                currentCode = item.getCode();
                total++;
            } else if (sysCodeItem.getParentCode().equals(currentCode)) {
                items.get(items.size() - 1).setChildren(initItems(currentCode, sysCodeItems));
            } else {
                break;
            }
        }

        return items;
    }

    private List<String> getCodes(CodeItem item) {
        List<String> items = new ArrayList<>();
        items.add(item.getCode());

        for (CodeItem child : item.getChildren()) {
            items.addAll(getCodes(child));
        }
        return items;
    }

    public boolean getCodesByEq(String code, List<CodeItem> items, List<String> result) {
        for (CodeItem item : items) {
            if (item.getCode().equals(code)) {
                result.addAll(getCodes(item));
                return true;
            }

            //递归判断子节点
            if (getCodesByEq(code, item.getChildren(), result)) {
                return true;
            }
        }
        return false;
    }

    public void getCodesByNq(String code, List<String> result) {
        //获取等于code的集合
        List<String> eqCodes = new ArrayList<>();
        getCodesByEq(code, items, eqCodes);

        result.addAll(allCodes);
        result.removeAll(eqCodes);
    }

    public boolean getCodesByGt(String code, List<CodeItem> items, List<String> result) {
        for (CodeItem item : items) {
            if (item.getCode().equals(code)) {
                return true;
            }

            result.add(item.getCode());

            if (getCodesByGt(code, item.getChildren(), result)) {
                return true;
            }
        }
        return false;
    }

    public void getCodesByGe(String code, List<String> result) {
        getCodesByGt(code, items, result);
        getCodesByEq(code, items, result);
    }


    public void getCodesByLt(String code, List<String> result) {
        //获取大于等于code的集合
        List<String> geCodes = new ArrayList<>();
        getCodesByGe(code, geCodes);

        result.addAll(allCodes);
        result.removeAll(geCodes);
    }


    public void getCodesByLe(String code, List<String> result) {
        //获取大于code的集合
        List<String> gtCodes = new ArrayList<>();
        getCodesByGt(code, items, gtCodes);

        result.addAll(allCodes);
        result.removeAll(gtCodes);
    }

    public void getCodesByIn(List<String> codes, List<String> result) {
        //获取每个code对应的集合
        for (String code : codes) {
            getCodesByEq(code, items, result);
        }

        //去重
        Set<String> set = new HashSet<>(result.size());
        set.addAll(result);
        result.clear();
        result.addAll(set);
    }

    public void getCodesByNotIn(List<String> codes, List<String> result) {
        //获取所有codes对应的集合
        List<String> inCodes = new ArrayList<>();
        getCodesByIn(codes, inCodes);

        result.addAll(allCodes);
        result.removeAll(inCodes);
    }

    public void getCodesByBetweenAnd(String begin, String end, List<String> result) {
        //获取大于等于begin的集合
        getCodesByGe(begin, result);

        //获取小于等于end的集合
        List<String> leCodes = new ArrayList<>();
        getCodesByLe(end, leCodes);

        //取并集
        result.retainAll(leCodes);
    }


}
