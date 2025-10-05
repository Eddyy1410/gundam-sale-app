package com.huyntd.superapp.gundam_shop.service.User.Implement;

import com.huyntd.superapp.gundam_shop.dto.request.UserRegisterRequest;
import com.huyntd.superapp.gundam_shop.mapper.UserMapper;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.User.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserServiceImplement implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    @Override
    public User create(UserRegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("Email already exists");
            return null;
        }

        User newUser = userMapper.toUser(request);
        newUser.setRole(UserRole.CUSTOMER);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);

        return null;
    }
}
