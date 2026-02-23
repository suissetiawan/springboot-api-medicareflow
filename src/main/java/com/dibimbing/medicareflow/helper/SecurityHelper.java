package com.dibimbing.medicareflow.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.dibimbing.medicareflow.entity.UserAccount;

@Component
public class SecurityHelper {

    public static UserDetails convertToUserDetails(UserAccount userAccount) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + userAccount.getRole()));
        
        return User.builder()
            .username(userAccount.getUsername())
            .password(userAccount.getPassword())
            .authorities(authorities)
            .build();
    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        return null;
    }
}
