package com.exe01.backend.controller;

import com.exe01.backend.constant.ConstAPI;
import com.exe01.backend.dto.UserDTO;
import com.exe01.backend.dto.request.user.CreateUserRequest;
import com.exe01.backend.dto.request.user.UpdateUserRequest;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping(value = ConstAPI.AuthenticationAPI.LOGIN_WITH_PASSWORD_USERNAME)
    public List<UserDTO> login() {
        return userService.findAll();
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.USER)
    public PagingModel findAllUserOrderByIdWithPaging(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        return userService.findAllWithPaging(page, limit);
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.FIND_USER_BY_ID + "{id}")
    public UserDTO findUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(value = ConstAPI.AuthenticationAPI.CREATE_USER)
    public UserDTO create(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @PutMapping(value = ConstAPI.AuthenticationAPI.UPDATE_USER + "{id}")
    public UserDTO update(@RequestBody UpdateUserRequest request,  @PathVariable("id") Long id) {
        return userService.update(request, id);
    }

    @GetMapping(value = ConstAPI.AuthenticationAPI.TEST_AWS_DEPLOY)
    public String testAwsDeploy() {
        return "hello test aws deploy";
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
