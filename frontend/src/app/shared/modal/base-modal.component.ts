import { Component, input, output } from '@angular/core';
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle } from '@angular/material/dialog';
import { MatDivider } from '@angular/material/divider';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';


import { ScopedTranslationPipe } from '../pipes/scoped-translation-pipe';
import { BaseFormActionsComponent } from '../base-form-actions/base-form-actions.component';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-base-modal',
  imports: [
    MatDialogTitle,
    MatDivider,
    MatDialogContent,
    MatDialogActions,
    MatIcon,
    MatDialogClose,
    MatIconButton,
    ScopedTranslationPipe,
    BaseFormActionsComponent
  ],
  templateUrl: './base-modal.component.html',
  styleUrl: './base-modal.component.scss'
})
export class BaseModalComponent {
  formGroup = input.required<FormGroup>();

  canceled = output();
}

export const defaultSize = {
  width: '33vw',
  minWidth: '400px',
  height: 'fit-content'
};
