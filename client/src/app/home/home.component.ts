import { Component } from '@angular/core';
import { PostService } from '../service/post.service';
import { postModel } from '../models/postModel';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  posts:postModel[]=[];
constructor(private postService:PostService){
  this.postService.getAllPosts().subscribe({
    next : (post: postModel[]) => {
      this.posts = post;
    },
    error:(err)=>{
      console.log(err)
    }
  })
}
}


