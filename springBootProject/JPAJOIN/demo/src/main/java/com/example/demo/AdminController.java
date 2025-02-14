package com.example.demo;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController // This means that this class is a Controller
@RequestMapping(path = "api")
@CrossOrigin
public class AdminController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;
    @Autowired
    private AppartmentRepository appartmentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @PostMapping("admin/User")
    public @ResponseBody String CofirmRequest(@RequestBody String jsonStr) throws JSONException {
        JSONObject obj = new JSONObject(jsonStr);
        String usn = obj.getString("username");
        Optional<User> temp = this.userRepository.findById(usn);
        if (!temp.isPresent())
            return "ERROR";
        User existing = temp.get();
        existing.setOwner(true);
        existing.setRequestforOwner(false);
        this.userRepository.save(existing);
        return "SUCCESS";
    }
    @GetMapping("admin/User/{usn}/Appartment/Bookings")
    @ResponseBody Iterable< Booking> getBookingsByHost(@PathVariable String usn){
        User usr=this.userRepository.findById(usn).get();
        List<appartment> temp=this.appartmentRepository.findAllByOwner(usr);
        List<Booking> result=new ArrayList<Booking>();
        for(int i=0;i<temp.size();i++){
            result.addAll(this.bookingRepository.findByAppId(temp.get(i).getId()));
        }
        return result;
    }
    @GetMapping("admin/User/{usn}/Bookings")
    @ResponseBody Iterable<Booking> getBookingsByClient(@PathVariable String usn){
        return this.bookingRepository.findByUserName(usn);
    }
}
