package com.example.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
@CrossOrigin
public class MainController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;
    @Autowired
    private AppartmentRepository appartmentRepository;
    @Autowired
    private BookingRepository bookingRepository;
    //////////////////////////////////////////////////////////////
    ///USERS
    ////////////////////////////////////////////////////////////////
    @PostMapping(path="/newUser") // Map ONLY POST Requests
    public @ResponseBody String addNewUser(@RequestBody String jsonStr) throws JSONException
    {
        JSONObject obj=new JSONObject(jsonStr);
        User n=new User();
        Optional<User> temp=this.userRepository.findById(obj.getString("username"));
        if(temp.isPresent())
            return "USER ALLREADY EXISTS SORRY";
        n.setUserName(obj.getString("username"));
        n.setPassword(obj.getString("password"));
        n.setEmail(obj.getString("email"));
        n.setFirstName(obj.getString("firstname"));
        n.setLastName(obj.getString("lastname"));
        n.setPhoneNumber(obj.getString("phonenumber"));
        if(obj.getBoolean("Request"))
        {
            n.setRequestforOwner(true);
            n.setOwner(false);
            n.setRenter(true);
            this.userRepository.save(n);
            return "YOU WILL BE NOTIFIED";
        }
        else{
            n.setOwner(false);
            n.setRenter(true);
            n.setRequestforOwner(false);
            this.userRepository.save(n);
            return "OK";
        }
    }

    @GetMapping(path="/admin/allUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
         return userRepository.findAll();

    }
    @GetMapping(path="user/oneUser")
    public @ResponseBody Optional<User> getUserById(@RequestParam("username") String usn)
    {
       //return userRepository.FindByUsername(usn);
        Optional<User>Result=userRepository.findById(usn);
       if(!Result.isPresent())
            return null;
         User temp=Result.get();
         return Result;
         /*System.out.println(temp.getPassword());
         System.out.println(psw);
         return null;*/

    }

    @PutMapping("/EditUserData/Password")
    public @ResponseBody String updatePassword(@RequestBody String jsonStr) throws  JSONException
    {
        JSONObject obj=new JSONObject(jsonStr);
        String username=obj.getString("username");
        String newPassword=obj.getString("newPassword");
        Optional<User> usr=userRepository.findById(username);
        if(!usr.isPresent())
            return "WRONG USERNAME";
        User found=usr.get();
        found.setPassword(newPassword);
        this.userRepository.save(found);
        return "YOUR PASSWORD HAS CHANGED";
    }
    @PutMapping("/EditUserData/Phone")
    public @ResponseBody String editUserDataP(@RequestBody String jsonStr) throws JSONException{
        JSONObject obj=new JSONObject(jsonStr);
        String username=obj.getString("username");
        String phone=obj.getString("phone");
        User found=this.userRepository.findById(username).get();
        found.setPhoneNumber(phone);
        this.userRepository.save(found);
        return "YOUR EMAIL HAS CHANGED";
    }
    @PutMapping("/EditUserData/Email")
    public @ResponseBody String updateEmail(@RequestBody String jsonStr) throws  JSONException
    {
        JSONObject obj=new JSONObject(jsonStr);
        String username=obj.getString("username");
        String newEmail=obj.getString("newEmail");
        Optional<User> usr=userRepository.findById(username);
        if(!usr.isPresent())
            return "WRONG USERNAME";
        User found=usr.get();
        found.setEmail(newEmail);
        this.userRepository.save(found);
        return "YOUR EMAIL HAS CHANGED";
    }

    @PostMapping("admin/ConfirmRequest")
    public @ResponseBody String CofirmRequest(@RequestBody String jsonStr) throws JSONException
    {
        JSONObject obj=new JSONObject(jsonStr);
        String usn=obj.getString("username");
        Optional<User> temp=this.userRepository.findById(usn);
        if(!temp.isPresent())
            return "ERROR";
        User existing=temp.get();
        existing.setOwner(true);
        existing.setRequestforOwner(false);
        this.userRepository.save(existing);
        return "SUCCESS";
    }
    @GetMapping("admin/GetAllRequests")
    public @ResponseBody Iterable<User> getAllRequestingUsers(){
        List<User> result=this.userRepository.getAllByRequestforOwner(true);
        return result;
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("user/appartment/book")
    public @ResponseBody String BookApp(@RequestBody String jsonStr) throws JSONException{
        JSONObject obj=new JSONObject(jsonStr);
        String renter= obj.getString("renter");
        System.out.println(renter);
        Integer id=obj.getInt("appId");
        JSONArray jArray=obj.getJSONArray("Dates");
        ArrayList<String> listdata = new ArrayList<String>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getString(i));
            }
        }
        appartment temp=this.appartmentRepository.findById(id).get();
        temp.removeDates(listdata);
        this.appartmentRepository.save(temp);
        Booking n=new Booking();
        n.setAppId(id);
        n.setUserName(renter);
        n.setDatesBooked(listdata);
        this.bookingRepository.save(n);
        return "OK";
    }
    @GetMapping("user/getBookings")
    @ResponseBody Iterable< Booking> getBookings(@RequestParam("id") Integer appId){
        return this.bookingRepository.findByAppId(appId);
    }
    @GetMapping("admin/getBookingsbyHost")
    @ResponseBody Iterable< Booking> getBookingsByHost(@RequestParam("usn") String usn){
        List<appartment>temp=this.appartmentRepository.findByownernameAllIgnoringCase(usn);
        List<Booking> result=new ArrayList<Booking>();
        for(int i=0;i<temp.size();i++){
            result.addAll(this.bookingRepository.findByAppId(temp.get(i).getId()));
        }
        return result;
    }
    @GetMapping("admin/getBookingsByClient")
    @ResponseBody Iterable<Booking> getBookingsByClient(@RequestParam("usn") String usn){
        return this.bookingRepository.findByUserName(usn);
    }
    @PostMapping("user/uploadProfilePic")
    public @ResponseBody byte[] uploadProfilePic(@RequestParam("imgFile") MultipartFile file,@RequestParam("usn") String usn) throws IOException {
        User temp=this.userRepository.findById(usn).get();
        temp.setPic(file.getBytes());
        userRepository.save(temp);
        return temp.getPic();
    }

}
