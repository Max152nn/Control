package com.example.demo;

import com.example.demo.domain.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserLog,Long> {
    UserLog findByUsername(String username);
}
