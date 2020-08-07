import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from 'src/app/models/User';
import {message} from 'src/app/models/message';
import {ActivatedRoute} from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { appartment } from '../models/appartment';
import { AppartmentService } from '../appartment.service';
import { MessageService } from '../message.service';
import { allowedNodeEnvironmentFlags } from 'process';

@Component({
  selector: 'app-message-exchange',
  templateUrl: './message-exchange.component.html',
  styleUrls: ['./message-exchange.component.css']
})
export class MessageExchangeComponent implements OnInit {

  SenderUsn:String;
  ReceiverUsn:String;
  messageText:String;
  Date:String;
  curApp:appartment;
  com_history:message[];
  constructor(private route: ActivatedRoute,private userhttp:UserService,private httpC:HttpClient,private appHtttp:
    AppartmentService, private messageHttp:MessageService) { }

  ngOnInit(): void {
    this.Date=new Date().toString();
    console.log(this.Date)
    //let input=this.route.snapshot.params.sender;
    //this.SenderUsn=input.split(":")[2];
    ///////////////////////////
    let input=this.route.snapshot.params.receiver;
    this.ReceiverUsn=input.split(":")[2];
    input=this.route.snapshot.params.appartment;
    let appId=input.split(":")[2];
      this.httpC.get("https://localhost:8443/demo/user/UserName",{ responseType: 'text'}).subscribe(
        data=>{
          this.SenderUsn=data;
          console.log(this.SenderUsn,this.ReceiverUsn); 
          this.userhttp.getMessagesBySenderAndReceiver(this.ReceiverUsn,this.SenderUsn).subscribe(
            data=>{
              this.com_history=data;
              console.log(this.com_history);
            });
          }
          );
    this.appHtttp.getAppartmentById(appId).subscribe(
      data=>{
        this.curApp=data
        console.log(data);
      }
    ) ;
   
  }
  Send_Message(){
    this.userhttp.messageUsr(this.SenderUsn,this.ReceiverUsn,this.Date,this.messageText,this.curApp.id);
    alert("Your message has been sent")
    let m=new message();
    m.senderUsn=this.SenderUsn;
    m.receiverUsn=this.ReceiverUsn;
    m.date=this.Date;
    m.text=this.messageText;
    m.appId=this.curApp.id;
    m.answered=false;
  }
  Delete_Message(id){
    this.messageHttp.deleteMessage(id);
    alert("Message Deleted Successfully");
  }
   Test(){

   }
}
