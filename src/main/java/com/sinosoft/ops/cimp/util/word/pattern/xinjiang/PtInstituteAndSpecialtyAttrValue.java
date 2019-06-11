package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;


import com.sinosoft.ops.cimp.service.export.ExportService;
import com.sinosoft.ops.cimp.util.CodeTranslateUtil;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by rain chen on 2017/10/10.
 * 学历学位 -- 毕业院校系及专业
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class PtInstituteAndSpecialtyAttrValue implements AttrValue {

    private final String key = "ptInstituteAndSpecialty";
    private final int order = 14;

    @Override
    @SuppressWarnings("unchecked")
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService) throws AttrValueException {
        String ftInstituteAndSpecialty = "";

        //特殊处理 在职教育无学历不需要判断是否学历
        List listA005 = (List) attrValueContext.get("A005");
        for (Object A005 : listA005) {
            Map eduMap = (Map) A005;
            String record = StringUtil.obj2Str(eduMap.get("A005001_A"));
            String eduType = StringUtil.obj2Str(eduMap.get("A005025"));// 教育类别
            if(eduType.equals("2") && StringUtil.isEmptyOrNull(record))
            {
                String ftInstitute = StringUtil.obj2Str(eduMap.get("A005040")); //毕业院校
                String ftSpecialtyCode = StringUtil.obj2Str(eduMap.get("A005010_A")); //专业信息代码
                String ftSpecialty = StringUtil.obj2Str(eduMap.get("A005010_B")); //专业信息名称
                ftSpecialty = ftSpecialty.endsWith("专业")?ftSpecialty:(ftSpecialty+"专业");
                ftInstituteAndSpecialty = ftInstitute + ftSpecialty;
            }
        }

        Map<String, List<Map>> jobEduMapList = (Map<String, List<Map>>) attrValueContext.get("jobEduMapList");
        List<Map> jobEdu = jobEduMapList.get("xueli");    //全日制教育

        if (jobEdu != null && jobEdu.size() > 0) {
            Map jobEduMap = jobEdu.get(0);
            String A005001A = StringUtil.obj2Str(jobEduMap.get("A005001_A")); //学历代码
            //高中及以下学历， 毕业院系专业不显示
            if (StringUtils.isNotEmpty(A005001A) && NumberUtils.toInt(A005001A.substring(0, 1)) > 5) {
                return "";
            }

            String ftInstitute = StringUtil.obj2Str(jobEduMap.get("A005040")); //毕业院校
            String ftSpecialtyCode = StringUtil.obj2Str(jobEduMap.get("A005010_A")); //专业信息代码
            String ftSpecialty = StringUtil.obj2Str(jobEduMap.get("A005010_B")); //专业信息名称
            if (!StringUtils.equals(ftSpecialty,"")){
                ftSpecialty = ftSpecialty.endsWith("专业")?ftSpecialty:(ftSpecialty+"专业");
            }else if(StringUtil.isNotEmptyOrNull(ftSpecialtyCode)){
                ftSpecialty = CodeTranslateUtil.codeToName("BT0055", ftSpecialtyCode, exportWordService);
            	ftSpecialty = ftSpecialty.endsWith("专业")?ftSpecialty:(ftSpecialty+"专业");
            }
            ftInstituteAndSpecialty = ftInstitute + ftSpecialty;
            //20190520新增 学历相同，但毕业院校和专业不同
            for(int i=1;i<jobEdu.size();i++) {
            	Map o=jobEdu.get(i);
            	String xl=StringUtil.obj2Str(o.get("A005001_A"));
            	if(xl.equals(A005001A)) {
            		String yx = StringUtil.obj2Str(o.get("A005040")); //毕业院校
                    String zydm = StringUtil.obj2Str(o.get("A005010_A")); //专业信息代码
                    String zymc = StringUtil.obj2Str(o.get("A005010_B")); //专业信息名称
                    if (!StringUtils.equals(zymc,"")){
                    	zymc = zymc.endsWith("专业")?zymc:(zymc+"专业");
                    }else if(StringUtil.isNotEmptyOrNull(zydm)){
                    	zymc = CodeTranslateUtil.codeToName("BT0055", zydm, exportWordService);
                    	zymc = zymc.endsWith("专业")?zymc:(zymc+"专业");
                    }
                    String yxzy=yx + zymc;
                    if(ftInstituteAndSpecialty.indexOf(yxzy)<0) {
                    	ftInstituteAndSpecialty = ftInstituteAndSpecialty +(char)11+ yxzy;
                    }
                    
            	}
            }
        }
        
        //20190520新增，获取学位对应的学校
        List<Map> degree = jobEduMapList.get("xuewei");
		if (degree != null && degree.size() > 0) {
			Map jobEduMap = degree.get(0);
			String A005050A = StringUtil.obj2Str(jobEduMap.get("A005050_A"));
			String ftInstitute = StringUtil.obj2Str(jobEduMap.get("A005040")); //毕业院校
			String ftSpecialtyCode = StringUtil.obj2Str(jobEduMap.get("A005010_A")); //专业信息代码
            String ftSpecialty = StringUtil.obj2Str(jobEduMap.get("A005010_B")); //专业信息名称
            if (!StringUtils.equals(ftSpecialty,"")){
                ftSpecialty = ftSpecialty.endsWith("专业")?ftSpecialty:(ftSpecialty+"专业");
            }else if(StringUtil.isNotEmptyOrNull(ftSpecialtyCode)){
                ftSpecialty = CodeTranslateUtil.codeToName("BT0055", ftSpecialtyCode, exportWordService);
            	ftSpecialty = ftSpecialty.endsWith("专业")?ftSpecialty:(ftSpecialty+"专业");
            }
            String yxzy=ftInstitute + ftSpecialty;
            if(ftInstituteAndSpecialty.indexOf(yxzy)<0) {
            	ftInstituteAndSpecialty =ftInstituteAndSpecialty+(char)11+ yxzy;
            }
            String xwPre=A005050A.substring(0, 1);
            for(int i=1;i<degree.size();i++) {
            	Map o=degree.get(i);
            	String xw=StringUtil.obj2Str(o.get("A005050_A"));
            	if(xw.startsWith(xwPre)&&!xw.equals(A005050A)) {
            		String yx=StringUtil.obj2Str(o.get("A005040"));
            		String zydm=StringUtil.obj2Str(o.get("A005010_A"));
            		String zymc=StringUtil.obj2Str(o.get("A005010_B"));
        			if (!StringUtils.equals(zymc,"")){
        				zymc = zymc.endsWith("专业")?zymc:(zymc+"专业");
                    }else if(StringUtil.isNotEmptyOrNull(zydm)){
                    	zymc = CodeTranslateUtil.codeToName("BT0055", zydm, exportWordService);
                    	zymc = zymc.endsWith("专业")?zymc:(zymc+"专业");
                    }
        			String yxzy1=yx+zymc;
        			if(ftInstituteAndSpecialty.indexOf(yxzy)<0) {
                    	ftInstituteAndSpecialty =ftInstituteAndSpecialty+(char)11+ yxzy1;
                    }
            	}
            }
		}

        return ftInstituteAndSpecialty;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String getTitle() {
        return "学历学位--毕业院校系及专业";
    }
}
