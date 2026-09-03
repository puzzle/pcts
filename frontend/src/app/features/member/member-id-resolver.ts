import { ResolveFn } from '@angular/router';

export const memberIdResolver: ResolveFn<number> = (route): number => {
  const id: string | undefined = route.paramMap.get('id')
    ?.split('?')[0];

  return id ? Number(id) : 0;
};
