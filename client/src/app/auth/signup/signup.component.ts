import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit{

  registerForm: FormGroup;
  constructor(private accountService:AuthService, private router: Router, private toastr: ToastrService){}
  ngOnInit(){
    this.intializeForm()
  }
 
  intializeForm()
  {
    this.registerForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    })
  }
  signup(){
    this.accountService.signup(this.registerForm.value).subscribe({
      next:()=>{
        this.router.navigate(["/login"],{queryParams:{registered:true}})
      },
      error:()=>{
        this.toastr.error("error while registering")
      }
    })
  }
}
