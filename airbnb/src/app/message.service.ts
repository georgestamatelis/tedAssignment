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
      let url="https://localhost:8443/accesories/messages/delete?id="+id;
      this.http.delete(url).subscribe(
        data=>console.log(data)
      );
  }
  markMessage(id:Number){
    let url="https://localhost:8443/accesories/messages/markAsAnswered?id="+id;
    this.http.put(url,{}).subscribe(
      res=>{
        console.log(res);
      }
    )
  }
  getMessages(appId):Observable<message[]>{
    let url="https://localhost:8443/accesories/messages/ByAppartment?appId="+appId;
    return this.http.get<message[]>(url);
  }
}
