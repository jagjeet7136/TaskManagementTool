package com.app.tmtool.service;

import com.app.tmtool.entity.Users;
import com.app.tmtool.exceptions.UserNameAlreadyExistsException;
import com.app.tmtool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Users saveUser(Users newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        try {
            Users savedUser = userRepository.save(newUser);
            savedUser.setConfirmPassword("");
            return savedUser;
        }
        catch (Exception ex) {
            throw new UserNameAlreadyExistsException("Username " + newUser.getUsername() + " already exists");
        }
    }
}
