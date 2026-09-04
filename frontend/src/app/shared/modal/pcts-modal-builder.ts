import { PCTSDialogConfig, PctsModalService } from './pcts-modal.service';
import { Observable } from 'rxjs';
import { Type } from '@angular/core';
import { DialogResult, StrictlyTypedDialog } from './strictly-typed-dialog.helper';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';

class PctsModalBuilder<T extends { id: number }> {
  private modalService: PctsModalService;

  private component: Type<StrictlyTypedDialog<PCTSDialogConfig<T>, DialogResult<T>>> | undefined;

  private onSubmitMethod: ((model: T) => Observable<T>) | undefined;

  private onSuccess: (() => void) | undefined;

  private submitOptions: ModalSubmitMode[] | undefined;

  constructor() {
    this.reset();
  }

  public reset() {
    this.modalService = new PctsModalService();
  }

  withComponent(component: Type<StrictlyTypedDialog<PCTSDialogConfig<T>, DialogResult<T>>>) {
    this.component = component;
  }

  withOnSubmitMethod(onSubmitMethod: (model: T) => Observable<T>) {
    this.onSubmitMethod = onSubmitMethod;
  }

  withOnSuccessMethod(onSuccess: () => void) {
    this.onSuccess = onSuccess;
  }

  withSubmitOptions(submitOptions: ModalSubmitMode[]) {
    this.submitOptions = submitOptions;
  }

  build() {
    this.modalService.createDialogOpener()
  }
}

class PctsModalBuilderDirector<T extends { id: number }> {
  private builder: PctsModalBuilder<T> | undefined;

  public setBuilder(builder: PctsModalBuilder<T>) {
    this.builder = builder;
  }
}
