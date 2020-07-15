package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    List<Review> findAllByUserName(String UserName);
    List<Review> findAllByAppId(Integer appId);
    List<Review> findAllByUser(User usr);
    List<Review> findAllByAppartment(appartment app);
}
