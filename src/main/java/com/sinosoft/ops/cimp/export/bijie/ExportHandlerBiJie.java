package com.sinosoft.ops.cimp.export.bijie;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.PdfSaveOptions;
import com.sinosoft.ops.cimp.export.AbstractExportHandler;
import com.sinosoft.ops.cimp.export.data.*;
import com.sinosoft.ops.cimp.export.processor.*;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExportHandlerBiJie extends AbstractExportHandler {

    @Override
    protected void init() {
        //放入 属性 - 属性获取处理器
        attrValueMap.put(NameAttrValue.KEY, new NameAttrValue());
//        attrValueMap.put(PhotoAttrValue.KEY, new PhotoAttrValue());
        attrValueMap.put(GenderAttrValue.KEY, new GenderAttrValue());
        attrValueMap.put(BirthdayAttrValue.KEY, new BirthdayAttrValue());
        attrValueMap.put(NationAttrValue.KEY, new NationAttrValue());
        attrValueMap.put(NativeAttrValue.KEY, new NativeAttrValue());
        attrValueMap.put(BirthPlaceAttrValue.KEY, new BirthPlaceAttrValue());
        attrValueMap.put(PartyDateAttrValue.KEY, new PartyDateAttrValue());
        attrValueMap.put(JobDateAttrValue.KEY, new JobDateAttrValue());
        attrValueMap.put(HealthyAttrValue.KEY, new HealthyAttrValue());
        attrValueMap.put(TechnicPositionAttrValue.KEY, new TechnicPositionAttrValue());
        attrValueMap.put(SpecialtyAttrValue.KEY, new SpecialtyAttrValue());
        attrValueMap.put(FtDiplomaAndDegreeAttrValue.KEY, new FtDiplomaAndDegreeAttrValue());
        attrValueMap.put(FtInstituteAndSpecialtyAttrValue.KEY, new FtInstituteAndSpecialtyAttrValue());
        attrValueMap.put(PtDiplomaAndDegreeAttrValue.KEY, new PtDiplomaAndDegreeAttrValue());
        attrValueMap.put(PtInstituteAndSpecialtyAttrValue.KEY, new PtInstituteAndSpecialtyAttrValue());
        attrValueMap.put(PositionAttrValue.KEY, new PositionAttrValue());
        attrValueMap.put(ResumeAttrValue.KEY, new ResumeAttrValue());
        attrValueMap.put(HornorAttrValue.KEY, new HornorAttrValue());
        attrValueMap.put(EvaluationAttrValue.KEY, new EvaluationAttrValue());
        attrValueMap.put(FamilyRelAttrValue.KEY, new FamilyRelAttrValue());
        attrValueMap.put(FamilyNameAttrValue.KEY, new FamilyNameAttrValue());
        attrValueMap.put(FamilyAgeAttrValue.KEY, new FamilyAgeAttrValue());
        attrValueMap.put(FamilyPartyAttrValue.KEY, new FamilyPartyAttrValue());
        attrValueMap.put(FamilyImpAttrValue.KEY, new FamilyImpAttrValue());
        attrValueMap.put(FamilyOrgAndJobAttrValue.KEY, new FamilyOrgAndJobAttrValue());
        //放入 属性 - 属性值处理器
        attrValueProcessorMap.put(NameAttrValue.KEY, new NameAttrValueProcessor());
//        attrValueProcessorMap.put(PhotoAttrValue.KEY, new PhotoAttrValueProcessor());
        attrValueProcessorMap.put(NationAttrValue.KEY, new GenericNameAttrValueProcessor());
        attrValueProcessorMap.put(NativeAttrValue.KEY, new GenericNameAttrValueProcessor());
        attrValueProcessorMap.put(BirthPlaceAttrValue.KEY, new GenericNameAttrValueProcessor());
        attrValueProcessorMap.put(TechnicPositionAttrValue.KEY, new TechnicPositionAttrValueProcessor());
        attrValueProcessorMap.put(SpecialtyAttrValue.KEY, new TechnicPositionAttrValueProcessor());
        attrValueProcessorMap.put(FtDiplomaAndDegreeAttrValue.KEY, new DiplomaAndDegreeAttrValueProcessor());
        attrValueProcessorMap.put(PtDiplomaAndDegreeAttrValue.KEY, new DiplomaAndDegreeAttrValueProcessor());
        attrValueProcessorMap.put(PositionAttrValue.KEY, new PositionAttrValueProcessor());
        attrValueProcessorMap.put(ResumeAttrValue.KEY, new ResumeAttrValueProcessor());
        attrValueProcessorMap.put(HornorAttrValue.KEY, new HornorAttrValueProcessor());
        attrValueProcessorMap.put(EvaluationAttrValue.KEY, new EvaluationAttrValueProcessor());
        attrValueProcessorMap.put(FamilyOrgAndJobAttrValue.KEY, new FamilyOrgAndJobAttrValueProcessor());
        attrValueProcessorMap.put("default", new DefaultAttrValueProcessor());
    }


    public NameAttrValue getNameAttrValue() {
        return (NameAttrValue) attrValueMap.get(NameAttrValue.KEY);
    }


    /**
     * 执行属性值样式处理器
     *
     * @param templateFilePath 模板文件路径
     * @param attrValues       属性名和属性值的键值对
     * @param outputFilePath   文件输出路径
     */
    @Override
    public void processAttrValue(String templateFilePath, Map<String, Object> attrValues, String outputFilePath) throws Exception {
        //根据resume的内容更换模板
        List<Map<String, Object>> resume = (List<Map<String, Object>>) attrValues.get("resume");

        List<Integer> lineWords = new ArrayList<>();
        if (resume != null && resume.size() > 0) {
            for (Map<String, Object> map : resume) {
                String resumeContent = StringUtil.obj2Str(map.get("resumeContent"));
                Integer count = 0;
                //递归判断该条断行处
                getRightLine(resumeContent);
                for (Integer addLine : addLines) {
                    count += addLine == null ? 0 : addLine;
                }
                if (addLines.size() > 0) {
                    count = resumeContent.length() - addLines.size() * 4;
                } else {
                    count = count + resumeContent.length();
                }
                lineWords.add(count);
                addLines = new ArrayList<>();
            }
        }

        GetRightFontSizeProcessor getRightFontSizeProcessor = new GetRightFontSizeProcessor();
        double rightFontSize = getRightFontSizeProcessor.getRightFontSize(lineWords);
        //如果字体大于等于12小于等于14则使用悬挂缩进为10.5的
        Map<Double, String> fontSize2Template = new HashMap<Double, String>();
        fontSize2Template.put(12d, "_11.docx");
        fontSize2Template.put(11d, "_11.docx");
        fontSize2Template.put(10.5d, "_11.docx");
        fontSize2Template.put(10d, "_11.docx");
        fontSize2Template.put(9d, "_11_5.docx");
        fontSize2Template.put(8d, "_11_5.docx");
        fontSize2Template.put(7.5d, "_12.docx");
        fontSize2Template.put(6.5d, "_12.docx");
        fontSize2Template.put(5.5d, "_13.docx");
        fontSize2Template.put(5d, "_13_5.docx");
        String templateName = fontSize2Template.get(rightFontSize);
        if (StringUtils.isNotEmpty(templateName)) {
            if (templateFilePath.endsWith(".doc")) {
                String s = templateFilePath.replaceAll("\\.doc", templateName);
                templateFilePath = s.substring(0, s.lastIndexOf(".")) + ".doc";
            } else {
                templateFilePath = templateFilePath.replaceAll("\\.docx", templateName);
            }
        } else {
            templateFilePath = templateFilePath.replaceAll("\\.docx", "_10_5.docx");
        }
        //获取模板得到document对象
        Path path = Paths.get(templateFilePath);
        if (Files.notExists(path)) {
            throw new RuntimeException("模版文件未找到");
        }
        //破解Aspose代码
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream license = loader.getResourceAsStream("license.xml");// 凭证文件
        License aposeLic = new License();
        aposeLic.setLicense(license);

        license.close();
        Document document = new Document(path.toFile().getPath());

        for (Map.Entry<String, Object> entry : attrValues.entrySet()) {
            String attrName = entry.getKey();
            Object attrValue = entry.getValue();

            Map<String, Object> attrAndValue = new HashMap<>(1);
            if (attrValueProcessorMap.containsKey(attrName)) {
                AttrValueProcessor attrValueProcessor = attrValueProcessorMap.get(attrName);

                if (attrValue instanceof Map) {
                    for (Map.Entry<String, String> entry1 : ((Map<String, String>) attrValue).entrySet()) {
                        String key = entry1.getKey();
                        String value = entry1.getValue();
                        Map<String, Object> attrAndValueMap = new HashMap<>(1);
                        attrAndValueMap.put(key, value);
                        attrValueProcessor.processAttr(document, attrAndValueMap);
                    }
                } else {
                    if (StringUtils.equals(attrName, "resume")) {
                        Map<String, Object> resumeMap = new HashMap<>(2);
                        resumeMap.put("fontSize", rightFontSize);
                        resumeMap.put("resumeContent", attrValue);
                        attrValueProcessor.processAttr(document, resumeMap);
                    } else {
                        attrAndValue.put(attrName, attrValue);
                        attrValueProcessor.processAttr(document, attrAndValue);
                    }
                }
            } else {
                attrAndValue.put(attrName, attrValue);
                AttrValueProcessor defaultAttrValueProcessor = attrValueProcessorMap.get("default");
                if (attrValue instanceof Map) {
                    for (Map.Entry<String, String> entry1 : ((Map<String, String>) attrValue).entrySet()) {
                        String key = entry1.getKey();
                        String value = entry1.getValue();
                        Map<String, Object> attrAndValueMap = new HashMap<>(1);
                        attrAndValueMap.put(key, value);
                        defaultAttrValueProcessor.processAttr(document, attrAndValueMap);
                    }
                } else {
                    defaultAttrValueProcessor.processAttr(document, attrAndValue);
                }
            }
        }
        //清空标记域，防止域没有被填充而输出域
        document.getMailMerge().deleteFields();

        //保存文件
        if (outputFilePath.endsWith(".pdf")) {
            document.save(outputFilePath, new PdfSaveOptions());
        } else {
            document.save(outputFilePath);
        }
    }

    private int getRightLine(String resumeContent) {
        Pattern compile = Pattern.compile("[：| ；]\\d{4}\\.");
        Matcher matcher = compile.matcher(resumeContent);

        Pattern pattern = Pattern.compile("\\d{4}\\.");
        Matcher matcherNumber = pattern.matcher(resumeContent);
        if (matcher.find()) {
            int start = matcher.start();
            if (start >= 20 && start < 27) {
                addLines.add(27 - start);
            }
            if (matcherNumber.find() && (start - 8) >= 20) {
                addLines.add(35 - start);
            }
            String right = resumeContent.substring(start + 1);
            int i = getRightLine(right.trim());
            if (i == -1) {
                return -1;
            }
        }
        return -1;
    }

}
