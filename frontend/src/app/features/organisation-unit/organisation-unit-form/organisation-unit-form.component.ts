import { Component, effect, inject, signal, WritableSignal } from '@angular/core';
import { BaseModalComponent } from '../../../shared/modal/base-modal.component';
import { OrganisationUnitModel } from '../organisation-unit.model';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputFieldComponent } from '../../../shared/input-field/input-field.component';
import { MatInput } from '@angular/material/input';
import { BaseFormComponent } from '../../../shared/form/base-form.component';
import { BaseFormActionsComponent } from '../../../shared/base-form-actions/base-form-actions.component';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-organisation-unit-form',
  imports: [
    BaseModalComponent,
    InputFieldComponent,
    MatInput,
    ReactiveFormsModule,
    BaseFormComponent,
    BaseFormActionsComponent
  ],
  templateUrl: './organisation-unit-form.component.html'
})
export class OrganisationUnitFormComponent {
  private readonly fb = inject(FormBuilder);

  readonly dialog: MatDialogRef<OrganisationUnitFormComponent> = inject(MatDialogRef<OrganisationUnitFormComponent>);

  organisationUnit: WritableSignal<OrganisationUnitModel> = signal({} as OrganisationUnitModel);

  organisationUnitForm: FormGroup = this.fb.group({
    id: [null],
    name: ['',
      Validators.required]
  });

  constructor() {
    effect(() => {
      this.organisationUnitForm.patchValue(this.organisationUnit());
    });
  }

  onSubmit() {
    console.log(this.fb);
  }

  onCancel() {
    this.dialog.close();
  }
}
