export function getI18nTitle(prefix: string): string {
  return `${prefix}.TITLE`;
}

export function getI18nAddOrEditTitle(isEdit: boolean): string {
  return isEdit ? 'GENERAL.EDIT_TITLE' : 'GENERAL.ADD_TITLE';
}

export function getI18nAddOrEditButton(isEdit: boolean): string {
  return isEdit ? 'GENERAL.SAVE' : 'GENERAL.ADD';
}

