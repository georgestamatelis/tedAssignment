package com.example.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppartmentRepository extends CrudRepository<appartment, Integer> {

    public List<appartment> findAllByOwner(User owner);
    ///////////////////////////////////////////////////////////////
    public List<appartment>  findBylocationOrderByPriceAllIgnoringCase(String location);
    ////////////////////////////////////////////////////////////////////
    public List<appartment>  findByLocationAndCapacityOrderByPriceAllIgnoringCase(String location,Integer Capacity);
   //@Query("select a from apparment a where ")
    //public List<appartment>  findByLocationAndCapacityAndDates(String location,Integer capacity);


    public List<appartment>  findByLocationAndCapacityAndHasWifiOrderByPriceAllIgnoringCase(String location,Integer Capacity, Boolean hasWifi);
    public List<appartment>  findByLocationAndCapacityAndHasElevatorOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasElevator);
    public List<appartment>  findByLocationAndCapacityAndHasheatOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasheat);
    public List<appartment>  findByLocationAndCapacityAndHasParkingOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasParking);
    public List<appartment>  findByLocationAndCapacityAndHasTvOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasTV);
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<appartment> findByLocationAndCapacityAndHasWifiAndHasElevatorOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasWifi,Boolean HasElevator);
    public List<appartment> findByLocationAndCapacityAndHasheatAndHasElevatorOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasWifi,Boolean H);
    public List<appartment> findByLocationAndCapacityAndHasParkingAndHasElevatorOrderByPriceAllIgnoringCase(String location,Integer Capacity,Boolean hasWifi,Boolean H);


}

