package com.sinosoft.ops.cimp.controller.sys.tag;

import com.google.common.collect.Lists;
import com.sinosoft.ops.cimp.annotation.SystemApiGroup;
import com.sinosoft.ops.cimp.controller.BaseController;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import com.sinosoft.ops.cimp.exception.BusinessException;
import com.sinosoft.ops.cimp.service.sys.tag.CadreTagService;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagCategoryService;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagService;
import com.sinosoft.ops.cimp.vo.to.sys.tag.SysTagModel;
import com.sinosoft.ops.cimp.vo.to.sys.tag.SysTagVO;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SystemApiGroup
@Api(description = "系统查询标签")
@RestController
@RequestMapping(value = "/sys/tag")
@SuppressWarnings("unchecked")
public class SysTagController extends BaseController {

    private final SysTagCategoryService sysTagCategoryService;
    private final SysTagService sysTagService;
    private final CadreTagService cadreTagService;

    @Autowired
    public SysTagController(SysTagCategoryService sysTagCategoryService, SysTagService sysTagService, CadreTagService cadreTagService) {
        this.sysTagCategoryService = sysTagCategoryService;
        this.sysTagService = sysTagService;
        this.cadreTagService = cadreTagService;
    }

    @RequestMapping(value = "/getSysTagCategory", method = RequestMethod.GET)
    public ResponseEntity<List<SysTagCategory>> getSysTagCategory() throws BusinessException {
        String cadreTagCategory = "CadreInfo";

        List<SysTagCategory> tagCategories = sysTagCategoryService.findAllByModelName(cadreTagCategory);
        return ok(tagCategories);
    }

    @RequestMapping(value = "/getSysTag", method = RequestMethod.GET)
    public ResponseEntity<List<SysTag>> getSysTag(@RequestParam("sysTagCategoryId") String sysTagCategoryId) throws BusinessException {
        if (StringUtils.isNotEmpty(sysTagCategoryId)) {
            List<String> tagCategoryIds = Lists.newArrayList();
            tagCategoryIds.add(sysTagCategoryId);
            List<SysTag> sysTags = sysTagService.findAll(tagCategoryIds);
            ok(sysTags);
        }
        return ok(Lists.newArrayList());
    }

    @RequestMapping(value = "/saveSysTagCategory", method = RequestMethod.POST)
    public ResponseEntity<SysTagCategory> saveSysTagCategory(@RequestBody SysTagCategory sysTagCategory) throws BusinessException {
        if (sysTagCategory != null) {
            return ok(sysTagCategoryService.save(sysTagCategory));
        }
        return ok(null);
    }

    @RequestMapping(value = "/updateSysTagCategory", method = RequestMethod.POST)
    public ResponseEntity<SysTagCategory> updateSysTagCategory(@RequestBody SysTagCategory sysTagCategory) throws BusinessException {
        if (sysTagCategory != null && sysTagCategory.getId() != null) {
            return ok(sysTagCategoryService.update(sysTagCategory));
        }
        return ok(null);
    }

    @RequestMapping(value = "/deleteSysTagCategory", method = RequestMethod.POST)
    public ResponseEntity deleteSysTagCategory(@RequestParam("sysTagCategoryId") String sysTagCategoryId) throws BusinessException {
        if (StringUtils.isNotEmpty(sysTagCategoryId)) {
            sysTagCategoryService.delete(sysTagCategoryId);
        }
        return ok(null);
    }

    @RequestMapping(value = "/saveSysTag", method = RequestMethod.POST)
    public ResponseEntity saveSysTag(@RequestBody SysTag sysTag) throws BusinessException {
        if (sysTag != null) {
            ok(sysTagService.save(sysTag));
        }
        return ok(null);
    }

    @RequestMapping(value = "/updateSysTag", method = RequestMethod.POST)
    public ResponseEntity updateSysTag(@RequestBody SysTag sysTag) throws BusinessException {
        if (sysTag != null && sysTag.getId() != null) {
            ok(sysTagService.update(sysTag));
        }
        return ok(null);
    }

    @RequestMapping(value = "/deleteSysTag", method = RequestMethod.POST)
    public ResponseEntity deleteSysTag(@RequestParam("sysTagId") String sysTagId) throws BusinessException {
        if (StringUtils.isNotEmpty(sysTagId)) {
            sysTagService.delete(sysTagId);
        }
        return ok(null);
    }

    @RequestMapping(value = "/markTag", method = RequestMethod.POST)
    public ResponseEntity markTag(@RequestParam("sysTagId") String sysTagId) throws BusinessException {
        Optional<SysTag> sysTag = sysTagService.findSysTag(sysTagId);
        if (sysTag.isPresent()) {
            SysTag sys_tag = sysTag.get();
            List<SysTag> sysTagList = Lists.newArrayListWithCapacity(1);
            sysTagList.add(sys_tag);
            cadreTagService.parallelMarkingTag(sysTagList);
        }
        return ok("计算标签中");
    }

    @RequestMapping(value = "/markTags", method = RequestMethod.POST)
    public ResponseEntity markTags(@RequestParam("sysTagCategoryId") String sysTagCategoryId) throws BusinessException {
        if (StringUtils.isNotEmpty(sysTagCategoryId)) {
            List<String> sysTagCategoryIdList = Lists.newArrayList();
            sysTagCategoryIdList.add(sysTagCategoryId);

            List<SysTag> sysTagList = sysTagService.findAll(sysTagCategoryIdList);
            cadreTagService.parallelMarkingTag(sysTagList);
        }
        return ok("计算标签中");
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
