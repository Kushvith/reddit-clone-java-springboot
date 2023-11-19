import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  model:any={};
  err:boolean;
  registerSuccessMessage: string;
  constructor(private accountService:AuthService, private activatedRoute: ActivatedRoute,
    private router: Router, private toastr: ToastrService){}
  ngOnInit() {
    this.activatedRoute.queryParams
    .subscribe(params => {
      if (params['registered'] !== undefined && params['registered'] === 'true') {
        this.toastr.success('Signup Successful');
        this.registerSuccessMessage = 'Please Check your inbox for activation email '
          + 'activate your account before you Login!';
      }
    });
  }
  login(){
    this.accountService.login(this.model).subscribe({
      next:(data)=>{
        this.err = false;
        this.router.navigateByUrl('/');
        this.toastr.success('Login Successful');
      },
      error:()=>{
        this.err = true
      }
    })
  }
}
