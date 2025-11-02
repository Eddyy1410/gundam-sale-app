package com.huyntd.superapp.gundam_shop.configuration.util;

import com.huyntd.superapp.gundam_shop.dto.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {

    private final UserPrincipal userPrincipal;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRole = userPrincipal.getRole().toString().toUpperCase();
        // Collections.singleton là mảng chỉ cos 1 phần tử
        // Hợp lý vì User chỉ có 1 role, nếu nhiều role thì dùng List
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userRole));
    }

    public int getId() {
        return userPrincipal.getId();
    }

    @Override
    public String getUsername() {
        // Trả về email của user, coi email là định danh chính của người dùng trong Spring Security
        return userPrincipal.getEmail();
    }

    @Override
    public String getPassword() {
        return null;
    }

    // Quan trọng: Phương thức để lấy DTO nếu cần truy cập các thông tin khác trong Controller
    public UserPrincipal getUser() {
        return this.userPrincipal;
    }

}
