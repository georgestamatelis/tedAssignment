import { Component, OnInit } from '@angular/core';
import { AppartmentService } from '../appartment.service';
import { appartment } from '../models/appartment';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {ImageModel} from 'src/app/models/ImageModel';
import { DomSanitizer } from '@angular/platform-browser';
import {NgxPaginationModule} from 'ngx-pagination'; // <-- import the module



@Component({
  selector: 'app-manage-app1',
  templateUrl: './manage-app1.component.html',
  styleUrls: ['./manage-app1.component.css']
})
export class ManageApp1Component implements OnInit {
  ///pagination shit
  p: number = 1;

  ///////////
  selectedFile;
  retrievedImage: any;
  base64Data: any;
  cur:appartment;
  img:ImageModel;
  imageList:ImageModel[];
  imageBlobUrl:any;
  url:any;
  uploadData:any;
  thumbnail: any;
  imageToShow;
  constructor(private route :ActivatedRoute ,private appHttp:AppartmentService,private http:HttpClient,
    private sanitizer:DomSanitizer,private client:HttpClient) { }

  ngOnInit(): void {
    this.cur=new appartment;
    this.appHttp.getAppartmentById(+this.route.snapshot.params.id.split(":")[2]).subscribe(
      data=>this.cur=data
    );
    this.getImage();
  }
  getImage(){  
    let id=this.route.snapshot.params.id.split(":")[2];
    console.log(id);
    let url="https://localhost:8443/img/byId?id="+id;
    this.http.get<ImageModel[]>(url).subscribe(
      res=>{
        this.imageList=res;
       // this.img=res;
        console.log(res);
       // let url=URL.createObjectURL(res[0].pic);
        let objectURL = 'data:'+res[0].type+';base64,' + res[0].pic;
       // let objectURL = 'data:image/jpeg;base64,' + res[0].pic;
       //this.imageToShow=this.sanitizer.bypassSecurityTrustUrl(url);
       this.imageToShow= 'data:image/jpg;base64,'+(this.sanitizer.bypassSecurityTrustResourceUrl(res[0].pic) as any);//.changingThisBreaksApplicationSecurity;
        //this.imageToShow = this.sanitizer.bypassSecurityTrustResourceUrl(objectURL);
      })
      
  }
  CreateImageFromBlob(image: Blob) {
    console.log(image);
    let reader = new FileReader();
    reader.addEventListener("load", () => {
      this.imageBlobUrl = reader.result;
    }, false);
    if (image) {
      reader.readAsDataURL(image);
    }
  }
 DeleteImage(id:number){
   let url="https://localhost:8443/img/imgId?id="+id;
   this.http.delete<String>(url).subscribe(
     result=>console.log(result)
   );
 }
 public  onFileChanged(event) {
  console.log(event);
  this.selectedFile = event.target.files[0];
  this. uploadData = new FormData();

  this.uploadData.append('myFile',this.selectedFile,this.selectedFile.name);
  this.uploadData.append("id",this.cur.id.toString());
 
 }
 AddPic(){
  let url="https://localhost:8443/img/user/upload";
  this.client.post(url,this.uploadData).subscribe(
    res=>{
      console.log(res)
    }
  );
}  
ChangeDescription(){
  this.appHttp.update_appartment_description(this.cur.description,this.cur.id);
}
 
  

}
