package com.exe01.backend.service;

import com.exe01.backend.dto.UserDTO;
import com.exe01.backend.dto.request.user.CreateUserRequest;
import com.exe01.backend.dto.request.user.UpdateUserRequest;
import com.exe01.backend.models.PagingModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends IGenericService<UserDTO> {
    UserDetailsService userDetailsService();

    PagingModel findAllWithPaging(Integer page, Integer limit);

    UserDTO create(CreateUserRequest request);

    UserDTO update(UpdateUserRequest request, Long id);

//    ResponseEntity<?> updateUserImage(Long id, String avatarUrl);
//
//    ResponseEntity<?> searchSortFilter(UserDTO userDTO, String sortByFirstName, int page, int limit);
//
//    ResponseEntity<?> searchSortFilterADMIN(UserDTO userDTO, String sortByFirstName, int page, int limit);
}
