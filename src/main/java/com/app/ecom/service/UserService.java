package com.app.ecom.service;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> getAllUsers();

    Optional<UserResponse> getUserById(long id);

    void addUsers(UserRequest user);

    boolean updateUsers(UserRequest user,long id);
}
