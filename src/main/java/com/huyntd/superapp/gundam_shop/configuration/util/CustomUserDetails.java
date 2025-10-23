package com.huyntd.superapp.gundam_shop.configuration.util;

import com.huyntd.superapp.gundam_shop.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRole = user.getRole().toString().toUpperCase();
        // Collections.singleton là mảng chỉ cos 1 phần tử
        // Hợp lý vì User chỉ có 1 role, nếu nhiều role thì dùng List
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userRole));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        // Trả về email của user, coi email là định danh chính của người dùng trong Spring Security
        return user.getEmail();
    }

    public int getId() {
        return user.getId();
    }
}
