import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-anonymous',
  templateUrl: './anonymous.component.html',
  styleUrls: ['./anonymous.component.css']
})
export class AnonymousComponent implements OnInit {

  location:String="";
  people:Number;
  constructor() { }

  ngOnInit(): void {
  }

}
