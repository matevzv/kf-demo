import { Injectable } from '@angular/core';
 
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

import { user } from './user';
 
@Injectable()
export class HelloRestService {
 
  baseURL: string = "hello/";
 
  constructor(private http: HttpClient) {
  }
 
  getHello(userName: string): Observable<any> {
    return this.http.get(this.baseURL + userName)
  }
 
}
