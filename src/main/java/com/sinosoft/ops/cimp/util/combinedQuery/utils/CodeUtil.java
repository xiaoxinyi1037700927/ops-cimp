package com.sinosoft.ops.cimp.util.combinedQuery.utils;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sinosoft.ops.cimp.cache.CacheManager;
import com.sinosoft.ops.cimp.constant.Constants;
import com.sinosoft.ops.cimp.entity.sys.syscode.QSysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.QSysCodeSet;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@SuppressWarnings("unchecked")
@Component
public class CodeUtil {

    private final JPAQueryFactory jpaQueryFactory;

    public CodeUtil(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    /**
     * 判断码值是否正确
     *
     * @param codeSetId
     * @param code
     * @param name
     * @return
     */
    public boolean judgeCode(Integer codeSetId, String code, String name) {
        List<SysCodeItem> codeItems = (List<SysCodeItem>) getCache(codeSetId);
        for (SysCodeItem codeItem : codeItems) {
            if (codeItem.getCode().equalsIgnoreCase(code) && codeItem.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 根据代码集id获取代码项信息
     *
     * @param codeSetId
     * @return
     */
    public List<SysCodeItem> getSysCodeItems(int codeSetId) {
        return (List<SysCodeItem>) getCache(codeSetId);
    }


    /**
     * 根据代码集名称获取id
     *
     * @param name
     * @return
     */
    public Integer getCodeSetIdByName(String name) {
        if (StringUtils.isNotEmpty(name)) {
            List<SysCodeSet> codeSets = (List<SysCodeSet>) getCache(null);
            for (SysCodeSet codeSet : codeSets) {
                if (codeSet.getName().equalsIgnoreCase(name)) {
                    return codeSet.getId();
                }
            }
        }

        return null;
    }


    private Object getCache(Integer codeSetId) {
        String key = "combinedQuery/" + codeSetId;

        Object o = CacheManager.getInstance().get(Constants.SYS_CODE_SET_CACHE, key);

        if (o == null) {
            o = codeSetId == null ? getCodeSets() : getCodeItems(codeSetId);
            CacheManager.getInstance().put(Constants.SYS_CODE_SET_CACHE, key, o);
        }

        return o;
    }


    private List<SysCodeSet> getCodeSets() {
        QSysCodeSet qSysCodeSet = QSysCodeSet.sysCodeSet;
        return jpaQueryFactory.select(qSysCodeSet).from(qSysCodeSet)
                .where(qSysCodeSet.status.eq(0))
                .orderBy(qSysCodeSet.ordinal.asc()).fetch();
    }

    private List<SysCodeItem> getCodeItems(Integer codeSetId) {
        QSysCodeItem qSysCodeItem = QSysCodeItem.sysCodeItem;
        return jpaQueryFactory.select(qSysCodeItem).from(qSysCodeItem)
                .where(qSysCodeItem.codeSetId.eq(codeSetId).and(qSysCodeItem.status.eq(0)))
                .orderBy(qSysCodeItem.ordinal.asc()).fetch();
    }


}
