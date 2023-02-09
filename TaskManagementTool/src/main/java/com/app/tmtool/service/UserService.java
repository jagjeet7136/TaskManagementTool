package com.app.tmtool.service;

import com.app.tmtool.entity.User;
import com.app.tmtool.exceptions.UnauthorizedException;
import com.app.tmtool.exceptions.UserNameAlreadyExistsException;
import com.app.tmtool.repository.UserRepository;
import com.app.tmtool.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User saveUser(User newUser) {
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        try {
            User savedUser = userRepository.save(newUser);
            savedUser.setConfirmPassword("");
            return savedUser;
        }
        catch (Exception ex) {
            logger.error("some error occur: {} ", ex.getMessage());
            throw new UserNameAlreadyExistsException("Username " + newUser.getUsername() + " already exists");
        }
    }

    public String getAccessToken(String googleIdToken) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                new JacksonFactory())
                .setAudience(Collections.singletonList(
                        "1058285539800-giakdnaabdjjs4v02ln6lupqbd9h8a6t.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(googleIdToken);
        }
        catch (Exception ex) {
            logger.error("token is not valid {}", googleIdToken);
            throw new UnauthorizedException("Unauthorized");
        }
        if(idToken == null ) {
            logger.error("token is not valid {}", googleIdToken);
            throw new UnauthorizedException("Unauthorized");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        if(!payload.getEmailVerified()) {
            logger.error("email not verified {}", payload.getEmail());
            throw new UnauthorizedException("Unauthorized");
        }

        User user = userRepository.findBySub(payload.getSubject());
        Long userId;
        if(user == null) {
            user = new User();
            user.setSub(payload.getSubject());
            user.setUsername(payload.getEmail());
            user.setFullName(payload.get("name").toString());
        }

        userId = (userRepository.save(user)).getId();
        return jwtTokenProvider.generateToken(userId, user.getFullName(), payload.getEmail());
    }
}
