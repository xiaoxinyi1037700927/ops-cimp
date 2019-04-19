package com.sinosoft.ops.cimp.export.data;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sinosoft.ops.cimp.export.common.ExportConstant;
import com.sinosoft.ops.cimp.export.common.EnumType;
import com.sinosoft.ops.cimp.export.common.SetList;
import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by SML
 * date : 2017/11/10
 * des : 对简历的任职机构和职务进行处理
 */
public class ResumeOrganHandle {


    public static void main(String[] args) throws Exception {


        List<String> strings = new ArrayList<>();
        strings.add("贵州省黔西县委统战部" + EnumType.UNIT_JOB_SPLIT.value + "副部长");
        strings.add("贵州省黔西县工商业联合会" + EnumType.UNIT_JOB_SPLIT.value + "党组成员");
        strings.add("贵州省黔西县工商业联合会" + EnumType.UNIT_JOB_SPLIT.value + "党组书记");
//        strings.add("贵州省毕节市人民检察院正县级检察员");
//        strings.add("贵州省毕节市委政法委员会常务副书记");
//        strings.add("贵州省毕节市综治办主任");

//        List list = comBineByDutyRule(strings);
//        Map map = comBineStartSameOrg(strings);

        System.out.println(combineOrganAndDuty(strings));

//        System.out.println(EnumType.UNIT_JOB_SPLIT.value);


    }


    /**
     * 合并机构和职务
     */
    public static List<String> combineOrganAndDuty(List orgList) throws Exception {
        SetList<String> result = new SetList<>();
        if (orgList == null || orgList.size() <= 1) {
            removeSpecialStr(orgList);
            return orgList;
        }
//        List list = comBineByDutyRule(orgList);
        Map<String, List> map = comBineStartSameOrgTemp(orgList);
        for (Map.Entry<String, List> entry : map.entrySet()) {
            result.add(entry.getKey() + String.join("、", entry.getValue()));
        }
        //ReSetResult(result);
        removeSpecialStr(result);
        return result;
    }

    //合并List中相同的数据
    private static void ReSetResult(List<String> list) {
        if (list != null && list.size() > 0) {
            for (int i = list.size() - 1; i > 0; i--) {
                String temp = list.get(i);
                String tempnext = list.get(i - 1);
                String samestring = compareStrMaxSame(temp, tempnext);
//				if (samestring.indexOf("镇") > 0) {
//					samestring=samestring.substring(0, samestring.indexOf("镇"));
//				} else if (samestring.indexOf("县") > 0) {
//					samestring=samestring.substring(0, samestring.indexOf("县"));
//				} else if (samestring.indexOf("区") > 0) {
//					samestring=samestring.substring(0, samestring.indexOf("区"));
//				} else if (samestring.indexOf("市") > 0) {
//					samestring=samestring.substring(0, samestring.indexOf("市"));
//				} else if (samestring.indexOf("省") > 0) {
//					samestring=samestring.substring(0, samestring.indexOf("省"));
//				}
                if (samestring.length() > 1) {
                    if (samestring.endsWith("省")) {
                        list.set(i, "省" + temp.replaceAll(samestring, ""));
                    } else if (samestring.endsWith("市")) {
                        list.set(i, "市" + temp.replaceAll(samestring, ""));
                    } else if (samestring.endsWith("区")) {
                        list.set(i, "区" + temp.replaceAll(samestring, ""));
                    } else if (samestring.endsWith("县")) {
                        list.set(i, "县" + temp.replaceAll(samestring, ""));
                    } else if (samestring.endsWith("镇")) {
                        list.set(i, "镇" + temp.replaceAll(samestring, ""));
                    } else if (samestring.endsWith("乡")) {
                        list.set(i, "乡" + temp.replaceAll(samestring, ""));
                    } else {
                        list.set(i, temp.replaceAll(samestring, ""));
                    }
                }
                list.set(i, list.get(i).replaceAll("人民政府", "").replaceAll("市市", "市").replaceAll("市副市长", "副市长").replaceAll("县县", "县").replaceAll("县副县长", "副县长").replaceAll("镇镇", "镇").replaceAll("镇副镇长", "副镇长"));
            }
        }
    }

    private static void removeSpecialStr(List<String> list) {
        if (list != null && list.size() > 0) {
            List<String> delKeys = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).contains(EnumType.UNIT_JOB_SPLIT.value)) {
                    continue;
                }
                list.add(list.get(i).replace(EnumType.UNIT_JOB_SPLIT.value, ""));
                delKeys.add(list.get(i));
            }
            list.removeAll(delKeys);
        }
    }

    /**
     * 所任职务按词库合并
     *
     * @param list
     * @return
     * @throws Exception
     */
    private static List<String> comBineByDutyRule(List<String> list) throws Exception {
        JSONObject jsonObject = getJsonFromText();
        if (jsonObject == null) {
            return list;
        }
        SetList<String> results = new SetList<>();
        for (Map.Entry json : jsonObject.entrySet()) {
            String curDuty = String.valueOf(json.getKey());
            for (String s : list) {
                String job = "";
                try {
                    job = s.split(EnumType.UNIT_JOB_SPLIT.value)[1];
                } catch (Exception e) {
//                    e.printStackTrace();
                    continue;
                }
                if (StringUtils.equals(curDuty, job)) {
                    JSONArray jsonArray = jsonObject.getJSONArray(curDuty);
                    SetList relationDuties = handleRelationDuty(jsonArray, list, s);

                    results.add(relationDuties.size() > 0 ? (s + "、" + String.join("、", relationDuties)) : s);
                    break;
                }
            }
        }

        //添加未满足条件处理机构
        results.addAll(StringUtil.listDeepCopy(list));
        return results;
    }


    private static JSONObject getJsonFromText() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream("organization.json");
        InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(reader);

        StringBuilder stringBuilder = new StringBuilder();
        String jsonStr = "";
        try {
            while ((jsonStr = bufferedReader.readLine()) != null) {
                stringBuilder.append(jsonStr);
            }
            inputStream.close();

            return JSON.parseObject(stringBuilder.toString());
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("按所任职务根据对应的词库进行合并失败！");
        }

        return null;
    }

    /**
     * 处理第二关系职务
     *
     * @param src
     * @param target
     * @return
     */
    private static SetList<String> handleRelationDuty(JSONArray src, List<String> target, String tarDuty) {
        SetList<String> setList = new SetList<>();

        Iterator iterator = src.iterator();
        while (iterator.hasNext()) {
            String curDuty = String.valueOf(iterator.next());
            List<String> hasHandleKey = new ArrayList<>();
            hasHandleKey.add(tarDuty);
            for (String s : target) {
                if (s.endsWith(curDuty)) {
                    setList.add(curDuty);
                    hasHandleKey.add(s);
                }
            }
            target.removeAll(hasHandleKey);
        }
        return setList;
    }


    /**
     * 合并机构相同部分 从第一个字符开始
     *
     * @param listStr
     * @return
     * @throws Exception
     */
    private static Map<String, SetList> comBineStartSameOrgTemp(List<String> listStr) throws Exception {
        //返回结果
        Map<String, SetList> resMap = new LinkedHashMap<>();

        if (listStr == null || listStr.size() <= 1) {
            SetList setList = new SetList();
            setList.addAll(StringUtil.listDeepCopy(listStr));
            resMap.put("", setList);
            return resMap;
        }

        /*****处理机构完全相同的机构*****/
        Map<String, String> orgJobs = new LinkedHashMap<>();
        for (String curstr : listStr) {
            String org = "";
            String job = "";
            try {
                org = curstr.split(EnumType.UNIT_JOB_SPLIT.value)[0];
                job = curstr.split(EnumType.UNIT_JOB_SPLIT.value)[1];
            } catch (Exception e) {
                orgJobs.put(curstr, "");
                continue;
            }
            String res = orgJobs.get(org);
            if (res == null) {
                orgJobs.put(org, job);
            } else {
                if (org.endsWith(ExportConstant.RESUME_SPECIAL_AREA_CITY)) {
                    orgJobs.put(org, res + "、" + ExportConstant.RESUME_SPECIAL_AREA_CITY + job);
                } else if (org.endsWith(ExportConstant.RESUME_SPECIAL_AREA_REGION)) {
                    orgJobs.put(org, res + "、" + ExportConstant.RESUME_SPECIAL_AREA_REGION + job);
                } else if (org.endsWith(ExportConstant.RESUME_SPECIAL_AREA_COUNTY)) {
                    orgJobs.put(org, res + "、" + ExportConstant.RESUME_SPECIAL_AREA_COUNTY + job);
                } else {
                    orgJobs.put(org, res + "、" + job);
                }
            }
        }

        List<String> orgJobList = new ArrayList<>(orgJobs.keySet());
        for (String s : orgJobList) {
            SetList setList = new SetList();
            setList.add(orgJobs.get(s) != null ? orgJobs.get(s) : "");
            resMap.put(s, setList);
        }

        return resMap;
    }

    /**
     * 合并机构相同部分 从第一个字符开始
     *
     * @param listStr
     * @return
     * @throws Exception
     */
    private static Map<String, SetList> comBineStartSameOrg(List<String> listStr) throws Exception {
        //返回结果
        TreeMap<String, SetList> resMap = new TreeMap<>();

        if (listStr == null || listStr.size() <= 1) {
            SetList setList = new SetList();
            setList.addAll(StringUtil.listDeepCopy(listStr));
            resMap.put("", setList);
            return resMap;
        }


        /*****处理机构完全相同的机构*****/
        TreeMap<String, String> orgJobs = new TreeMap<>();
        for (String curstr : listStr) {
            String org = "";
            String job = "";
            try {
                org = curstr.split(EnumType.UNIT_JOB_SPLIT.value)[0];
                job = curstr.split(EnumType.UNIT_JOB_SPLIT.value)[1];
            } catch (Exception e) {
                orgJobs.put(curstr, "");
                continue;
            }
            String res = orgJobs.get(org);
            if (res == null) {
                orgJobs.put(org, job);
            } else {
                if (org.endsWith(ExportConstant.RESUME_SPECIAL_AREA_CITY)) {
                    orgJobs.put(org, res + "、" + ExportConstant.RESUME_SPECIAL_AREA_CITY + job);
                } else if (org.endsWith(ExportConstant.RESUME_SPECIAL_AREA_REGION)) {
                    orgJobs.put(org, res + "、" + ExportConstant.RESUME_SPECIAL_AREA_REGION + job);
                } else if (org.endsWith(ExportConstant.RESUME_SPECIAL_AREA_COUNTY)) {
                    orgJobs.put(org, res + "、" + ExportConstant.RESUME_SPECIAL_AREA_COUNTY + job);
                } else {
                    orgJobs.put(org, res + "、" + job);
                }
            }
        }

        //处理机构部分相同需要合并的机构
        List<String> hasHandel = new ArrayList<>(); //存放已经处理的机构，防止重复处理
        List<String> orgJobList = new ArrayList<>(orgJobs.keySet());
        for (String curstr : orgJobList) { //第一层
            if (StringUtils.isEmpty(curstr) || hasHandel.contains(curstr)) {
                continue;
            }
            String sameStr = "";
            for (String tarStr : orgJobList) { //第二层
                //判断这个机构是否已经处理
                if (StringUtils.equals(tarStr, curstr) || hasHandel.contains(tarStr)) {
                    continue;
                }

                sameStr = compareStrMaxSame(curstr, tarStr);
                int length = sameStr.length();
                if (!curstr.startsWith(sameStr) || length < 5) {
                    continue;
                }

                //加入已处理队列
                hasHandel.add(curstr);
                hasHandel.add(tarStr);


                //判断是否需要在职务前添加区市县
                String isAppendStr = "";
                if (sameStr.endsWith(ExportConstant.RESUME_SPECIAL_AREA_CITY)) {
                    isAppendStr = ExportConstant.RESUME_SPECIAL_AREA_CITY;
                } else if (sameStr.endsWith(ExportConstant.RESUME_SPECIAL_AREA_REGION)) {
                    isAppendStr = ExportConstant.RESUME_SPECIAL_AREA_REGION;
                } else if (sameStr.endsWith(ExportConstant.RESUME_SPECIAL_AREA_COUNTY)) {
                    isAppendStr = ExportConstant.RESUME_SPECIAL_AREA_COUNTY;
                }

                //判断resList中是否存在该机构
                SetList existOrg = resMap.get(sameStr);
                if (existOrg != null && !StringUtils.equals(tarStr.substring(length), "")) {
                    existOrg.add(isAppendStr + tarStr.substring(length) + orgJobs.get(tarStr));
                    resMap.put(sameStr, existOrg);
                    break;
                }
                SetList setList = new SetList();
                if (!StringUtils.equals(curstr.substring(length), "")) {
                    setList.add(curstr.substring(length) + orgJobs.get(curstr));
                }
                if (!StringUtils.equals(tarStr.substring(length), "")) {
                    setList.add(isAppendStr + tarStr.substring(length) + orgJobs.get(tarStr));
                }
                resMap.put(sameStr, setList);
                break;
            }
        }
        //处理机构没有相同部分的机构
        orgJobList.removeAll(hasHandel);
        for (String s : orgJobList) {
            resMap.put(s, new SetList());
        }
        return resMap;
    }


    private static String checkOrgExist(List<String> list, String sameStr) {
        for (String s : list) {
            if (s.startsWith(sameStr)) {
                return s;
            }
        }
        return null;
    }

    /**
     * 取两个字符串的最大交集
     *
     * @param src
     * @param target
     * @return
     */
    private static String compareStrMaxSame(String src, String target) {
        String resStr = target;
        int begin = 0;
        int end = target.length();
        int i = 1;
        while (!src.contains(resStr)) {
            if (end == target.length()) {
                begin = 0;
                end = (target.length()) - (i++);
            } else {
                begin++;
                end++;
            }
            resStr = target.substring(begin, end);
        }
        return resStr;
    }
}