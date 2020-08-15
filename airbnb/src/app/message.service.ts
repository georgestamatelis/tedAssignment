import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { message } from './models/message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor(private http: HttpClient) { }

  deleteMessage(id:Number){
      let url="https://localhost:8443/accesories/messages/"+id;
      this.http.delete(url).subscribe(
        data=>console.log(data)
      );
  }
  markMessage(id:Number){
    let url="https://localhost:8443/accesories/messages/"+id;
    this.http.put(url,{}).subscribe(
      res=>{
        console.log(res);
      }
    )
  }
  sendMessage(receiver:String,sender:String,text:String,date:String,id:Number){
    let url="https://localhost:8443/accesories/messages";
    let body={
      "receiver":receiver,
      "sender":sender,
      "text":text,
      "date":date,
      "appId":id
    }
    this.http.post(url,body).subscribe(
      res=>{
        console.log(res);
      }
    )
  }
  getMessages(appId):Observable<message[]>{
    let url="https://localhost:8443/accesories/Apartment/"+appId+"/messages";///ByAppartment?appId="+appId;
    return this.http.get<message[]>(url);
  }
}
