package com.sinosoft.ops.cimp.util.word.pattern.xinjiang;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.newskysoft.iimp.word.util.StringUtil;

import java.io.IOException;
import java.util.*;

/**
 * Created by SML
 * date : 2017/11/6
 * des :
 */
public class ResumeAttrVauleHandle {
    private static final Logger logger = LoggerFactory.getLogger(ResumeAttrVauleHandle.class);
	public List resumeInfoList;
	public List importantActiveInfoList;
    public List eduInfoList;
    public List trainInfoList;
    public List jobInfoList;



    /**
     * 处理简历的结束日期为空的简历
     *
     * @param emptyDatekeys       结束日期为空的keys
     * @param jobGroupByStartTime
     */
    public void handResumeEndDateIsEmpty(List<String> emptyDatekeys, Map<String, List<Map>> jobGroupByStartTime) throws Exception {
        Map<String, List<Map>> endDateIsEmptyJob = new HashMap<>();

        //处理结束日期为空的增任职务
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
            listMap.put(keyT, new ArrayList<Map>());
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
             * 过滤掉不是全日制教育的学历信息
             */
            String A005025 = StringUtil.obj2Str(jobInfoMap.get("A005025"));  //教育类别
            if (StringUtils.isNotEmpty(A005025) && !StringUtils.equals(A005025, "1")) {
                continue;
            }

            String jobReason = StringUtil.obj2Str(jobInfoMap.get("A010020"));       //职务说明
            //挂职任职 DM004
            if (StringUtils.isNotEmpty(jobReason) && StringUtils.equals(jobReason, "71")) {
                continue;
            }

            Object jobStartDate = StringUtils.isNotEmpty(StringUtil.obj2Str(jobInfoMap.get("A070001"))) ? jobInfoMap.get("A070001") : jobInfoMap.get("A070001");     //决定或批准任职的日期
            Object jobEndDate = StringUtils.isNotEmpty(StringUtil.obj2Str(jobInfoMap.get("A070005"))) ? jobInfoMap.get("A070005") : jobInfoMap.get("A070005");     //决定或批准免职的日期

            DateTime jobStartDateTime = null;
            DateTime jobEndDateTime = null;
            if (jobStartDate != null) {
                try {
                    jobStartDateTime = new DateTime(jobStartDate);
                }catch(Exception e) {
                    logger.error("无效的日期："+jobStartDateTime,e);
                    jobStartDateTime = new DateTime();
                }
            }
            if (jobEndDate != null) {
                try {
                    jobEndDateTime = new DateTime(jobEndDate);
                }catch(Exception e) {
                    logger.error("无效的日期："+jobEndDateTime,e);
                    jobEndDateTime = new DateTime();
                }
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
     * @param professName 专业
     * @param degreeName  学历名称
     * @return
     */
    public boolean checkGetTwoRecordOneSchool(String schoolName, String professName, String degreeName, List<Map> eduList) {
        if (StringUtils.isEmpty(degreeName) || eduList == null || eduList.size() == 0) {
            return false;
        }

        for (Map eduMap : eduList) {
            String name = StringUtil.obj2Str(eduMap.get("A005040"));  //学校
            String eduName = StringUtil.obj2Str(eduMap.get("A005001B"));   //学历名称
            String A005010B = StringUtil.obj2Str(eduMap.get("A005010B"));      //所学专业名称

            if (StringUtils.equals(eduName, degreeName)) {
                continue;
            }
            if (StringUtils.equals(schoolName, name) &&
                    StringUtils.equals(professName, A005010B)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 按经历的日期排序
     */
    public List<Map.Entry<String, List<Map>>> sortByResumeDate(Map<String, List<Map>> jobInfoList) {

        List<Map.Entry<String, List<Map>>> list = new ArrayList<>(jobInfoList.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, List<Map>>>() {
            @Override
            public int compare(Map.Entry<String, List<Map>> o1, Map.Entry<String, List<Map>> o2) {
                	//获取时间
                    String date1 = o1.getKey().split("_")[0];
                    String date2 = o2.getKey().split("_")[0];
                    if(date1.isEmpty()){
                    	return -1;
                    }
                    if(date2.isEmpty()){
                    	return 1;
                    }
                    if (new DateTime(date1.replace(".", "-")).getMillis() < new DateTime(date2.replace(".", "-")).getMillis()) {
                        return -1;
                    } else {
                        return 1;
                    }
            }
        });

        return list;
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
            try{
            	long curStart;
            	if(!curStartTime.isEmpty()){
            	    try {
            	        curStart = new DateTime(curStartTime.replace(".", "-")).getMillis();
            	    }catch(Exception e) {
                        logger.error("无效的日期："+curStartTime,e);
                        curStart = new DateTime().getMillis();
                    }
            	}else{
            		curStart = new DateTime().getMillis();
            	}
            	long start ;
            	if(!startTime.isEmpty()){
            	    try {
            	        start = new DateTime(startTime.replace(".", "-")).getMillis();
            	    }catch(Exception e) {
                        logger.error("无效的日期："+startTime,e);
                        start = new DateTime().getMillis();
                    }
            	}else{
            		start = new DateTime().getMillis();
            	}
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
                } else if (StringUtils.equals(endTime, "") && StringUtils.equals(cuEndTime, "")) { //结束日期为空
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
            }catch(Exception e){
                logger.error("handelDateSameAndAdd",e);
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

	public List getResumeInfoList() {
		return resumeInfoList;
	}

	public void setResumeInfoList(List resumeInfoList) {
		this.resumeInfoList = resumeInfoList;
	}

	public List getImportantActiveInfoList() {
		return importantActiveInfoList;
	}

	public void setImportantActiveInfoList(List importantActiveInfoList) {
		this.importantActiveInfoList = importantActiveInfoList;
	}
    
}
