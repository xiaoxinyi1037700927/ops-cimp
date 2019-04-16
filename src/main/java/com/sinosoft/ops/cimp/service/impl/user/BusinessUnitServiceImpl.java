package com.sinosoft.ops.cimp.service.impl.user;

import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.oraganization.BusinessUnitOrg;
import com.sinosoft.ops.cimp.entity.sys.oraganization.Organization;
import com.sinosoft.ops.cimp.entity.sys.oraganization.QBusinessUnitOrg;
import com.sinosoft.ops.cimp.entity.sys.user.BusinessUnit;
import com.sinosoft.ops.cimp.entity.sys.user.QBusinessUnit;
import com.sinosoft.ops.cimp.mapper.user.OrganizationViewMapper;
import com.sinosoft.ops.cimp.repository.user.BusinessUnitOrgRepository;
import com.sinosoft.ops.cimp.repository.user.BusinessUnitRepository;
import com.sinosoft.ops.cimp.repository.user.OrganizationRepository;
import com.sinosoft.ops.cimp.service.user.BusinessUnitService;
import com.sinosoft.ops.cimp.util.SecurityUtils;
import com.sinosoft.ops.cimp.vo.from.user.DeleteAttachmentViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.BusinessUnitAddViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.BusinessUnitModifyViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.BusinessUnitOrgChangeViewModel;
import com.sinosoft.ops.cimp.vo.to.user.BusinessUnitListViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.BusinessUnitOrgListViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.OrganizationSearchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BusinessUnitServiceImpl implements BusinessUnitService {
    @Autowired
    private BusinessUnitRepository businessUnitRepository;
    @Autowired
    private BusinessUnitOrgRepository businessUnitOrgRepository;
    @Autowired
    private OrganizationRepository organizationRepository;





    @Override
    public PaginationViewModel<BusinessUnitListViewModel> findBusinessUnitList(OrganizationSearchViewModel searchViewModel) {
        String organizationId = SecurityUtils.getSubject().getCurrentUser().getOrganizationId();
        PaginationViewModel<BusinessUnitListViewModel> page = new PaginationViewModel<>();
        int pageIndex = searchViewModel.getPageIndex();
        int pageSize = searchViewModel.getPageSize();
        if (pageIndex <= 0) pageIndex = 1;
        if (pageSize <= 0) pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize,new Sort(Sort.Direction.DESC,QBusinessUnit.businessUnit.createTime.getMetadata().getName()));
        BooleanBuilder builder = new BooleanBuilder();
//        builder.and(QBusinessUnit.businessUnit.createOrgId.eq(organizationId));
        if (!StringUtils.isEmpty(searchViewModel.getName())) {
            builder.and(QBusinessUnit.businessUnit.name.contains(searchViewModel.getName()));
        }
        if (!StringUtils.isEmpty(searchViewModel.getBusinessUnitId())) {
            builder.and(QBusinessUnit.businessUnit.id.eq(searchViewModel.getBusinessUnitId()));
        }
        Page<BusinessUnit> all = businessUnitRepository.findAll(builder, pageRequest);
        List<BusinessUnitListViewModel> collect = all.getContent().stream().map(x -> OrganizationViewMapper.INSTANCE.businessUnitToViewModel(x)).collect(Collectors.toList());
        collect.forEach(x -> {
            List<BusinessUnitOrg> businessUnitOrgList = (List<BusinessUnitOrg>) businessUnitOrgRepository.findAll(QBusinessUnitOrg.businessUnitOrg.businessUnitId.eq(x.getBusinessUnitId()));
            List<String> orgIds = businessUnitOrgList.stream().map(y -> y.getOrganizationId()).collect(Collectors.toList());
            List<Organization> byIdIn = organizationRepository.findByIdIn(orgIds);
            List<BusinessUnitOrgListViewModel> collect1 = byIdIn.stream().map(z -> OrganizationViewMapper.INSTANCE.organizationToBUOViewModel(z)).collect(Collectors.toList());
            x.setBusinessUnitOrgListViewModelList(collect1);
//            List<Attachment> byBusinessId = attachmentRepository.findByBusinessId(x.getBusinessUnitId());
//            List<AttachmentViewModel> attachmentViewModelList = byBusinessId.stream().map(y -> PositionQuotasMapper.INSTANCE.attachmentToViewModel(y)).collect(Collectors.toList());
//            x.setAttachmentViewModelList(attachmentViewModelList);
        });
        long totalCounts = all.getTotalElements();
        page.setPageIndex(searchViewModel.getPageIndex());
        page.setPageSize(searchViewModel.getPageSize());
        page.setTotalCount(totalCounts);
        page.setData(collect);
        return page;
    }

    @Override
    @Transactional
    public Boolean modifyBusinessUnit(BusinessUnitModifyViewModel modifyViewModel) {
        Optional<BusinessUnit> byId = businessUnitRepository.findById(modifyViewModel.getId());
        if (byId.isPresent()) {
            BusinessUnit businessUnit = byId.get();
            businessUnit.setName(modifyViewModel.getName());
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean addBusinessUnit(BusinessUnitAddViewModel addViewModel) {
        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setName(addViewModel.getName());
        businessUnit.setValidityDate(addViewModel.getValidityDate());
        businessUnit.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        businessUnit.setCreateOrgId(SecurityUtils.getSubject().getCurrentUser().getOrganizationId());
        BusinessUnit save = businessUnitRepository.save(businessUnit);

        this.addBusinessUnitOrg(save.getId(), save.getName(), addViewModel.getOrganizationIds(), addViewModel.getValidityDate());



        return true;
    }

    @Override
    @Transactional
    public Boolean deleteBusinessUnit(List<String> ids) {
        ids.forEach(id -> {
            businessUnitRepository.deleteById(id);
            //attachmentRepository.deleteByBusinessId(id);
        });
        return true;
    }

    @Override
    @Transactional
    public Boolean changeBusinessUnitOrgList(BusinessUnitOrgChangeViewModel modifyViewModel) {
        Optional<BusinessUnit> byId = businessUnitRepository.findById(modifyViewModel.getBusinessUnitId());
        if (byId.isPresent()) {
            BusinessUnit businessUnit = byId.get();
            businessUnit.setName(modifyViewModel.getBusinessUnitName());
            businessUnit.setValidityDate(modifyViewModel.getValidityDate());
            List<String> organizationIds = modifyViewModel.getOrganizationIds();
            businessUnitOrgRepository.deleteByBusinessUnitId(modifyViewModel.getBusinessUnitId());
            this.addBusinessUnitOrg(modifyViewModel.getBusinessUnitId(), modifyViewModel.getBusinessUnitName(), organizationIds, modifyViewModel.getValidityDate());

            return true;
        } else {
            return false;
        }
    }

//    @Override
//    public List<AttachmentVO> uploadAttachment(List<MultipartFile> files, String pathName, String businessUnitId, String type) {
//        //先上传文件到服务器
//        List<AttachmentVO> attachmentVOList = attachmentService.uploadFile(files, pathName);
//        attachmentVOList.forEach(attachmentVO -> {
//            if (type.equals(MenuConstants.有效证明文件)) {
//                attachmentVO.setUploadByType(MenuConstants.有效证明文件);
//            }else if (type.equals(MenuConstants.失效证明文件)) {
//                attachmentVO.setUploadByType(MenuConstants.失效证明文件);
//            }
//        });
//        return attachmentVOList;
//    }

    @Override
    @Transactional
    public Boolean deleteFile(DeleteAttachmentViewModel deleteViewModel) {
        //先删除文件
       // attachmentService.deleteFile(deleteViewModel);
        return null;
    }

    @Transactional
    public void addBusinessUnitOrg(String businessUnitId, String businessUnitName, List<String> organizationIds, Date validityDate) {
        organizationIds.forEach(organizationId -> {
            if (null != organizationId) {
                BusinessUnitOrg businessUnitOrg = new BusinessUnitOrg();
                businessUnitOrg.setBusinessUnitId(businessUnitId);
                businessUnitOrg.setBusinessUnitName(businessUnitName);
                businessUnitOrg.setOrganizationId(organizationId);
                businessUnitOrg.setValidityDate(validityDate);
                businessUnitOrg.setCreateId(SecurityUtils.getSubject().getCurrentUser().getId());
                businessUnitOrg.setCreateTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                businessUnitOrgRepository.save(businessUnitOrg);
            }
        });

    }


}
