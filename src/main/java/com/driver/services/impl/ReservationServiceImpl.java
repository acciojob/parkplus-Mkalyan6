package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

          Optional<ParkingLot> parkingLotOptioanal=parkingLotRepository3.findById(parkingLotId);
           Optional<User> optionalUser=userRepository3.findById(userId);

           Reservation reservation=new Reservation();

           if(optionalUser.isPresent()&&parkingLotOptioanal.isPresent() ) {
               User user = optionalUser.get();
                   ParkingLot parkingLot = parkingLotOptioanal.get();
                    // Go through all the spots in the parkinglot and find the one which is less coslier when compared
                     int totalCost=Integer.MAX_VALUE;
                     Spot reserSpot=null;

                     for(Spot spot: parkingLot.getSpotList()) {
                         if (!spot.getOccupied()) {
                             int wheelsForSpot = 0;

                             if (spot.getSpotType() == SpotType.TWO_WHEELER) wheelsForSpot = 2;
                             else if (spot.getSpotType() == SpotType.FOUR_WHEELER) wheelsForSpot = 4;
                             else {
                                 wheelsForSpot = Integer.MAX_VALUE;
                             }

                             if (wheelsForSpot >= numberOfWheels) {
                                 int totalForPresentSpot = spot.getPricePerHour() * timeInHours;
                                 if (totalCost > totalForPresentSpot) {
                                     // change the total cost
                                     // change the spot to be reserved , as it leading in less total cost;
                                     totalCost = totalForPresentSpot;
                                     reserSpot = spot;
                                 }

                             }
                         }
                     }
                     if(reserSpot==null){
                         throw new Exception("Cannot make reservation");// means in the parking lot we donot get the spot which
                         // suits the wheels of the vehicle
                     }

                     // Now make the reservation and assign values to attributes
                     reserSpot.setOccupied(true);

                     reservation.setSpot(reserSpot);
                     reservation.setUser(user);
                     reservation.setNumberOfHours(timeInHours);

                     Payment payment=new Payment();
                     payment.setPaymentCompleted(false);
                     reservation.setPayment(payment);
                     reservationRepository3.save(reservation);





           }else{
               throw new Exception("Cannot make reservation");
           }
        reservationRepository3.save(reservation);
           return  reservation;

    }
}
