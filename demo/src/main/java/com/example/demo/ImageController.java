package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(path="img")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;
    @PostMapping("user/upload")
    public ImageModel uploadImage(@RequestParam("myFile") MultipartFile file, @RequestParam("id")String id ) throws IOException {
        Integer appId=Integer.parseInt(id);
        System.out.println(appId+"FOR FUCK'S SAKE");
        ImageModel img = new ImageModel( file.getOriginalFilename(),file.getContentType(),file.getBytes(),appId);
        final ImageModel savedImage = imageRepository.save(img);
        System.out.println("Image saved");
        return savedImage;
    }
    @GetMapping("byId")
    @ResponseBody Iterable<ImageModel> getAllImagesById(@RequestParam Integer id){
        return this.imageRepository.findAllByAppId(id);

    }
    @DeleteMapping("user/imgId")
    @ResponseBody String DeleteImageById(@RequestParam("id")Long  id){
        ImageModel temp=this.imageRepository.findById(id).get();
        this.imageRepository.delete(temp);
        return "OK";
    }
}
