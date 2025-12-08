import { Component, input, InputSignal } from '@angular/core';
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle } from '@angular/material/dialog';
import { MatDivider } from '@angular/material/divider';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'app-base-modal',
  imports: [
    MatDialogTitle,
    MatDivider,
    MatDialogContent,
    MatDialogActions,
    MatIcon,
    MatDialogClose,
    MatIconButton
  ],
  templateUrl: './base-modal.component.html',
  styleUrl: './base-modal.component.scss'
})
export class BaseModalComponent {
  title: InputSignal<string | undefined> = input<string>();
}
