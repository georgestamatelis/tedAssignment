  import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from 'src/app/models/User'
import { message } from '../models/message';
import { Router, ActivatedRoute } from '@angular/router';
import { MessageService } from '../message.service';
import { AppartmentService } from '../appartment.service';
import { Booking } from '../models/booking';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  loginOk:Boolean;
  myBookings:Booking[];
  p: number = 1;
  p2:number=1;
  temp:number=0;
  reply:Boolean;
  public testUsr:User;
  ShowMessages:Boolean;
  show:Boolean;
  messages:message[];
  constructor(public http:UserService,private router :Router,private appHttp:AppartmentService,private route:ActivatedRoute,private messageHttp:MessageService) { }

  ngOnInit(): void {
    this.loginOk=false;
    this.ShowMessages=false;
    let arr=this.route.snapshot.params.userName.split(":");
    let userN=arr[1];
    this.http.getUser(userN).subscribe(
      data=>{
          this.testUsr=data;
          console.log(data);
          this.appHttp.getBookinsByClientObservable(this.testUsr.userName)
          .subscribe(
            res=>{
                this.myBookings=res;
                console.log(res);
            }
          )
      }
  );
    
  }
  Delete_Message(message):void{
    //will add backend request soon
   this.messageHttp.deleteMessage(message.id);
   delete this.messages[this.messages.indexOf(message)];
  }
  seeMessages(){
    this.http.getMessages(this.testUsr.userName).subscribe(
      data=>{
        this.messages=data;
        this.ShowMessages=true;
      }
    );
  }
  Reply(receiver){
    this.router.navigateByUrl("/chat/:receiver:"+receiver);
  }
 
  reset(){
    this.reply=false;
  }
  logout():void{
    this.http.logout();
    this.show=false;
    this.router.navigateByUrl("/");

     
  }
  rateAppartment(id:number,value:number){
    if(value <0 || value >5){
      alert("PLEASE KEEP THE VALUE BETWEEN 0 AND 5")
        return;
    }
    this.appHttp.addReview(this.testUsr.userName,id,value,"no-comment");
  }
}
