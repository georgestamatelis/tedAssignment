package com.example.demo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
//import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity // This tells Hibernate to make a table out of this class
public class User {
    //data
    @Id
    private String userName;
    private String email;
    @NotNull
    private String Password;
    Boolean requestforOwner;
    Boolean isOwner;
    Boolean isRenter;
    String firstName;
    String lastName;
    String phoneNumber;
    /*@OneToMany
    List<message> sent=new ArrayList<message>();
    @OneToMany
    List<message> received=new ArrayList<message>();
*/
    /////methods
    public void setUserName(String userName){
        this.userName=userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }
    public void setPassword(String ps){
        this.Password=ps;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public Boolean getRequestforOwner() {
        return requestforOwner;
    }

    public void setRequestforOwner(Boolean requestforOwner) {
        this.requestforOwner = requestforOwner;
    }

    public Boolean getRenter() {
        return isRenter;
    }

    public void setRenter(Boolean renter) {
        isRenter = renter;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
