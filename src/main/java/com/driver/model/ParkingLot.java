package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    String address;

    @OneToMany(mappedBy="parkingLot",cascade = CascadeType.ALL)
    List<Spot> spotLists=new ArrayList<>();

    public ParkingLot() {
    }

    public ParkingLot(Integer id, String name, String address, List<Spot> spotLists) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.spotLists = spotLists;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Spot> getSpotLists() {
        return spotLists;
    }

    public void setSpotLists(List<Spot> spotLists) {
        this.spotLists = spotLists;
    }
}
