package com.sinosoft.ops.cimp.service.archive;

import com.sinosoft.ops.cimp.entity.archive.BusArchApply;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyDetail;
import com.sinosoft.ops.cimp.entity.archive.BusArchApplyPerson;

import java.util.HashMap;
import java.util.List;

public interface BusArchApplyService {
	public void update(BusArchApply entity);
	public void create(BusArchApply entity, List<BusArchApplyPerson> baplist, List<BusArchApplyDetail> badlist)  throws Exception;
	public void update(BusArchApply entity, List<BusArchApplyPerson> baplist, List<BusArchApplyDetail> badlist, List<String> listDelPerson, List<String> listDelArch)  throws Exception;
	public void updateflg(String id, Integer type) throws Exception;
	public List<BusArchApply> getApplyByUser(String userid, String resourceid);
	public List<HashMap<String, Object>> getTreeByApplyId(String applyid, String empid);
	public List<HashMap<String, Object>> getPersonByApplyId(String applyid);
	public List<HashMap<String, Object>> getDetailByPersonId(String personid, String empid);
}
