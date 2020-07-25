import { Component, OnInit, Input } from '@angular/core';
import { appartment } from '../models/appartment';
import { ActivatedRoute, Router } from '@angular/router';
import { AppartmentService } from '../appartment.service';
import { UserService } from '../user.service';
import {User} from 'src/app/models/User';
import { HttpClient } from '@angular/common/http';
import {ImageModel} from 'src/app/models/ImageModel';
import {NgxPaginationModule} from 'ngx-pagination'; // <-- import the module

declare var ol: any;

@Component({
  selector: 'app-appartment-details',
  templateUrl: './appartment-details.component.html',
  styleUrls: ['./appartment-details.component.css']
})

export class AppartmentDetailsComponent implements OnInit {

 p:number=1;
  map: any;
has_reviewed:Boolean;
  Question:String;
  openChat:Boolean;
  userLoggedIn:Boolean;
  cur:appartment;
  id:String;
  id1:number;
  Dates:String[];
  review:number;
  usr:User; 
  imageList:ImageModel[];
  //averageReview:number;
  constructor(private httpC:HttpClient,private route: ActivatedRoute,private router:Router,private apphttp:AppartmentService,public userhttp:UserService,private http:HttpClient) { }

  ngOnInit(): void {   
    /////////////////////////////////////////
    this.userLoggedIn=this.userhttp.isAuthenticated();
    console.log(this.userLoggedIn);
    this.id=this.route.snapshot.params.id;
    console.log(this.id);
    var arr=this.id.split(":");
    this.id1=+arr[2];
    console.log(this.id1);
    this.apphttp.getAppartmentById(this.id1).subscribe(
      data=>{this.cur=data;
      console.log(this.cur);
      this.set_up_map();
    }
    );
  this.openChat=false;  
  let sd=this.route.snapshot.params.startD;
  arr=sd.split(":");
  let StartD=arr[2];
  console.log(StartD);
  let ed=this.route.snapshot.params.endD;
  arr=ed.split(":");
  let endD=arr[2];
  console.log(endD);
  this.Dates=[StartD,endD]; //for now
  this.getImages();
   
  }
  set_up_map(){
    console.log(this.cur.longitude,this.cur.latitude);
    this.map = new ol.Map({
      target: 'map',
      layers: [
        new ol.layer.Tile({
          source: new ol.source.OSM()
        })
      ],
      view: new ol.View({
        center: ol.proj.fromLonLat([this.cur.longitude,this.cur.latitude]),
        zoom: 8
      })
    });
  }
  Book():void{
    if(this.userhttp.loggedIn())
      {
        this.apphttp.bookAppartment(this.id1,this.Dates,this.userhttp.getLastUsr().userName);
        document.getElementById("resUlt").innerHTML="Success";
        console.log(this.Dates);
      }
    else{
      window.alert("You must be logged in to make a purchase!");
    }

  }
  setCenter() {
    var view = this.map.getView();
    view.setCenter(ol.proj.fromLonLat([this.cur.longitude, this.cur.latitude]));
    view.setZoom(8);
  }
  message():void{
    if(this.userhttp.loggedIn())
     {
     // this.openChat=true;
      var usn:String
      this.httpC.get("https://localhost:8443/demo/user/UserName",{ responseType: 'text'}).subscribe(
        data=>{
          usn=data; 
          console.log(data);
          this.router.navigateByUrl("/chat/:receiver:"+this.cur.ownername);

        }
      )
     } 
    else
      window.alert("You must be logged in to send a message");
  }
  submit(){
    if(!this.userhttp.loggedIn())
     {
       window.alert("You must be logged in to make a review");
       return;
     }
    if(this.has_reviewed)
      {
        window.alert("You have allready reviewed the appartment prety recently");
        return;
      }
    this.has_reviewed=true;
    console.log(this.review);
    this.apphttp.addReview(this.userhttp.getLastUsr().userName,this.cur.id,this.review," ");
  }
  sendMessage(){
    this.usr=this.userhttp.getLastUsr();
    this.userhttp.messageUsr(this.usr.userName,this.cur.ownername,"2-2-2020",this.Question)
  }
  getImages(){
    let url="https://localhost:8443/img/byId?id="+this.id1;
    this.http.get<ImageModel[]>(url).subscribe(
      res=>{
        this.imageList=res;
        console.log(res);
      }
    )
  }
}
