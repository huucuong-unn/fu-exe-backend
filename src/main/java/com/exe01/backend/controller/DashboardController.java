package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.Dashboard.DashboardResponse;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.impl.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Dashboard Controller")
public class DashboardController {

    @Autowired
    private DashBoardService dashBoardService;

    @Operation(summary = "Get dashboard", description = "API dashboard")
    @GetMapping(value = ConstAPI.DashboardAPI.GET_DASHBOARD)
    public DashboardResponse getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all company with page: {}, limit: {}", page, limit);
        return dashBoardService.getDashboardData();
    }


}
