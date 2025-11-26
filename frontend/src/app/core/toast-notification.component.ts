import { Component, inject } from '@angular/core';
import { MatSnackBar, MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';

export interface ToastItem {
  message: string;
  routerLinkUrl?: string;
  routerLinkText?: string;
  icon?: string;
  closed?: boolean;
}

@Component({
  selector: 'app-toast-notification',
  templateUrl: './toast-notification.component.html',
  styleUrls: ['./toast-notification.component.scss'],
  imports: [MatIcon,
    MatIconButton]
})
export class ToastNotificationComponent {
  count = 0;

  private readonly snackBar = inject(MatSnackBar);

  public readonly data: ToastItem[] = inject(MAT_SNACK_BAR_DATA);

  close(item: ToastItem) {
    item.closed = true;
    this.count--;

    if (this.count === 0) {
      this.snackBar.dismiss();
    }
  }
}
