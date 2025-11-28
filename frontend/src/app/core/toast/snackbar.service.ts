import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToastItem, SnackbarComponent } from './snackbar.component';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly snackBar = inject(MatSnackBar);

  showToasts(items: ToastItem[], type: 'success' | 'error') {
    this.snackBar.openFromComponent(SnackbarComponent, {
      data: items,
      panelClass: type,
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
      duration: 5000
    });
  }
}
