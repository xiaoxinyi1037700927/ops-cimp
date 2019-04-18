package com.sinosoft.ops.cimp.service.export.impl;

import com.sinosoft.ops.cimp.dao.ExportDao;
import com.sinosoft.ops.cimp.export.bijie.ExportHandlerBiJie;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.data.NameAttrValue;
import com.sinosoft.ops.cimp.service.common.impl.BaseServiceImpl;
import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.service.export.Pdf2htmlService;
import com.sinosoft.ops.cimp.util.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class ExportServiceImpl extends BaseServiceImpl implements ExportService {

    @Autowired
    @Qualifier("exportDao")
    private ExportDao exportWordDao;

    @Autowired
    private Pdf2htmlService pdf2htmlService;

    @Value("${exportXMLElements}")
    private String exportXMLElements;

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

    /**
     * 生成干部任免表word文件(毕节)
     *
     * @param empId
     * @return
     */
    @Override
    public String generateGbrmbWordBiJie(String empId) {
        String templateFilePath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.TEMPLATE_WORD_GBRMB_BJ;
        String exportPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_WORD_GBRMB;
        String exportPathZip = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_WORD_GBRMB_ZIP;

        try {
            ExportHandlerBiJie exportHandler = new ExportHandlerBiJie();
            Map<String, Object> allAttrValue = exportHandler.getAllAttrValue(empId);

            NameAttrValue nameAttrValue = exportHandler.getNameAttrValue();
            String outFile = exportPath + nameAttrValue.getName() + nameAttrValue.getCardNo() + ExportConstant.RMB_SUFFIX_WORD;

            //如果html已存在 或 不存在但生成成功
            if (FileUtils.fileExists(outFile) || exportHandler.generate(templateFilePath, allAttrValue, outFile)) {
                return outFile;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 生成干部任免表pdf文件(毕节)
     *
     * @param empId
     * @return
     */
    @Override
    public String generateGbrmbHTMLBiJie(String empId) {
        String templateFilePath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.TEMPLATE_WORD_GBRMB_BJ;
        String exportPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_WORD_GBRMB;
        String htmlFileName = empId + ExportConstant.RMB_SUFFIX_HTML;
        String htmlFilePath = exportPath + htmlFileName;

        //如果html文件已存在，直接返回
        if (FileUtils.fileExists(htmlFilePath)) {
            return htmlFilePath;
        }

        String pdfFileName = empId + ExportConstant.RMB_SUFFIX_PDF;
        String pdfFilePath = exportPath + pdfFileName;

        try {
            //先生成任免表pdf文件
            ExportHandlerBiJie exportHandler = new ExportHandlerBiJie();
            Map<String, Object> allAttrValue = exportHandler.getAllAttrValue(empId);
            exportHandler.processAttrValue(templateFilePath, allAttrValue, pdfFilePath);

            //pdf2html
            if (pdf2htmlService.pdf2Html(exportPath, pdfFileName, exportPath, htmlFileName)) {
                return htmlFilePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //删除生成的pdf文件
            FileUtils.deleteFile(pdfFilePath);
        }
        return null;
    }

    /**
     * 生成干部任免表lrmx文件
     *
     * @param empId
     * @return
     */
    @Override
    public String generateGbrmbLRMX(String empId) {
        String exportPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_LRMX;
        try {
            ExportHandlerBiJie exportHandler = new ExportHandlerBiJie();
            Map<String, Object> allAttrValue = exportHandler.getAllAttrValue(empId);
            NameAttrValue nameAttrValue = exportHandler.getNameAttrValue();
            String lrmxFilePath = exportPath + nameAttrValue.getName() + "_" + System.currentTimeMillis() + ExportConstant.RMB_SUFFIX_LRMX;

            if (FileUtils.fileExists(lrmxFilePath) || createXMLByDOM4J(FileUtils.createFile(lrmxFilePath), allAttrValue)) {
                return lrmxFilePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean createXMLByDOM4J(File dest, Map<String, Object> allAttrValue) {
        // 创建Document对象
        Document document = DocumentHelper.createDocument();
        List<Map.Entry<String, Integer>> elementList = getXMlElements();
        Element root = document.addElement("Person");

        for (Map.Entry<String, Integer> entry : elementList) {
            // 创建根节点
            Element e = root.addElement(entry.getKey());
            addText(e, entry.getKey(), allAttrValue);
        }

        // 创建输出格式(OutputFormat对象)
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setNewlines(true); //设置是否换行
        ///设置输出文件的编码
        format.setEncoding("utf-8");
        //处理简历换行显示
        //  format.setLineSeparator("\n");
        format.setTrimText(false);
        format.setIndent("  ");
        XMLWriter writer = null;
        try {
            // stringWriter字符串是用来保存XML文档的
            // StringWriter stringWriter = new StringWriter();
            // xmlWriter是用来把XML文档写入字符串的(工具)
            // 创建XMLWriter对象
            writer = new XMLWriter(new FileOutputStream(dest), format);
            //XMLWriter writer = new XMLWriter(stringWriter, format);
            //设置不自动进行转义
            writer.setEscapeText(false);

            // 生成XML文件
            writer.write(document);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                //关闭XMLWriter对象
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private List<Map.Entry<String, Integer>> getXMlElements() {
        Map<String, Integer> map = new HashMap<>();
        String[] es = exportXMLElements.split(",");
        for (int i = 0; i < es.length; i++) {
            map.put(es[i], i + 1);
        }

        //根据值排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));

        return list;
    }


    private void addText(Element e, String key, Map<String, Object> allAttrValue) {
        switch (key) {
            case "XingMing"://姓名
                e.addText(allAttrValue.get("name") == null ? "" : allAttrValue.get("name").toString());
                break;
            case "XingBie"://性别
                e.addText(allAttrValue.get("gender") == null ? "" : allAttrValue.get("gender").toString());
                break;
            case "ChuShengNianYue"://出生年月
                String value = allAttrValue.get("birthday") == null ? "" : allAttrValue.get("birthday").toString().replace(".", "");
                if (value.contains((char) 11 + "")) {
                    value = (value.split((char) 11 + ""))[0];
                }
                e.addText(value);
                break;
            case "MinZu"://民资
                e.addText(allAttrValue.get("nation") == null ? "" : allAttrValue.get("nation").toString());
                break;
            case "JiGuan"://籍贯
                e.addText(allAttrValue.get("native") == null ? "" : allAttrValue.get("native").toString());
                break;
            case "ChuShengDi"://出生地
                e.addText(allAttrValue.get("birthPlace") == null ? "" : allAttrValue.get("birthPlace").toString());
                break;
            case "RuDangShiJian"://入党时间
                e.addText(allAttrValue.get("partyDate") == null ? "" : allAttrValue.get("partyDate").toString());
                break;
            case "CanJiaGongZuoShiJian"://参加工作时间
                e.addText(allAttrValue.get("jobDate") == null ? "" : allAttrValue.get("jobDate").toString());
                break;
            case "JianKangZhuangKuang"://健康状况
                e.addText(allAttrValue.get("healthy") == null ? "" : allAttrValue.get("healthy").toString());
                break;
            case "ZhuanYeJiShuZhiWu"://专业技术职务
                e.addText(allAttrValue.get("technicPosition") == null ? "" : allAttrValue.get("technicPosition").toString());
                break;
            case "ShuXiZhuanYeYouHeZhuanChang"://熟悉专业有何专长
                e.addText(allAttrValue.get("specialty") == null ? "" : allAttrValue.get("specialty").toString());
                break;
            case "QuanRiZhiJiaoYu_XueLi"://全日制教育学历
                String xueli = "";
                if (allAttrValue.get("ftDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ftDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 0) {
                        xueli = (String) xueliList.get(0);
                    }
                }
                e.addText(xueli);
                break;
            case "QuanRiZhiJiaoYu_XueWei":
                String xuewei = "";
                if (allAttrValue.get("ftDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ftDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 1) {
                        xuewei = (String) xueliList.get(1);
                    }
                }
                e.addText(xuewei);
                break;
            case "QuanRiZhiJiaoYu_XueLi_BiYeYuanXiaoXi":
                e.addText(allAttrValue.get("ftInstituteAndSpecialty") == null ? "" : allAttrValue.get("ftInstituteAndSpecialty").toString());
                break;
            case "QuanRiZhiJiaoYu_XueWei_BiYeYuanXiaoXi":
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "ZaiZhiJiaoYu_XueLi"://在职教育学历
                String xueli2 = "";
                if (allAttrValue.get("ptDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ptDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 0) {
                        xueli2 = (String) xueliList.get(0);
                    }
                }
                e.addText(xueli2);
                break;
            case "ZaiZhiJiaoYu_XueWei":
                String xuewei2 = "";
                if (allAttrValue.get("ptDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ptDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 1) {
                        xuewei2 = (String) xueliList.get(1);
                    }
                }
                e.addText(xuewei2);
                break;
            case "ZaiZhiJiaoYu_XueLi_BiYeYuanXiaoXi":
                e.addText(allAttrValue.get("ptInstituteAndSpecialty") == null ? "" : allAttrValue.get("ptInstituteAndSpecialty").toString());
                break;
            case "ZaiZhiJiaoYu_XueWei_BiYeYuanXiaoXi":
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "XianRenZhiWu"://现任职务
                e.addText(allAttrValue.get("position") == null ? "" : allAttrValue.get("position").toString());
                break;
            case "NiRenZhiWu":
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "NiMianZhiWu":
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "JiangChengQingKuang"://奖惩情况
                value = allAttrValue.get("hornor") == null ? "" : allAttrValue.get("hornor").toString();
                e.addText(value);
                break;
            case "NianDuKaoHeJieGuo"://年度考核结果
                e.addText(allAttrValue.get("evaluation") == null ? "" : allAttrValue.get("evaluation").toString());
                break;
            case "RenMianLiYou":
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "JianLi"://简历
                List<Map<String, String>> listMap = (List<Map<String, String>>) allAttrValue.get("resume");
                //处理简历
                if (!listMap.isEmpty()) {
                    handleResume(e, listMap);
                }
                break;
            case "JiaTingChengYuan"://家庭成员
                Map<String, String> fmRel = (HashMap<String, String>) allAttrValue.get("fmRel") == null ? new HashMap<String, String>() : (HashMap<String, String>) allAttrValue.get("fmRel");
                Map<String, String> fmName = (HashMap<String, String>) allAttrValue.get("fmName") == null ? new HashMap<String, String>() : (HashMap<String, String>) allAttrValue.get("fmName");
                Map<String, String> fmAge = (HashMap<String, String>) allAttrValue.get("fmAge") == null ? new HashMap<String, String>() : (HashMap<String, String>) allAttrValue.get("fmAge");
                Map<String, String> fmParty = (HashMap<String, String>) allAttrValue.get("fmParty") == null ? new HashMap<String, String>() : (HashMap<String, String>) allAttrValue.get("fmParty");
                Map<String, String> fmOrgAndJob = (HashMap<String, String>) allAttrValue.get("fmOrgAndJob") == null ? new HashMap<String, String>() : (HashMap<String, String>) allAttrValue.get("fmOrgAndJob");
                //处理家庭成员
                handleFamilyPerson(e, fmRel, fmName, fmAge, fmParty, fmOrgAndJob);
                break;
            case "ChengBaoDanWei"://呈报单位
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "JiSuanNianLingShiJian"://计算年龄时间
                //value=allAttrValue.get("")==null?"":allAttrValue.get("").toString();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                e.addText(format.format(date));
                break;
            case "TianBiaoShiJian"://填表时间
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "TianBiaoRen"://填表人
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "ShenFenZheng"://身份证
                e.addText(allAttrValue.get("") == null ? "" : allAttrValue.get("").toString());
                break;
            case "ZhaoPian":
//                e.addText(Base64.getEncoder().encodeToString((byte[]) allAttrValue.get("photo")).trim());
                break;
            case "Version":
                e.addText("3.2.1.6");
                break;
        }
    }

    //处理简历
    private void handleResume(Element e, List<Map<String, String>> listMap) {
        for (int i = 0; i < listMap.size(); i++) {
            String resumeStartDate = listMap.get(i).get("resumeStartDate");
            String resumeContent = listMap.get(i).get("resumeContent");
            //String rn=System.getProperty("line.separator");
            if (resumeContent.contains("<")) {
                resumeContent = resumeContent.replace("<", "[");
            }
            if (resumeContent.contains(">")) {
                resumeContent = resumeContent.replace(">", "]");
            }

            if (i == listMap.size() - 1) {//最后一个简历，没有结束日期，用空格占位
                if (resumeStartDate.length() <= 9) {//2014.11--
                    e.addText(resumeStartDate + "         " + resumeContent + System.getProperty("line.separator"));
                } else if (resumeStartDate.contains("至今")) {//2014.11--至今
                    e.addText(resumeStartDate + "     " + resumeContent + System.getProperty("line.separator"));
                } else {
                    e.addText(resumeStartDate + "  " + resumeContent + System.getProperty("line.separator"));
                }
            } else {
                e.addText(resumeStartDate + "  " + resumeContent + System.getProperty("line.separator"));
            }
            //e.addText(resumeStartDate+"  "+resumeContent+"\r\n");
        }
    }

    //处理家庭成员
    private void handleFamilyPerson(Element e, Map<String, String> fmRel, Map<String, String> fmName, Map<String, String> fmAge, Map<String, String> fmParty, Map<String, String> fmOrgAndJob) {

        for (int i = 0; i < fmRel.size(); i++) {
            Element item = e.addElement("Item");
            String chengWei = fmRel.get("fmRel_" + i) == null ? "" : fmRel.get("fmRel_" + i);//称谓
            item.addElement("ChengWei").addText(chengWei);

            String xingMing = fmName.get("fmName_" + i) == null ? "" : fmName.get("fmName_" + i);//姓名
            item.addElement("XingMing").addText(xingMing);

            /*String chuShengRiQi=fmAge.get("fmAge_"+i)==null?"":fmAge.get("fmAge_"+i);//出生日期
				item.addElement("ChuShengRiQi").addText(chuShengRiQi);*/
            //fmAge是任免表家庭成员用的年龄
            String chuShengRiQi = fmAge.get("fmBirthday_" + i) == null ? "" : fmAge.get("fmBirthday_" + i).toString().replace("-", "");//出生日期
            item.addElement("ChuShengRiQi").addText(chuShengRiQi);

            String zhengZhiMianMao = fmParty.get("fmParty_" + i) == null ? "" : fmParty.get("fmParty_" + i);//政治面貌
            item.addElement("ZhengZhiMianMao").addText(zhengZhiMianMao);

            String gongZuoDanWeiJiZhiWu = fmOrgAndJob.get("fmOrgAndJob_" + i) == null ? "" : fmOrgAndJob.get("fmOrgAndJob_" + i);//工作单位及职务
            item.addElement("GongZuoDanWeiJiZhiWu").addText(gongZuoDanWeiJiZhiWu);
        }
    }

}
