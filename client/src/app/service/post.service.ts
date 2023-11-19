import { Injectable } from '@angular/core';
import { postModel } from '../models/postModel';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http:HttpClient) { }
  getAllPosts(): Observable<Array<postModel>> {
    return this.http.get<Array<postModel>>("http://localhost:8080/api/posts");
  }
}
