/**
 * @Project:      IIMP
 * @Title:          StatisticsSheetDesign.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.entity.sheet;

/**
 * @ClassName:  StatisticsSheetDesign
 * @Description: 统计表表格设计
 * @Author:        Nil
 * @Date:            2017年8月13日 下午3:37:12
 * @Version        1.0.0
 */
public class StatisticsSheetDesign extends SheetDesign {
    private static final long serialVersionUID = 8569680774546131249L;

    public StatisticsSheetDesign(){
		this.type=SheetType.STATISTICS.getValue();
	}
}
