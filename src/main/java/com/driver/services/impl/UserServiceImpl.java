package com.driver.services.impl;

import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.User;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository4;

    @Autowired
    SpotRepository spotRepository4;
    @Override
    public void deleteUser(Integer userId) {
        // User has many reservaations made and in each reservation he may select many spots in parkinglots ,so set
        // those spots as available
        Optional<User> optionalUser = userRepository4.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            for (Reservation reservation : user.getReservationList()) {
                     Spot spot=reservation.getSpot();
                             spot.setOccupied(false);
                             spotRepository4.save(spot);

            }
            user.getReservationList().clear();
            userRepository4.save(user); //

        }
        userRepository4.deleteById(userId);
    }

    @Override
    public User updatePassword(Integer userId, String password) {
        User user=new User();
        Optional<User> optionalUser=userRepository4.findById(userId);
        if(optionalUser.isPresent()){
            user=optionalUser.get();
            user.setPassword(password);
        }
        userRepository4.save(user);
        return user;

    }

    @Override
    public void register(String name, String phoneNumber, String password) {
        User user=new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        userRepository4.save(user);

    }
}
