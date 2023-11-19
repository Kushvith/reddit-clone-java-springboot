import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../service/auth.service';
import { LoginResponse } from '../models/loginresponse';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  isTokenRefreshing = false;
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);
  constructor(public authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (this.authService.getJwtToken()) {
     request =  this.addToken(request, this.authService.getJwtToken());
    }
    return next.handle(request).pipe(catchError(error => {
      console.log(error.status)
      if (error instanceof HttpErrorResponse
          && error.status === 403) {
          return this.handleAuthErrors(request, next);
      } else {
          return throwError(()=>error);
      }
  }));
  }
  private handleAuthErrors(req:HttpRequest<any>,next:HttpHandler){
   
    if(!this.isTokenRefreshing){
     
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next(null)
      console.log(1)
      return this.authService.refreshToken().pipe(
        switchMap((refreshToken:LoginResponse)=>{
            console.log(2)
            this.isTokenRefreshing = false;
            this.refreshTokenSubject.next(refreshToken.authenticationToken);
            return next.handle(this.addToken(req, refreshToken.authenticationToken));
          }
         
        )
      )
    }
    return throwError(()=>"something went wrong");
  }
  private addToken(req: HttpRequest<any>, jwtToken: string) {
    return req.clone({
        headers: req.headers.set('Authorization',
            'Bearer ' + jwtToken)
    });
}
}
