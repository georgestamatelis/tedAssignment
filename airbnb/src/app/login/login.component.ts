import { Component, OnInit } from '@angular/core';
import { UserService } from '../user.service';
import { Route } from '@angular/compiler/src/core';
import { Router } from '@angular/router';
import { ThrowStmt } from '@angular/compiler';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(public http:UserService,private route:Router) { }
  username:String="";
  password:String="";
  logged:Boolean;
  
  ngOnInit(): void {


  }
  handleClick(){

    if(this.username=="admin1"){
      if(this.password!="123")
        return;
      console.log("fuck me");
      this.http.authAdmin().subscribe(
        res=>{
          console.log(res);
          localStorage.setItem('token', res.headers.get('Authorization'));
          this.route.navigateByUrl("/admin")
        }
      )
    }
    else
    this.http.loginUser(this.username,this.password).subscribe(
      res=>{
        console.log(res);
        localStorage.setItem('token', res.headers.get('Authorization'));
        this.logged=true;
        this.route.navigateByUrl("/user/:"+this.username);
        this.http.getUser(this.username);
      }
    );
  }

}
