package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Repository
public interface AppartmentRepository extends CrudRepository<appartment, Integer> {

    public List<appartment>  findByownernameAllIgnoringCase(String ownername);
    public List<appartment>  findBylocationOrderByPriceAllIgnoringCase(String location);

}

