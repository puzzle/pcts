import { inject, Provider } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { filter, Observable, ReplaySubject } from 'rxjs';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { FormGroup } from '@angular/forms';
import { I18N_PREFIX } from '../i18n-prefix.token';
import { PCTSDialogConfig, PctsModalService } from './pcts-modal.service';
import { ScopedTranslationService } from '../i18n-prefix.provider';

export abstract class StrictlyTypedDialog<DialogData, DialogResult> {
  protected data: DialogData = inject(MAT_DIALOG_DATA);

  protected abstract formGroup: FormGroup;

  dialogRef: MatDialogRef<StrictlyTypedDialog<DialogData, DialogResult>, DialogResult> =
    inject(MatDialogRef);
}

export interface TypedMatDialogRef<T, R = any> extends MatDialogRef<T, R> {
  afterSubmitted: Observable<R>;
}

export function enrichMatDialogRef<D, R>(ref: MatDialogRef<D, R>) {
  const afterSubmitted$ = new ReplaySubject<R>(1);

  ref.afterClosed()
    .pipe(filter((e): e is R => e !== undefined))
    .subscribe({
      next: (e) => afterSubmitted$.next(e),
      complete: () => afterSubmitted$.complete(), // Important for cleanup
      error: (err) => afterSubmitted$.error(err)
    });

  (ref as any).afterSubmitted = afterSubmitted$.asObservable();

  return ref as TypedMatDialogRef<D, R>;
}


export type WithNullable<T, K extends keyof any> = Omit<T, K> & {
  [P in (K & keyof T)]: T[P] | null;
};

export interface DialogResult<D> {
  modalSubmitMode: ModalSubmitMode;
  submittedModel: D;
}

export function provideModalI18nPrefix<T extends { id: number }>(addSuffix: string, editSuffix: string): Provider[] {
  return [{
    provide: I18N_PREFIX,
    useFactory: () => {
      const dialogData = inject<PCTSDialogConfig<T>>(MAT_DIALOG_DATA);
      const parentPrefix = inject(I18N_PREFIX, { optional: true,
        skipSelf: true });

      return getPrefix<T>(
        dialogData, parentPrefix, addSuffix, editSuffix
      );
    }
  },
  ScopedTranslationService,
  PctsModalService];
}

function getPrefix<T extends { id: number }>(
  dialogData: PCTSDialogConfig<T>, parentPrefix: string | null, addSuffix: string, editSuffix: string
) {
  const isEditMode = !!dialogData.model?.id;
  console.log(dialogData.model);
  const suffix = isEditMode ? editSuffix : addSuffix;

  return parentPrefix ? `${parentPrefix}.${suffix}` : suffix;
}
