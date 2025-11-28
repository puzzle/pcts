import { Component, inject, ViewEncapsulation } from '@angular/core';
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
  selector: 'app-snackbar',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.scss'],
  imports: [MatIcon,
    MatIconButton]
})
export class SnackbarComponent {
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
