package com.example.demo;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/accesories") // This means URL's start with /demo (after Application path)
@CrossOrigin
public class SecondraryController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private  ReviewRepository reviewRepository;
    ////////////////////////////
    @PostMapping("messages/newMessage")
    public @ResponseBody
    String newMessage(@RequestBody String jsonStr)throws JSONException
    {
        JSONObject obj=new JSONObject(jsonStr);
        String receiverUsn=obj.getString("receiver");
        String senderUsn=obj.getString("sender");
        String date=obj.getString("date");
        String text=obj.getString("text");
        Message msg=new Message();
        msg.setDate(date);
        msg.setReceiverUsn(receiverUsn);
        msg.setSenderUsn(senderUsn);
        msg.setText(text);
        messageRepository.save(msg);
        return "OK";
    }
    @GetMapping("messages/getAllByUsr")
    public @ResponseBody Iterable<Message> getAllByUsr(@RequestParam("receiver") String receiver){
        return this.messageRepository.findAllByReceiverUsn(receiver);
    }
    @DeleteMapping("messages/delete")
    public @ResponseBody String DeleteM(@RequestParam("id")Integer  id){
        this.messageRepository.delete(this.messageRepository.findById(id).get());
        return "OK";
    }
    ///now time for reviews
    @PostMapping("newReview")
    public  @ResponseBody String newReview(@RequestBody String jsonStr)throws JSONException
    {
        JSONObject obj=new JSONObject(jsonStr);
        Review n=new Review();
        n.setUserName(obj.getString("userName"));
        n.setAppId(obj.getInt("appId"));
        //System.out.println("NOOOOOOO "+obj.getInt("appId"));

        n.setComment(obj.getString("comment"));
        n.setNumber(obj.getInt("number"));
        reviewRepository.save(n);
        return  "OK";
    }
    @GetMapping("getReviews")
    public @ResponseBody Iterable<Review> getReviews(@RequestParam("id") Integer appId){
            return this.reviewRepository.findAllByAppId(appId);
    }
}
