import { ResolveFn } from '@angular/router';

export const memberIdResolver: ResolveFn<number> = (route): number => {
  const id: string | null = route.paramMap.get('id');

  if (!id) {
    throw new Error('Member id is missing in the route');
  }

  return Number(id);
};
