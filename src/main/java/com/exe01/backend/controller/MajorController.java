package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.MajorDTO;
import com.exe01.backend.dto.request.major.CreateMajorRequest;
import com.exe01.backend.dto.request.major.UpdateMajorRequest;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
public class MajorController {

    @Autowired
    private IMajorService majorService;

    @GetMapping(value = ConstAPI.MajorAPI.GET_MAJOR)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return majorService.getAll(page, limit);
    }

    @GetMapping(value = ConstAPI.MajorAPI.GET_MAJOR_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return majorService.findAllByStatusTrue(page, limit);
    }

    @GetMapping(value = ConstAPI.MajorAPI.GET_MAJOR_BY_ID + "{id}")
    public MajorDTO findById(@PathVariable("id") UUID id) {
        return majorService.findById(id);
    }

    @PostMapping(value = ConstAPI.MajorAPI.CREATE_MAJOR)
    public MajorDTO create(@RequestBody CreateMajorRequest request) {
        return majorService.create(request);
    }

    @PutMapping(value = ConstAPI.MajorAPI.UPDATE_MAJOR + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateMajorRequest request) {
        return majorService.update(id, request);
    }

    @DeleteMapping(value = ConstAPI.MajorAPI.DELETE_MAJOR + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) {
        return majorService.delete(id);
    }
}
