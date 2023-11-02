import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  private readonly billsUrl:string = environment.apiUrl + "/bills";
  private readonly defaultHeaders:HttpHeaders = new HttpHeaders().set("Content-Type", "application/json");

  constructor(private httpClient: HttpClient) { }

  generateReport(data: any) {
    return this.httpClient.post(this.billsUrl, data, {
      headers: this.defaultHeaders
    })
  }

  getPdf(id: number):Observable<Blob> {
    return this.httpClient.get(this.billsUrl + "/" + id + "/pdf", {responseType:'blob'});
  }

  getAll() {
    return this.httpClient.get(this.billsUrl);
  }

  delete(id:number){
    return this.httpClient.delete(this.billsUrl + "/" + id, {
      headers: this.defaultHeaders
    })
  }
}