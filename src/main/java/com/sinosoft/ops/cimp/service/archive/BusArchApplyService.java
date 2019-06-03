package com.sinosoft.ops.cimp.service.archive;

import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyDetail;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyPerson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BusArchApplyService {
	public void update(BusArchApply entity);

	/**
	 * 创建申请
	 * @param entity
	 * @param baplist
	 * @param badlist
	 * @throws Exception
	 */
	public void create(BusArchApply entity, List<BusArchApplyPerson> baplist, List<BusArchApplyDetail> badlist)  throws Exception;

	/**
	 * 修改申请查看档案（-1删除）
	 * @param entity
	 * @param baplist
	 * @param badlist
	 * @throws Exception
	 */
	public void update(BusArchApply entity, List<BusArchApplyPerson> baplist, List<BusArchApplyDetail> badlist)  throws Exception;

	/**
	 * 修改查看申请
	 * @param id
	 * @param type
	 * @throws Exception
	 */
	public void updateflg(String id, Integer type) throws Exception;

	/**
	 * 查看申请和审批
	 * @param userid
	 * @param resourceid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public Map<String,Object> getApplyByUser(String userid, String resourceid, Integer pageIndex, Integer pageSize);

	/**
	 * 申请和审批档案树
	 * @param applyid
	 * @param empid
	 * @return
	 */
	public List<HashMap<String, Object>> getTreeByApplyId(String applyid, String empid);

	/**
	 * 根据id查看申请详情
	 * @param applyid
	 * @return
	 */
	public List<HashMap<String, Object>> getPersonByApplyId(String applyid);
	public List<HashMap<String, Object>> getDetailByPersonId(String personid, String empid);

}
