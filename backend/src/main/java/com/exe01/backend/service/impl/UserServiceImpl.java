package com.exe01.backend.service.impl;

import com.exe01.backend.dto.UserDTO;
import com.exe01.backend.repository.UserRepository;
import com.exe01.backend.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.exe01.backend.entity.User;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UserDTO findById(Long id) {
        return null;
    }

    @Override
    public List<UserDTO> findAllByStatusTrue(int page, int limit) {
        return null;
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> userlist = userRepository.findAll();
        return userlist.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    @Override
    public ResponseEntity<?> save(UserDTO userDTO) {
        return null;
    }

    @Override
    public ResponseEntity<?> changeStatus(Long id) {
        return null;
    }

    @Override
    public Boolean checkExist(Long id) {
        return null;
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

    @Override
    public ResponseEntity<?> updateUserImage(Long id, String avatarUrl) {
        return null;
    }

    @Override
    public ResponseEntity<?> searchSortFilter(UserDTO userDTO, String sortByFirstName, int page, int limit) {
        return null;
    }

    @Override
    public ResponseEntity<?> searchSortFilterADMIN(UserDTO userDTO, String sortByFirstName, int page, int limit) {
        return null;
    }
}
