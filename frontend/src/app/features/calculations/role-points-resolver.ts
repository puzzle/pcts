import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { MemberService } from '../member/member.service';
import { Observable } from 'rxjs';
import { RolePointsModel } from '../member/detail-view/RolePointsModel';

export const rolePointsResolver: ResolveFn<RolePointsModel[]> = (route): Observable<RolePointsModel[]> => {
  const memberService = inject(MemberService);

  const id: string | undefined = route.paramMap.get('id')
    ?.split('?')[0];

  const parsedId = id ? Number(id) : 0;

  return memberService.getPointsForActiveCalculationsForRoleByMemberId(parsedId);
};
