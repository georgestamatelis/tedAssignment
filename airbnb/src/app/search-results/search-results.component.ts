import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../user.service';
import { AppartmentService } from '../appartment.service';
import { appartment } from '../models/appartment';
import { review } from '../models/review';

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.css']
})
export class SearchResultsComponent implements OnInit {

  wifi:Boolean;
  pets:Boolean;
  appartmentTypes:String[]=["private flat","shared room","full house"]
  desiredType:String;
  parking:Boolean;
  ac:Boolean;
  smoking:Boolean;
  tv:Boolean;
  elevator:Boolean;
  country:String;
  city:String;
  neighborhood:String;
  capacity:number;
  startD:String;
  p: number = 1;
  endD:String;
  maxCost:number;
  appList:appartment[];
  constructor(private route: ActivatedRoute,private userHttp:UserService,private appHttp:AppartmentService) { }
  //we need location dates and capacity from url, we will deal with aditional constraints here
  ngOnInit(): void {
    this.startD=this.route.snapshot.params.startD.split(":")[2];
    this.endD=this.route.snapshot.params.endD.split(":")[2];
    this.capacity=this.route.snapshot.params.capacity.split(":")[2];
    this.country=this.route.snapshot.params.country.split(":")[2];
    this.city=this.route.snapshot.params.city.split(":")[2];
    this.neighborhood=this.route.snapshot.params.neighborhood.split(":")[2];
    this.appHttp.getAppartmentsBylocation(this.country,this.city,this.neighborhood,this.startD,this.endD,this.capacity).subscribe(
      data=>{
        this.appList=data;
        console.log(data);
        this.appList.forEach(
          element => {
            console.log(element.id);
            this.appHttp.getReviews(element.id).subscribe(
              res=>{element.reviews=res
                console.log(res)
              element.numberOfReviews=res.length
              }
        );})      }
    )
 }
 getAverage(array: review[]):number{
  var average:number=0;
  console.log(array)
  for(let i=0;i<array.length;i++){
   average+=array[i].number;
  }
  average=average/array.length;
  if(isNaN(average))
    return 0;
  return average;
}
setFilters(){
  
}
}
