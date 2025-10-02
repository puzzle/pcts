import { ResolveFn } from '@angular/router';


export const idResolver: ResolveFn<string> = (route, state) => {
  const id = route.paramMap.get('id');
  if (!id) {
    throw new Error('ID param is missing');
  }
  return id;
};

