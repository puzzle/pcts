import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { CalculationService } from './calculation.service';
import { CalculationModel } from './calculationDto';

export const calculationTemplateResolver: ResolveFn<CalculationModel> = (route, state) => {
  const calculationService = inject(CalculationService);

  const memberId: string | null = route.paramMap.get('id');
  const roleId: string | null = route.paramMap.get('roleId');

  if (!memberId) {
    throw new Error('member id is missing in the route');
  }

  if (!roleId) {
    throw new Error('role id is missing in the route');
  }

  return calculationService.getTemplate(Number(memberId), Number(roleId));
};
