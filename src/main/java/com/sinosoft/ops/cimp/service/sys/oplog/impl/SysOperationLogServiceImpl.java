package com.sinosoft.ops.cimp.service.sys.oplog.impl;

import com.querydsl.core.BooleanBuilder;
import com.sinosoft.ops.cimp.dto.PaginationViewModel;
import com.sinosoft.ops.cimp.entity.sys.oplog.QSysOperationLog;
import com.sinosoft.ops.cimp.entity.sys.oplog.SysOperationLog;
import com.sinosoft.ops.cimp.repository.sys.oplog.SysOperationLogRepository;
import com.sinosoft.ops.cimp.service.sys.oplog.SysOperationLogService;
import com.sinosoft.ops.cimp.vo.from.sys.oplog.SysOperationLogSearchVO;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    private final SysOperationLogRepository sysOperationLogRepository;

    @Autowired
    public SysOperationLogServiceImpl(SysOperationLogRepository sysOperationLogRepository) {
        this.sysOperationLogRepository = sysOperationLogRepository;
    }

    @Override
    public void saveLog(SysOperationLog sysOperationLog) {
        sysOperationLogRepository.save(sysOperationLog);
    }

    @Override
    public PaginationViewModel<SysOperationLog> findByPage(int pageIndex, int pageSize) {
        PaginationViewModel<SysOperationLog> paginationViewModel = new PaginationViewModel<>();
        Page<SysOperationLog> logPage = sysOperationLogRepository.findAll(PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Order.desc(QSysOperationLog.sysOperationLog.opDateTime.getMetadata().getName()))));

        paginationViewModel.setData(logPage.getContent());
        paginationViewModel.setPageIndex(pageIndex);
        paginationViewModel.setPageSize(pageSize);
        paginationViewModel.setTotalCount(logPage.getTotalElements());

        return paginationViewModel;
    }

    @Override
    public PaginationViewModel<SysOperationLog> search(SysOperationLogSearchVO sysOperationLogSearchVO) {
        String loginName = sysOperationLogSearchVO.getLoginName();
        String userName = sysOperationLogSearchVO.getUserName();
        String opStartTime = sysOperationLogSearchVO.getOpStartTime();
        String opEndTime = sysOperationLogSearchVO.getOpEndTime();
        String pageIndex = sysOperationLogSearchVO.getPageIndex();
        String pageSize = sysOperationLogSearchVO.getPageSize();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(loginName)) {
            booleanBuilder.and(QSysOperationLog.sysOperationLog.loginName.containsIgnoreCase(loginName));
        }
        if (StringUtils.isNotEmpty(userName)) {
            booleanBuilder.and(QSysOperationLog.sysOperationLog.userName.containsIgnoreCase(userName));
        }
        if (StringUtils.isNotEmpty(opStartTime)) {
            try {
                Date date = DateFormatUtil.parseDate(DateFormatUtil.PATTERN_ISO_ON_DATE, opStartTime);
                booleanBuilder.and(QSysOperationLog.sysOperationLog.opDateTime.after(date));
            } catch (ParseException e) {
                //忽略日期异常，不添加日期条件
            }
        }
        if (StringUtils.isNotEmpty(opEndTime)) {
            try {
                Date date = DateFormatUtil.parseDate(DateFormatUtil.PATTERN_ISO_ON_DATE, opEndTime);
                booleanBuilder.and(QSysOperationLog.sysOperationLog.opDateTime.before(date));
            } catch (ParseException e) {
                //忽略日期异常，不添加日期条件
            }
        }
        int pageIndexInt = 0;
        if (StringUtils.isNotEmpty(pageIndex)) {
            pageIndexInt = Integer.parseInt(pageIndex);
        }
        int pageSizeInt = 0;
        if (StringUtils.isNotEmpty(pageSize)) {
            pageSizeInt = Integer.parseInt(pageSize);
        }

        PageRequest pageRequest = PageRequest.of(pageIndexInt, pageSizeInt, Sort.by(Sort.Order.desc(QSysOperationLog.sysOperationLog.opDateTime.getMetadata().getName())));
        Page<SysOperationLog> operationLogPage = sysOperationLogRepository.findAll(booleanBuilder, pageRequest);
        PaginationViewModel<SysOperationLog> paginationViewModel = new PaginationViewModel<>();
        paginationViewModel.setTotalCount(operationLogPage.getNumberOfElements());
        paginationViewModel.setPageSize(paginationViewModel.getPageSize());
        paginationViewModel.setPageIndex(paginationViewModel.getPageIndex());
        paginationViewModel.setData(paginationViewModel.getData());
        
        return paginationViewModel;
    }
}
