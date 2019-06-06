package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.annotation.SheetApiGroup;
import com.sinosoft.ops.cimp.common.model.DataStatus;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sheet.SheetDataSource;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sheet.SheetDataSourceService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 报表分类控制器
 */
@SheetApiGroup
@Api(description = "报表分类控制器")
@Controller("sheetDataSourceController")
@RequestMapping("sheet/sheetDataSource")
public class SheetDataSourceController extends BaseController {
    @Autowired
    private SheetDataSourceService sheetDataSourceService;

    SecurityUtils securityUtils = new SecurityUtils();

    @ApiOperation(value = "新增数据源")
    @PostMapping("/create")

    public ResponseEntity<String> create(SheetDataSource entity) throws BusinessException {
        try{
            if (entity.getCreatedBy() == null) {
//                String userid = securityUtils.getCurrentUser().getId();
//                entity.setCreatedBy(UUID.fromString(userid));
                entity.setCreatedBy(UUID.randomUUID());
            }
            entity.setStatus(DataStatus.NORMAL.getValue());
            entity.setOrdinal(sheetDataSourceService.getNextOrdinal());
            sheetDataSourceService.analyzeSqlExpress(entity);
            entity.setId(UUID.randomUUID());
            entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            entity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            sheetDataSourceService.save(entity);
            return ok("操作成功");
        }catch(Exception e){
            return fail("创建失败!");
        }
    }

    @ApiOperation(value = "删除数据源")
    @PostMapping("/deleteById")

    public ResponseEntity<String> deleteById(@RequestParam("id") String id) throws BusinessException {
        try {
            sheetDataSourceService.deleteById(UUID.fromString(id));
            return ok("根据id删除成功");
        } catch (Exception e) {
            return fail("根据id删除失败");
        }
    }

    @ApiOperation(value = "根据id获取数据源")
    @PostMapping("/getById")

    public ResponseEntity<String> getById(@RequestParam("id") String id) throws BusinessException {
        try {
            UUID uuid = UUID.fromString(id);
            SheetDataSource result = sheetDataSourceService.getById(uuid);
            return ok(result);
        } catch (Exception e) {
            return fail("查询失败");
        }
    }

    @ApiOperation(value = "根据分类获取数据源")
    @PostMapping("/getByCategoryId")

    public ResponseEntity<String> getByCategoryId(@RequestParam("categoryid") Integer categoryid) throws BusinessException {
        try {
            Collection<SheetDataSource> result = sheetDataSourceService.getByCategoryId(categoryid);
            return ok(result);
        } catch (Exception e) {
            return fail("查询失败");
        }
    }

    @ApiOperation(value = "取得引用情况")
    @PostMapping("/getRefSituation")

    public ResponseEntity<String> getRefSituation(@RequestParam("id") String id) throws BusinessException {
        try {
            List<Map> list = sheetDataSourceService.getRefSituation(id);
            return ok(list);
        } catch (Exception e) {
            return fail("查询失败");
        }
    }

    @ApiOperation(value = "删除数据源")
    @PostMapping("/delete")

    public ResponseEntity<String> delete(SheetDataSource entity) throws BusinessException {
        try {
            sheetDataSourceService.delete(entity);
            return ok("删除成功");
        } catch (Exception e) {
            return fail("删除失败");
        }
    }

    @ApiOperation(value = "修改数据源")
    @PostMapping("/update")

    public ResponseEntity<String> update(SheetDataSource entity) throws BusinessException {
        try {
            SheetDataSource updateentity=sheetDataSourceService.getById(entity.getId());
            if (securityUtils.getCurrentUser() != null) {
//                String userid = securityUtils.getCurrentUser().getId();
//                entity.setLastModifiedBy(UUID.fromString(userid));
                updateentity.setLastModifiedBy(UUID.randomUUID());
            }
            // 最后修改时间
            updateentity.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
            updateentity.setName(entity.getName());
            updateentity.setSql(entity.getSql());
            sheetDataSourceService.analyzeSqlExpress(updateentity);
            sheetDataSourceService.update(updateentity);
            return ok("修改成功");
        } catch (Exception e) {
            return fail("修改失败");
        }
    }
}
