  import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from 'src/app/models/User'
import { message } from '../models/message';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  loginOk:Boolean;
  p: number = 1;
  reply:Boolean;
  public testUsr:User;
  ShowMessages:Boolean;
  show:Boolean;
  messages:message[];
  constructor(public http:UserService,private router :Router,private route:ActivatedRoute) { }

  ngOnInit(): void {
    this.loginOk=false;
    this.ShowMessages=false;
    let arr=this.route.snapshot.params.userName.split(":");
    let userN=arr[1];
    this.http.getUser(userN).subscribe(
      data=>{
          this.testUsr=data;
          console.log(data);
      }
  );
    
  }
  handleClick():void{
    
  }
  Delete_Message():void{

  }
  seeMessages(){
    this.http.getMessages(this.testUsr.userName).subscribe(
      data=>{
        this.messages=data;
        this.ShowMessages=true;
      }
    );
  }
  Reply(){
    this.reply=true;
  }
  Search():void
  {}
  reset(){
    this.reply=false;
  }
  logout():void{
    this.http.logout();
    this.show=false;
    this.router.navigateByUrl("/");

     
  }
}
