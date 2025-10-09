import { ResolveFn } from '@angular/router';
import { MemberModel } from './member.model';
import { MemberService } from './member.service';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';

export const idResolver: ResolveFn<MemberModel> = (route,
  state): Observable<MemberModel> | MemberModel => {
  const id: string | null = route.paramMap.get('id');

  const memberService = inject(MemberService);

  if (id === null) {
    return {} as MemberModel;
  } else {
    return memberService.getMemberById(+id);
  }
};

