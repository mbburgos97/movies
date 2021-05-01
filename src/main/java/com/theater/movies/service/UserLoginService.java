package com.theater.movies.service;

import com.theater.movies.enums.LoginStatus;
import com.theater.movies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));

        userEntity.setLoginStatus(LoginStatus.ACTIVE);
        userRepository.save(userEntity);

        return new User(userEntity.getUsername(), userEntity.getPassword(), List.of());
    }
}
