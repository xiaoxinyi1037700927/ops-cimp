package com.sinosoft.ops.cimp.service.archive;

import com.sinosoft.ops.cimp.service.archive.bean.bean.PersonAndPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BusinessService{
	/**
	 * 根据depid查申请人信息和岗位
	 * @param Depid
	 * @return
	 */
	public List<PersonAndPost> getPersonAndPostByDepid(String Depid);

	/**
	 * 根据empid查看树结构
	 * @param empid
	 * @return
	 */
	public List<HashMap<String, Object>> getPersonMaterial(String empid);
	public List<String> findRoleNameByUserId(String upperCase);
	public List<Map<String, Object>> getA02ByDepid(String Depid);
	public int updateA02Data(List<Object> oblist) ;
}
