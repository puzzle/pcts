import { ResolveFn, Router } from '@angular/router';
import { MemberModel } from './member.model';
import { MemberService } from './member.service';
import { inject } from '@angular/core';
import { Observable, of, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const memberDataResolver: ResolveFn<MemberModel> = (route): Observable<MemberModel> => {
  const id: string | null = route.paramMap.get('id');
  const router = inject(Router);
  const memberService = inject(MemberService);

  if (id === null) {
    return of({} as MemberModel);
  }

  if (isNaN(+id)) {
    router.navigate(['/member']);
    return EMPTY;
  }

  return memberService.getMemberById(+id)
    .pipe(catchError((error) => {
      router.navigate(['/member']);

      return EMPTY;
    }));
};
