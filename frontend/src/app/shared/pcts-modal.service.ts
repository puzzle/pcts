import { inject, Injectable, Injector, Type } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { defaultSize } from './modal/base-modal.component';
import { enrichMatDialogRef, StrictlyTypedMatDialog, TypedMatDialogRef } from './strictly-typed-mat-dialog';

type GetData<T> = T extends StrictlyTypedMatDialog<infer D, any> ? D : never;
type GetResult<T> = T extends StrictlyTypedMatDialog<any, infer R> ? R : never;
type ConfigWithData<D> = Omit<MatDialogConfig, 'data'> & { data: D };

@Injectable()
export class PctsModalService {
  private readonly dialog = inject(MatDialog);

  private readonly injector = inject(Injector);

  openModal<C extends StrictlyTypedMatDialog<any, any>>(component: Type<C>,
    config: ConfigWithData<GetData<C>>) {
    return this.open(component, config);
  }

  openModalAtCurrentHierarchy<C extends StrictlyTypedMatDialog<any, any>>(component: Type<C>,
    config: ConfigWithData<GetData<C>>) {
    return this.open(component, { injector: this.injector,
      ...config });
  }


  private open<C extends StrictlyTypedMatDialog<any, any>>(component: Type<C>,
    config: ConfigWithData<GetData<C>>): TypedMatDialogRef<C, GetResult<C>> {
    const ref = this.dialog.open(component, { ...defaultSize,
      ...config });
    return enrichMatDialogRef(ref);
  }
}
