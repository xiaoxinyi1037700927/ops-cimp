package com.sinosoft.ops.cimp.service.sys.tag.impl;

import com.sinosoft.ops.cimp.CimpApplication;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTag;
import com.sinosoft.ops.cimp.entity.sys.tag.SysTagCategory;
import com.sinosoft.ops.cimp.service.sys.tag.CadreTagService;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagCategoryService;
import com.sinosoft.ops.cimp.service.sys.tag.SysTagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CimpApplication.class)
public class CadreTagServiceImplTest {

    @Autowired
    private SysTagService sysTagService;
    @Autowired
    private SysTagCategoryService sysTagCategoryService;
    @Autowired
    private CadreTagService cadreTagService;

    @Test
    public void markingTag() {
        List<SysTagCategory> cadreInfoTagCategories = sysTagCategoryService.findAllByModelName("CadreInfo");
        List<String> tagCategoryIds = cadreInfoTagCategories.stream().map(SysTagCategory::getId).collect(Collectors.toList());

        List<SysTag> sysTags = sysTagService.findAll(tagCategoryIds);

        for (SysTag sysTag : sysTags) {
            int i = cadreTagService.markingTag(sysTag);
            System.out.println("打了【" + i + "】个标签");
        }
    }

    @Test
    public void testParallelMarkingTag() {
        List<SysTagCategory> cadreInfoTagCategories = sysTagCategoryService.findAllByModelName("CadreInfo");
        List<String> tagCategoryIds = cadreInfoTagCategories.stream().map(SysTagCategory::getId).collect(Collectors.toList());

        List<SysTag> sysTags = sysTagService.findAll(tagCategoryIds);

        cadreTagService.parallelMarkingTag(sysTags);
    }
}