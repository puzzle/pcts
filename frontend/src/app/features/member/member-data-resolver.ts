import { ResolveFn, Router } from '@angular/router';
import { MemberModel } from './member.model';
import { MemberService } from './member.service';
import { inject } from '@angular/core';
import { Observable, of, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';

export const memberDataResolver: ResolveFn<MemberModel> = (route): Observable<MemberModel> => {
  // Extract the route `id` and strip any query parameters if present
  const id: string | undefined = route.paramMap.get('id')
    ?.split('?')[0];
  const router = inject(Router);
  const memberService = inject(MemberService);
  if (!id) {
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
