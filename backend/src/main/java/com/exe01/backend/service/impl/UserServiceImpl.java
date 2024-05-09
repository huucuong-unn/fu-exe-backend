package com.exe01.backend.service.impl;

import com.exe01.backend.dto.UserDTO;
import com.exe01.backend.dto.request.user.CreateUserRequest;
import com.exe01.backend.dto.request.user.UpdateUserRequest;
import com.exe01.backend.entity.Role;
import com.exe01.backend.entity.User;
import com.exe01.backend.models.PagingModel;
import com.exe01.backend.repository.RoleRepository;
import com.exe01.backend.repository.UserRepository;
import com.exe01.backend.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id);
        UserDTO userDTO = new UserDTO();
        if (Objects.isNull(user)) {
            return userDTO;
        }
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhone(user.getPhone());
        userDTO.setDob(user.getDob());
        userDTO.setGender(user.getGender());
        userDTO.setAvatarUrl(user.getAvatarUrl());
        userDTO.setUserRoleId(user.getRoleId().getId());
        userDTO.setCreateBy(user.getCreateBy());
        userDTO.setModifiedBy(user.getModifiedBy());
        return userDTO;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setPhone(user.getPhone());
            userDTO.setDob(user.getDob());
            userDTO.setGender(user.getGender());
            userDTO.setAvatarUrl(user.getAvatarUrl());
            userDTO.setUserRoleId(user.getRoleId().getId());
            userDTO.setCreateBy(user.getCreateBy());
            userDTO.setModifiedBy(user.getModifiedBy());
            return userDTO;
        }).collect(Collectors.toList());
    }

    public PagingModel findAllWithPaging(Integer page, Integer limit) {
        PagingModel result = new PagingModel();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<User> users = userRepository.findAllByOrderById(pageable);
        result.setTotalPage(((int) Math.ceil((double) (totalItemForAdmin()) / limit)));
        result.setListResult(users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setPhone(user.getPhone());
            userDTO.setDob(user.getDob());
            userDTO.setGender(user.getGender());
            userDTO.setAvatarUrl(user.getAvatarUrl());
            userDTO.setUserRoleId(user.getRoleId().getId());
            userDTO.setCreateBy(user.getCreateBy());
            userDTO.setModifiedBy(user.getModifiedBy());
            return userDTO;
        }).collect(Collectors.toList()));
        return result;
    }

    private Integer totalItemForAdmin() {
        return (int) userRepository.count();
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO create(CreateUserRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setAvatarUrl(request.getAvatarUrl());

        Role role = roleRepository.findById(request.getRoleId());

        if (Objects.isNull(role)) {
            throw new EntityNotFoundException();
        }

        user.setRoleId(role);
        userRepository.save(user);

        return convertToDTO(user);
    }

    @Override
    public UserDTO update(UpdateUserRequest request, Long id) {
        User user = userRepository.findById(id);

        if (Objects.isNull(user)) {
            throw new EntityNotFoundException();
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setDob(request.getDob());
        user.setGender(request.getGender());
        user.setAvatarUrl(request.getAvatarUrl());

        Role role = roleRepository.findById(request.getRoleId());

        if (Objects.isNull(role)) {
            throw new EntityNotFoundException();
        }

        user.setRoleId(role);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPhone(user.getPhone());
        userDTO.setDob(user.getDob());
        userDTO.setGender(user.getGender());
        userDTO.setAvatarUrl(user.getAvatarUrl());
        userDTO.setUserRoleId(user.getRoleId().getId());
        userDTO.setCreateBy(user.getCreateBy());
        userDTO.setModifiedBy(user.getModifiedBy());
        return userDTO;
    }


    @Override
    public Boolean checkExist(Long id) {
        User user = userRepository.findById(id);

        return !Objects.isNull(user);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

//    @Override
//    public ResponseEntity<?> updateUserImage(Long id, String avatarUrl) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<?> searchSortFilter(UserDTO userDTO, String sortByFirstName, int page, int limit) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<?> searchSortFilterADMIN(UserDTO userDTO, String sortByFirstName, int page, int limit) {
//        return null;
//    }
}
