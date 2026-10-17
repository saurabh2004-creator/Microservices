package com.app.ecom.service;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::mapToUserResponse)
                .toList();
    }

    @Override
    public Optional<UserResponse> getUserById(long id) {
        return userRepo.findById(id).map(this::mapToUserResponse);

    }


    @Override
    public void addUsers(UserRequest userRequest) {
        User user =  new User();
        updateUserFromRequest(user,userRequest);
        userRepo.save(user);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getAddress()!=null){
            Address address = new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setState(userRequest.getAddress().getState());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }


    @Override
    public boolean updateUsers(UserRequest userRequest,long id) {
        return userRepo.findById(id)
                .map(existingUser -> {
                    updateUserFromRequest(existingUser,userRequest);
                    userRepo.save(existingUser);
                    return true;
                }).orElse(false);
    }
    public UserResponse mapToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());
        userResponse.setRole(user.getRole());
        if(user.getAddress() != null) {
            Address address = user.getAddress();
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(address.getStreet());
            addressDTO.setCity(address.getCity());
            addressDTO.setState(address.getState());
            addressDTO.setZipcode(address.getZipcode());
            addressDTO.setCountry(address.getCountry());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }
}
