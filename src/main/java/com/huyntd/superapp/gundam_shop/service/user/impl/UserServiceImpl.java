package com.huyntd.superapp.gundam_shop.service.user.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.huyntd.superapp.gundam_shop.dto.request.UserCreateRequest;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createCustomer(UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = userMapper.toUser(request);
        newUser.setRole(UserRole.CUSTOMER);

        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse create(UserCreateRequest request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = userMapper.toUser(request);

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
    public void saveFcmToken(String fcmToken) {
        List<User> users = userRepository.findByRole("STAFF");
        for(User user : users) {
            user.setFcmToken(fcmToken);
            userRepository.save(user);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse getUser(int userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    @Override
    public UserResponse updateUser(int userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if (userRepository.existsByEmail(request.getEmail())) {
            if (user.getEmail().equals(request.getEmail())) {
                request.setPassword(passwordEncoder.encode(request.getPassword()));
                userMapper.updateUser(user, request);
            } else throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAllUsersResponse();
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateMyProfile(com.huyntd.superapp.gundam_shop.dto.request.UserProfileUpdateRequest request) {
        var ctx = SecurityContextHolder.getContext();
        String name = ctx.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        var ctx = SecurityContextHolder.getContext();
        String email = ctx.getAuthentication().getName();
        if (email == null || email.isBlank()) throw new AppException(ErrorCode.UNAUTHENTICATED);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
