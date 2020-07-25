import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
//import { Observable } from 'rxjs';
import{UserService} from 'src/app/user.service';
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private http:UserService,private router:Router){

  }
 canActivate():any{
  if(this.http.loggedIn())
    return true;
  window.alert("Access not allowed!");
  return false;
 }
  
}
