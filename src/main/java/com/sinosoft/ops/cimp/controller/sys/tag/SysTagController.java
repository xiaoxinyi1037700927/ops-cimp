package com.sinosoft.ops.cimp.controller.sys.tag;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagCategoryService;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagService;
import com.sinosoft.ops.cimp.vo.to.sys.tag.SysTagModel;
import com.sinosoft.ops.cimp.vo.to.sys.tag.SysTagVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SystemApiGroup
@Api(description = "系统查询标签")
@RestController
@RequestMapping(value = "/sys/tag")
@SuppressWarnings("unchecked")
public class SysTagController extends BaseController {

    private final SysTagCategoryService sysTagCategoryService;
    private final SysTagService sysTagService;

    @Autowired
    public SysTagController(SysTagCategoryService sysTagCategoryService, SysTagService sysTagService) {
        this.sysTagCategoryService = sysTagCategoryService;
        this.sysTagService = sysTagService;
    }

    /**
     * 获取查询干部的所有查询标签
     */
    @RequestMapping(value = "/getCadreTag", method = RequestMethod.GET)
    public ResponseEntity<List<SysTagModel>> getCadreTag() throws BusinessException {
        List<SysTagModel> sysTagModels = Lists.newArrayList();
        String cadreTagCategory = "CadreInfo";

        List<SysTagCategory> tagCategories = sysTagCategoryService.findAllByModelName(cadreTagCategory);
        List<String> tagCategoryIds = tagCategories.stream().map(SysTagCategory::getId).collect(Collectors.toList());

        List<SysTag> sysTags = sysTagService.findAll(tagCategoryIds);
        Map<String, List<SysTag>> sysTagMap = sysTags.stream().collect(Collectors.groupingBy(SysTag::getTagCategoryId));
        for (SysTagCategory tagCategory : tagCategories) {
            SysTagModel sysTagModel = new SysTagModel();
            sysTagModel.setTagCategoryId(tagCategory.getId());
            sysTagModel.setTagCategoryName(tagCategory.getTagCategoryName());
            sysTagModel.setTagModel(tagCategory.getTagModel());
            List<SysTag> sysTagList = sysTagMap.getOrDefault(tagCategory.getId(), Lists.newArrayList());

            List<SysTagVO> sysTagList1 = Lists.newArrayListWithCapacity(sysTagList.size());
            for (SysTag sysTag : sysTagList) {
                SysTagVO sysTagVO = new SysTagVO();
                String tagId = sysTag.getId();
                sysTagVO.setTagId(tagId);
                sysTagVO.setTagName(sysTag.getTag());
                sysTagList1.add(sysTagVO);
            }
            sysTagModel.setSysTags(sysTagList1);
            sysTagModels.add(sysTagModel);
        }
        return ok(sysTagModels);
    }
}
