package com.example.demo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    private String userName;
    private String email;
    @NotNull
    private String Password;

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
}
