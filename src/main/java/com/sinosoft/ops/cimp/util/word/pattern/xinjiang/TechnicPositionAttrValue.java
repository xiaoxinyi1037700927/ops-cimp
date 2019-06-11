package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.service.word.ExportService;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.Map.Entry;



/**
 * Created by rain chen on 2017/10/9.
 * 专业技术职务
 * @author rain chen
 * @version 1.0
 * @since 1.0
 * Copyright (C) 2017. SinSoft All Rights Received
 */
public class TechnicPositionAttrValue implements AttrValue {

    private final String key = "technicPosition";
    private final int order = 9;
    
    @Override
    public Object getAttrValue(Map<String, Object> attrValueContext, String empId,ExportService exportWordService){
        final String technicPositionInfoSql = "SELECT * FROM Emp_A105 Emp_A105 WHERE EMP_ID = '%s'  and status = 0 ORDER BY Emp_A105.A105015 ASC, Emp_A105.A105010 DESC";
        //System.out.println("-----------000000000----------"+empId);
        List<Map<String, Object>> techPositionList = exportWordService.findBySQL(String.format(technicPositionInfoSql, empId));
        //wft 新疆   专业技术资格等级代码:BT0240
        //使用码表 代码类型为：GB/T12407-2008
        // 410 高级
        // 411 正高级
        // 412 副高级
        // 420 中级
        // 430 初级
        // 434 助理级
        // 435 员级
        // 499 未定职级专业技术人员
        //获取最高职级职称

        if (techPositionList != null && techPositionList.size() > 0) {
            attrValueContext.put("A105", techPositionList);
            Map techPosition = (Map) techPositionList.get(0);
            Integer a06002 = NumberUtils.toInt(StringUtil.obj2Str(techPosition.get("A105015")));
           /* if (a06002 < 420) {
                return techPosition.get("A105001_B");
            } else {
                return "";
            }*/
		    
            //同系列只显示最高的，不同系列显示系列内最高的---- 20190520黄玉石 
            ////wft 20180314  任免表中如果存在两个同等级的专业技术职务，则均需要显示出来
            Map<String,String> countMap=new HashMap<>();
            Map<Integer,String> map=new HashMap<Integer,String>();//key:一条专业技术职务的map，value:A105001_A的最后一位
            for(int i=0;i<techPositionList.size();i++){
                Object obj = techPositionList.get(i).get("A105001_A");
                if(obj==null) {
                    continue;
                }
                SysCodeItem sci=exportWordService.getCodeItemByCode("BT0230", obj.toString());
                String pptr=  sci.getParentCode();
                if(obj!=null) {
                	//改为取A105001_A的父代码  黄玉石 20190520  ////专业技术职务代码A105001_A的前两位 --
                	if(countMap.containsKey(pptr)) {
                		String val=countMap.get(pptr)+","+Integer.valueOf(i).toString();
                		countMap.put(pptr,val);
                	}else {
                		countMap.put(pptr,Integer.valueOf(i).toString());
                	}
                    map.put(Integer.valueOf(i), obj.toString().substring(obj.toString().length()-1));
                }
            }
            
            List<Entry<Integer,String>> list = new ArrayList<Entry<Integer,String>>(map.entrySet());
            Collections.sort(list,new Comparator<Entry<Integer,String>>() {
                //升序排序  
            	@Override
                public int compare(Entry<Integer, String> o1,  
                        Entry<Integer, String> o2) {  
                    return o1.getValue().compareTo(o2.getValue());  
                }  
            });
            if(countMap.size()==1){//只有一个类别
            	 Entry<Integer,String> highEntry = list.get(0);//尾数最小的就是最高级别
                 //String mantissa=highEntry.getValue();
   	             return ((Map)techPositionList.get(highEntry.getKey())).get("A105001_B")==null?"":((Map)techPositionList.get(highEntry.getKey())).get("A105001_B").toString();
            }
            else if(countMap.size()>1){//多个类别,级别最高的有多个都输出
//	            Map.Entry<Integer,String> highEntry = list.get(0);//尾数最小的就是最高级别
//	            String mantissa=highEntry.getValue();
//	            String valueTypes=((Map)techPositionList.get(highEntry.getKey())).get("A105001_B")==null?"":((Map)techPositionList.get(highEntry.getKey())).get("A105001_B").toString();
//	            for(int i=1;i<list.size();i++){
//	            	if(mantissa.equals(list.get(i).getValue())){
//	            		valueTypes+=((Map)techPositionList.get(highEntry.getKey())).get("A105001_B")==null?"":((Map)techPositionList.get(highEntry.getKey())).get("A105001_B")+" ";
//	            	}else{
//	            		break;
//	            	}
//	            }
            	Map<String,String> has=new HashMap<>();
            	String valueTypes="";
	            for(int i=0;i<list.size();i++){
	            	for (Entry<String, String> entry : countMap.entrySet()) {
	            		String[] idArr= entry.getValue().split(",");
	            		if(Arrays.asList(idArr).contains(String.valueOf(i))&&!has.containsKey(entry.getKey())) {
	            			has.put(entry.getKey(), "");
	            			valueTypes+=((Map)techPositionList.get(i)).get("A105001_B")==null?"":((Map)techPositionList.get(i)).get("A105001_B")+"、";
	            			break;
	            		}
	            	}
	            }
	            if(valueTypes.length()>0) {
	            	return valueTypes.substring(0,valueTypes.length()-1);
	            }
	            else {
	            	return "";
	            }
            }
            else{
            	return "";
            }
        } else {
            attrValueContext.put("A105", new ArrayList<Map>());
        }
        return "";
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
        return "专业技术职务";
    }
}
