package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class appartment {
    @Id
    @GeneratedValue (strategy=GenerationType.AUTO)
    private  Integer id;
    ///foreign key
    @NotNull
    private String ownername;
    /////characteristics
    private int price;
    private String location;
    private float size;
    private int floor;
    private Boolean hasheat;
    private Boolean idAvailable; //SOS


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Boolean getHasheat() {
        return hasheat;
    }

    public void setHasheat(Boolean hasheat) {
        this.hasheat = hasheat;
    }

    public Boolean getIdAvailable() {
        return idAvailable;
    }

    public void setIdAvailable(Boolean idAvailable) {
        this.idAvailable = idAvailable;
    }
    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }
}
