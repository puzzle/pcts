import { inject, Injectable, Injector } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ComponentType } from '@angular/cdk/portal';
import { defaultSize } from './modal/base-modal.component';

@Injectable()
export class ModalService {
  matDialog = inject(MatDialog);

  injector = inject(Injector);

  openModal<T>(component: ComponentType<T>, options: MatDialogConfig = {}) {
    return this.matDialog.open(component, { ...defaultSize,
      ...options });
  }

  openModalAtCurrentHierarchy<T>(component: ComponentType<T>, options: MatDialogConfig = {}) {
    return this.matDialog.open(component, { ...defaultSize,
      injector: this.injector,
      ...options });
  }
}
