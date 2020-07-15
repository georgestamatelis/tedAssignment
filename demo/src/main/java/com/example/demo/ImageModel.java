package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="image_model")
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class ImageModel {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;
    @Column(name="appId")
    private Integer appId;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    //Custom Construtor
    public ImageModel(String name, String type, byte[] pic,Integer appId) {
        this.name = name;
        this.type = type;
        this.pic = pic;
        this.appId=appId;
    }
}