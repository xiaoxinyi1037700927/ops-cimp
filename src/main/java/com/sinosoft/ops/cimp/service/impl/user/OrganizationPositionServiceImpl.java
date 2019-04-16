package com.sinosoft.ops.cimp.service.impl.user;


import com.sinosoft.ops.cimp.entity.sys.user.organization.OrganizationPosition;
import com.sinosoft.ops.cimp.entity.sys.user.organization.OrganizationPositionService;
import com.sinosoft.ops.cimp.repository.user.OrganizationPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationPositionServiceImpl implements OrganizationPositionService {

    @Autowired
    private OrganizationPositionRepository organizationPositionRepository;

    @Override
    public List<OrganizationPosition> findByOrganizationId(String organizationId) {
        return organizationPositionRepository.findByOrganizationId(organizationId);
    }

    @Override
    public OrganizationPosition findById(String positionId) {
        OrganizationPosition organizationPosition = null;
        Optional<OrganizationPosition> options = organizationPositionRepository.findById(positionId);
        if (options.isPresent()) {
            return options.get();
        }
        return organizationPosition;
    }
}
