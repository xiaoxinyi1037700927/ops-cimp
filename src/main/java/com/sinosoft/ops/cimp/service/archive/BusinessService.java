package com.sinosoft.ops.cimp.service.archive;

import com.sinosoft.ops.cimp.service.archive.bean.bean.PersonAndPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BusinessService{
	public List<PersonAndPost> getPersonAndPostByDepid(String Depid);
	public List<HashMap<String, Object>> getPersonMaterial(String empid);
	public List<String> findRoleNameByUserId(String upperCase);
	public List<Map<String, Object>> getA02ByDepid(String Depid);
	public int updateA02Data(List<Object> oblist) ;
}
