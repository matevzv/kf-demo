import { Component } from '@angular/core';
import { HelloRestService } from './rest.service';
import { user } from './user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'kf';
  userName: string = "";
  user: user = {msg: ""};

  constructor(private restService: HelloRestService) { }
 
  public getHello() {
    this.restService.getHello(this.userName)
      .subscribe(
        (response) => {                           //next() callback
          console.log('response received')
          this.user = response;
        },
        (error) => {                              //error() callback
          console.error('Request failed with error')
        },
        () => {                                   //complete() callback
          console.error('Request completed')      //This is actually not needed 
        })
  }
}
