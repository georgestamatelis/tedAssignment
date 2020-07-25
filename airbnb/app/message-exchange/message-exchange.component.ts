import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from 'src/app/models/User';
import {message} from 'src/app/models/message';
import {ActivatedRoute} from '@angular/router';
import { HttpClient } from '@angular/common/http';

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
  com_history:message[];
  constructor(private route: ActivatedRoute,private userhttp:UserService,private httpC:HttpClient) { }

  ngOnInit(): void {
    this.Date=new Date().toString();
    console.log(this.Date)
    //let input=this.route.snapshot.params.sender;
    //this.SenderUsn=input.split(":")[2];
    ///////////////////////////
    let input=this.route.snapshot.params.receiver;
    this.ReceiverUsn=input.split(":")[2];
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
   
  }
  Send_Message(){
    this.userhttp.messageUsr(this.SenderUsn,this.ReceiverUsn,this.Date,this.messageText);
  }

   Test(){

   }
}
