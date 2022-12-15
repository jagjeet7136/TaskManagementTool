package com.app.tmtool.repository;

import com.app.tmtool.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String username);

    Users getById(Long id);

    Optional<Users> findById(Long id);
}
