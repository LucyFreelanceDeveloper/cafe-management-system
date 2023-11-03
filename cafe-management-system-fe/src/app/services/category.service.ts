import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private readonly categoriesUrl: string = environment.apiUrl + "/categories";
  private readonly defaultHeaders: HttpHeaders = new HttpHeaders().set("Content-Type", "application/json");

  constructor(private httpClient: HttpClient) {

  }

  add(data: any) {
    return this.httpClient.post(this.categoriesUrl, data, {
      headers: this.defaultHeaders
    })
  }

  update(data: any) {
    return this.httpClient.put(this.categoriesUrl, data, {
      headers: this.defaultHeaders
    })
  }

  get() {
    return this.httpClient.get(this.categoriesUrl);
  }

  delete(id: any) {
    return this.httpClient.delete(this.categoriesUrl + "/" + id, {
      headers: this.defaultHeaders
    });
  }

  getFiltered() {
    return this.httpClient.get(this.categoriesUrl + "?filterValue=true")
  }
}