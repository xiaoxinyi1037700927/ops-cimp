package com.sinosoft.ops.cimp.controller.export.gbrmb.bijie;

import com.sinosoft.ops.cimp.config.annotation.BusinessApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.export.ExportHandler;
import com.sinosoft.ops.cimp.export.bijie.ExportPdfBiJie;
import com.sinosoft.ops.cimp.export.bijie.ExportWordBiJie;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.data.NameAttrValue;
import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.service.export.Pdf2htmlService;
import com.sinosoft.ops.cimp.util.FileUtils;
import com.sinosoft.ops.cimp.util.MultiZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@BusinessApiGroup
@Api(description = "导出干部任免表(毕节)")
@Controller
@RequestMapping("/export/gbrmb/bj")
public class ExportGbrmbBiJieController extends BaseController {
    @Autowired
    private Pdf2htmlService pdf2htmlService;

    @Autowired
    private ExportService exportService;


    @ApiOperation(value = "生成并导出干部任免表word文件(毕节)")
    @GetMapping("/word/generateAndExport")
    public void generateAndExportGbrmb(HttpServletRequest request, HttpServletResponse response, String empIds) {
        if (StringUtils.isEmpty(empIds)) {
            writeJson(response, "empIds不能为空！");
            return;
        }

        String templateFilePath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.TEMPLATE_WORD_GBRMB_BJ;
        String exportPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_WORD_GBRMB;
        String exportPathZip = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_WORD_GBRMB_ZIP;
        String returnFilePath = "";

        String[] empIdArr = StringUtils.split(empIds, ",");
        boolean toPackage = empIdArr.length > 1;

        ArrayList<String> outFileNames = new ArrayList<>(empIdArr.length);
        try {
            //生成任免表的word文件
            for (String empId : empIdArr) {
                ExportHandler exportHandler = new ExportWordBiJie();
                Map<String, Object> allAttrValue = exportHandler.getAllAttrValue(empId);

                NameAttrValue nameAttrValue = ((ExportWordBiJie) exportHandler).getNameAttrValue();
                String outFile = exportPath + nameAttrValue.getName() + nameAttrValue.getCardNo() + "任免表.docx";

                exportHandler.processAttrValue(templateFilePath, allAttrValue, outFile);

                outFileNames.add(outFile);
            }

            if (outFileNames.size() == 0) {
                writeJson(response, "任免表生成失败！");
                return;
            }

            returnFilePath = toPackage ? exportPathZip + "干部任免表_" + System.currentTimeMillis() : outFileNames.get(0);

            if (toPackage) {
                File outputZipFile = new File(exportPathZip);
                if (!outputZipFile.exists()) {
                    // 若是上级目录也不存在, 则一并创建
                    outputZipFile.mkdirs();
                }

                // 将所有word压缩为一个zip文件
                MultiZipUtil.zip(returnFilePath, outFileNames);
                returnFilePath += ".zip";
            }

            String returnFileName = returnFilePath.substring(returnFilePath.lastIndexOf("/") + 1);
            returnFileName = new String(returnFileName.getBytes("utf-8"), "utf-8");
            returnFileName = URLEncoder.encode(returnFileName, "UTF-8");

            request.setCharacterEncoding("utf-8");
            //设置向浏览器端传送的文件格式
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + returnFileName);

//            returnFilePath = returnFilePath.replaceAll("\\\\", new String("\\\\\\\\")).replaceAll("/", new String("\\\\\\\\"));

            //写入文件流
            try (FileInputStream fis = new FileInputStream(returnFilePath);
                 OutputStream os = response.getOutputStream()) {
                byte[] b = new byte[1024 * 10];
                int i = 0;
                while ((i = fis.read(b)) > 0) {
                    os.write(b, 0, i);
                }
                os.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            writeJson(response, "任免表生成失败！");
            return;
        } finally {
            //删除生成的zip临时包
            if (toPackage) {
                FileUtils.deleteFile(returnFilePath);
            }
        }
    }

    @ApiOperation(value = "生成干部任免表html文件(毕节)")
    @PostMapping("/html/generate")
    public ResponseEntity generateAndExportGbrmbHTML(HttpServletRequest request, HttpServletResponse response, String empId) throws BusinessException {
        if (StringUtils.isEmpty(empId)) {
            return fail("empId不能为空！");
        }

        String templateFilePath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.TEMPLATE_WORD_GBRMB_BJ;
        String exportPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_WORD_GBRMB;

        String pdfFileName = empId + "RMB.pdf";
        String pdfFilePath = exportPath + pdfFileName;
        String htmlFileName = empId + "RMB.html";
        String htmlFilePath = exportPath + htmlFileName;

        try {
            File htmlFile = new File(htmlFilePath);
            if (!htmlFile.exists() || !htmlFile.isFile()) {
                //先生成任免表pdf文件
                ExportHandler exportHandler = new ExportPdfBiJie();
                Map<String, Object> allAttrValue = exportHandler.getAllAttrValue(empId);
                exportHandler.processAttrValue(templateFilePath, allAttrValue, pdfFilePath);

                //pdf2html
                boolean isSuccess = pdf2htmlService.pdf2Html(exportPath, pdfFileName, exportPath, htmlFileName);
                if (!isSuccess) {
                    return fail("pdf2html failed");
                }
            }

            String html = pdf2htmlService.analysisHtml(htmlFilePath);
            return ok(html);
        } catch (Exception e) {
            e.printStackTrace();
            return fail("任免表生成失败！");
        } finally {
            //删除临时生成的pdf文件
            FileUtils.deleteFile(pdfFilePath);
        }
    }


    @ApiOperation(value = "生成并导出干部任免表word文件(毕节)")
    @GetMapping("/lrmx/generateAndExport")
    public void generateAndExportGbrmbLRMX(HttpServletRequest request, HttpServletResponse response, String empIds) {
        if (StringUtils.isEmpty(empIds)) {
            writeJson(response, "empIds不能为空！");
            return;
        }

        String exportPath = ExportConstant.EXPORT_BASE_PATH + ExportConstant.EXPORT_LRMX;
        String returnFilePath = "";

        String[] empIdArr = StringUtils.split(empIds, ",");

        ArrayList<String> outFileNames = new ArrayList<>(empIdArr.length);

        try {
            for (String empId : empIdArr) {
                ExportHandler exportHandler = new ExportWordBiJie();
                Map<String, Object> allAttrValue = exportHandler.getAllAttrValue(empId);
                NameAttrValue nameAttrValue = ((ExportWordBiJie) exportHandler).getNameAttrValue();
                String lrmxFilePath = exportPath + nameAttrValue.getName() + "_" + System.currentTimeMillis() + ".lrmx";
                File file = new File(lrmxFilePath);
                if (!file.exists()) {
                    File dir = new File(exportPath);
                    if (!dir.exists()) dir.mkdirs();
                    file.createNewFile();
                }
                CreateXMLByDOM4J(file, allAttrValue);
                outFileNames.add(lrmxFilePath);
            }
            generatorZipAndDownload(outFileNames, request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateXMLByDOM4J(File dest, Map<String, Object> allAttrValue) {
        // 创建Document对象
        Document document = DocumentHelper.createDocument();
        // addElements(document);
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
        try {
            // stringWriter字符串是用来保存XML文档的
            // StringWriter stringWriter = new StringWriter();
            // xmlWriter是用来把XML文档写入字符串的(工具)
            // 创建XMLWriter对象
            XMLWriter writer = new XMLWriter(new FileOutputStream(dest), format);
            //XMLWriter writer = new XMLWriter(stringWriter, format);
            //设置不自动进行转义
            writer.setEscapeText(false);

            // 生成XML文件
            writer.write(document);

            //关闭XMLWriter对象
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map.Entry<String, Integer>> getXMlElements() {
        Map<String, Integer> map = new HashMap<>();
        try {
            Configurations configs = new Configurations();
            Configuration configuration = configs.properties(new File("classpath:application.properties"));
            String elements = configuration.getString("exportXMLElements");

            String[] es = elements.split(",");
            for (int i = 0; i < es.length; i++) {
                map.put(es[i], i + 1);
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        //根据值排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> {
            if (o1.getValue() > o2.getValue()) {
                return 1;
            } else if (o1.getValue() < o2.getValue()) {
                return -1;
            } else {
                return 0;
            }
        });
        return list;
    }

    public void generatorZipAndDownload(ArrayList<String> fileNames, HttpServletRequest request, HttpServletResponse response) {
        String outputZipFilePath = request.getSession().getServletContext()
                .getRealPath("resources/download/lrmx/zip/");
        File dir = new File(outputZipFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 如果需要打包则生成zip临时文件，不需要直接导出当前干部任免表
        String outputZipFileNamePath = outputZipFilePath + "\\" + "干部任免表_lrmx" + new Date().getTime();
        String zipName = outputZipFileNamePath.substring(outputZipFileNamePath.lastIndexOf("\\") + 1) + ".zip";
        try {
            MultiZipUtil.zip(outputZipFileNamePath, fileNames);
        } catch (Exception ee) {
            writeJson(response, "下载时打包失败！");
            return;
        }
        String outputZipPath = "";
        try {
            request.setCharacterEncoding("utf-8");
            zipName = new String(zipName.getBytes("utf-8"), "utf-8");
            // 获取文件路径
            outputZipPath = outputZipFileNamePath.replaceAll("\\\\", new String("\\\\\\\\")).replaceAll("/",
                    new String("\\\\\\\\")) + ".zip";

            outputZipPath = outputZipPath == null ? "" : outputZipPath;
            // 设置向浏览器端传送的文件格式
            response.setContentType("application/x-download");

            zipName = URLEncoder.encode(zipName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + zipName);
            FileInputStream fis = null;
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                fis = new FileInputStream(outputZipPath);
                byte[] b = new byte[1024 * 10];
                int i = 0;
                while ((i = fis.read(b)) > 0) {
                    os.write(b, 0, i);
                }
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 删除生成的zip临时包
        FileUtils.deleteFile(outputZipPath);
    }

    public String addText(Element e, String key, Map<String, Object> allAttrValue) {

        String value = "";
        switch (key) {
            case "XingMing"://姓名
                value = allAttrValue.get("name") == null ? "" : allAttrValue.get("name").toString();
                e.addText(value);
                break;
            case "XingBie"://性别
                value = allAttrValue.get("gender") == null ? "" : allAttrValue.get("gender").toString();
                e.addText(value);
                break;
            case "ChuShengNianYue"://出生年月
                value = allAttrValue.get("birthday") == null ? "" : allAttrValue.get("birthday").toString().replace(".", "");
                if (value.contains((char) 11 + "")) {
                    value = (value.split((char) 11 + ""))[0];
                }
                e.addText(value);
                break;
            case "MinZu"://民资
                value = allAttrValue.get("nation") == null ? "" : allAttrValue.get("nation").toString();
                e.addText(value);
                break;
            case "JiGuan"://籍贯
                value = allAttrValue.get("native") == null ? "" : allAttrValue.get("native").toString();
                e.addText(value);
                break;
            case "ChuShengDi"://出生地
                value = allAttrValue.get("birthPlace") == null ? "" : allAttrValue.get("birthPlace").toString();
                e.addText(value);
                break;
            case "RuDangShiJian"://入党时间
                value = allAttrValue.get("partyDate") == null ? "" : allAttrValue.get("partyDate").toString();
                e.addText(value);
                break;
            case "CanJiaGongZuoShiJian"://参加工作时间
                value = allAttrValue.get("jobDate") == null ? "" : allAttrValue.get("jobDate").toString();
                e.addText(value);
                break;
            case "JianKangZhuangKuang"://健康状况
                value = allAttrValue.get("healthy") == null ? "" : allAttrValue.get("healthy").toString();
                e.addText(value);
                break;
            case "ZhuanYeJiShuZhiWu"://专业技术职务
                value = allAttrValue.get("technicPosition") == null ? "" : allAttrValue.get("technicPosition").toString();
                e.addText(value);
                break;
            case "ShuXiZhuanYeYouHeZhuanChang"://熟悉专业有何专长
                value = allAttrValue.get("specialty") == null ? "" : allAttrValue.get("specialty").toString();
                e.addText(value);
                break;
            case "QuanRiZhiJiaoYu_XueLi"://全日制教育学历
                String xueli = "";
                if (allAttrValue.get("ftDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ftDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 0) {
                        xueli = (String) xueliList.get(0);
                    }
                }
                value = xueli;
                //value=allAttrValue.get("ftDiplomaAndDegree")==null?"":allAttrValue.get("ftDiplomaAndDegree").toString().replace("[", "").replace("]", "");
                e.addText(value);
                break;
            case "QuanRiZhiJiaoYu_XueWei":
                String xuewei = "";
                if (allAttrValue.get("ftDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ftDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 1) {
                        xuewei = (String) xueliList.get(1);
                    }
                }
                value = xuewei;
                //value=allAttrValue.get("")==null?"":allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "QuanRiZhiJiaoYu_XueLi_BiYeYuanXiaoXi":
                value = allAttrValue.get("ftInstituteAndSpecialty") == null ? "" : allAttrValue.get("ftInstituteAndSpecialty").toString();
                e.addText(value);
                break;
            case "QuanRiZhiJiaoYu_XueWei_BiYeYuanXiaoXi":
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "ZaiZhiJiaoYu_XueLi"://在职教育学历
                String xueli2 = "";
                if (allAttrValue.get("ptDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ptDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 0) {
                        xueli2 = (String) xueliList.get(0);
                    }
                }
                value = xueli2;
                //value=allAttrValue.get("ptDiplomaAndDegree")==null?"":allAttrValue.get("ptDiplomaAndDegree").toString().replace("[", "").replace("]", "");
                e.addText(value);
                break;
            case "ZaiZhiJiaoYu_XueWei":
                String xuewei2 = "";
                if (allAttrValue.get("ptDiplomaAndDegree") != null) {
                    List xueliList = (List) allAttrValue.get("ptDiplomaAndDegree");
                    if (xueliList != null && xueliList.size() > 1) {
                        xuewei2 = (String) xueliList.get(1);
                    }
                }
                value = xuewei2;
                //value=allAttrValue.get("")==null?"":allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "ZaiZhiJiaoYu_XueLi_BiYeYuanXiaoXi":
                value = allAttrValue.get("ptInstituteAndSpecialty") == null ? "" : allAttrValue.get("ptInstituteAndSpecialty").toString();
                e.addText(value);
                break;
            case "ZaiZhiJiaoYu_XueWei_BiYeYuanXiaoXi":
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "XianRenZhiWu"://现任职务
                value = allAttrValue.get("position") == null ? "" : allAttrValue.get("position").toString();
                e.addText(value);
                break;
            case "NiRenZhiWu":
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "NiMianZhiWu":
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "JiangChengQingKuang"://奖惩情况
                value = allAttrValue.get("hornor") == null ? "" : allAttrValue.get("hornor").toString();
                e.addText(value);
                break;
            case "NianDuKaoHeJieGuo"://年度考核结果
                value = allAttrValue.get("evaluation") == null ? "" : allAttrValue.get("evaluation").toString();
                e.addText(value);
                break;
            case "RenMianLiYou":
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
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
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "JiSuanNianLingShiJian"://计算年龄时间
                //value=allAttrValue.get("")==null?"":allAttrValue.get("").toString();
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                e.addText(format.format(date));
                break;
            case "TianBiaoShiJian"://填表时间
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "TianBiaoRen"://填表人
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "ShenFenZheng"://身份证
                value = allAttrValue.get("") == null ? "" : allAttrValue.get("").toString();
                e.addText(value);
                break;
            case "ZhaoPian":
//                Object file = allAttrValue.get("photo");
//                String photo = handlePhoto(file);
//                e.addText(photo.trim());
                break;
            case "Version":
                e.addText("3.2.1.6");
                break;
        }
        return "";
    }

    public String handlePhoto(Object file) {
        byte[] bs = (byte[]) file;
        // 对字节数组Base64编码
        BASE64Encoder encode = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encode.encode(bs);
    }

    //处理简历
    public void handleResume(Element e, List<Map<String, String>> listMap) {
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
    public void handleFamilyPerson(Element e, Map<String, String> fmRel, Map<String, String> fmName, Map<String, String> fmAge, Map<String, String> fmParty, Map<String, String> fmOrgAndJob) {

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
