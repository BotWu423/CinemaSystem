package com.example.movietheatersystem.config;

import com.example.movietheatersystem.entity.User;
import com.example.movietheatersystem.repository.UserRepository;
import com.example.movietheatersystem.security.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("User Not Found with -> username or email : " + username));

        return UserPrinciple.build(user); // 假设你有一个 UserPrinciple 类来构建 UserDetails 对象
    }
}