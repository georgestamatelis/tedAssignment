import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

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
}
