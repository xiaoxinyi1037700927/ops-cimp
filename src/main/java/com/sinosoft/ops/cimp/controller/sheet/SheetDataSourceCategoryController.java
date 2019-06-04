package com.sinosoft.ops.cimp.controller.sheet;

import com.sinosoft.ops.cimp.annotation.SheetApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 报表分类控制器
 */
@SheetApiGroup
@Api(description = "报表分类控制器")
@Controller("sheetDataSourceCategoryController")
@RequestMapping("/sheetDataSourceCategory")
public class SheetDataSourceCategoryController extends BaseController {
//    @Autowired
//    private SheetDataSourceCategoryService sheetDataSourceCategoryService;
//
//    @ApiOperation(value = "新增数据源分类")
//    @PostMapping("/addCategory")
//    public ResponseEntity<String> addCategory(@RequestParam("categoryName") String categoryName,
//                                              @RequestParam("parentId") String parentId) throws BusinessException {
//        SheetDataSourceCategory sheetDataSourceCategory = new SheetDataSourceCategory();
//        sheetDataSourceCategory.setName(categoryName);
//        sheetDataSourceCategory.setParentId(parentId);
//        sheetDataSourceCategory.setStatus(0);
//        sheetDataSourceCategory.setCreatedTime(new Timestamp(System.currentTimeMillis()));
//        sheetDataSourceCategory.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
//        sheetDataSourceCategory.setOrdinal(sheetDataSourceCategoryService.getNextOrdinal());
//        boolean flag = sheetDataSourceCategoryService.addSheetDataSourceCategory(sheetDataSourceCategory);
//        if(flag){return ok("操作成功");}
//        return fail("操作异常!");
//    }
}
