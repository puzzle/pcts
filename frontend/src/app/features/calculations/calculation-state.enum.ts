export enum CalculationState {
  DRAFT = 'DRAFT',
  ACTIVE = 'ACTIVE',
  ARCHIVED = 'ARCHIVED'
}

export const calculationStateSortingPriority: Record<string, number> = {
  DRAFT: 1,
  ACTIVE: 2,
  ARCHIVED: 3
};
