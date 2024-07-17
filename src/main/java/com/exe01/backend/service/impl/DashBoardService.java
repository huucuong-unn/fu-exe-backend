package com.exe01.backend.service.impl;

import com.exe01.backend.dto.Dashboard.DashboardResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.service.IApplicationService;
import com.exe01.backend.service.ICompanyService;
import com.exe01.backend.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashBoardService {

    @Autowired
    IApplicationService applicationService;

    @Autowired
    ICompanyService companyService;

    @Autowired
    ITransactionService transactionService;

    public DashboardResponse getDashboardData() throws BaseException {
DashboardResponse dashboardResponse = new DashboardResponse();
dashboardResponse.setApplicationByMonth(applicationService.getApplicationByMonth());
dashboardResponse.setTopFiveCompany(companyService.getTopFiveCompany());
dashboardResponse.setMonthlyRevenue(transactionService.getMonthlyRevenue());
return dashboardResponse;
    }

}
