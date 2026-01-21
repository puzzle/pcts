import { inject, Injectable, Injector, Type } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { defaultSize } from './modal/base-modal.component';
import { enrichMatDialogRef, StrictlyTypedMatDialog, TypedMatDialogRef } from './strictly-typed-mat-dialog';

// Extract the Data type from the component
type ExtractData<C> = C extends StrictlyTypedMatDialog<infer D, any> ? D : never;
// Extract the Result type from the component
type ExtractResult<C> = C extends StrictlyTypedMatDialog<any, infer R> ? R : never;

// Helper to enforce/relax data requirements in MatDialogConfig
type WithRequiredData<D> = Omit<MatDialogConfig<D>, 'data'> & { data: D };
type WithOptionalData<D> = Omit<MatDialogConfig<D>, 'data'> & { data?: D };
type WithoutData = Omit<MatDialogConfig<never>, 'data'> & { data?: never };

// The "Smart" Tuple: Decides arguments based on Data type
type ArgsFor<C> =
// Case 1: Data is strictly 'never' or 'void' -> Config is optional, Data forbidden
  ExtractData<C> extends never | void
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

  openModal<C extends StrictlyTypedMatDialog<any, any>>(component: Type<C>,
    config: WithRequiredData<ExtractData<C>>) {
    return this.open(component, config);
  }

  openModalAtCurrentHierarchy<C extends StrictlyTypedMatDialog<any, any>>(component: Type<C>,
    config: WithRequiredData<ExtractData<C>>) {
    return this.open(component, { injector: this.injector,
      ...config });
  }

  private open<C extends StrictlyTypedMatDialog<any, any>>(
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
}
