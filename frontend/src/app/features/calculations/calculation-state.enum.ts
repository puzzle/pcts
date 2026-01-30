export enum CalculationState {
  DRAFT = 'Entwurf',
  ACTIVE = 'Aktiv',
  ARCHIVED = 'ARCHIVED'
}

export const calculationStateSortingPriority: Record<string, number> = {
  DRAFT: 1,
  ACTIVE: 2,
  ARCHIVED: 3
};
