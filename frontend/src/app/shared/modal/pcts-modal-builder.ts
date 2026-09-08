import { PCTSDialogConfig } from './pcts-modal.service';
import { concatMap, Observable } from 'rxjs';
import { DialogResult, StrictlyTypedDialog } from './strictly-typed-dialog.helper';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DestroyRef, Type } from '@angular/core';

type DialogComponent<T extends { id: number }> = StrictlyTypedDialog<PCTSDialogConfig<T>, DialogResult<T>>;

type openModalType<T extends { id: number }> = (
  component: Type<DialogComponent<T>>,
  options: { data: PCTSDialogConfig<T> }
) => {
  afterSubmitted: Observable<{
    modalSubmitMode: ModalSubmitMode;
    submittedModel: T;
  }>;
};

export class PctsModalBuilder<T extends { id: number }> {
  private component: Type<DialogComponent<T>> | undefined;

  private onSubmitMethod: ((model: T) => Observable<T>) | undefined;

  private onSuccess: (() => void) | undefined;

  private submitOptions: ModalSubmitMode[] | undefined;

  private readonly destroyRef: DestroyRef;

  private readonly openModal: openModalType<T>;

  constructor(destroyRef: DestroyRef, openModal: openModalType<T>) {
    this.destroyRef = destroyRef;
    this.openModal = openModal;
  }

  withComponent(component: Type<DialogComponent<T>>) {
    this.component = component;
    return this;
  }

  withOnSubmitMethod(onSubmitMethod: (model: T) => Observable<T>) {
    this.onSubmitMethod = onSubmitMethod;
    return this;
  }

  withOnSuccessMethod(onSuccess: () => void) {
    this.onSuccess = onSuccess;
    return this;
  }

  withSubmitOptionsForEdit() {
    this.submitOptions = [];
    return this;
  }

  withSubmitOptionsForAdd() {
    this.submitOptions = [ModalSubmitMode.COPY,
      ModalSubmitMode.ENTER_ANOTHER];
    return this;
  }

  withSubmitOptions(submitOptions: ModalSubmitMode[]) {
    this.submitOptions = submitOptions;
    return this;
  }

  build() {
    if (!this.component || !this.onSubmitMethod) {
      throw new Error('Component and onSubmitMethod must be provided');
    } else {
      const component = this.component;
      const onSubmitMethod = this.onSubmitMethod;

      const opener = (model?: T) => {
        const config: PCTSDialogConfig<T> = {
          model: model,
          submitOptions: this.submitOptions ?? []
        };
        this.openModal(component, { data: config })
          .afterSubmitted
          .pipe(takeUntilDestroyed(this.destroyRef), concatMap(({ modalSubmitMode, submittedModel }: { modalSubmitMode: ModalSubmitMode;
            submittedModel: T; }) => {
            switch (modalSubmitMode) {
              case ModalSubmitMode.SAVE:
                break;
              case ModalSubmitMode.ENTER_ANOTHER:
                opener();
                break;
              case ModalSubmitMode.COPY:
                opener(submittedModel);
                break;
              default:
                modalSubmitMode satisfies never;
            }

            return onSubmitMethod(submittedModel);
          }))
          .subscribe(() => {
            this.onSuccess?.();
          });
      };
      return opener;
    }
  }
}

