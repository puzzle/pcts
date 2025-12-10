import { Component, computed, effect, inject, input, InputSignal, signal } from '@angular/core';
import { MatDialogActions, MatDialogClose, MatDialogContent, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatDivider } from '@angular/material/divider';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import {
  OrganisationUnitFormComponent
} from '../../features/organisation-unit/organisation-unit-form/organisation-unit-form.component'

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

export const defaultSize = {
  width: '33vw',
  minWidth: '400px',
  height: 'fit-content'
};
