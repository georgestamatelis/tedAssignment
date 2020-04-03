package com.example.demo;

import org.hibernate.annotations.Table;

import javax.persistence.*;

@Entity
///@Table(name="MESAGE")
public class Message {
    @Id
    @GeneratedValue (strategy=GenerationType.AUTO)
    private  Integer id;
    ////////////
    private String senderUsn;
    private String receiverUsn;
    private String date;
    private String text;
    ////////////////////////////////////////////////////////////////////////
    //////////METHODS
    ////////////////////////////////////////////////////////////


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderUsn() {
        return senderUsn;
    }

    public void setSenderUsn(String senderUsn) {
        this.senderUsn = senderUsn;
    }

    public String getReceiverUsn() {
        return receiverUsn;
    }

    public void setReceiverUsn(String receiverUsn) {
        this.receiverUsn = receiverUsn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
