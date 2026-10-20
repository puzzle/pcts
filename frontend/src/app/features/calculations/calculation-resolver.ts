import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { CalculationService } from './calculation.service';
import { CalculationModel } from './calculation.model';

export const calculationResolver: ResolveFn<CalculationModel> = (route, state) => {
  const calculationService = inject(CalculationService);

  const id: string | null = route.paramMap.get('calculationId');

  if (!id) {
    throw new Error('calculation id is missing in the route');
  }

  return calculationService.getCalculationById(Number(id));
};
