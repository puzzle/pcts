import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackbarComponent } from './snackbar.component';

@Injectable({ providedIn: 'root' })
export class SnackbarService {
  private readonly snackBar = inject(MatSnackBar);

  showToasts(messages: string[], type: 'success' | 'error') {
    this.snackBar.openFromComponent(SnackbarComponent, {
      data: messages,
      panelClass: type,
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
      duration: 5000
    });
  }
}
