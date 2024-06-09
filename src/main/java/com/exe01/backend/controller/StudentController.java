package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.StudentDTO;
import com.exe01.backend.dto.request.student.CreateStudentRequest;
import com.exe01.backend.dto.request.student.UpdateStudentRequest;
import com.exe01.backend.exception.BaseException;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IStudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Tag(name = "Student Controller")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @Operation(summary = "Get all student", description = "API get all student")
    @GetMapping(value = ConstAPI.StudentAPI.GET_STUDENT)
    public PagingModel getALl(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all students with page: {}, limit: {}", page, limit);
        return studentService.getAll(page, limit);
    }

    @Operation(summary = "Get all student with status active", description = "API get all student with status active")
    @GetMapping(value = ConstAPI.StudentAPI.GET_STUDENT_STATUS_TRUE)
    public PagingModel findAllWithStatusActive(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) throws BaseException {
        log.info("Getting all active students with page: {}, limit: {}", page, limit);
        return studentService.findAllByStatusTrue(page, limit);
    }

    @Operation(summary = "Get student by id", description = "API get student by id")
    @GetMapping(value = ConstAPI.StudentAPI.GET_STUDENT_BY_ID + "{id}")
    public StudentDTO findById(@PathVariable("id") UUID id) throws BaseException {
        try {
            log.info("Getting student with id: {}", id);
            return studentService.findById(id);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new BaseException(500, e.getMessage(), "Internal Server Error");
        }

    }

    @Operation(summary = "Create student", description = "API create new student")
    @PostMapping(value = ConstAPI.StudentAPI.CREATE_STUDENT)
    public StudentDTO create(@RequestBody CreateStudentRequest request) throws BaseException {
        log.info("Creating new student with request: {}", request);
        return studentService.create(request);
    }

    @Operation(summary = "Update student", description = "API update student")
    @PutMapping(value = ConstAPI.StudentAPI.UPDATE_STUDENT + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateStudentRequest request) throws BaseException {
        log.info("Updating student with id: {}, request: {}", id, request);
        return studentService.update(id, request);
    }

    @Operation(summary = "Delete student", description = "API delete student")
    @DeleteMapping(value = ConstAPI.StudentAPI.DELETE_STUDENT + "{id}")
    public Boolean delete(@PathVariable("id") UUID id) throws BaseException {
        log.info("Deleting student with id: {}", id);
        return studentService.delete(id);
    }

    @Operation(summary = "Change status student", description = "API change status student")
    @PutMapping(value = ConstAPI.StudentAPI.CHANGE_STATUS_STUDENT + "{id}")
    public Boolean changeStatus(@PathVariable("id") UUID id) throws BaseException {
        log.info("Change status student with id: {}", id);
        return studentService.changeStatus(id);
    }
}
