package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id;
   // @ForeignKey
    String userName;
 ///   @ForeignKey
    Integer appId;
    @ElementCollection
    List<String> DatesBooked=new ArrayList<String>();


    public List<String> getDatesBooked() {
        return DatesBooked;
    }

    public void setDatesBooked(List<String> datesBooked) {
        DatesBooked = datesBooked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
       this.userName = userName;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}
