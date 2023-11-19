import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable, map, tap } from 'rxjs';
import { LoginResponse } from '../models/loginresponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private localstorage: LocalStorageService) { }
  signup(model: FormGroup): Observable<any> {
    return this.http.post("http://localhost:8080/api/auth/signup", model);
  }
  login(model: any): Observable<boolean> {
    return this.http.post<LoginResponse>('http://localhost:8080/api/auth/login', model)
      .pipe(map(data => {
        this.localstorage.store('authenticationToken', data.authenticationToken);
        this.localstorage.store('username', data.username);
        this.localstorage.store('refreshToken', data.refreshToken);
        this.localstorage.store('expiresAt', data.expiresAt);
        return true;
      }))
  }
  refreshToken(){
    const refreshToken = {
      refreshToken:this.getRefreshToken(),
      username:this.getUserName()
    }
    return this.http.post<LoginResponse>("http://localhost:8080/api/auth/refresh/token",refreshToken).pipe(
      tap(response =>{
        console.log(2)
        this.localstorage.store('authenticationToken',response.authenticationToken);
        this.localstorage.store("expiresAt",response.expiresAt)
        return response;
      })
    )
  }
  getJwtToken() {
    return this.localstorage.retrieve('authenticationToken');
  }

  getRefreshToken() {
    return this.localstorage.retrieve('refreshToken');
  }

  getUserName() {
    return this.localstorage.retrieve('username');
  }

  getExpirationTime() {
    return this.localstorage.retrieve('expiresAt');
  }
}
