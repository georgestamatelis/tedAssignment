import { Component, OnInit } from '@angular/core';
import { AppartmentService } from '../appartment.service';
import { appartment } from '../models/appartment';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import {ImageModel} from 'src/app/models/ImageModel';
import { DomSanitizer } from '@angular/platform-browser';
import {NgxPaginationModule} from 'ngx-pagination'; // <-- import the module

declare var ol: any;

@Component({
  selector: 'app-manage-app1',
  templateUrl: './manage-app1.component.html',
  styleUrls: ['./manage-app1.component.css']
})
export class ManageApp1Component implements OnInit {
  ///pagination shit
  p: number = 1;
  map: any;
  lon;
  lat;
  hood:String;
  city:String;
  country:String; 
  ///////////
  public selectedFile;
  public event1;
  imgURL: any;
  receivedImageData: any;
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
  constructor(private router:Router,private route :ActivatedRoute ,private appHttp:AppartmentService,private http:HttpClient,
    private sanitizer:DomSanitizer,private client:HttpClient) { }

  ngOnInit(): void {
    this.cur=new appartment;
    this.appHttp.getAppartmentById(+this.route.snapshot.params.id.split(":")[2]).subscribe(
      data=>{
        this.cur=data
        this.set_up_map();
        let Arr=this.cur.location.split("+");
        this.hood=Arr[0]; this.city=Arr[1] ; this.country=Arr[2];
      }
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
   let url="https://localhost:8443/img/user/imgId?id="+id;
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


 PostIt(){
  this.appHttp.updateAppartment(this.cur,this.city,this.country,this.hood);
 }
 onFileChange(event){
  console.log(event);
  this.selectedFile = event.target.files[0];

  // Below part is used to display the selected image
  let reader = new FileReader();
  reader.readAsDataURL(event.target.files[0]);
  reader.onload = (event2) => {
    this.imgURL = reader.result;
 }
 }  
 set_up_map(){
   var mousePositionControl = new ol.control.MousePosition({
      coordinateFormat: ol.coordinate.createStringXY(4),
      projection: 'EPSG:4326',
      // comment the following two lines to have the mouse position
      // be placed within the map.
      className: 'custom-mouse-position',
      target: document.getElementById('mouse-position'),
      undefinedHTML: '&nbsp;'
    });


    this.map = new ol.Map({
      target: 'map',
      controls: ol.control.defaults({
        attributionOptions: {
          collapsible: false
        }
      }).extend([mousePositionControl]),
      layers: [
        new ol.layer.Tile({
          source: new ol.source.OSM()
        })
      ],
      view: new ol.View({
        center: ol.proj.fromLonLat([73.8567, 18.5204]),
        zoom: 8
      })
    });

    this.map.on('click', (args) =>{
      console.log(args.coordinate);
      var lonlat = ol.proj.transform(args.coordinate, 'EPSG:3857', 'EPSG:4326');
      console.log(lonlat);
      
      var lon = lonlat[0];
      var lat = lonlat[1];
      alert(`lat: ${lat} long: ${lon}`);
      this.cur.longitude=lon;
      this.cur.latitude=lat;
    });
    this.setCenter();
  }

  setCenter() {
    var view = this.map.getView();
    view.setCenter(ol.proj.fromLonLat([this.cur.longitude,this.cur.latitude]));
    view.setZoom(8);
  }
  changeMainPic(){
    this.appHttp.changePic(this.cur.id,this.selectedFile);
    window.alert("picture changer , redirecting");
    window.location.reload();
  }
 
}
