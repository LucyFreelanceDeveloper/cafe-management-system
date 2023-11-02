import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly userUrl: string = environment.apiUrl + "/user"
  private readonly defaultHeaders = new HttpHeaders().set("Content-Type", "application/json");

  constructor(private httpClient: HttpClient) { }

  signup(data: any) {
    return this.httpClient.post(this.userUrl + "/signup", data, {
      headers: this.defaultHeaders
    })
  }

  forgotPassword(data: any) {
    return this.httpClient.post(this.userUrl + "/forgotPassword", data, {
      headers: this.defaultHeaders
    })
  }

  login(data: any) {
    return this.httpClient.post(this.userUrl + "/login", data, {
      headers: this.defaultHeaders
    })
  }

  checkToken() {
    return this.httpClient.get(this.userUrl + "/checkToken");
  }

  changePassword(data: any) {
    return this.httpClient.post(this.userUrl + "/changePassword", data, {
      headers: this.defaultHeaders
    });
  }

  getAll() {
    return this.httpClient.get(this.userUrl);
  }

  update(data: any) {
    return this.httpClient.post(this.userUrl, data, {
      headers: this.defaultHeaders
    });
  }
}
