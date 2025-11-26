import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToastItem, ToastNotificationComponent } from './toast-notification.component';

@Injectable({ providedIn: 'root' })
export class ToastService {
  private readonly snackBar = inject(MatSnackBar);

  showToasts(items: ToastItem[]) {
    this.snackBar.openFromComponent(ToastNotificationComponent, {
      data: items,
      verticalPosition: 'bottom',
      horizontalPosition: 'right',
      duration: 10000,
      panelClass: ['toast-wrapper']
    });
  }
}
