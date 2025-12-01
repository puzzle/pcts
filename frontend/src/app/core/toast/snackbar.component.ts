import { Component, inject, signal, ViewEncapsulation, WritableSignal } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';
import { MatIcon } from '@angular/material/icon';
import { MatIconButton } from '@angular/material/button';

@Component({
  selector: 'app-snackbar',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.scss'],
  imports: [MatIcon,
    MatIconButton]
})
export class SnackbarComponent {
  private readonly snackBarRef = inject(MatSnackBarRef);

  private readonly initialData: string[] = inject<string[]>(MAT_SNACK_BAR_DATA);

  public messages: WritableSignal<string[]> = signal(this.initialData);

  close(indexToRemove: number) {
    this.messages.update((currentMessages: string[]) => {
      return currentMessages.filter((_: string, index: number) => index !== indexToRemove);
    });

    if (this.messages().length === 0) {
      this.snackBarRef.dismiss();
    }
  }
}
