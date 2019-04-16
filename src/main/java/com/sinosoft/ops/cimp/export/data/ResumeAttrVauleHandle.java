package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by SML
 * date : 2017/11/6
 * des :
 */
public class ResumeAttrVauleHandle extends ResumeOrganHandle {

    public List eduInfoList;
    public List trainInfoList;
    public List jobInfoList;
    public List professionalInfoList;

    /**
     * 处理简历的结束日期为空的简历
     *
     * @param emptyDatekeys       结束日期为空的keys
     * @param jobGroupByStartTime
     */
    public void handResumeEndDateIsEmpty(List<String> emptyDatekeys, Map<String, List<Map>> jobGroupByStartTime) throws Exception {
        Map<String, List<Map>> endDateIsEmptyJob = new HashMap<>();

        //处理增任职务
        handelDateSameAndAdd(jobGroupByStartTime, jobGroupByStartTime);

        for (Map.Entry<String, List<Map>> entry : jobGroupByStartTime.entrySet()) {
            String key = entry.getKey();
            String[] startAndEndStrs = entry.getKey().split("_");
            String startTime = startAndEndStrs[0];
            String endTime = "";
            if (startAndEndStrs.length > 1) {
                endTime = startAndEndStrs[1];
            }
            //处理结束日期为空的
            if (StringUtils.equals(endTime, "")) {
                int index = emptyDatekeys.indexOf(key);
                String endKey = "";
                try {
                    endKey = emptyDatekeys.get(index + 1);
                    endKey = endKey.substring(0, endKey.length() - 1);

                    List<Map> mapList = StringUtil.listDeepCopy(jobGroupByStartTime.get(key + endKey));
                    if (mapList != null) {
                        mapList.addAll(entry.getValue());
                        endDateIsEmptyJob.put(key + endKey, mapList);
                    } else {
                        endDateIsEmptyJob.put(key + endKey, entry.getValue());
                    }
                } catch (Exception e) {
                    //最后一个日期
                    endDateIsEmptyJob.put(key, entry.getValue());
//                    e.printStackTrace();
                }
            }
        }

        //清除结束为空的key
        for (String key : emptyDatekeys) {
            jobGroupByStartTime.remove(key);
        }
        //合并map
        jobGroupByStartTime.putAll(endDateIsEmptyJob);

    }


    public Map<String, List<Map>> handelResumeDateCorss(Map<String, List<Map>> jobGroupByStartTime) {
        Map<String, List<Map>> listMap = new HashMap<>();

        List<String> dateList = getAllResumeDates(jobGroupByStartTime);
        List<String> sameList = getSameDayDates(jobGroupByStartTime);
        if (dateList == null || dateList.size() == 0) {
            return listMap;
        }
        for (int i = 0; i < dateList.size(); i++) {
            String start = dateList.get(i);
            String end = null;
            try {
                end = dateList.get(i + 1);
            } catch (Exception e) {
//                e.printStackTrace();
            }
            String keyT = start + "_";

            if (end != null) {
                keyT = keyT + end;
            }
            listMap.put(keyT, new ArrayList<>());
        }
        for (int i = 0; i < sameList.size(); i++) {
            String start = sameList.get(i);
            String keyT = start + "_" + start;
            listMap.put(keyT, new ArrayList<>());
        }
        return listMap;
    }

    /**
     * 获取简历所有的日期
     *
     * @param jobGroupByStartTime
     * @return
     */
    public List<String> getAllResumeDates(Map<String, List<Map>> jobGroupByStartTime) {

        TreeSet<String> treeSet = new TreeSet<>();
        for (Map.Entry<String, List<Map>> entry : jobGroupByStartTime.entrySet()) {
            String[] startAndEndStrs = entry.getKey().split("_");
            String startTime = startAndEndStrs[0];
            String endTime = "";
            if (startAndEndStrs.length > 1) {
                endTime = startAndEndStrs[1];
            }

            if (StringUtils.isNotEmpty(endTime)) {
                treeSet.add(endTime);
            }

            treeSet.add(startTime);
        }
        return new ArrayList<>(treeSet);
    }

    /**
     * 获取简历任免时间相同的数据
     *
     * @param jobGroupByStartTime
     * @return
     */
    public List<String> getSameDayDates(Map<String, List<Map>> jobGroupByStartTime) {

        TreeSet<String> treeSet = new TreeSet<>();
        for (Map.Entry<String, List<Map>> entry : jobGroupByStartTime.entrySet()) {
            String[] startAndEndStrs = entry.getKey().split("_");
            String startTime = startAndEndStrs[0];
            String endTime = "";
            if (startAndEndStrs.length > 1) {
                endTime = startAndEndStrs[1];
            }
            if (StringUtils.isNotEmpty(endTime)) {
                if (startTime.equals(endTime))
                    treeSet.add(endTime);
            }
        }
        return new ArrayList<>(treeSet);
    }

    /**
     * 把简历的日期转换成Map
     * <p>
     * key 为起始日期与结束日期的 规则：yyyy.MM_yyyyMM
     * <p>
     * value 为一个List 存放 简历的详细记录
     *
     * @param jobGroupByStartTime
     * @param jobInfoList
     * @param emptyDateList
     */
    public void handleDateToKey(Map<String, List<Map>> jobGroupByStartTime, List jobInfoList, List<String> emptyDateList) {
        for (Object aJobInfo : jobInfoList) {

            Map jobInfoMap = (Map) aJobInfo;
            /**
             * 过滤掉不是   (全日制教育的学历信息,最高学历标识为是)其他信息
             */
            String A08020 = StringUtil.obj2Str(jobInfoMap.get("A08020"));//教育类别
            if (StringUtils.isNotEmpty(A08020) && !StringUtils.equals(A08020, "1")) {
                continue;
            }

            String jobReason = StringUtil.obj2Str(jobInfoMap.get("A02049"));       //职务说明
            //挂职任职 DM004
            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "4")) {
                continue;
            }

            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "41")) {
                continue;
            }

            // shixianggui-20180312, bug327: 简历中借用借调存在拆分原有任职经历
            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "42")) {
                continue;
            }

            // shixianggui-20180314, 赵科提出bug: 简历中 43-跟班学习  存在拆分原有任职经历问题
            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "43")) {
                continue;
            }

            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "44")) {
                continue;
            }

            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "45")) {
                continue;
            }

            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "46")) {
                continue;
            }

            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "47")) {
                continue;
            }

            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "48")) {
                continue;
            }

//            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "9")) {
//                continue;
//            }

            Object jobStartDate = StringUtils.isNotEmpty(StringUtil.obj2Str(jobInfoMap.get("A02043"))) ? jobInfoMap.get("A02043") : jobInfoMap.get("A08004");     //决定或批准任职的日期
            Object jobEndDate = StringUtils.isNotEmpty(StringUtil.obj2Str(jobInfoMap.get("A02065"))) ? jobInfoMap.get("A02065") : jobInfoMap.get("A08007");     //决定或批准免职的日期

            DateTime jobStartDateTime = null;
            DateTime jobEndDateTime = null;
            if (jobStartDate != null) {
                jobStartDateTime = new DateTime(jobStartDate);
            }
            if (jobEndDate != null) {
                jobEndDateTime = new DateTime(jobEndDate);
            }

            String key = "";
            if (jobStartDateTime != null) {
                key = jobStartDateTime.toString("yyyy.MM");
            }
            if (jobEndDateTime != null) {
                key = key + "_" + jobEndDateTime.toString("yyyy.MM");
            } else {
                key = key + "_";
                if (!emptyDateList.contains(key)) {
                    emptyDateList.add(key);
                }
            }

            List<Map> listMaps = jobGroupByStartTime.get(key);
            if (listMaps == null) {
                List temp = new ArrayList<Map>();
                temp.add(jobInfoMap);
                jobGroupByStartTime.put(key, temp);
            } else {  //说明任职日期有相同时间的
                listMaps.add(jobInfoMap);
                jobGroupByStartTime.put(key, listMaps);
            }
        }
        //过滤无效的key
        jobGroupByStartTime.remove("_");
        emptyDateList.remove("_");
    }

    /**
     * 判断是否是同一学校同一系同一专业先后取得不同层次学历的
     *
     * @param schoolName  学校
     * @param departName  系
     * @param professName 专业
     * @param degreeName  学历名称
     * @return
     */
    public boolean checkGetTwoRecordOneSchool(String schoolName, String departName, String professName, String degreeName, List<Map> eduList) {
        if (StringUtils.isEmpty(degreeName) || eduList == null || eduList.size() == 0) {
            return false;
        }

        for (Map eduMap : eduList) {
            String name = StringUtil.obj2Str(eduMap.get("A08014"));  //学校
            String eduName = StringUtil.obj2Str(eduMap.get("A08002_A"));   //学历名称
            String a08024 = StringUtil.obj2Str(eduMap.get("A08024"));      //所学专业名称
            String a08016 = StringUtil.obj2Str(eduMap.get("A08016"));      //院系名称

            if (StringUtils.equals(eduName, degreeName)) {
                continue;
            }
            if (StringUtils.equals(schoolName, name) &&
                    StringUtils.equals(departName, a08016) &&
                    StringUtils.equals(professName, a08024)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 按经历的日期排序
     */
    public List<Map.Entry<String, List<Map>>> sortByResumeDate(Map<String, List<Map>> jobInfoList) {

        return jobInfoList.entrySet().stream().sorted(new Comparator<Map.Entry<String, List<Map>>>() {
            @Override
            public int compare(Map.Entry<String, List<Map>> o1, Map.Entry<String, List<Map>> o2) {
                //获取时间
                String date1 = o1.getKey().split("_")[0];
                String date2 = o2.getKey().split("_")[0];
                if (new DateTime(date1.replace(".", "-")).getMillis() < new DateTime(date2.replace(".", "-")).getMillis()) {
                    return -1;
                } else if (new DateTime(date1.replace(".", "-")).getMillis() > new DateTime(date2.replace(".", "-")).getMillis()) {
                    return 1;
                } else {
                    if (o1.getKey().split("_").length > 1 && o2.getKey().split("_").length == 1) {
                        return 1;
                    } else if (o1.getKey().split("_").length == 1 && o2.getKey().split("_").length > 1) {
                        return -1;
                    } else {
                        String enddate1 = o1.getKey().split("_")[1];
                        String enddate2 = o2.getKey().split("_")[1];
                        if (new DateTime(enddate1.replace(".", "-")).getMillis() < new DateTime(enddate2.replace(".", "-")).getMillis()) {
                            return -1;
                        } else if (new DateTime(enddate1.replace(".", "-")).getMillis() > new DateTime(enddate2.replace(".", "-")).getMillis()) {
                            return 1;
                        }
                    }
                    return 0;
                }
            }
        }).collect(Collectors.toList());
    }


    /**
     * 处理经历增任职务
     * <p>
     * 通过 经历的起始日期去遍历当前Map中的每个list, 如果有包含于此日期的，则说明该记录为增任职务
     *
     * @param jobGroupByStartTime 任职经历和学历信息
     * @return
     */
    public void handelDateSameAndAdd(Map<String, List<Map>> jobGroupByStartTime, Map<String, List<Map>> jobGroupOld) throws IOException, ClassNotFoundException {
        for (Map.Entry<String, List<Map>> entry : jobGroupByStartTime.entrySet()) {
            String key = entry.getKey();
            String[] startAndEndStrs = entry.getKey().split("_");
            String startTime = startAndEndStrs[0];
            String endTime = "";
            if (startAndEndStrs.length > 1) {
                endTime = startAndEndStrs[1];
            }
            handelDateSameAndAdd(jobGroupByStartTime, jobGroupOld, key, startTime, endTime);
        }
    }

    private void handelDateSameAndAdd(Map<String, List<Map>> jobGroupByStartTime, Map<String, List<Map>> jobGroupOld, String key, String curStartTime, String cuEndTime) throws IOException, ClassNotFoundException {
        for (Map.Entry<String, List<Map>> entry : jobGroupOld.entrySet()) {
            String[] startAndEndStrs = entry.getKey().split("_");
            String startTime = startAndEndStrs[0];
            String endTime = "";
            if (startAndEndStrs.length > 1) {
                endTime = startAndEndStrs[1];
            }
            long curStart = new DateTime(curStartTime.replace(".", "-")).getMillis();
            long start = new DateTime(startTime.replace(".", "-")).getMillis();
            if (!StringUtils.equals(endTime, "") && !StringUtils.equals(cuEndTime, "")) { //结束日期不为空
                long curEnd = new DateTime(cuEndTime.replace(".", "-")).getMillis();
                long end = new DateTime(endTime.replace(".", "-")).getMillis();
                //判断是否有增任职务
                if ((curStart >= start && curEnd <= end)) {
                    List<Map> list = StringUtil.listDeepCopy(jobGroupByStartTime.get(key));
                    if (list == null || list.size() == 0) {
                        jobGroupByStartTime.put(key, jobGroupOld.get(entry.getKey()));
                    } else {
                        list.addAll(jobGroupOld.get(entry.getKey()));
                        jobGroupByStartTime.put(key, list);
                    }
                }
            } else if ((StringUtils.equals(endTime, "") && StringUtils.equals(cuEndTime, "")) //两个结束日期为空
                    || (StringUtils.equals(endTime, "") && !StringUtils.equals(cuEndTime, ""))) { //目标结束日期为空，当前结束日期不为空
                if (curStart >= start) {
                    List<Map> list = StringUtil.listDeepCopy(jobGroupByStartTime.get(key));
                    if (list == null || list.size() == 0) {
                        jobGroupByStartTime.put(key, jobGroupOld.get(entry.getKey()));
                    } else {
                        list.addAll(jobGroupOld.get(entry.getKey()));
                        jobGroupByStartTime.put(key, list);
                    }
                }
            }
        }
    }

    public List getEduInfoList() {
        return eduInfoList;
    }

    public void setEduInfoList(List eduInfoList) {
        this.eduInfoList = eduInfoList;
    }

    public List getTrainInfoList() {
        return trainInfoList;
    }

    public void setTrainInfoList(List trainInfoList) {
        this.trainInfoList = trainInfoList;
    }

    public List getJobInfoList() {
        return jobInfoList;
    }

    public void setJobInfoList(List jobInfoList) {
        this.jobInfoList = jobInfoList;
    }


    public List getProfessionalInfoList() {
        return professionalInfoList;
    }


    public void setProfessionalInfoList(List professionalInfoList) {
        this.professionalInfoList = professionalInfoList;
    }

}
