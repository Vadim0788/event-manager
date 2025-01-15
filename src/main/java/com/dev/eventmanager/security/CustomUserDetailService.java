package com.dev.eventmanager.security;

import com.dev.eventmanager.users.UserEntity;
import com.dev.eventmanager.users.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        // Вернуть CustomUserDetails с id пользователя и авторитетами
        return new CustomUserDetails(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                Collections.singleton(authority) // Коллекция из одного элемента
        );
//        return User.withUsername(username)
//                .password(user.getPassword())
//                .authorities(user.getRole())
//                .build();
    }

}
