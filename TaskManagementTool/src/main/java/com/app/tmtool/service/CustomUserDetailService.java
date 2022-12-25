package com.app.tmtool.service;

import com.app.tmtool.entity.User;
import com.app.tmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User users = userRepository.findByUsername(username);
        if(users==null) {
            throw new UsernameNotFoundException("User not found");
        }
        return users;
    }

    @Transactional
    public User loadUserById(Long id) {
        User users = userRepository.getById(id);
        if(users==null) {
            throw new UsernameNotFoundException("User not found");
        }
        return users;
    }
}
