package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/bonus") // This means URL's start with /demo (after Application path)
@CrossOrigin
public class RecomedationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppartmentRepository appartmentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private int get_Average_Review(User ui){
        List<Review> allR=this.reviewRepository.findAllByUserName(ui.getUserName());
        int sum=0;
        for(int i=0;i<allR.size();i++){
            sum+=allR.get(i).getNumber();
        }
        return sum/allR.size();
    }
    private int RatingFunction(User ui,appartment app_i){
        if(this.reviewRepository.findAllByAppId(app_i.getId())==null)
        {
            //not reviewed
            return 0;
        }

        return 0;
    }
    @GetMapping("user/Recomended")
    public Iterable<appartment> getRecomended(@RequestParam("usn")String usn){
        List<appartment> allAppartments=(List)this.appartmentRepository.findAll();
        int number_of_results=5;
        return null;
    }
}
