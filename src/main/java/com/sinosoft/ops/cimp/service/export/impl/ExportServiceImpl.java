package com.sinosoft.ops.cimp.service.export.impl;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.dao.ExportDao;
import com.sinosoft.ops.cimp.export.common.bean.AttributeBean;
import com.sinosoft.ops.cimp.export.common.bean.CategoryBean;
import com.sinosoft.ops.cimp.export.common.bean.CategoryData;
import com.sinosoft.ops.cimp.service.export.ExportService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ExportServiceImpl implements ExportService {

    private final Logger logger = LoggerFactory.getLogger(ExportServiceImpl.class);

    private final ExportDao exportWordDao;

    private static final List<String> RELATIONSHIP_NAMES = Arrays.asList("本人", "户主", "配偶", "夫", "丈夫", "妻", "妻子", "独生子", "儿子", "长子", "次子", "三子", "四子", "五子", "养子或继子", "女婿", "其他儿子", "独生女", "女儿", "长女", "次女", "三女", "四女", "五女", "养女或继女", "儿媳", "其他女儿", "孙子、孙女或外孙子、外孙女", "孙子", "孙女", "外孙子", "外孙女", "孙媳妇或外孙媳妇", "孙女婿或外孙女婿", "曾孙子或外曾孙子", "曾孙女或外曾孙女", "其他孙子、孙女或外孙子、外孙女", "父母", "父亲", "母亲", "公公", "婆婆", "岳父", "岳母", "继父或养父", "继母或养母", "其他父母关系", "祖父母或外祖父母", "祖父", "祖母", "外祖父", "外祖母", "配偶的祖父母或外祖父母", "曾祖父", "曾祖母", "配偶的曾祖父母或外曾祖父母", "其他祖父母或外祖父母关系", "兄弟姐妹", "兄", "嫂", "弟", "弟媳", "姐姐", "姐夫", "妹妹", "妹夫", "其他兄弟姐妹", "其他", "伯父", "伯母", "叔父", "婶母", "舅父", "舅母", "姨父", "姨母", "姑父", "姑母", "堂兄弟、堂姐妹", "表兄弟、表姐妹", "侄子", "侄女", "外甥", "外甥女", "其他亲属", "保姆", "非亲属");

    @Autowired
    public ExportServiceImpl(ExportDao exportWordDao) {
        this.exportWordDao = exportWordDao;
    }

    @Override
    @Transactional
    public List<Map<String, Object>> findBySQL(String sql) {
        try {
            return exportWordDao.findBySQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> findBySQL(String sql, Object... args) {
        try {
            return exportWordDao.findBySQL(sql, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String analysisHtml(String htmlFilePathName) {
        File input = new File(htmlFilePathName);
        Document doc = null;
        try {
            doc = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null) {
            return "";
        }

        // 禁止格式化文档元素
        doc.outputSettings().prettyPrint(false);
        try {
            analysisPfPage(doc);
            return doc.toString();
        } catch (Exception e) {
            logger.error("pdf2html, 解析html出错: ", e);
        }
        return "";
    }

    /**
     * 解析 pf page
     */
    private void analysisPfPage(Document doc) throws ClassNotFoundException, IOException {

        // 初始化相关子集信息
        CategoryData.initCategoryData(doc);

        // 移除 <script></script>
        removeScriptElement(doc);

        StringBuffer buildStyle = new StringBuffer("");// 构建 style
        StringBuffer buildDiv = new StringBuffer("");// 构建 div

        // pf1
        Elements pfChildrenElements = CategoryData.getPfChildrenById(doc, "pf1");
        setPhotoEvent(pfChildrenElements);
        pfPageX(CategoryData.getCategoryBeansPf1(), pfChildrenElements, buildStyle, buildDiv);
        writeInDiv(pfChildrenElements, buildDiv);//写入 div
        buildDiv.setLength(0);

        // pf2
        pfChildrenElements = CategoryData.getPfChildrenById(doc, "pf2");
        pfPageX(CategoryData.getCategoryBeansPf2(), pfChildrenElements, buildStyle, buildDiv);
        writeInDiv(pfChildrenElements, buildDiv);//写入 div

        // 写入 style
        writeInStyle(doc, buildStyle);
    }

    /**
     * 移除 <script></script>
     */
    private void removeScriptElement(Document doc) {
        Element elementHead = doc.head();
        if (elementHead != null) {
            Elements scriptElements = elementHead.select("script");
            if (scriptElements != null) {
                scriptElements.remove();
            }
        }
    }

    /**
     * 写入 div
     */
    private void writeInDiv(Elements pfChildrenElements, StringBuffer buildDiv) {
        Element lastElement = pfChildrenElements.get(pfChildrenElements.size() - 1);
        if (lastElement != null) {
            lastElement.after(buildDiv.toString());

        }
    }

    /**
     * 写入 style
     */
    private void writeInStyle(Document doc, StringBuffer buildStyle) {
        Element titleNode = doc.getElementsByTag("title").first();
        if (titleNode != null) {
            titleNode.before("<style>" + '\n' + buildStyle.toString() + "</style>");
        }
    }

    /**
     * 设置照片点击事件
     */
    private void setPhotoEvent(Elements pfChildrenElements) {
        if (pfChildrenElements == null) {
            return;
        }
        Element firstElement = pfChildrenElements.first();
        if (firstElement != null) {
            // onclick="alert('Hi')" onmouseover="this.style.backgroundColor='#5eb9f0'" onmouseout="this.style.backgroundColor=''" style="cursor:pointer"
            firstElement.attr("onclick", "titleClickFn('EmpA001')");
//			firstElement.attr("onmouseover", "this.style.backgroundColor='#5eb9f0'");//#F4F9FD, #FFA500, #5eb9f0
//			firstElement.attr("onmouseout", "this.style.backgroundColor=''");
            firstElement.attr("style", "cursor:pointer");
        }
    }

    /**
     * 处理 pf page
     */
    private void pfPageX(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, StringBuffer buildStyle, StringBuffer buildDiv) {
        // 设置 nameIndex
        List<Integer> nameIndexs = new ArrayList<>();
        setNameIndex(categoryBeansPf, pfChildrenElements, nameIndexs);

        // 设置 nameIndex 对应的 valueIndex
        setValueIndex(categoryBeansPf, pfChildrenElements, nameIndexs);

        // 为 valueIndex 添加事件
        List<AttributeBean> divEventAttributeBeans = new ArrayList<>();
        setDivEvent(categoryBeansPf, pfChildrenElements, buildDiv, divEventAttributeBeans);

        setStyle(buildStyle, divEventAttributeBeans);
    }

    private void setNameIndex(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, List<Integer> nameIndexs) {
        String divText = null;
        for (Element divElement : pfChildrenElements) {
            divText = CategoryData.disposeDivText(divElement.text());
            if ("".equals(divText)) {
                continue;
            }

            //处理生成html时，"年度考核结果"顺序错乱的问题
            if (divText.length() == 6 && StringUtils.containsAny(divText, "年", "度", "考", "核", "结", "果")) {
                divText = "年度考核结果";
            }

            if (isContainsDivText(categoryBeansPf, divText, divElement.elementSiblingIndex())) {
                nameIndexs.add(divElement.elementSiblingIndex());
            }
        }
    }


    private void setValueIndex(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, List<Integer> nameIndexs) {
        int pfChildrenElementsSize = pfChildrenElements.size();
        for (CategoryBean categoryBean : categoryBeansPf) {
            for (AttributeBean attributeBean : categoryBean.getAttributeBeans()) {
                if (attributeBean.getNameIndex() != -1
                        && "A".equals(attributeBean.getProcessType())
                        && (attributeBean.getNameIndex() + 1 < pfChildrenElementsSize)
                        && !nameIndexs.contains(attributeBean.getNameIndex() + 1)) {
                    attributeBean.setValueIndex(attributeBean.getNameIndex() + 1);
                }
            }
        }
    }

    private void setDivEvent(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, StringBuffer buildDiv, List<AttributeBean> divEventAttributeBeans) {
        Element valueElement = null;
        for (CategoryBean categoryBean : categoryBeansPf) {
            String sysTableNameEn = categoryBean.getSysTableNameEn();
            if (StringUtils.equals(sysTableNameEn, "EmpA36")) {
                List<Element> addATagElements = Lists.newArrayList();

                List<Element> x18Element = pfChildrenElements.stream().filter(e -> RELATIONSHIP_NAMES.contains(e.text())).collect(Collectors.toList());
                addATagElements.addAll(x18Element);

                String[] className = new String[4];
                if (x18Element.size() > 0) {
                    for (int i = 0; i < x18Element.size(); i++) {
                        Element element = x18Element.get(i);
                        //获取第三个className的值，因为它表示纵坐标
                        className[i] = (String) element.classNames().toArray()[2];
                    }
                }

                for (String s : className) {
                    List<Element> yElements = pfChildrenElements.stream().filter(e -> e.classNames().contains(s)).collect(Collectors.toList());
                    addATagElements.addAll(yElements);
                }
                for (Element element : addATagElements) {

                    element.attr("onclick", "titleClickFn('EmpA36')");
//					element.attr("onclick", "alert('" + categoryBean.getParentId() + " - " + categoryBean.getCurrentId() + "')");
                    element.attr("onmouseover", "this.style.backgroundColor='#5eb9f0'");//#F4F9FD, #FFA500, #5eb9f0
                    element.attr("onmouseout", "this.style.backgroundColor=''");
                    element.attr("style", "cursor:pointer");
                }
                continue;
            }
            for (AttributeBean attributeBean : categoryBean.getAttributeBeans()) {
                if (attributeBean.getNameIndex() == -1) {
                    continue;
                }
                if (attributeBean.getValueIndex() == -1) {
                    if ("A".equals(attributeBean.getProcessType())) {
                        buildDiv.append("<div id=\"" + attributeBean.getDivId() + "\" onclick=\"titleClickFn('" + categoryBean.getSysTableNameEn() + "')\" onmouseover=\"this.style.backgroundColor='#5eb9f0'\" onmouseout=\"this.style.backgroundColor=''\" style=\"cursor:pointer\"></div>");
                        divEventAttributeBeans.add(attributeBean);
                        continue;
                    } else if ("B".equals(attributeBean.getProcessType())) {
                        valueElement = pfChildrenElements.get(attributeBean.getNameIndex());
                    } else {
                        continue;
                    }
                } else {
                    valueElement = pfChildrenElements.get(attributeBean.getValueIndex());
                }
                if (valueElement != null) {
                    valueElement.attr("onclick", "titleClickFn('" + categoryBean.getSysTableNameEn() + "')");
//					valueElement.attr("onclick", "alert('" + categoryBean.getParentId() + " - " + categoryBean.getCurrentId() + "')");
                    valueElement.attr("onmouseover", "this.style.backgroundColor='#5eb9f0'");//#F4F9FD, #FFA500, #5eb9f0
                    valueElement.attr("onmouseout", "this.style.backgroundColor=''");
                    valueElement.attr("style", "cursor:pointer");
                }
            }
        }
    }

    private void setStyle(StringBuffer buildStyle, List<AttributeBean> valueIndexNegativeBeans) {
        for (AttributeBean attributeBean : valueIndexNegativeBeans) {
            if (attributeBean.getNameIndex() != -1) {
                buildStyle.append("#");
                buildStyle.append(attributeBean.getDivId());
                buildStyle.append("{");
                buildStyle.append(attributeBean.getDivPosition());
                buildStyle.append(attributeBean.getDivExpression("left"));
                buildStyle.append(attributeBean.getDivExpression("bottom"));
                buildStyle.append(attributeBean.getDivExpression("width"));
                buildStyle.append(attributeBean.getDivExpression("height"));
                buildStyle.append("}");
                buildStyle.append('\n');
            }
        }
    }

    private boolean isContainsDivText(List<CategoryBean> categoryBeansPf, String divText, int attributeNameIndex) {
        for (CategoryBean categoryBean : categoryBeansPf) {
            for (AttributeBean attributeBean : categoryBean.getAttributeBeans()) {
                if (attributeBean.getNameIndex() == -1 && divText.equals(attributeBean.getName())) {
                    attributeBean.setNameIndex(attributeNameIndex);
                    return true;
                }
            }
        }
        return false;
    }


}
