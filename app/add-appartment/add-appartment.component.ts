import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { AppartmentService } from '../appartment.service';
import { appartment } from '../models/appartment';
import { ActivatedRoute } from '@angular/router';
import{User} from 'src/app/models/User';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-add-appartment',
  templateUrl: './add-appartment.component.html',
  styleUrls: ['./add-appartment.component.css']
})
export class AddAppartmentComponent implements OnInit {

  tempApp:appartment;
  endD:String="";
  startD:String="";
  country:String="";
  city:String="";
  hood:String="";
  dates: String[];
  curentUser:User;
  psw:String;
  /////////////////////////////////////////////////
  public selectedFile;
  public event1;
  imgURL: any;
  receivedImageData: any;
  base64Data: any;
  convertedImage: any;
  constructor(private route: ActivatedRoute,private userHttp:UserService,private appHttp:AppartmentService,private httpClient:HttpClient ) { }

  ngOnInit(): void {
    this.tempApp=new appartment;
    ///initializing all to false
    this.tempApp.hasTv=this.tempApp.hasWifi=this.tempApp.hasElevator=this.tempApp.hasParking=false;
    this.tempApp.hasheat=this.tempApp.idAvailable=this.tempApp.allowPets=false;
    this.tempApp.allowSmoking=false;
    //this.dates=new String[];
  }
 
  get_dates(startD:string,endD:string):String[]{
    var result:String[]=new Array();
    result.push(startD);
    let sd=+startD.split("-")[0]; 
    let sm=+startD.split("-")[1];
    let sy=+startD.split("-")[2];
    let ed=+endD.split("-")[0];
    let em=+endD.split("-")[1];
    let ey=+endD.split("-")[2];
    for(let day=sd+1;day<=30;day++){
      let temp=day.toString()+"-"+sm.toString()+"-"+sy.toString();
      result.push(temp);  
      if(temp==endD)
        return result;
    }
    for(let year =sy ;year<=ey;year++){
      for(let month=sm+1; month<=em;month++){
        for(let day=1;day<=30;day++){
            let temp=day.toString()+"-"+month.toString()+"-"+year.toString();
            result.push(temp);
            if(temp==endD)
              return result;
        }
      }
    }
    return result;

  }
  PostIt():void{
    
    let arr=this.route.snapshot.params.userName.split(":");
    this.tempApp.ownername=arr[1];
    console.log(arr[1]);
    this.userHttp.getUser(arr[1]).subscribe(
      data=>{
        this.curentUser=data;
        this.psw=data.password;}
    );
    var date:String=this.startD;
  //  this.dates=[this.startD,this.endD];
    this.dates=this.get_dates(this.startD.toString(),this.endD.toString());
   // console.log(this.dates);
    console.log(this.dates);
    this.tempApp.dates=this.dates;
    this.appHttp.postAppartment(this.tempApp,this.psw,this.city,this.country,this.hood,this.selectedFile);
  }
  public  onFileChanged(event) {
    console.log(event);
    this.selectedFile = event.target.files[0];

    // Below part is used to display the selected image
    let reader = new FileReader();
    reader.readAsDataURL(event.target.files[0]);
    reader.onload = (event2) => {
      this.imgURL = reader.result;
  };

 }

 
}
