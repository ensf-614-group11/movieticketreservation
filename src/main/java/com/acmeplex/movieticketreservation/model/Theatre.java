package com.acmeplex.movieticketreservation.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

  @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
  private List<TheatreRoom> theatreRooms;

    public Theatre() {}


    public Theatre(String location) {
        this.location = location;

    }

    public String getLocation(){
        return location; 
    }

    public void setLocation(String location){
        this.location = location; 
    }

    public Long getId() {
        return id;
    }

    public List<TheatreRoom> getTheatreRooms() {
        return theatreRooms;
    }

    public void setTheatreRooms(List<TheatreRoom> theatreRooms) {
        this.theatreRooms = theatreRooms;
    }
}
