import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BillService {

  url = environment.apiUrl;

  constructor(private httpClient: HttpClient) { }

  generateReport(data: any) {
    return this.httpClient.post(this.url + "/bills", data, {
      headers: new HttpHeaders().set("Content-Type", "application/json")
    })
  }

  getPdf(id: any) {
    return this.httpClient.get(this.url + "/bills/" + id + "/pdf");
  }

  getBills() {
    return this.httpClient.get(this.url + "/bills")
  }
}
