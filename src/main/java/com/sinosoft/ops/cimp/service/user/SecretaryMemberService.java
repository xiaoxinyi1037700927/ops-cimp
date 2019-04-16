package com.sinosoft.ops.cimp.service.user;


import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.user.SecretaryMember;
import com.sinosoft.ops.cimp.vo.from.user.organization.SecretaryMemberAddViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.SecretaryMemberModifyViewModel;
import com.sinosoft.ops.cimp.vo.from.user.organization.SecretaryMemberSearchViewModel;
import com.sinosoft.ops.cimp.vo.user.organization.SecretaryMemberViewModel;

import java.util.List;

public interface SecretaryMemberService {

    PaginationViewModel<SecretaryMemberViewModel> findByPageData(SecretaryMemberSearchViewModel searchViewModel);

    SecretaryMember findById(String secretaryMemberId);

    boolean addSecretaryMember(SecretaryMemberAddViewModel addViewModel);

    boolean modifySecretaryMember(SecretaryMemberModifyViewModel modifyViewModel);

    boolean deleteById(String secretaryMemberId);

    List<SecretaryMemberViewModel> findByOrganizationId();
}
