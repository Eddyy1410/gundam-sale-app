package com.huyntd.superapp.gundam_shop.service.user.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.huyntd.superapp.gundam_shop.dto.request.UserOAuth2RegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UserUpdateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.UserMapper;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.user.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserServiceImplement implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = userMapper.toUser(request);
        newUser.setRole(UserRole.CUSTOMER);

        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    @Override
    public Optional<User> createOAuth2(UserOAuth2RegisterRequest request) {
        return Optional.of(userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .fullName(request.getFullName())
                        .role(UserRole.CUSTOMER)
                        .build())));
    }

    @Override
    public UserResponse getCustomer(String userId) {
        try {
            int id = Integer.parseInt(userId);
            return userMapper.toUserResponse(userRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        } catch (NumberFormatException ex) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user;
        try {
            int id = Integer.parseInt(userId);
            user = userRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        } catch (NumberFormatException ex) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            if (user.getEmail().equals(request.getEmail())) {
                request.setPassword(passwordEncoder.encode(request.getPassword()));
                userMapper.updateUser(user, request);
            } else throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void changePassword(com.huyntd.superapp.gundam_shop.dto.request.PasswordRequest request) {
        // find current user by email present in security context or request - here assume request contains currentPassword and newPassword and we change for currently authenticated user
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse getMyInfo() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<User> getUser() {
        return userRepository.findAll();
    }

}
