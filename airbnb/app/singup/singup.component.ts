import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import {User} from 'src/app/models/User';
import { Router } from '@angular/router';
//import { FormBuilder, FormGroup, FormArray, FormControl, Validators } from '@angular/forms';


@Component({
  selector: 'app-singup',
  templateUrl: './singup.component.html',
  styleUrls: ['./singup.component.css']
})
export class SingupComponent implements OnInit {
  success:Boolean;
  show:Boolean;
  newUser:User;
  secondPassword:String="";
  constructor(private http:UserService,private router:Router) { }

  ngOnInit(): void {
    this.newUser=new User;
    this.newUser.renter=false;
    this.newUser.owner=false;
    this.success=false;
    this.show=false;
  }
  checkCheckBoxvalue(event){
    this.newUser.requestforOwner=!this.newUser.requestforOwner;
  }
  handleClick():void{
    this.show=true;   
    console.log(this.newUser.requestforOwner) 
    if(!this.http.newUser(this.newUser))
      {
        this.success=false;
      }
    else
      this.success=true;

  }

}
