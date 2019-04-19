package com.sinosoft.ops.cimp.service.export.impl;

import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.common.StreamGobbler;
import com.sinosoft.ops.cimp.export.common.bean.AttributeBean;
import com.sinosoft.ops.cimp.export.common.bean.CategoryBean;
import com.sinosoft.ops.cimp.export.common.bean.CategoryData;
import com.sinosoft.ops.cimp.service.export.Pdf2htmlService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class Pdf2htmlServiceImpl implements Pdf2htmlService {

    private final Logger logger = LoggerFactory.getLogger(Pdf2htmlServiceImpl.class);

    @Override
    public boolean pdf2Html(String pdfFilePath, String pdfFileName,
                            String htmlFilePath, String htmlFileName) throws Exception {


        StringBuilder command = new StringBuilder();
        command.append(ExportConstant.PDF2HTML_PATH).append(" ");

        // 尽量减少用于文本的HTML元素的数目 (default: 0)
        command.append("--optimize-text 1 ");
        command.append("--zoom 1.4 ");

        // html中显示链接：0 - false, 1- true
        command.append("--process-outline 0 ");

        // 嵌入html中的字体后缀(ttf,otf,woff,svg) (default: "woff")
        command.append("--font-format woff ");

        command.append(pdfFilePath).append(pdfFileName).append(" ");
        command.append(htmlFileName).append(" ");

        try {
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec(command.toString(), null, new File(pdfFilePath));

            StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
            errorGobbler.start(); // 开启屏幕标准错误流
            StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
            outGobbler.start(); // 开启屏幕标准输出流

            int w = p.waitFor();
            int v = p.exitValue();
            if (w == 0 && v == 0) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
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
     *
     * @param doc
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void analysisPfPage(Document doc) throws ClassNotFoundException, IOException {

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
     *
     * @param doc
     */
    public void removeScriptElement(Document doc) {
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
     *
     * @param pfChildrenElements
     * @param buildDiv
     */
    public void writeInDiv(Elements pfChildrenElements, StringBuffer buildDiv) {
        Element lastElement = pfChildrenElements.get(pfChildrenElements.size() - 1);
        if (lastElement != null) {
            lastElement.after(buildDiv.toString());

        }
    }

    /**
     * 写入 style
     *
     * @param doc
     * @param buildStyle
     */
    public void writeInStyle(Document doc, StringBuffer buildStyle) {
        Element titleNode = doc.getElementsByTag("title").first();
        if (titleNode != null) {
            titleNode.before("<style>" + '\n' + buildStyle.toString() + "</style>");
        }
    }

    /**
     * 设置照片点击事件
     *
     * @param pfChildrenElements
     */
    public void setPhotoEvent(Elements pfChildrenElements) {
        if (pfChildrenElements == null) {
            return;
        }
        Element firstElement = pfChildrenElements.first();
        if (firstElement != null) {
            // onclick="alert('Hi')" onmouseover="this.style.backgroundColor='#5eb9f0'" onmouseout="this.style.backgroundColor=''" style="cursor:pointer"
            firstElement.attr("onclick", "titleClickFn(113,14)");
//			firstElement.attr("onmouseover", "this.style.backgroundColor='#5eb9f0'");//#F4F9FD, #FFA500, #5eb9f0
//			firstElement.attr("onmouseout", "this.style.backgroundColor=''");
            firstElement.attr("style", "cursor:pointer");
        }
    }

    /**
     * 处理 pf page
     *
     * @param categoryBeansPf
     * @param pfChildrenElements
     * @param buildStyle
     */
    public void pfPageX(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, StringBuffer buildStyle, StringBuffer buildDiv) {
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

    public void setNameIndex(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, List<Integer> nameIndexs) {
        String divText = null;
        for (Element divElement : pfChildrenElements) {
            divText = CategoryData.disposeDivText(divElement.text());
            if ("".equals(divText)) {
                continue;
            }
            if (isContainsDivText(categoryBeansPf, divText, divElement.elementSiblingIndex())) {
                nameIndexs.add(divElement.elementSiblingIndex());
            }
        }
    }

    public void setValueIndex(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, List<Integer> nameIndexs) {
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

    public void setDivEvent(List<CategoryBean> categoryBeansPf, Elements pfChildrenElements, StringBuffer buildDiv, List<AttributeBean> divEventAttributeBeans) {
        Element valueElement = null;
        for (CategoryBean categoryBean : categoryBeansPf) {
            for (AttributeBean attributeBean : categoryBean.getAttributeBeans()) {
                if (attributeBean.getNameIndex() == -1) {
                    continue;
                }
                if (attributeBean.getValueIndex() == -1) {
                    if ("A".equals(attributeBean.getProcessType())) {
//						buildDiv.append("<div id=\"" + attributeBean.getDivId() + "\" onclick=\"alert('147')\" onmouseover=\"this.style.backgroundColor='#5eb9f0'\" onmouseout=\"this.style.backgroundColor=''\" style=\"cursor:pointer\">" + attributeBean.getDivId() + "</div>");
                        buildDiv.append("<div id=\"" + attributeBean.getDivId() + "\" onclick=\"titleClickFn(" + categoryBean.getParentId() + ", " + categoryBean.getCurrentId() + ")\" onmouseover=\"this.style.backgroundColor='#5eb9f0'\" onmouseout=\"this.style.backgroundColor=''\" style=\"cursor:pointer\"></div>");
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
                    valueElement.attr("onclick", "titleClickFn(" + categoryBean.getParentId() + ", " + categoryBean.getCurrentId() + ")");
//					valueElement.attr("onclick", "alert('" + categoryBean.getParentId() + " - " + categoryBean.getCurrentId() + "')");
                    valueElement.attr("onmouseover", "this.style.backgroundColor='#5eb9f0'");//#F4F9FD, #FFA500, #5eb9f0
                    valueElement.attr("onmouseout", "this.style.backgroundColor=''");
                    valueElement.attr("style", "cursor:pointer");
                }
            }
        }
    }

    public void setStyle(StringBuffer buildStyle, List<AttributeBean> valueIndexNegativeBeans) {
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

    public boolean isContainsDivText(List<CategoryBean> categoryBeansPf, String divText, int attributeNameIndex) {
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
