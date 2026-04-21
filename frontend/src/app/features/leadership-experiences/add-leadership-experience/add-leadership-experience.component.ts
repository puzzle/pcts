import { Component, computed, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { BaseFormComponent } from '../../../shared/form/base-form.component';
import { BaseModalComponent } from '../../../shared/modal/base-modal.component';
import { InputFieldComponent } from '../../../shared/input-field/input-field.component';
import { MatAutocomplete, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { MenuButtonComponent } from '../../../shared/menu-button/menu-button.component';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ScopedTranslationPipe } from '../../../shared/pipes/scoped-translation-pipe';
import { ModalSubmitMode } from '../../../shared/enum/modal-submit-mode.enum';
import { MemberModel } from '../../member/member.model';
import { isValueInListSignal } from '../../../shared/form/form-validators';
import { toSignal } from '@angular/core/rxjs-interop';
import { DialogResult, StrictlyTypedDialog } from '../../../shared/modal/strictly-typed-dialog.helper';
import { LeadershipExperienceModel } from '../leadership-experience.model';
import { LeadershipExperienceTypeService } from '../leadership-experiences-type/leadership-experience-type.service';
import { LeadershipExperienceTypeModel } from '../leadership-experiences-type/leadership-experience-type.model';
import { provideI18nPrefix } from '../../../shared/i18n-prefix.provider';

@Component({
  selector: 'app-add-leadership-experience.component',
  imports: [
    BaseFormComponent,
    BaseModalComponent,
    InputFieldComponent,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatButton,
    MatInput,
    MatOption,
    MenuButtonComponent,
    ReactiveFormsModule,
    ScopedTranslationPipe
  ],
  templateUrl: './add-leadership-experience.component.html',
  providers: [provideI18nPrefix('LEADERSHIP_EXPERIENCE.FORM.ADD')]
})
export class AddLeadershipExperienceComponent extends StrictlyTypedDialog<LeadershipExperienceModel | undefined, DialogResult<LeadershipExperienceModel>> implements OnInit {
  private readonly fb = inject(FormBuilder);

  protected readonly ModalSubmitMode = ModalSubmitMode;

  private readonly leadershipExperienceTypeOptions: WritableSignal<LeadershipExperienceTypeModel[]> = signal([]);

  private readonly leadershipExperienceTypeService = inject(LeadershipExperienceTypeService);

  formGroup = this.fb.nonNullable.group({
    id: [null as null | number],
    member: [null as MemberModel | null],
    leadershipExperienceType: [null as LeadershipExperienceTypeModel | null,
      [Validators.required,
        isValueInListSignal(this.leadershipExperienceTypeOptions)]],
    comment: ['' as string | null]
  });

  constructor() {
    super();
    if (this.data) {
      this.formGroup.patchValue({
        ...this.data
      });
    }
  }

  ngOnInit(): void {
    this.leadershipExperienceTypeService.getAllLeadershipExperienceTypes()
      .subscribe((organisationUnits) => {
        this.leadershipExperienceTypeOptions.set(organisationUnits);
        this.formGroup.get('leadershipExperienceType')
          ?.updateValueAndValidity();
      });
  }

  onCancel() {
    this.dialogRef.close();
  }

  protected displayLeadershipExperienceTypes = (leadershipExperienceType: LeadershipExperienceTypeModel): string => {
    return leadershipExperienceType?.name ?? '';
  };

  protected leadershipExperienceTypeControlSignal = toSignal(this.formGroup.get('leadershipExperienceType')!.valueChanges, { initialValue: this.formGroup.get('leadershipExperienceType')!.value });

  protected leadershipExperienceTypeFilteredOptions = computed(() => {
    const value = this.leadershipExperienceTypeControlSignal() ?? '';
    return this.filterLeadershipExperienceType(value);
  });

  filterLeadershipExperienceType(value: LeadershipExperienceTypeModel | string | null): LeadershipExperienceTypeModel[] {
    if (value === null || value === undefined || value === '') {
      return this.leadershipExperienceTypeOptions();
    }

    const filterValue = (typeof value === 'string' ? value : value.name).toLowerCase();

    if (filterValue === '') {
      return this.leadershipExperienceTypeOptions();
    }
    return this.leadershipExperienceTypeOptions()
      .filter((option) => option.name.toLowerCase()
        .includes(filterValue));
  }

  onSubmit(submitMod: ModalSubmitMode) {
    this.dialogRef.close({
      modalSubmitMode: submitMod,
      submittedModel: this.formGroup.getRawValue() as LeadershipExperienceModel
    });
  }
}
