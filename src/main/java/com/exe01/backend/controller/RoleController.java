package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.RoleDTO;
import com.exe01.backend.dto.request.role.CreateRoleRequest;
import com.exe01.backend.dto.request.role.UpdateRoleRequest;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping(value = ConstAPI.AuthenticationAPI.ROLE)
    public PagingModel getAll(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return roleService.getAll(page, limit);
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.ROLE_STATUS_TRUE)
    public PagingModel getAllWithStatusTrue(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return roleService.findAllByStatusTrue(page, limit);
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.ROLE_BY_ID + "{id}")
    public RoleDTO findById(@PathVariable("id") UUID id) {
        return roleService.findById(id);
    }

    @PostMapping(value = ConstAPI.AuthenticationAPI.CREATE_ROLE)
    public RoleDTO create(@RequestBody CreateRoleRequest request) {
        return roleService.create(request);
    }

    @PutMapping(value = ConstAPI.AuthenticationAPI.UPDATE_ROLE + "{id}")
    public Boolean update(@PathVariable("id") UUID id, @RequestBody UpdateRoleRequest request) {
        return roleService.update(id, request);
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.TEST_AWS_DEPLOY2)
    public String testAwsDeploy2() {
        return "            *     ,MMM8&&&.            *\n" +
                "                  MMMM88&&&&&    .\n" +
                "                 MMMM88&&&&&&&\n" +
                "     *           MMM88&&&&&&&&\n" +
                "                 MMM88&&&&&&&&\n" +
                "                 'MMM88&&&&&&'\n" +
                "                   'MMM8&&&'      *    \n" +
                "          |\\___/|     /\\___/\\\n" +
                "          )     (     )    ~( .              '\n" +
                "         =\\     /=   =\\~    /=\n" +
                "           )===(       ) ~ (\n" +
                "          /     \\     /     \\\n" +
                "          |     |     ) ~   (\n" +
                "         /       \\   /     ~ \\\n" +
                "         \\       /   \\~     ~/\n" +
                "  jgs_/\\_/\\__  _/_/\\_/\\__~__/_/\\_/\\_/\\_/\\_/\\_\n" +
                "  |  |  |  |( (  |  |  | ))  |  |  |  |  |  |\n" +
                "  |  |  |  | ) ) |  |  |//|  |  |  |  |  |  |\n" +
                "  |  |  |  |(_(  |  |  (( |  |  |  |  |  |  |\n" +
                "  |  |  |  |  |  |  |  |\\)|  |  |  |  |  |  |\n" +
                "  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | cuong";
    }
}
