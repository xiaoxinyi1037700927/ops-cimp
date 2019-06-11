/**
 * @Project:      IIMP
 * @Title:          SheetController.java
 * @Copyright: © 2017 www.newskysoft.com.cn. All rights reserved.
 */
package com.sinosoft.ops.cimp.controller.sheet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.sinosoft.ops.cimp.common.model.*;
import com.sinosoft.ops.cimp.common.service.BaseService;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoItem;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSet;
import com.sinosoft.ops.cimp.entity.infostruct.SysInfoSetCategory;
import com.sinosoft.ops.cimp.entity.sheet.*;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeItem;
import com.sinosoft.ops.cimp.entity.sys.syscode.SysCodeSet;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoItemService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetCategoryService;
import com.sinosoft.ops.cimp.service.infostruct.SysInfoSetService;
import com.sinosoft.ops.cimp.service.sheet.*;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeItemService;
import com.sinosoft.ops.cimp.service.sys.syscode.SysCodeSetService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName: SheetComController
 * @Description: Sheet通用控制类
 * @Author: wangyg
 * @Date: 2018.06.07
 * @Version 1.0.0
 */
@Controller("sheetComController")
@RequestMapping("sheet")
public class SheetComController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(SheetComController.class);

	private static final int maxComplicate = 100;// 最大的并发数为100
	private static int currentLine = 0;
	private static List<String> userList = new ArrayList<String>();

	private static ExecutorService excutor;
	static {
		excutor = Executors.newFixedThreadPool(7);
	}

	@Autowired
	private SheetComService sheetComService;
	@Autowired
	private SysInfoSetCategoryService sysInfoSetCategoryService;
	@Autowired
	private SysInfoSetService sysInfoSetService;
	@Autowired
	private SysInfoItemService sysInfoItemService;
	@Autowired
	private SysCodeItemService sysCodeItemService;
	@Autowired
	private SysCodeSetService sysCodeSetService;
	@Autowired
	private SheetDesignSectionService sheetDesignSectionService;
	@Autowired
	private SheetDesignDataSourceService sheetDesignDataSourceService;
	@Autowired
	private SheetDesignFieldService sheetDesignFieldService;
	@Autowired
	private SheetDesignConditionService sheetDesignConditionService;
	@Autowired
	private SheetDesignExpressionService sheetDesignExpressionService;
	@Autowired
	private SheetDesignCarrierService sheetDesignCarrierService;
	@Autowired
	private SheetService sheetService;
	@Autowired
	private SheetParameterService sheetParameterService;
	@Autowired
	private SheetDesignService sheetDesignService;
	@Autowired
	private SheetDataService sheetDataService;

	/**
	 * 
	 * 根据codeset 名称 一次获取整个树形结构数据
	 */
	@RequestMapping("/getCodeItemTreeAll")
	@ResponseBody
	public ResponseResult getCodeItemTreeAll(HttpServletRequest request, HttpServletResponse response) {

		try {
			String codeSetName = request.getParameter("codeSetName");

			SysCodeSet codeSet = sysCodeSetService.getByName(codeSetName);

			Collection<SysCodeItem> itemList = sysCodeItemService.getBySetId(codeSet.getId());

			Collection<TreeNode> tree = sheetComService.generateItemTree(itemList, codeSetName);

			return ResponseResult.success(tree, tree.size(), "构建树形结构成功");
		} catch (Exception e) {
			logger.error("构建树形失败", e);
			return ResponseResult.failure("构建树形失败");
		}
	}

	/**
	 * 
	 * 根据code 和codeset名称 获取当前code下子节点
	 */
	@RequestMapping("/getChildCodeItemTreeList")
	@ResponseBody
	public ResponseResult getChildCodeItemList(HttpServletRequest request, HttpServletResponse response) {

		try {
			String code = request.getParameter("code");
			String codeSetName = request.getParameter("codeSetName");
			SysCodeSet codeSet = sysCodeSetService.getByName(codeSetName);
			// 如果 code 为 null 则显示第一级树
			List<SysCodeItem> list = null;
			if (code == null) {
				list = sheetComService.getChildCodeItemList(codeSet.getId(), codeSetName);
			} else {
				// 如果不为空 则显示 该code节点下的子节点
				list = sheetComService.getChildCodeItemList(codeSet.getId(), code);
			}
			List<DefaultTreeNode> treeList = sheetComService.conversionTree(list);
			return ResponseResult.success(treeList, treeList.size(), "获取子节点成功");
		} catch (Exception e) {
			logger.error("获取子节点失败", e);
			return ResponseResult.failure("获取子节点失败");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ResponseBody
	@RequestMapping("/getInfoSetTree")
	public ResponseResult getInfoSetTree(HttpServletRequest request, HttpServletResponse response,
			String strDataSourceID) {
		List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> InfoSetData = new ArrayList<HashMap<String, Object>>();
		Collection<SysInfoSetCategory> sysInfoSetCategorys = sysInfoSetCategoryService.getAll();
		if (sysInfoSetCategorys.size() > 0) {
			for (SysInfoSetCategory sysInfoSetCategory : sysInfoSetCategorys) {
				InfoSetData = new ArrayList<HashMap<String, Object>>();
				Collection<SysInfoSet> sysInfoSets = sysInfoSetService.getByGroupId(sysInfoSetCategory.getId());
				Collections.sort((List) sysInfoSets, new Comparator<SysInfoSet>() {
					@Override
					public int compare(SysInfoSet o1, SysInfoSet o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				for (SysInfoSet sysInfoSet : sysInfoSets) {
					SysInfoSet infoSet = sysInfoSetService.getById(sysInfoSet.getId());
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("treeid", infoSet.getId());
					map.put("tablename", infoSet.getTableName());
					map.put("text", infoSet.getNameCn());
					map.put("expanded", false);
					map.put("leaf", true);
					map.put("nameEx", infoSet.getNameEx2());
					InfoSetData.add(map);
				}

				HashMap<String, Object> mapInfoSetData = new HashMap<String, Object>();
				mapInfoSetData.put("treeid", sysInfoSetCategory.getId());
				mapInfoSetData.put("text", sysInfoSetCategory.getName());
				mapInfoSetData.put("expanded", false);
				mapInfoSetData.put("leaf", false);
				mapInfoSetData.put("children", InfoSetData);
				root.add(mapInfoSetData);
			}
		}

		return ResponseResult.success(root, root.size(), "取得数据集数据项");
	}

	@ResponseBody
	@RequestMapping("/getInfoSetAndItemTree")
	public ResponseResult getInfoSetAndItemTree(HttpServletRequest request, HttpServletResponse response,
			String strDataSourceID) {
		List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> InfoSetData = new ArrayList<HashMap<String, Object>>();
		Collection<SysInfoSetCategory> sysInfoSetCategorys = sysInfoSetCategoryService.getAll();
		if (sysInfoSetCategorys.size() > 0) {
			for (SysInfoSetCategory sysInfoSetCategory : sysInfoSetCategorys) {
				InfoSetData = new ArrayList<HashMap<String, Object>>();
				Collection<SysInfoSet> sysInfoSets = sysInfoSetService.getByGroupId(sysInfoSetCategory.getId());
				for (SysInfoSet sysInfoSet : sysInfoSets) {
					SysInfoSet infoSet = sysInfoSetService.getById(sysInfoSet.getId());
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("treeid", infoSet.getId());
					map.put("text", infoSet.getNameCn());
					map.put("expanded", false);
					map.put("leaf", false);
					Collection<SysInfoItem> infoItems = sysInfoItemService.getBySetId(infoSet.getId());
					List<HashMap<String, Object>> children1 = new ArrayList<HashMap<String, Object>>();
					for (SysInfoItem infoItem : infoItems) {
						HashMap<String, Object> map1 = new HashMap<String, Object>();
						map1.put("treeid", infoItem.getId());
						map1.put("columnname", infoItem.getColumnName());
						map1.put("text", infoItem.getNameCn());
						map1.put("expanded", false);
						map1.put("leaf", true);
						map1.put("xtype", infoItem.getInputType());
						map1.put("jdbctype", infoItem.getJdbcType());
						if ("combox".equals(infoItem.getInputType())) {
							map1.put("codeType", infoItem.getReferenceCodeSet());
						} else if ("code".equals(infoItem.getInputType())) {
							map1.put("codeType", infoItem.getReferenceCodeSet());
						} else if ("comboxtree".equals(infoItem.getInputType())) {
							map1.put("codeType", infoItem.getReferenceCodeSet());
						}
						children1.add(map1);
					}
					map.put("children", children1);
					InfoSetData.add(map);
				}

				HashMap<String, Object> mapInfoSetData = new HashMap<String, Object>();
				mapInfoSetData.put("treeid", sysInfoSetCategory.getId());
				mapInfoSetData.put("text", sysInfoSetCategory.getName());
				mapInfoSetData.put("expanded", false);
				mapInfoSetData.put("leaf", false);
				mapInfoSetData.put("children", InfoSetData);
				root.add(mapInfoSetData);
			}
		}

		return ResponseResult.success(root, root.size(), "取得数据集数据项");
	}

	@ResponseBody
	@RequestMapping("/getItemTreeByDataRange")
	public ResponseResult getItemTreeByDataRange(HttpServletRequest request, HttpServletResponse response,
			String dataRange) {
		List<HashMap<String, Object>> root = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> InfoSetData = new ArrayList<HashMap<String, Object>>();
		Collection<SysInfoSetCategory> sysInfoSetCategorys = sysInfoSetCategoryService.getAll();
		if (sysInfoSetCategorys.size() > 0) {
			for (SysInfoSetCategory sysInfoSetCategory : sysInfoSetCategorys) {
				InfoSetData = new ArrayList<HashMap<String, Object>>();
				Collection<SysInfoSet> sysInfoSets = sysInfoSetService.getByGroupId(sysInfoSetCategory.getId());
				if (sysInfoSets.stream()
						.filter(item -> dataRange.toLowerCase().contains(item.getTableName().toLowerCase()))
						.collect(Collectors.toList()).size() > 0) {
					for (SysInfoSet sysInfoSet : sysInfoSets.stream()
							.filter(item -> dataRange.toLowerCase().contains(item.getTableName().toLowerCase()))
							.collect(Collectors.toList())) {
						SysInfoSet infoSet = sysInfoSetService.getById(sysInfoSet.getId());
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("treeid", infoSet.getId());
						map.put("text", infoSet.getNameCn());
						map.put("tablename", infoSet.getTableName());
						map.put("expanded", false);
						map.put("leaf", false);
						Collection<SysInfoItem> infoItems = sysInfoItemService.getBySetId(infoSet.getId());
						List<HashMap<String, Object>> children1 = new ArrayList<HashMap<String, Object>>();
						for (SysInfoItem infoItem : infoItems) {
							if (infoItem.getInputType().equals("hidden"))
								continue;
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("treeid", infoItem.getId());
							map1.put("columnname", infoItem.getColumnName());
							map1.put("text", infoItem.getNameCn());
							map1.put("expanded", false);
							map1.put("leaf", true);
							map1.put("xtype", infoItem.getInputType());
							map1.put("jdbctype", infoItem.getJdbcType());
							if ("combox".equals(infoItem.getInputType())) {
								map1.put("codeType", infoItem.getReferenceCodeSet());
							} else if ("code".equals(infoItem.getInputType())) {
								map1.put("codeType", infoItem.getReferenceCodeSet());
							} else if ("comboxtree".equals(infoItem.getInputType())) {
								map1.put("codeType", infoItem.getReferenceCodeSet());
							}
							children1.add(map1);
						}
						map.put("children", children1);
						InfoSetData.add(map);
					}

					HashMap<String, Object> mapInfoSetData = new HashMap<String, Object>();
					mapInfoSetData.put("treeid", sysInfoSetCategory.getId());
					mapInfoSetData.put("text", sysInfoSetCategory.getName());
					mapInfoSetData.put("expanded", false);
					mapInfoSetData.put("leaf", false);
					mapInfoSetData.put("children", InfoSetData);
					root.add(mapInfoSetData);
				}
			}
		}

		return ResponseResult.success(root, root.size(), "取得数据集数据项");
	}

	// @ResponseBody
	// @RequestMapping("/getItemTreeByDataPosition")
	// public ResponseResult getItemTreeByDataPosition(HttpServletRequest
	// request, HttpServletResponse response,String designId,Integer
	// rowNo,Integer columnNo) {
	// try {
	// Collection<SheetDesignDataSource> dataSources =
	// sheetDesignDataSourceService.getByDesignId(UUID.fromString(designId));
	// SheetDesignDataSource dataSource =
	// sheetComService.getUniqueDataSource(dataSources, rowNo, columnNo);
	// String dataRange = dataSource.getDataRange();
	// List<HashMap<String, Object>> root = new ArrayList<HashMap<String,
	// Object>>();
	// List<HashMap<String, Object>> InfoSetData = new ArrayList<HashMap<String,
	// Object>>();
	// Collection<SysInfoSetCategory> sysInfoSetCategorys =
	// sysInfoSetCategoryService.getAll();
	// if (sysInfoSetCategorys.size()>0) {
	// for(SysInfoSetCategory sysInfoSetCategory : sysInfoSetCategorys)
	// {
	// InfoSetData = new ArrayList<HashMap<String, Object>>();
	// Collection<SysInfoSet> sysInfoSets =
	// sysInfoSetService.getByGroupId(sysInfoSetCategory.getId());
	// if(sysInfoSets.stream().filter(item->
	// dataRange.toLowerCase().contains(item.getTableName().toLowerCase())).collect(Collectors.toList()).size()>0)
	// {
	// for (SysInfoSet sysInfoSet : sysInfoSets.stream().filter(item->
	// dataRange.toLowerCase().contains(item.getTableName().toLowerCase())).collect(Collectors.toList()))
	// {
	// SysInfoSet infoSet = sysInfoSetService.getById(sysInfoSet.getId());
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("treeid", infoSet.getId());
	// map.put("text", infoSet.getNameCn());
	// map.put("tablename", infoSet.getTableName());
	// map.put("expanded", false);
	// map.put("leaf", false);
	// map.put("checked", false);
	// Collection<SysInfoItem> infoItems =
	// sysInfoItemService.getBySetId(infoSet.getId());
	// List<HashMap<String, Object>> children1 = new ArrayList<HashMap<String,
	// Object>>();
	// for(SysInfoItem infoItem: infoItems)
	// {
	// HashMap<String, Object> map1 = new HashMap<String, Object>();
	// map1.put("treeid", infoItem.getId());
	// map1.put("columnname", infoItem.getColumnName());
	// map1.put("text", infoItem.getNameCn());
	// map1.put("expanded", false);
	// map1.put("leaf", true);
	// map1.put("xtype", infoItem.getInputType());
	// map1.put("jdbctype", infoItem.getJdbcType());
	// map1.put("checked", false);
	// if ("combox".equals(infoItem.getInputType())) {
	// map1.put("codeType", infoItem.getReferenceCodeSet());
	// } else if ("code".equals(infoItem.getInputType())) {
	// map1.put("codeType", infoItem.getReferenceCodeSet());
	// } else if ("comboxtree".equals(infoItem.getInputType())) {
	// map1.put("codeType", infoItem.getReferenceCodeSet());
	// }
	// children1.add(map1);
	// }
	// map.put("children", children1);
	// InfoSetData.add(map);
	// }
	//
	// HashMap<String, Object> mapInfoSetData = new HashMap<String, Object>();
	// mapInfoSetData.put("treeid", sysInfoSetCategory.getId());
	// mapInfoSetData.put("text", sysInfoSetCategory.getName());
	// mapInfoSetData.put("expanded", false);
	// mapInfoSetData.put("leaf", false);
	// mapInfoSetData.put("checked", false);
	// mapInfoSetData.put("children", InfoSetData);
	// root.add(mapInfoSetData);
	// }
	// }
	// }
	//
	// return ResponseResult.success(root, root.size(),"取得数据集数据项");
	// } catch (Exception e) {
	// logger.error("根据位置取得数据集数据项失败",e);
	// return ResponseResult.failure("根据位置取得数据集数据项失败");
	// }
	// }

	// 根据陈处要求根据条件元查询对应的数据
	@ResponseBody
	@RequestMapping("/getItemTreeByDataPosition")
	public ResponseResult getItemTreeByDataPosition(HttpServletRequest request, HttpServletResponse response,
			String designId, Integer rowNo, Integer columnNo) {
		try {
			List<HashMap<String, Object>> root = sheetComService.getItemTreeByDataPosition(designId, rowNo, columnNo);
			return ResponseResult.success(root, root.size(), "取得数据集数据项");
		} catch (Exception e) {
			logger.error("根据位置取得数据集数据项失败", e);
			return ResponseResult.failure("根据位置取得数据集数据项失败");
		}
	}

	@ResponseBody
	@RequestMapping("/getItemTreeBySectionNo")
	public ResponseResult getItemTreeBySectionNo(HttpServletRequest request, HttpServletResponse response,
			String designId, String sectionNo) {
		try {
			List<HashMap<String, Object>> root = sheetComService.getItemTreeBySectionNo(designId, sectionNo);
			return ResponseResult.success(root, root.size(), "取得数据集数据项");
		} catch (Exception e) {
			logger.error("根据位置取得数据集数据项失败", e);
			return ResponseResult.failure("根据位置取得数据集数据项失败");
		}
	}

	@RequestMapping("/getSysInfoSetList")
	@ResponseBody
	public ResponseResult getSysInfoSetList(HttpServletRequest request, HttpServletResponse response) {

		try {
			Collection<SysInfoSet> list = sheetComService.getInfoSet();
			return ResponseResult.success(list, 1, "查询成功！");
		} catch (Exception e) {
			logger.error("SheetDesignTagController getSysInfoSetList error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "查询失败！");
		}
	}

	@RequestMapping("/getSysInfoItemList")
	@ResponseBody
	public ResponseResult getSysInfoItemList(HttpServletRequest request, HttpServletResponse response) {

		try {
			String sinfoSetId = request.getParameter("infoSetId");
			Integer infoSetId = Integer.valueOf(sinfoSetId);
			Collection<SysInfoItem> list = sheetComService.getByInfoSetId(infoSetId);
			return ResponseResult.success(list, 1, "查询成功！");
		} catch (Exception e) {
			logger.error("SheetDesignTagController getSysInfoSetList error:{}", Throwables.getStackTraceAsString(e));
			return ResponseResult.failure(0, "查询失败！");
		}
	}

	/**
	 * 模板试算
	 * 
	 * @param request
	 * @param response
	 * @param designId
	 */
	@RequestMapping("/buildSheetData")
	@ResponseBody
	public ResponseResult buildSheetData(HttpServletRequest request, HttpServletResponse response, String designId) {
		try {
			JSONArray paramArray = new JSONArray();
			if (request.getParameter("templetParams") != null) {
				paramArray = JSON.parseArray(request.getParameter("templetParams"));
			}
			UUID userId = getCurrentLoggedInUser().getId();
			List<SheetData> sheetDatas = sheetComService.getComputeDatas4Excel(userId,null,UUID.fromString(designId), paramArray,sheetComService,0);
			
			// 公式计算
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(UUID.fromString(designId));
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
			List<Map<String, Object>> list = sheetComService.formulaCaculation(sheetDatas, range, expressions);

			return ResponseResult.success(list, list.size(), "构建表格数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("构建表格数据失败", e);
			if (e instanceof IllegalArgumentException) {
				return ResponseResult.failure(e.getMessage());
			}
			if (e instanceof SheetException)
				return ResponseResult.failure(e.getMessage());
			// 多线程异常会被包一层,所以暂时先用解析的方式去做
			if (e.getMessage().contains("SheetException:"))
				return ResponseResult.failure(e.getMessage().substring(e.getMessage().indexOf("SheetException:") + 15));
			return ResponseResult.failure("构建表格数据失败");
		}
	}

	/**
	 * 构建excel表格单元格数据
	 * 
	 * @param request
	 * @param response
	 * @param designId
	 */
	@RequestMapping("/buildListSheetData")
	@ResponseBody
	public ResponseResult buildListSheetData(HttpServletRequest request, HttpServletResponse response,
			String designId) {
		try {
			JSONArray paramArray = new JSONArray();
			if (request.getParameter("templetParams") != null) {
				paramArray = JSON.parseArray(request.getParameter("templetParams"));
			}
			
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(UUID.fromString(designId));
			UUID userId = getCurrentLoggedInUser().getId();
			List<SheetData> sheetDatas = sheetComService.getComputeDatas4Excel(userId,null,UUID.fromString(designId), paramArray, sheetComService,0);

			// 公式计算
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
			List<Map<String, Object>> list = sheetComService.formulaCaculation(sheetDatas, range, expressions);

			return ResponseResult.success(list, list.size(), "构建表格数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("构建表格数据失败", e);
			if (e instanceof IllegalArgumentException) {
				return ResponseResult.failure(e.getMessage());
			}
			if (e instanceof SheetException)
				return ResponseResult.failure(e.getMessage());
			// 多线程异常会被包一层,所以暂时先用解析的方式去做
			if (e.getMessage().contains("SheetException:"))
				return ResponseResult.failure(e.getMessage().substring(e.getMessage().indexOf("SheetException:") + 15));
			return ResponseResult.failure("构建表格数据失败");
		}
	}

	/**
	 * 构建excel表格单元格数据
	 * 
	 * @param request
	 * @param response
	 * @param sheetId
	 */
	@RequestMapping("/directStatisticList")
	@ResponseBody
	public ResponseResult directStatisticList(HttpServletRequest request, HttpServletResponse response,
			String sheetId) {
		try {
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			//获取json格式的报表参数
			JSONArray paramArray = getSheetParameter2Json(UUID.fromString(sheetId));
			
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(sheet.getDesignId());
			UUID userId = getCurrentLoggedInUser().getId();
			List<SheetData> sheetDatas = sheetComService.getComputeDatas4Excel(userId,UUID.fromString(sheetId),sheet.getDesignId(), paramArray, sheetComService,0);

			// 公式计算
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(sheet.getDesignId());
			List<Map<String, Object>> list = sheetComService.formulaCaculation(sheetDatas, range, expressions);

			return ResponseResult.success(list, list.size(), "构建表格数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("构建表格数据失败", e);
			if (e instanceof IllegalArgumentException) {
				return ResponseResult.failure(e.getMessage());
			}
			if (e instanceof SheetException)
				return ResponseResult.failure(e.getMessage());
			// 多线程异常会被包一层,所以暂时先用解析的方式去做
			if (e.getMessage().contains("SheetException:"))
				return ResponseResult.failure(e.getMessage().substring(e.getMessage().indexOf("SheetException:") + 15));
			return ResponseResult.failure("构建表格数据失败");
		}
	}

	/**
	 * 报表直统
	 */
	@RequestMapping("/directStatistic")
	@ResponseBody
	public ResponseResult directStatistic(HttpServletRequest request, HttpServletResponse response, String sheetId) {
		try {
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
		
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(sheet.getDesignId());
			UUID userId = getCurrentLoggedInUser().getId();
			List<SheetData> sheetDatas = sheetComService.getComputeDatas4Excel(userId,sheet.getId(),sheet.getDesignId(), jsonArray, sheetComService,0);

			// 公式计算
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(sheet.getDesignId());
			List<Map<String, Object>> list = sheetComService.formulaCaculation(sheetDatas, range, expressions);

			return ResponseResult.success(list, list.size(), "报表直统成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("报表直统失败", e);
			if (e instanceof IllegalArgumentException) {
				return ResponseResult.failure(e.getMessage());
			}
			if (e instanceof SheetException)
				return ResponseResult.failure(e.getMessage());
			// 多线程异常会被包一层,所以暂时先用解析的方式去做
			if (e.getMessage().contains("SheetException:"))
				return ResponseResult.failure(e.getMessage().substring(e.getMessage().indexOf("SheetException:") + 15));
			return ResponseResult.failure("构建表格数据失败");
		}
	}

	/**
	 * 
	 * 批量报表直统
	 * 
	 * @param request
	 * @param response
	 * @param sheetId
	 */
	@RequestMapping("/batchDirectStatistic")
	@ResponseBody
	public ResponseResult batchDirectStatistic(HttpServletRequest request, HttpServletResponse response,
			String sheetId) {
		try {
			UUID userId = this.getCurrentLoggedInUser().getId();
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			
			sheetComService.batchDirectStatistic(sheet.getId(),sheet.getDesignId(),jsonArray,sheetComService,userId);
			
			return ResponseResult.success("批量直统成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量直统失败--sheetId："+sheetId, e);
			if (e instanceof IllegalArgumentException) {
				return ResponseResult.failure(e.getMessage());
			}
			if (e instanceof SheetException)
				return ResponseResult.failure(e.getMessage());
			// 多线程异常会被包一层,所以暂时先用解析的方式去做
			if (e.getMessage() != null && e.getMessage().contains("SheetException:"))
				return ResponseResult.failure(e.getMessage().substring(e.getMessage().indexOf("SheetException:") + 15));
			return ResponseResult.failure("构建表格数据失败");
		}

	}
	
	@RequestMapping("/submitStatisticTask")
	@ResponseBody
	public ResponseResult submitStatisticTask(HttpServletRequest request, HttpServletResponse response,
			String designId) {
	    String sheetId=request.getParameter("sheetId");
		try {
			UUID sheetID=null;
			if(sheetId == null){
				sheetID = null;
			}else{
				sheetID = UUID.fromString(sheetId);
			}
			if(sheetID!=null) {
			    Sheet sheet = sheetService.getById(sheetID);
    	        if(sheet!=null) {
    	            boolean archived=(sheet.getArchived()==null)?false:sheet.getArchived();
    	            if(archived) {//已归档
    	                return ResponseResult.failure("不允许直统已归档的报表！");
    	            }
    	        }
			}
			
			//获取json格式的报表参数
			JSONArray jsonArray = new JSONArray();
			if(request.getParameter("templetParams") != null && sheetId == null){
				jsonArray = JSON.parseArray(request.getParameter("templetParams"));
			}else if(sheetId != null){
				jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			}
			//Integer statisticType = getIntegerParam(request, "priority", 0);
			Integer statisticType = getIntegerParam(request, "statisticType", 0);
			UUID userId = getCurrentLoggedInUser().getId();
			sheetComService.getComputeDatas4Excel(userId,sheetID, UUID.fromString(designId), jsonArray, sheetComService,statisticType);
			
			return ResponseResult.success("批量直统成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("批量直统失败--sheetId："+sheetId, e);
			if (e instanceof IllegalArgumentException) {
				return ResponseResult.failure(e.getMessage());
			}
			if (e instanceof SheetException)
				return ResponseResult.failure(e.getMessage());
			// 多线程异常会被包一层,所以暂时先用解析的方式去做
			if (e.getMessage() != null && e.getMessage().contains("SheetException:"))
				return ResponseResult.failure(e.getMessage().substring(e.getMessage().indexOf("SheetException:") + 15));
			return ResponseResult.failure("构建表格数据失败");
		}

	}
	
	@RequestMapping("pollingViewStatus")
	@ResponseBody
	public ResponseResult pollingViewStatus(String sheetId,String computeType,String statisticType,String designId){
		try {
			String key;
			// 1为试算 0为直统
			if("1".equals(computeType)){
				key = designId;
			}else{
				key = sheetId;
			}
			UUID userId = getCurrentLoggedInUser().getId();
			SheetStatisticsStatus status = sheetComService.pollingGetStatus(userId,UUID.fromString(key));
			if(status.getStatus() == -1){
				sheetComService.cancelStatistics(userId,UUID.fromString(key));
				return ResponseResult.success(status,1);
			}
			if(status.getNum().get()<status.getTotal()){
				Map<String, Integer> map = new HashMap<>();
				map.put("total", status.getTotal());
				map.put("current", status.getNum().get());
				map.put("status", 0);
				return ResponseResult.success(map, 1,"未统计完成，请等待");
			}else{
				
				
				Map<String, Object> map = new HashMap<>();
				//1为列表直统保存  0 为 报表内直统回显
				if("0".equals(statisticType)){
					ConcurrentLinkedQueue<SheetData> datas = status.getDatas();
					List<SheetData> sheetDatas = new ArrayList<>(datas);
					Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
							.getCaculationFormulaByDesignId(UUID.fromString(designId));
					// 公式计算
					Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
					List<Map<String, Object>> list = new ArrayList<>();
					if(sheetId != null){
						Collection<SheetData> collection = sheetDataService.getBySheetId(sheetId);
						Map<String, SheetData> dataMap = new HashMap<String, SheetData>();
						if(!CollectionUtils.isEmpty(collection)){
							for (SheetData sheetData : collection) {
								SheetData cloneData = (SheetData) ((BaseService) sheetComService).cloneObject(sheetData);
								cloneData.setId(null);
								cloneData.setCreatedBy(null);
								cloneData.setLastModifiedBy(null);
								cloneData.setSheetId(null);
								dataMap.put(sheetData.getRowNo()+"-"+sheetData.getColumnNo(), cloneData);
							}
						}
						if(!CollectionUtils.isEmpty(sheetDatas)){
							List<SheetData> dataList1 = new LinkedList<>();
							for (SheetData data : sheetDatas) {
								if(dataMap.containsKey(data.getRowNo()+"-"+data.getColumnNo())){
									SheetData sd = dataMap.get(data.getRowNo()+"-"+data.getColumnNo());
									sd.setStringValue(data.getStringValue());
									dataList1.add(sd);
									dataMap.remove(data.getRowNo()+"-"+data.getColumnNo());
								}else{
									dataList1.add(data);
								}
								//dataList1.add(data);
							}
							dataList1.addAll(dataMap.values());
							list = sheetComService.formulaCaculation(sheetId, designId,userId, dataList1);
							List<Map<String,Object>> fillUnit = sheetComService.fillUnit(UUID.fromString(designId), UUID.fromString(sheetId));
							list.addAll(fillUnit);
							for (Map<String, Object> d : list) {
								Map<String, Integer> position = sheetComService.getCtrlPositionByRelativePosition(UUID.fromString(designId), (Integer)d.get("rowNo"), (Integer)d.get("columnNo"));
								d.put("ctrlRowNo", position.get("rowNo"));
								d.put("ctrlColumnNo",position.get("columnNo"));
							}
						}
						
					}else{
						list = sheetComService.formulaCaculation(sheetDatas, range, expressions);
					}
					map.put("status", 1);
					map.put("datas", list);
				}else{
					map.put("status", 1);
				}
				sheetComService.cancelStatistics(userId, UUID.fromString(key));
				return ResponseResult.success(map,1,"统计完成");
			}
		} catch (Exception e) {
			logger.error("轮询查看状态失败",e);
			return ResponseResult.failure("轮询查看状态失败");
		}
	}

	@RequestMapping("/viewSqlDescription")
	@ResponseBody
	public ResponseResult viewSqlDescription(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, Integer rowNo, Integer columnNo) {

		try {
			JSONArray jsonArray = new JSONArray();
			if (StringUtils.isNotBlank(sheetId)) {
				//获取json格式的报表参数
				jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			}
			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));

			Map<String, Object> sqlDescription = sheetComService.getSqlDescription(designDataSources, designFields,
					designConditions, rowNo, columnNo, jsonArray);
			return ResponseResult.success(sqlDescription, 1, "查看sql描述成功");
		} catch (SheetException e) {
			// e.printStackTrace();
			return ResponseResult.failure(e.getMessage());
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
			return ResponseResult.failure(e.getMessage());
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("查看sql描述失败", e);
			return ResponseResult.failure("查看sql描述失败");
		}
	}

	@RequestMapping("/viewWordSqlDescription")
	@ResponseBody
	public ResponseResult viewWordSqlDescription(HttpServletRequest request, HttpServletResponse response,
			String sheetId, String designId, String sectionNo) {

		try {
			JSONArray jsonArray = new JSONArray();
			if (StringUtils.isNotBlank(sheetId)) {
				//获取json格式的报表参数
				jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			}
			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));

			Map<String, Object> sqlDescription = sheetComService.getSqlDescription(designDataSources, designConditions,
					UUID.fromString(designId), sectionNo, jsonArray);
			return ResponseResult.success(sqlDescription, 1, "查看sql描述成功");
		} catch (SheetException e) {
			// e.printStackTrace();
			return ResponseResult.failure(e.getMessage());
		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
			return ResponseResult.failure(e.getMessage());
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("查看sql描述失败", e);
			return ResponseResult.failure("查看sql描述失败");
		}

	}

	/** 查询数据反查 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDataCheck")
	@ResponseBody
	public ResponseResult queryDataCheck(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, Integer rowNo, Integer columnNo, String[] fields) {
		try {
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			
			PageableQueryParameter queryParameter = new PageableQueryParameter();
			queryParameter.setPageNo(getIntegerParam(request, "page", 1));
			queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));

			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(UUID.fromString(designId));

			PageableQueryResult result = sheetComService.getQueryDataByPage(designDataSources, designFields,
					designConditions, queryParameter, rowNo, columnNo, fields, jsonArray);

			int i = 1;
			for (String field : fields) {
				SysInfoItem infoItem = sysInfoItemService.getByColumnName(field.split("\\.")[0], field.split("\\.")[1]);
				if (infoItem != null && StringUtils.isNotBlank(infoItem.getReferenceCodeSet())) {

					for (Map<String, Object> data : (List<Map<String, Object>>) result.getData()) {
						String code = (String) data.get("F" + i);
						if(StringUtils.isNotBlank(code)){
							SysCodeItem codeItem = sysCodeItemService.getByCode(infoItem.getReferenceCodeSet(), code);
							if(codeItem != null){
								data.put("F" + i, codeItem.getName());
							}
						}
					}
				}
				i++;
			}

			return ResponseResult.success(result.getData(), result.getTotalCount(), "获取查询数据反查成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取查询数据反查失败", e);
			return ResponseResult.failure("获取查询数据反查失败");
		}
	}

	/** 查询数据反查 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryWordDataCheck")
	@ResponseBody
	public ResponseResult queryWordDataCheck(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, String sectionNo, String[] fields) {
		try {
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			
			PageableQueryParameter queryParameter = new PageableQueryParameter();
			queryParameter.setPageNo(getIntegerParam(request, "page", 1));
			queryParameter.setPageSize(getIntegerParam(request, "limit", Constants.DEFAULT_PAGE_SIZE));

			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(UUID.fromString(designId));

			PageableQueryResult result = sheetComService.getQueryDataByPage(designDataSources, designFields,
					designConditions, queryParameter, UUID.fromString(designId), sectionNo, fields, jsonArray);

			int i = 1;
			for (String field : fields) {
				SysInfoItem infoItem = sysInfoItemService.getByColumnName(field.split("\\.")[0], field.split("\\.")[1]);
				if (infoItem != null && StringUtils.isNotBlank(infoItem.getReferenceCodeSet())) {

					for (Map<String, Object> data : (List<Map<String, Object>>) result.getData()) {
						String code = (String) data.get("F" + i);
						if(StringUtils.isNotBlank(code)){
							SysCodeItem codeItem = sysCodeItemService.getByCode(infoItem.getReferenceCodeSet(), code);
							if(codeItem != null){
								data.put("F" + i, codeItem.getName());
							}
						}
					}
				}
				i++;
			}

			return ResponseResult.success(result.getData(), result.getTotalCount(), "获取查询数据反查成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取查询数据反查失败", e);
			return ResponseResult.failure("获取查询数据反查失败");
		}
	}

	/** 导出查询数据反查 */
	@RequestMapping("/exportQueryData")
	public void exportQueryData(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, Integer rowNo, Integer columnNo, String[] fields, String[] fieldsDes) {
		String id = request.getSession().getId();
		try {

			if (currentLine > maxComplicate) {
				writeJson(response, ResponseResult.failure("超过最大并发数"));
				return;
			} else if (userList.contains(id)) {
				writeJson(response, ResponseResult.failure("正在下载..."));
				return;
			}
			userList.add(id);
			currentLine++;
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			
			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(UUID.fromString(designId));

			List<Map<String, Object>> result = sheetComService.getQueryData(designDataSources, designFields,
					designConditions, rowNo, columnNo, fields, jsonArray);
			int i = 1;
			for (String field : fields) {
				SysInfoItem infoItem = sysInfoItemService.getByColumnName(field.split("\\.")[0], field.split("\\.")[1]);
				if (infoItem != null && StringUtils.isNotBlank(infoItem.getReferenceCodeSet())) {

					for (Map<String, Object> data : result) {
						String code = (String) data.get("F" + i);
						if(StringUtils.isNotBlank(code)){
							SysCodeItem codeItem = sysCodeItemService.getByCode(infoItem.getReferenceCodeSet(), code);
							if(codeItem != null){
								data.put("F" + i, codeItem.getName());
							}
						}
					}
				}
				i++;
			}
			sheetComService.exportQueryData(fieldsDes, result, response);
		} catch (Exception e) {
			logger.error("获取查询数据反查失败", e);
			writeJson(response, "导出数据失败");
		} finally {
			currentLine--;
			if (userList.contains(id)) {
				userList.remove(id);
			}
		}
	}

	/** 导出查询数据反查 */
	@RequestMapping("/exportWordQueryData")
	public void exportWordQueryData(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, String sectionNo, String[] fields, String[] fieldsDes) {
		String id = request.getSession().getId();
		try {

			if (currentLine > maxComplicate) {
				writeJson(response, ResponseResult.failure("超过最大并发数"));
				return;
			} else if (userList.contains(id)) {
				writeJson(response, ResponseResult.failure("正在下载..."));
				return;
			}
			userList.add(id);
			currentLine++;
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			
			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(UUID.fromString(designId));

			List<Map<String, Object>> result = sheetComService.getQueryData(designDataSources, designFields,
					designConditions, UUID.fromString(designId), sectionNo, fields, jsonArray);
			int i = 1;
			for (String field : fields) {
				SysInfoItem infoItem = sysInfoItemService.getByColumnName(field.split("\\.")[0], field.split("\\.")[1]);
				if (infoItem != null && StringUtils.isNotBlank(infoItem.getReferenceCodeSet())) {

					for (Map<String, Object> data : result) {
						String code = (String) data.get("F" + i);
						if(StringUtils.isNotBlank(code)){
							SysCodeItem codeItem = sysCodeItemService.getByCode(infoItem.getReferenceCodeSet(), code);
							if(codeItem != null){
								data.put("F" + i, codeItem.getName());
							}
						}
					}
				}
				i++;
			}
			sheetComService.exportQueryData(fieldsDes, result, response);
			writeJson(response, "导出数据成功");

		} catch (Exception e) {
			logger.error("获取查询数据反查失败", e);
			writeJson(response, "导出数据失败");
		} finally {
			currentLine--;
			if (userList.contains(id)) {
				userList.remove(id);
			}
		}
	}

	/** 查询数据反查SQL */
	@RequestMapping("/viewQueryDataSql")
	@ResponseBody
	public ResponseResult viewQueryDataSql(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, Integer rowNo, Integer columnNo, String[] fields, String[] fieldsDes) {
		try {
			JSONArray jsonArray = new JSONArray();
			if (StringUtils.isNotBlank(sheetId)) {
				//获取json格式的报表参数
				jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			}

			Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));
			Collection<SheetDesignField> designFields = sheetDesignFieldService
					.getByDesignId(UUID.fromString(designId));

			Map<String, Object> result = sheetComService.getQueryDataSql(designDataSources, designFields,
					designConditions, rowNo, columnNo, fields, fieldsDes, jsonArray);
			return ResponseResult.success(result, 1, "获取查询数据SQL描述成功");
		} catch (Exception e) {
			logger.error("获取查询数据SQL描述失败", e);
			return ResponseResult.failure("获取查询数据SQL描述失败");
		}
	}

	/**word模板试算*/
	@RequestMapping("/buildWordData")
	@ResponseBody
	public ResponseResult buildWordData(HttpServletRequest request, HttpServletResponse response, String designId) {
		try {
			JSONArray paramArray = new JSONArray();
			if (request.getParameter("templetParams") != null) {
				paramArray = JSON.parseArray(request.getParameter("templetParams"));
			}
			// 获取构建word需要的数据
			List<Map<String, Object>> datas = getBuildWordData(designId, paramArray);
			SheetDesignCarrier designCarrier = sheetDesignCarrierService.getByDesignId(UUID.fromString(designId));
			sheetComService.fillData2Pdf(designCarrier.getContent(), datas, response);
			return ResponseResult.success("成功");
		} catch (Exception e) {
			logger.error("构建word数据失败", e);
			return ResponseResult.failure("失败");
		}
	}

	// word报表直统
	@RequestMapping("/computeWordData")
	@ResponseBody
	public ResponseResult computeWordData(HttpServletRequest request, HttpServletResponse response, String sheetId) {
		try {
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			// 获取构建word需要的数据
			List<Map<String, Object>> wordDatas = getBuildWordData(sheet.getDesignId().toString(), jsonArray);
			SheetDesignCarrier designCarrier = sheetDesignCarrierService.getByDesignId(sheet.getDesignId());
			sheetComService.fillData2Pdf(designCarrier.getContent(), wordDatas, response);
			return ResponseResult.success("成功");
		} catch (Exception e) {
			logger.error("构建word数据失败", e);
			return ResponseResult.failure("失败");
		}
	}

	/**word报表直统保存*/
	@RequestMapping("/directStatisticsWordData")
	@ResponseBody
	public ResponseResult directStatisticsWordData(HttpServletRequest request, HttpServletResponse response,
			String sheetId) {
		try {
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			//获取json格式的报表参数
			JSONArray jsonArray = getSheetParameter2Json(UUID.fromString(sheetId));
			// 获取构建word需要的数据
			List<Map<String, Object>> wordDatas = getBuildWordData(sheet.getDesignId().toString(), jsonArray);
			// 保存数据
			List<SheetData> sheetDatas = new ArrayList<>();
			for (Map<String, Object> map : wordDatas) {
				SheetData sheetData = new SheetData();
				sheetData.setId(UUID.randomUUID());
				sheetData.setSheetId(sheet.getId());
				sheetData.setBeingEdited(false);
				sheetData.setSectionNo((String) map.get("sectionNo"));
				sheetData.setStringValue((String) map.get("bindValue"));
				sheetData.setCreatedBy(this.getCurrentLoggedInUser().getId());
				sheetData.setCreatedTime(new Timestamp(System.currentTimeMillis()));
				sheetData.setLastModifiedBy(this.getCurrentLoggedInUser().getId());
				sheetData.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
				sheetData.setStatus(DataStatus.NORMAL.getValue());
				sheetData.setRowNo(-1);
				sheetData.setColumnNo(-1);
				sheetDatas.add(sheetData);
			}
			sheetService.saveInfosAndDatas(sheet.getId(), sheetDatas, null, null,true);
			return ResponseResult.success("直统word成功");
		} catch (Exception e) {
			logger.error("直统word失败", e);
			return ResponseResult.failure("直统word失败");
		}
	}

	@RequestMapping("/wordData2Pdf")
	@ResponseBody
	public ResponseResult wordData2Pdf(HttpServletRequest request, HttpServletResponse response, String sheetId) {
		try {
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			// 获取构建word需要的数据
			Collection<Map<String, Object>> datas = sheetDataService.getBuildWordDatas(UUID.fromString(sheetId));

			SheetDesignCarrier designCarrier = sheetDesignCarrierService.getByDesignId(sheet.getDesignId());
			sheetComService.fillData2Pdf(designCarrier.getContent(), datas, response);
			return ResponseResult.success("成功");
		} catch (Exception e) {
			logger.error("构建word数据失败", e);
			return ResponseResult.failure("失败");
		}
	}

	/**
	 * 导出word
	 */
	@RequestMapping("/exportWord")
	public void exportWord(HttpServletRequest request, HttpServletResponse response, String sheetId) {
		String id = request.getSession().getId();
		try {
			if (currentLine > maxComplicate) {
				writeJson(response, ResponseResult.failure("超过最大并发数"));
				return;
			} else if (userList.contains(id)) {
				writeJson(response, ResponseResult.failure("正在下载..."));
				return;
			}
			userList.add(id);
			currentLine++;
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			// 获取构建word需要的数据
			Collection<Map<String, Object>> datas = sheetDataService.getBuildWordDatas(UUID.fromString(sheetId));

			SheetDesignCarrier designCarrier = sheetDesignCarrierService.getByDesignId(sheet.getDesignId());
			sheetComService.fillData2Word(designCarrier.getContent(), datas, response, sheet.getName());
		} catch (Exception e) {
			logger.error("导出word失败", e);
			writeJson(response, ResponseResult.failure("导出word失败"));
		} finally {
			currentLine--;
			if (userList.contains(id)) {
				userList.remove(id);
			}
		}
	}

	/**
	 * 公式运算数据反查
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/formulaCalculationDataCheck")
	@ResponseBody
	public ResponseResult formulaCalculationDataCheck(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> checkInfos) {
		try {
			String designId = (String) checkInfos.get("designId");
			Integer rowNo = (Integer) checkInfos.get("rowNo");
			Integer columnNo = (Integer) checkInfos.get("columnNo");
			List<Map<String, Object>> sheetDatas = (List<Map<String, Object>>) checkInfos.get("sheetDatas");
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(UUID.fromString(designId));
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
			
			Map<String, Object> formulaCaculationData = sheetComService.getFormulaCaculationData((String)sheetDatas.get(0).get("sheetId"), designId, sheetDatas, rowNo, columnNo);
			return ResponseResult.success(formulaCaculationData, 1, "公式运算数据反查成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("公式运算数据反查失败", e);
			return ResponseResult.failure("公式运算数据反查失败");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/exportFormulaCalculaitionData")
	public void exportFormulaCalculaitionData(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Map<String, Object> checkInfos) {
		String id = request.getSession().getId();
		try {
			if (currentLine > maxComplicate) {
				writeJson(response, ResponseResult.failure("超过最大并发数"));
				return;
			} else if (userList.contains(id)) {
				writeJson(response, ResponseResult.failure("正在下载..."));
				return;
			}
			userList.add(id);
			currentLine++;
			String designId = (String) checkInfos.get("designId");
			Integer rowNo = (Integer) checkInfos.get("rowNo");
			Integer columnNo = (Integer) checkInfos.get("columnNo");
			List<Map<String, Object>> sheetDatas = (List<Map<String, Object>>) checkInfos.get("sheetDatas");
			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
					.getCaculationFormulaByDesignId(UUID.fromString(designId));
			Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
			Map<String, Object> formulaCaculationData = sheetComService.getFormulaCaculationData(sheetDatas, range,
					expressions, rowNo, columnNo);
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			for (Object sheetData : (List) formulaCaculationData.get("datas")) {
				Map json = (Map) JSON.toJSON(sheetData);
				dataList.add(json);
			}
			sheetComService.exportFormulaCaculationData(dataList, response);
		} catch (Exception e) {
			logger.error("导出公式运算数据失败", e);
			writeJson(response, ResponseResult.failure("导出公式运算数据失败"));
		} finally {
			currentLine--;
			if (userList.contains(id)) {
				userList.remove(id);
			}
		}

	}

	/**
	 * 根据绝对位置获取相对位置
	 */
	@RequestMapping("/getRelativePositionByCtrlPosition")
	@ResponseBody
	public ResponseResult getRelativePositionByCtrlPosition(String designId, Integer ctrlRowNo, Integer ctrlColumnNo) {
		try {
			Collection<SheetDesignSection> sections = sheetDesignSectionService
					.getByDesignId(UUID.fromString(designId));
			SheetDesignSection section = sheetComService.getUniqueSectionByCtrl(sections, ctrlRowNo + 1,
					ctrlColumnNo + 1);
			// 数据块绝对位置和相对位置的差值，确定单元格的绝对位置
			int betweenRow = section.getCtrlRowStart() - section.getStartRowNo();
			int betweenColumn = section.getCtrlColumnStart() - section.getStartColumnNo();

			Map<String, Integer> position = new HashMap<String, Integer>();
			position.put("rowNo", ctrlRowNo - betweenRow + 1);
			position.put("columnNo", ctrlColumnNo - betweenColumn + 1);
			return ResponseResult.success(position, 1, "根据绝对位置获取相对位置成功");
		} catch (Exception e) {
			logger.error("根据绝对位置获取相对位置失败", e);
			return ResponseResult.failure("根据绝对位置获取相对位置失败");
		}
	}
	
	/**
	 * 数据校核
	 */
	@RequestMapping("/formulaCheck")
	@ResponseBody
	public ResponseResult formulaCheck(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId) {
		try {
			
			List<Map<String, Object>> result = sheetComService.formulaCheck(sheetId, designId, null);
			byte innerCheckStatus = 1; // 表内校核状态
			byte outerCheckStatus = 1; // 表间校核状态
			for (Map<String, Object> logic : result) {
				if("1".equals(logic.get("status"))){
					if (1 == (byte) logic.get("type") && !((boolean) logic.get("value"))) {
						innerCheckStatus = 0;
					} else if (3 == (byte) logic.get("type") && !((boolean) logic.get("value"))) {
						outerCheckStatus = 0;
					}
				}
			}
			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
			sheet.setInnerCheckStatus(innerCheckStatus);
			sheet.setOuterCheckStatus(outerCheckStatus);
			sheetService.update(sheet);
			return ResponseResult.success(result, result.size(), "数据校核成功");
		} catch (SheetException e) {
			logger.error(e.getMessage(), e);
			return ResponseResult.failure(e.getMessage());
		} catch (Exception e) {
			logger.error("数据校核失败", e);
			return ResponseResult.failure("数据校核失败");
		}
	}
	

//	/**
//	 * 数据校核
//	 */
//	@RequestMapping("/formulaCheck")
//	@ResponseBody
//	public ResponseResult formulaCheck(HttpServletRequest request, HttpServletResponse response, String sheetId,
//			String designId) {
//		try {
//			// 获取模板下所有校核表达式
//			Collection<SheetDesignExpression> expressions = sheetDesignExpressionService
//					.getCheckFormulaByDesignId(UUID.fromString(designId));
//			List<Map<String, Object>> result = new ArrayList<>();
//			Map<String, Map<String, Object>> dataMap = new HashMap<>();
//			if (!CollectionUtils.isEmpty(expressions)) {
//				// 把表号解析出来
//				Set<String> set = analyzeSheetNoByExpression(expressions);
//				
//				//--
//				Map<String, String> nos = new HashMap<String, String>();
//				int i = 1;
//				for (String string : set) {
//					nos.put(string, i+"");
//					i++;
//				}
//				//--
//				if (!CollectionUtils.isEmpty(set)) {
//					// 根据sheetId获取参数，并根据参数和表号获取需要校核的报表数据
//					Collection<SheetParameter> parameters = sheetParameterService
//							.getBySheetId(UUID.fromString(sheetId));
//					Collection<SheetInfo> sheetInfos = sheetComService.getSheetInfos(set, parameters);
//					dataMap = generateDataMap(sheetInfos, sheetId,nos);
//				} else {
//					Collection<SheetData> datas = sheetDataService.getBySheetId(sheetId);
//					SheetDesign sheetDesign = sheetDesignService.getById(UUID.fromString(designId));
//					Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
//					for (SheetData sheetData : datas) {
//						Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
//						data.put("sheetNo", sheetDesign.getSheetNo());
//						data.put("sheetName", sheet.getName());
//						dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
//					}
//				}
//
//				Map<String, Integer> range = sheetDesignSectionService.getRangeByDesignId(UUID.fromString(designId));
//
//				for (SheetDesignExpression expression : expressions) {
//					SheetEvalueateListener listener = null;
//					String regex = "T([a-zA-Z0-9\\.]*)R";
//					Pattern pattern = Pattern.compile(regex);
//					try {
//						
//						Matcher matcher = pattern.matcher(expression.getContent());
//						String content = expression.getContent();
//						while (matcher.find()) {
//							String group = matcher.group(1);
//							String string = nos.get(group);
//							content = content.replaceAll("T"+group+"R", "T"+string+"R");
//						}
//						
//						listener = sheetComService.analyzeFormula(content + ";", dataMap,
//								range.get("startRowNo"), range.get("endRowNo"), range.get("startColumnNo"),
//								range.get("endColumnNo"), expression.getType());
//					} catch (Exception e) {
//						logger.error("公式：" + expression.getName() + " 有误，请检查！", e);
//						throw new SheetException("公式：" + expression.getName() + " 有误，请检查！", e);
//					}
//					for (Map<String, Object> map : listener.getLogicResult()) {
//						map.put("expressionId", expression.getId());
//						String statement = (String)map.get("statement");
//						Matcher matcher = pattern.matcher(statement);
//						while(matcher.find()){
//							String group = matcher.group(1);
//							for (String string : nos.keySet()) {
//								if(StringUtils.equals(nos.get(string), group)){
//									statement = statement.replaceAll("T"+group+"R", "T"+string+"R");
//									map.put("statement",statement);
//								}
//							}
//							
//						}
//					}
//					result.addAll(listener.getLogicResult());
//				}
//			}
//			byte innerCheckStatus = 1; // 表内校核状态
//			byte outerCheckStatus = 1; // 表间校核状态
//			for (Map<String, Object> logic : result) {
//				if (1 == (byte) logic.get("type") && !((boolean) logic.get("value"))) {
//					innerCheckStatus = 0;
//				} else if (3 == (byte) logic.get("type") && !((boolean) logic.get("value"))) {
//					outerCheckStatus = 0;
//				}
//			}
//			Sheet sheet = sheetService.getById(UUID.fromString(sheetId));
//			sheet.setInnerCheckStatus(innerCheckStatus);
//			sheet.setOuterCheckStatus(outerCheckStatus);
//			sheetService.update(sheet);
//			return ResponseResult.success(result, result.size(), "数据校核成功");
//		} catch (SheetException e) {
//			logger.error(e.getMessage(), e);
//			return ResponseResult.failure(e.getMessage());
//		} catch (Exception e) {
//			logger.error("数据校核失败", e);
//			return ResponseResult.failure("数据校核失败");
//		}
//	}

	/**
	 * 根据公式查看校核结果
	 */
	@RequestMapping("/formulaCheckData")
	@ResponseBody
	public ResponseResult formulaCheckData(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, String statement) {
		try {
			List<Map<String,Object>> checkDatas = sheetComService.formulaCheck(sheetId, designId, statement);

			return ResponseResult.success(checkDatas, checkDatas.size(), "获取公式校核数据成功");
		} catch (Exception e) {
			logger.error("获取公式校核数据失败", e);
			return ResponseResult.failure("获取公式校核数据失败");
		}
	}

	/**
	 * 导出公式查看校核结果
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/exportFormulaCheckData")
	public void exportFormulaCheckData(HttpServletRequest request, HttpServletResponse response, String sheetId,
			String designId, String statement) {
		String id = request.getSession().getId();
		try {
			if (currentLine > maxComplicate) {
				writeJson(response, ResponseResult.failure("超过最大并发数"));
				return;
			} else if (userList.contains(id)) {
				writeJson(response, ResponseResult.failure("正在下载..."));
				return;
			}
			userList.add(id);
			currentLine++;
			
			List<Map<String,Object>> checkDatas = sheetComService.formulaCheck(sheetId, designId, statement);
			List<Map<String, Object>> datas = new ArrayList<>();
			for (Map<String, Object> map : checkDatas) {
				datas.addAll((List) map.get("checkDatas"));
			}
			sheetComService.exportFormulaCaculationData(datas, response);
			

		} catch (Exception e) {
			logger.error("导出公式校核数据失败", e);
			writeJson(response, ResponseResult.failure("导出公式运算数据失败"));
		} finally {
			currentLine--;
			if (userList.contains(id)) {
				userList.remove(id);
			}
		}
	}

	/**
	 * 获取单元格绑定类型
	 * 
	 * @param request
	 * @param response
	 * @param designId
	 */
	@RequestMapping("/getCellBindType")
	@ResponseBody
	public ResponseResult getCellBindType(HttpServletRequest request, HttpServletResponse response, String designId) {
		try {

			// 查出同一designId的数据块、数据源、数据项、条件项
			Collection<SheetDesignSection> sections = sheetDesignSectionService
					.getByDesignId(UUID.fromString(designId));
			List<SheetDesignCondition> designConditions = sheetDesignConditionService
					.getByDesignId(UUID.fromString(designId));

			List<Map<String, Object>> bindList = new ArrayList<>();

			for (SheetDesignSection sheetDesignSection : sections.stream().filter(item -> !item.getAutoExpand())
					.collect(Collectors.toList())) {

				// 数据块绝对位置和相对位置的差值，确定单元格的绝对位置
				int betweenRow = sheetDesignSection.getCtrlRowStart() - sheetDesignSection.getStartRowNo();
				int betweenColumn = sheetDesignSection.getCtrlColumnStart() - sheetDesignSection.getStartColumnNo();

				for (int i = sheetDesignSection.getStartRowNo(); i <= sheetDesignSection.getEndRowNo(); i++) {

					for (int j = sheetDesignSection.getStartColumnNo(); j <= sheetDesignSection.getEndColumnNo(); j++) {

						// 符合条件的条件项集合
						Collection<SheetDesignCondition> conditions = sheetComService
								.getDesignConditions(designConditions, i, j);
						// 获取绑定类型
						String bindType = getBindType(conditions);
						Map<String, Object> bind = new HashMap<>();
						bind.put("ctrlRowNo", i + betweenRow - 1);
						bind.put("ctrlColumnNo", j + betweenColumn - 1);
						bind.put("bindType", bindType);
						bindList.add(bind);
					}
				}
			}

			return ResponseResult.success(bindList, bindList.size(), "获取单元格绑定类型成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取单元格绑定类型失败", e);
			return ResponseResult.failure("获取单元格绑定类型失败");
		}
	}
	
	@RequestMapping("/cancelStatistics")
	@ResponseBody
	public ResponseResult cancelStatistics(String sheetId,String designId){
		try {
			UUID key;
			if(sheetId == null){
				key = UUID.fromString(designId);
			}else{
				key = UUID.fromString(sheetId);
			}
			UUID userId = getCurrentLoggedInUser().getId();
			sheetComService.cancelStatistics(userId,key);
			return ResponseResult.success("取消统计成功");
		} catch (Exception e) {
			logger.error("取消统计失败",e);
			return ResponseResult.failure("取消统计失败");
		}
	}
	
	@RequestMapping("viewSheetSize")
	@ResponseBody
	public ResponseResult viewSheetSize(){
		try {
			Map<String,Object> map = sheetComService.viewSheetSize();
			return ResponseResult.success(map, 1);
		} catch (Exception e) {
			logger.error("TODO:异常描述",e);
			return ResponseResult.failure();
		}
	}

	/**
	 * 构建表格数据map 格式：key：t-r-c value：data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Map<String, Object>> generateDataMap(Collection<SheetInfo> sheetInfos, String sheetId,Map<String, String> nos) {
		Map<String, Map<String, Object>> dataMap = new HashMap<>();

		for (SheetInfo sheetInfo : sheetInfos) {
			Collection<SheetData> sheetDatas = sheetDataService.getBySheetId(sheetInfo.getSheetId());

			for (SheetData sheetData : sheetDatas) {
				Map<String, Object> data = (Map<String, Object>) JSON.toJSON(sheetData);
				data.put("sheetNo", sheetInfo.getSheetNo());
				data.put("sheetName", sheetInfo.getSheetName());
				// key 表号-行号-列号 value 单元格map数据
				dataMap.put(nos.get(sheetInfo.getSheetNo()) + "-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
				if (StringUtils.equals(sheetId, sheetInfo.getSheetId().toString())) {
					// 表内校核表号默认为0
					dataMap.put("0-" + sheetData.getRowNo() + "-" + sheetData.getColumnNo(), data);
				}
			}
		}
		return dataMap;
	}

	/**
	 * 
	 * 根据表达式集合解析出表号
	 * 
	 * @param expressions
	 *            表达式集合
	 * @return 不重复的表号集合
	 */
	private Set<String> analyzeSheetNoByExpression(Collection<SheetDesignExpression> expressions) {
		Set<String> set = new HashSet<>();// 表号集合
		String regex = "T([a-zA-Z0-9\\.]*)R";
		for (SheetDesignExpression sheetDesignExpression : expressions) {
			String content = sheetDesignExpression.getContent();
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				String tableNo = matcher.group(1);
				set.add(tableNo);
			}
		}
		return set;
	}

	/**
	 * 获取绑定类型
	 */
	private String getBindType(Collection<SheetDesignCondition> conditions) {
		List<String> bindTypes = new ArrayList<>();
		for (SheetDesignCondition condition : conditions) {
			if (condition.getStartRowNo() == -1 && condition.getEndRowNo() == -1 && condition.getStartColumnNo() == -1
					&& condition.getEndColumnNo() == -1) {
				bindTypes.add("1");// 全表绑定
			} else if (condition.getStartColumnNo() == -1 && condition.getEndColumnNo() == -1) {
				bindTypes.add("2");// 行绑定
			} else if (condition.getStartRowNo() == -1 && condition.getEndRowNo() == -1) {
				bindTypes.add("3");// 列表绑定
			} else if (condition.getStartRowNo().equals(condition.getEndRowNo())
					&& condition.getStartColumnNo().equals(condition.getEndColumnNo())) {
				bindTypes.add("4");// 单元格绑定
			} else {
				bindTypes.add("5");// 块绑定
			}
		}
		String bindType = "";
		if (bindTypes.size() == 1) {
			bindType = bindTypes.get(0);
		} else if (bindTypes.size() > 1) {
			if (bindTypes.contains("2") && bindTypes.contains("3")) {
				bindType = "4";
			} else if (bindTypes.contains("2") && bindTypes.contains("5")) {
				bindType = "4";
			} else if (bindTypes.contains("3") && bindTypes.contains("5")) {
				bindType = "4";
			} else if (bindTypes.contains("4")) {
				bindType = "4";
			} else if (bindTypes.contains("3")) {
				bindType = "3";
			} else if (bindTypes.contains("2")) {
				bindType = "2";
			} else if (!bindTypes.contains("2") && !bindTypes.contains("3") && !bindTypes.contains("4")) {
				int size = 0;
				for (String type : bindTypes) {
					if ("5".equals(type)) {
						size++;
					}
				}
				if (size == 0) {
					bindType = "1";
				} else if (size == 1) {
					bindType = "5";
				} else {
					bindType = "6";
				}
			}
		}
		return bindType;
	}

	/**
	 * 
	 * 获取构建word需要的数据
	 * 
	 * @param designId
	 * @return
	 * @throws SheetException
	 */
	private List<Map<String, Object>> getBuildWordData(String designId, JSONArray jsonArray) throws SheetException {
		// 查出同一designId的数据块、数据源、数据项、条件项
		Collection<SheetDesignSection> sections = sheetDesignSectionService.getByDesignId(UUID.fromString(designId));
		Collection<SheetDesignDataSource> designDataSources = sheetDesignDataSourceService
				.getByDesignId(UUID.fromString(designId));
		
		List<SheetDesignCondition> designConditions = sheetDesignConditionService
				.getByDesignId(UUID.fromString(designId));
		List<Map<String, Object>> wordDatas = new ArrayList<>();
		for (SheetDesignSection section : sections) {
			SheetDesignDataSource dataSource = sheetComService.getUniqueDataSource(designDataSources,
					section.getSectionNo());
			SheetDesignField designField = sheetDesignFieldService.getBingData(UUID.fromString(designId),
					section.getSectionNo());
			Collection<SheetDesignCondition> conditions = sheetComService.getDesignConditions(designConditions,
					section.getSectionNo());
			if (designField != null) {
				String querySql = sheetComService.getQuerySql(dataSource, designField, conditions, jsonArray, null);
				Object object = sheetComService.getValueBySql(querySql);
				// section.setBindValue(object==null?"":object.toString());
				Map<String, Object> data = new HashMap<>();
				data.put("name", section.getName());
				data.put("sectionNo", section.getSectionNo());
				data.put("bindValue", object == null ? "" : object.toString());
				wordDatas.add(data);
			}

		}
		return wordDatas;
	}
	
	/**
	 * 获取报表参数并转换成JSON数组
	 * @param sheetId
	 */
	public JSONArray getSheetParameter2Json(UUID sheetId){
		Collection<SheetParameter> parameters = sheetParameterService.getBySheetId(sheetId);
		JSONArray jsonArray = new JSONArray();
		if (!CollectionUtils.isEmpty(parameters)) {
			for (SheetParameter parameter : parameters) {
				JSONObject json = new JSONObject();
				json.put("name", parameter.getParameterName());
				json.put("value", parameter.getParameterValue());
				jsonArray.add(json);
			}
		}
		return jsonArray;
	}
}
