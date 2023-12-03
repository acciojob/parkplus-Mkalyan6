package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;

    @Autowired
    ReservationRepository reservationRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        // create an parkingLot object and save to database
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setAddress(address);
        parkingLot.setName(name);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot spot=new Spot();
        // find the parking lot  with the given id first
        Optional<ParkingLot> parkingLotOptional=parkingLotRepository1.findById(parkingLotId);
        if(parkingLotOptional.isPresent()){
            ParkingLot parkingLot=parkingLotOptional.get();

            spot.setPricePerHour(pricePerHour);

            if(numberOfWheels<=2)spot.setSpotType(SpotType.TWO_WHEELER);
            else if(numberOfWheels<=4)spot.setSpotType(SpotType.FOUR_WHEELER);
            else{ spot.setSpotType(SpotType.OTHERS);}

            spot.setOccupied(false);

//            parkingLot.getSpotLists().add(spot);
            spot.setParkingLot(parkingLot);
            parkingLot.getSpotList().add(spot);
            parkingLotRepository1.save(parkingLot);

        }

        return spot;
    }

    @Override
    public void deleteSpot(int spotId) {
        // find the spot from the database with the spotId
        Optional<Spot> optionalSpot=spotRepository1.findById(spotId);
        if(optionalSpot.isPresent()){
            Spot spot=optionalSpot.get();
            ParkingLot parkingLot=spot.getParkingLot();
            spot.setParkingLot(null);
//            parkingLot.getSpotLists().remove(spot);
//            parkingLotRepository1.save(parkingLot);
            spotRepository1.save(spot);
        }
        spotRepository1.deleteById(spotId);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
//        Optional<Spot> optionalSpot=spotRepository1.findById(spotId);
//        Spot spot=new Spot();
//          spot.setPricePerHour(pricePerHour);
//        if(optionalSpot.isPresent()){
//             spot=optionalSpot.get();
//            spot.setPricePerHour(pricePerHour);
//
//            // if parkinglot with the id provided is found ,then update it in spot entity
//             Optional<ParkingLot> optionalParkingLot=parkingLotRepository1.findById(parkingLotId);
//             if(optionalParkingLot.isPresent()){
//                 ParkingLot parkingLot=optionalParkingLot.get();
//
//                 // delete the present parking lot the spot is present, then update with the new parking lot
//                 spot.setParkingLot(null);
//                 spotRepository1.save(spot);
//                 spot.setParkingLot(parkingLot);
//                 spotRepository1.save(spot);
//
//             }
//        }
//        return spot;
        Optional<ParkingLot> optionalParkingLot=parkingLotRepository1.findById(parkingLotId);
        if(optionalParkingLot.isPresent()){
            List<Spot>spotList=optionalParkingLot.get().getSpotList();
            Optional<Spot> optionalSpot=spotRepository1.findById(spotId);
            if(optionalSpot.isPresent()){
                if(spotList.contains(optionalSpot.get())){
                    Spot spot=optionalSpot.get();
                    spot.setOccupied(false);
                    spot.setParkingLot(optionalParkingLot.get());
                    spot.setPricePerHour(pricePerHour);
                    spotRepository1.save(spot);
                }
            }
        }
        return new Spot();


    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

        Optional<ParkingLot> optionalParkingLot=parkingLotRepository1.findById(parkingLotId);
        if(optionalParkingLot.isPresent()){
            ParkingLot parkingLot=optionalParkingLot.get();
            for(Spot spot: parkingLot.getSpotList()){
                for(Reservation reservation: spot.getReservationList()){
                    reservation.setUser(null);
                    reservationRepository1.save(reservation);
                }
            }
        }
       parkingLotRepository1.deleteById(parkingLotId);

        }

    }

