import { inject, Injectable } from '@angular/core';
import { MemberModel } from './member.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MemberService {
  private readonly httpClient = inject(HttpClient);

  private readonly API_URL = '/api/v1/members';


  getAllMembers(): Observable<MemberModel[]> {
    return this.httpClient.get<MemberModel[]>(this.API_URL);
  }
}
