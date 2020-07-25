import { Injectable } from '@angular/core';
import {User} from 'src/app/models/User'
import { HttpClient,HttpHeaders, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { message } from './models/message';
//import { NULL_EXPR } from '@angular/compiler/src/output/output_ast';
//import { maxHeaderSize } from 'http';


@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  tester:User; 
  allUsers: String="https://localhost:8443/demo/admin/allUsers";
  constructor(private http: HttpClient) { }

  authAdmin():Observable<HttpResponse<string>>{
    let loginUrl="https://localhost:8443/login";
    let ln={
      "username":"admin1",
      "password":"123"
    }
    console.log("the fuck's going on??");
    return this.http.post<string>(loginUrl, ln, { observe: 'response'});
  }
  XauthAdmin(){
   localStorage.removeItem("token");
  }   
  isAdminloggedIn(){
    if(localStorage.getItem("token"))
      return true;
    return false;
  }   

  
  isAuthenticated(){
    return !!localStorage.getItem("token");
  }
  getLastUsr():User
  {
    return this.tester;
  }
  getUser(usn:String) :Observable<User>
  {
    let url="https://localhost:8443/demo/user/oneUser/?username="+usn;
    this.http.get<User>(url).subscribe(data=>this.tester=data);
    return this.http.get<User>(url);
  }
  getAllUsers() :Observable<User[]>
  {

    return this.http.get<User[]>(this.allUsers.toString());
  }
  getAllUserNames() :Observable<String[]>{
    return this.http.get<String[]>("https://localhost:8443/demo/allUserNames");
  }
  getAllRequestingUsers(): Observable<User[]>
  {
    return this.http.get<User[]>("https://localhost:8443/demo/admin/GetAllRequests");
  }
  confirmRequest(username:String): void {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      })
    };
    
    let tempUrl="https://localhost:8443/demo/admin/ConfirmRequest";
    console.log(username);
    let body={
      "username":username
    }
    let subscription="";
    this.http.post<String>(tempUrl,body,httpOptions).subscribe(result=> subscription=result.toString());
  }
  //////////////////////////////////////////////////
  newUser(usr :User): Boolean{
    const httpOptions = {
      headers: { 'Content-Type': 'application/json'},    
    };
    let tempUrl="https://localhost:8443/demo/newUser";
    let subscription="";
    let body={
      "username":usr.userName,
      "password":usr.password,
      "email":usr.email,
       "phonenumber":usr.phoneNumber,
      "firstname":usr.firstName,
      "lastname":usr.lastName,
      "Request":"false",
      "renterReq":usr.renter
  };

    if(usr.requestforOwner)
    {
      body={
        "username":usr.userName,
        "password":usr.password,
        "email":usr.email,
         "phonenumber":usr.phoneNumber,
        "firstname":usr.firstName,
        "lastname":usr.lastName,
         "Request":"true",
         "renterReq":usr.renter
        }
    };
    
   this.authAdmin().subscribe(
    res=>{
      console.log(res);
      localStorage.setItem('token', res.headers.get('Authorization'));
    });
    this.getUser(usr.userName).subscribe(
      data=>this.tester=data
    );
    if(this.tester)
        return false
    
   
    this.http.post<String>(tempUrl,body).subscribe(result=> subscription=result.toString());
    return true;
  }
  uploadProfilePic(usn:String,picture){
    let url="https://localhost:8443/demo/user/uploadProfilePic";
    this.http.put(url,picture).subscribe(
      res=>{
        console.log(res)
      //  this.usr.pic=event.target.files[0];
     //   this.usr.pic=res;
            }
    )
  }
  changePassword(usr :User,identifier:String,newPassword :String):Boolean{
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      })
    };
    if(usr.password != identifier)
      return false; //WRONG PASSWORD
    let tempUrl="https://localhost:8443/demo/EditUserData/Password";
    let subscription="";
    let body={
      "username":usr.userName,
      "newPassword":newPassword
    }
    this.http.put<String>(tempUrl,body,httpOptions).subscribe(result => subscription=result.toString());
    return true;
    
  }
  //////////////////////////////////////////
  changeEmail(usr:User, newEmail:String):Boolean
  {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
      })
    };
    let tempUrl="https://localhost:8443/demo/EditUserData/Email";
    let subscription="";
    let body={
      "username":usr.userName,
      "newEmail":newEmail
    }
    this.http.put<String>(tempUrl,body,httpOptions).subscribe(result => subscription=result.toString());
    return true;

  }
  loggedIn(){
    if(!this.isAuthenticated())
      return false;
    return true;
  }
  
  getToken(){
    return localStorage.getItem('token');
  }
  logout(){
    localStorage.removeItem("token");
  }
  messageUsr(sender:String,receiver:String,date:String,text:String){
    let url="https://localhost:8443/accesories/messages/newMessage"
    let body={
      "receiver":receiver,
      "sender":sender,
      "date":date,
      "text":text
    };
    let subscription="";
    this.http.post<String>(url,body).subscribe(
      data=>subscription=data.toString()
    );
  }
  getMessages(usn:String):Observable<message[]>{
    let url="https://localhost:8443/accesories/messages/getAllByUsr/?receiver="+usn;
    console.log("fuck");
    return this.http.get<message[]>(url);
      
  }
  getMessagesBySenderAndReceiver(sender:String,receiver:String):Observable<message[]>
  {
    let url="https://localhost:8443/accesories/messages/duplex?receiver="+sender+"&sender="+receiver;
    console.log(url);
    return this.http.get<message[]>(url);
    // return null;
  }
  changeNumber(usn:String,phoneNumber:String){
    let body={
      "username":usn,
      "phone":phoneNumber
    };
    let url="https://localhost:8443/demo/EditUserData/Phone";
    this.http.put<String>(url,body).subscribe(
      data=>{
        console.log(data);
      }
    );
  }
  loginUser(usn:String,password:String){
    let loginUrl="https://localhost:8443/login";
    let ln={
      "username":usn,
      "password":password
    }
    console.log("the fuck's going on??");
    return this.http.post<string>(loginUrl, ln, { observe: 'response'});
  }
}

