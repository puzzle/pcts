import { FormModalConfig, ModelWithId, PctsModalService, WithRequiredData } from './pcts-modal.service';
import { concatMap, map, Observable } from 'rxjs';
import { DialogResult, StrictlyTypedDialog, TypedMatDialogRef } from './strictly-typed-dialog.helper';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DestroyRef, Injector, Type } from '@angular/core';
import { I18N_PREFIX } from '../i18n-prefix.token';

type ModalComponent<T extends ModelWithId> = StrictlyTypedDialog<FormModalConfig<T>, DialogResult<T>>;

type openModalType<T extends ModelWithId> = (
  component: Type<ModalComponent<T>>,
  options: WithRequiredData<FormModalConfig<T>>
) => TypedMatDialogRef<ModalComponent<T>, DialogResult<T>>;

type onSubmitMethodType<T extends ModelWithId> = (model: T) => Observable<T>;

export class PctsModalBuilder<T extends ModelWithId> {
  private component: Type<ModalComponent<T>> | undefined;

  private onSubmitMethod: ((model: T) => Observable<T>) | undefined;

  private onSuccess: (() => void) | undefined;

  private submitOptions: ModalSubmitMode[] | undefined;

  private i18nPrefix: string | undefined;

  private readonly destroyRef: DestroyRef;

  private readonly injector: Injector;

  private readonly openModal: openModalType<T>;

  constructor(destroyRef: DestroyRef, openModal: openModalType<T>, injector: Injector) {
    this.destroyRef = destroyRef;
    this.openModal = openModal;
    this.injector = injector;
  }

  withComponent(component: Type<ModalComponent<T>>) {
    this.component = component;
    return this;
  }

  withOnSubmitMethod(onSubmitMethod: onSubmitMethodType<T>) {
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

  withI18nPrefix(i18nPrefix: string) {
    this.i18nPrefix = i18nPrefix;
    return this;
  }

  build() {
    if (!this.component || !this.onSubmitMethod) {
      throw new Error('Component and onSubmitMethod must be provided');
    }

    const component = this.component;
    const onSubmitMethod = this.onSubmitMethod;
    const submitOptions = this.submitOptions ?? [];
    const i18nPrefix = this.i18nPrefix ?? '';
    const onSuccess = this.onSuccess;

    return this.createOpenerMethod(
      component, onSubmitMethod, submitOptions, i18nPrefix, onSuccess
    );
  }

  private getInjectorForI18nPrefix(i18nPrefix: string) {
    return Injector.create({ providers: [{ provide: I18N_PREFIX,
      useValue: i18nPrefix },
    PctsModalService],
    parent: this.injector });
  }

  private evaluateSubmitModes(modalSubmitMode: ModalSubmitMode) {
    switch (modalSubmitMode) {
      case ModalSubmitMode.SAVE:
        return { shouldReopen: false,
          withModal: false };
      case ModalSubmitMode.ENTER_ANOTHER:
        return { shouldReopen: true,
          withModal: false };
      case ModalSubmitMode.COPY:
        return { shouldReopen: true,
          withModal: true };
      default:
        modalSubmitMode satisfies never;
        return { shouldReopen: false,
          withModal: false };
    }
  }

  private createOpenerMethod(
    component: Type<ModalComponent<T>>,
    onSubmitMethod: onSubmitMethodType<T>,
    submitOptions: ModalSubmitMode[],
    i18nPrefix: string,
    onSuccess?: () => void
  ) {
    const opener = (model?: T) => {
      const data: FormModalConfig<T> = {
        model: model,
        submitOptions: submitOptions
      };

      const injector = this.getInjectorForI18nPrefix(i18nPrefix);

      this.openModal(component, {
        data: data,
        injector: injector
      })
        .afterSubmitted
        .pipe(takeUntilDestroyed(this.destroyRef), concatMap(({ modalSubmitMode, submittedModel }) => this.onFormSubmit(
          submittedModel, modalSubmitMode, onSubmitMethod, opener.bind(this)
        )))
        .subscribe(() => {
          onSuccess?.();
        });
    };
    return opener;
  }

  private onFormSubmit(
    submittedModel: T,
    modalSubmitMode: ModalSubmitMode,
    onSubmitMethod: onSubmitMethodType<T>,
    opener: (model?: T) => void
  ) {
    return onSubmitMethod(submittedModel)
      .pipe(map(() => {
        const submitMode = this.evaluateSubmitModes(modalSubmitMode);

        if (submitMode.shouldReopen) {
          opener(submitMode.withModal ? submittedModel : undefined);
        }
      }));
  }
}
