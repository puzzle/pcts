import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { MemberService } from './member.service';
import { Observable } from 'rxjs';
import { MemberCvOverviewModel } from './member-cv-overview.model';

export const memberOverviewResolver: ResolveFn<MemberCvOverviewModel> = (route, state): Observable<MemberCvOverviewModel> => {
  const memberService = inject(MemberService);

  const id: string | undefined = route.paramMap.get('id')
    ?.split('?')[0];

  const parsedId = id ? Number(id) : 0;

  return memberService.getMemberOverviewByMemberId(parsedId);
};
