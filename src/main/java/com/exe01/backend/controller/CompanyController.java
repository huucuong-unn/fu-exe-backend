package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.CompanyDTO;
import com.exe01.backend.dto.request.company.BaseCompanyRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.ICompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@Slf4j
@Tag(name = "Company Controller")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @Operation(summary = "Get all company", description = "API get all company")
    @GetMapping(value = ConstAPI.CompanyAPI.GET_COMPANY)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all company with page: {}, limit: {}", page, limit);
        return companyService.getAll(page, limit);
    }

    @Operation(summary = "Get all company with status active", description = "API get all company with status active")
    @GetMapping(value = ConstAPI.CompanyAPI.GET_COMPANY_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active company with page: {}, limit: {}", page, limit);
        return companyService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get all company with search and sort", description = "API get all company with search and sort")
    @GetMapping(value = ConstAPI.CompanyAPI.GET_COMPANY_BY_SEARCH_SORT)
    public PagingModel searchSortCompany(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "address", required = false) String address, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active company with page: {}, limit: {}", page, limit);
        return companyService.searchSortCompany(name, address, page, limit);
    }

    @Operation(summary = "Get account by id", description = "API get account by id")
    @GetMapping(value = ConstAPI.CompanyAPI.GET_COMPANY_BY_ID + "{id}")
    public CompanyDTO findById(@PathVariable("id") UUID id) throws BaseException {
        log.info("Getting company with id: {}", id);
        return companyService.findById(id);
    }

    @Operation(summary = "Create company", description = "API create new company")
    @PostMapping(value = ConstAPI.CompanyAPI.CREATE_COMPANY)
    public CompanyDTO create(@RequestBody BaseCompanyRequest request) throws BaseException {
        log.info("Creating new account with request: {}", request);
        return companyService.create(request);
    }

    @Operation(summary = "Update company", description = "API update company")
    @PutMapping(value = ConstAPI.CompanyAPI.UPDATE_COMPANY + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody BaseCompanyRequest request) throws BaseException {
        log.info("Updating company with id: {}, request: {}", id, request);
        return companyService.update(id, request);
    }

    @Operation(summary = "Change status company", description = "API change status compnay")
    @PutMapping(value = ConstAPI.CompanyAPI.CHANGE_STATUS_COMPANY + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Change status company with id: {}", id);
        return companyService.changeStatus(id);
    }

    @Operation(summary = "Get all company by status true without paging", description = "Get all company by status true without paging")
    @GetMapping(value = ConstAPI.CompanyAPI.GET_COMPANY_STATUS_TRUE_WITHOUT_PAGING)
    public List<CompanyDTO> getAllCompanyByStatusTrueWithoutPaging() throws BaseException {
        log.info("Getting all company by status true without paging");
        return companyService.findAllByStatus();
    }

}
