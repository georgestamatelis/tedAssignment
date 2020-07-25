import { Injectable } from '@angular/core';
import {User} from 'src/app/models/User'
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HttpHeaders  } from '@angular/common/http';
//import { ResponseContentType, RequestOptions } from '@angular/common/http';

import {appartment} from "src/app/models/appartment"
import {review} from "src/app/models/review";
////////////////////////
@Injectable({
  providedIn: 'root'
})
export class AppartmentService {

  allAppartments:String="https://localhost:8443/demo/allApartments";
  constructor(private http: HttpClient) {  }
  getAllAppartments() :Observable<appartment[]>
  {
    
    return this.http.get<appartment[]>(this.allAppartments.toString());
  }
  getAllAppartmentsBlob(){
  

    return this.http.get(this.allAppartments.toString(), {responseType: 'blob'});
  }
  getAppartmentsBylocation(country:String,city:String,hood:String,start:String,end:String,capacity:number):Observable<appartment[]>
  {
    let url="https://localhost:8443/demo/ByLocation/Dates/?country="+country+"&city="+city+"&neighborhood="+hood+"&startD="+start+"&endD="+end+"&capacity="+capacity;
    return this.http.get<appartment[]>(url);
  }
  getAppartmentById(id:Number): Observable<appartment>
  {
    let url="https://localhost:8443/demo/Appartments/ById?id="+id.toString();
    return this.http.get<appartment>(url);
  }
  getAppartmentsByOwnerName(str:String): Observable<appartment []>{
    let url="https://localhost:8443/demo/user/AppByUsr?username="+str;
    return this.http.get<appartment[]>(url);
  }
  getAppartmentsByOwnerNameBlob(str:String){
    let url="https://localhost:8443/demo/user/AppByUsr?username="+str;
    return this.http.get(url,{responseType: 'blob'});
  }
  //////////post
  postAppartment(app:appartment,psw:String,town:String,country:String,hood:String,selectedFile):void{
    let body={
    "Ownername": app.ownername,
	  "OwnerPassword": psw,
	  "size":app.size,
	"country":country,
	"town":town,
	"neighborhood":hood,
	"hasHeat": app.hasheat,
	"floor":app.floor,
	"AllowPets":app.allowPets,
	"Price":app.price,
	"AllowSmoking":app.allowSmoking,
	"Address":app.address,
	"HasParking":app.hasParking,
	"HasWifi":app.hasWifi,
	"capacity":app.capacity,
  "Dates":app.dates,
  "longitude":app.longitude,
  "latitude":app.latitude,
  "type":app.type,
  "numberOfBeds":app.numberOfBeds,
  "accessInfo":app.accessInfo
    }
    let f=new FormData()
    f.append("imgFile",selectedFile);
    console.log("fuck");
    let url="https://localhost:8443/demo/newApartment";
    this.http.post<Number>(url,body).subscribe(
      data=>{          
      this.http.post<String>("https://localhost:8443/demo/user/updateAppImage?id="+data,f).subscribe(
        res=>{console.log(res);}
      );
      }
    );
  }
  /////////delete
  deleteAppartment(appId:Number){
    let url="https://localhost:8443/demo/appartment"+appId.toString();
    let body={
      "ID":appId
    };
    var subscription:String="";
    this.http.delete<String>(url).subscribe(
      data=>subscription=data
    );
  }
  bookAppartment(appId:number,Dates:String[],usn:String){
    let body={
      "appId":appId,
      "renter":usn,
      "Dates":Dates
    };
    let url="https://localhost:8443/demo/user/appartment/book";
    let subscription="";
    this.http.post<String>(url,body).subscribe(
      data=>{
        subscription=data.toString()
        console.log(data);
      }
    );

  }
  getReviews(appId:Number):Observable<review[]>
  {
    let Url="https://localhost:8443/accesories/getReviews?id="+appId;
    return this.http.get<review[]>(Url);
  }
  addReview(usn:String,appId:number,review:number,comment:String){
    let body={
      "userName":usn,
     "appId":appId,
     "comment":comment,
     "number":review
    };
    let url="https://localhost:8443/accesories/newReview";
    let subscription="";
    this.http.post<String>(url,body).subscribe(
      data=>subscription=data.toString()
    );
  }
  getReviewByOwnerName(usn:String){
    let url="https://localhost:8443/accesories/getReviewsByOwner?usn="+usn;
    return this.http.get(url,{responseType: 'blob'});
  }
  getReviewsByConductor(usn:String){
    let url="https://localhost:8443/accesories/getReviewsByCreator?usn="+usn;
    return this.http.get(url,{responseType: 'blob'});
  }
  getBookingsByHost(usn:String){
    let url="https://localhost:8443/demo/getBookingsbyHost?usn="+usn;
    return this.http.get(url,{responseType: 'blob'});
  }
  getBookingsByClient(usn:String){
    let url="https://localhost:8443/demo/getBookingsByClient?usn="+usn;
    return this.http.get(url,{responseType: 'blob'});
  }
  update_appartment(cur:appartment ){
    let body={
   "pets":cur.allowPets,
	"smoke":cur.allowSmoking,
	"capacity":cur.capacity,
	"floor":cur.floor,
	"ac":cur.hasheat,
	"parking":cur.hasParking,
	"wifi":cur.hasWifi,
	"price":cur.price,
	"location":cur.location,
	"lift":cur.hasElevator,
	"description":cur.description,
	"Dates":cur.dates
    }
    let url="https://localhost:8443/demo/skata";
    this.http.put<String>(url,body).subscribe(
      data=>{
        console.log(data);
      }
    )
  }
  update_appartment_description(description:String,id:number){
    let url="https://localhost:8443/demo/user/updateAppDescription";
    this.http.post<string>(url,{"id":id,"description":description}).subscribe(
      response=>{
        console.log(response);
      }
    )
  }
}