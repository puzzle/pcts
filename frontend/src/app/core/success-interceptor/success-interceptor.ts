import { HttpInterceptorFn } from '@angular/common/http';

export const successInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req);
};
