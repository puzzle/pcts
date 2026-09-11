import { FormDialogConfig, ModelWithId, PctsModalService } from './pcts-modal.service';
import { concatMap, Observable } from 'rxjs';
import { DialogResult, StrictlyTypedDialog } from './strictly-typed-dialog.helper';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DestroyRef, Injector, Type } from '@angular/core';
import { I18N_PREFIX } from '../i18n-prefix.token';
import { ScopedTranslationService } from '../i18n-prefix.provider';

type DialogComponent<T extends ModelWithId> = StrictlyTypedDialog<FormDialogConfig<T>, DialogResult<T>>;

type openModalType<T extends ModelWithId> = (
  component: Type<DialogComponent<T>>,
  options: { data: FormDialogConfig<T>;
    injector: Injector; }
) => {
  afterSubmitted: Observable<{
    modalSubmitMode: ModalSubmitMode;
    submittedModel: T;
  }>;
};

export class PctsModalBuilder<T extends ModelWithId> {
  private component: Type<DialogComponent<T>> | undefined;

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

    const opener = (model?: T) => {
      const data: FormDialogConfig<T> = {
        model: model,
        submitOptions: this.submitOptions ?? []
      };

      const injector = this.getInjectorForI18nPrefix(this.i18nPrefix ?? '');


      this.openModal(component, { data: data,
        injector: injector })
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

  private getInjectorForI18nPrefix(i18nPrefix: string) {
    return Injector.create({ providers: [{ provide: I18N_PREFIX,
      useValue: i18nPrefix },
    ScopedTranslationService,
    PctsModalService],
    parent: this.injector });
  }
}
