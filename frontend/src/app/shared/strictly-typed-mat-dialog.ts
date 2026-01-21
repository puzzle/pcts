import { inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { filter, Observable, ReplaySubject } from 'rxjs';

export abstract class StrictlyTypedMatDialog<DialogData, DialogResult> {
  protected data: DialogData = inject(MAT_DIALOG_DATA);

  protected dialogRef: MatDialogRef<StrictlyTypedMatDialog<DialogData, DialogResult>, DialogResult> =
    inject(MatDialogRef);
}

export interface TypedMatDialogRef<T, R = any> extends MatDialogRef<T, R> {
  afterSubmitted: Observable<R>;
}

export function enrichMatDialogRef<D, R>(ref: MatDialogRef<D, R>) {
  const afterSubmitted$ = new ReplaySubject<R>(1);

  ref.afterClosed()
    .pipe(filter((e): e is R => Boolean(e)))
    .subscribe((e) => afterSubmitted$.next(e));

  (ref as any).afterSubmitted = afterSubmitted$.asObservable();

  return ref as TypedMatDialogRef<D, R>;
}
