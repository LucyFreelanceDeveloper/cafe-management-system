import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  url = environment.apiUrl;

  constructor(private httpClient: HttpClient) { }

  add(data: any) {
    return this.httpClient.post(this.url + "/products", data, {
      headers: new HttpHeaders().set("Content-Type", "application/json")
    })
  }

  update(data: any) {
    return this.httpClient.put(this.url + "/products", data, {
      headers: new HttpHeaders().set("Content-Type", "application/json")
    })
  }

  getProducts() {
    return this.httpClient.get(this.url + "/products");
  }

  delete(id: any) {
    return this.httpClient.delete(this.url + "/products/" + id, {
      headers: new HttpHeaders().set("Conent-Type", "application/json")
    });
  }
}
