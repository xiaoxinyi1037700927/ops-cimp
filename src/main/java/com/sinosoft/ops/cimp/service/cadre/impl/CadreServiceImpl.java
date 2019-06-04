package com.sinosoft.ops.cimp.service.cadre.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinosoft.ops.cimp.constant.OpsErrorMessage;
import com.sinosoft.ops.cimp.constant.RolePermissionPageSqlEnum;
import com.sinosoft.ops.cimp.dao.SysTableInfoDao;
import com.sinosoft.ops.cimp.dao.domain.sys.table.SysTableModelInfo;
import com.sinosoft.ops.cimp.entity.combinedQuery.CombinedQuery;
import com.sinosoft.ops.cimp.entity.emp.EmpPhoto;
import com.sinosoft.ops.cimp.entity.user.User;
import com.sinosoft.ops.cimp.entity.user.UserRole;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.repository.combinedQuery.CombinedQueryRepository;
import com.sinosoft.ops.cimp.repository.emp.EmpPhotoRepository;
import com.sinosoft.ops.cimp.service.cadre.CadreService;
import com.sinosoft.ops.cimp.service.user.RolePermissionPageSqlService;
import com.sinosoft.ops.cimp.util.IdUtil;
import com.sinosoft.ops.cimp.util.JsonUtil;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.util.combinedQuery.CombinedQueryParser;
import com.sinosoft.ops.cimp.util.combinedQuery.beans.CombinedQueryParseException;
import com.sinosoft.ops.cimp.vo.from.cadre.*;
import com.sinosoft.ops.cimp.vo.from.user.rolePermissionPageSql.RPPageSqlSearchModel;
import com.sinosoft.ops.cimp.vo.to.cadre.*;
import com.sinosoft.ops.cimp.vo.to.user.rolePermissionPageSql.RPPageSqlViewModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CadreServiceImpl implements CadreService {

    private final JdbcTemplate jdbcTemplate;
    private final EmpPhotoRepository empPhotoRepository;
    private final SysTableInfoDao sysTableInfoDao;
    private final CombinedQueryParser parser;
    private final CombinedQueryRepository combinedQueryRepository;
    private final RolePermissionPageSqlService rolePermissionPageSqlService;

    public CadreServiceImpl(JdbcTemplate jdbcTemplate, EmpPhotoRepository empPhotoRepository, SysTableInfoDao sysTableInfoDao, CombinedQueryParser parser, CombinedQueryRepository combinedQueryRepository, RolePermissionPageSqlService rolePermissionPageSqlService) {
        this.jdbcTemplate = jdbcTemplate;
        this.empPhotoRepository = empPhotoRepository;
        this.sysTableInfoDao = sysTableInfoDao;
        this.parser = parser;
        this.combinedQueryRepository = combinedQueryRepository;
        this.rolePermissionPageSqlService = rolePermissionPageSqlService;
    }


    /**
     * 干部列表查询
     *
     * @param searchModel
     * @return
     * @throws BusinessException
     */
    @SuppressWarnings("unchecked")
    @Override
    public CadreDataVO listCadre(CadreSearchModel searchModel) throws BusinessException {
        int pageIndex = searchModel.getPageIndex() > 0 ? searchModel.getPageIndex() : 1;
        int pageSize = searchModel.getPageSize() > 0 ? searchModel.getPageSize() : 10;
        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = pageIndex * pageSize;

        User currentUser = SecurityUtils.getSubject().getCurrentUser();
        if (currentUser == null) {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请登陆之后访问接口");
        }
        List<UserRole> currentUserRole = SecurityUtils.getSubject().getCurrentUserRole();
        List<String> roleIds = currentUserRole.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        RPPageSqlSearchModel rpPageSqlSearchModel = new RPPageSqlSearchModel();
        rpPageSqlSearchModel.setRoleIds(roleIds);

        List<RPPageSqlViewModel> pageSqlByRoleList = rolePermissionPageSqlService.findRPPageSqlListByRoleIds(rpPageSqlSearchModel);
        Optional<RPPageSqlViewModel> sqlViewModel = pageSqlByRoleList.stream().filter(s -> StringUtils.equals(s.getSqlNameEn(), RolePermissionPageSqlEnum.NAME_EN.干部集合.value)).findFirst();
        if (sqlViewModel.isPresent()) {
            RPPageSqlViewModel rpPageSqlViewModel = sqlViewModel.get();
            String execListSql = rpPageSqlViewModel.getExecListSql();
            String execCountSql = rpPageSqlViewModel.getExecCountSql();
            String selectCountFieldEn = rpPageSqlViewModel.getSelectCountFieldEn();
            String selectListFieldsEn = rpPageSqlViewModel.getSelectListFieldsEn();
            String defaultSortFields = "1".equals(searchModel.getIncludeSubNode()) ? rpPageSqlViewModel.getDefaultSortIncludeSub() : rpPageSqlViewModel.getDefaultSortExcludeSub();
            Map defaultFieldsMap = JsonUtil.parseStringToObject(defaultSortFields, LinkedHashMap.class);


            String execCadreListSql = execListSql.replaceAll("\\$\\{deptId}", searchModel.getDeptId())
                    .replaceAll("\\$\\{startIndex}", String.valueOf(startIndex))
                    .replaceAll("\\$\\{endIndex}", String.valueOf(endIndex))
                    .replaceAll("\\$\\{includeSubNode}", searchModel.getIncludeSubNode());

            String execCadreCountSql = execCountSql.replaceAll("\\$\\{deptId}", searchModel.getDeptId())
                    .replaceAll("\\$\\{includeSubNode}", searchModel.getIncludeSubNode());

            //自定义查询条件
            StringBuilder conditionSql = new StringBuilder(" ");

            //姓名搜索
            if (StringUtils.isNotEmpty(searchModel.getName())) {
                conditionSql.append(" AND a001.A01001 LIKE '%").append(searchModel.getName()).append("%' ");
            }

            //组合查询
            if (StringUtils.isNotEmpty(searchModel.getExprStr()) || StringUtils.isNotEmpty(searchModel.getCombinedQueryId())) {
                String exprStr = searchModel.getExprStr();
                Optional<CombinedQuery> optional = combinedQueryRepository.findById(searchModel.getCombinedQueryId());

                if (StringUtils.isEmpty(searchModel.getExprStr())) {
                    if (optional.isPresent()) {
                        exprStr = optional.get().getExpression();
                    }
                }

                try {
                    conditionSql.append(parser.parseSql(exprStr));
                } catch (CombinedQueryParseException e) {
                    throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "组合查询解析失败，请核对组合查询信息!");
                }
            }

            //标签查询
            if (StringUtils.isNotEmpty(searchModel.getCadreTagIds())) {
                String tagIds = Arrays.stream(searchModel.getCadreTagIds().split(",")).collect(Collectors.joining("','", "('", "')"));
                conditionSql.append(" AND a001.EMP_ID IN (SELECT DISTINCT EMP_ID FROM CADRE_TAG WHERE TAG_ID IN ").append(tagIds).append(") ");
            }

            //高级查询
            if (StringUtils.isNotEmpty(searchModel.getTableConditions())) {
                SysTableModelInfo cadreInfo = sysTableInfoDao.getTableInfo("CadreInfo");
                List<String> tableNames = new ArrayList<>();
                StringBuilder whereSql = new StringBuilder();

                //解析查询条件
                Map<String, Object> conditions = JsonUtil.parseStringToObject(searchModel.getTableConditions(), HashMap.class);
                for (Map.Entry<String, Object> condition : conditions.entrySet()) {
                    String tableName = cadreInfo.getTableNameEnAndSaveTableMap().get(condition.getKey());
                    tableNames.add(tableName);

                    for (Map map : (List<Map>) condition.getValue()) {
                        String fieldName = map.get("fieldName").toString();
                        String op = map.get("condition").toString();
                        String conditionValue = map.get("conditionValue").toString();

                        if (StringUtils.equalsIgnoreCase(op, "like")) {
                            conditionValue = "%" + conditionValue + "%";
                        }

                        whereSql.append(" and ").append(tableName).append(".").append(fieldName).append(" ")
                                .append(op).append(" '").append(conditionValue).append("' ");
                    }
                }

                String firstTable = tableNames.get(0);
                StringBuilder sb = new StringBuilder();
                sb.append(" AND a001.EMP_ID IN(SELECT DISTINCT ")
                        .append(firstTable).append(".EMP_ID ")
                        .append(" FROM ").append(firstTable);
                for (int i = 1; i < tableNames.size(); i++) {
                    String tableName = tableNames.get(i);
                    sb.append(" INNER JOIN ").append(tableName).append(" ON ")
                            .append(firstTable).append(".EMP_ID = ").append(tableName).append(".EMP_ID ");
                }

                sb.append(" WHERE 1=1 ").append(whereSql).append(") ");

                sb.append(sb);
            }

            //自定义排序
            StringBuilder orderBySql = new StringBuilder(" ");
            if ("1".equals(searchModel.getIsInit())) {
                //初始化时添加默认排序
                List<SortModel> sorts = new ArrayList<>();
                defaultFieldsMap.forEach((k, v) -> sorts.add(new SortModel(v.toString(), 0)));
                searchModel.setSorts(sorts);
            }

            if (searchModel.getSorts() != null && searchModel.getSorts().size() > 0) {
                orderBySql.append(" ORDER BY ");
                for (SortModel sortModel : searchModel.getSorts()) {
                    String[] names = sortModel.getName().split(",");
                    String order = sortModel.getIsDesc() == 0 ? " asc " : " desc ";
                    for (String name : names) {
                        orderBySql.append(name).append(order).append(", ");
                    }
                }
                orderBySql.delete(orderBySql.length() - 2, orderBySql.length() - 1);
            }

            //将自定义条件拼接到列表查询sql中
            execCadreListSql = execCadreListSql.replaceAll("\\$\\{custom}", conditionSql.toString() + orderBySql.toString());
            execCadreCountSql = execCadreCountSql.replaceAll("\\$\\{custom}", conditionSql.toString());

            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(execCadreListSql);
            Map<String, Object> countMap = jdbcTemplate.queryForMap(execCadreCountSql);

            CadreDataVO cadreDataVO = new CadreDataVO();
            cadreDataVO.setPageIndex(pageIndex);
            cadreDataVO.setPageSize(pageSize);
            Object cadreCount = countMap.get(selectCountFieldEn);
            if (cadreCount == null) {
                cadreDataVO.setDataCount(0L);
            } else {
                cadreDataVO.setDataCount(Long.parseLong(String.valueOf(cadreCount)));
            }
            List<CadreVO> cadreVOS = Lists.newArrayList();
            Map selectFields = JsonUtil.parseStringToObject(selectListFieldsEn, LinkedHashMap.class);

            for (Map<String, Object> map : mapList) {
                CadreVO cadreVO = new CadreVO();
                List<CadreFieldVO> fieldVOS = Lists.newArrayList();
                selectFields.forEach((k, v) -> {
                    CadreFieldVO fieldVO = new CadreFieldVO();
                    Object o = map.get(k.toString());
                    fieldVO.setFieldNameEn(k);
                    fieldVO.setFieldNameCn(v);
                    fieldVO.setFieldValue(o);
                    fieldVOS.add(fieldVO);
                });
                cadreVO.setFields(fieldVOS);
                cadreVOS.add(cadreVO);
            }
            cadreDataVO.setCadres(cadreVOS);
            cadreDataVO.setTableFields(selectFields);

            //排序字段
            List<SortFieldModel> sortFields = new ArrayList<>();

            //添加默认排序字段
            defaultFieldsMap.forEach((k, v) -> {
                SortFieldModel model = new SortFieldModel();
                model.setDefault(true);
                model.setName(k.toString());
                model.setFieldName(v.toString());
                sortFields.add(model);
            });
            //添加可排序字段
            selectFields.forEach((k, v) -> {
                if (!"EMP_ID".equals(k)) {
                    SortFieldModel model = new SortFieldModel();
                    model.setName(v.toString());
                    model.setFieldName(k.toString());
                    sortFields.add(model);
                }
            });
            cadreDataVO.setSortFields(sortFields);

            return cadreDataVO;
        } else {
            throw new BusinessException(OpsErrorMessage.MODULE_NAME, OpsErrorMessage.ERROR_MESSAGE, "请检查角色配置的干部信息数据权限");
        }
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
        if (StringUtils.isNotEmpty(empId)) {
            Map<String, Object> data = jdbcTemplate.queryForMap(String.format("select PHOTO_FILE from EMP_PHOTO where ROWNUM = 1 AND EMP_ID = '%s'", empId));
            if (data.size() > 0) {
                return (byte[]) data.get("PHOTO_FILE");
            } else {
                return null;
            }
        } else {
            return null;
        }
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
            AsyncGenerateGbrmb.execute(empId);

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
    public boolean modifySortInDep(CadreSortInDepModifyModel modifyModel) {

        //待移动单位
        String orgId = modifyModel.getOrgId();
        String fromEmpId = modifyModel.getFromEmpId();
        String toEmpId = modifyModel.getToEmpId();
        String moveType = modifyModel.getMoveType();

        //1.判断移动的干部是否是同一个单位
        List<CadreSortInDepModel> sortInDep = this.getSortInDep(orgId);
        Map<String, CadreSortInDepModel> cadreMap = sortInDep.stream().collect(Collectors.toMap((CadreSortInDepModel::getEmpId), (v) -> v, (k1, k2) -> k1, LinkedHashMap::new));

        boolean fromB = cadreMap.containsKey(fromEmpId);
        boolean fromA = cadreMap.containsKey(toEmpId);

        if (fromB && fromA) {
            Map<String, String> modifySortNumberMap = Maps.newHashMap();

            CadreSortInDepModel fromCadreSortInDepModel = cadreMap.get(fromEmpId);
            CadreSortInDepModel toCadreSortInDepModel = cadreMap.get(toEmpId);

            String toSortNumber = toCadreSortInDepModel.getSortNumber();

            int i = 0;
            if (StringUtils.equals(moveType, "0")) {
                modifySortNumberMap.put(fromCadreSortInDepModel.getEmpId(), toSortNumber);
                i = sortInDep.indexOf(toCadreSortInDepModel);
            }
            if (StringUtils.equals(moveType, "1")) {
                Integer reToSort = Integer.parseInt(toSortNumber) + 1;
                modifySortNumberMap.put(fromCadreSortInDepModel.getEmpId(), String.valueOf(reToSort));
                i = sortInDep.indexOf(toCadreSortInDepModel) + 1;
            }
            List<CadreSortInDepModel> reSortList = sortInDep.subList(i, sortInDep.size());
            if (StringUtils.equals(moveType, "0")) {
                reSortList.removeIf((e) -> StringUtils.equals(e.getEmpId(), fromEmpId));
                modifySortNumberMap.put(fromCadreSortInDepModel.getEmpId(), toSortNumber);
            }
            Map<String, String> reSortMap = reSortList.stream().peek(
                    c -> {
                        String sortNumber = c.getSortNumber();
                        String reSortNumber = String.valueOf(Integer.parseInt(sortNumber) + 1);
                        c.setSortNumber(reSortNumber);
                    }
            ).collect(Collectors.toMap(CadreSortInDepModel::getEmpId, CadreSortInDepModel::getSortNumber, (k1, k2) -> k1));
            modifySortNumberMap.putAll(reSortMap);


            //生成sql语句更新
            String sql = "UPDATE EMP_A02 SET A02025 = :sortNumber WHERE EMP_ID = :empId AND A02001_B = :orgId";
            List<Object[]> params = Lists.newArrayList();
            for (Map.Entry<String, String> entry : modifySortNumberMap.entrySet()) {
                Object[] temp = new Object[3];
                String key = entry.getKey();
                String value = entry.getValue();
                temp[0] = value;
                temp[1] = key;
                temp[2] = orgId;
                params.add(temp);
            }

            jdbcTemplate.batchUpdate(sql, params);

        } else {
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public boolean modifySortOrder(List<CadreSortInDepModifyModel> modifyModels) {
        String sql = "UPDATE EMP_A02 SET A02025 = :sortNumber WHERE EMP_ID = :empId AND A02001_B = :orgId";

        try {
            List<Object[]> argsList = new ArrayList<>(modifyModels.size());
            for (CadreSortInDepModifyModel modifyModel : modifyModels) {
                Object[] args = new Object[3];
                args[0] = modifyModel.getSortNumber();
                args[1] = modifyModel.getFromEmpId();
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

    @Override
    public boolean cadreCardIdExist(String cardId) {
        if (StringUtils.isNotEmpty(cardId)) {
            String sql = "SELECT COUNT(*) AS \"cardCount\" FROM EMP_A001 WHERE A001003 = '%s'";
            String execSql = String.format(sql, cardId);

            Map<String, Object> resultMap = jdbcTemplate.queryForMap(execSql);
            Object cardCount = resultMap.get("cardCount");
            if (cardCount != null) {
                int cardCountInt = Integer.parseInt(String.valueOf(cardCount));
                return cardCountInt > 0;
            }
            return false;
        }
        return false;
    }
}
