//package com.exe01.backend.service;
//
//import com.exe01.backend.dto.UserDTO;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//public interface IUserService extends IGenericService<UserDTO> {
//    UserDetailsService userDetailsService();
//
//    ResponseEntity<?> updateUserImage(Long id, String avatarUrl);
//
//    ResponseEntity<?> searchSortFilter(UserDTO userDTO, String sortByFirstName, int page, int limit);
//
//    ResponseEntity<?> searchSortFilterADMIN(UserDTO userDTO, String sortByFirstName, int page, int limit);
//}
