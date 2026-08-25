import { DestroyRef, inject, Injectable, Injector, Type } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { defaultSize } from './base-modal.component';
import { enrichMatDialogRef, StrictlyTypedDialog, TypedMatDialogRef } from './strictly-typed-dialog.helper';
import { MemberModel } from '../../features/member/member.model';
import { concatMap, filter, Observable } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ModalSubmitMode } from '../enum/modal-submit-mode.enum';
import { MemberOverviewModel } from '../../features/member/member-overview.model';

// Extract the Data type from the component
type ExtractData<C> = C extends StrictlyTypedDialog<infer D, any> ? D : never;
// Extract the Result type from the component
type ExtractResult<C> = C extends StrictlyTypedDialog<any, infer R> ? R : never;

// Helper to enforce/relax data requirements in MatDialogConfig
type WithRequiredData<D> = Omit<MatDialogConfig<D>, 'data'> & { data: D };
type WithOptionalData<D> = Omit<MatDialogConfig<D>, 'data'> & { data?: D };
type WithoutData = Omit<MatDialogConfig<never>, 'data'> & { data?: never };

// The "Smart" Tuple: Decides arguments based on Data type
type ArgsFor<C> =
// Case 1: Data is strictly 'never' or 'void' -> Config is optional, Data forbidden
  ExtractData<C> extends void
    ? [config?: WithoutData]
  // Case 2: Data allows undefined/null -> Config is optional, Data optional
    : undefined extends ExtractData<C>
      ? [config?: WithOptionalData<NoInfer<ExtractData<C>>>]
      : null extends ExtractData<C>
        ? [config?: WithOptionalData<NoInfer<ExtractData<C>>>]
      // Case 3: Data is required -> Config is required, Data required
        : [config: WithRequiredData<NoInfer<ExtractData<C>>>];

@Injectable()
export class PctsModalService {
  private readonly dialog = inject(MatDialog);

  private readonly injector = inject(Injector);

  private readonly destroyRef = inject(DestroyRef);

  openModal<C extends StrictlyTypedDialog<any, any>>(component: Type<C>,
    config: WithRequiredData<ExtractData<C>>) {
    return this.open(component, config);
  }

  openModalAtCurrentHierarchy<C extends StrictlyTypedDialog<any, any>>(component: Type<C>,
    config: WithRequiredData<ExtractData<C>>) {
    return this.open(component, { injector: this.injector,
      ...config });
  }

  private open<C extends StrictlyTypedDialog<any, any>>(
    component: Type<C>,
    ...args: ArgsFor<C>
  ): TypedMatDialogRef<C, ExtractResult<C>>;

  // Implementation (Type Erasure)
  private open(component: Type<any>, config?: MatDialogConfig<any>): MatDialogRef<any, any> {
    const finalConfig = {
      ...defaultSize,
      ...config
    };
    const ref = this.dialog.open(component, finalConfig);
    return enrichMatDialogRef(ref);
  }

  public createDialogOpener = <T extends { member?: MemberModel }>(
    component: any,
    addServiceCall: (model: T) => Observable<any>,
    member: MemberOverviewModel | null
  ) => {
    const opener = (model?: T) => {
      this.openModal(component, { data: model })
        .afterSubmitted
        .pipe(takeUntilDestroyed(this.destroyRef), filter(() => !!member?.id), concatMap(({ modalSubmitMode, submittedModel }: { modalSubmitMode: ModalSubmitMode;
          submittedModel: T; }) => {
          submittedModel.member = { id: member!.id } as MemberModel;

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

          return addServiceCall(submittedModel);
        }))
        .subscribe(() => {

        });
    };

    return opener;
  };
}
