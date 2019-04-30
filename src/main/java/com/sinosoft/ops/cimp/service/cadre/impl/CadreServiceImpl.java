package com.sinosoft.ops.cimp.service.cadre.impl;

import com.sinosoft.ops.cimp.service.cadre.CadreService;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreBasicInfoVO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CadreServiceImpl implements CadreService {

    private final JdbcTemplate jdbcTemplate;

    public CadreServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CadreBasicInfoVO getCadreBasicInfo(String empId) {
        String sql = String.format("select a001.EMP_ID as \"empId\", " +
                "  a001.A01001 as \"name\", " +
                "  a001.A01002_A as \"namePinYin\", " +
                "  (select name from SYS_CODE_ITEM where CODE_SET_ID = 18 and code = a001.A01004) as \"gender\",  " +
                " to_char(A01007,'yyyy-mm-dd') as \"birthday\", " +
                "  a001.A01011_A as \"birthPlace\", " +
                "  photo.SUB_ID as \"photoId\" " +
                " from EMP_A001 a001 left join EMP_PHOTO photo on a001.EMP_ID = photo.EMP_ID " +
                " where a001.EMP_ID = '%s' ", empId);

        Map<String, Object> data = jdbcTemplate.queryForMap(sql);

        CadreBasicInfoVO result = new CadreBasicInfoVO();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(result.getClass());
            PropertyDescriptor[] proDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor prop : proDescriptors) {
                if (data.containsKey(prop.getName())) {
                    prop.getWriteMethod().invoke(result, data.get(prop.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public byte[] getPhoto(String photoId) {
        Map<String, Object> data = jdbcTemplate.queryForMap(String.format("select PHOTO_FILE from EMP_PHOTO where SUB_ID = '%s'", photoId));
        if (data.size() > 0) {
            return (byte[]) data.get("PHOTO_FILE");
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Object> searchCadres(List<String> cadreTagIds, HashMap<String, Object> tableConditions) {

        return null;
    }
}
