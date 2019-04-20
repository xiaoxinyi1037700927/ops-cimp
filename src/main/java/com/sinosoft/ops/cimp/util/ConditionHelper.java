/**
 * @Project: IIMP
 * @Title: ConditionHelper.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;
import java.util.Set;


/**
 * @ClassName: ConditionHelper
 * @description: 条件帮助类
 * @author: Nil
 * @date: 2017年12月6日 下午11:45:08
 * @version 1.0.0
 * @since JDK 1.7
 */
public class ConditionHelper {
    /**
     * 生成导航条件
     *
     * @param appId
     *            应用标识
     * @param dataSourceId
     *            数据源标识
     * @param includeDescendants
     *            是否包含下级
     * @return 导航条件
     * @author Nil
     * @since JDK 1.7
     */
    public static String buildNavigationCondition(Integer appId, Integer dataSourceId, boolean includeDescendants) {
        if (200 == appId && 1 == dataSourceId) {// 单位基本信息管理  500,13  新单位系统 wft 20180428
            return includeDescendants ? " and t1.TREE_LEVEL_CODE like :code" : " and t1.PPTR=:pptr";
        } else if (500 == appId && 13 == dataSourceId) {// 单位基本信息管理  500,13  新单位系统 wft 20180428
            return includeDescendants ? " and t1.TREE_LEVEL_CODE like :code" : " and t1.TREE_LEVEL_CODE=:pptr";
        } else if (200 == appId && 2 == dataSourceId) {// 干部基本信息管理
            return includeDescendants ? " and t1.A01063 = '1' and t1.emp_id in( " + "select emp_id from EMP_A001 t1 where ( "
                    + "exists ( select 1 from DEP_B001 d where d.status=0 and t1.A001004_A=d.dep_id and d.tree_level_code like :code )) "
                    + "union all " + "select a02.emp_id emp_id from EMP_A02 a02 left join DEP_B001 d on A02001_B=d.dep_id "
                    + "where a02.status=0 and A02055='2' and d.status=0  and d.tree_level_code like :code) "
                    : " and t1.A01063 = '1'  and  t1.emp_id in( "
                    + "select t_t1.emp_id from EMP_A001 t_t1 where t_t1.A001004_A= :connTreeId "
                    + "union all select a02.emp_id emp_id from EMP_A02 a02 "
                    + "where status=0 and A02055='2' and A02001_B = :connTreeId) ";
        } else if (400 == appId && 10 == dataSourceId) {// 干部档案信息
            return includeDescendants ? " and t1.A01063 = '1' and t1.emp_id in( " + "select emp_id from EMP_A001 t1 where ( "
                    + "exists ( select 1 from DEP_B001 d where d.status=0 and t1.A001004_A=d.dep_id and d.tree_level_code like :code )) "
                    + "union all " + "select a02.emp_id emp_id from EMP_A02 a02 left join DEP_B001 d on A02001_B=d.dep_id "
                    + "where A02055='2' and d.status=0  and d.tree_level_code like :code) "
                    : " and t1.A01063 = '1'  and  t1.emp_id in( "
                    + "select t_t1.emp_id from EMP_A001 t_t1 where t_t1.A001004_A= :connTreeId "
                    + "union all select a02.emp_id emp_id from EMP_A02 a02 "
                    + "where status=0 and A02055='2' and A02001_B = :connTreeId) ";
        } else if (100 == appId && 3 == dataSourceId) {// 党组织基本信息管理
            return includeDescendants ? " and t1.TREE_LEVEL_CODE like :code" : " and t1.PPTR=:pptr";
        } else if (100 == appId && 4 == dataSourceId) {// 党员基本信息管理
            return includeDescendants
                    ? " and exists ( select 1 from EMP_C001 c1 where c1.status=0 and t1.emp_id=c1.emp_id and exists ( select 1 from PARTY_D001 p where p.status=0 and  c1.c001001_a=p.party_id and p.tree_level_code like :code ) )"
                    : " and exists ( select 1 from EMP_C001 c1 where c1.status=0 and t1.emp_id=c1.emp_id and c1.c001001_a =:C001001A )";
        } else if (200 == appId && 5 == dataSourceId) {// 离退干部信息管理
            return includeDescendants
                    ? " and exists ( select 1 from DEP_B001 d where d.status=0 and  t1.A001004_A=d.dep_id and d.tree_level_code like :code )"
                    : " and t1.A001004_A=:connTreeId";
        }
        // shixianggui-20180301, 新增 "离退休人员信息"
        else if (200 == appId && 6 == dataSourceId) {
            return includeDescendants
                    ? " and exists ( select 1 from DEP_B001 d where d.status=0 and  t1.A001004_A=d.dep_id and d.tree_level_code like :code )"
                    : " and t1.A001004_A=:connTreeId";
        }
        // shixianggui-20180301, 新增 "调离人员信息"
        else if (200 == appId && 7 == dataSourceId) {
            return includeDescendants
                    ? " and exists ( select 1 from DEP_B001 d where d.status=0 and  t1.A001004_A=d.dep_id and d.tree_level_code like :code )"
                    : " and t1.A001004_A=:connTreeId";
        }
        // shixianggui-20180301, 新增 "已去世人员信息"
        else if (200 == appId && 8 == dataSourceId) {
            return includeDescendants
                    ? " and exists ( select 1 from DEP_B001 d where d.status=0 and  t1.A001004_A=d.dep_id and d.tree_level_code like :code )"
                    : " and t1.A001004_A=:connTreeId";
        }
        // shixianggui-20180301, 新增 "其他人员信息"
        else if (200 == appId && 9 == dataSourceId) {
            return includeDescendants
                    ? " and exists ( select 1 from DEP_B001 d where d.status=0 and  t1.A001004_A=d.dep_id and d.tree_level_code like :code )"
                    : " and t1.A001004_A=:connTreeId";
        } else {
            return "";
        }
    }

    /**
     * 生成分类查询条件
     *
     * @param appId
     *            应用标识
     * @param dataSourceId
     *            数据源标识
     * @param categoryId
     *            分类查询标识
     * @return 分类查询条件
     * @author Nil
     * @since JDK 1.7
     */
    public static String buildCategoryCondition(Integer appId, Integer dataSourceId, String categoryId) {
        /*
         * if (100==appId && 3==dataSourceId) {// 党组织基本信息管理 return
         * buildPartyOrganizationCondition(categoryId); } else if (100==appId &&
         * 4==dataSourceId) {// 党员基本信息管理 return buildPartyMemberCondition(categoryId); }
         * else if (200==appId && 1==dataSourceId) {// 单位基本信息管理 return
         * buildOrganizationCondition(categoryId); } else
         */
        if (200 == appId && 2 == dataSourceId) {// 干部基本信息管理
            return buildCadreCondition(categoryId);
        }
        // shixianggui-20180301, 新增 "离退休人员信息"
        else if (200 == appId && 6 == dataSourceId) {
            return " and t1.A01063 = '2' ";
        }
        // shixianggui-20180301, 新增 "调离人员信息"
        else if (200 == appId && 7 == dataSourceId) {
            return " and t1.A01063 = '3' ";
        }
        // shixianggui-20180301, 新增 "已去世人员信息"
        else if (200 == appId && 8 == dataSourceId) {
            return " and t1.A01063 = '4' ";
        }
        // shixianggui-20180301, 新增 "已去世人员信息"
        else if (200 == appId && 9 == dataSourceId) {
            return " and t1.A01063 = '9' ";
        }
        /*
         * else if (300==appId && 5==dataSourceId) {// 离退干部信息管理 return
         * buildRetireeCondition(categoryId); }
         */
        else {
            return "";
        }
    }

    // 离退干部分类查询
    public static String buildRetireeCondition(String categoryId) {
        StringBuilder sb = new StringBuilder();
        if ("Retiree".equals(categoryId)) {// 离休、退休 无身份限制
        } else if ("Retiree00".equals(categoryId)) {// 全部--离休、退休 干部 活的
            sb.append(" and (t1.A001050>='4' and t1.A001050<='515')");// 个人身份 为 干部
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and (a2.A231005>='1' and a2.A231005<='2') and (a2.A231020 is null or a2.A231020='') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");
        } else if ("Retiree01".equals(categoryId)) {// 离休干部 干部 活的
            sb.append(" and (t1.A001050>='4' and t1.A001050<='515')");// 个人身份
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and a2.A231005='1' and (a2.A231020 is null or a2.A231020='') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");
        } else if ("Retiree02".equals(categoryId)) {// 退休干部 活的
            sb.append(" and (t1.A001050>='4' and t1.A001050<='515')");// 个人身份
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and a2.A231005='2' and (a2.A231020 is null or a2.A231020='') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");
        } else if ("Retiree03".equals(categoryId)) {// 去世干部
            sb.append(" and (t1.A001050>='4' and t1.A001050<='515')");// 个人身份
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and (a2.A231005>='1' and a2.A231005<='2') and a2.A231020 is not null and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");
        } else if ("Retiree04".equals(categoryId)) {// 本年增加
            sb.append(" and (t1.A001050>='4' and t1.A001050<='515')");// 个人身份
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and (a2.A231005>='1' and a2.A231005<='2') and a2.A231010 > to_date((to_char(sysdate,'yyyy')||'-01-01'),'yyyy-mm-dd') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");
        } else if ("Retiree05".equals(categoryId)) {// 本年减少
            sb.append(" and (t1.A001050>='4' and t1.A001050<='515')");// 个人身份
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and (a2.A231005>='1' and a2.A231005<='2') and a2.A231020 > to_date((to_char(sysdate,'yyyy')||'-01-01'),'yyyy-mm-dd') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");
        }
        return sb.toString();
    }

    // 干部分类查询
    public static String buildCadreCondition(String categoryId) {
        StringBuilder sb = new StringBuilder();
        //排除离退休人员
        sb.append(" and t1.A01063 <> '2' ");
        if ("Cadre".equals(categoryId)) {// 默认

        } else if ("Cadre01".equals(categoryId)) {// 县处级以上干部
            sb.append(
                    " and exists ((select emp_id from emp_a05 a5 where a5.status=0 and a5.A05024='1' and t1.emp_id=a5.emp_id and A05002_B in ('131','132','231','232')))");
        } else if ("Cadre02".equals(categoryId)) {// 乡科级干部
            sb.append(
                    " and exists ((select emp_id from emp_a05 a5 where a5.status=0 and a5.A05024='1' and t1.emp_id=a5.emp_id and A05002_B in ('141','142','241','242')))");
        } else if ("Cadre03".equals(categoryId)) {// 科级以下干部
            sb.append(
                    " and exists ((select emp_id from emp_a05 a5 where a5.status=0 and a5.A05024='1' and t1.emp_id=a5.emp_id and A05002_B in ('150','160','199','250','260')))");
        }

        return sb.toString();
    }

    // 党员分类查询
    public static String buildPartyMemberCondition(String categoryId) {
        StringBuilder sb = new StringBuilder();
        if ("Emp003000".equals(categoryId)) {// 全部党员（只显示正式党员和预备党员）
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
            sb.append(
                    " and not exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002005<'2' and c2.C002010=(select max(x.C002010) from EMP_C002 x where x.status=0 and t1.emp_id=x.emp_id) )");// 非
            // 中共减员
        } else if ("Emp003001".equals(categoryId)) {// 正式党员
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and a3.A030001='01' )");// 政治面貌
            sb.append(
                    " and not exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002005<'2' and c2.C002010=(select max(x.C002010) from EMP_C002 x where x.status=0 and t1.emp_id=x.emp_id) )");// 非
            // 中共减员
        } else if ("Emp003002".equals(categoryId)) {// 预备党员
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and a3.A030001='02' )");// 政治面貌
            sb.append(
                    " and not exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002005<'2' and c2.C002010=(select max(x.C002010) from EMP_C002 x where x.status=0 and t1.emp_id=x.emp_id) )");// 非
            // 中共减员
        } else if ("Emp003003".equals(categoryId)) {// 失联党员
            sb.append(
                    " and exists ( select 1 from EMP_C001 c1 where c1.status=0 and c1.C001035 is not null and ( c1.C001040 is null or  c1.C001040=''))");// 失去联系日期和失联结束日期
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
        } else if ("Emp003004".equals(categoryId)) {// 离退休党员
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
            sb.append(
                    " and exists ( select 1 from EMP_A231 a2 where a2.status=0 and t1.emp_id=a2.emp_id and (a2.A231005='1' or a2.A231005='2') and (a2.A231020 is null or a2.A231020='') and a2.A231010=(select max(x.A231010) from EMP_A231 x where x.status=0 and x.emp_id=t1.emp_id) )");// 离退
            // 未
            // 死亡
        } else if ("Emp003005".equals(categoryId)) {// 其他（含去世、出党、停止党籍等）
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
            sb.append(
                    " and exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002005>'2' and c2.C002005<>'207' )");// 中共减员
            // 非组织关系转出（207）
        } else if ("Emp003006".equals(categoryId)) {// 本年内增加党员
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
            sb.append(
                    " and exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002010 > to_date((to_char(sysdate,'yyyy')||'-01-01'),'yyyy-mm-dd') and (c2.C002005>='1' and c2.C002005<'2' ) and c2.C002010=(select max(x.C002010) from EMP_C002 x where x.status=0 and x.emp_id=t1.emp_id) )");// 中共增员
        } else if ("Emp003007".equals(categoryId)) {// 本年内减少党员
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
            sb.append(
                    " and exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002010 > to_date((to_char(sysdate,'yyyy')||'-01-01'),'yyyy-mm-dd') and c2.C002005>='2' and c2.C002010=(select max(x.C002010) from EMP_C002 x where x.status=0 and x.emp_id=t1.emp_id) )");// 中共减员
        } else if ("Emp003008".equals(categoryId)) {// 本年发展党员
            sb.append(
                    " and exists ( select 1 from EMP_A030 a3 where a3.status=0 and t1.emp_id=a3.emp_id and (a3.A030001='01' or a3.A030001='02') )");// 政治面貌
            sb.append(
                    " and exists ( select 1 from EMP_C002 c2 where c2.status=0 and t1.emp_id=c2.emp_id and c2.C002010 > to_date((to_char(sysdate,'yyyy')||'-01-01'),'yyyy-mm-dd') and (c2.C002005>='10101' and c2.C002005<'10102' ) and c2.C002010=(select max(x.C002010) from EMP_C002 x where x.status=0 and x.emp_id=t1.emp_id) )");// 中共增员
        }
        return sb.toString();
    }

    // 单位分类查询
    public static String buildOrganizationCondition(String categoryId) {
        StringBuilder sb = new StringBuilder();
        if ("Dep001001".equals(categoryId)) {// 法人单位
            // add by xusq 20171223
            /*
             * sb.
             * append(" and exists ( select 1 from EMP_A02 a02 where  a02.emp_id = t1.emp_id and EXISTS( SELECT DISTINCT (t1.a01001)"
             * +
             * "	FROM EMP_A02 a2, DEP_B001 db WHERE t1.A001004_A = Db.dep_id AND a2.status = 0 AND t1.emp_id = a2.emp_id"
             * + "	AND Db.tree_level_code :code" + "		) )");
             */
            sb.append(" and (t1.B001110='1' and t1.B001130 is null)");
        } else if ("Dep001002".equals(categoryId)) {// 内设机构
            sb.append(" and (t1.B001110='2' and t1.B001130 is null)");
        } else if ("Dep001003".equals(categoryId)) {// 二级单位
            sb.append(" and (t1.B001110='3' and t1.B001130 is null)");
        } else if ("Dep001004".equals(categoryId)) {// 机构分组
            sb.append(" and (t1.B001110='4' and t1.B001130 is null)");
        } else if ("Dep001005".equals(categoryId)) {// 公有制企业
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   ( t1.B001065>='11' and t1.B001065<='129' ) or ");
            sb.append("   ( t1.B001065>='21' and t1.B001065<='22' ) or ");
            sb.append("   ( t1.B001065>='31' and t1.B001065<='32' ) ");
            sb.append(" ) ");
        } else if ("Dep001006".equals(categoryId)) {// 非公有制企业
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   ( ( t1.B001065>='13' and t1.B001065<'14' ) and t1.B001065<>'134' ) or ");
            sb.append("   ( t1.B001065>='23' and t1.B001065<='29' ) or ");
            sb.append("   ( t1.B001065>='33' and t1.B001065<='39' ) ");
            sb.append(" ) ");
            sb.append(" and t1.B001070<>'L722' ");
            sb.append(" and t1.B001070<>'L7231' ");
        } else if ("Dep001007".equals(categoryId)) {// 乡镇、建制村
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   t1.B001045='62' or ");
            sb.append("   t1.B001045='63' or ");
            sb.append("   t1.B001045='712' or ");
            sb.append("   ( t1.B001045>='72' and t1.B001045<'73' ) ");
            sb.append(" ) ");
        } else if ("Dep001008".equals(categoryId)) {// 街道、社区(居委会)
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   ( t1.B001045>='61' and t1.B001045<'62' ) or ");
            sb.append("   ( t1.B001045>='71' and t1.B001045<'72' ) or ");
            sb.append("     t1.B001045 = '5A5' or ");
            sb.append("   ( t1.B001045>='78' and t1.B001045<'79' ) ");
            sb.append(" ) ");
            sb.append(" and (t1.B001075='82' or t1.B001075='84') ");
        } else if ("Dep001009".equals(categoryId)) {// 街道
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   t1.B001045='611' or ");
            sb.append("   t1.B001045='711' or ");
            sb.append("   t1.B001045='781' ");
            sb.append(" ) ");
            sb.append(" and t1.B001075='82'");
        } else if ("Dep0010010".equals(categoryId)) {// 城市社区(居委会)
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   t1.B001045='711' or ");
            sb.append("   t1.B001045='781' ");
            sb.append(" ) ");
            sb.append(" and t1.B001075='84'");
        } else if ("Dep0010011".equals(categoryId)) {// 团场及乡镇社区(居委会)--1*2*(3+4+5+6)*7
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");// 与隶属单位关系标识 1和3
            sb.append(" and t1.B001015 is not null ");// 组织机构代码或统一社会信用代码 非空
            sb.append(" and ( ");
            sb.append("   t1.B001045='612' or ");// 隶属关系 612
            sb.append("   t1.B001045='712' or ");// 隶属关系 712
            sb.append("   t1.B001045='782' or ");// 隶属关系 782
            sb.append("   t1.B001045='5A5' "); // 隶属关系 5A5
            sb.append(" ) ");
            sb.append(" and t1.B001075='84'");// 单位机构类别 84
        } else if ("Dep0010012".equals(categoryId)) {// 团场
            sb.append(" and (t1.B001110='1' or t1.B001110='3') ");
            sb.append(" and t1.B001015 is not null ");
            sb.append(" and ( ");
            sb.append("   t1.B001045='5A' or ");
            sb.append("   t1.B001045='5A1' ");
            sb.append(" ) ");
        } else if ("Dep0010013".equals(categoryId)) {// 连队
            sb.append(" and t1.B001045 ='5A3'");
            sb.append(" and ( ");
            sb.append("   ( t1.B001065>='11' and t1.B001065<='129' ) or ");
            sb.append("   ( t1.B001065>='21' and t1.B001065<='22' ) or ");
            sb.append("   ( t1.B001065>='31' and t1.B001065<='32' ) ");
            sb.append(" ) ");
            sb.append(" and (t1.B001070>='A' and  t1.B001070<'B')");
            sb.append(" and (t1.B001070>'A05' and  t1.B001070<'A05')");
        }
        return sb.toString();
    }

    // 党组织分类查询条件
    public static String buildPartyOrganizationCondition(String categoryId) {
        String sql = "";
        if ("PartyOrg001".equals(categoryId)) {// 地方党委
            sql = " and (t1.D001010 ='1' and t1.D001040 is null) ";
        } else if ("PartyOrg002".equals(categoryId)) {// 党工委
            sql = " and (t1.D001010 like '2%' and t1.D001040 is null) ";
        } else if ("PartyOrg003".equals(categoryId)) {// 党组
            sql = " and (t1.D001010 like '3%' and t1.D001040 is null) ";
        } else if ("PartyOrg004".equals(categoryId)) {// 党委 411,421,611,811(BT0405)
            sql = " and ((t1.D001010 = '411' or t1.D001010 = '421' or t1.D001010 = '611' or t1.D001010 = '811')  and t1.D001040 is null) ";
        } else if ("PartyOrg005".equals(categoryId)) {// 党总支 412,422,621,812(BT0405)
            sql = " and ((t1.D001010 = '412' or t1.D001010 = '422' or t1.D001010 = '621' or t1.D001010 = '812')  and t1.D001040 is null) ";
        } else if ("PartyOrg006".equals(categoryId)) {// 党支部 413,423,631,813BT0405)
            sql = " and ((t1.D001010 = '413' or t1.D001010 = '423' or t1.D001010 = '631' or t1.D001010 = '813')  and t1.D001040 is null) ";
        } else if ("PartyOrg007".equals(categoryId)) {// 联合党支部 632,814(BT0405)
            sql = " and ((t1.D001010 ='632' or t1.D001010 ='814') and t1.D001040 is null) ";
        }
        return sql;
    }

    /**
     * 获取信息集关联字段
     *
     * @param appId
     * @param dataSourceId
     * @author zhaizf
     * @time 2017年12月12日
     */
    public static String getConnectColumn(Integer appId, Integer dataSourceId, Integer setId) {
        if (100 == appId && 3 == dataSourceId || 90 == setId) {// 党组织基本信息管理
            return "PARTY_ID";
        } else if (100 == appId && 4 == dataSourceId || 14 == setId) {// 党员基本信息管理
            return "EMP_ID";
        } else if (200 == appId && 1 == dataSourceId || 9 == setId) {// 单位基本信息管理
            return "DEP_ID";
        } else if (200 == appId && 2 == dataSourceId) {// 干部基本信息管理
            return "EMP_ID";
        } else if (400 == appId && 10 == dataSourceId) {// 干部基本信息管理
            return "EMP_ID";
        } else if (200 == appId && 5 == dataSourceId) {// 离退干部信息管理
            return "EMP_ID";
        }
        return "NONE";
    }

    /**
     * 根据所给参数获取分类查询的sql条件
     *
     * @param name
     * @param typeArr
     * @return
     */
    public static String getClassificationQueryCondition(String classificationQuery, Map<String, String> classificationMap) {
        Map<String, String> queryMap = JSONObject.parseObject(classificationQuery, new TypeReference<Map<String, String>>() {
        });
        Set<String> keys = queryMap.keySet();
        StringBuffer sql = new StringBuffer();
        sql.append(" ");
        for (String key : keys) {
            sql.append(" and ( ");
            String itemStr = queryMap.get(key);
            Map<String, String> items = JSONObject.parseObject(itemStr, new TypeReference<Map<String, String>>() {
            });
            Set<String> itemKeys = items.keySet();
            boolean inFlag = false;
            for (String itemKey : itemKeys) {
                String itemValue = classificationMap.get(itemKey);
                sql.append(itemValue);
                inFlag = true;
                sql.append(" or ");
            }
            if (inFlag) {
                sql.delete(sql.length() - 3, sql.length() - 1);
            }
            sql.append(" )");
            if (!inFlag) {
                sql.delete(sql.length() - 9, sql.length());
            }
        }
        return sql.toString();
		/*
		
		if (name == null || "".equals(name) || queryItems == null || queryItems.length < 1) {
			return "";
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" and ( ");
		switch (name) {
		case "leader":
			for(String type:queryItems) {
				switch(type) {
				case "gongren":
					sql.append(" t1.A01051 = '24' or ");//工人
					break;
				case "nongmin":
					sql.append(" t1.A01051 = '27' or ");//农民
					break;
				case "xuesheng":
					sql.append(" t1.A01051 = '31' or ");//学生
					break;
				case "gangwuyuan":
					sql.append(" t1.A01051 = '11' or ");//公务员（含参照公务员管理人员）
					break;
				}
			}
			sql.delete(sql.length()-3, sql.length()-1);
			break;
		}
		sql.append(" )");
		return sql.toString();
		*/
    }


    public static String disposeString(Object target) {
        return target == null ? "" : target.toString().trim();
    }

}
