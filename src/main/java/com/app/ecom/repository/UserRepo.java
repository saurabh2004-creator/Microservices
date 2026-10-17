package com.app.ecom.repository;

import com.app.ecom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface UserRepo extends JpaRepository<User, Long> {
}
