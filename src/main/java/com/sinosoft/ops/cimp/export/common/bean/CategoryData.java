package com.sinosoft.ops.cimp.export.common.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sinosoft.ops.cimp.util.ParticularUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


/**
 * 获取类别相关的 data
 *
 * @author shixianggui
 * @Date: 20180306
 */
public class CategoryData {

    public static void initCategoryData(Document doc) {
        initDivStyleXYWHMap(doc);
        initAttributeBeanXYWH(doc);
    }

    /************************* categoryBeansPf1 *************************/

    private static List<CategoryBean> categoryBeansPf1;

    private static List<CategoryBean> categoryBeansPf2;

    public static List<CategoryBean> getCategoryBeansPf1() throws ClassNotFoundException, IOException {
        return ParticularUtils.objectCopy(categoryBeansPf1);
    }

    public static List<CategoryBean> getCategoryBeansPf2() throws ClassNotFoundException, IOException {
        return ParticularUtils.objectCopy(categoryBeansPf2);
    }

    static {
        String leftLogicExpression = "#{left}+#{width}";
        String processTypeA = "A";

        // ++++++++++++++ pf1 ++++++++++++++
        categoryBeansPf1 = new ArrayList<>();

        // 113(基本情况) && 14(基本情况)
        List<AttributeBean> attributeBeans11314 = new ArrayList<>();
        attributeBeans11314.add(new AttributeBean("姓名", "_NameId", leftLogicExpression, null, "104", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("民族", "_NationId", leftLogicExpression, null, "104", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("性别", "_SexId", leftLogicExpression, null, "86", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("籍贯", "_NativePlaceId", leftLogicExpression, null, "86", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("参加工作时间", "_WorkTimeId", leftLogicExpression, null, "86", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("出生年月（岁）", "_BirthId", leftLogicExpression, null, "94", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("出生地", "_BirthplaceId", leftLogicExpression, null, "94", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("健康状况", "_HealthConditionId", leftLogicExpression, null, "94", null, processTypeA));
        attributeBeans11314.add(new AttributeBean("熟悉专业有何专长", "_SpecialtyId", leftLogicExpression, null, "180", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(113, 14, attributeBeans11314));

        // 113(基本情况) && 53(政治面貌)
        List<AttributeBean> attributeBeans11353 = new ArrayList<>();
        attributeBeans11353.add(new AttributeBean("入党时间", "_JoinedPartyTimeId", leftLogicExpression, null, "104", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(113, 53, attributeBeans11353));

        // 120(学习教育) && 8(学历)
        List<AttributeBean> attributeBeans1208 = new ArrayList<>();
        attributeBeans1208.add(new AttributeBean("学历学位", "_CollegeUniversityId", leftLogicExpression, null, null, null, "C"));
        attributeBeans1208.add(new AttributeBean("全日制教育", "_FullTimeEducationId", leftLogicExpression, null, "160", null, processTypeA));
        attributeBeans1208.add(new AttributeBean("毕业院校系及专业", "_UniversityDepartmentAndMajorFromFTEId", leftLogicExpression, null, "210", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(120, 8, attributeBeans1208));

        // 120(学习教育) && 95(学位)
        List<AttributeBean> attributeBeans12095 = new ArrayList<>();
        attributeBeans12095.add(new AttributeBean("在职教育", "_InServiceEducationId", leftLogicExpression, null, "160", null, processTypeA));
        attributeBeans12095.add(new AttributeBean("毕业院校系及专业", "_UniversityDepartmentAndMajorFromISEId", leftLogicExpression, null, "210", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(120, 95, attributeBeans12095));

        // 114(任职情况) && 2(职务信息)
        List<AttributeBean> attributeBeans1142 = new ArrayList<>();
        attributeBeans1142.add(new AttributeBean("现任职务", "_PresentOccupationId", leftLogicExpression, null, "460", null, processTypeA));
        attributeBeans1142.add(new AttributeBean("简历", "_ResumeId", leftLogicExpression, null, "556", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(114, 2, attributeBeans1142));

        // 118(专业技术) && 6(专业技术任职资格)
        List<AttributeBean> attributeBeans1186 = new ArrayList<>();
        attributeBeans1186.add(new AttributeBean("专业技术职务", "_TitleOfTechnicalPostId", leftLogicExpression, null, "180", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(118, 6, attributeBeans1186));

        // 139(岗位变动) && 48(拟任拟免职务)
        List<AttributeBean> attributeBeans13948 = new ArrayList<>();
        attributeBeans13948.add(new AttributeBean("拟任职务", "_DesignatedPositionId", leftLogicExpression, null, "460", null, processTypeA));
        attributeBeans13948.add(new AttributeBean("拟免职务", "_ToFreePostId", leftLogicExpression, null, "460", null, processTypeA));
        categoryBeansPf1.add(new CategoryBean(139, 48, attributeBeans13948));


        // ++++++++++++++ pf2 ++++++++++++++
        categoryBeansPf2 = new ArrayList<>();

        // 126(奖惩考核) && 94(奖惩情况)
        List<AttributeBean> attributeBeans12694 = new ArrayList<>();
        attributeBeans12694.add(new AttributeBean("奖惩情况", "_RewardAndPunishmentId", leftLogicExpression, null, "555", null, processTypeA));
        categoryBeansPf2.add(new CategoryBean(126, 94, attributeBeans12694));

        // 不处理
//		attributeBeansTemp2.add(new AttributeBean("任免理由", "_ToAppointOrRemoveReasonId", leftLogicExpression, null, "555", null, processTypeA));

        // 113(基本情况) && 34(家庭成员及社会关系) 家庭主要成员及重要社会关系
        // 20180417--145(基本情况) && 34(家庭成员及社会关系) 家庭主要成员及重要社会关系
        List<AttributeBean> attributeBeans11334 = new ArrayList<>();
        attributeBeans11334.add(new AttributeBean("家庭主要成员及重要社会关系", "_RelationId", leftLogicExpression, null, null, null, "B"));
//		attributeBeans11334.add(new AttributeBean("称谓", "_RelationTitleId", leftLogicExpression, null, null, null, processTypeB));
//		attributeBeans11334.add(new AttributeBean("姓名", "_RelationNameId", leftLogicExpression, null, null, null, processTypeB));
//		attributeBeans11334.add(new AttributeBean("年龄", "_RelationAgeId", null, "#{bottom}-#{height}", null, null, processTypeB));
//		attributeBeans11334.add(new AttributeBean("政治面貌", "_RelationPoliticsStatusId", leftLogicExpression, null, null, null, processTypeB));
//		attributeBeans11334.add(new AttributeBean("工作单位及职务", "_RelationOcupertinoId", leftLogicExpression, null, null, null, processTypeB));
        categoryBeansPf2.add(new CategoryBean(145, 34, attributeBeans11334));


        // 126(奖惩考核) && 15(考核情况)
        List<AttributeBean> attributeBeans12615 = new ArrayList<>();
        attributeBeans12615.add(new AttributeBean("年度考核结果", "_AnnualAssessmentResultsId", leftLogicExpression, "555", null, null, processTypeA));
        categoryBeansPf2.add(new CategoryBean(126, 15, attributeBeans12615));
    }


    /************************* divStyleXYWHMap *************************/

    private static Map<String, String> divStyleXYWHMap;

    public static Map<String, String> getDivStyleXYWHMap() {
        return divStyleXYWHMap;
    }

    private static void initDivStyleXYWHMap(Document doc) {
        if (divStyleXYWHMap == null) {
            synchronized (CategoryData.class) {
                if (divStyleXYWHMap == null) {
                    setDivStyleXYWHMap(doc);
                }
            }
        }
    }

    private static void setDivStyleXYWHMap(Document doc) {
        divStyleXYWHMap = new HashMap<>();

        Element elementHead = doc.head();
        if (elementHead == null) {
            return;
        }

        Elements styleElements = elementHead.select("style");
        if (styleElements == null) {
            return;
        }

        Element styleLastElement = styleElements.get(styleElements.size() - 1);
        if (styleLastElement == null) {
            return;
        }

        String styleLastResult = styleLastElement.toString();
        StringBuffer styleLastBuffer = new StringBuffer("");
        styleLastBuffer.append(styleLastResult.substring(styleLastResult.indexOf("@media screen and ("), styleLastResult.lastIndexOf("@media print{")));

        // 点
        String pointStr = ".";
        int pointIndex = 0;

        // 大括号 {
        String braceLeftStr = "{";
        int braceLeftIndex = 0;

        String braceRightStr = "}";
        int braceRightIndex = 0;

        for (int i = 0; i < 10000 * 10; i++) {
            pointIndex = styleLastBuffer.indexOf(pointStr, pointIndex + 1);
            if (pointIndex < 0) {
                break;
            }

            braceLeftIndex = styleLastBuffer.indexOf(braceLeftStr, pointIndex);
            if (braceLeftIndex < 0) {
                break;
            }
            if ((braceLeftIndex - pointIndex) > 4) {
                continue;
            }

            braceRightIndex = styleLastBuffer.indexOf(braceRightStr, braceLeftIndex);
            if (braceRightIndex < 0) {
                break;
            }
            if (braceRightIndex < braceLeftIndex) {
                continue;
            }
            divStyleXYWHMap.put(styleLastBuffer.substring(pointIndex, braceLeftIndex),
                    styleLastBuffer.substring(braceLeftIndex + 1, braceRightIndex)
                            .replaceAll("left:", "")
                            .replaceAll("bottom:", "")
                            .replaceAll("width:", "")
                            .replaceAll("height:", "")
                            .replaceAll("px", "")
                            .replaceAll(";", "")
                            .replaceAll(" ", ""));
        }
    }


    /*********************** initAttributeBeanXYWH ************************/

    private static boolean isInitAttributeBeanXYWH = false;

    private static void initAttributeBeanXYWH(Document doc) {
        if (!isInitAttributeBeanXYWH) {
            synchronized (CategoryData.class) {
                if (!isInitAttributeBeanXYWH) {
                    initDivStyleXYWHMap(doc);
                    setAttributeBeanXYWH(divStyleXYWHMap, categoryBeansPf1, getPfChildrenById(doc, "pf1"));
                    setAttributeBeanXYWH(divStyleXYWHMap, categoryBeansPf2, getPfChildrenById(doc, "pf2"));
                    isInitAttributeBeanXYWH = true;
                }
            }
        }
    }

    private static void setAttributeBeanXYWH(Map<String, String> divStyleXYWHMap, List<CategoryBean> categoryBeansPf,
                                             Elements pfChildrenElements) {

        if (pfChildrenElements == null) {
            return;
        }

        String divText = null;
        String classValue = null;
        String[] classValueArray = null;
        int mark = 1;
        for (Element divElement : pfChildrenElements) {
            mark = 1;
            divText = disposeDivText(divElement.text());
            if ("".equals(divText)) {
                continue;
            }

            for (CategoryBean categoryBean : categoryBeansPf) {
                for (AttributeBean attributeBean : categoryBean.getAttributeBeans()) {
                    if (attributeBean.getLeft() == null && divText.equals(attributeBean.getName())) {
                        classValue = divElement.className();
                        classValueArray = classValue.split(" ");
                        for (String s : classValueArray) {
                            if (s.contains("x")) {
                                attributeBean.setLeft(divStyleXYWHMap.get("." + s.trim()));
                            } else if (s.contains("y")) {
                                attributeBean.setBottom(divStyleXYWHMap.get("." + s.trim()));
                            } else if (s.contains("w")) {
                                attributeBean.setWidth(divStyleXYWHMap.get("." + s.trim()));
                            } else if (s.contains("h")) {
                                attributeBean.setHeight(divStyleXYWHMap.get("." + s.trim()));
                            }
                        }
                        mark = 0;
                        break;
                    }
                }
                if (mark == 0) {
                    break;
                }
            }
        }
    }

    public static Elements getPfChildrenById(Document doc, String id) {
        Element pf1Element = doc.getElementById(id);
        Node node1 = pf1Element.childNode(0);

        if (!(node1 instanceof Element)) {
            return null;
        }
        return ((Element) node1).children();
    }

    public static String disposeDivText(String divText) {
        return divText == null ? null : (divText.length() > 30 ? "" : divText.replaceAll(" ", ""));
    }
}
