package com.sinosoft.ops.cimp.service.cadre.impl;

import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.entity.emp.EmpPhoto;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.export.ExportManager;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbHtmlBiJie;
import com.sinosoft.ops.cimp.export.handlers.impl.ExportGbrmbWordBiJie;
import com.sinosoft.ops.cimp.repository.emp.EmpPhotoRepository;
import com.sinosoft.ops.cimp.service.cadre.CadreService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreOrgModifyModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreSortInDepModifyModel;
import com.sinosoft.ops.cimp.vo.from.cadre.CadreStatusModifyModel;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreBasicInfoVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreSearchVO;
import com.sinosoft.ops.cimp.vo.to.cadre.CadreSortInDepModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class CadreServiceImpl implements CadreService {

    private final JdbcTemplate jdbcTemplate;
    private final EmpPhotoRepository empPhotoRepository;
    private final SysTableInfoDao sysTableInfoDao;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);


    public CadreServiceImpl(JdbcTemplate jdbcTemplate, EmpPhotoRepository empPhotoRepository, SysTableInfoDao sysTableInfoDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.empPhotoRepository = empPhotoRepository;
        this.sysTableInfoDao = sysTableInfoDao;
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
    public byte[] getPhoto(String empId) {
        Map<String, Object> data = jdbcTemplate.queryForMap(String.format("select PHOTO_FILE from EMP_PHOTO where ROWNUM = 1 AND EMP_ID = '%s'", empId));
        if (data.size() > 0) {
            return (byte[]) data.get("PHOTO_FILE");
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Object> searchCadres(CadreSearchVO searchVO) throws BusinessException {
        Map<String, Object> resultMap = Maps.newHashMap();

        String searchCadreSql = "SELECT\n" +
                "  EMP_ID                                      AS \"EMP_ID\",\n" +
                "  A01001                                      AS \"a01001\",\n" +
                "  A01004                                      AS \"a01004\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 18 AND code = A01004)  AS \"a01004value\",\n" +
                "  to_char(A01007, 'yyyy-mm-dd')               AS \"a01007\",\n" +
                "  A01017                                      AS \"a01017\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 23 AND code = A01017)  AS \"a01017value\",\n" +
                "  A01011_A                                    AS \"a01011A\",\n" +
                "  A01014_B                                    AS \"a01014B\",\n" +
                "  A01011_B                                    AS \"a01014B\",\n" +
                "  to_char(A01034, 'yyyy-mm-dd')               AS \"a01034\",\n" +
                "  A01028                                      AS \"a01028\",\n" +
                "  A001003                                     AS \"A001003\",\n" +
                "  A01060                                      AS \"A01060\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 161 AND code = A01060) AS \"A01060value\",\n" +
                "  A01057_A                                    AS \"a01057A\",\n" +
                "  A01065                                      AS \"a01065\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 162 AND code = A01065) AS \"a01065value\",\n" +
                "  A01057                                      AS \"a01057\",\n" +
                "  A01111                                      AS \"a01111\",\n" +
                "  A01062                                      AS \"a01062\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 9 AND code = A01062)   AS \"a01062value\",\n" +
                "  A01081                                      AS \"a01081\",\n" +
                "  A01063                                      AS \"a01063\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 101 AND code = A01063) AS \"a01063value\",\n" +
                "  A01031                                      AS \"a01031\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 24 AND code = A01031)  AS \"a01031value\",\n" +
                "  A01057_B                                    AS \"a01057B\",\n" +
                "  A01027                                      AS \"a01027\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 20 AND code = A01027)  AS \"a01027value\",\n" +
                "  A01002_A                                    AS \"a01002A\",\n" +
                "  A01015                                      AS \"a01015\",\n" +
                "  A01002_B                                    AS \"a01002B\",\n" +
                "  to_char(A01040, 'yyyy-mm-dd')               AS \"a01040\",\n" +
                "  A01051                                      AS \"a01051\",\n" +
                "  to_char(A01094, 'yyyy-mm-dd')               AS \"a01094\",\n" +
                "  A01088                                      AS \"a01088\",\n" +
                "  A01061                                      AS \"a01061\",\n" +
                "  A01087                                      AS \"a01087\",\n" +
                "  A01097                                      AS \"a01097\",\n" +
                "  A01014_Z                                    AS \"a01014Z\",\n" +
                "  A01110                                      AS \"a01110\",\n" +
                "  A001004_A                                   AS \"a001004a\",\n" +
                "  A01014_A                                    AS \"a01014A\",\n" +
                "  A01067                                      AS \"a01067\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 166 AND code = A01067) AS \"a01067value\",\n" +
                "  A01058                                      AS \"a01058\",\n" +
                "  A01071                                      AS \"a01071\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 86 AND code = A01071)  AS \"a01071value\",\n" +
                "  A01074                                      AS \"a01074\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 1 AND code = A01074)   AS \"a01074value\",\n" +
                "  A01103                                      AS \"a01103\",\n" +
                "  A01107                                      AS \"a01107\",\n" +
                "  A01073                                      AS \"a01073\",\n" +
                "  A01090                                      AS \"a01090\",\n" +
                "  to_char(A01054, 'yyyy-mm-dd')               AS \"a01054\",\n" +
                "  A01052                                      AS \"a01052\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 106 AND code = A01052) AS \"a01052value\",\n" +
                "  A01048                                      AS \"a01048\",\n" +
                "  (SELECT name\n" +
                "   FROM SYS_CODE_ITEM\n" +
                "   WHERE CODE_SET_ID = 19 AND code = A01048)  AS \"a01048value\",\n" +
                "  A01108                                      AS \"a01108\",\n" +
                "  A01086                                      AS \"a01086\",\n" +
                "  A01037                                      AS \"a01037\",\n" +
                "  A01044                                      AS \"a01044\",\n" +
                "  A01084                                      AS \"a01084\",\n" +
                "  A01083                                      AS \"a01083\"\n" +
                "FROM (SELECT\n" +
                "        ROWNUM AS rn,\n" +
                "        t.*\n" +
                "      FROM (SELECT\n" +
                "              a001.*,\n" +
                "              tmp.codeLen,\n" +
                "              tmp.code,\n" +
                "              tmp.sortNum\n" +
                "            FROM EMP_A001 a001 INNER JOIN (SELECT\n" +
                "                                             a02.EMP_ID,\n" +
                "                                             min(length(nvl(org.code, '99999')))         AS codeLen,\n" +
                "                                             nvl(min(nvl(org.code, '99999')), '99999')   AS code,\n" +
                "                                             nvl(min(nvl(a02.A02023, '99999')), '99999') AS sortNum\n" +
                "                                           FROM EMP_A02 a02 INNER JOIN ORGANIZATION org ON a02.A02001_B = org.ID\n" +
                "                                           WHERE\n" +
                "                                             a02.STATUS = '0' AND a02.A02055 = '2' AND org.CODE LIKE (SELECT code || '%'\n" +
                "                                                                                                      FROM ORGANIZATION\n" +
                "                                                                                                      WHERE ID =\n" +
                "                                                                                                            '${deptId}')\n" +
                "                                           GROUP BY a02.EMP_ID) tmp ON a001.EMP_ID = tmp.EMP_ID \n" +
                "              INNER JOIN (SELECT DISTINCT EMP_ID FROM CADRE_TAG WHERE TAG_ID IN ${tagIds}) CADRE_TAG ON CADRE_TAG.EMP_ID = tmp.EMP_ID   " +
                "            WHERE a001.A01063 <> '2' AND a001.STATUS = '0'\n" +
                "            ORDER BY tmp.codeLen, tmp.code, tmp.sortNum, a001.ORDINAL) t\n" +
                "      WHERE ROWNUM <= '${endIndex}') t\n" +
                "WHERE rn >= '${startIndex}'";

        if (searchVO.getCadreTagIds().size() > 0) {
            String tagIdInCondition = searchVO.getCadreTagIds().stream().collect(Collectors.joining("','", "('", "')"));
            searchCadreSql = searchCadreSql.replaceAll("\\$\\{deptId\\}", searchVO.getDeptId())
                    .replaceAll("\\$\\{tagIds\\}", tagIdInCondition)
                    .replaceAll("\\$\\{startIndex\\}", searchVO.getStartIndex())
                    .replaceAll("\\$\\{endIndex\\}", searchVO.getEndIndex());
        } else {
            searchCadreSql = searchCadreSql.replaceAll("INNER JOIN \\(SELECT DISTINCT EMP_ID FROM CADRE_TAG WHERE TAG_ID IN \\$\\{tagIds\\}\\) CADRE_TAG ON CADRE_TAG.EMP_ID = tmp.EMP_ID ", "");
        }

        if (searchVO.getTableConditions().size() > 0) {
            int insertIndex = 0;
            int i = searchCadreSql.indexOf("CADRE_TAG.EMP_ID = tmp.EMP_ID   ");
            //若没有添加标签项
            if (i == -1) {
                insertIndex = searchCadreSql.indexOf("tmp ON a001.EMP_ID = tmp.EMP_ID ");
            } else {
                insertIndex = i;
            }

            Iterator<Map.Entry<String, Object>> iterator = searchVO.getTableConditions().entrySet().iterator();
            StringBuilder tableInnerJoinBuild = new StringBuilder();

            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String tableName = next.getKey();
                Map value = (Map) next.getValue();
                Object fieldName = value.get("fieldName");
                Object condition = value.get("condition");
                Object conditionValue = value.get("conditionValue");
                //查询为干部模型
                SysTableModelInfo cadreInfo = sysTableInfoDao.getTableInfo("CadreInfo");
                String saveTableName = cadreInfo.getTableNameEnAndSaveTableMap().get(tableName);
                List<List<String>> tableFields = cadreInfo.getTableNameEnAndFieldMap().get(String.valueOf(tableName));
                Map<String, List<List<String>>> fieldMap = tableFields.stream().collect(Collectors.groupingBy(e -> e.get(0)));
                List<List<String>> fieldDbSaveType = fieldMap.get(String.valueOf(fieldName));
                List<String> list = fieldDbSaveType.get(0);
                String dbSaveName = null;
                if (list != null) {
                    dbSaveName = list.get(1);
                }
                if (StringUtils.equalsIgnoreCase(condition.toString(), "like")) {
                    conditionValue = "%" + conditionValue + "%";
                }
                tableInnerJoinBuild.append(" INNER JOIN (SELECT DISTINCT EMP_ID FROM ").append(saveTableName).append(" ").append(tableName).append(" WHERE ").append(tableName).append(".").append(dbSaveName).append(" ").append(condition).append(" ").append("'").append(conditionValue).append("') ").append(tableName.toUpperCase()).append(" ON ").append(tableName.toUpperCase()).append(" .EMP_ID = tmp.EMP_ID ");
            }
            //插入位置应该最后一个条件的末尾
            searchCadreSql = new StringBuilder(searchCadreSql).insert(insertIndex + 32, tableInnerJoinBuild.toString()).toString();

            //替换所有变量
            searchCadreSql = searchCadreSql.replaceAll("\\$\\{deptId\\}", searchVO.getDeptId())
                    .replaceAll("\\$\\{startIndex\\}", searchVO.getStartIndex())
                    .replaceAll("\\$\\{endIndex\\}", searchVO.getEndIndex());
        }

        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(searchCadreSql);
        resultMap.put("cadreList", mapList);

        StringBuffer replace = new StringBuffer(searchCadreSql).replace(0, 4462, "SELECT COUNT(*) AS \"cadreCount\" FROM ");
        String countSql = replace.replace(replace.indexOf("WHERE ROWNUM"), replace.length(), ")").toString();

        Map<String, Object> map = jdbcTemplate.queryForMap(countSql);
        resultMap.putAll(map);

        return resultMap;
    }

    /**
     * 上传干部照片
     */
    @Override
    public boolean uploadPhoto(String empId, MultipartFile photo) {
        EmpPhoto empPhoto = empPhotoRepository.getByEmpId(empId);

        try {
            if (empPhoto == null) {
                empPhoto = new EmpPhoto();
                empPhoto.setSubId(IdUtil.uuid());
                empPhoto.setEmpId(empId);
                empPhoto.setSeqid(100);
            }
            empPhoto.setPhotoFile(photo.getBytes());
            empPhotoRepository.save(empPhoto);

            //修改照片异步生成干部的任免表文件
            executorService.submit(() -> {
                try {
                    ExportManager.generate(new ExportGbrmbHtmlBiJie(empId));
                    ExportManager.generate(new ExportGbrmbWordBiJie(empId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取干部单位内排序
     */
    @Override
    public List<CadreSortInDepModel> getSortInDep(String orgId) {
        String sql = "select a001.EMP_ID as empId, " +
                "       max(a001.A01001) as name, " +
                "       LISTAGG( A02016_A, '，') within group ( order by A02016_A) as position, " +
                "       min(nvl(a02.A02025,999)) as sortNumber " +
                " from EMP_A001 a001 inner join EMP_A02 a02 on a001.EMP_ID = a02.EMP_ID " +
                " where a001.STATUS = '0' and a02.STATUS = '0' and a02.A02055 = '2' " +
                " and a02.A02001_B = '" + orgId + "' " +
                " group by a001.EMP_ID " +
                " order by sortNumber";

        return jdbcTemplate.queryForList(sql).stream().map(map -> {
            CadreSortInDepModel model = new CadreSortInDepModel();
            model.setOrgId(orgId);
            model.setEmpId(map.get("empId").toString());
            model.setName(map.get("name").toString());
            model.setPositionName(map.get("position").toString());
            model.setSortNumber(map.get("sortNumber").toString());
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 修改干部在单位内排序
     */
    @Transactional
    @Override
    public boolean modifySortInDep(List<CadreSortInDepModifyModel> modifyModels) {
        String sql = "UPDATE EMP_A02 SET A02025 = :sortNumber WHERE EMP_ID = :empId AND A02001_B = :orgId";

        try {
            List<Object[]> argsList = new ArrayList<>(modifyModels.size());
            for (CadreSortInDepModifyModel modifyModel : modifyModels) {
                Object[] args = new Object[3];
                args[0] = modifyModel.getSortNumber();
                args[1] = modifyModel.getEmpId();
                args[2] = modifyModel.getOrgId();
                argsList.add(args);

            }
            jdbcTemplate.batchUpdate(sql, argsList);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 修改干部状态
     */
    @Override
    public boolean modifyStatus(CadreStatusModifyModel modifyModel) {
        List<String> empIds = modifyModel.getEmpIds();
        String status = modifyModel.getStatus();

        if (empIds == null || empIds.size() == 0 || StringUtils.isEmpty(status)) {
            return false;
        }

        if ("QS".equals(status)) {
            status = "4";
        } else if ("QT".equals(status)) {
            status = "9";
        } else {
            return false;
        }

        String sql = "UPDATE EMP_A001 SET A01063 = '" + status + "' WHERE EMP_ID in " + empIds.stream().collect(Collectors.joining("','", "('", "')"));

        jdbcTemplate.update(sql);

        return true;
    }

    /**
     * 修改干部所属单位
     */
    @Override
    public boolean modifyOrganization(CadreOrgModifyModel modifyModel) {
        List<String> empIds = modifyModel.getEmpIds();
        String orgId = modifyModel.getOrgId();
        if (empIds == null || empIds.size() == 0 || StringUtils.isEmpty(orgId)) {
            return false;
        }

        String sql = "UPDATE EMP_A001 SET A001004_A = '" + orgId + "' WHERE EMP_ID in " + empIds.stream().collect(Collectors.joining("','", "('", "')"));

        jdbcTemplate.update(sql);

        return true;
    }
}
