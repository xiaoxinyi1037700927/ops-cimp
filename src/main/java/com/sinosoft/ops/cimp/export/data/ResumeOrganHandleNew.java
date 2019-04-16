package com.sinosoft.ops.cimp.export.data;

import com.sinosoft.ops.cimp.export.common.EnumType;
import com.sinosoft.ops.cimp.util.ParticularUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ResumeOrganHandleNew {

	public static String main(List<String> organizationDutyList){
		try {
			// 根据机构隶属关系排序
			organizationDutyList = sortOrgBySubjectionRelation(organizationDutyList);
			
			// 根据 "任职机构性质类别" 排序
			organizationDutyList = sortOrganizationByDutyType(organizationDutyList);
			
			// 规则二
			organizationDutyList = secondRule(organizationDutyList);
			
			if(organizationDutyList == null){
				return "";
			}
			
			String firstOrganization = null;
			String[] previousOrganizationDutyArray = null;
			String[] currentOrganizationDutyArray = null;
			if(organizationDutyList.size() == 1){
				currentOrganizationDutyArray = organizationDutyList.get(0).split(EnumType.UNIT_JOB_SPLIT.value);
				if(currentOrganizationDutyArray.length == 1){
					return currentOrganizationDutyArray[0];
				} else if(currentOrganizationDutyArray.length >= 2){
					return currentOrganizationDutyArray[0] + currentOrganizationDutyArray[1];
				}
			}
			
			StringBuffer build = new StringBuffer();
			String caesuraSignOrComma = null;
			String buildOrganization = null;
			String firstRuleResult = null;// 规则一
			
			// [机构名称]unit_job_split[职务名称]unit_job_split[机构隶属关系code级别]unit_job_split[任职机构性质code类别]
			String previousOrganization = null;
			String previousDuty = null;
			String previousOrgSubjectionRelationCode = null; 
			String previousDutyType = null;
			String previousDepCode = null;
			String currentOrganization = null;
			String currentDuty = null;
			String currentOrgSubjectionRelationCode = null; 
			String currentDutyType = null;
			String currentDepCode = null;
			
			String[] analysisPCOrgNameArray = null;
			
			for(int i = 1; i < organizationDutyList.size(); i++){
				previousOrganizationDutyArray = organizationDutyList.get(i-1).split(EnumType.UNIT_JOB_SPLIT.value);
				currentOrganizationDutyArray = organizationDutyList.get(i).split(EnumType.UNIT_JOB_SPLIT.value);
				
				if(previousOrganizationDutyArray.length >= 5 && currentOrganizationDutyArray.length >= 5){
					previousOrganization = ParticularUtils.trim(previousOrganizationDutyArray[0], "");
					previousDuty = ParticularUtils.trim(previousOrganizationDutyArray[1], "");
					previousOrgSubjectionRelationCode = ParticularUtils.trim(previousOrganizationDutyArray[2], "");
					previousDutyType = ParticularUtils.trim(previousOrganizationDutyArray[3], "");
					previousDepCode = ParticularUtils.trim(previousOrganizationDutyArray[4], "");
					
					currentOrganization = ParticularUtils.trim(currentOrganizationDutyArray[0], "");
					currentDuty = ParticularUtils.trim(currentOrganizationDutyArray[1], "");
					currentOrgSubjectionRelationCode = ParticularUtils.trim(currentOrganizationDutyArray[2], "");
					currentDutyType = ParticularUtils.trim(currentOrganizationDutyArray[3], "");
					currentDepCode = ParticularUtils.trim(currentOrganizationDutyArray[4], "");
					
//					analysisPCOrgNameArray = analysisPreviousAndCurrentOrgNameToArray(previousOrganization, currentOrganization);
					analysisPCOrgNameArray = prepareAnalysisPCOrgNameArray(previousOrganization, currentOrganization);
					
				}else{
					if(previousOrganizationDutyArray.length == 1){
						build.append(previousOrganizationDutyArray[0]);
					} else if(previousOrganizationDutyArray.length >= 2){
						build.append(previousOrganizationDutyArray[0] + previousOrganizationDutyArray[1]);
					}
					
					if(i == organizationDutyList.size()-1){
						build.append(", ");
						if(currentOrganizationDutyArray.length == 1){
							build.append(currentOrganizationDutyArray[0]);
						} else if(currentOrganizationDutyArray.length >= 2){
							build.append(currentOrganizationDutyArray[0] + currentOrganizationDutyArray[1]);
						}
					}
					continue;
				}
				
				if(i == 1){
					firstOrganization = previousOrganization;
					build.append(previousOrganization).append(previousDuty);
				}
				
				// 1).构建分割号: 顿号(CaesuraSign)/逗号(Comma)
				caesuraSignOrComma = buildCaesuraSignOrComma(previousOrganization, previousDuty, previousOrgSubjectionRelationCode, previousDutyType, previousDepCode, currentOrganization, currentDuty, currentOrgSubjectionRelationCode, currentDutyType, currentDepCode);
				if(isCharacterMatchForCommon(caesuraSignOrComma, CAESURA_SIGN)){
					build.append("、");
				}else if(COMMA.equals(caesuraSignOrComma)){
					build.append(", ");
				}
				
				// 2).构建机构名称
				buildOrganization = buildOrganization(caesuraSignOrComma, firstOrganization, 
						previousOrganization, previousOrgSubjectionRelationCode, previousDutyType, 
						currentOrganization, currentOrgSubjectionRelationCode, currentDutyType, analysisPCOrgNameArray);
				
				// 规则一
				if(previousOrgSubjectionRelationCode.equals(currentOrgSubjectionRelationCode)){
					firstRuleResult = firstRule(buildOrganization, firstOrganization, currentOrganization, currentDuty, currentOrgSubjectionRelationCode);
				}else{
					firstRuleResult = "";
				}
				
				if(!"".equals(firstRuleResult)){
					build.append(firstRuleResult);
					continue;
				}
				build.append(buildOrganization);
				
				// 3).构建职务名称
				build.append(buildDuty(currentDuty));
			}
			return build.toString();
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static final String CAESURA_SIGN = "CaesuraSign";
	private static final String CAESURA_SIGN_PREFIX_ORG = "CaesuraSignPrefixOrg";
	private static final String CAESURA_SIGN_ORG = "CaesuraSignOrg";
	private static final String COMMA = "Comma";
	private static final String CAESURA_SIGN_GET_COMMA_VALUE = "CaesuraSignGetCommaValue";// 返回顿号, 但获取但获取逗号规则
	
	private static Map<String, String[]> isCaesuraSignDataMap = new HashMap<>();
	static {
		
		/**
		 * 不论机构名称是否相同, 只要相邻两条简历的 "任职机构性质类别" 包含以下, 则是顿号分割.
		 * 101-党委班子(指中央、省、地、市、县、区、乡等党委班子)
		 * 121-政府班子(指中央、省、地、市、县、区、乡等政府班子)
		 * 131-人大班子(指中央、省、地、市、县、区等人大班子)
		 * 141-政协班子(指中央、省、市、县等政协班子)
		 */
		isCaesuraSignDataMap.put("dutyTypes", new String[]{"101", "121", "131", "141"});
		
		/**
		 * 简历前后序号不区分;
		 * 101-党委班子(指中央、省、地、市、县、区、乡等党委班子)
		 * 121-政府班子(指中央、省、地、市、县、区、乡等政府班子)
		 * 131-人大班子(指中央、省、地、市、县、区等人大班子)
		 * 141-政协班子(指中央、省、市、县等政协班子)
		 * 前一条简历的 "任职机构性质类别" 包含以上的类别, 
		 * 
		 * 且当前简历的 机构名称 包含 "组织部、统战部、宣传部、政法委、纪委、县委办公室、市委办公室、区委办公室"
		 * ||
		 * 133-人大部门党委
		 * 134-人大部门党组
		 * 123-政府部门党委
		 * 124-政府部门党组
		 * 143-政协部门党委
		 * 144-政协部门党组
		 */
		isCaesuraSignDataMap.put("dutyTypes133", new String[]{"133", "134", "123", "124", "143", "144"});
		isCaesuraSignDataMap.put("orgBeBasedOnDutyType", new String[]{"组织部", "统战部", "宣传部", "政法委", "纪委", "县委办公室", "市委办公室", "区委办公室", "人大常委会办公室"});
		
		isCaesuraSignDataMap.put("orgEq", new String[]{"党组", "党委"});

		/**
		 * 简历前后序号不区分 
		 * 前一条简历 机构名称 包含 "党工委",
		 * 且第二条简历 机构名称 包含 "管委会、组织部、政治部、纪工委"
		 */
		isCaesuraSignDataMap.put("previousOrgArray", new String[]{"党工委", "纪委", "组织部", "宣传部", "%人民政府"});// , "行署"
		isCaesuraSignDataMap.put("currentOrgArray", new String[]{"管委会", "组织部", "政治部", "纪工委", "监察", "统战部", "人才办"});//"政府办公室", "实绩考核办"[20180409 17:54]   "行署办公室", "党建办"
		
		/**
		 * 若是机构隶属关系为 "[72]村民委员会", "[71]居民委员会", "[70]居民、村民委员会", "[63]乡", "[62]镇", "[61]街道", [60]街道、镇、乡  时, 分割符号是 顿号
		 */
		isCaesuraSignDataMap.put("orgSubjectionRelationCode", new String[]{"72", "71", "70", "63", "62", "61", "60"});
	}
	
	private static Map<String, String[]> buildCaesuraSignPrefixOrgDataMap = new HashMap<>();
	static {
		/**************** "前缀+机构名称" ****************/
//		{"省委", "provincialPartyCommittee"},
//		{"市委", "municipalPartyCommittee"},
//		{"州委", "stateCommittee"},
//		{"县委", "countyPartyCommittee"},
//		{"区委", "districtCommittee"}
		buildCaesuraSignPrefixOrgDataMap.put("previousOrg_PrefixOrg", new String[]{"县委", "市委", "区委", "省委", "纪委", "团委", "%政府"});
		buildCaesuraSignPrefixOrgDataMap.put("previousDuty_PrefixOrg", new String[]{"书记", "常委", "秘书长"});
		buildCaesuraSignPrefixOrgDataMap.put("currentOrg_PrefixOrg", new String[]{"政府", "人大", "政协", "青联"});
		buildCaesuraSignPrefixOrgDataMap.put("currentDuty_PrefixOrg", new String[]{"书记", "党组", "成员"});
		
		
		/**************** "机构名称" ****************/
		buildCaesuraSignPrefixOrgDataMap.put("previousOrg_Org", new String[]{"纪委", "委员会", "街道党工委"});
		buildCaesuraSignPrefixOrgDataMap.put("previousDuty_Org", new String[]{"书记", "委员","常委"});
		buildCaesuraSignPrefixOrgDataMap.put("currentOrg_Org", new String[]{"监察局", "监察委员会", "纪委", "街道办事处"});
		buildCaesuraSignPrefixOrgDataMap.put("currentDuty_Org", new String[]{"局长", "主任","委员"});
		
	}
	
	/**
	 * CaesuraSign(顿号)/Comma(逗号)
	 */
	public static String buildCaesuraSignOrComma(
			String previousOrganization, String previousDuty, String previousOrgSubjectionRelationCode, String previousDutyType, String previousDepCode, 
			String currentOrganization, String currentDuty, String currentOrgSubjectionRelationCode, String currentDutyType, String currentDepCode){
		
		// 机构隶属关系code 相同
		if(previousOrgSubjectionRelationCode.equals(currentOrgSubjectionRelationCode)){
			boolean flag = true;
			
			// 机构名称相同
			if(flag && previousOrganization.equals(currentOrganization)){
				flag = false;
			}
			if(flag){
				for(String s : isCaesuraSignDataMap.get("orgEq")){
					if(previousOrganization.endsWith(s)){
						if(previousOrganization.equals(currentOrganization+s)){
							flag = false;
						}
					}else if(currentOrganization.endsWith(s)){
						if(currentOrganization.equals(previousOrganization+s)){
							flag = false;
						}
					}
				}
			}
			
			if(flag && isCharacterMatch(previousDutyType, isCaesuraSignDataMap.get("dutyTypes"))){
				if(flag && isCharacterMatch(currentDutyType, isCaesuraSignDataMap.get("dutyTypes"))){
					flag = false;
				}
				if(flag && (isCharacterMatch(previousDutyType, isCaesuraSignDataMap.get("orgBeBasedOnDutyType")) || isCharacterMatch(currentOrganization, isCaesuraSignDataMap.get("orgBeBasedOnDutyType")))){
					flag = false;
				}
				if(flag && (isCharacterMatch(previousDutyType, isCaesuraSignDataMap.get("dutyTypes133")) || isCharacterMatch(currentOrganization, isCaesuraSignDataMap.get("dutyTypes133")))){
					flag = false;
				}
			}
			
			if(flag 
					&& ((isCharacterMatch(previousOrganization, isCaesuraSignDataMap.get("previousOrgArray")) && isCharacterMatch(currentOrganization, isCaesuraSignDataMap.get("currentOrgArray")))
							||(isCharacterMatch(currentOrganization, isCaesuraSignDataMap.get("previousOrgArray")) && isCharacterMatch(previousOrganization, isCaesuraSignDataMap.get("currentOrgArray"))))){
				flag = false;
			}
			
			if(flag){
//				analysisPCOrgNameArray
//				if("0".equals(array[0])){
//					if(isCharacterMatch(array[5], organizationNameSuffixDataTest)){
//						flag = false;
//					}
//				}else if("1".equals(array[0])){
//					if(isCharacterMatch(array[3], organizationNameSuffixDataTest)){
//						flag = false;
//					}
//				}
				if(previousOrgSubjectionRelationCode.equals(currentOrgSubjectionRelationCode) 
						&& isCharacterMatch(previousOrgSubjectionRelationCode, isCaesuraSignDataMap.get("orgSubjectionRelationCode"))){
					flag = false;
				}
				
				// 根据 单位(机构)代码 来判断, 如果当前简历的机构 是 前一个简历机构的下属机构时, 使用顿号;
				if(!previousDepCode.equals(currentDepCode)){
					if (isCharacterMatchForCommon(previousDepCode, currentDepCode) || isCharacterMatchForCommon(currentDepCode, previousDepCode)) {
//						flag = false;
						return CAESURA_SIGN_GET_COMMA_VALUE;
					}
				}
			}
			
			if(!flag){
				return buildCaesuraSign(previousOrganization, previousDuty, currentOrganization, currentDuty);
			}
			
		}
		return COMMA;
	}
	public static String buildCaesuraSign(String previousOrganization, String previousDuty, String currentOrganization, String currentDuty){
		if(isCharacterMatch(previousOrganization, buildCaesuraSignPrefixOrgDataMap.get("previousOrg_PrefixOrg")) 
				&& isCharacterMatch(previousDuty, buildCaesuraSignPrefixOrgDataMap.get("previousDuty_PrefixOrg")) 
				&& isCharacterMatch(currentOrganization, buildCaesuraSignPrefixOrgDataMap.get("currentOrg_PrefixOrg"))
				&& isCharacterMatch(currentDuty, buildCaesuraSignPrefixOrgDataMap.get("currentDuty_PrefixOrg"))
				){
			return CAESURA_SIGN_PREFIX_ORG;
		}
		if(isCharacterMatch(previousOrganization, buildCaesuraSignPrefixOrgDataMap.get("previousOrg_Org")) 
				&& isCharacterMatch(previousDuty, buildCaesuraSignPrefixOrgDataMap.get("previousDuty_Org")) 
				&& isCharacterMatch(currentOrganization, buildCaesuraSignPrefixOrgDataMap.get("currentOrg_Org"))
				&& isCharacterMatch(currentDuty, buildCaesuraSignPrefixOrgDataMap.get("currentDuty_Org"))
				){
			
			return CAESURA_SIGN_ORG;
		}
		return CAESURA_SIGN;
	}
	
	public static boolean isCharacterMatch(String targetStr, String[] matchCharArray){
		for(String matchChar : matchCharArray){
			if(isCharacterMatchForLogicalOperator(targetStr, matchChar)){
				return true;
			}
		}
		return false;
	}
	public static boolean isCharacterMatchForPriority(String targetStr, String matchChar){
		if(matchChar.indexOf("(") >= 0){
			String[] array = matchChar.split("&&");
			int bracketLeft = 0;
			int bracketRight = 0;
			for(String s : array){
				if((bracketLeft=s.indexOf("(")) >= 0){
					if((bracketRight=s.indexOf(")")) >= 0){
						if(!isCharacterMatchForLogicalOperator(targetStr, s.substring(bracketLeft+1, bracketRight))){
							return false;
						}
					}
				}else{
					if(!isCharacterMatchForLogicalOperator(targetStr, s)){
						return false;
					}
				}
			}
			return true;
		}
		return isCharacterMatchForLogicalOperator(targetStr, matchChar);
	}
	public static boolean isCharacterMatchForLogicalOperator(String targetStr, String matchChar){
		if(matchChar.indexOf("||") >= 0){
			String[] array = matchChar.split("\\|\\|");
			for(String matchCharTemp : array){
				if(isCharacterMatchForCommon(targetStr, matchCharTemp)){
					return true;
				}
			}
		}else if(matchChar.indexOf("&&") >= 0){
			String[] array = matchChar.split("&&");
			for(String matchCharTemp : array){
				if(!isCharacterMatchForCommon(targetStr, matchCharTemp)){
					return false;
				}
			}
			return true;
		}
		return isCharacterMatchForCommon(targetStr, matchChar);
	}
	public static boolean isCharacterMatchForCommon(String targetStr, String matchChar){
		if(("".equals(targetStr) && !"".equals(matchChar)) || ("".equals(matchChar) && !"".equals(targetStr))){
			return false;
		}
		
		if(matchChar.indexOf("NotContains") >= 0){
			return !isContains(targetStr, matchChar.replace("NotContains", ""));
		}
		if(matchChar.indexOf("NotEqual") >= 0){
			return !targetStr.equals(matchChar.replace("NotEqual", ""));
		}
		if(matchChar.indexOf("Equal") >= 0){
			return targetStr.equals(matchChar.replace("Equal", ""));
		}
		return isContains(targetStr, matchChar);
	}
	public static boolean isContains(String targetStr, String matchChar){
		if(matchChar.indexOf("%") < 0){
			return targetStr.indexOf(matchChar) >= 0;
		}
		if(matchChar.startsWith("%")){
			return targetStr.endsWith(matchChar.replace("%", ""));
		}
		if(matchChar.endsWith("%")){
			return targetStr.startsWith(matchChar.replace("%", ""));
		}
		return false;
	}
	
	// 规则: 任职机构性质类别不属于 101(党委班子)、121(政府班子)、131(人大班子) 时, 
	// (前一条简历和当前简历的隶属关系相同的情况下)
	// 若是机构隶属关系为 "[72]村民委员会", "[71]居民委员会", "[70]居民、村民委员会", "[63]乡", "[62]镇", "[61]街道", [60]街道、镇、乡  时, 需要提取机构名称, 不提取机构隶属关系前缀.
	private static String[] organizationNameSuffixDataTest = new String[] {"72", "71", "70", "63", "62", "61", "60"};
	private static String[] organizationNameSuffixDataTest2 = new String[] {"101", "121", "131"};
	
	
	// 上下两条简历机构名称开头包含 "共青团" 时, 前一条简历直接提取机构名称, 
	// 当前一条简历去除相同部分, 再加上 "共青团"
	private static String[] orgNameContain = new String[]{"共青团%"};
	
	// 当 DutyType 是如下代码时, 需要全部提取机构名称
	// 17(民主党派机关)、171(民主党派班子)、172(民主党派部门)、176(民主党派部门内设机构)、179(民主党派机关内其他机构)
	private static String[] organizationNameSuffixDataTest3 = new String[] {"17", "171", "172", "176", "179"};
	
	private static String[] orgSameGrade = new String[]{"地委", "地区"};//相同等级
	/**
	 * 构建 Organization(机构名称)
	 * @param build
	 * @param currentOrganization
	 * @param equalToOrganization
	 */
	public static String buildOrganization(String caesuraSignOrComma, String firstOrganization, 
			String previousOrganization, String previousOrgSubjectionRelationCode, String previousDutyType,
			String currentOrganization, String currentOrgSubjectionRelationCode, String currentDutyType,
			String[] analysisPCOrgNameArray
			){
		
		// new String[]{"0(无后缀)", "相同部分(无后缀)", "previous/current/previousAndcurrent", "解析当前相同部分后缀", "往上一级隶属机构获取相同的字符串(有后缀)", "后缀名称"};
		// new String[]{"1(有后缀)", "相同部分(有后缀)", "previous/current/previousAndcurrent", "解析当前相同部分后缀", "", ""};
		if(CAESURA_SIGN.equals(caesuraSignOrComma)){
			// 规则: ...
			if(!isCharacterMatch(currentDutyType, organizationNameSuffixDataTest2)){
				if(previousOrgSubjectionRelationCode.equals(currentOrgSubjectionRelationCode)){
					if(isCharacterMatch(previousOrgSubjectionRelationCode, organizationNameSuffixDataTest)){
						if("0".equals(analysisPCOrgNameArray[0])){
							return currentOrganization.replace(analysisPCOrgNameArray[4], "");
						}else if("1".equals(analysisPCOrgNameArray[0])){
							return currentOrganization.replace(analysisPCOrgNameArray[1], "");
						}
					}
				}
			}
			return "";
		}
		if(CAESURA_SIGN_PREFIX_ORG.equals(caesuraSignOrComma)){
			if("0".equals(analysisPCOrgNameArray[0])){
				if("current".equals(analysisPCOrgNameArray[2])){
					return currentOrganization.replace(analysisPCOrgNameArray[1], "");
				}
				return analysisPCOrgNameArray[5] + currentOrganization.replace(analysisPCOrgNameArray[4], "");
			}else if("1".equals(analysisPCOrgNameArray[0])){
				return analysisPCOrgNameArray[3] + currentOrganization.replace(analysisPCOrgNameArray[1], "");
			}
			return currentOrganization;
		}
		if(CAESURA_SIGN_ORG.equals(caesuraSignOrComma)){
			if("0".equals(analysisPCOrgNameArray[0])){
				if("previous".equals(analysisPCOrgNameArray[2])){
					return currentOrganization.replace(analysisPCOrgNameArray[4], "");
				}
				return currentOrganization.replace(analysisPCOrgNameArray[3], "");
			}else if("1".equals(analysisPCOrgNameArray[0])){
				return currentOrganization.replace(analysisPCOrgNameArray[1], "");
			}
			return currentOrganization;
		}
		
		// 20180415
		if(isCharacterMatch(previousOrganization, orgNameContain) || isCharacterMatch(currentOrganization, orgNameContain)){
			if("0".equals(analysisPCOrgNameArray[0])){
				return currentOrganization.replace(analysisPCOrgNameArray[4], "共青团");
			}else if("1".equals(analysisPCOrgNameArray[0])){
				return currentOrganization.replace(analysisPCOrgNameArray[1], "共青团");
			}
		}
		
		// 20180412
		if(isCharacterMatch(currentDutyType, organizationNameSuffixDataTest3)){
			return currentOrganization;
		}
		
		String result = null;
		
		// 判断前一条简历和当前条简历是否为同一隶属关系
		// 1).若是一致, 
		if(ParticularUtils.trim(previousOrgSubjectionRelationCode, "").equals(ParticularUtils.trim(currentOrgSubjectionRelationCode, ""))){
			// a.若是相同部分不含  机构集后缀(省、市、县等等), 则往隶属机构级别上一个层次查找(后缀含有: 省、县等等), 去除相同部分, 不用提取后缀;
			if("0".equals(analysisPCOrgNameArray[0])){
				if("previous".equals(analysisPCOrgNameArray[2])){
					result = currentOrganization.replace(analysisPCOrgNameArray[4], "");
//					return currentOrganization.replace(analysisPCOrgNameArray[1], "");
				}else if("current".equals(analysisPCOrgNameArray[2])){
					result = currentOrganization.replace(analysisPCOrgNameArray[1], "");
				}else{
//					return currentOrganization.replace(analysisPCOrgNameArray[1], "");
					result = currentOrganization.replace(analysisPCOrgNameArray[4], "");
				}
			}
			// b.若是相同部分含 机构集后缀(省、市、县等等), 去除相同部分, 提取后缀;
			else if("1".equals(analysisPCOrgNameArray[0])){
				// 20180402
				if(isCharacterMatchForLogicalOperator(currentOrganization.replace(analysisPCOrgNameArray[4], ""), "乡")){
					result = currentOrganization.replace(analysisPCOrgNameArray[1], "");
				}else{
					// 当上下两条隶属关系不为 [10]中央、[20]省(含自治区、直辖市) 级别, 且前缀是 "省" 时, 不提取 "省"
					if(!"10".equals(currentOrgSubjectionRelationCode) && !"20".equals(currentOrgSubjectionRelationCode) && "省".equals(analysisPCOrgNameArray[3])){
						result = currentOrganization.replace(analysisPCOrgNameArray[1], "");
					}else{
						if("州".equals(analysisPCOrgNameArray[3]) && currentOrganization.indexOf("贵州") >= 0){
							result = currentOrganization.replace(analysisPCOrgNameArray[1], "");
						}else{
							result = analysisPCOrgNameArray[3] + currentOrganization.replace(analysisPCOrgNameArray[1], "");
						}
					}
				}
			}
		}
		
		// 2).若是不一致, (这个是没有提取前缀)
		else{
			// 1).若是相同部分不含 机构集后缀, 则往隶属机构级别上一个层次查找(后缀含有: 省、县等等), 去除相同部分;
			if("0".equals(analysisPCOrgNameArray[0])){
				if(!"".equals(analysisPCOrgNameArray[2])){
					result = currentOrganization.replace(analysisPCOrgNameArray[1], "").replace(analysisPCOrgNameArray[3], "");
				}else{
					result = currentOrganization.replace(analysisPCOrgNameArray[3], "");
				}
			}
			// 2).若是相同部分含 机构集后缀, 去除相同部分;
			//    例如: 
			//    "贵州省赫章县监察局unit_job_split局长"
			//    "贵州省赫章县威奢乡unit_job_split人大主席"
			//    > 贵州省赫章县监察局局长, 威奢乡人大主席
			else if("1".equals(analysisPCOrgNameArray[0])){
				result = currentOrganization.replace(analysisPCOrgNameArray[1], "");
			}
		}
		
		if(result != null){
			// [20180409 18:05]
			if(isCharacterMatch(previousOrganization, orgSameGrade)){
				int index = 0;
				for(String s : orgSameGrade){
					if((index=currentOrganization.indexOf(s)) >= 0){
						return currentOrganization.substring(index, currentOrganization.length());
					}
				}
			}
			
			// ...
			if(result.startsWith("省")){
				if(isCharacterMatch(result, commaNotOrgSuffixNameData)){
					return result.replace("省", "");
				}
			}
			
			return result;
		}
		
		// 两个字符串都没有相匹配的字符
		return currentOrganization;
	}
	// 逗号规则: 当两条简历最后一个匹配是 "省" 时, 且第二条简历中还有 "市、县、区" 等等时, 不提取省, 只提取机构名.
	private static String[] commaNotOrgSuffixNameData = new String[] {"村民委员会", "居民委员会", "乡", "镇", "街道", "办事处", "县", "地区", "区", "市"};// , "省", "中央"
	
	// 上下两条简历机构名称开头包含 "共青团" 时, 前一条简历直接提取机构名称, 
	// 当前一条简历去除相同部分, 再加上 "共青团";
	//
	// "市委/县委/区委" 是一个机构名称, 与 "市/县/区" 是有区别;
//	private static String municipalPartyCommittee = "市委";
//	private static String municipalPartyCommitteeMark = "municipalPartyCommittee";
//	private static String countyPartyCommittee = "县委";
//	private static String countyPartyCommitteeMark = "countyPartyCommittee";
	private static String[][] partyCommitteeArray = new String[][]{
		{"省委", "provincialPartyCommittee"},
		{"市委", "municipalPartyCommittee"},
		{"州委", "stateCommittee"},
		{"县委", "countyPartyCommittee"},
		{"区委", "districtCommittee"}
	};
	
	private static String[][] parenthesisLeftRight = new String[][]{
		{"（", "）"},
		{"(", ")"},
		{"previous_parenthesisContent", "current_parenthesisContent", "equal_parenthesisContent"}
	};
	public static String[] prepareAnalysisPCOrgNameArray(String previousOrganization, String currentOrganization){
		
		String[] analysisPCOrgNameArray = null;
		
		// 预处理1: 加密/解密 括号内容
		int plIndex = 0, prIndex = 0;
		String ppContent = null;
		String cpContent = null;
		boolean isequalPC = false;
		if(((plIndex=previousOrganization.indexOf(parenthesisLeftRight[0][0])) >= 0 || (plIndex=previousOrganization.indexOf(parenthesisLeftRight[1][0])) >= 0) 
				&& ((prIndex=previousOrganization.indexOf(parenthesisLeftRight[0][1])) >= 0 || (prIndex=previousOrganization.indexOf(parenthesisLeftRight[1][1])) >= 0)){
			ppContent = previousOrganization.substring(plIndex, prIndex+1);
			previousOrganization = previousOrganization.replace(ppContent, parenthesisLeftRight[2][0]);
		}
		if(((plIndex=currentOrganization.indexOf(parenthesisLeftRight[0][0])) >= 0 || (plIndex=currentOrganization.indexOf(parenthesisLeftRight[1][0])) >= 0) 
				&& ((prIndex=currentOrganization.indexOf(parenthesisLeftRight[0][1])) >= 0 || (prIndex=currentOrganization.indexOf(parenthesisLeftRight[1][1])) >= 0)){
			cpContent = currentOrganization.substring(plIndex, prIndex+1);
			if(cpContent.equals(ppContent)){
				previousOrganization = previousOrganization.replace(parenthesisLeftRight[2][0], parenthesisLeftRight[2][2]);
				currentOrganization = currentOrganization.replace(cpContent, parenthesisLeftRight[2][2]);
				isequalPC = true;
			}else{
				currentOrganization = currentOrganization.replace(cpContent, parenthesisLeftRight[2][1]);
			}
		}
		
		// 预处理2: 
		boolean isPrepareTow = false;
		if(isCharacterMatch(previousOrganization, orgNameContain)){
			isPrepareTow = true;
			previousOrganization = encryptPartyCommittee(previousOrganization);
		}
		if(isCharacterMatch(currentOrganization, orgNameContain)){
			isPrepareTow = true;
			currentOrganization = encryptPartyCommittee(currentOrganization);
		}
		
		analysisPCOrgNameArray = analysisPreviousAndCurrentOrgNameToArray(previousOrganization, currentOrganization);
		for(int index = 0; index < analysisPCOrgNameArray.length; index++){
			if(isPrepareTow){
				analysisPCOrgNameArray[index] = decodePartyCommittee(analysisPCOrgNameArray[index]);
			}
			if(isequalPC){
				analysisPCOrgNameArray[index] = analysisPCOrgNameArray[index].replaceAll(parenthesisLeftRight[2][2], ppContent);
			}else{
				if(ppContent != null){
					analysisPCOrgNameArray[index] = analysisPCOrgNameArray[index].replaceAll(parenthesisLeftRight[2][0], ppContent);
				}
				if(cpContent != null){
					analysisPCOrgNameArray[index] = analysisPCOrgNameArray[index].replaceAll(parenthesisLeftRight[2][1], cpContent);
				}
			}
		}
		return analysisPCOrgNameArray;
	}
	public static String encryptPartyCommittee(String orgName){
		for(int i = 0; i < partyCommitteeArray.length; i++){
			if(orgName.indexOf(partyCommitteeArray[i][0]) >= 0){
				return orgName.replace(partyCommitteeArray[i][0], partyCommitteeArray[i][1]);
			}
		}
		return orgName;
	}
	public static String decodePartyCommittee(String orgName){
		for(int i = 0; i < partyCommitteeArray.length; i++){
			if(orgName.indexOf(partyCommitteeArray[i][1]) >= 0){
				return orgName.replace(partyCommitteeArray[i][1], partyCommitteeArray[i][0]);
			}
		}
		return orgName;
	}
	
	
//	private static final String IS_SUFFIX = "isSuffix"; // 有无后缀, 0: 无; 1: 有;
//	private static final String EQUAL_PART_NOT_SUFFIX = "equalPartNotSuffix"; // 相同部分(无后缀)
//	private static final String EQUAL_PART_HAS_SUFFIX = "equalPartHasSuffix"; // 相同部分(有后缀)
//	private static final String IS_PREVIOUS_CURRENT_ORG_SUFFIX = "isPreviousCurrentOrgSuffix";
//	private static final String EQUAL_PART_SUFFIX = "equalPartSuffix"; // 解析当前相同部分后缀
	
	
	/**
	 * array string:
	 * new String[]{"0(无后缀)", "相同部分(无后缀)", "previous/current/previousAndcurrent", "解析当前相同部分后缀", "往上一级隶属机构获取相同的字符串(有后缀)", "后缀名称"};
	 * new String[]{"1(有后缀)", "相同部分(有后缀)", "previous/current/previousAndcurrent", "解析当前相同部分后缀", "", ""};
	 * @param previousOrganization
	 * @param currentOrganization
	 * @return
	 */
	public static String[] analysisPreviousAndCurrentOrgNameToArray(String previousOrganization, String currentOrganization){
		// new String[]{"0(无后缀)", "相同部分(无后缀)", "previous/current/previousAndcurrent", "解析当前相同部分后缀", "往上一级隶属机构获取相同的字符串(有后缀)", "后缀名称"};
		// new String[]{"1(有后缀)", "相同部分(有后缀)", "previous/current/previousAndcurrent", "解析当前相同部分后缀", "", ""};
		String[] array = new String[]{"", "", "", "", "", ""};
		String equalsStr = analysisPreviousAndCurrentOrgNameToStr(previousOrganization, currentOrganization);
		
		String suffixName = null;
		String isPreviousAndcurrent = null;
		boolean b = false;
		for(String s : orgNameSuffixData){
			// 20180404
			if(equalsStr.indexOf("经济开发区") >= 0){
				equalsStr = equalsStr.replace("经济开发区", "");
				int index = 0;
				for(String s2 : orgNameSuffixData){
					if((index=equalsStr.indexOf(s2)) >= 0){
						equalsStr = equalsStr.substring(index, equalsStr.length());
					}
				}
				break;
			}
			if(equalsStr.endsWith(s)){
				suffixName = s;
				b = true;
				break;
			}
		}
		
		if(!b){
			String previousOrganizationReplace = previousOrganization.replace(equalsStr, "");
			String currentOrganizationReplace = currentOrganization.replace(equalsStr, "");
			// 是否有后缀
			boolean flag = false;
			for(String s : orgNameSuffixData){
				if(previousOrganizationReplace.startsWith(s)){
					flag = true;
					break;
				}
				if(currentOrganizationReplace.startsWith(s)){
					flag = true;
					break;
				}
			}
			
			if(!flag){
				int index = 0;
				for(String s : orgNameSuffixData){
					if((index=equalsStr.indexOf(s)) >= 0){
						suffixName = s;
						equalsStr = equalsStr.substring(0, index+s.length());
						break;
					}
				}
			}
		}
		
		if(suffixName == null){
			array[0] = "0";
			array[1] = equalsStr;
			
			String subStr = null;
			subStr = previousOrganization.substring(previousOrganization.indexOf(equalsStr)+equalsStr.length(), previousOrganization.length());
			for(String s : orgNameSuffixData){
				if(subStr.startsWith(s)){
					suffixName = s;
					isPreviousAndcurrent = "previous";
					break;
				}
			}
			if(suffixName == null){
				subStr = currentOrganization.substring(currentOrganization.indexOf(equalsStr)+equalsStr.length(), currentOrganization.length());
				for(String s : orgNameSuffixData){
					if(subStr.startsWith(s)){
						suffixName = s;
						isPreviousAndcurrent = "current";
						break;
					}
				}
			}
			if(isPreviousAndcurrent != null){
				array[2] = isPreviousAndcurrent;
			}
			if(suffixName != null){
				array[3] = suffixName;
			}
			
			int index = 0;
			for(String s : orgNameSuffixData){
				if((index=equalsStr.indexOf(s)) >= 0){
					array[4] = equalsStr.substring(0, index+s.length());
					array[5] = s;
					break;
				}
			}
			
		}else{
			array[0] = "1";
			array[1] = equalsStr;
			array[2] = "previousAndcurrent";
			if(suffixName != null){
				array[3] = suffixName;
			}
		}
		return array;
	}
	private static String[] orgNameSuffixData = new String[] {"村民委员会", "居民委员会", "乡", "镇", "街道", "办事处", "县", "地区", "区", "市", "省", "州", "中央"};
	public static String analysisPreviousAndCurrentOrgNameToStr(String previousOrganization, String currentOrganization){
		char[] previousOrganizationArray = previousOrganization.toCharArray();
		char[] currentOrganizationArray = currentOrganization.toCharArray();
		int previousIndex = 0;
		int currentIndex = 0;
		int equalPreviousOrganizationLength = 0;
		for(int i = 0; i < previousOrganizationArray.length; i++){
			previousIndex = i;
			currentIndex = 0;
			equalPreviousOrganizationLength = 0;
			for(int j = 0; j < 50; j++){
				if(previousIndex >= previousOrganizationArray.length || currentIndex >= currentOrganizationArray.length){
					break;
				}
				if(previousOrganizationArray[previousIndex] == currentOrganizationArray[currentIndex]){
					previousIndex++;
					equalPreviousOrganizationLength++;
				}else{
					if(equalPreviousOrganizationLength >= 2){
						break;
					}
					equalPreviousOrganizationLength = 0;
				}
				currentIndex++;
			}
			if(equalPreviousOrganizationLength >= 2){
				StringBuffer build = new StringBuffer();
				for(int k = i; k < previousIndex; k++){
					build.append(String.valueOf(previousOrganizationArray[k]));
				}
				return build.toString();
			}
		}
		return "";
	}
	
	/**
	 * 构建 Duty(职务)
	 * @param currentDuty
	 * @return
	 */
	public static String buildDuty(String currentDuty){
		return currentDuty;
	}
	
	
	
	
	/**
	 * 规则一: (前提: 上下两条简历的机构隶属code相同的前提下)
	 * @param buildOrganization
	 * @param firstOrganization
	 * @param previousOrganization
	 * @param organization
	 * @param duty
	 * @return
	 */
	public static String firstRule(String buildOrganization, String firstOrganization, String currentOrganization, String currentDuty, String currentOrgSubjectionRelationCode){
		boolean isBreak = false;
		String orgGradeData = null;
		Set<String[]> keySet = null;
		
		for(int i = 0; i < townBelowArray.length; i++){
			if(currentOrgSubjectionRelationCode.equals(townBelowArray[i][0])){
				keySet = townBelowOrgDutyDataMap.keySet();
				for(String[] key : keySet){
					if(isCharacterMatchForLogicalOperator(currentDuty, key[1])){
						if(isCharacterMatchForLogicalOperator("organization", key[0])){
							orgGradeData = townBelowOrgDutyDataMap.get(key).replace("_duty", currentDuty);
							isBreak = true;
							break;
						}else if(isCharacterMatchForLogicalOperator(currentOrganization, key[0])){
							orgGradeData = townBelowOrgDutyDataMap.get(key).replace("_duty", currentDuty);
							isBreak = true;
							break;
						}
					}
				}
			}
			if(isBreak){
				break;
			}
		}
		if(orgGradeData == null){
			isBreak = false;
			for(int i = 0; i < countyAboveArray.length; i++){
				if(currentOrgSubjectionRelationCode.equals(countyAboveArray[i][0])){
					keySet = countyAboveOrgDutyDataMap.keySet();
					for(String[] key : keySet){
						if(isCharacterMatchForLogicalOperator(currentDuty, key[1])){
							if(isCharacterMatchForLogicalOperator("organization", key[0])){
								orgGradeData = countyAboveOrgDutyDataMap.get(key).replace("_duty", currentDuty);
								isBreak = true;
								break;
							}else if(isCharacterMatchForLogicalOperator(currentOrganization, key[0])){
								orgGradeData = countyAboveOrgDutyDataMap.get(key).replace("_duty", currentDuty);
								isBreak = true;
								break;
							}
						}
					}
				}
				if(isBreak){
					break;
				}
			}
		}
		
		if(orgGradeData != null){
			if(orgGradeData.indexOf(buildOrganization) >= 0){
				return orgGradeData;
			}
			
			String firstOrganizationSuffix = null;
			String currentOrganizationSuffix = null;
			for(int i = 0; i < firstRuleForOrgSuffixArray.length; i++){
//			for(String s : firstRuleForOrgSuffixArray){
				if(firstOrganizationSuffix == null && currentOrgSubjectionRelationCode.equals(firstRuleForOrgSuffixArray[i][0])){
					firstOrganizationSuffix = firstRuleForOrgSuffixArray[i][1];
				}
				if(currentOrganizationSuffix == null && currentOrgSubjectionRelationCode.equals(firstRuleForOrgSuffixArray[i][0])){
					currentOrganizationSuffix = firstRuleForOrgSuffixArray[i][1];
				}
			}
			if(firstOrganizationSuffix != null && currentOrganizationSuffix != null && firstOrganizationSuffix.equals(currentOrganizationSuffix)){
				return orgGradeData;
			}
			return buildOrganization + orgGradeData;
		}
		return "";
	}
	private static String[][] firstRuleForOrgSuffixArray = new String[][]{{"61", "街道"}, {"62", "镇"}, {"63", "乡"}, {"50", "县"}, {"40", "市"}, {"40", "地区"}, {"20", "省"}};
	private static String[][] townBelowArray = new String[][]{{"61", "街道"}, {"62", "镇"}, {"63", "乡"}};
	private static Map<String[], String> townBelowOrgDutyDataMap = new LinkedHashMap<>();
	static{
		/**
    	 * 机构隶属关系乡/镇以下(包含乡/镇)的 (根据 机构隶属关系 判断)
    	 * [60]街道、镇、乡, [61]街道, [62]镇, [63]乡
    	 * 只要第二条或第二条后的职务包含有下列指定的字符时, 直接提取职务信息, 不提取机构名称;
    	 */
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "乡长"}, "_duty");
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "镇长"}, "_duty");
    	townBelowOrgDutyDataMap.put(new String[]{"政法", "政法委书记||书记"}, "政法委书记");
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "武装部长"}, "武装部长");
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "人大主席"}, "人大主席");
		townBelowOrgDutyDataMap.put(new String[]{"organization", "主席"}, "人大_duty");
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "组织委员"}, "组织委员");
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "宣传委员"}, "宣传委员");
    	townBelowOrgDutyDataMap.put(new String[]{"organization", "纪委书记"}, "纪委书记");
    	townBelowOrgDutyDataMap.put(new String[]{"人大工委", "主任"}, "人大工委_duty");
    	townBelowOrgDutyDataMap.put(new String[]{"人大主席团", "主席"}, "人大_duty");
    	
	}
	private static String[][] countyAboveArray = new String[][]{{"50", "县"}, {"40", "市"}, {"40", "地区"}, {"20", "省"}};
	private static Map<String[], String> countyAboveOrgDutyDataMap = new LinkedHashMap<>();
    static{
    	/**
    	 * 机构隶属关系县以上(包含县)的 (根据 机构隶属关系 判断)
    	 * 只要第二条或第二条后的机构和职务包含有下列指定的字符时, 直接提取职务信息, 不提取机构名称;
    	 */
    	countyAboveOrgDutyDataMap.put(new String[]{"organization", "县长"}, "_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"organization", "区长"}, "_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"organization", "市长"}, "_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"政法委", "Equal书记"}, "政法委书记");
    	countyAboveOrgDutyDataMap.put(new String[]{"武装部", "部长"}, "武装部部长");
    	countyAboveOrgDutyDataMap.put(new String[]{"人大常委会办公室", "主任"}, "人大常委会办公室_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"人大", "主任"}, "_duty");// put(new String[]{"人大", "主任"}, "人大_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"县委办公室", "主任"}, "县委办公室主任");
    	countyAboveOrgDutyDataMap.put(new String[]{"区委办公室", "主任"}, "区委办公室主任");
    	countyAboveOrgDutyDataMap.put(new String[]{"市委", "Equal秘书长"}, "市委秘书长");
    	countyAboveOrgDutyDataMap.put(new String[]{"组织部", "Equal部长"}, "组织部部长");
    	countyAboveOrgDutyDataMap.put(new String[]{"宣传部", "Equal部长"}, "宣传部部长");
		countyAboveOrgDutyDataMap.put(new String[]{"第一纪工委", "书记"}, "第一纪工委_duty");
		countyAboveOrgDutyDataMap.put(new String[]{"第二纪工委", "书记"}, "第二纪工委_duty");
		countyAboveOrgDutyDataMap.put(new String[]{"第三纪工委", "书记"}, "第三纪工委_duty");
		countyAboveOrgDutyDataMap.put(new String[]{"第四纪工委", "书记"}, "第四纪工委_duty");
		countyAboveOrgDutyDataMap.put(new String[]{"第五纪工委", "书记"}, "第五纪工委_duty");
		countyAboveOrgDutyDataMap.put(new String[]{"第六纪工委", "书记"}, "第六纪工委_duty");
		countyAboveOrgDutyDataMap.put(new String[]{"第七纪工委", "书记"}, "第七纪工委_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"纪委", "书记"}, "纪委书记");
    	countyAboveOrgDutyDataMap.put(new String[]{"政协", "主席"}, "_duty");//put(new String[]{"政协", "主席"}, "政协_duty")
    	countyAboveOrgDutyDataMap.put(new String[]{"统战部", "部长"}, "统战部部长");
    	countyAboveOrgDutyDataMap.put(new String[]{"管委会", "主任"}, "管委会_duty");
    	countyAboveOrgDutyDataMap.put(new String[]{"政治部", "主任"}, "政治部主任");

    }
    
    
    /**
     * 规则二:
     * [机构名称]unit_job_split[职务名称]unit_job_split[机构隶属关系code级别]unit_job_split[任职机构性质code类别]
     * 当前一条简历和当前简历机构名称完全一致时, 
     * 前一条简历的职务名称是 '委员||常委' 时, 且第二条简历的职务名称是 '常委||副书记||书记' 时, 
     * 则将这两条简历信息合并为一条, 职务名称为第二条简历的职务名称.
     * @param organizationDutyList
     * @return
     */
    public static List<String> secondRule(List<String> organizationDutyList) {
    	if(organizationDutyList == null || organizationDutyList.size() == 1){
    		return organizationDutyList;
    	}
    	
    	String[] previousOrganizationDutyArray = null;
		String[] currentOrganizationDutyArray = null;
		boolean isContains = false;
		boolean isContainsLast = false;
		int secondRuleForDutyArrayIndex = 1;
		int index = 0;
		Map<Integer, String> map = new HashMap<>();
    	for(int i = 1; i < organizationDutyList.size(); i++){
    		previousOrganizationDutyArray = organizationDutyList.get(i-1).split(EnumType.UNIT_JOB_SPLIT.value);
    		currentOrganizationDutyArray = organizationDutyList.get(i).split(EnumType.UNIT_JOB_SPLIT.value);
    		if(previousOrganizationDutyArray.length < 2 || currentOrganizationDutyArray.length < 2){
    			map.put(i-1, organizationDutyList.get(i-1));
    			if(i == organizationDutyList.size()-1){
    				map.put(i, organizationDutyList.get(i));
    			}
    			secondRuleForDutyArrayIndex = 1;
    			isContains = false;
    			isContainsLast = false;
    			continue;
    		}
    		
    		// 两条简历的机构名称完全相同
    		if(ParticularUtils.trim(previousOrganizationDutyArray[0], "").equals(ParticularUtils.trim(currentOrganizationDutyArray[0], ""))
    				&& secondRuleForDutyArrayIndex < secondRuleForDutyArray.length
    				&& isCharacterMatchForLogicalOperator(previousOrganizationDutyArray[1], secondRuleForDutyArray[secondRuleForDutyArrayIndex-1])
    				&& isCharacterMatchForLogicalOperator(currentOrganizationDutyArray[1], secondRuleForDutyArray[secondRuleForDutyArrayIndex])
    				){
    			index = i;
    			secondRuleForDutyArrayIndex++;
    			isContains = true;
    			if(i != organizationDutyList.size()-1){
					continue;
				}
    			isContainsLast = true;
    		}
			
    		if(isContains){
    			if(isContainsLast){
    				map.put(i, organizationDutyList.get(i));
    			}else if(i == organizationDutyList.size()-1){
    				map.put(index, organizationDutyList.get(index));
    				map.put(i, organizationDutyList.get(i));
    			}else{
    				map.put(index, organizationDutyList.get(index));
    			}
    			
			}else{
				map.put(i-1, organizationDutyList.get(i-1));
				if(i == organizationDutyList.size()-1){
					map.put(i, organizationDutyList.get(i));
				}
			}
    		secondRuleForDutyArrayIndex = 1;
			isContains = false;
			isContainsLast = false;
    	}
    	
    	List<String> resultList = new ArrayList<>();
    	Set<Integer> keySet = map.keySet();
    	for(Integer key : keySet){
    		resultList.add(map.get(key));
    	}
    	return resultList;
    }
    private static String[] secondRuleForDutyArray = new String[]{"Equal委员||Equal常委", "Equal常委||Equal副书记||Equal书记", "Equal书记||Equal副书记"};
    // 不考虑 "常委||副书记||书记" 先后顺序提取 ==== 未处理??????
    
    
    /**
     * 根据 "机构隶属关系" 排序:
     * [机构名称]unit_job_split[职务名称]unit_job_split[机构隶属关系code级别]unit_job_split[任职机构性质code类别]
     * [10] 中央
     * [20] 省(含自治区、直辖市)
     * [40] 市、地区(含自治州、盟、省辖市、直辖市辖区<县>)
     * [50] 县(含地<州、盟>辖市、省辖市辖区、自治县<旗>、县级市)
     * [60] 街道、镇、乡
     * [61] 街道
     * [62] 镇
     * [63] 乡
     * [70] 居民、村民委员会
     * [71] 居民委员会
     * [72] 村民委员会
     * [90] 其他
     * @param organizationDutyList
     * @return
     */
    public static List<String> sortOrgBySubjectionRelation(List<String> organizationDutyList){
    	if(organizationDutyList == null || organizationDutyList.size() == 1){
    		return organizationDutyList;
    	}
    	
    	String[] currentOrganizationDutyArray = null;
    	float[] subjectionRelationCodeArray = new float[organizationDutyList.size()];
    	int index = 0;
    	for(int i = 0; i < organizationDutyList.size(); i++){
    		if(organizationDutyList.get(i) == null){
    			continue;
    		}
    		currentOrganizationDutyArray = organizationDutyList.get(i).split(EnumType.UNIT_JOB_SPLIT.value);
    		if(index < subjectionRelationCodeArray.length){
    			if(currentOrganizationDutyArray.length >= 3){
        			subjectionRelationCodeArray[index] = ParticularUtils.toNumber(ParticularUtils.trim(currentOrganizationDutyArray[2], "100"), 100) + 0.01f*(i+1);
        		}else{
        			subjectionRelationCodeArray[index] = 100 + 0.01f*(i+1);
        		}
    			index++;
    		}else{
    			break;
    		}
    	}
    	
    	float temp = 0.0f;
    	for(int i = 0; i < subjectionRelationCodeArray.length-1; i++){
    		for(int j = 0; j < subjectionRelationCodeArray.length-1; j++){
    			if(subjectionRelationCodeArray[j] > subjectionRelationCodeArray[j+1]){
    				temp = subjectionRelationCodeArray[j+1];
    				subjectionRelationCodeArray[j+1] = subjectionRelationCodeArray[j];
    				subjectionRelationCodeArray[j] = temp;
    			}
    		}
    	}
    	List<String> resultList = new ArrayList<>();
    	for(float subjectionRelationCode : subjectionRelationCodeArray){
    		if(subjectionRelationCode > 0.0f){
    			resultList.add(organizationDutyList.get(new BigDecimal(String.valueOf(subjectionRelationCode)).subtract(new BigDecimal(String.valueOf((int)subjectionRelationCode))).multiply(new BigDecimal("100")).subtract(new BigDecimal("1")).intValue()));
    		}
    	}
    	return resultList;
    }
    
    
    
    
    
    /** =============================== 根据 "任职机构性质类别" 排序 ================================ **/
    private static Map<Integer, Float> dutyTypeCodeSortData = null;
    static{
    	dutyTypeCodeSortData = new HashMap<>();
    	dutyTypeCodeSortData.put(1, 1.0f);//1-机关
    	dutyTypeCodeSortData.put(10, 2.0f);//10-党委机关
    	dutyTypeCodeSortData.put(101, 3.0f);//101-党委班子（指中央、省、地、市、县、区、乡等党委班子）
    	dutyTypeCodeSortData.put(121, 4.0f);//121-政府班子（指中央、省、地、市、县、区、乡等政府班子）
    	dutyTypeCodeSortData.put(131, 5.0f);//131-人大班子（指中央、省、地、市、县、区等人大班子）
    	dutyTypeCodeSortData.put(141, 6.0f);//141-政协班子（指中央、省、市、县等政协班子）
    	dutyTypeCodeSortData.put(111, 7.0f);//111-党的纪检班子（指中央、省、地、市、县、区等纪检班子）
    	dutyTypeCodeSortData.put(102, 8.0f);//102-党委部门
    	dutyTypeCodeSortData.put(105, 9.0f);//105-党委部门的机关党委
    	dutyTypeCodeSortData.put(106, 10.0f);//106-党委部门内设机构
    	dutyTypeCodeSortData.put(109, 11.0f);//109-党的机关内其他机构
    	dutyTypeCodeSortData.put(11, 12.0f);//11-党的纪检机关
    	dutyTypeCodeSortData.put(112, 13.0f);//112-党的纪检部门
    	dutyTypeCodeSortData.put(115, 14.0f);//115-党的纪检部门的机关党委
    	dutyTypeCodeSortData.put(116, 15.0f);//116-党的纪检部门内设机构
    	dutyTypeCodeSortData.put(119, 16.0f);//119-党的纪检机关内其他机构
    	dutyTypeCodeSortData.put(13, 17.0f);//13-人大机关
    	dutyTypeCodeSortData.put(132, 18.0f);//132-人大部门
    	dutyTypeCodeSortData.put(133, 19.0f);//133-人大部门党委
    	dutyTypeCodeSortData.put(134, 20.0f);//134-人大部门党组
    	dutyTypeCodeSortData.put(135, 21.0f);//135-人大部门的机关党委
    	dutyTypeCodeSortData.put(136, 22.0f);//136-人大部门内设机构
    	dutyTypeCodeSortData.put(139, 23.0f);//139-人大机关内其他机构
    	dutyTypeCodeSortData.put(12, 24.0f);//12-政府机关
    	dutyTypeCodeSortData.put(122, 25.0f);//122-政府部门
    	dutyTypeCodeSortData.put(123, 26.0f);//123-政府部门党委
    	dutyTypeCodeSortData.put(124, 27.0f);//124-政府部门党组
    	dutyTypeCodeSortData.put(125, 28.0f);//125-政府部门的机关党委
    	dutyTypeCodeSortData.put(126, 29.0f);//126-政府部门内设机构
    	dutyTypeCodeSortData.put(129, 30.0f);//129-政府机关内其他机构
    	dutyTypeCodeSortData.put(14, 31.0f);//14-政协机关
    	dutyTypeCodeSortData.put(142, 32.0f);//142-政协部门
    	dutyTypeCodeSortData.put(143, 33.0f);//143-政协部门党委
    	dutyTypeCodeSortData.put(144, 34.0f);//144-政协部门党组
    	dutyTypeCodeSortData.put(145, 35.0f);//145-政协部门的机关党委
    	dutyTypeCodeSortData.put(146, 36.0f);//146-政协部门内设机构
    	dutyTypeCodeSortData.put(149, 37.0f);//149-政协机关内其他机构
    	dutyTypeCodeSortData.put(15, 38.0f);//15-法检机关
    	dutyTypeCodeSortData.put(151, 39.0f);//151-法检班子（指中央、省、地、市、县、区等法检班子）
    	dutyTypeCodeSortData.put(152, 40.0f);//152-法检部门
    	dutyTypeCodeSortData.put(153, 41.0f);//153-法检部门党委
    	dutyTypeCodeSortData.put(154, 42.0f);//154-法检部门党组
    	dutyTypeCodeSortData.put(155, 43.0f);//155-法检部门的机关党委
    	dutyTypeCodeSortData.put(156, 44.0f);//156-法检部门内设机构
    	dutyTypeCodeSortData.put(159, 45.0f);//159-法检机关内其他机构
    	dutyTypeCodeSortData.put(16, 46.0f);//16-人民团体机关
    	dutyTypeCodeSortData.put(161, 47.0f);//161-人民团体班子
    	dutyTypeCodeSortData.put(162, 48.0f);//162-人民团体部门
    	dutyTypeCodeSortData.put(163, 49.0f);//163-人民团体部门党委
    	dutyTypeCodeSortData.put(164, 50.0f);//164-人民团体部门党组
    	dutyTypeCodeSortData.put(165, 51.0f);//165-人民团体的机关党委
    	dutyTypeCodeSortData.put(166, 52.0f);//166-人民团体部门内设机构
    	dutyTypeCodeSortData.put(169, 53.0f);//169-人民团体机关内其他机构
    	dutyTypeCodeSortData.put(17, 54.0f);//17-民主党派机关
    	dutyTypeCodeSortData.put(171, 55.0f);//171-民主党派班子
    	dutyTypeCodeSortData.put(172, 56.0f);//172-民主党派部门
    	dutyTypeCodeSortData.put(176, 57.0f);//176-民主党派部门内设机构
    	dutyTypeCodeSortData.put(179, 58.0f);//179-民主党派机关内其他机构
    	dutyTypeCodeSortData.put(19, 59.0f);//19-其他机关
    	dutyTypeCodeSortData.put(4, 60.0f);//4-事业单位
    	dutyTypeCodeSortData.put(41, 61.0f);//41-参照公务员管理的事业单位
    	dutyTypeCodeSortData.put(411, 62.0f);//411-参照公务员管理的事业单位（行政类）
    	dutyTypeCodeSortData.put(412, 63.0f);//412-参照公务员管理的事业单位（公益一类）
    	dutyTypeCodeSortData.put(413, 64.0f);//413-参照公务员管理的事业单位（公益二类）
    	dutyTypeCodeSortData.put(419, 65.0f);//419-参照公务员管理的事业单位（尚未分类）
    	dutyTypeCodeSortData.put(49, 66.0f);//49-其他事业单位
    	dutyTypeCodeSortData.put(5, 67.0f);//5-企业
    	dutyTypeCodeSortData.put(511, 68.0f);//511-国有企业
    	dutyTypeCodeSortData.put(512, 69.0f);//512-国有联营企业
    	dutyTypeCodeSortData.put(521, 70.0f);//521-集体企业
    	dutyTypeCodeSortData.put(522, 71.0f);//522-集体联营企业
    	dutyTypeCodeSortData.put(531, 72.0f);//531-私营独资企业
    	dutyTypeCodeSortData.put(532, 73.0f);//532-私人合伙企业
    	dutyTypeCodeSortData.put(533, 74.0f);//533-私营有限责任公司
    	dutyTypeCodeSortData.put(551, 75.0f);//551-国有与集体联营企业
    	dutyTypeCodeSortData.put(552, 76.0f);//552-国有与私人联营企业
    	dutyTypeCodeSortData.put(553, 77.0f);//553-集体与私人联营企业
    	dutyTypeCodeSortData.put(554, 78.0f);//554-国有、集体与私人联营企业
    	dutyTypeCodeSortData.put(561, 79.0f);//561-股份有限公司
    	dutyTypeCodeSortData.put(562, 80.0f);//562-有限责任公司
    	dutyTypeCodeSortData.put(571, 81.0f);//571-中外合资经营企业
    	dutyTypeCodeSortData.put(572, 82.0f);//572-中外合作经营企业
    	dutyTypeCodeSortData.put(573, 83.0f);//573-外资企业
    	dutyTypeCodeSortData.put(581, 84.0f);//581-与港、澳、台合资经营企业
    	dutyTypeCodeSortData.put(582, 85.0f);//582-与港、澳、台合作经营企业
    	dutyTypeCodeSortData.put(583, 86.0f);//583-港、澳、台独资企业
    	dutyTypeCodeSortData.put(591, 87.0f);//591-租赁企业
    	dutyTypeCodeSortData.put(599, 88.0f);//599-其他企业
    	dutyTypeCodeSortData.put(6, 89.0f);//6-社会团体
    	dutyTypeCodeSortData.put(601, 90.0f);//601-学术性社会团体
    	dutyTypeCodeSortData.put(602, 91.0f);//602-专业性社会团体
    	dutyTypeCodeSortData.put(603, 92.0f);//603-行业性社会团体
    	dutyTypeCodeSortData.put(604, 93.0f);//604-联合性社会团体
    	dutyTypeCodeSortData.put(699, 94.0f);//699-其他社会团体
    	dutyTypeCodeSortData.put(7, 95.0f);//7-军队
    	dutyTypeCodeSortData.put(701, 96.0f);//701-兵团以上机关
    	dutyTypeCodeSortData.put(702, 97.0f);//702-兵团以上机关附属单位
    	dutyTypeCodeSortData.put(703, 98.0f);//703-军机关
    	dutyTypeCodeSortData.put(704, 99.0f);//704-军机关附属单位
    	dutyTypeCodeSortData.put(705, 100.0f);//705-师、团机关及附属单位
    	dutyTypeCodeSortData.put(706, 101.0f);//706-营以下部队
    	dutyTypeCodeSortData.put(707, 102.0f);//707-省军区系统
    	dutyTypeCodeSortData.put(708, 103.0f);//708-卫戍区、警备区系统
    	dutyTypeCodeSortData.put(709, 104.0f);//709-军队院校
    	dutyTypeCodeSortData.put(710, 105.0f);//710-军队医院、疗养院
    	dutyTypeCodeSortData.put(711, 106.0f);//711-后勤分部
    	dutyTypeCodeSortData.put(712, 107.0f);//712-军队仓库
    	dutyTypeCodeSortData.put(713, 108.0f);//713-军队科研机构
    	dutyTypeCodeSortData.put(714, 109.0f);//714-军队干休所
    	dutyTypeCodeSortData.put(715, 110.0f);//715-军队其他单位
    	dutyTypeCodeSortData.put(8, 111.0f);//8-宗教团体
    	dutyTypeCodeSortData.put(9, 112.0f);//9-其他单位
    	dutyTypeCodeSortData.put(91, 113.0f);//91-民办非企业
    	dutyTypeCodeSortData.put(92, 114.0f);//92-个体工商户
    	dutyTypeCodeSortData.put(921, 115.0f);//921-城镇个体工商户
    	dutyTypeCodeSortData.put(922, 116.0f);//922-乡镇个体工商户
    	dutyTypeCodeSortData.put(93, 117.0f);//93-境外机构
    	dutyTypeCodeSortData.put(99, 118.0f);//99-其他单位
    }
    
    /**
     * 根据 "任职机构性质类别" 排序
     * [机构名称]unit_job_split[职务名称]unit_job_split[机构隶属关系code级别]unit_job_split[任职机构性质code类别]
     * @param organizationDutyList
     * @return
     */
    public static List<String> sortOrganizationByDutyType(List<String> organizationDutyList){
    	
    	if(organizationDutyList != null && organizationDutyList.size() < 2){
    		return organizationDutyList;
    	}
    	
    	// 排序结果
    	int[] sortIndexs = new int[organizationDutyList.size()];
    	int sortIndex = 0;
    	
    	String[] previousOrganizationDutyArray = null;
    	String[] currentOrganizationDutyArray = null;
    	boolean firstEqualOrgCode = true;
    	
    	List<Float> dutyTypeCodeList = new ArrayList<>();
    	int dutyTypeCodeKey = 0;
    	Float dutyTypeCodeValue = 0.0f;
    	int[] resultArray = null;
    	
    	for(int i = 1; i < organizationDutyList.size(); i++){
    		previousOrganizationDutyArray = organizationDutyList.get(i-1).split(EnumType.UNIT_JOB_SPLIT.value);
    		currentOrganizationDutyArray = organizationDutyList.get(i).split(EnumType.UNIT_JOB_SPLIT.value);
    		if(previousOrganizationDutyArray.length < 3 || currentOrganizationDutyArray.length < 3){
    			sortIndexs[sortIndex++] = (i);
				
				// [20180410 09:39]
				if(i == organizationDutyList.size()-1){
					sortIndexs[sortIndex++] = (i+1);
				}
    			continue;
    		}
    		
    		// previousOrg和currentOrg机构隶属关系Code是否相同
    		if(!ParticularUtils.trim(previousOrganizationDutyArray[2], "").equals(ParticularUtils.trim(currentOrganizationDutyArray[2], ""))){
    			if(!firstEqualOrgCode){
    				resultArray = bubbling(dutyTypeCodeList);
    				for(int index : resultArray){
    					if(sortIndex < sortIndexs.length){
    						sortIndexs[sortIndex++] = index;
    					}else{
    						break;
    					}
    				}
    				if(i == organizationDutyList.size()-1){
    					sortIndexs[sortIndex++] = (i+1);
    				}
    				dutyTypeCodeList = new ArrayList<>();
    				firstEqualOrgCode = true;
    			}else{
    				sortIndexs[sortIndex++] = (i);
    				
    				// [20180410 09:39]
    				if(i == organizationDutyList.size()-1){
    					sortIndexs[sortIndex++] = (i+1);
    				}
    			}
    			continue;
    		}
    		// equal
    		else{
				if(firstEqualOrgCode){
					if(previousOrganizationDutyArray.length >= 4){
						dutyTypeCodeKey = ParticularUtils.toNumber(ParticularUtils.trim(previousOrganizationDutyArray[3], "1000"), 1000);
						dutyTypeCodeValue = dutyTypeCodeSortData.get(dutyTypeCodeKey);
						if(dutyTypeCodeValue == null){
							dutyTypeCodeList.add((float)dutyTypeCodeKey + 0.01f*(i));
						}else{
							dutyTypeCodeList.add(dutyTypeCodeValue +  + 0.01f*(i));
						}
					}else{
						dutyTypeCodeList.add(1000.0f + 0.01f*(i));
					}
					
					if(currentOrganizationDutyArray.length >= 4){
						dutyTypeCodeKey = ParticularUtils.toNumber(ParticularUtils.trim(currentOrganizationDutyArray[3], "1000"), 1000);
						dutyTypeCodeValue = dutyTypeCodeSortData.get(dutyTypeCodeKey);
						if(dutyTypeCodeValue == null){
							dutyTypeCodeList.add((float)dutyTypeCodeKey + 0.01f*(i+1));
						}else{
							dutyTypeCodeList.add(dutyTypeCodeValue +  + 0.01f*(i+1));
						}
					}else{
						dutyTypeCodeList.add(1000.0f + 0.01f*(i+1));
					}
					firstEqualOrgCode = false;
					
				}else{
					if(currentOrganizationDutyArray.length >= 4){
						dutyTypeCodeKey = ParticularUtils.toNumber(ParticularUtils.trim(currentOrganizationDutyArray[3], "1000"), 1000);
						dutyTypeCodeValue = dutyTypeCodeSortData.get(dutyTypeCodeKey);
						if(dutyTypeCodeValue == null){
							dutyTypeCodeList.add((float)dutyTypeCodeKey + 0.01f*(i+1));
						}else{
							dutyTypeCodeList.add(dutyTypeCodeValue +  + 0.01f*(i+1));
						}
					}else{
						dutyTypeCodeList.add(1000.0f + 0.01f*(i+1));
					}
				}
			}
    		
    		if(i == organizationDutyList.size()-1){
    			if(!firstEqualOrgCode){
    				resultArray = bubbling(dutyTypeCodeList);
    				for(int index : resultArray){
    					sortIndexs[sortIndex++] = index;
    				}
    				dutyTypeCodeList = new ArrayList<>();
    				firstEqualOrgCode = true;
    			}
    		}
    	}
    	
    	List<String> resultList = new ArrayList<>();
    	for(int index : sortIndexs){
    		if(index > 0 && index <= organizationDutyList.size()){
    			resultList.add(organizationDutyList.get(index-1));
    		}
    	}
    	return resultList;
    }
    public static int[] bubbling(List<Float> dutyTypeCodeList){
    	float[] dutyTypeCodeArray = new float[dutyTypeCodeList.size()];
    	for(int i = 0; i < dutyTypeCodeArray.length; i++){
    		dutyTypeCodeArray[i] = dutyTypeCodeList.get(i);
    	}
    	
    	float temp = 0.0f;
    	for(int i = 0; i < dutyTypeCodeArray.length-1; i++){
    		for(int j = 0; j < dutyTypeCodeArray.length-1; j++){
    			if(dutyTypeCodeArray[j] > dutyTypeCodeArray[j+1]){
    				temp = dutyTypeCodeArray[j+1];
    				dutyTypeCodeArray[j+1] = dutyTypeCodeArray[j];
    				dutyTypeCodeArray[j] = temp;
    			}
    		}
    	}
    	
    	int[] resultArray = new int[dutyTypeCodeArray.length];
    	for(int i = 0; i < dutyTypeCodeArray.length; i++){
    		resultArray[i] = new BigDecimal(String.valueOf(dutyTypeCodeArray[i])).subtract(new BigDecimal(String.valueOf((int)dutyTypeCodeArray[i]))).multiply(new BigDecimal("100")).intValue();
    	}
    	return resultArray;
    }
    
}
