import { inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { filter, Observable, ReplaySubject } from 'rxjs';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { FormGroup } from '@angular/forms';
import { tap } from 'rxjs/operators';

export abstract class StrictlyTypedDialog<DialogData, DialogResult> {
  protected data: DialogData = inject(MAT_DIALOG_DATA);

  protected abstract formGroup: FormGroup;

  protected dialogRef: MatDialogRef<StrictlyTypedDialog<DialogData, DialogResult>, DialogResult> =
    inject(MatDialogRef);
}

export interface TypedMatDialogRef<T, R = any> extends MatDialogRef<T, R> {
  afterSubmitted: Observable<R>;
}

export function enrichMatDialogRef<D, R>(ref: MatDialogRef<D, R>) {
  const afterSubmitted$ = new ReplaySubject<R>(1);

  ref.afterClosed()
    .pipe(tap(console.log), filter((e): e is R => e !== undefined), tap(console.log))
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
