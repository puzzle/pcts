import { ResolveFn } from '@angular/router';

export const memberIdResolver: ResolveFn<number> = (route): number => {
  const id: string | null = route.paramMap.get('id');

  return id ? Number(id) : 0;
};
